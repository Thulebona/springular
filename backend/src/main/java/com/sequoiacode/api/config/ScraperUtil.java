package com.sequoiacode.api.config;

import com.sequoiacode.api.domain.Credential;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;
import java.util.List;

public class ScraperUtil {

    private static WebDriver web;

    static {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        web = new ChromeDriver(options);
    }

    private static Boolean isLoggedIn(){
        return web.findElements(By.xpath("//ul/li/a[@title='Logout']")).size() > 0;
    }


    public static void login(Credential credential, WASEnv env) {
        web.get(env.label);
        if(!isLoggedIn()) {
            WebElement userName = findElement(By.name("j_username"));
            WebElement passWord = findElement(By.name("j_password"));

            userName.sendKeys(credential.getUsername());
            passWord.sendKeys(credential.getPassword());
            findElementWithClick(By.xpath("//input[@value='Log in']"));
            findElementWithClick(By.xpath("//input[@value='OK']"));
        }
    }
    public static List getApps(Credential credential, WASEnv env) {
        login(credential,env);
        switchFrame("navigation");

        Select dropdown = new Select(findElement(By.xpath("//select[@id='navFilterSelection']")));
        dropdown.selectByVisibleText("All tasks");
        findElementWithClick(By.xpath("//a/span[text()='Applications']"));
        findElementWithClick(By.xpath("//a/span[text()='Application Types']"));
        findElementWithClick(By.xpath("//a[@id='C0WebSphere enterprise applications']"));

        switchParentFrame();
        switchFrame("detail");
        System.out.println(web.getCurrentUrl());
//        findElementWithClick(By.xpath("//*[@id=\"com.ibm.ws.console.appdeployment.ApplicationDeploymentCollectionForm\"]/table[3]/tbody[3]/tr[2]/td[2]/a"));
//        findElementWithClick(By.xpath("//*[@id=\"child_ApplicationDeployment.DetailProperties.category\"]/ul/li[6]/a"));
        return getTableValues();
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


    public static List getTableValues() {
        List<WebElement> rows = web.findElements(By.xpath("//table[@class='framing-table']/tbody[3]/tr[@class='table-row']"));
        for (WebElement row : rows) {
            System.out.println(row.getText());
            WebElement key = row.findElement(By.xpath("/td[2]"));
            System.out.println(key.getText());
            WebElement val = row.findElement(By.xpath("./td[2]"));
            System.out.println(val.getText());
        }
        return rows;
    }
}
