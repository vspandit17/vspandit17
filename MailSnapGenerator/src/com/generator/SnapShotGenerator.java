package com.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.base.utility.EncryptionUtility;
import com.base.utility.FileUtil;
import com.base.webDrivers.WebDriverUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SnapShotGenerator {
	
	public static void main(String[] args) {
		Path absolutePath = Paths.get(".").toAbsolutePath();
		String  path = absolutePath.toString().replaceAll("\\\\", "/").replaceAll("[.]", "");
		
		//System.setProperty("webdriver.chrome.driver", args[0]);
		WebDriverManager.chromedriver().setup();
		WebDriver driver=new ChromeDriver();
		//WebDriver driver = WebDriverUtil.getChromeDriver();
		
		String url="file://"+args[0];
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//a[contains(@view,'dashboard-view')]/i[contains(@class,'material-icons') and contains(.,'track_changes')]")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//driver.findElement(By.xpath("//i[contains(@class,'fa-circle-o-notch')]")).click();
		//driver.manage().window().setSize(new Dimension(Integer.parseInt(args[1].replace("px", "")), Integer.parseInt(args[2].replace("px", ""))));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		 path = (args[0].split("Reports"))[0]+"mailReport/";
		 System.out.println(path + "  path");
	    String directoryName = path.concat("");
	File dir=new File(directoryName);
	dir.mkdir();
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		try {
			copyFileUsingStream(scrFile, new File(path.concat("mailReport.html")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		driver.close();
		String htmlContent = "<html><body><img width='" + args[1] + "' height='" + args[2]
				+ "' src='data:image/png;base64,".concat(EncryptionUtility.encodeFileToBase64Binary(new File(path.concat("mailReport.html")))).concat("'/></body></html>");
		FileUtil.writeToFile(htmlContent, path.concat("mailReport.html"));
	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}
}
