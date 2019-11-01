package com.sequoiacode.scraper.config;

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
        findDropdownSelect(By.xpath("//select[@id='navFilterSelection']"),"All tasks");
        findElementWithClick(By.xpath("//div/a[@aria-expanded='false']/span[text()='Applications']"));
        findElementWithClick(By.xpath("//div/a[@aria-expanded='false']/span[text()='Application Types']"));
        findElementWithClick(By.xpath("//a[@id='C0WebSphere enterprise applications']"));

        switchFrame("detail");
        return getTableValues();
    }


    public WebElement findElement(By by) {
        return web.findElement(by);
    }

    public List<WebElement> findElements(By by) {
        return web.findElements(by);
    }

    public void findElementWithClick(By by) {
        List<WebElement> element = findElements(by);
        if (element.size() > 0) {
            element.get(0).click();
            element.clear();
        }
    }

    public void switchFrame(String frameName) {
        web.switchTo().parentFrame();
        web.switchTo().frame(frameName);
    }

    public void switchParentFrame() {
        web.switchTo().parentFrame();
    }

    public void findDropdownSelect(By by, String selectValue) {
        Select dropdown = new Select(findElement(by));
        dropdown.selectByVisibleText(selectValue);
    }

    private List<WasApp> getTableValues() {
        String xpath  = "//table[@class='framing-table']/tbody[3]/tr[@class='table-row']";
        List<WebElement> rows = findElements(By.xpath(xpath));
        Set<WasApp> wasApps =  new HashSet<>();
        for (WebElement row : rows) {
            String appName = row.findElement(By.xpath(xpath +"/td[2]")).getText();
            String status = row.findElement(By.xpath(xpath +"/td[3]/a/img")).getAttribute("title");
            if(appName !=null && status !=null) {
                wasApps.add(new WasApp(appName, status));
            }
            System.out.println(appName);
            System.out.println(status);

        }
        return new ArrayList<>(wasApps);
    }

    public String getCookies() {
        return web.manage().getCookies().toString();
    }
}
