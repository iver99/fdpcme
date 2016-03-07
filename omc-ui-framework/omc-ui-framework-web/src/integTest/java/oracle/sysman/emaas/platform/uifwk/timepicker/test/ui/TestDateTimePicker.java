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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * @author shangwan
 */
public class TestDateTimePicker extends CommonUIUtils
{
	private static String verifyDate(String time) throws ParseException {
		SimpleDateFormat format = new java.text.SimpleDateFormat("MMM d, yyyy hh:mm a");
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM d, yyyy");
		if(time==null ||"".equals(time)){
			return "";
		}
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar current = Calendar.getInstance();
		Calendar today = Calendar.getInstance();    //today
		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));

		today.set( Calendar.HOUR_OF_DAY, 0);
		today.set( Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance();    //yesterday

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);
		yesterday.set( Calendar.HOUR_OF_DAY, 0);
		yesterday.set( Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(date);

		if(current.after(today)){
			return "Today";
		}
		else if(current.before(today) && current.after(yesterday)){
			return "Yesterday";
		}
		else{
			//return format2.format(format1.parse(time.substring(0, 10)));
			String[] sTmpDay = time.split(",");
			//String sTemp = sTmpDay[0];
			if (sTmpDay[0].length() == 5){
				return time.substring(0,11);
			}
			else
			{
				return time.substring(0,12);
			}
			
			//int index = time.indexOf("-")+1;
			//return time.substring(index, time.length());
		}
	}
	
	private static String verifyDate_custom(String time) throws ParseException {
		SimpleDateFormat format = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM d, yyyy");
		if(time==null ||"".equals(time)){
			return "";
		}
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar current = Calendar.getInstance();
		Calendar today = Calendar.getInstance();    //today
		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));

		today.set( Calendar.HOUR_OF_DAY, 0);
		today.set( Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance();    //yesterday

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);
		yesterday.set( Calendar.HOUR_OF_DAY, 0);
		yesterday.set( Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(date);

		if(current.after(today)){
			return "Today";
		}
		else if(current.before(today) && current.after(yesterday)){
			return "Yesterday";
		}
		else{
			return format2.format(format1.parse(time.substring(0, 10)));
			//return time.substring(0,11);
			//int index = time.indexOf("-")+1;
			//return time.substring(index, time.length());
		}
	}


	public static void selectPeriod(WebDriver driver, String uicontrol, String period) throws Exception
	{
		String sDateTime = null;
		String sStartDate = null;
		String sStartTime = null;
		String sEndDate = null;
		String sEndTime = null;
		SimpleDateFormat fmt1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		SimpleDateFormat fmt2 = new SimpleDateFormat("MMM d, yyyy h:mm a");
		SimpleDateFormat fmt3 = new SimpleDateFormat("h:mm a");
		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();
		//Verify the component displayed
		Assert.assertTrue(driver.isElementPresent(UIControls.sTimeRangeBtn_1));
		Assert.assertTrue(driver.isElementPresent(UIControls.sTimeRangeBtn_2));
		driver.getLogger().info(driver.getText(UIControls.sTimeRangeBtn_1));
		String sDateTime_2 = driver.getText(UIControls.sTimeRangeBtn_2);

		//click Time Range dropdown list
		driver.getLogger().info("Click the DateTimePicker Componment");
		driver.click(UIControls.sTimeRangeBtn_1);
		driver.takeScreenShot();
//		Thread.sleep(5000);
//		driver.getLogger().info("Verify the Pick Panel displayed");
//		Assert.assertTrue(driver.isElementPresent(UIControls.sPickPanel));

		//click time period button
		Thread.sleep(5000);
		driver.getLogger().info("Click button: "+ period);
		driver.click(uicontrol);
		Thread.sleep(5000);
		driver.takeScreenShot();

		//verify the date time range is changed to 15 minutes
//		Thread.sleep(5000);
//		driver.getLogger().info("Verify the "+period+" is selected");
//		Assert.assertEquals(driver.getText(UIControls.sTimePeriod), period);

		//get the start date time and end date time after click the time period
//		driver.getLogger().info("get the start date time and end date time after click the time period");
//		sStartDate=driver.getValue(UIControls.sStartDateInput);
//		sStartTime=driver.getValue(UIControls.sStartTimeInput);
//
//		sEndDate=driver.getValue(UIControls.sEndDateInput);
//		sEndTime=driver.getValue(UIControls.sEndTimeInput);
//		driver.getLogger().info(sStartDate+" " +sStartTime+"  "+sEndDate+" "+sEndTime);
//		//convert string to datetime "MM/dd/yyyy hh:mm a"
//		driver.getLogger().info("convert string to datetime 'MM/dd/yyyy hh:mm a'");
//		dTmpStart = fmt1.parse(sStartDate+" "+sStartTime);
//		dTmpEnd = fmt1.parse(sEndDate+" "+sEndTime);
//

//		driver.getLogger().info(sTmpStartDay+" " +sTmpEndDay);
		//click Apply button
//		Thread.sleep(5000);
//		driver.getLogger().info("Click Apply button");
//		driver.click(UIControls.sApplyBtn);
		//verify the date time is set
//		Thread.sleep(5000);
//		driver.takeScreenShot();

		driver.getLogger().info("Verify the component is existed");
		Assert.assertTrue(driver.isElementPresent(UIControls.sTimeRangeBtn_1_new));

		driver.getLogger().info("Verify the result");
		//verify the result in label
//		Assert.assertEquals(fmt2.format(dTmpStart),driver.getText(UIControls.sStartText));
//		Assert.assertEquals(fmt2.format(dTmpEnd),driver.getText(UIControls.sEndText));
		driver.getLogger().info("Verify the result in label");		
		String sTmpStartDateTime = driver.getText(UIControls.sStartText);
		String sTmpEndDateTime = driver.getText(UIControls.sEndText);
		dTmpStart = fmt2.parse(sTmpStartDateTime);
		dTmpEnd = fmt2.parse(sTmpEndDateTime);
		
		driver.getLogger().info("sStartText: "+ sTmpStartDateTime);
		driver.getLogger().info("sEndText: "+ sTmpEndDateTime);
		
		long lTimeRange = dTmpEnd.getTime()-dTmpStart.getTime();
		
		//verify the time range is expected
		switch(period)
		{
			case "Last 15 minutes":
				Assert.assertEquals(lTimeRange/(60*1000),15);
				break;
			case "Last 30 minutes":
				Assert.assertEquals(lTimeRange/(60*1000),30);
				break;
			case "Last 60 minutes":
				Assert.assertEquals(lTimeRange/(60*1000),60);
				break;
			case "Last 4 hours":
				Assert.assertEquals(lTimeRange/(60*60*1000),4);
				break;
			case "Last 6 hours":
				Assert.assertEquals(lTimeRange/(60*60*1000),6);
				break;
			case "Last 1 day":
				Assert.assertEquals(lTimeRange/(24*60*60*1000),1);
				break;
			case "Last 7 days":
				Assert.assertEquals(lTimeRange/(24*60*60*1000),7);
				break;
			case "Last 30 days":
				Assert.assertEquals(lTimeRange/(24*60*60*1000),30);
				break;
			case "Last 90 day":
				Assert.assertEquals(lTimeRange/(24*60*60*1000),90);
				break;
		}
		
		//verify the result in time range
		driver.getLogger().info("Verify the result in componment");
		driver.getLogger().info(driver.getText(UIControls.sTimeRangeBtn_1_new));
		sDateTime = driver.getText(UIControls.sTimeRangeBtn_1_new);
		//verify if the date is today or yesterday
		String sTmpStartDay=TestDateTimePicker.verifyDate(sTmpStartDateTime);
		driver.getLogger().info("StartDay: "+sTmpStartDay);
		String sTmpEndDay=TestDateTimePicker.verifyDate(sTmpEndDateTime);
		driver.getLogger().info("EndDay: "+sTmpEndDay);
		//get the start time & end time
		String sTmpStartTime = fmt3.format(dTmpStart);
		driver.getLogger().info("StartTime: "+sTmpStartTime);
		String sTmpEndTime = fmt3.format(dTmpEnd);
		driver.getLogger().info("EndTime: "+sTmpEndTime);
		
		if (sTmpStartDay.equals(sTmpEndDay)&&sTmpStartDay.equals("Today")){
			Assert.assertEquals(sDateTime, period+": "+sTmpStartDay+" "+sTmpStartTime+" - "+sTmpEndTime);
		}
		else{
			Assert.assertEquals(sDateTime, period+": "+sTmpStartDay+" "+sTmpStartTime+" - "+sTmpEndDay+" "+sTmpEndTime);
		}

		//Assert.assertEquals(sDateTime, period+": "+sTmpStartDay+" "+sTmpStartTime+" - "+sTmpEndDay+" "+sTmpEndTime);		
		
//		if (sStartTime.startsWith("0")){
//			sStartTime = sStartTime.substring(1);
//			driver.getLogger().info("sStartTime: "+sStartTime);
//		}
//		if (sEndTime.startsWith("0")){
//			sEndTime = sEndTime.substring(1);
//			driver.getLogger().info("sEndTime: "+sEndTime);
//		}

//		if (sTmpStartDay.equals(sTmpEndDay)&&sTmpStartDay.equals("Today")){
//			Assert.assertEquals(sDateTime, period+": "+sTmpStartDay+" "+sStartTime+" - "+sEndTime);
//		}
//		else{
//			Assert.assertEquals(sDateTime, period+": "+sTmpStartDay+" "+sStartTime+" - "+sTmpEndDay+" "+sEndTime);
//		}

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

		TestDateTimePicker.selectPeriod(webdriver, UIControls.sLast15MinBtn, "Last 15 mins");

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

		TestDateTimePicker.selectPeriod(webdriver, UIControls.sLast30MinBtn, "Last 30 mins");

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

		TestDateTimePicker.selectPeriod(webdriver, UIControls.sLast60MinBtn, "Last 60 mins");

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

		TestDateTimePicker.selectPeriod(webdriver, UIControls.sLast4HourBtn, "Last 4 hours");

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

		TestDateTimePicker.selectPeriod(webdriver, UIControls.sLast6HourBtn, "Last 6 hours");

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

		TestDateTimePicker.selectPeriod(webdriver, UIControls.sLast1DayBtn, "Last 1 day");

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

		TestDateTimePicker.selectPeriod(webdriver, UIControls.sLast7DayBtn, "Last 7 days");

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

		TestDateTimePicker.selectPeriod(webdriver, UIControls.sLast30DayBtn, "Last 30 days");

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

		TestDateTimePicker.selectPeriod(webdriver, UIControls.sLast90DayBtn, "Last 90 days");

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
		String sStartDate = null;
		String sStartTime = null;
		String sEndDate = null;
		String sEndTime = null;
		SimpleDateFormat fmt1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		SimpleDateFormat fmt2 = new SimpleDateFormat("MMM dd, yyyy h:mm a");
		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();
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

		//set wrong start date time and end date time
		webdriver.clear(UIControls.sStartDateInput);
		webdriver.sendKeys(UIControls.sStartDateInput, "10/12/2015");
		webdriver.clear(UIControls.sStartTimeInput);
		webdriver.sendKeys(UIControls.sStartTimeInput, "09:54 AM");
		webdriver.clear(UIControls.sEndDateInput);
		webdriver.sendKeys(UIControls.sEndDateInput, "10/11/2015");
		webdriver.clear(UIControls.sEndTimeInput);
		webdriver.sendKeys(UIControls.sEndTimeInput, "09:54 AM");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sErrorMsg));
		Assert.assertEquals(webdriver.getText(UIControls.sErrorMsg),"The start date/time is after the end date/time.");

		//set start date time and end date time
		webdriver.clear(UIControls.sStartDateInput);
		webdriver.sendKeys(UIControls.sStartDateInput, "10/11/2015");
		webdriver.clear(UIControls.sStartTimeInput);
		webdriver.sendKeys(UIControls.sStartTimeInput, "09:54 AM");
		webdriver.clear(UIControls.sEndDateInput);
		webdriver.sendKeys(UIControls.sEndDateInput, "10/12/2015");
		webdriver.clear(UIControls.sEndTimeInput);
		webdriver.sendKeys(UIControls.sEndTimeInput, "09:54 AM");
		
		//verify the date time range is changed to time period
		Thread.sleep(5000);
		//webdriver.getLogger().info("Verify the Custom is selected");
		//Assert.assertEquals(webdriver.getText(UIControls.sTimePeriod), "Custom");

		//get the start date time and end date time after click the time period
		webdriver.getLogger().info("get the start date time and end date time after click the time period");
		sStartDate=webdriver.getValue(UIControls.sStartDateInput);
		sStartTime=webdriver.getValue(UIControls.sStartTimeInput);

		sEndDate=webdriver.getValue(UIControls.sEndDateInput);
		sEndTime=webdriver.getValue(UIControls.sEndTimeInput);
		webdriver.getLogger().info(sStartDate+" " +sStartTime+"  "+sEndDate+" "+sEndTime);
		//convert string to datetime "MM/dd/yyyy hh:mm a"
		webdriver.getLogger().info("convert string to datetime 'MM/dd/yyyy hh:mm a'");
		dTmpStart = fmt1.parse(sStartDate+" "+sStartTime);
		dTmpEnd = fmt1.parse(sEndDate+" "+sEndTime);

		//verify if the date is today or yesterday
		String sTmpStartDay=TestDateTimePicker.verifyDate_custom(sStartDate+" "+sStartTime);
		String sTmpEndDay=TestDateTimePicker.verifyDate_custom(sEndDate+" "+sEndTime);
		webdriver.getLogger().info(sTmpStartDay+" " +sTmpEndDay);


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
		if (sStartTime.startsWith("0")){
			sStartTime = sStartTime.substring(1);
			webdriver.getLogger().info("sStartTime: "+sStartTime);
		}
		if (sEndTime.startsWith("0")){
			sEndTime = sEndTime.substring(1);
			webdriver.getLogger().info("sEndTime: "+sEndTime);
		}

		if (sTmpStartDay.equals(sTmpEndDay)&&sTmpStartDay.equals("Today")){
			Assert.assertEquals(sDateTime, "Custom: "+sTmpStartDay+" "+sStartTime+" - "+sEndTime);
		}
		else{
			Assert.assertEquals(sDateTime, "Custom: "+sTmpStartDay+" "+sStartTime+" - "+sTmpEndDay+" "+sEndTime);
		}
		//verify the result in label
		webdriver.getLogger().info("Verify the result in label");
		webdriver.getLogger().info("dateformat: "+ fmt2.format(dTmpStart));
		webdriver.getLogger().info("sStartText: "+ webdriver.getText(UIControls.sStartText));
		Assert.assertEquals(fmt2.format(dTmpStart),webdriver.getText(UIControls.sStartText));
		Assert.assertEquals(fmt2.format(dTmpEnd),webdriver.getText(UIControls.sEndText));

		//verify the second component value is not changed
		Assert.assertEquals(sDateTime_2,webdriver.getText(UIControls.sTimeRangeBtn_2_new));

		webdriver.shutdownBrowser(true);

	}
	
	@Test
	public void testDateTimePicker_Latest() throws Exception
	{

		CommonUIUtils.commonUITestLog("This is to test DateTimeSelector Component");

		String testName = this.getClass().getName() + ".testDateTimePicker_Latest";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
		Thread.sleep(5000);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);
		
		//Verify the component displayed
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sTimeRangeBtn_1));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sTimeRangeBtn_2));
		webdriver.getLogger().info(webdriver.getText(UIControls.sTimeRangeBtn_1));
		String sDateTime_2 = webdriver.getText(UIControls.sTimeRangeBtn_2);

		//click Time Range dropdown list
		webdriver.getLogger().info("Click the DateTimePicker Componment");
		webdriver.click(UIControls.sTimeRangeBtn_1);
		webdriver.takeScreenShot();
//		Thread.sleep(5000);
//		webdriver.getLogger().info("Verify the Pick Panel displayed");
//		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPickPanel));

		//click Latest button
		Thread.sleep(5000);
		webdriver.getLogger().info("Click button: Latest");
		webdriver.click(UIControls.sLatestBtn);
		webdriver.takeScreenShot();

		//verify the date time range is changed to time period
//		Thread.sleep(5000);
//		webdriver.getLogger().info("Verify the Latest is selected");
//		Assert.assertEquals(webdriver.getText(UIControls.sTimePeriod), "Latest");		

		//click Apply button
//		Thread.sleep(5000);
//		webdriver.getLogger().info("Click Apply button");
//		webdriver.click(UIControls.sApplyBtn);
		//verify the date time is set
		Thread.sleep(5000);
		webdriver.takeScreenShot();

		webdriver.getLogger().info("Verify the component is existed");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sTimeRangeBtn_1_new));

		webdriver.getLogger().info("Verify the result");
		webdriver.getLogger().info(webdriver.getText(UIControls.sTimeRangeBtn_1_new));
		Assert.assertEquals(webdriver.getText(UIControls.sTimeRangeBtn_1_new), "Latest");
	}

}
