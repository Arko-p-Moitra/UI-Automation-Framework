package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actionDriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	
	private ActionDriver action;
	
	//Define Locators
	
 private By adminTAB= By.xpath("//span[text()='Admin']");
 private By userIDButton=By.xpath("//p[@class='oxd-userdropdown-name']");
 private By logoutButton=By.xpath("//a[text()='Logout']");
 private By orangeHRMLogo=By.xpath("//div[@class='oxd-brand-banner']/img");
 
// public HomePage(WebDriver driver) {
//	 this.action=new ActionDriver(driver);
// }
// 
 
 public HomePage(WebDriver driver) {
		this.action = BaseClass.getActionDriver();
	}
 //Method to verify if Admin tab is visible
 public boolean isAdminTabVisible() {
	 return action.isDisplayed(adminTAB);
 }
 
 public boolean verifyOrangeHRMLogo() {
	 return action.isDisplayed(orangeHRMLogo);
 }
 
 //Method to perform logout
 public void logout()
 {
	 action.click(userIDButton);
	 action.click(logoutButton);
 }

}
