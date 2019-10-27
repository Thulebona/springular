package com.sequoiacode.api.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScraperUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    private static WebDriver web = null;
    private static String was_location;
    private static Map<String, WebDriver> drivers = new HashMap<>();

    @Value("${was.env.url}")
    public void setWasLocation(String url) {
        was_location = url;
    }

    private static void getDrive(String userId) {
        if (web == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--ignore-certificate-errors");
            web = new ChromeDriver(options);
        }
        drivers.get(userId);
    }

    private static Boolean isLoggedInOnWAS() {
        return web.findElements(By.xpath("//ul/li/a[@title='Logout']")).size() > 0;
    }

    public static WebDriver login(UserDetails user) {
        getDrive(user.getUsername());
        if (!isLoggedInOnWAS()) {
            web.get(was_location);
            findElement(By.name("j_username")).sendKeys(user.getUsername());
            findElement(By.name("j_password")).sendKeys(user.getPassword());

            findElementWithClick(By.xpath("//input[@value='Log in']"));
            findElementWithClick(By.xpath("//input[@value='OK']"));
        }
        return web;
    }

    public static List getApps() {
        switchFrame("navigation");

        Select dropdown = new Select(findElement(By.xpath("//select[@id='navFilterSelection']")));
        dropdown.selectByVisibleText("All tasks");
        findElementWithClick(By.xpath("//a/span[text()='Applications']"));
        findElementWithClick(By.xpath("//a/span[text()='Application Types']"));
        findElementWithClick(By.xpath("//a[@id='C0WebSphere enterprise applications']"));

        switchParentFrame();
        switchFrame("detail");
        return getTableValues();
    }


    public static WebElement findElement(By by) {
        return web.findElement(by);
    }

    public static List<WebElement> findElements(By by) {
        return web.findElements(by);
    }

    public static void findElementWithClick(By by) {
        List<WebElement> element = findElements(by);
        if (element.size() > 0) {
            element.get(0).click();
        }
    }

    public static void switchFrame(String frameName) {
        web.switchTo().frame(frameName);
    }

    public static void switchParentFrame() {
        web.switchTo().parentFrame();
    }

    public static void findDropdownSelect(By by, String selectValue) {
        Select dropdown = new Select(findElement(by));
        dropdown.selectByVisibleText(selectValue);
    }

    public static List getTableValues() {
        List<WebElement> rows = findElements(By.xpath("//table[@class='framing-table']/tbody[3]/tr[@class='table-row']"));
        for (WebElement row : rows) {
            System.out.println(row.getText());
            WebElement key = row.findElement(By.xpath("/td[2]"));
        }
        return rows;
    }

    public static String getCookies() {
        return web.manage().getCookies().toString();
    }
}
