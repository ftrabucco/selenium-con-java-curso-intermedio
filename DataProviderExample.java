package vatesorg.intermedio.intermedio;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class DataProviderExample {
    WebDriver driver;

    @BeforeTest
    public void setup(){
        System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\drive\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://google.com");
    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }

    @Test(dataProvider = "SearchProvider" , dataProviderClass = DataProviderClass.class)
    public void testMethod(String tester, String search) throws InterruptedException {
        WebElement searchText=driver.findElement(By.name("q"));
        searchText.sendKeys(search);
        System.out.println("Welcome : "+ tester + " your search is: "+search);
        Thread.sleep(3000);

        String testValue=searchText.getAttribute("value");
        System.out.println("Test value is --> "+ testValue + " and is equals to "+ search);
        searchText.clear();

        Assert.assertTrue(testValue.equals(search));
    }




    /*
    //En este ejemplo usamos dataprovider sin llamar a la clase externa
    @DataProvider(name="SearchProvider")
    public Object[][]getDataFromProvider(){
        return new Object[][]{
                {"Fernando","Google"},
                {"Luis","Yahoo"},
                {"Sara","Gmail"},
                {"Lorena","Amazon"},
        };
    }

    @Test(dataProvider = "SearchProvider")
    public void testMetod(String tester, String search) throws InterruptedException {
        WebElement searchText=driver.findElement(By.name("q"));
        searchText.sendKeys(search);
        System.out.println("Welcome : "+ tester + " your search is: "+search);
        Thread.sleep(3000);

        String testValue=searchText.getAttribute("value");
        System.out.println("The value is--> " +testValue + " and is equals to"+search );
        searchText.clear();
        Assert.assertTrue(testValue.equals(search));
    }
     */


}
