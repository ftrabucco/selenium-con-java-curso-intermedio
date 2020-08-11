package vatesorg.intermedio.intermedio;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class JavaScript {
    private WebDriver driver;
    String expectedResult = null;
    String actualResult = null;
    String baseURL = "http://demo.guru99.com/test/newtours/";
    private JavascriptExecutor js;
    String pageLoadStatus="";

    private boolean highLight(WebElement element){
        js =(JavascriptExecutor)driver;
        for (int i=0;i<3;i++){
            try {
                js.executeScript("arguments[0].setAttribute{'style','background:red}",element);
                Thread.sleep(1000);
                js.executeScript("arguments[0].setAttribute{'style','background:}",element);
            }catch (Exception e){
                System.err.println("JavaScript | method : highlight | Exception desc "+ e.getMessage());
                return false;
            }
        }
        return true;
    }

    private boolean WaitForPageLoad(){
        try {
            do {
                js=(JavascriptExecutor)driver;
                pageLoadStatus=(String) js.executeScript("return document.readyState");
            }while (!pageLoadStatus.equals("complete"));
        }catch (Exception e){
            System.err.println("JavaScript | method : WaitForPageLoad | Exception desc "+ e.getMessage());
            return false;
        }
        return true;
    }

    private boolean scrollWindow(){
        try {
        js=(JavascriptExecutor)driver;
        //scroll up(0,-250)/ down(0,250)
        js.executeScript("window.scrollBy(0,250)");
        }catch (Exception e){
            System.err.println("JavaScript | method : highlight | Exception desc "+ e.getMessage());
            return false;
        }
        return true;
    }

    @BeforeTest
    public void launchBrowser() {
        String chromePath = System.getProperty("user.dir") + "\\drive\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromePath);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(baseURL);
        WaitForPageLoad();
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }

    @Test(priority = 0)
    public void goToRegisterPage() {
    WebElement linkRegister=driver.findElement(By.linkText("REGISTER"));
    Assert.assertTrue(highLight(linkRegister));
    js.executeScript("arguments[0].click();",linkRegister);
    expectedResult="Welcome: Mercury Tours";
    actualResult=driver.getTitle();
    Assert.assertEquals(actualResult,expectedResult);
    Assert.assertTrue(scrollWindow());
    }

    @Test(priority = 1)
    public void registerAnUse() {
        try {
        WebElement txtFirstName=driver.findElement(By.name("firstName"));
        highLight(txtFirstName);
        txtFirstName.sendKeys("Francisco T");
        WebElement ddlCountry=driver.findElement(By.name("country"));
        highLight(ddlCountry);
        new Select(ddlCountry).deselectByVisibleText("AUSTRIA");

        highLight(driver.findElement(By.id("email")));
        driver.findElement(By.id("email")).sendKeys("fulano@gmail.com");

        highLight(driver.findElement(By.name("password")));
        driver.findElement(By.name("passord")).sendKeys("123");

        WebElement txtConfirmPass=driver.findElement(By.name("confirmPassword"));
        highLight(txtConfirmPass);
        txtConfirmPass.sendKeys("123");
        txtConfirmPass.submit();

        Assert.assertTrue(WaitForPageLoad());

        highLight(driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr/td[2]/table/tbody/tr[4]/td/table/tbody/tr/td[2]/table/tbody/tr[3]/td/p[3]/font/b")));
        } catch (
                NoSuchElementException ne) {
            System.out.println("No se encontró el WebElement: " + ne.getMessage());
        } catch (Exception ex) {
            Assert.fail("Test failed:! " + ex.getMessage());
        }

    }
}
