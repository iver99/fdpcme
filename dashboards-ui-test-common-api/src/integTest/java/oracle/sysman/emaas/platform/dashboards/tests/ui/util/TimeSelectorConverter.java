/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil.TimeRange;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class TimeSelectorConverter
{
	public static void setDateTimeRange(WebDriver driver, String startDateTime, String endDateTime) throws Exception
	{
		String start = TimeSelectorConverter.timeFormatChange(driver, startDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		String end = TimeSelectorConverter.timeFormatChange(driver, endDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		driver.getLogger().info("the start time for default time range is :" + start + ",the end time for default time range is :" + end);
		driver.getLogger().info("we are going to set the custom time right drawer->default time range");
		String regex = "(.*?)\\s+(.*)";
		Pattern p = Pattern.compile(regex);
		Matcher mStart = p.matcher(start);
		Matcher mEnd = p.matcher(end);
		String startDate = null;
		String startTime = null;
		String endDate = null;
		String endTime = null;

		if (mStart.find()) {
			if (mStart.group(1) == null || mStart.group(1).isEmpty() || mStart.group(2) == null || mStart.group(2).isEmpty()) {
				throw new Exception("the start date or the start time is empty.Please check:\n" + start);
			}
			startDate = mStart.group(1);
			startTime = mStart.group(2);
			driver.getLogger().info("start date is:" + startDate + ",start time is:" + startTime);

		}
		if (mEnd.find()) {
			if (mEnd.group(1) == null || mEnd.group(1).isEmpty() || mEnd.group(2) == null || mEnd.group(2).isEmpty()) {
				throw new Exception("the end date or the end time is empty.Please check:\n" + end);
			}
			endDate = mEnd.group(1);
			endTime = mEnd.group(2);
			driver.getLogger().info("end date is:" + endDate + ",end time is:" + endTime);
		}

		//set start date time and end date time
		driver.getLogger().info("Verify if custom panpel displayed...");
		WebDriverWait wdwait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wdwait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(TimeSelectorUIControls.sPickPanel)));
		//webd.isDisplayed(TimeSelectorUIControls.sPickPanel);
		driver.takeScreenShot();

		driver.getLogger().info("Input the start date time and end date time...");
		driver.click("css=" + TimeSelectorUIControls.sStartDateInput);
		driver.clear("css=" + TimeSelectorUIControls.sStartDateInput);
		driver.sendKeys("css=" + TimeSelectorUIControls.sStartDateInput, startDate);
		driver.click("css=" + TimeSelectorUIControls.sStartTimeInput);
		driver.clear("css=" + TimeSelectorUIControls.sStartTimeInput);
		driver.sendKeys("css=" + TimeSelectorUIControls.sStartTimeInput, startTime);
		driver.click("css=" + TimeSelectorUIControls.sEndDateInput);
		driver.clear("css=" + TimeSelectorUIControls.sEndDateInput);
		driver.sendKeys("css=" + TimeSelectorUIControls.sEndDateInput, endDate);
		driver.click("css=" + TimeSelectorUIControls.sEndTimeInput);
		driver.clear("css=" + TimeSelectorUIControls.sEndTimeInput);
		driver.sendKeys("css=" + TimeSelectorUIControls.sEndTimeInput, endTime);
		driver.takeScreenShot();
	}
	
	public static void clickApplyButton(WebDriver webd) throws Exception
	{
		//click Apply button
		webd.getLogger().info("Click Apply button");
		webd.isElementPresent("css=" + TimeSelectorUIControls.sApplyBtn);

		if (webd.getAttribute("css=" + TimeSelectorUIControls.sApplyBtn + "@disabled") != null) {
			throw new Exception("the Apply Button is disabled, can't be clicked");
		}
		else {
			webd.click("css=" + TimeSelectorUIControls.sApplyBtn);
			WaitUtil.waitForPageFullyLoaded(webd);
			webd.takeScreenShot();
		}
	}
	
	public static String dateConvert(WebDriver driver, String convertDate, TimeRange timerange) throws Exception
	{
		String timeRange = timerange.getRangeOption();
		String tmpDate = "";
		String returnStartDate = "";
		String returnEndDate = "";

		if (convertDate.startsWith(timeRange)) {
			tmpDate = convertDate.substring(timeRange.length() + 1);
		}
		else {
			tmpDate = convertDate;
		}
		System.out.println("tmpDate:" + tmpDate);
		String[] date = tmpDate.split("-");
		String StartDate = date[0].trim();
		String EndDate = date[1].trim();
		String tmpStartDate = StartDate;
		String[] tmpStart = StartDate.split(" ");

		Calendar current = Calendar.getInstance();
		Calendar today = Calendar.getInstance(); //today
		today.clear();
		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));

		Calendar yesterday = Calendar.getInstance(); //yesterday
		yesterday.clear();
		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);

		int actualTodayMonth = today.get(Calendar.MONTH) + 1;
		int actualYesterdayMonth = yesterday.get(Calendar.MONTH) + 1;

		if (StartDate.startsWith("Today")) {
			StartDate = StartDate.replace("Today",
					actualTodayMonth + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR));
			returnStartDate = TimeSelectorConverter.timeFormatChange(driver, StartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

		}
		else if (StartDate.startsWith("Yesterday")) {
			StartDate = StartDate.replace("Yesterday", actualYesterdayMonth + "/" + yesterday.get(Calendar.DAY_OF_MONTH) + "/"
					+ yesterday.get(Calendar.YEAR));
			returnStartDate = TimeSelectorConverter.timeFormatChange(driver, StartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
		}
		else {
			returnStartDate = StartDate;
		}

		if (EndDate.startsWith("Today")) {
			EndDate = EndDate.replace("Today",
					actualTodayMonth + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR));
			returnEndDate = TimeSelectorConverter.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

		}
		else if (EndDate.startsWith("Yesterday")) {
			EndDate = EndDate.replace("Yesterday", actualYesterdayMonth + "/" + yesterday.get(Calendar.DAY_OF_MONTH) + "/"
					+ yesterday.get(Calendar.YEAR));
			returnEndDate = TimeSelectorConverter.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
		}
		else if (Character.isDigit(EndDate.charAt(0))) {
			if (tmpStartDate.startsWith("Today")) {
				EndDate = actualTodayMonth + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR) + " "
						+ EndDate;
				returnEndDate = TimeSelectorConverter.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			}
			else if (tmpStartDate.startsWith("Yesterday")) {
				EndDate = actualYesterdayMonth + "/" + yesterday.get(Calendar.DAY_OF_MONTH) + "/" + yesterday.get(Calendar.YEAR)
						+ " " + EndDate;
				returnEndDate = TimeSelectorConverter.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			}
			else {
				returnEndDate = tmpStart[0] + " " + tmpStart[1] + " " + tmpStart[2] + " " + EndDate;
			}
		}
		else {
			returnEndDate = EndDate;

		}

		String[] tmpReturnStartDate = returnStartDate.split(" ");
		String[] tmpReturnEndDate = returnEndDate.split(" ");

		if (tmpReturnStartDate[3].startsWith("0")) {
			returnStartDate = tmpReturnStartDate[0] + " " + tmpReturnStartDate[1] + " " + tmpReturnStartDate[2] + " "
					+ tmpReturnStartDate[3].substring(1) + " " + tmpReturnStartDate[4];
		}
		if (tmpReturnEndDate[3].startsWith("0")) {
			returnEndDate = tmpReturnEndDate[0] + " " + tmpReturnEndDate[1] + " " + tmpReturnEndDate[2] + " "
					+ tmpReturnEndDate[3].substring(1) + " " + tmpReturnEndDate[4];
		}

		return timeRange + ": " + returnStartDate + " - " + returnEndDate;

	}
	
	private static String timeFormatChange(WebDriver driver, String testTime, String inputDateFormat, String outputDateFormat)
			throws Exception
	{
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputDateFormat);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputDateFormat);
		Date date = null;
		try {
			date = inputFormat.parse(testTime);
		}
		catch (ParseException e) {
			driver.getLogger().info(e.getLocalizedMessage());
		}

		return outputFormat.format(date);
	}

}
