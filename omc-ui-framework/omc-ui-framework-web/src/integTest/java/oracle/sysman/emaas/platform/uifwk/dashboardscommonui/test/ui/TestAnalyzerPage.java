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

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestAnalyzerPage extends CommonUIUtils
{
	private static final Logger LOGGER = LogManager.getLogger(TestAnalyzerPage.class);

	@BeforeClass
	public static void initValue()
	{
		//CommonUIUtils.getAppName(sTenantId,sSsoUserName);
		CommonUIUtils.getRoles(sTenantId, sSsoUserName);
	}

	@Test
	public void testLogAnalyzerPage()
	{
		try {
			CommonUIUtils.commonUITestLog("This is to test Log Analyzer Page");

			String testName = this.getClass().getName() + ".testLogAnalyzerPage";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
			//CommonUIUtils.getAppName(sTenantId,sSsoUserName);
			//CommonUIUtils.getRoles(sTenantId,sSsoUserName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=LogAnalytics", sTenantId, sSsoUserName,
					sSsoPassword);
			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			CommonUIUtils.verifyPageContent(webdriver, "Log Analytics");

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			CommonUIUtils.verifyMenu(webdriver, isLAAdmin);

			//click the compass icon again
			webdriver.getLogger().info("Click the Application navigator icon again");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			webdriver.getLogger().info("Verify the Links menu disappeared");
			CommonUIUtils.verifyNoLinksMenu(webdriver);

			//Open a widget
			CommonUIUtils.openWidget(webdriver, true);

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);

		}
		catch (Exception ex) {
			LOGGER.info("context", ex);
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void testTargetAnalyzerPage()
	{
		try {
			CommonUIUtils.commonUITestLog("This is to test Target Analyzer Page");

			String testName = this.getClass().getName() + ".testTargetAnalyzerPage";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
			//CommonUIUtils.getAppName(sTenantId,sSsoUserName);
			//CommonUIUtils.getRoles(sTenantId,sSsoUserName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=ITAnalytics", sTenantId, sSsoUserName,
					sSsoPassword);
			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			CommonUIUtils.verifyPageContent(webdriver, "IT Analytics");

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			CommonUIUtils.verifyMenu(webdriver, isITAAdmin);

			//click the compass icon again
			webdriver.getLogger().info("Click the Application navigator icon again");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			webdriver.getLogger().info("Verify the Links menu disappeared");
			CommonUIUtils.verifyNoLinksMenu(webdriver);

			//Open a widget
			CommonUIUtils.openWidget(webdriver, true);

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);

		}
		catch (Exception ex) {
			LOGGER.info("context", ex);
			Assert.fail(ex.getLocalizedMessage());
		}
	}
}
