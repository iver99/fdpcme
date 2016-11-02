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
public class TestOtherPage extends CommonUIUtils
{

	private static final Logger LOGGER = LogManager.getLogger(TestOtherPage.class);

	@BeforeClass
	public static void initValue()
	{
		//CommonUIUtils.getAppName(sTenantId,sSsoUserName);
		CommonUIUtils.getRoles(sTenantId, sSsoUserName);
	}

	@Test
	public void testAPMPage()
	{
		try {
			CommonUIUtils.commonUITestLog("This is to test APM Page");

			String testName = this.getClass().getName() + ".testAPMPage";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
			//CommonUIUtils.getAppName(sTenantId,sSsoUserName);
			//CommonUIUtils.getRoles(sTenantId,sSsoUserName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils
					.loginCommonUI(webdriver, "?appId=APM", sTenantId, sSsoUserName, sSsoPassword);
			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			CommonUIUtils.verifyPageContent(webdriver, "Application Performance Monitoring");

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			CommonUIUtils.verifyMenu(webdriver, isAPMAdmin);

			//click the compass icon again
			webdriver.getLogger().info("Click the Application navigator icon again");
			webdriver.waitForElementPresent(UIControls.SCOMPASSICON);
			webdriver.click(UIControls.SCOMPASSICON);
			webdriver.takeScreenShot();

			webdriver.getLogger().info("Verify the Links menu disappeared");
			CommonUIUtils.verifyNoLinksMenu(webdriver);

			//Open a widget
			CommonUIUtils.openWidget(webdriver, false);

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
	public void testErrorPage()
	{
		try {
			String testName = this.getClass().getName() + ".testErrorPage";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=Error", sTenantId, sSsoUserName,
					sSsoPassword);
			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			//page loading
			webdriver.getLogger().info("Wait for the common UI page loading");
			webdriver.waitForElementPresent("toolbar-left");
			webdriver.takeScreenShot();

			//verify the product name,app name,content of page
			webdriver.getLogger().info("Verify the page content");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.SORACLEIMAGE));
			Assert.assertEquals(webdriver.getAttribute(UIControls.SORACLEIMAGE + "@alt"), "Oracle");
			Assert.assertTrue(webdriver.isElementPresent("css=" + UIControls.SPRODUCTTEXT_CSS));
			webdriver.getLogger().info("The Product is:  " + webdriver.getText("css=" + UIControls.SPRODUCTTEXT_CSS));
			Assert.assertEquals(webdriver.getText("css=" + UIControls.SPRODUCTTEXT_CSS), "Management Cloud");
			//Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
			Assert.assertTrue(webdriver.isElementPresent(UIControls.SPAGETEXT));
			webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.SPAGETEXT));
			Assert.assertEquals(webdriver.getText(UIControls.SPAGETEXT),
					"Sample page for OMC UI Framework components testing only");
			//Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
			Assert.assertTrue(webdriver.isElementPresent(UIControls.SADDWIDGETICON));

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

			//Open a widget
			CommonUIUtils.openWidget(webdriver, false);

			//logout
			CommonUIUtils.logoutCommonUI(webdriver);
		}
		catch (Exception ex) {
			LOGGER.info("context", ex);
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void testTenantPage()
	{
		try {
			CommonUIUtils.commonUITestLog("This is to test Tenant Manager Page");

			String testName = this.getClass().getName() + ".testTenantPage";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
			//CommonUIUtils.getAppName(sTenantId,sSsoUserName);
			//CommonUIUtils.getRoles(sTenantId,sSsoUserName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=TenantManagement", sTenantId, sSsoUserName,
					sSsoPassword);
			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			CommonUIUtils.verifyPageContent(webdriver, "Set up Oracle Management Cloud");

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

			//Open a widget
			CommonUIUtils.openWidget(webdriver, false);

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
