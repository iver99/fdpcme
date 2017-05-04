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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.UIControls;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestTimePicker_MillionSeconds extends LoginAndLogout
{
	private static void verifyResult(WebDriver driver, int index, String returnDate, String StartLabelLocator,
			String EndLabelLocator, boolean DateOnly) throws ParseException
	{

		WaitUtil.waitForPageFullyLoaded(driver);

		String strTimePickerText = driver.getWebDriver().findElements(By.cssSelector(UIControls.TIMERANGEBTN_CSS)).get(index - 1)
				.getText();

		driver.getLogger().info("TimePickerLabel: " + strTimePickerText);
		Assert.assertEquals(strTimePickerText, TimeSelectorUtil.getTimeRangeLabel(driver, index));

		String timeRange = "";

		if (!strTimePickerText.equals("") && strTimePickerText != null) {
			String[] tmpArray = strTimePickerText.split(":");
			timeRange = tmpArray[0];
		}

		driver.getLogger().info("timerange: " + timeRange);
		driver.getLogger().info("returnDate: " + returnDate);

		String str_Range = "";
		String str_TimeInterval = "";
		String str_TimeUnit = "";

		if (timeRange.startsWith("Last")) {
			str_Range = timeRange.substring(5);
			driver.getLogger().info("Range:  " + str_Range);
			if (Character.isDigit(str_Range.charAt(0))) {
				String[] str_Temp = str_Range.split(" ");
				str_TimeInterval = str_Temp[0];
				str_TimeUnit = str_Temp[1];
			}
			else {
				str_TimeInterval = "1";
				str_TimeUnit = str_Range;
			}
		}

		String sTmpStartDateTime = driver.getText(StartLabelLocator);
		String sTmpEndDateTime = driver.getText(EndLabelLocator);

		SimpleDateFormat fmt = null;

		if (DateOnly) {
			fmt = new SimpleDateFormat("MMM d, yyyy");
		}
		else {

			fmt = new SimpleDateFormat("MMM d, yyyy h:mm:ss:SSS a");
		}

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = "";

		if (!"Latest".equals(timeRange)) {
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

		driver.getLogger().info(Long.toString(lTimeRange));

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(dTmpStart);

		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(dTmpEnd);

		int i = Integer.parseInt(str_TimeInterval);

		//verify the time range is expected
		switch (str_TimeUnit) {
			case "second":
				Assert.assertEquals(lTimeRange / 1000, 1);
				break;
			case "seconds":
				Assert.assertEquals(lTimeRange / 1000, i);
				break;
			case "minute":
				Assert.assertEquals(lTimeRange / (60 * 1000), 1);
				break;
			case "minutes":
				Assert.assertEquals(lTimeRange / (60 * 1000), i);
				break;
			case "hour":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 1);
				break;
			case "hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), i);
				break;
			case "day":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 1);
				break;
			case "days":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), i);
				break;
			case "week":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000 * 7), 1);
				break;
			case "weeks":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000 * 7), i);
				break;
			case "month":
				Assert.assertEquals(calEnd.get(Calendar.YEAR) * 12 + calEnd.get(Calendar.MONTH) - calStart.get(Calendar.YEAR)
						* 12 - calStart.get(Calendar.MONTH), 1);
				//below assertion not always true
				//e.g. Last 1 Month, Now is March 31, 2017, the start time is Feb 28, 2017 (there is no 31 in Feb, 2017)
				//TODO calculate correct start date separately and compare
				//Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "months":
				Assert.assertEquals(calEnd.get(Calendar.YEAR) * 12 + calEnd.get(Calendar.MONTH) - calStart.get(Calendar.YEAR)
						* 12 - calStart.get(Calendar.MONTH), i);
				//below assertion not always true
				//e.g. Last 11 Months, Now is March 31, 2017, the start time is Feb 29, 2016 (there is no 31 in Feb, 2016)
				//TODO calculate correct start date separately and compare
				//Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "year":
				Assert.assertEquals(calStart.get(Calendar.YEAR) + 1, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				//below assertion not always true
				//e.g. Last 1 Year, Now is Feb 29, 2016, the start time is Feb 28, 2015 (there is no 29 in Feb, 2015)
				//TODO calculate correct start date separately and compare
				//Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "years":
				Assert.assertEquals(calStart.get(Calendar.YEAR) + i, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				//below assertion not always true
				//e.g. Last 2 Years, Now is Feb 29, 2016, the start time is Feb 28, 2014 (there is no 29 in Feb, 2014)
				//TODO calculate correct start date separately and compare
				//Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			default:
				break;
		}

		driver.getLogger().info("Verify Result Pass!");

	}

	private static void verifyResult(WebDriver driver, String returnDate, TimeRange option, String StartLabelLocator,
			String EndLabelLocator) throws ParseException
	{
		TestTimePicker_MillionSeconds.verifyResult(driver, returnDate, option, StartLabelLocator, EndLabelLocator, false);
	}

	private static void verifyResult(WebDriver driver, String returnDate, TimeRange option, String StartLabelLocator,
			String EndLabelLocator, boolean DateOnly) throws ParseException
	{
		WaitUtil.waitForPageFullyLoaded(driver);

		String strTimePickerText = driver.getWebDriver().findElement(By.cssSelector(UIControls.TIMERANGEBTN_CSS)).getText();

		driver.getLogger().info("Return Date: " + returnDate);
		driver.getLogger().info("TimePickerLabel: " + strTimePickerText);
		Assert.assertEquals(strTimePickerText, TimeSelectorUtil.getTimeRangeLabel(driver));

		String timeRange = option.getRangeOption();

		String sTmpStartDateTime = driver.getText(StartLabelLocator);
		String sTmpEndDateTime = driver.getText(EndLabelLocator);

		SimpleDateFormat fmt = null;

		if (DateOnly) {
			fmt = new SimpleDateFormat("MMM d, yyyy");
		}
		else {

			fmt = new SimpleDateFormat("MMM d, yyyy h:mm:ss:SSS a");
		}

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = "";

		if (!"Latest".equals(timeRange)) {
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
			case "Last 24 hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 24);
				break;
			case "Last 12 months":
				Assert.assertEquals(calStart.get(Calendar.YEAR) + 1, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "Last 8 hours":
				Assert.assertEquals(lTimeRange / (60 * 60 * 1000), 8);
				break;
			case "Last 7 days":
				Assert.assertEquals(lTimeRange / (24 * 60 * 60 * 1000), 7);
				break;
			case "Last 60 mins":
				Assert.assertEquals(lTimeRange / (60 * 1000), 60);
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
			default:
				break;
		}

		driver.getLogger().info("Verify Result Pass!");
	}

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName, "timeSelectorMilliseconds.html");
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Custom_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTimeWithMillisecond(webd, 2, "10/10/2016 12:00:00:000 AM",
				"12/14/2016 12:30:59:000 PM");

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Custom_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Custom");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setCustomTimeWithMillisecond(webd, "04/14/2016 12:00:00:000 AM",
				"04/14/2016 12:30:59:000 PM");

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Custom, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Day_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Day_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Day_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Day_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Days_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Days_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 12, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Days_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Days_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Day);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Hour_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Hour_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Hour_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Hour_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Hours_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Hours_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 27, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Hours_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Hours_ShortTerm()");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Hour);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Minute_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Minute_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Minute_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Minute_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Minutes_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Minutes_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 28, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Minutes_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Minutes_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Minute);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Month_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Month_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Month_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Month_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Months_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Months_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 5, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Months_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Months_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Month);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Second_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Second_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Second_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Second_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Seconds_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Seconds_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 79, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Seconds_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Seconds_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Second);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Week_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Week_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Week_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Week_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Weeks_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Weeks_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 4, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Weeks_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Weeks_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Week);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Year_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Year_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 1, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Year_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Year_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 1, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Years_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Years_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, 2, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 2, returnDate, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_FlexibleDate_Years_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_FlexibleDate_Years_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Custom");
		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithMillisecond(webd, 2, TimeUnit.Year);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, 1, returnDate, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1, false);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last12Months() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last12Months");

		//set time range
		webd.getLogger().info("set timerange as Last 12 Months");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Last12Months);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Last12Months, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last14Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last14Days");

		//set time range
		webd.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Last14Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Last14Days, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last15Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last15Mins");

		//set time range
		webd.getLogger().info("set timerange as Last 15 minutes");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.Last15Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Last15Mins, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last24Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last24Hours");

		//set time range
		webd.getLogger().info("set timerange as Last 24 hours");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 1, TimeRange.Last24Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Last24Hours, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Days()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Days");

		//set time range
		webd.getLogger().info("set timerange as Last 30 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Last30Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		try {
			TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Last30Days, UIControls.SSTARTTEXT_MILLIONSEC2,
					UIControls.SENDTEXT_MILLIONSEC2);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last30Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last30Mins");

		//set time range
		webd.getLogger().info("set timerange as Last 30 minutes");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.Last30Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Last30Mins, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last60Mins() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last60Mins");

		//set time range
		webd.getLogger().info("set timerange as Last 60 minutes");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.NewLast60Mins);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.NewLast60Mins, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last7Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last7Days");

		//set time range
		webd.getLogger().info("set timerange as new Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.NewLast7Days);
		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.NewLast7Days, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last8Hours() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last8Hours");

		//set time range
		webd.getLogger().info("set timerange as Last 8 hours");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.Last8Hours);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Last8Hours, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Last90Days() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Last90Days");

		//set time range
		webd.getLogger().info("set timerange as Last 90 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Last90Days);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Last90Days, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2);
		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Latest_LongTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Latest_LongTerm");

		//set time range
		webd.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, 2, TimeRange.Latest);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Latest, UIControls.SSTARTTEXT_MILLIONSEC2,
				UIControls.SENDTEXT_MILLIONSEC2);

		webd.shutdownBrowser(true);
	}

	@Test(alwaysRun = true)
	public void testTimePicker_Latest_ShortTerm() throws ParseException
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testTimePicker_Latest_ShortTerm");

		//set time range
		webd.getLogger().info("set timerange as Latest");
		String returnDate = TimeSelectorUtil.setTimeRangeWithMillisecond(webd, TimeRange.Latest);

		//verify the result
		webd.getLogger().info("verify the time range is set correctly");
		TestTimePicker_MillionSeconds.verifyResult(webd, returnDate, TimeRange.Latest, UIControls.SSTARTTEXT_MILLIONSEC1,
				UIControls.SENDTEXT_MILLIONSEC1);

		webd.shutdownBrowser(true);
	}
}
