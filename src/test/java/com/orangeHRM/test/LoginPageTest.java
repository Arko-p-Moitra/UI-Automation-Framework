package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass{

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setupPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	
//	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	@Test(description= "Verify Login of the website")
	public void verifyLoginTest() {
		ExtentManager.startTest("Valid Login Test");
		ExtentManager.logStep("Navigating to Login page");
		loginPage.login("Admin", "admin123");
		ExtentManager.logStep("Veffying Admin Tab");
		Assert.assertTrue(homePage.isAdminTabVisible());
		ExtentManager.logStep("Logging out of the system");
		homePage.logout();
		ExtentManager.logStep("Logged out successfully");
		staticWait(20);
	}
	
	
	@Test(description= "Verify behaviour if the login details are wrong")
	public void errorMessageValidate() {
		ExtentManager.startTest("Invalid Login Test");
		ExtentManager.logStep("Navigating to Login page");
		ExtentManager.logStep("Entering wrong password");
		loginPage.login("Admin323", "admin123");
		ExtentManager.logStep("Verifying error message");
		if(loginPage.errorMessage()) {
			ExtentManager.logStep("Verification successfull");	
		}
		else {
			ExtentManager.logFailure(getDriver(),"Verification failed","Invalid credentials Text Not Found");	
		}
		
	}
	
	@Test(description= "Verify behaviour if the login details are wrong(Failed version)")
	public void errorMessageValidateFailed() {
		ExtentManager.startTest("Invalid Login Test (Failed)");
		ExtentManager.logStep("Navigating to Login page");
		ExtentManager.logStep("Entering wrong password");
		loginPage.login("Admin", "admin123");
		ExtentManager.logStep("Verifying error message");
		if(loginPage.errorMessage()) {
			ExtentManager.logStep("Verification successfull");	
		}
		else {
			ExtentManager.logFailure(getDriver(),"Verification failed","Invalid credentials Text Not Found");	
		}
		
	}

}
