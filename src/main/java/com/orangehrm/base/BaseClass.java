package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actionDriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop; /*
										 * We are keeping it static cozz Static will be initialized in class level so
										 * that if we execute it multiple times it will keep the same value
										 */
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite
	public void loadConfig() throws IOException {

		// Get data from config file
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");

		// Start the extent report
		ExtentManager.getReporter();
	}

	@BeforeMethod
	public void setUp() throws IOException {
		System.out.println("Setting the webdriver for :" + this.getClass().getSimpleName());
		System.out.println("--------------------------------------------------------");
		launchBrowser();
		configureBrowser();
		staticWait(10);

		logger.info("Webdriver Initialized and Browser Maximised");

		// Initializing the action driver object
//		if (actionDriver == null) {
//			actionDriver = new ActionDriver(driver);
//			logger.info("Action driver is created.");
//		}

		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread = " + Thread.currentThread().threadId());

	}

	private void launchBrowser() {

		// Initialize the webdriver based on the browser preference

		String browser = prop.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {
			driver.set(new ChromeDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver instance is created");
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver.set(new FirefoxDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("Firefox instance is created");
		} else if (browser.equalsIgnoreCase("edge")) {
			driver.set(new EdgeDriver());
			ExtentManager.registerDriver(getDriver());
			logger.info("Edge instance is created");
		} else {
			throw new IllegalArgumentException("Browser not supported");
		}
	}

	private void configureBrowser() {
		// Implicit wait

		int implicitWait = Integer.parseInt(prop.getProperty("inplicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// maximize the window

		getDriver().manage().window().maximize();

		// navigate to the website

		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to the URL" + e.getMessage());
		}

	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				logger.info("Unable to get the driver" + e.getMessage());
			}
		}

		System.out.println("Webdriver instance is closed");
		driver.remove();
		actionDriver.remove();
		ExtentManager.endTest();

	}

	// Driver Getter method for prop
	public static Properties getProp() {
		return prop;
	}

	// Getter method for webDriver
	public static WebDriver getDriver() {

		if (driver.get() == null) {
			System.out.println("Webdriver is not initialized");
			throw new IllegalStateException("Webdriver is not initialized");
		}
		return driver.get();
	}

	// Getter method for webDriver
	public static ActionDriver getActionDriver() {

		if (actionDriver.get() == null) {
			System.out.println("ActionDriver is not initialized");
			logger.info("ActionDriver instances =" + Thread.currentThread().threadId());
			throw new IllegalStateException("ActionDriver is not initialized");

		}
		return actionDriver.get();
	}

	// Static wait
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
