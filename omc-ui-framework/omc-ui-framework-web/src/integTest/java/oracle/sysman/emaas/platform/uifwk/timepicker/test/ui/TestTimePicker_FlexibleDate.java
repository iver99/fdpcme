/*
 * Copyright (C) 2016 Oracle
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
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util.UIControls;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestTimePicker_FlexibleDate extends CommonUIUtils
{
	private static void verifyResult(WebDriver driver, int index, String returnDate, String StartLabelLocator,
			String EndLabelLocator, boolean DateOnly) throws ParseException
	{

		WaitUtil.waitForPageFullyLoaded(driver);

		String strTimePickerText = driver.getWebDriver().findElements(By.cssSelector(UIControls.TIMERANGEBTN_CSS)).get(index - 1)
				.getText();

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

			fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");
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
				Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "months":
				Assert.assertEquals(calEnd.get(Calendar.YEAR) * 12 + calEnd.get(Calendar.MONTH) - calStart.get(Calendar.YEAR)
						* 12 - calStart.get(Calendar.MONTH), i);
				Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "year":
				Assert.assertEquals(calStart.get(Calendar.YEAR) + 1, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			case "years":
				Assert.assertEquals(calStart.get(Calendar.YEAR) + i, calEnd.get(Calendar.YEAR));
				Assert.assertEquals(calStart.get(Calendar.MONTH), calEnd.get(Calendar.MONTH));
				Assert.assertEquals(calStart.get(Calendar.DAY_OF_MONTH), calEnd.get(Calendar.DAY_OF_MONTH));
				break;
			default:
				break;
		}

		driver.getLogger().info("Verify Result Pass!");

	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_Day() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_Day";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 1, TimeUnit.Day);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_FewDays() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_FewDays";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 5, TimeUnit.Day);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_FewHours() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_FewHours";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 23, TimeUnit.Hour);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_FewMinutes() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_FewMinutes";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 60, TimeUnit.Minute);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_FewMonths() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_FewMonths";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 11, TimeUnit.Month);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	//@Test
	public void testTimePicker_Compact_FlexibleDate_FewSeconds() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_FewSeconds";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 20, TimeUnit.Second);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_FewWeeks() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_FewWeeks";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 4, TimeUnit.Week);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_FewYears() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_FewYears";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 2, TimeUnit.Year);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_Hour() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_Hour";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 1, TimeUnit.Hour);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_Minute() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_Minute";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 1, TimeUnit.Minute);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_Month() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_Month";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 1, TimeUnit.Month);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	//@Test
	public void testTimePicker_Compact_FlexibleDate_Second() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_Second";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 1, TimeUnit.Second);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_Week() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_Week";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 1, TimeUnit.Week);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_Compact_FlexibleDate_Year() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_Compact_FlexibleDate_Year";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, 1, TimeUnit.Year);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 2, returnDate, UIControls.SSTARTTEXT_COMPACT,
				UIControls.SENDTEXT_COMPACT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_DateOnly_FlexibleDate_Day() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_DateOnly_FlexibleDate_Day";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webdriver, 3, 1, TimeUnit.Day);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_DateOnly_FlexibleDate_FewDays() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_DateOnly_FlexibleDate_FewDays";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webdriver, 3, 3, TimeUnit.Day);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_DateOnly_FlexibleDate_FewMonths() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_DateOnly_FlexibleDate_FewMonths";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webdriver, 3, 6, TimeUnit.Month);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_DateOnly_FlexibleDate_FewWeeks() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_DateOnly_FlexibleDate_FewWeeks";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webdriver, 3, 4, TimeUnit.Week);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_DateOnly_FlexibleDate_FewYears() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_DateOnly_FlexibleDate_FewYears";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webdriver, 3, 4, TimeUnit.Year);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_DateOnly_FlexibleDate_Month() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_DateOnly_FlexibleDate_Month";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webdriver, 3, 1, TimeUnit.Month);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_DateOnly_FlexibleDate_Week() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_DateOnly_FlexibleDate_Week";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webdriver, 3, 1, TimeUnit.Week);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_DateOnly_FlexibleDate_Year() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_DateOnly_FlexibleDate_Year";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRangeWithDateOnly(webdriver, 3, 1, TimeUnit.Year);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 3, returnDate, UIControls.SSTARTTEXT_DATEONLY,
				UIControls.SENDTEXT_DATEONLY, true);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_Day() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_Day";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 1, TimeUnit.Day);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_FewDays() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_FewDays";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, TimeUnit.Day);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_FewHours() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_FewHours";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, TimeUnit.Hour);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_FewMinutes() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_FewMinutes";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, TimeUnit.Minute);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_FewMonths() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_FewMonths";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, TimeUnit.Month);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_FewWeekss() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_FewWeekss";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, TimeUnit.Week);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_FewYears() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_FewYears";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 2, TimeUnit.Year);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_Hour() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_Hour";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 1, TimeUnit.Hour);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_Minute() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_Minute";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 1, TimeUnit.Minute);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_Month() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_Month";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 1, TimeUnit.Month);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_Week() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_Week";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 1, TimeUnit.Week);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}

	@Test
	public void testTimePicker_FlexibleDate_Year() throws ParseException
	{
		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testTimePicker_FlexibleDate_Year";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//set time range
		webdriver.getLogger().info("set timerange as Custom");

		String returnDate = TimeSelectorUtil.setFlexibleRelativeTimeRange(webdriver, 1, TimeUnit.Year);

		//verify the result
		webdriver.getLogger().info("verify the time range is set correctly");
		TestTimePicker_FlexibleDate.verifyResult(webdriver, 1, returnDate, UIControls.SSTARTTEXT, UIControls.SENDTEXT, false);

		webdriver.shutdownBrowser(true);
	}
}
