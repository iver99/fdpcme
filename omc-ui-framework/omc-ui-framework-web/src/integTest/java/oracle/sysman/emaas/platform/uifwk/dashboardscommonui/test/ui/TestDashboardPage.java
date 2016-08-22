/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WidgetSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDashboardPage extends CommonUIUtils
{
	private static final Logger logger = LogManager.getLogger(TestDashboardPage.class);
	@BeforeClass
	public static void initValue()
	{
		CommonUIUtils.getAppName(sTenantId, sSsoUserName);
		CommonUIUtils.getRoles(sTenantId, sSsoUserName);
	}

	@Test
	public void testDashboardPage_noPara() 
	{
		try {
			String testName = this.getClass().getName() + ".testDashboardPage_noPara";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
			webdriver.getLogger().info("This is to test Dashboard Page");

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, sTenantId, sSsoUserName, sSsoPassword);
			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			CommonUIUtils.verifyPageContent(webdriver, sAppName);

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			CommonUIUtils.verifyMenu(webdriver, isDSAdmin);

			//click the compass icon again
			webdriver.getLogger().info("Click the Application navigator icon again");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			webdriver.getLogger().info("Verify the Links menu disappeared");
			CommonUIUtils.verifyNoLinksMenu(webdriver);

			//Add a widget
			CommonUIUtils.addWidget(webdriver);

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);

		}
		catch (Exception ex) {
			logger.info("context",ex);
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void testDashboardPage_withPara() 
	{
		try {
			CommonUIUtils.commonUITestLog("This is to test Dashboard Page");

			String testName = this.getClass().getName() + ".testDashboardPage_withPara";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
			//CommonUIUtils.getAppName(sTenantId,sSsoUserName);
			//CommonUIUtils.getRoles(sTenantId,sSsoUserName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=Dashboard", sTenantId, sSsoUserName,
					sSsoPassword);
			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			CommonUIUtils.verifyPageContent(webdriver, sAppName);

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			CommonUIUtils.verifyMenu(webdriver, isDSAdmin);

			//click the compass icon again
			webdriver.getLogger().info("Click the Application navigator icon again");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			webdriver.getLogger().info("Verify the Links menu disappeared");
			CommonUIUtils.verifyNoLinksMenu(webdriver);

			//Add a widget
			CommonUIUtils.addWidget(webdriver);

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);

		}
		catch (Exception ex) {
			logger.info("context",ex);
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	//Testcase for adding widget using widgetselector

	@Test
	public void testWidgetSelector() 
	{
		try {
			String WidgetName_1 = "Database Errors Trend";

			String testName = this.getClass().getName() + ".testWidgetSelector";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
			webdriver.getLogger().info("This is to test Dashboard Page");

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, sTenantId, sSsoUserName, sSsoPassword);
			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			CommonUIUtils.verifyPageContent(webdriver, sAppName);

			// let's try to wait until page is loaded and jquery loaded before calling waitForPageFullyLoaded
			WebDriverWait wait = new WebDriverWait(webdriver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DashBoardPageId.WIDGETSELECTOR_ADDBUTTONID)));
			WaitUtil.waitForPageFullyLoaded(webdriver);

			//click on Add button
			webdriver.click("id=" + DashBoardPageId.WIDGETSELECTOR_ADDBUTTONID);
			webdriver.takeScreenShot();

			//Adding widgets using widgetSElector diagoue
			webdriver.getLogger().info("satrt widget selector dialogue box opens");

			//webdriver.getLogger().info("set Page # in widget selector dialog");
			//WidgetSelectorUtil.page(webdriver, 2);

			//webdriver.getLogger().info("Previous page");
			//WidgetSelectorUtil.pagingPrevious(webdriver);

			//webdriver.getLogger().info("Next page");
			//WidgetSelectorUtil.pagingNext(webdriver);

			webdriver.getLogger().info("Add widget");
			WidgetSelectorUtil.addWidget(webdriver, WidgetName_1);

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);
		}
		catch (Exception ex) {
			logger.info("context",ex);
			Assert.fail(ex.getLocalizedMessage());
		}
	}
}