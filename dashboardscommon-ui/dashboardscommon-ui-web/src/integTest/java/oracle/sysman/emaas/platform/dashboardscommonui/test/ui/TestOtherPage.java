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
public class TestOtherPage extends CommonUIUtils
{

	@Test
	public void testAPMPage_withAllPara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testAPMPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=APM&isAdmin=true");

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

		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sPageText));

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

		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testAPMPage_withAllPara_notAdmin() throws Exception
	{
		String testName = this.getClass().getName() + ".testAPMPage_withAllPara_notAdmin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=APM&isAdmin=false");

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

		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sPageText));

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

		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testErrorPage() throws Exception
	{
		String testName = this.getClass().getName() + ".testErrorPage";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=Error&isAdmin=true");

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		Assert.assertFalse(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));
		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sPageText));

		//click Add Widget icon

		webdriver.click(UIControls.sAddWidgetBtn);

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testTenantPage_withAllPara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testTenantPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=APM&isAdmin=true");

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

		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sPageText));

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

		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testTenantPage_withAllPara_notAdmin() throws Exception
	{
		String testName = this.getClass().getName() + ".testTenantPage_withAllPara_notAdmin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=APM&isAdmin=false");

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

		Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sPageText));

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

		webdriver.takeScreenShot();

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}
}
