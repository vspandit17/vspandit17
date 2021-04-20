package com.generator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.base.utility.EncryptionUtility;
import com.base.utility.FileUtil;
import com.base.webDrivers.WebDriverUtil;

public class JmeterAndSeleniumSnapGenerator {

	public static void main(String[] args) throws IOException, InterruptedException {
		JmeterAndSeleniumSnapGenerator snapShotGenerator = new JmeterAndSeleniumSnapGenerator();
		if (args.length > 1)
			snapShotGenerator.takeSnapshot(args);
		else
			snapShotGenerator.takeSnapshot(args[0]);
	}

	private void takeSnapshot(String... ersReportparam) {
		Path absolutePath = Paths.get(".").toAbsolutePath();
		String path = absolutePath.toString().replaceAll("\\\\", "/").replaceAll("[.]", "");
		System.setProperty("webdriver.chrome.driver", ersReportparam[0]);
		WebDriver driver = WebDriverUtil.getChromeDriver();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get(path + ersReportparam[1]);
		driver.findElement(By.xpath("//a[contains(@view,'dashboard-view')]/i[contains(@class,'material-icons') and contains(.,'track_changes')]"))
		                        .click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		driver.manage().window().setSize(new Dimension(Integer.parseInt(ersReportparam[2]), Integer.parseInt(ersReportparam[3])));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(ersReportparam[4].concat("/mailSnap.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		driver.close();
		String htmlContent = "<html><body><img width='" + ersReportparam[5] + "' height='" + ersReportparam[6] + "' src='data:image/png;base64,"
		                        .concat(EncryptionUtility.encodeFileToBase64Binary(new File(ersReportparam[4].concat("/mailSnap.png"))))
		                        .concat("'/></body></html>");
		FileUtil.writeToFile(htmlContent, ersReportparam[4].concat("/mailReport.html"));
	}

	private void takeSnapshot(String mailPath) throws IOException, InterruptedException {
		System.setProperty("webdriver.chrome.driver",
		                        /* "setup/driver/chromedriver.exe" */ "C:\\JMETER_SCRIPTS\\setup\\driver\\chromedriver.exe");
		WebDriver driver = WebDriverUtil.getChromeDriver();
		try {
			driver.manage().window().maximize();
			System.out.println("Mail Path is " + mailPath);
			driver.get(mailPath + "/index.html");
			Thread.sleep(5000);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			WebElement webElement = driver.findElement(By.xpath("//table[@id='statisticsTable']"));
			executor.executeScript("arguments[0].scrollIntoView();", webElement);
			// webElement =
			// driver.findElement(By.xpath("//*[@id='page-wrapper']/div/div[2]/div[3]"));
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			BufferedImage fullImg = ImageIO.read(screenshot);
			BufferedImage eleScreenshot = fullImg.getSubimage(webElement.getLocation().getX(), 0, webElement.getSize().getWidth(),
			                        webElement.getSize().getHeight() + 50);
			ImageIO.write(eleScreenshot, "png", screenshot);
			File screenshotLocation = new File(mailPath + "\\screenshot.png");
			FileUtils.copyFile(screenshot, screenshotLocation);
			String htmlContent = "<html><body><img width='" + "100%" + "' height='" + "100%" + "' src='data:image/png;base64,"
			                        .concat(EncryptionUtility.encodeFileToBase64Binary(new File(mailPath.concat("/screenshot.png"))))
			                        .concat("'/></body></html>");
			FileUtil.writeToFile(htmlContent, mailPath.concat("/mailReport.html"));
		} finally {
			driver.close();
		}

	}

}
