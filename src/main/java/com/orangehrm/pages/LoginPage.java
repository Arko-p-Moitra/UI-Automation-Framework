package com.orangehrm.pages;

import java.awt.Desktop.Action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actionDriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver action;
	
	//DefineLocators using By class
	
	private By userName = By.xpath("//input[@name='username']");
	private By passWord=  By.xpath("//input[@name='password']");
	private By loginButton=By.xpath("//button[contains(.,'Login')]");
	private By errorMessage=By.xpath("//p[text()='Invalid credentials']");
	
	
	//We are initializing the action driver here
//	public LoginPage(WebDriver driver) {
//		this.action=new ActionDriver(driver);
//	}
	
	public LoginPage(WebDriver driver) {
		this.action = BaseClass.getActionDriver();
	}
	//Method to perform login
	
	public void login(String username, String password)
	{
		action.enterText(userName, username);
		action.enterText(passWord, password);
		action.click(loginButton);
	}
	
	//Method to check if error message is displayed
	public boolean errorMessage() {
		return action.compareText(errorMessage, "Invalid credentials");
	}
	
	public void verifyErrorMessage(String expected) {
		login("admin", "admin9876");
		String errorMsg = action.getText(errorMessage);
		action.compareText(errorMessage, "Invalid credentials");
	}
	
	
	
	
}
