package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>(); // To make the report thread safe
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize Extent report

	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("Orange HRM Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(spark);

			// Adding system information
			extent.setSystemInfo("Operating system", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));

		}

		return extent;
	}

	// Start the test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// End the test

	public synchronized static void endTest() {
		getReporter().flush();
	}

	// Get Current Thread's test
	public synchronized static ExtentTest getTest() {
		return test.get();
	}

	// Method to get the name of the current test
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No Test is currently active for this thread";
		}
	}

	// Log a step
	public static void logStep(String message) {
		getTest().info(message);
	}

	// Log step validation with SS
	public static void logStepWithScreenShot(WebDriver driver, String logMessage, String screenShotMessage) {
		getTest().pass(logMessage);
		attachSS(driver, screenShotMessage);
	}

	// Failure message log
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
		getTest().fail(logMessage);
		attachSS(driver, screenShotMessage);
	}

	// Skip message log
	public static void logSkip(String logMessage) {
		getTest().skip(logMessage);
	}

	// Take screenshots with date and time in the file
//	public synchronized static String takeScreenshot(WebDriver driver, String ScreenshotName) throws IOException {
//
//		TakesScreenshot ts = (TakesScreenshot) driver;
//		File src = ts.getScreenshotAs(OutputType.FILE);
//
//		// Format data and time for fileName
//		String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
//
//		// Save the screenshot
//		String destinationPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/screenShots/"
//				+ ScreenshotName + "_" + timeStamp + ".png";
//
//		File finalPath = new File(destinationPath);
//		FileUtils.copyFile(src, finalPath);
//
//		// Convert screenshot to Base64 format embedding
//
//		String base64Format = convertToBase64(src);
//		return base64Format;
//
//	}

	public synchronized static String takeScreenshot(WebDriver driver, String ScreenshotName) throws IOException {

		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);

		String timeStamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());

		String destinationPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/screenShots/"
				+ ScreenshotName + "_" + timeStamp + ".png";

		File finalPath = new File(destinationPath);
		finalPath.getParentFile().mkdirs(); // 🔥 ensure directory exists
		FileUtils.copyFile(src, finalPath);

		return convertToBase64(finalPath);
	}

	// Convert SS to base64 format
	public static String convertToBase64(File screenShotFile) throws IOException {

		String base64Format = "";
		// Read the file content into a byte array
		byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
		// Convert the byte to a base64 String
		return base64Format = Base64.getEncoder().encodeToString(fileContent);

	}

	// Attach SS to report using base 64
	public synchronized static void attachSS(WebDriver driver, String message) {

		String screenShotBase64;
		try {
			screenShotBase64 = takeScreenshot(driver, getTestName() + "_" + message);
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenShotBase64).build());
		} catch (IOException e) {
			getTest().fail("Failed to attach screenshots = " + message);
			e.printStackTrace();
		}

	}

	// Register Webdriver for current thread

	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().threadId(), driver);
	}

}
