/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboardscommonui.test.ui;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestAnalyzerPage extends CommonUIUtils
{
	@Test
	public void testLogAnalyzerPage_withAllPara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testLogAnalyzerPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=LogAnalytics&isAdmin=true");

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		Assert.assertTrue(webdriver.isTextPresent("Add", UIControls.sPageText));

		//click the compass icon

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();

		//verify the menus
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sLinksMenu));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboards));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sLinksMenu));

		//click Add Widget icon

		webdriver.click(UIControls.sAddWidgetBtn);

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();
		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();

		//Add a widget
		webdriver.select(UIControls.sWidgetSelct, UIControls.sWidgetSelct);
		webdriver.click(UIControls.sAddWidgetBtn);
		webdriver.takeScreenShot();
		webdriver.click(UIControls.sCloseWidget);
		webdriver.takeScreenShot();

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testLogAnalyzerPage_withAllPara_notAdmin() throws Exception
	{
		String testName = this.getClass().getName() + ".testLogAnalyzerPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=LogAnalytics&isAdmin=false");

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		Assert.assertTrue(webdriver.isTextPresent("Add", UIControls.sPageText));

		//click the compass icon

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();

		//verify the menus
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sLinksMenu));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboards));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertFalse(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sAdminLink));

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sLinksMenu));

		//click Add Widget icon

		webdriver.click(UIControls.sAddWidgetBtn);

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();
		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();

		//Add a widget
		webdriver.select(UIControls.sWidgetSelct, UIControls.sWidgetSelct);
		webdriver.click(UIControls.sAddWidgetBtn);
		webdriver.takeScreenShot();
		webdriver.click(UIControls.sCloseWidget);
		webdriver.takeScreenShot();

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testLogAnalyzerPage_withOnePara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testLogAnalyzerPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=LogAnalytics");

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		Assert.assertTrue(webdriver.isTextPresent("Add", UIControls.sPageText));

		//click the compass icon

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();

		//verify the menus
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sLinksMenu));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboards));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sLinksMenu));

		//click Add Widget icon

		webdriver.click(UIControls.sAddWidgetBtn);

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();
		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();

		//Add a widget
		webdriver.select(UIControls.sWidgetSelct, UIControls.sWidgetSelct);
		webdriver.click(UIControls.sAddWidgetBtn);
		webdriver.takeScreenShot();
		webdriver.click(UIControls.sCloseWidget);
		webdriver.takeScreenShot();

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testTargetAnalyzerPage_withAllPara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testLogAnalyzerPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=ITAnalytics&isAdmin=true");

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		Assert.assertTrue(webdriver.isTextPresent("Add", UIControls.sPageText));

		//click the compass icon

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();

		//verify the menus
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sLinksMenu));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboards));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sLinksMenu));

		//click Add Widget icon

		webdriver.click(UIControls.sAddWidgetBtn);

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();
		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();

		//Add a widget
		webdriver.select(UIControls.sWidgetSelct, UIControls.sWidgetSelct);
		webdriver.click(UIControls.sAddWidgetBtn);
		webdriver.takeScreenShot();
		webdriver.click(UIControls.sCloseWidget);
		webdriver.takeScreenShot();

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testTargetAnalyzerPage_withAllPara_notAdmin() throws Exception
	{
		String testName = this.getClass().getName() + ".testLogAnalyzerPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=ITAnalytics&isAdmin=false");

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		Assert.assertTrue(webdriver.isTextPresent("Add", UIControls.sPageText));

		//click the compass icon

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();

		//verify the menus
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sLinksMenu));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboards));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertFalse(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sAdminLink));

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sLinksMenu));

		//click Add Widget icon

		webdriver.click(UIControls.sAddWidgetBtn);

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();
		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();

		//Add a widget
		webdriver.select(UIControls.sWidgetSelct, UIControls.sWidgetSelct);
		webdriver.click(UIControls.sAddWidgetBtn);
		webdriver.takeScreenShot();
		webdriver.click(UIControls.sCloseWidget);
		webdriver.takeScreenShot();

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testTargetAnalyzerPage_withOnePara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testLogAnalyzerPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=ITAnalytics");

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		Assert.assertTrue(webdriver.isTextPresent("Add", UIControls.sPageText));

		//click the compass icon

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();

		//verify the menus
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sLinksMenu));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboards));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sDashboardLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sLinksMenu));

		//click Add Widget icon

		webdriver.click(UIControls.sAddWidgetBtn);

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();
		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWdigetWindowTitle));
		webdriver.takeScreenShot();

		//Add a widget
		webdriver.select(UIControls.sWidgetSelct, UIControls.sWidgetSelct);
		webdriver.click(UIControls.sAddWidgetBtn);
		webdriver.takeScreenShot();
		webdriver.click(UIControls.sCloseWidget);
		webdriver.takeScreenShot();

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

}
