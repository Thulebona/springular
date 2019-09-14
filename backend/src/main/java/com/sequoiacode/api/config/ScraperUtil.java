package com.sequoiacode.api.config;

import com.sequoiacode.api.domain.Credential;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ScraperUtil {

    private static WebDriver web;

    static {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
        web = new ChromeDriver(options);
    }
    public static void login(Credential credential, WASEnv env) {
        web.get(env.label);
        WebElement userName = findElement(By.name("j_username"));
        WebElement passWord = findElement(By.name("j_password"));

        userName.sendKeys(credential.getUsername());
        passWord.sendKeys(credential.getPassword());
        findElementWithClick(By.xpath("//input[@value='Log in']"));
        findElementWithClick(By.xpath("//input[@value='OK']"));
    }
    public static void getAppAndSecurity(Credential credential, WASEnv env) {
            login(credential,env);
        switchFrame("navigation");

        Select dropdown = new Select(findElement(By.xpath("//select[@id='navFilterSelection']")));
        dropdown.selectByVisibleText("All tasks");
        findElementWithClick(By.xpath("//*[@id='T0']/div[5]/a"));
        findElementWithClick(By.xpath("//*[@id='N3']/div[1]/a"));
        findElementWithClick(By.xpath("//a[@id='C0WebSphere enterprise applications']"));

        switchParentFrame();
        switchFrame("detail");

        findElementWithClick(By.xpath("//*[@id=\"com.ibm.ws.console.appdeployment.ApplicationDeploymentCollectionForm\"]/table[3]/tbody[3]/tr[2]/td[2]/a"));
        findElementWithClick(By.xpath("//*[@id=\"child_ApplicationDeployment.DetailProperties.category\"]/ul/li[6]/a"));
        System.out.println(web.getCurrentUrl());
    }


    public static WebElement findElement(By by) {
        return web.findElement(by);
    }

    public static void findElementWithClick(By by) {
        List<WebElement> element = web.findElements(by);
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
}
