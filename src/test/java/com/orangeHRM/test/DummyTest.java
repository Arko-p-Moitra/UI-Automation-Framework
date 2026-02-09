package com.orangeHRM.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;

public class DummyTest extends BaseClass{

	@Test
	public void dummyTest() {
		
		String title=getDriver().getTitle();
	//	assert.assertEquals(title,"OrangeHRM");
		assert title.equals("OrangeHRM");
		
	}
}
