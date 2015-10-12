/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.timepicker.test.ui;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDateTimePicker extends CommonUIUtils
{
	
	public static void selectPeriod(WebDriver driver, String uicontrol, String period) throws Exception
	{
		String sDateTime = null;	
		//Verify the component displayed
		Assert.assertTrue(driver.isElementPresent(UIControls.sTimeRangeBtn_1));
		Assert.assertTrue(driver.isElementPresent(UIControls.sTimeRangeBtn_2));
		driver.getLogger().info(driver.getText(UIControls.sTimeRangeBtn_1));
		String sDateTime_2 = driver.getText(UIControls.sTimeRangeBtn_2);
				
		//click Time Range dropdown list
		driver.getLogger().info("Click the DateTimePicker Componment");
		driver.click(UIControls.sTimeRangeBtn_1);
		driver.takeScreenShot();
		Thread.sleep(5000);
		driver.getLogger().info("Verify the Pick Panel displayed");
		Assert.assertTrue(driver.isElementPresent(UIControls.sPickPanel));
				
				//click time period button
				Thread.sleep(5000);
				driver.getLogger().info("Click button: "+ period);
				driver.click(uicontrol);
				driver.takeScreenShot();
						
				//verify the date time range is changed to 15 minutes
				Thread.sleep(5000);
				driver.getLogger().info("Verify the "+period+" is selected");
				Assert.assertEquals(driver.getText(UIControls.sTimePeriod), period);
				
				//click Apply button
				Thread.sleep(5000);
				driver.getLogger().info("Click Apply button");
				driver.click(UIControls.sApplyBtn);
				//verify the date time is set
				Thread.sleep(5000);
				driver.takeScreenShot();
				
				driver.getLogger().info("Verify the component is existed");
				Assert.assertTrue(driver.isElementPresent(UIControls.sTimeRangeBtn_1_new));
				
				driver.getLogger().info("Verify the result");
				driver.getLogger().info(driver.getText(UIControls.sTimeRangeBtn_1_new));
				sDateTime = driver.getText(UIControls.sTimeRangeBtn_1_new);
				String[] sTemp = sDateTime.split(":|-");
				Assert.assertEquals(sTemp[0], period);
				Assert.assertEquals(sTemp[1].trim()+":"+sTemp[2].trim(),driver.getText(UIControls.sStartText));
				Assert.assertEquals(sTemp[3].trim()+":"+sTemp[4].trim(),driver.getText(UIControls.sEndText));

				//verify the second component value is not changed	
				Assert.assertEquals(sDateTime_2,driver.getText(UIControls.sTimeRangeBtn_2_new));
	}
	
	@Test
	public void testDateTimePicker_Last15Min() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last15Min";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);		
		
		selectPeriod(webdriver, UIControls.sLast15MinBtn, "Last 15 minutes");
		
		webdriver.shutdownBrowser(true);
		
	}
	
	@Test
	public void testDateTimePicker_Last30Min() throws Exception
	{		

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last30Min";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);		
		
		selectPeriod(webdriver, UIControls.sLast30MinBtn, "Last 30 minutes");
		
		webdriver.shutdownBrowser(true);
	}	
	
	@Test
	public void testDateTimePicker_Last60Min() throws Exception
	{		

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last60Min";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);		
		
		selectPeriod(webdriver, UIControls.sLast60MinBtn, "Last 60 minutes");
		
		webdriver.shutdownBrowser(true);
	}	
	
	@Test
	public void testDateTimePicker_Last4Hour() throws Exception
	{		

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last4Hour";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);		
		
		selectPeriod(webdriver, UIControls.sLast4HourBtn, "Last 4 hours");
		
		webdriver.shutdownBrowser(true);
	}
	
	@Test
	public void testDateTimePicker_Last6Hour() throws Exception
	{		

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last6Hour";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);		
		
		selectPeriod(webdriver, UIControls.sLast6HourBtn, "Last 6 hours");
		
		webdriver.shutdownBrowser(true);
	}	
	
	@Test
	public void testDateTimePicker_Last1Day() throws Exception
	{		

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last1Day";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);		
		
		selectPeriod(webdriver, UIControls.sLast1DayBtn, "Last 1 day");
		
		webdriver.shutdownBrowser(true);
	}
	
	@Test
	public void testDateTimePicker_Last7Day() throws Exception
	{		

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last7Day";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);		
		
		selectPeriod(webdriver, UIControls.sLast7DayBtn, "Last 7 days");
		
		webdriver.shutdownBrowser(true);
	}
	
	@Test
	public void testDateTimePicker_Last30Day() throws Exception
	{		

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last30Day";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);		
		
		selectPeriod(webdriver, UIControls.sLast30DayBtn, "Last 30 days");
		
		webdriver.shutdownBrowser(true);
	}
	
	@Test
	public void testDateTimePicker_Last90Day() throws Exception
	{		

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last90Day";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);		
		
		selectPeriod(webdriver, UIControls.sLast90DayBtn, "Last 90 days");
		
		webdriver.shutdownBrowser(true);
	}
	
	@Test
	public void testDateTimePicker_Custom() throws Exception
	{
		
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Custom";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);
		
		String sDateTime = null;	
		//Verify the component displayed
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sTimeRangeBtn_1));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sTimeRangeBtn_2));
		webdriver.getLogger().info(webdriver.getText(UIControls.sTimeRangeBtn_1));
		String sDateTime_2 = webdriver.getText(UIControls.sTimeRangeBtn_2);
				
		//click Time Range dropdown list
		webdriver.getLogger().info("Click the DateTimePicker Componment");
		webdriver.click(UIControls.sTimeRangeBtn_1);
		webdriver.takeScreenShot();
		Thread.sleep(5000);
		webdriver.getLogger().info("Verify the Pick Panel displayed");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPickPanel));
				
		//click Custom button
		Thread.sleep(5000);
		webdriver.getLogger().info("Click button: Custom");
		webdriver.click(UIControls.sLastCustomBtn);
		webdriver.takeScreenShot();
						
		//verify the date time range is changed to time period
		Thread.sleep(5000);
		webdriver.getLogger().info("Verify the Custom is selected");
		Assert.assertEquals(webdriver.getText(UIControls.sTimePeriod), "Custom");
		
		//set start date time and end date time
		webdriver.clear(UIControls.sStartDateInput);
		webdriver.sendKeys(UIControls.sStartDateInput, "10/11/2015");
		webdriver.clear(UIControls.sStartTimeInput);
		webdriver.sendKeys(UIControls.sStartTimeInput, "09:54 AM");
		webdriver.clear(UIControls.sEndDateInput);
		webdriver.sendKeys(UIControls.sEndDateInput, "10/12/2015");
		webdriver.clear(UIControls.sEndTimeInput);
		webdriver.sendKeys(UIControls.sEndTimeInput, "09:54 AM");
		
				
		//click Apply button
		Thread.sleep(5000);
		webdriver.getLogger().info("Click Apply button");
		webdriver.click(UIControls.sApplyBtn);
		//verify the date time is set
		Thread.sleep(5000);
		webdriver.takeScreenShot();
			
		webdriver.getLogger().info("Verify the component is existed");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sTimeRangeBtn_1_new));
				
		webdriver.getLogger().info("Verify the result");
		webdriver.getLogger().info(webdriver.getText(UIControls.sTimeRangeBtn_1_new));
		sDateTime = webdriver.getText(UIControls.sTimeRangeBtn_1_new);
		String[] sTemp = sDateTime.split(":|-");
		Assert.assertEquals(sTemp[0], "Custom");
		Assert.assertEquals(sTemp[1].trim()+":"+sTemp[2].trim(),webdriver.getText(UIControls.sStartText));
		Assert.assertEquals(sTemp[3].trim()+":"+sTemp[4].trim(),webdriver.getText(UIControls.sEndText));

		//verify the second component value is not changed	
		Assert.assertEquals(sDateTime_2,webdriver.getText(UIControls.sTimeRangeBtn_2_new));
		
		webdriver.shutdownBrowser(true);
		
	}
}
