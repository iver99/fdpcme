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
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestAnalyzerPage extends CommonUIUtils
{
	private static final Logger logger = LogManager.getLogger(TestAnalyzerPage.class);
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
			logger.info("context",ex);
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
			logger.info("context",ex);
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	/*
	@Test
	public void testLogAnalyzerPage_withAllPara_Admin() throws Exception
	{
		try {
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
			Thread.sleep(10000);
			//verify the product name,app name,content of page
			webdriver.getLogger().info("Verify the page content");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
			Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
			webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
			Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
			webdriver.getLogger().info("The App is:  " + webdriver.getText(UIControls.sAppText));
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), "Log Analytics");
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
			webdriver.getLogger().info("The Link menu is:  " + webdriver.getAttribute(UIControls.sLinksMenu + "@style"));
			Assert.assertNotEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

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
			Thread.sleep(5000);
			webdriver.takeScreenShot();

			webdriver.getLogger().info("Verify the Links menu disappeared");
			Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

			//click Open Widget icon
			webdriver.getLogger().info("Verify if Open Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");

			webdriver.getLogger().info("Click the Open icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(10000);

			webdriver.getLogger().info("Verify the Open Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Open button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Open");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			webdriver.getLogger().info("Verify the select category drop-down list in Add Widgets button is displayed");
			try {
				webdriver.getLogger().info("the category display is: " + webdriver.isDisplayed(UIControls.sCategorySelect));
			}
			catch (RuntimeException re) {
				Assert.fail(re.getLocalizedMessage());
			}
			//Assert.assertFalse(webdriver.isElementPresent(UIControls.sCategorySelect));

			//Open a widget
			webdriver.getLogger().info("Select a widget and open it in the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(5000);
			webdriver.getLogger().info("Click Open button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the widget has been opened in main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);
		}
		catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}

	}

		@Test
		public void testLogAnalyzerPage_withAllPara_notAdmin() throws Exception
		{
			String testName = this.getClass().getName() + ".testLogAnalyzerPage_withAllPara_notAdmin";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=LogAnalytics&isAdmin=false");

			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			//page loading
			webdriver.getLogger().info("Wait for the common UI page loading");
			webdriver.waitForElementPresent("toolbar-left");
			webdriver.takeScreenShot();
			Thread.sleep(10000);
			//verify the product name,app name,content of page
			webdriver.getLogger().info("Verify the page content");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
			Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
			webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
			Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
			webdriver.getLogger().info("The App is:  " + webdriver.getText(UIControls.sAppText));
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), "Log Analytics");
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

			//click Open Widget icon
			webdriver.getLogger().info("Verify if Open Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");

			webdriver.getLogger().info("Click the Open icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the Open Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Open button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Open");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));

			//Add a widget
			webdriver.getLogger().info("Select a widget and open it in the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(5000);
			webdriver.getLogger().info("Click Open button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the widget has been opened in main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);

		}

		@Test
		public void testLogAnalyzerPage_withOnePara_Admin() throws Exception
		{
			String testName = this.getClass().getName() + ".testLogAnalyzerPage_withOnePara_Admin";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=LogAnalytics");

			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			//page loading
			webdriver.getLogger().info("Wait for the common UI page loading");
			webdriver.waitForElementPresent("toolbar-left");
			webdriver.takeScreenShot();
			Thread.sleep(10000);
			//verify the product name,app name,content of page
			webdriver.getLogger().info("Verify the page content");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
			Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
			webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
			Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
			webdriver.getLogger().info("The App is:  " + webdriver.getText(UIControls.sAppText));
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), "Log Analytics");
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

			//click Open Widget icon
			webdriver.getLogger().info("Verify if Open Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");

			webdriver.getLogger().info("Click the Open icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the Open Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Open button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Open");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));

			//Open a widget
			webdriver.getLogger().info("Select a widget and open it in the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(5000);
			webdriver.getLogger().info("Click Open button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the widget has been opened in main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);

		}

		@Test
		public void testTargetAnalyzerPage_withAllPara_Admin() throws Exception
		{
			String testName = this.getClass().getName() + ".testTargetAnalyzerPage_withAllPara_Admin";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=ITAnalytics&isAdmin=true");

			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			//page loading
			webdriver.getLogger().info("Wait for the common UI page loading");
			webdriver.waitForElementPresent("toolbar-left");
			webdriver.takeScreenShot();
			Thread.sleep(10000);
			//verify the product name,app name,content of page
			webdriver.getLogger().info("Verify the page content");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
			Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
			webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
			Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
			webdriver.getLogger().info("The App is:  " + webdriver.getText(UIControls.sAppText));
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), "IT Analytics");
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

			//click Open Widget icon
			webdriver.getLogger().info("Verify if Open Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");

			webdriver.getLogger().info("Click the Open icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the Open Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Open button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Open");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));

			//Open a widget
			webdriver.getLogger().info("Select a widget and open it in the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(5000);
			webdriver.getLogger().info("Click Open button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the widget has been opened in main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);

		}

		@Test
		public void testTargetAnalyzerPage_withAllPara_notAdmin() throws Exception
		{
			String testName = this.getClass().getName() + ".testTargetAnalyzerPage_withAllPara_notAdmin";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=ITAnalytics&isAdmin=false");

			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			//page loading
			webdriver.getLogger().info("Wait for the common UI page loading");
			webdriver.waitForElementPresent("toolbar-left");
			webdriver.takeScreenShot();
			Thread.sleep(10000);
			//verify the product name,app name,content of page
			webdriver.getLogger().info("Verify the page content");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
			Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
			webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
			Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
			webdriver.getLogger().info("The App is:  " + webdriver.getText(UIControls.sAppText));
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), "IT Analytics");
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

			//click Open Widget icon
			webdriver.getLogger().info("Verify if Open Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");

			webdriver.getLogger().info("Click the Open icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the Open Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Open button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Open");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));

			//Open a widget
			webdriver.getLogger().info("Select a widget and open it in the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(5000);
			webdriver.getLogger().info("Click Open button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the widget has been opened in main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);

		}

		@Test
		public void testTargetAnalyzerPage_withOnePara_Admin() throws Exception
		{
			String testName = this.getClass().getName() + ".testTargetAnalyzerPage_withOnePara_Admin";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=ITAnalytics");

			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			//page loading
			webdriver.getLogger().info("Wait for the common UI page loading");
			webdriver.waitForElementPresent("toolbar-left");
			webdriver.takeScreenShot();
			Thread.sleep(10000);
			//verify the product name,app name,content of page
			webdriver.getLogger().info("Verify the page content");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sOracleImage));
			Assert.assertEquals(webdriver.getAttribute(UIControls.sOracleImage + "@alt"), "Oracle");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sProductText));
			webdriver.getLogger().info("The Product is:  " + webdriver.getText(UIControls.sProductText));
			Assert.assertEquals(webdriver.getText(UIControls.sProductText), "Management Cloud");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAppText));
			webdriver.getLogger().info("The App is:  " + webdriver.getText(UIControls.sAppText));
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), "IT Analytics");
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

			//click Open Widget icon
			webdriver.getLogger().info("Verify if Open Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Open");

			webdriver.getLogger().info("Click the Open icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the Open Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Open", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Open button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Open");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));

			//Open a widget
			webdriver.getLogger().info("Select a widget and open it in the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(5000);
			webdriver.getLogger().info("Click Open button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(5000);

			webdriver.getLogger().info("Verify the widget has been opened in main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);
		}
	 */
}
