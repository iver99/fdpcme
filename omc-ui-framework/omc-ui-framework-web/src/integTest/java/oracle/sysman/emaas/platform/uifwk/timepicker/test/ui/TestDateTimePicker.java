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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDateTimePicker extends CommonUIUtils
{
	private static void verifyResult(WebDriver driver, String returnDate, TimeRange option, String StartLabelLocator,
			String EndLabelLocator) throws Exception
	{

                WaitUtil.waitForPageFullyLoaded(driver);
		String timeRange = option.getRangeOption();

		String sTmpStartDateTime = driver.getText(StartLabelLocator);
		String sTmpEndDateTime = driver.getText(EndLabelLocator);

		SimpleDateFormat fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = "";

		if (!timeRange.equals("Latest")) {
			tmpReturnDate = returnDate.substring(timeRange.length() + 1);
			driver.getLogger().info("timerange: " + timeRange);
			driver.getLogger().info("returnDate: " + tmpReturnDate);

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
				Assert.assertEquals(calStart.get(Calendar.YEAR) + 1, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Custom, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Custom_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Custom_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTime(webdriver, 2, "04/14/2015 12:00 AM", "04/14/2016 12:30 PM");

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Custom, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last14Days, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last14Days_compact() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last14Days_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last14Days);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last14Days, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last15Mins, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);

	}

	@Test
	public void testDateTimePicker_Last15Mins_compact() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last15Mins_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 15 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last15Mins);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last15Mins, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last1Day, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last1Day_compact() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last1Day_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 1 day");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last1Day);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last1Day, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last1Year, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last1Year_compact() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last1Year_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last year");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last1Year);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last1Year, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last30Days, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last30Days_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last30Days_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 30 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last30Days);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last30Days, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last30Mins, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last30Mins_compact() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last30Mins_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 30 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last30Mins);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last30Mins, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last4Hours, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last4Hours_compact() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last4Hours_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 4 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last4Hours);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last4Hours, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last60Mins, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last60Mins_compact() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last60Mins_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 60 minutes");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last60Mins);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last60Mins, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last6Hours, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last6Hours_compact() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last6Hours_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 6 hours");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last6Hours);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last6Hours, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last7Days, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last7Days_compact() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last7Days_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last7Days);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last7Days, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last90Days, UIControls.sStartText, UIControls.sEndText);
		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Last90Days_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Last90Days_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Last 90 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last90Days);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Last90Days, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);
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
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Latest, UIControls.sStartText, UIControls.sEndText);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testDateTimePicker_Latest_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Latest_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Latest);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestDateTimePicker.verifyResult(webdriver, returnDate, TimeRange.Latest, UIControls.sStartTextCompact,
				UIControls.sEndTextCompact);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterDays() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterDays";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedDays = { 1, 3, 5, 7 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, null, excludedDays, null);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfo));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterDays_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterDays_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedDays = { 2 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, 2, null, excludedDays, null);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfoCompact));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterDaysMonths() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterDaysMonths";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedDays = { 3, 5, 7, 2 };
		int[] excludedMonths = { 1, 2, 8, 7, 9, 11 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, null, excludedDays, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfo));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterDaysMonths_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterDaysMonths_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedDays = { 2, 4, 6 };
		int[] excludedMonths = { 3, 6, 9, 12 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, 2, null, excludedDays, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfoCompact));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterHours() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterHours";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, "0-12,20-22", null, null);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfo));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterHours_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterHours_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, 2, "5-12,20-22", null, null);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfoCompact));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterHoursDays() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterHoursDays";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedDays = { 1, 2, 3, 4 };

		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, "0-6,9-15", excludedDays, null);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfo));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterHoursDays_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterHoursDays_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedDays = { 4, 6 };

		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, 2, "0-6,9-15", excludedDays, null);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfoCompact));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterHoursDaysMonths() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterHoursDaysMonths";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedDays = { 1, 5, 6 };
		int[] excludedMonths = { 1, 2, 6, 7, 9, 11 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, "7-12,21-23", excludedDays, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfo));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterHoursDaysMonths_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterHoursDaysMonths_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedDays = { 6 };
		int[] excludedMonths = { 2, 6, 8 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, 2, "1-3,7-9,21-23", excludedDays, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfoCompact));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterHoursMonths() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterHoursMonths";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time

		int[] excludedMonths = { 1, 5, 7, 9, 12 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, "0-5,19-22", null, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfo));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterHoursMonths_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterHoursMonths_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time

		int[] excludedMonths = { 2, 3, 6, 9, 12, 11 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, 2, "0-5,19-22", null, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfoCompact));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterMonths() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterMonths";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedMonths = { 1, 2, 5, 6, 7, 11 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, null, null, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfo));

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testFilterMonths_compact() throws Exception
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testFilterMonths_compact";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//configure filter time
		int[] excludedMonths = { 2, 5, 8, 11, 3, 12 };
		String returnFilterInfo = TimeSelectorUtil.setTimeFilter(webdriver, 2, null, null, excludedMonths);

		//verify the result
		Assert.assertEquals(returnFilterInfo, webdriver.getText(UIControls.sFilterInfoCompact));

		webdriver.shutdownBrowser(true);
	}
}
