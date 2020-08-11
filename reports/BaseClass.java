package vatesorg.intermedio.reports;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class BaseClass {

    static WebDriver driver;
    static String chromePath=System.getProperty("user.dir")+"\\drive\\chromedriver.exe";

    public static WebDriver getDriver(){
    if (driver==null){
        System.setProperty("webdriver.chrome.driver",chromePath);
        driver=new ChromeDriver();
        driver.manage().window().maximize();
    }
    return driver;
    }

    public static void takeScreenShot(WebDriver driver,String fileWithPath)throws IOException {
        TakesScreenshot srcShot=(TakesScreenshot)driver;
        File srcFile = srcShot.getScreenshotAs(OutputType.FILE);
        File destFile = new File (fileWithPath);
        FileUtils.copyFile(srcFile,destFile);
    }

    public static void sendReportByEmail(String from, String pass, String to, String subjet, String body){
        Properties prop= System.getProperties();
        String host= "smtp.gmail.com";
        prop.put("mail.smpt.starttIs.enable","true");
        prop.put("mail.smtp.host",host);
        prop.put("mail.smpt.password",pass);
        prop.put("mail.smtp.port","587");//25,465,587
        prop.put("mail.smt.auth","true");
        Session session=Session.getDefaultInstance(prop);
        MimeMessage message=new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subjet);
            message.setText(body);
            BodyPart objMessageBodyPart= new MimeBodyPart();
            objMessageBodyPart.setText(body);
            Multipart multipart= new MimeMultipart();
            multipart.addBodyPart(objMessageBodyPart);
            objMessageBodyPart= new MimeBodyPart();

            String fileName=System.getProperty("user.dir")+"\\SeleniumIntermedio.pdf";

            DataSource source=new FileDataSource(fileName);
            objMessageBodyPart.setDataHandler(new DataHandler(source));
            objMessageBodyPart.setFileName(fileName);
            multipart.addBodyPart(objMessageBodyPart);
            message.setContent(multipart);
            Transport transport=session.getTransport("smpt");
            transport.connect(host,from,pass);
            transport.sendMessage(message,message.getAllRecipients());
            transport.close();

        } catch (AddressException e){
            System.err.println("Problems with email adress"+e.getMessage());
        } catch (MessagingException e){
            System.err.println("Could not connect to SMPT host, reviw your host and port: "+e.getMessage());
        }
    }


}
