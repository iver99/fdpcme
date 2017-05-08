/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.testng.Assert;

/**
 * @author shangwan
 */
public class CommonUIUtils
{
	private static WebDriver driver;

	public static void loadWebDriver(WebDriver webDriver)
	{
		driver = webDriver;
	}

	public static void verifyResult(WebDriver driver, int index, String returnDate, TimeRange option, String StartLabelLocator,
			String EndLabelLocator, boolean DateOnly, boolean MillionSeconds) throws ParseException
	{
		driver.getLogger().info("Start to verify Results...");
		Validator.equalOrLargerThan("index", index, 1);
		WaitUtil.waitForPageFullyLoaded(driver);

		String strTimePickerText = driver.getWebDriver().findElements(By.cssSelector(UIControls.TIMERANGEBTN_CSS)).get(index - 1)
				.getText();

		driver.getLogger().info("TimePickerLabel: " + strTimePickerText);
		Assert.assertEquals(strTimePickerText, TimeSelectorUtil.getTimeRangeLabel(driver, index));

		String timeRange = option.getRangeOption();

		String sTmpStartDateTime = driver.getText(StartLabelLocator);
		String sTmpEndDateTime = driver.getText(EndLabelLocator);

		SimpleDateFormat fmt = null;

		if (DateOnly) {
			fmt = new SimpleDateFormat("MMM d, yyyy");
		}
		else {
			if (MillionSeconds) {
				fmt = new SimpleDateFormat("MMM d, yyyy h:mm:ss:SSS a");
			}
			else {
				fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");
			}
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

	public static void verifyResult_FlexibleDate(WebDriver driver, int index, String returnDate, String StartLabelLocator,
			String EndLabelLocator, boolean DateOnly, boolean MillionSeconds) throws ParseException
	{
		driver.getLogger().info("Start to verify Results...");
		Validator.equalOrLargerThan("index", index, 1);
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
			if (MillionSeconds) {
				fmt = new SimpleDateFormat("MMM d, yyyy h:mm:ss:SSS a");
			}
			else {
				fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");
			}
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
}
