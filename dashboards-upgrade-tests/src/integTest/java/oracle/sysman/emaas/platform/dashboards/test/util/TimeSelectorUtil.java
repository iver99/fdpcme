package oracle.sysman.emaas.platform.dashboards.test.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import oracle.sysman.emaas.platform.dashboards.test.util.TimeSelectorUIControls;
import oracle.sysman.emaas.platform.dashboards.test.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class TimeSelectorUtil
{
	public enum TimeRange
	{
		Last15Mins("Last 15 mins"), Last30Mins("Last 30 mins"), Last60Mins("Last 60 mins"), Last4Hours(
				"Last 4 hours"), Last6Hours("Last 6 hours"), Last1Day("Last 1 day"), Last7Days("Last 7 days"), Last30Days(
						"Last 30 days"), Last90Days("Last 90 days"), Latest("Latest"), Custom("Custom");

		private final String timerange;

		private TimeRange(String timerange)
		{
			this.timerange = timerange;
		}

		public String getRangeOption()
		{
			return timerange;
		}

	}

	public static String setCustomTime(WebDriver webd, int index, String startDateTime, String endDateTime) throws Exception
	{

		String start = TimeSelectorUtil.timeFormatChange(webd, startDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		String end = TimeSelectorUtil.timeFormatChange(webd, endDateTime, "MM/dd/yy hh:mm a", "MM/dd/yyyy hh:mm a");
		webd.getLogger().info("the start time in dashboard is:" + start + ",the end time in dashboard is:" + end);
		webd.getLogger().info("we are going to set the custom time in dashboard page");

		TimeSelectorUtil.clickTimePicker(webd, index);
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);
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
		WebDriverWait wdwait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wdwait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(TimeSelectorUIControls.sPickPanel)));
		//webd.isDisplayed(TimeSelectorUIControls.sPickPanel);
		webd.takeScreenShot();

		webd.getLogger().info("Input the start date time and end date time...");
		webd.click("css=" + TimeSelectorUIControls.sStartDateInput);
		webd.clear("css=" + TimeSelectorUIControls.sStartDateInput);
		webd.sendKeys("css=" + TimeSelectorUIControls.sStartDateInput, startDate);
		webd.click("css=" + TimeSelectorUIControls.sStartTimeInput);
		webd.clear("css=" + TimeSelectorUIControls.sStartTimeInput);
		webd.sendKeys("css=" + TimeSelectorUIControls.sStartTimeInput, startTime);
		webd.click("css=" + TimeSelectorUIControls.sEndDateInput);
		webd.clear("css=" + TimeSelectorUIControls.sEndDateInput);
		webd.sendKeys("css=" + TimeSelectorUIControls.sEndDateInput, endDate);
		webd.click("css=" + TimeSelectorUIControls.sEndTimeInput);
		webd.clear("css=" + TimeSelectorUIControls.sEndTimeInput);
		webd.sendKeys("css=" + TimeSelectorUIControls.sEndTimeInput, endTime);
		webd.takeScreenShot();

		if (webd.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
			throw new Exception(webd.getText(TimeSelectorUIControls.sErrorMsg));
		}
		else {
			TimeSelectorUtil.clickApplyButton(webd);
			String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
					.get(index - 1).getText();
			return TimeSelectorUtil.dateConvert(webd, returnTimeRange, TimeRange.Custom);
		}

	}

	//Date MM/dd/yyyy
	//Time hh:mm a
	public static String setCustomTime(WebDriver webd, String startDateTime, String endDateTime) throws Exception
	{
		return TimeSelectorUtil.setCustomTime(webd, 1, startDateTime, endDateTime);
	}

	public static String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption) throws Exception
	{
		TimeSelectorUtil.clickTimePicker(webd, Index);
		switch (rangeoption) {
			case Last15Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_15Min);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_15Min);
				webd.takeScreenShot();
				break;
			case Last30Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_30Min);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_30Min);
				webd.takeScreenShot();
				break;
			case Last60Mins:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_60Min);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_60Min);
				webd.takeScreenShot();
				break;
			case Last4Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_4Hour);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_4Hour);
				webd.takeScreenShot();
				break;
			case Last6Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_6Hour);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_6Hour);
				webd.takeScreenShot();
				break;
			case Last1Day:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_1Day);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_1Day);
				webd.takeScreenShot();
				break;
			case Last7Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_7Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_7Days);
				webd.takeScreenShot();
				break;
			case Last30Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_30Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_30Days);
				webd.takeScreenShot();
				break;
			case Last90Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_90Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_90Days);
				webd.takeScreenShot();
				break;
			case Latest:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Latest);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_Latest);
				webd.takeScreenShot();
				return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(Index - 1)
						.getText();
			case Custom:
				throw new Exception("Please use setCustomTime API to set Custom Range");

		}
		String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(Index - 1).getText();
		return TimeSelectorUtil.dateConvert(webd, returnTimeRange, rangeoption);

	}

	public static String setTimeRange(WebDriver webd, TimeRange rangeoption) throws Exception
	{
		return TimeSelectorUtil.setTimeRange(webd, 1, rangeoption);
	}

	private static void clickApplyButton(WebDriver webd) throws Exception
	{
		//click Apply button
		webd.getLogger().info("Click Apply button");
		webd.isElementPresent("css=" + TimeSelectorUIControls.sApplyBtn);

		if (webd.getAttribute("css=" + TimeSelectorUIControls.sApplyBtn + "@disabled") != null) {
			throw new Exception("the Apply Button is disabled, can't be clicked");
		}
		else {
			webd.click("css=" + TimeSelectorUIControls.sApplyBtn);
			webd.takeScreenShot();
		}
	}

	private static void clickCancleButton(WebDriver webd) throws Exception
	{
		//click Calcel button
		webd.getLogger().info("Click Calcel button");
		webd.isElementPresent("css=" + TimeSelectorUIControls.sCancelBtn);
		if (webd.getAttribute("css=" + TimeSelectorUIControls.sCancelBtn + "@disabled") != null) {
			throw new Exception("the Cancel Button is disabled, can't be clicked");
		}
		else {
			webd.click("css=" + TimeSelectorUIControls.sCancelBtn);
			webd.takeScreenShot();
		}
	}

	private static void clickTimePicker(WebDriver webd) throws Exception
	{
		//click the datetimepicker component
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRangeBtn);
		webd.click("css=" + TimeSelectorUIControls.sTimeRangeBtn);
		webd.takeScreenShot();
	}

	private static void clickTimePicker(WebDriver webd, int Index) throws Exception
	{
		//click the datetimepicker component
		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sTimeRangeBtn);
		webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(Index - 1).click();

		webd.takeScreenShot();
	}

	private static String dateConvert(WebDriver driver, String convertDate, TimeRange timerange) throws Exception
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
			returnStartDate = TimeSelectorUtil.timeFormatChange(driver, StartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

		}
		else if (StartDate.startsWith("Yesterday")) {
			StartDate = StartDate.replace("Yesterday",
					actualYesterdayMonth + "/" + yesterday.get(Calendar.DAY_OF_MONTH) + "/" + yesterday.get(Calendar.YEAR));
			returnStartDate = TimeSelectorUtil.timeFormatChange(driver, StartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
		}
		else {
			returnStartDate = StartDate;
		}

		if (EndDate.startsWith("Today")) {
			EndDate = EndDate.replace("Today",
					actualTodayMonth + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR));
			returnEndDate = TimeSelectorUtil.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

		}
		else if (EndDate.startsWith("Yesterday")) {
			EndDate = EndDate.replace("Yesterday",
					actualYesterdayMonth + "/" + yesterday.get(Calendar.DAY_OF_MONTH) + "/" + yesterday.get(Calendar.YEAR));
			returnEndDate = TimeSelectorUtil.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
		}
		else if (Character.isDigit(EndDate.charAt(0))) {
			if (tmpStartDate.startsWith("Today")) {
				EndDate = actualTodayMonth + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR) + " "
						+ EndDate;
				returnEndDate = TimeSelectorUtil.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			}
			else if (tmpStartDate.startsWith("Yesterday")) {
				EndDate = actualYesterdayMonth + "/" + yesterday.get(Calendar.DAY_OF_MONTH) + "/" + yesterday.get(Calendar.YEAR)
						+ " " + EndDate;
				returnEndDate = TimeSelectorUtil.timeFormatChange(driver, EndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
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

