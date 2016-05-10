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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil.TimeRange;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDateTimePicker extends CommonUIUtils
{
	private static void verifyResult(WebDriver driver, String returnDate, TimeRange option) throws Exception
	{

		String timeRange = option.getRangeOption();

		String sTmpStartDateTime = driver.getText(UIControls.sStartText);
		String sTmpEndDateTime = driver.getText(UIControls.sEndText);

		SimpleDateFormat fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = "";

		if (!timeRange.equals("Latest")) {
			tmpReturnDate = returnDate.substring(timeRange.length() + 1);
			driver.getLogger().info("timerange: "+timeRange);
			driver.getLogger().info("returnDate: "+tmpReturnDate);

			String[] tmpDate = tmpReturnDate.split("-");

			String tmpStartDate = tmpDate[0].trim();
			String tmpEndDate = tmpDate[1].trim();

			Assert.assertEquals(tmpStartDate, sTmpStartDateTime);
			Assert.assertEquals(tmpEndDate, sTmpEndDateTime);
		}

		driver.getLogger().info("Verify the result in label");

		dTmpStart = fmt.parse(sTmpStartDateTime);
		dTmpEnd = fmt.parse(sTmpEndDateTime);

		TimeZone tz = TimeZone.getDefault();

		int starttz = tz.getOffset(dTmpStart.getTime());
		int endtz = tz.getOffset(dTmpEnd.getTime());

		driver.getLogger().info("sStartText: " + sTmpStartDateTime);
		driver.getLogger().info("sEndText: " + sTmpEndDateTime);

		long lTimeRange = dTmpEnd.getTime() - dTmpStart.getTime() + (endtz - starttz);
		
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(dTmpStart);
		
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dTmpEnd);

		//verify the time range is expected
		switch (timeRange) {
			case "Last 15 mins":
				Assert.assertEquals(lTimeRange / (60 * 1000), 15);
				break;
			case "Last 30 mins":
				Assert.assertEquals(lTimeRange / (60 * 1000), 30);
				break;
			case "Last hour":
				Assert.assertEquals(lTimeRange / (60 * 1000), 60);
				break;
			case "Last 4 hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 4);
				break;
			case "Last 6 hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 6);
				break;
			case "Last day":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 1);
				break;
			case "Last week":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 7);
				break;
			case "Last 14 days":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 14);
				break;
			case "Last 30 days":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 30);
				break;
			case "Last 90 day":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 90);
				break;
			case "Last year":
				Assert.assertEquals(calStart.YEAR +1 , calEnd.YEAR);
				Assert.assertEquals(calStart.MONTH, calEnd.MONTH);
				Assert.assertEquals(calStart.DAY_OF_YEAR, calEnd.DAY_OF_YEAR);
				break;
			case "Latest":
				Assert.assertEquals(lTimeRange, 0);
				break;
		}

		driver.getLogger().info("Verify Result Pass!");

	}

	@Test
	public void testDateTimePicker_Custom() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Custom";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTime(webdriver, "04/14/2016 12:00 AM", "04/14/2016 12:30 PM");

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Custom);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last15Mins() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last15Mins";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 15 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last15Mins);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last15Mins);

		webdriver.shutdownBrowser(true);

	}

	@Test
	public void testDateTimePicker_Last1Day() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last1Day";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 1 day");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last1Day);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last1Day);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last30Days() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last30Days";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 30 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last30Days);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last30Days);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last30Mins() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last30Mins";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 30 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last30Mins);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last30Mins);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last4Hours() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last4Hours";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 4 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last4Hours);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last4Hours);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last60Mins() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last60Mins";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 60 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last60Mins);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last60Mins);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last6Hours() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last6Hours";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 6 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last6Hours);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last6Hours);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last7Days() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last7Days";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last7Days);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last7Days);

		webdriver.shutdownBrowser(true);
	}
	
	@Test
	public void testDateTimePicker_Last14Days() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last14Days";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last14Days);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last14Days);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last90Days() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last90Days";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 90 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last90Days);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last90Days);

		webdriver.shutdownBrowser(true);
	}
	
	@Test
	public void testDateTimePicker_Last1Year() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last1Year";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last year");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last1Year);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last1Year);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Latest() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Latest";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Latest);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Latest);

		webdriver.shutdownBrowser(true);
	}

}
