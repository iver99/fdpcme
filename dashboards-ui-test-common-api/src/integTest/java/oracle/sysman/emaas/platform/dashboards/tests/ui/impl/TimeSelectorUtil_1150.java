/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.TimeSelectorUIControls;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TimeSelectorUtil_1150 extends TimeSelectorUtil_1130
{
	@Override
	public String setFlexibleRelativeTimeRange(WebDriver driver, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		//open time selector
		clickTimePicker(driver, index);

		//click "Custom" option to open panel
		driver.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		driver.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		driver.takeScreenShot();

		driver.waitForElementPresent("css=" + TimeSelectorUIControls.sLastRadio);
		driver.click("css=" + TimeSelectorUIControls.sLastRadio);
		driver.takeScreenShot();

		driver.waitForElementPresent("css=" + TimeSelectorUIControls.sFlexRelTimeVal);
		driver.getElement("css=" + TimeSelectorUIControls.sFlexRelTimeVal).clear();
		driver.click("css=" + TimeSelectorUIControls.sFlexRelTimeVal);
		driver.sendKeys("css=" + TimeSelectorUIControls.sFlexRelTimeVal, String.valueOf(relTimeVal));

		driver.waitForElementPresent("css=" + TimeSelectorUIControls.sFlexRelTimeOpt);
		driver.click("css=" + TimeSelectorUIControls.sFlexRelTimeOpt);
		String optionLocator = getOptionsLocator(driver, relTimeUnit.getTimeUnit());
		driver.click("css=" + optionLocator);
		driver.takeScreenShot();

		if (driver.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
			try {
				throw new Exception(driver.getText(TimeSelectorUIControls.sErrorMsg));
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		else {
			try {
				clickApplyButton(driver);
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String returnTimeRange = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
					.get(index - 1).getText();

			if (returnTimeRange.startsWith("Last") && returnTimeRange.indexOf(":") > -1) {
				return dateConvert(driver, returnTimeRange, TimeRange.Custom, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			}
			else {
				String returnStartDate = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
						.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sStartDateInput)).getAttribute("value") 
						+ " "
						+ driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
						.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).getAttribute("value");
				String returnEndDate = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
						.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sEndDateInput)).getAttribute("value") 
						+ " "
						+ driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
						.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).getAttribute("value"); 

				returnStartDate = timeFormatChange(driver, returnStartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
				returnEndDate = timeFormatChange(driver, returnEndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

				String returnDate = returnTimeRange + ": " + returnStartDate + " - " + returnEndDate;

				return dateConvert(driver, returnDate, TimeRange.Custom, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			}
		}
	}

	@Override
	public String setFlexibleRelativeTimeRange(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		return setFlexibleRelativeTimeRange(webd, 1, relTimeVal, relTimeUnit);
	}

	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int index, int relTimeVal, TimeUnit relTimeUnit)
	{
		webd.getLogger().info("Start to setFlexibleRelativeTimeRangeWithDateOnly...");

		Validator.fromValidValues("relTimeUnit", relTimeUnit, TimeUnit.Day, TimeUnit.Week, TimeUnit.Month, TimeUnit.Year);

		// open time selector
		clickTimePicker(webd, index);

		// click "Custom" option to open panel
		webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.click("css=" + TimeSelectorUIControls.sTimeRange_Custom);
		webd.takeScreenShot();

		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sLastRadio);
		webd.click("css=" + TimeSelectorUIControls.sLastRadio);
		webd.takeScreenShot();

		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sFlexRelTimeVal);
		webd.getElement("css=" + TimeSelectorUIControls.sFlexRelTimeVal).clear();
		webd.click("css=" + TimeSelectorUIControls.sFlexRelTimeVal);
		webd.sendKeys("css=" + TimeSelectorUIControls.sFlexRelTimeVal, String.valueOf(relTimeVal));

		webd.waitForElementPresent("css=" + TimeSelectorUIControls.sFlexRelTimeOpt);
		webd.click("css=" + TimeSelectorUIControls.sFlexRelTimeOpt);
		String optionLocator = getOptionsLocator(webd, relTimeUnit.getTimeUnit());
		webd.click("css=" + optionLocator);
		webd.takeScreenShot();

		if (webd.isDisplayed(TimeSelectorUIControls.sErrorMsg)) {
			try {
				throw new Exception(webd.getText(TimeSelectorUIControls.sErrorMsg));
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		else {
			try {
				clickApplyButton(webd);
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
					.get(index - 1).getText();

			if (returnTimeRange.startsWith("Last") && returnTimeRange.indexOf(":") > -1) {
				return dateConvert(webd, returnTimeRange, TimeRange.Custom, "MM/dd/yyyy", "MMM d, yyyy");
			}
			else {
				String returnStartDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
						.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sStartDateInput)).getAttribute("value") 
						+ " "
						+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
						.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).getAttribute("value");
				String returnEndDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
						.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sEndDateInput)).getAttribute("value") 
						+ " "
						+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
						.get(index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).getAttribute("value"); 

				returnStartDate = timeFormatChange(webd, returnStartDate, "MM/dd/yyyy", "MMM d, yyyy");
				returnEndDate = timeFormatChange(webd, returnEndDate, "MM/dd/yyyy", "MMM d, yyyy");

				String returnDate = returnTimeRange + ": " + returnStartDate + " - " + returnEndDate;

				return dateConvert(webd, returnDate, TimeRange.Custom, "MM/dd/yyyy", "MMM d, yyyy");
			}
		}
	}

	@Override
	public String setFlexibleRelativeTimeRangeWithDateOnly(WebDriver webd, int relTimeVal, TimeUnit relTimeUnit)
	{
		return setFlexibleRelativeTimeRangeWithDateOnly(webd, 1, relTimeVal, relTimeUnit);
	}

	@Override
	public String setTimeRange(WebDriver webd, int Index, TimeRange rangeoption)
	{
		clickTimePicker(webd, Index);
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
			case Last2Hours:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_2Hour);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_2Hour);
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
			case Last14Days:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_14Days);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_14Days);
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
			case Last1Year:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_1Year);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_1Year);
				webd.takeScreenShot();
				break;
			case Latest:
				webd.isElementPresent("css=" + TimeSelectorUIControls.sTimeRange_Latest);
				webd.click("css=" + TimeSelectorUIControls.sTimeRange_Latest);
				webd.takeScreenShot();
				return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(Index - 1)
						.getText();
			case Custom:
				try {
					throw new Exception("Please use setCustomTime API to set Custom Range");
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			default:
				break;

		}
		String returnTimeRange = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn))
				.get(Index - 1).getText();

		if (returnTimeRange.startsWith(rangeoption.getRangeOption() + ":")) {
			return dateConvert(webd, returnTimeRange, rangeoption, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
		}
		else {			
			String returnStartDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
					.get(Index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sStartDateInput)).getAttribute("value") 
					+ " "
					+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
					.get(Index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).getAttribute("value");
			String returnEndDate = webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
					.get(Index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sEndDateInput)).getAttribute("value") 
					+ " "
					+ webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDateTimePick))
					.get(Index - 1).findElement(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).getAttribute("value"); 
			

			returnStartDate = timeFormatChange(webd, returnStartDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
			returnEndDate = timeFormatChange(webd, returnEndDate, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");

			String returnDate = returnTimeRange + ": " + returnStartDate + " - " + returnEndDate;
			return dateConvert(webd, returnDate, rangeoption, "MM/dd/yyyy hh:mm a", "MMM d, yyyy hh:mm a");
		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil#setTimeRange(oracle.sysman.qatool.uifwk.webdriver.WebDriver, oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange)
	 */
	@Override
	public String setTimeRange(WebDriver webd, TimeRange rangeoption)
	{
		return setTimeRange(webd, 1, rangeoption);
	}

	private String getOptionsLocator(WebDriver driver, String option)
	{
		String optionLocator = TimeSelectorUIControls.sFlexRelTimeOptList + "[value='" + option + "']";
		WebElement li = driver.getWebDriver().findElement(By.cssSelector(optionLocator));
		List<WebElement> list = driver.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sFlexRelTimeOptList));
		int index = list.indexOf(li);

		return TimeSelectorUIControls.sFlexRelTimeOptStart + (index + 1) + TimeSelectorUIControls.sFlexRelTimeOptEnd;
	}
}
