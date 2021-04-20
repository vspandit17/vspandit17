package com.base.webDrivers;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverUtil {

	public static WebDriver getChromeDriver() {

		WebDriver driver = new ChromeDriver(getChromeCapabilitiesAsperWebDriverConfig());
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		/*
		 * driver.get("chrome://extensions-frame"); WebElement checkbox = driver
		 * .findElement(By.xpath(
		 * "//label[@class='incognito-control']/input[@type='checkbox']")); if
		 * (!checkbox.isSelected()) { checkbox.click(); }
		 */
		driver.manage().window().maximize();
		return driver;
	}

	private static DesiredCapabilities getChromeCapabilitiesAsperWebDriverConfig() {
		System.setProperty("webdriver.chrome.driver", "setup/driver/chromedriver.exe");
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
		ChromeOptions chromeOptions = new ChromeOptions();
		HashMap<String, Object> chromePrefsMap = new HashMap<String, Object>();
		chromeOptions.setExperimentalOption("prefs", chromePrefsMap);
		chromeOptions.addArguments("test-type");
		chromeOptions.addArguments("chrome.switches", "--incognito");
		chromeOptions.addArguments("chrome.switches", "--disable-extensions");
		chromeOptions.addArguments("disable-infobars");
		chromeOptions.addArguments("--always-authorize-plugins=true");
		chromeOptions.addArguments("--silent");
		desiredCapabilities.setCapability("chrome.prefs", chromePrefsMap);
		desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		return desiredCapabilities;
	}

	public static WebDriver createWebDriverInstance(String webDriverType) {
		DesiredCapabilities capabilities = null;
		switch (webDriverType) {
		case "CHROME":
			capabilities = getChromeCapabilitiesAsperWebDriverConfig();
			capabilities.setBrowserName(webDriverType);
			WebDriver driver = new ChromeDriver(capabilities);
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.get("chrome://extensions-frame");
			WebElement checkbox = driver.findElement(By.xpath("//label[@class='incognito-control']/input[@type='checkbox']"));
			if (!checkbox.isSelected()) {
				checkbox.click();
			}
			return driver;
		default:
			throw new IllegalArgumentException("Illegal webDriverType '" + webDriverType + "' provided !!!");
		}
	}

}
