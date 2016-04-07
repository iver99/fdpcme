package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.TimeSelectorTimeRange.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.TimeSelectorUIControls;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TimeSelectorUtil
{

	private static WebDriver driver;

	public static void clickApplyButton(WebDriver webd) throws Exception
	{
		//click Apply button
		webd.getLogger().info("Click Apply button");
		webd.isElementPresent(TimeSelectorUIControls.sApplyBtn);

		if (webd.getAttribute(TimeSelectorUIControls.sApplyBtn + "@disabled") != null) {
			throw new Exception("the Apply Button is disabled, can't be clicked");
		}
		else {
			webd.click(TimeSelectorUIControls.sApplyBtn);
			webd.takeScreenShot();
		}
	}

	public static void clickCancleButton(WebDriver webd) throws Exception
	{
		//click Apply button
		webd.getLogger().info("Click Apply button");
		webd.isElementPresent(TimeSelectorUIControls.sCancelBtn);
		if (webd.getAttribute(TimeSelectorUIControls.sCancelBtn + "@disabled") != null) {
			throw new Exception("the Cancel Button is disabled, can't be clicked");
		}
		else {
			webd.click(TimeSelectorUIControls.sCancelBtn);
			webd.takeScreenShot();
		}
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime) throws Exception
	{

		String start = TimeSelectorUtil.timeFormatChange(startDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		String end = TimeSelectorUtil.timeFormatChange(endDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		webd.getLogger().info("the start time in dashboard is:" + start + ",the end time in dashboard is:" + end);
		webd.getLogger().info("we are going to set the custom time in dashboard page");

		TimeSelectorUtil.clickTimePicker(webd, index);
		webd.isElementPresent(TimeSelectorUIControls.sTimeRange_Custom);
		webd.click(TimeSelectorUIControls.sTimeRange_Custom);
		webd.takeScreenShot();

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
			webd.getLogger().info("start date is:" + startDate + ",start time is:" + startTime);

		}
		if (mEnd.find()) {
			if (mEnd.group(1) == null || mEnd.group(1).isEmpty() || mEnd.group(2) == null || mEnd.group(2).isEmpty()) {
				throw new Exception("the end date or the end time is empty.Please check:\n" + end);
			}
			endDate = mEnd.group(1);
			endTime = mEnd.group(2);
			webd.getLogger().info("end date is:" + endDate + ",end time is:" + endTime);
		}

		//set start date time and end date time
		webd.getLogger().info("Verify if custom panpel displayed...");
		WebDriverWait wdwait = new WebDriverWait(webd.getWebDriver(), 900L);
		wdwait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(TimeSelectorUIControls.sPickPanel.substring(4))));
		//webd.isDisplayed(TimeSelectorUIControls.sPickPanel);
		webd.takeScreenShot();

		webd.getLogger().info("Input the start date time and end date time...");
		webd.click(TimeSelectorUIControls.sStartDateInput);
		webd.clear(TimeSelectorUIControls.sStartDateInput);
		webd.sendKeys(TimeSelectorUIControls.sStartDateInput, startDate);
		webd.click(TimeSelectorUIControls.sStartTimeInput);
		webd.clear(TimeSelectorUIControls.sStartTimeInput);
		webd.sendKeys(TimeSelectorUIControls.sStartTimeInput, startTime);
		webd.click(TimeSelectorUIControls.sEndDateInput);
		webd.clear(TimeSelectorUIControls.sEndDateInput);
		webd.sendKeys(TimeSelectorUIControls.sEndDateInput, endDate);
		webd.click(TimeSelectorUIControls.sEndTimeInput);
		webd.clear(TimeSelectorUIControls.sEndTimeInput);
		webd.sendKeys(TimeSelectorUIControls.sEndTimeInput, endTime);
		webd.takeScreenShot();

		if (webd.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
			throw new Exception(webd.getText(TimeSelectorUIControls.sErrorMsg));
		}
		else {
			TimeSelectorUtil.clickApplyButton(webd);
			if (TimeSelectorUIControls.sTimeRangeBtn.startsWith("css=")) {
				return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn.substring(4)))
						.get(index - 1).getText();
			}
			else {
				return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(index - 1)
						.getText();
			}
		}

	}

	//Date MM/dd/yyyy
	//Time hh:mm a
	public static String setCustomTime(WebDriver webd, String startDateTime, String endDateTime) throws Exception
	{
		String start = TimeSelectorUtil.timeFormatChange(startDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		String end = TimeSelectorUtil.timeFormatChange(endDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		webd.getLogger().info("the start time in dashboard is:" + start + ",the end time in dashboard is:" + end);
		webd.getLogger().info("we are going to set the custom time in dashboard page");

		TimeSelectorUtil.clickTimePicker(webd);
		webd.isElementPresent(TimeSelectorUIControls.sTimeRange_Custom);
		webd.click(TimeSelectorUIControls.sTimeRange_Custom);
		webd.takeScreenShot();

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
			webd.getLogger().info("start date is:" + startDate + ",start time is:" + startTime);

		}
		if (mEnd.find()) {
			if (mEnd.group(1) == null || mEnd.group(1).isEmpty() || mEnd.group(2) == null || mEnd.group(2).isEmpty()) {
				throw new Exception("the end date or the end time is empty.Please check:\n" + end);
			}
			endDate = mEnd.group(1);
			endTime = mEnd.group(2);
			webd.getLogger().info("end date is:" + endDate + ",end time is:" + endTime);

		}

		//set start date time and end date time
		webd.getLogger().info("Verify if custom panpel displayed...");
		webd.isDisplayed(TimeSelectorUIControls.sPickPanel);
		webd.takeScreenShot();

		webd.getLogger().info("Input the start date time and end date time...");
		webd.click(TimeSelectorUIControls.sStartDateInput);
		webd.clear(TimeSelectorUIControls.sStartDateInput);
		webd.sendKeys(TimeSelectorUIControls.sStartDateInput, startDate);
		webd.click(TimeSelectorUIControls.sStartTimeInput);
		webd.clear(TimeSelectorUIControls.sStartTimeInput);
		webd.sendKeys(TimeSelectorUIControls.sStartTimeInput, startTime);
		webd.click(TimeSelectorUIControls.sEndDateInput);
		webd.clear(TimeSelectorUIControls.sEndDateInput);
		webd.sendKeys(TimeSelectorUIControls.sEndDateInput, endDate);
		webd.click(TimeSelectorUIControls.sEndTimeInput);
		webd.clear(TimeSelectorUIControls.sEndTimeInput);
		webd.sendKeys(TimeSelectorUIControls.sEndTimeInput, endTime);
		webd.takeScreenShot();

		if (webd.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
			throw new Exception(webd.getText(TimeSelectorUIControls.sErrorMsg));
		}
		else {
			TimeSelectorUtil.clickApplyButton(webd);
			return webd.getText(TimeSelectorUIControls.sTimeRangeBtn);
		}
	}

	public static String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption) throws Exception
	{
		TimeSelectorUtil.clickTimePicker(webd, Index);
		switch (rangeoption) {
			case Last15Mins:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_15Min);
				webd.click(TimeSelectorUIControls.sTimeRange_15Min);
				webd.takeScreenShot();
				break;
			case Last30Mins:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_30Min);
				webd.click(TimeSelectorUIControls.sTimeRange_30Min);
				webd.takeScreenShot();
				break;
			case Last60Mins:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_60Min);
				webd.click(TimeSelectorUIControls.sTimeRange_60Min);
				webd.takeScreenShot();
				break;
			case Last4Hours:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_4Hour);
				webd.click(TimeSelectorUIControls.sTimeRange_4Hour);
				webd.takeScreenShot();
				break;
			case Last6Hours:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_6Hour);
				webd.click(TimeSelectorUIControls.sTimeRange_6Hour);
				webd.takeScreenShot();
				break;
			case Last1Day:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_1Day);
				webd.click(TimeSelectorUIControls.sTimeRange_1Day);
				webd.takeScreenShot();
				break;
			case Last7Days:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_7Days);
				webd.click(TimeSelectorUIControls.sTimeRange_7Days);
				webd.takeScreenShot();
				break;
			case Last30Days:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_30Days);
				webd.click(TimeSelectorUIControls.sTimeRange_30Days);
				webd.takeScreenShot();
				break;
			case Last90Days:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_90Days);
				webd.click(TimeSelectorUIControls.sTimeRange_90Days);
				webd.takeScreenShot();
				break;
			case Latest:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_Latest);
				webd.click(TimeSelectorUIControls.sTimeRange_Latest);
				webd.takeScreenShot();
				break;
			case Custom:
				//				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_Custom);
				//				webd.click(TimeSelectorUIControls.sTimeRange_Custom);
				//				webd.takeScreenShot();
				break;

		}

		if (TimeSelectorUIControls.sTimeRangeBtn.startsWith("css=")) {
			return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn.substring(4)))
					.get(Index - 1).getText();
		}
		else {
			return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(Index - 1)
					.getText();
		}
	}

	public static String setTimeRange(WebDriver webd, TimeRange rangeoption) throws Exception
	{
		TimeSelectorUtil.clickTimePicker(webd);
		switch (rangeoption) {
			case Last15Mins:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_15Min);
				webd.click(TimeSelectorUIControls.sTimeRange_15Min);
				webd.takeScreenShot();
				break;
			case Last30Mins:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_30Min);
				webd.click(TimeSelectorUIControls.sTimeRange_30Min);
				webd.takeScreenShot();
				break;
			case Last60Mins:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_60Min);
				webd.click(TimeSelectorUIControls.sTimeRange_60Min);
				webd.takeScreenShot();
				break;
			case Last4Hours:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_4Hour);
				webd.click(TimeSelectorUIControls.sTimeRange_4Hour);
				webd.takeScreenShot();
				break;
			case Last6Hours:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_6Hour);
				webd.click(TimeSelectorUIControls.sTimeRange_6Hour);
				webd.takeScreenShot();
				break;
			case Last1Day:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_1Day);
				webd.click(TimeSelectorUIControls.sTimeRange_1Day);
				webd.takeScreenShot();
				break;
			case Last7Days:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_7Days);
				webd.click(TimeSelectorUIControls.sTimeRange_7Days);
				webd.takeScreenShot();
				break;
			case Last30Days:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_30Days);
				webd.click(TimeSelectorUIControls.sTimeRange_30Days);
				webd.takeScreenShot();
				break;
			case Last90Days:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_90Days);
				webd.click(TimeSelectorUIControls.sTimeRange_90Days);
				webd.takeScreenShot();
				break;
			case Latest:
				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_Latest);
				webd.click(TimeSelectorUIControls.sTimeRange_Latest);
				webd.takeScreenShot();
				break;
			case Custom:
				//				webd.isElementPresent(TimeSelectorUIControls.sTimeRange_Custom);
				//				webd.click(TimeSelectorUIControls.sTimeRange_Custom);
				//				webd.takeScreenShot();
				break;

		}
		return webd.getText(TimeSelectorUIControls.sTimeRangeBtn);
	}

	public static String timeFormatChange(String testTime, String inputDateFormat, String outputDateFormat) throws Exception
	{
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputDateFormat);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputDateFormat);
		Date date = null;
		try {
			date = inputFormat.parse(testTime);
		}
		catch (ParseException e) {
			System.out.println("can't parse the time");

		}

		System.out.println("the output time is:" + outputFormat.format(date));
		return outputFormat.format(date);
	}

	private static void clickTimePicker(WebDriver webd) throws Exception
	{
		//click the datetimepicker component
		webd.isElementPresent(TimeSelectorUIControls.sTimeRangeBtn);
		webd.click(TimeSelectorUIControls.sTimeRangeBtn);
		webd.takeScreenShot();
	}

	//    public static String setExcludeHour(WebDriver webd, int Index, String hours) throws Exception
	//    {
	//     	//click the datetimepicker component
	//    	clickTimePicker(webd, Index);
	//
	//    	//select time range as Custom
	//    	setTimeRange(webd, Index, TimeSelectorTimeRange.CUSTOM);
	//
	//    	//click time filter icon
	//    	webd.getLogger().info("Click Time Filter Icon");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
	//		webd.takeScreenShot();
	//
	//
	//    	//set exclude hours
	//		webd.getLogger().info("Enter excluded time");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).clear();
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).sendKeys(hours);
	//		webd.takeScreenShot();
	//		//click apply button
	//		webd.getLogger().info("Click Apply button");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sApplyBtn)).click();
	//		webd.takeScreenShot();
	//
	//		return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sFilterInfo)).get(Index).getText();
	//    }
	//
	//    public static String setExcludeHour(WebDriver webd, String hours) throws Exception
	//    {
	//     	//click the datetimepicker component
	//    	clickTimePicker(webd);
	//
	//    	//select time range as Custom
	//    	setTimeRange(webd, TimeSelectorTimeRange.CUSTOM);
	//
	//    	//click time filter icon
	//    	webd.getLogger().info("Click Time Filter Icon");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
	//		webd.takeScreenShot();
	//
	//    	//set exclude hours
	//		webd.getLogger().info("Enter excluded time");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).clear();
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).sendKeys(hours);
	//		webd.takeScreenShot();
	//		//click apply button
	//		webd.getLogger().info("Click Apply button");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sApplyBtn)).click();
	//		webd.takeScreenShot();
	//
	//		return webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sFilterInfo)).getText();
	//    }
	//
	//    public static void setExcludeDay(WebDriver webd, String ExcludeDays) throws Exception
	//    {
	//    	//click the datetimepicker component
	//    	clickTimePicker(webd);
	//
	//    	//select time range as Custom
	//    	setTimeRange(webd, TimeSelectorTimeRange.CUSTOM);
	//
	//    	//click time filter icon
	//    	webd.getLogger().info("Click Time Filter Icon");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
	//		webd.takeScreenShot();
	//
	//		//select excluded days
	//		WebElement webe =webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sDayMonday));
	//		if(webe.isSelected()){
	//			webe.click();
	//		};
	//		webd.takeScreenShot();
	//    }
	//
	//    public static void setExcludeMonth(WebDriver webd, String ExcludeMonths) throws Exception
	//    {
	//    	//click the datetimepicker component
	//    	clickTimePicker(webd);
	//
	//    	//select time range as Custom
	//    	setTimeRange(webd, TimeSelectorTimeRange.CUSTOM);
	//
	//    	//click time filter icon
	//    	webd.getLogger().info("Click Time Filter Icon");
	//		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
	//		webd.takeScreenShot();
	//
	//		//select excluded months
	//		WebElement webe =webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sDayMonday));
	//		if(webe.isSelected()){
	//			webe.click();
	//		};
	//		webd.takeScreenShot();
	//    }

	private static void clickTimePicker(WebDriver webd, int Index) throws Exception
	{
		//click the datetimepicker component
		webd.waitForElementPresent(TimeSelectorUIControls.sTimeRangeBtn);
		if (TimeSelectorUIControls.sTimeRangeBtn.startsWith("css=")) {
			webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn.substring(4))).get(Index - 1)
			.click();
		}
		else {
			webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(Index - 1).click();
		}
		webd.takeScreenShot();
	}
}
