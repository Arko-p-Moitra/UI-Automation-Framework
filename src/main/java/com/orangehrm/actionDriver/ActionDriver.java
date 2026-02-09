package com.orangehrm.actionDriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver,
				Duration.ofSeconds(Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"))));

	}

	// Click method

	public void click(By by) {
		String description = getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an element = " + description);
			ExtentManager.logStepWithScreenShot(driver, "Element Clicked", "Clicked Element");
			logger.info("Clicked an element = " + description);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Clicked an element = " + description,
					"Unable to click = " + description);
			logger.error("Unable to click the element = " + e.getMessage());
		}
	}

	// Method to get text from an input field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			ExtentManager.logStep("The element = " + getElementDescription(by));
			ExtentManager.logStepWithScreenShot(driver, "Compare Text", "Text Value");
			logger.info("Elemet Text = " + getElementDescription(by));

			return driver.findElement(by).getText();

		} catch (Exception e) {
			logger.error("No element found = " + e.getMessage());
			return "";
		}
	}

	// Method to compare two texts
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				logger.info("Text are matching = " + actualText + " equals " + expectedText);
				return true;

			} else {
				logger.error("Text are not matching = " + actualText + " equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			logger.error("Unable to compare text = " + e.getMessage());
			return false;
		}
	}

	// Method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			if (driver.findElement(by).isDisplayed()) {
				logger.info("Element is displayed = " + getElementDescription(by));
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("Element not displayed = " + e.getMessage());
			return false;
		}
	}

	// Method to enter data in input field

	public void enterText(By by, String input) {
		try {
			waitForElementToBeVisible(by);
			driver.findElement(by).clear();
			driver.findElement(by).sendKeys(input);
			logger.info("Element is displayed = " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to enter the value = " + e.getMessage());
		}
	}

	// Scroll to the element

	public void scrollToElement(By by) {

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement ele = driver.findElement(by);
			js.executeScript("arguments[0],scrollIntoView(true);", ele);
		} catch (Exception e) {
			logger.error("Unable to locate element = " + e.getMessage());
		}
	}

	// Wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Eelement is not clickable = " + e.getMessage());
		}

	}

	// Wait for element to be visible

	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
		} catch (Exception e) {
			logger.error("Eelement is not visible = " + e.getMessage());
		}

	}

	// Will get description of locators
	public String getElementDescription(By locator) {

		if (driver == null) {
			return "Driver is null";
		}
		if (locator == null) {
			return "Locator is null";
		}

		// Find the ele using the locator
		try {
			WebElement ele = driver.findElement(locator);
			String name = ele.getDomAttribute("name");
			String id = ele.getDomAttribute("id");
			String text = ele.getText();
			String className = ele.getDomAttribute("class");
			String placeHolder = ele.getDomAttribute("placeholder");

			// Return the description based on element attributs

			if (stringNotEmpty(name)) {
				return "Element with name : " + name;
			} else if (stringNotEmpty(id)) {
				return "Element with id : " + id;
			} else if (stringNotEmpty(text)) {
				return "Element with text : " + stringNotTooLong(text, 50);
			} else if (stringNotEmpty(className)) {
				return "Element with className : " + className;
			} else if (stringNotEmpty(placeHolder)) {
				return "Element with placeHolder : " + placeHolder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element = " + e.getMessage());
		}
		return null;

	}

	private boolean stringNotEmpty(String value) {

		return value != null && !value.isEmpty();

	}

	private String stringNotTooLong(String value, int maxLen) {

		if (value == null || value.length() <= maxLen) {
			return value;
		}
		return value.substring(0, maxLen) + ".....";
	}

}
