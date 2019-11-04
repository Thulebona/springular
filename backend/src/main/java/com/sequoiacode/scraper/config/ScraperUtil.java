package com.sequoiacode.scraper.config;

import com.sequoiacode.scraper.domain.AppSecurity;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.sequoiacode.scraper.domain.WasApp;

import java.io.Serializable;
import java.util.*;

@Component
public class ScraperUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    private WebDriver web = null;
    private String was_location;
    private Map<String, WebDriver> drivers = new HashMap<>();

    @Value("${was.env.url}")
    public void setWasLocation(String url) {
        was_location = url;
    }

    private void getDrive(String userId) {
        if (drivers.get(userId) == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--ignore-certificate-errors","--headless");
            drivers.put(userId, new ChromeDriver(options));
        }
        web = drivers.get(userId);
    }

    private Boolean isAuthOnWas() {
        return findElements(By.tagName("frame")).size() > 0;
    }

    public WebDriver login(UserDetails user) throws BadCredentialsException {
        getDrive(user.getUsername());
        if (!isAuthOnWas()) {
            web.get(was_location);
            findElement(By.name("j_username")).sendKeys(user.getUsername());
            findElement(By.name("j_password")).sendKeys(user.getPassword());

            findElementWithClick(By.xpath("//input[@value='Log in']"));
            findElementWithClick(By.xpath("//input[@value='OK']"));

            By loginError = By.xpath("//div[@class='errorMessage']");
            if (findElements(loginError).size() > 0) {
                throw new BadCredentialsException(findElement(loginError).getText().trim());
            }
        }
        return web;
    }

    public List<WasApp> getAppsInstalled() {
        switchFrame("navigation");
        findDropdownSelect(By.xpath("//select[@id='navFilterSelection']"), "All tasks");
        findElementWithClick(By.xpath("//div/a[@aria-expanded='false']/span[text()='Applications']"));
        findElementWithClick(By.xpath("//div/a[@aria-expanded='false']/span[text()='Application Types']"));
        findElementWithClick(By.xpath("//a[@id='C0WebSphere enterprise applications']"));
        switchFrame("detail");
        return getTableValues();
    }

    private WebElement findElement(By by) {
        return web.findElement(by);
    }

    private List<WebElement> findElements(By by) {
        return web.findElements(by);
    }

    private void findElementWithClick(By by) {
        List<WebElement> element = findElements(by);
        if (element.size() > 0) {
            element.get(0).click();
            element.clear();
        }
    }

    private void switchFrame(String frameName) {
        web.switchTo().parentFrame();
        web.switchTo().frame(frameName);
    }

    private void findDropdownSelect(By by, String selectValue) {
        Select dropdown = new Select(findElement(by));
        dropdown.selectByVisibleText(selectValue);
    }

    private List<WasApp> getTableValues() {
        String xpath = "//table[@class='framing-table']/tbody[3]";
        List<WebElement> rows = findElements(By.xpath(xpath + "/tr[@class='table-row']"));
        Set<WasApp> wasApps = new HashSet<>();
        for (int i = 2; i < rows.size() + 2; i++) {
            String appName = findElement(By.xpath(xpath + "/tr[" + i + "]/td[2]")).getText();
            String status = findElement(By.xpath(xpath + "/tr[" + i + "]/td[3]/a/img")).getAttribute("title");
            if (appName != null && status != null) {
                wasApps.add(new WasApp(appName, status));
            }
        }
        return new ArrayList<>(wasApps);
    }

    public List<AppSecurity> getAppSecurity(String appName) {
        String path = String.format("//table[@class='framing-table']/tbody[3]/tr[@class='table-row']/td[2]/a[contains(text(), '%s')]", appName);
        List<WebElement> app = findElements(By.xpath(path));
        Set<AppSecurity> appSecurities = new HashSet<>();
        if (app.size() > 0) {
            app.get(0).click();
            String securityRolePath = "//*[@id='MapRolesToUsersForm']/table/tbody/tr[1]/td/fieldset/table/tbody";
            String securityPath = "//div[@class='main-category-container']/ul/li[@title='Security role to user/group mapping']";
            findElementWithClick(By.xpath(securityPath));
            final int roleSize = findElements(By.xpath(securityRolePath + "/tr[@class='table-row']")).size();
            for (int i = 2; i < roleSize + 2; i++) {
                String role = findElement(By.xpath(securityRolePath + "/tr[" + i + "]/td[1]")).getText();
                String subject = findElement(By.xpath(securityRolePath + "/tr[" + i + "]/td[2]")).getText();
                String mappedUser = findElement(By.xpath(securityRolePath + "/tr[" + i + "]/td[3]")).getText();
                String group = findElement(By.xpath(securityRolePath + "/tr[" + i + "]/td[4]")).getText();

                appSecurities.add(new AppSecurity(role, subject, mappedUser, group));
            }
        }

        return new ArrayList<>(appSecurities);
    }
}
