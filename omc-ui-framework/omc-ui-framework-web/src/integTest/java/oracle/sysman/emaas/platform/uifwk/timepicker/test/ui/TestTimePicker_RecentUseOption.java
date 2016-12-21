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

import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
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
public class TestTimePicker_RecentUseOption extends CommonUIUtils
{

	@Test
	public void testInCompactMode()
	{
		CommonUIUtils.commonUITestLog("This is to test TimePicker Component");

		String testName = this.getClass().getName() + ".testInCompactMode";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//click the time picker and verify that there is no Recent Use option
		webdriver.getLogger().info("Click TimePicker");
		clickTimePicker(webdriver, 2);
		webdriver.getLogger().info("Verify no Recent Use");
		Assert.assertFalse(verifyRecentUseExist(webdriver, 1), "Recent Use Option should not be displayed");

		clickTimePicker(webdriver, 2);

		//set time range
		webdriver.getLogger().info("set timerange as Last 7 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last7Days);

		webdriver.getLogger().info("Verify that the newly selected time range has been added into Recent Use");

		//click the time picker and verify that there is Recent Use option
		webdriver.getLogger().info("Click TimePicker");
		clickTimePicker(webdriver, 2);
		webdriver.getLogger().info("Verify has Recent Use");
		Assert.assertTrue(verifyRecentUseExist(webdriver, 1), "Recent Use Option should be displayed");
		String[] context = verifyRecentUseContent(webdriver, 1);
		Assert.assertEquals(context[0], "Last week");
		;

		//add more time range to the Recent Use and verify them
		clickTimePicker(webdriver, 2);
		webdriver.getLogger().info("set timerange as Last 2 hours");
		TimeSelectorUtil.setTimeRange(webdriver, 2, TimeRange.Last2Hours);

		webdriver.getLogger().info("Verify the context in Recent Use");
		clickTimePicker(webdriver, 2);
		context = verifyRecentUseContent(webdriver, 1);
		Assert.assertEquals(context[1], "Last week");
		Assert.assertEquals(context[0], "Last 2 hours");

		webdriver.shutdownBrowser(true);

	}

	@Test
	public void testInDateOnly()
	{
		CommonUIUtils.commonUITestLog("This is to test TimePicker Component");

		String testName = this.getClass().getName() + ".testInDateOnly";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//click the time picker and verify that there is no Recent Use option
		webdriver.getLogger().info("Click TimePicker");
		clickTimePicker(webdriver, 3);
		webdriver.getLogger().info("Verify no Recent Use");
		Assert.assertFalse(verifyRecentUseExist(webdriver, 1), "Recent Use Option should not be displayed");

		clickTimePicker(webdriver, 3);

		//set time range
		webdriver.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRangeWithDateOnly(webdriver, 3, TimeRange.Last1Year);

		webdriver.getLogger().info("Verify that the newly selected time range has been added into Recent Use");

		//click the time picker and verify that there is Recent Use option
		webdriver.getLogger().info("Click TimePicker");
		clickTimePicker(webdriver, 3);
		webdriver.getLogger().info("Verify has Recent Use");
		Assert.assertTrue(verifyRecentUseExist(webdriver, 1), "Recent Use Option should be displayed");
		String[] context = verifyRecentUseContent(webdriver, 1);
		Assert.assertEquals(context[0], "Last year");

		//add more time range to the Recent Use and verify them
		clickTimePicker(webdriver, 3);
		webdriver.getLogger().info("set timerange as Last 30 days");
		TimeSelectorUtil.setTimeRangeWithDateOnly(webdriver, 3, TimeRange.Last30Days);

		webdriver.getLogger().info("Verify the context in Recent Use");
		clickTimePicker(webdriver, 3);
		context = verifyRecentUseContent(webdriver, 1);
		Assert.assertEquals(context[1], "Last year");
		Assert.assertEquals(context[0], "Last 30 days");

		webdriver.shutdownBrowser(true);

	}

	@Test
	public void testInNormalMode()
	{
		CommonUIUtils.commonUITestLog("This is to test TimePicker Component");

		String testName = this.getClass().getName() + ".testInNormalMode";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//click the time picker and verify that there is no Recent Use option
		webdriver.getLogger().info("Click TimePicker");
		clickTimePicker(webdriver, 1);
		webdriver.getLogger().info("Verify no Recent Use");
		Assert.assertFalse(verifyRecentUseExist(webdriver, 1), "Recent Use Option should not be displayed");

		clickTimePicker(webdriver, 1);

		//set time range
		webdriver.getLogger().info("set timerange as Last 14 days");
		String returnDate = TimeSelectorUtil.setTimeRange(webdriver, TimeRange.Last14Days);

		webdriver.getLogger().info("Verify that the newly selected time range has been added into Recent Use");

		//click the time picker and verify that there is Recent Use option
		webdriver.getLogger().info("Click TimePicker");
		clickTimePicker(webdriver, 1);
		webdriver.getLogger().info("Verify has Recent Use");
		Assert.assertTrue(verifyRecentUseExist(webdriver, 1), "Recent Use Option should be displayed");
		String[] context = verifyRecentUseContent(webdriver, 1);
		Assert.assertEquals(context[0], "Last 14 days");

		//add more time range to the Recent Use and verify them
		clickTimePicker(webdriver, 1);
		webdriver.getLogger().info("set timerange as Last 15 minutes");
		TimeSelectorUtil.setTimeRange(webdriver, 1, TimeRange.Last15Mins);

		webdriver.getLogger().info("Verify the context in Recent Use");
		clickTimePicker(webdriver, 1);
		context = verifyRecentUseContent(webdriver, 1);
		Assert.assertEquals(context[1], "Last 14 days");
		Assert.assertEquals(context[0], "Last 15 mins");

		webdriver.shutdownBrowser(true);
	}

	private void clickTimePicker(WebDriver webd, int Index)
	{
		//click the datetimepicker component
		webd.waitForElementPresent("css=" + UIControls.TIMERANGEBTN_CSS);
		webd.getWebDriver().findElements(By.cssSelector(UIControls.TIMERANGEBTN_CSS)).get(Index - 1).click();

		webd.takeScreenShot();
	}

	private String[] verifyRecentUseContent(WebDriver webd, int Index)
	{
		//click Recent Use option
		webd.getLogger().info("Click Recent Use Option");
		webd.getWebDriver().findElements(By.cssSelector(UIControls.RECENTUSE_CSS)).get(Index - 1).click();
		webd.takeScreenShot();

		//check the expand options
		webd.getLogger().info("Get the context");
		webd.waitForElementPresent("css=" + UIControls.RECENTUSECONTEXT_CSS);
		String context1 = webd.getElement("css=" + UIControls.RECENTUSECONTEXT_CSS).getText();

		context1 = context1.replaceAll("[\\r\\n]", ";");

		if (context1.contains(";")) {
			String[] tmp_context = context1.split(";");
			return tmp_context;
		}
		else {
			String[] tmp_context1 = { context1 };
			return tmp_context1;
		}

	}

	private boolean verifyRecentUseExist(WebDriver webd, int Index)
	{
		//verify Recent Use option is displayed or not
		return webd.getWebDriver().findElements(By.cssSelector(UIControls.RECENTUSE_CSS)).get(Index - 1).isDisplayed();
	}

}
