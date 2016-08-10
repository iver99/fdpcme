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

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestOtherPage extends CommonUIUtils
{
	@BeforeClass
	public static void initValue()
	{
		//CommonUIUtils.getAppName(sTenantId,sSsoUserName);
		CommonUIUtils.getRoles(sTenantId, sSsoUserName);
	}

	@Test
	public void testAPMPage() throws Exception
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
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void testErrorPage() throws Exception
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
			Assert.assertTrue(webdriver.isElementPresent(UIControls.SPRODUCTTEXT));
			webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.SPRODUCTTEXT));
			Assert.assertEquals(webdriver.getText(UIControls.SPRODUCTTEXT), "Management Cloud");
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
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void testTenantPage() throws Exception
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
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	/*
	@Test
	public void testAPMPage_withAllPara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testAPMPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=APM&isAdmin=true");

		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		webdriver.getLogger().info("Verify the page content");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
		Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertEquals(webdriver.getText(UIControls.sAppText), "Application Performance Monitoring");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
		Assert.assertEquals(webdriver.getText(UIControls.sPageText),
				"Sample page for OMC UI Framework components testing only");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		//click the compass icon
		webdriver.getLogger().info("Click the Application navigator icon");
		webdriver.click(UIControls.sCompassIcon);
		Thread.sleep(5000);
		webdriver.takeScreenShot();

		//verify the menus
		webdriver.getLogger().info("Verify the Links menu displayed");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: block;");

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHome));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudService));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		Thread.sleep(10000);

		webdriver.getLogger().info("Click the Application navigator icon again");
		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Thread.sleep(5000);
		webdriver.getLogger().info("Verify the Links menu disappeared");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

		//verify the Open widget icon is disabled
		webdriver.getLogger().info("Verify the Open widget icon");
		Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
		webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
		Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");
		webdriver.getLogger().info("Verify the Open widget icon is disabled");
		webdriver.getLogger().info("The icon has been:  " + webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
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

		Thread.sleep(10000);

		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		webdriver.getLogger().info("Verify the page content");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
		Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertEquals(webdriver.getText(UIControls.sAppText), "Application Performance Monitoring");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
		Assert.assertEquals(webdriver.getText(UIControls.sPageText),
				"Sample page for OMC UI Framework components testing only");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		//click the compass icon
		webdriver.getLogger().info("Click the Application navigator icon");
		webdriver.click(UIControls.sCompassIcon);
		Thread.sleep(5000);
		webdriver.takeScreenShot();

		//verify the menus
		webdriver.getLogger().info("Verify the Links menu displayed");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: block;");

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHome));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudService));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		//		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		//		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		//		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		//		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		Thread.sleep(10000);

		webdriver.getLogger().info("Click the Application navigator icon again");
		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Thread.sleep(5000);
		webdriver.getLogger().info("Verify the Links menu disappeared");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

		//verify the Open widget icon is disabled
		webdriver.getLogger().info("Verify the Open widget icon");
		Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
		webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
		Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");
		webdriver.getLogger().info("Verify the Open widget icon is disabled");
		webdriver.getLogger().info("The icon has been:  " + webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testAPMPage_withOnePara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testAPMPage_withOnePara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=APM");
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		webdriver.getLogger().info("Verify the page content");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
		Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertEquals(webdriver.getText(UIControls.sAppText), "Application Performance Monitoring");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
		Assert.assertEquals(webdriver.getText(UIControls.sPageText),
				"Sample page for OMC UI Framework components testing only");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		//click the compass icon
		webdriver.getLogger().info("Click the Application navigator icon");
		webdriver.click(UIControls.sCompassIcon);
		Thread.sleep(5000);
		webdriver.takeScreenShot();

		//verify the menus
		webdriver.getLogger().info("Verify the Links menu displayed");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: block;");

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHome));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudService));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		Thread.sleep(10000);

		webdriver.getLogger().info("Click the Application navigator icon again");
		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Thread.sleep(5000);
		webdriver.getLogger().info("Verify the Links menu disappeared");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

		//verify the Open widget icon is disabled
		webdriver.getLogger().info("Verify the Open widget icon");
		Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
		webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
		Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");
		webdriver.getLogger().info("Verify the Open widget icon is disabled");
		webdriver.getLogger().info("The icon has been:  " + webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testErrorPage_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testErrorPage_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=Error&isAdmin=true");
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		webdriver.getLogger().info("Verify the page content");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
		Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
		//Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
		Assert.assertEquals(webdriver.getText(UIControls.sPageText),
				"Sample page for OMC UI Framework components testing only");
		//Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		//verify the Open widget icon is disabled
		webdriver.getLogger().info("Verify the Open widget icon");
		Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
		webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
		Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");
		webdriver.getLogger().info("Verify the Open widget icon is disabled");
		webdriver.getLogger().info("The icon has been:  " + webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testErrorPage_notAdmin() throws Exception
	{
		String testName = this.getClass().getName() + ".testErrorPage_notAdmin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=Error&isAdmin=false");
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		webdriver.getLogger().info("Verify the page content");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
		Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
		//Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
		Assert.assertEquals(webdriver.getText(UIControls.sPageText),
				"Sample page for OMC UI Framework components testing only");
		//Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		//verify the Open widget icon is disabled
		webdriver.getLogger().info("Verify the Open widget icon");
		Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
		webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
		Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");
		webdriver.getLogger().info("Verify the Open widget icon is disabled");
		webdriver.getLogger().info("The icon has been:  " + webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));

		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testTenantPage_withAllPara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testTenantPage_withAllPara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=TenantManagement&isAdmin=true");
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		webdriver.getLogger().info("Verify the page content");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
		Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertEquals(webdriver.getText(UIControls.sAppText), "Set up Oracle Management Cloud");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
		Assert.assertEquals(webdriver.getText(UIControls.sPageText),
				"Sample page for OMC UI Framework components testing only");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		//click the compass icon
		webdriver.getLogger().info("Click the Application navigator icon");
		webdriver.click(UIControls.sCompassIcon);
		Thread.sleep(5000);
		webdriver.takeScreenShot();

		//verify the menus
		webdriver.getLogger().info("Verify the Links menu displayed");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: block;");

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHome));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudService));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		Thread.sleep(10000);

		webdriver.getLogger().info("Click the Application navigator icon again");
		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Thread.sleep(5000);
		webdriver.getLogger().info("Verify the Links menu disappeared");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

		//verify the Open widget icon is disabled
		webdriver.getLogger().info("Verify the Open widget icon");
		Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
		webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
		Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");
		webdriver.getLogger().info("Verify the Open widget icon is disabled");
		webdriver.getLogger().info("The icon has been:  " + webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		//logout
		CommonUIUtils.logoutCommonUI(webdriver);
	}

	@Test
	public void testTenantPage_withAllPara_notAdmin() throws Exception
	{
		String testName = this.getClass().getName() + ".testTenantPage_withAllPara_notAdmin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=TenantManagement&isAdmin=false");
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		webdriver.getLogger().info("Verify the page content");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
		Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertEquals(webdriver.getText(UIControls.sAppText), "Set up Oracle Management Cloud");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
		Assert.assertEquals(webdriver.getText(UIControls.sPageText),
				"Sample page for OMC UI Framework components testing only");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		//click the compass icon
		webdriver.getLogger().info("Click the Application navigator icon");
		webdriver.click(UIControls.sCompassIcon);
		Thread.sleep(5000);
		webdriver.takeScreenShot();

		//verify the menus
		webdriver.getLogger().info("Verify the Links menu displayed");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: block;");

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHome));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudService));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		//		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		//		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		//		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		//		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		Thread.sleep(10000);

		webdriver.getLogger().info("Click the Application navigator icon again");
		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Thread.sleep(5000);
		webdriver.getLogger().info("Verify the Links menu disappeared");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

		//verify the Open widget icon is disabled
		webdriver.getLogger().info("Verify the Open widget icon");
		Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
		webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
		Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");
		webdriver.getLogger().info("Verify the Open widget icon is disabled");
		webdriver.getLogger().info("The icon has been:  " + webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		//logout
		CommonUIUtils.logoutCommonUI(webdriver);

	}

	@Test
	public void testTenantPage_withOnePara_Admin() throws Exception
	{
		String testName = this.getClass().getName() + ".testTenantPage_withOnePara_Admin";
		WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

		//login
		Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=TenantManagement");
		Thread.sleep(10000);
		webdriver.getLogger().info("Assert that common UI login was successfuly");
		Assert.assertTrue(bLoginSuccessful);

		//page loading
		webdriver.getLogger().info("Wait for the common UI page loading");
		webdriver.waitForElementPresent("toolbar-left");
		webdriver.takeScreenShot();

		//verify the product name,app name,content of page
		webdriver.getLogger().info("Verify the page content");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
		Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
		webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
		Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
		Assert.assertEquals(webdriver.getText(UIControls.sAppText), "Set up Oracle Management Cloud");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
		webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
		Assert.assertEquals(webdriver.getText(UIControls.sPageText),
				"Sample page for OMC UI Framework components testing only");
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

		//click the compass icon
		webdriver.getLogger().info("Click the Application navigator icon");
		webdriver.click(UIControls.sCompassIcon);
		Thread.sleep(5000);
		webdriver.takeScreenShot();

		//verify the menus
		webdriver.getLogger().info("Verify the Links menu displayed");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: block;");

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHome));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sHomeLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudService));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sCloudServiceLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzer));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAnalyzerLink));

		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdmin));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminIcon));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLabel));
		Assert.assertTrue(webdriver.isElementPresent(UIControls.sAdminLink));

		Thread.sleep(10000);

		webdriver.getLogger().info("Click the Application navigator icon again");
		webdriver.click(UIControls.sCompassIcon);
		webdriver.takeScreenShot();
		Thread.sleep(5000);
		webdriver.getLogger().info("Verify the Links menu disappeared");
		Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

		//verify the Open widget icon is disabled
		webdriver.getLogger().info("Verify the Open widget icon");
		Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
		webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
		Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");
		webdriver.getLogger().info("Verify the Open widget icon is disabled");
		webdriver.getLogger().info("The icon has been:  " + webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetIcon + "@disabled"));
		//logout
		CommonUIUtils.logoutCommonUI(webdriver);
	}
	 */
}
