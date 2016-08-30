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

	//String sSsoUserName = CommonUIUtils.getEmaasPropertyValue("SSO_USERNAME");
	//String sSsoPassword = CommonUIUtils.getEmaasPropertyValue("SSO_PASSWORD");
	//String sTenantId = CommonUIUtils.getEmaasPropertyValue("TENANT_ID");

	@BeforeClass
	public static void initValue()
	{
		CommonUIUtils.getAppName(sTenantId, sSsoUserName);
		CommonUIUtils.getRoles(sTenantId, sSsoUserName);
	}

	@Test
	public void testDashboardPage_noPara() throws Exception
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
			webdriver.waitForElementPresent(UIControls.sCompassIcon);
			webdriver.click(UIControls.sCompassIcon);
			webdriver.takeScreenShot();

			CommonUIUtils.verifyMenu(webdriver, isDSAdmin);

			//click the compass icon again
			webdriver.getLogger().info("Click the Application navigator icon again");
			webdriver.waitForElementPresent(UIControls.sCompassIcon);
			webdriver.click(UIControls.sCompassIcon);
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
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void testDashboardPage_withPara() throws Exception
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
			webdriver.waitForElementPresent(UIControls.sCompassIcon);
			webdriver.click(UIControls.sCompassIcon);
			webdriver.takeScreenShot();

			CommonUIUtils.verifyMenu(webdriver, isDSAdmin);

			//click the compass icon again
			webdriver.getLogger().info("Click the Application navigator icon again");
			webdriver.waitForElementPresent(UIControls.sCompassIcon);
			webdriver.click(UIControls.sCompassIcon);
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
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	//Testcase for adding widget using widgetselector

	@Test
	public void testWidgetSelector() throws Exception
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
			wait.until(ExpectedConditions.elementToBeClickable(By.id(DashBoardPageId.WidgetSelector_AddButtonId)));
			WaitUtil.waitForPageFullyLoaded(webdriver);

			//click on Add button
			webdriver.click("id=" + DashBoardPageId.WidgetSelector_AddButtonId);
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
			Assert.fail(ex.getLocalizedMessage());
		}
	}

	/*
	@Test
	public void testDashboardPage_noPara() throws Exception
	{
		try {

			CommonUIUtils.commonUITestLog("This is to test Dashboard Page");

			String testName = this.getClass().getName() + ".testDashboardPage_noPara";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);
			Thread.sleep(5000);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver);

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
			webdriver.getLogger().info("The App is:  " + sAppName);
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), sAppName);
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
			webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
			Assert.assertEquals(webdriver.getText(UIControls.sPageText),
					"Sample page for OMC UI Framework components testing only");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.click(UIControls.sCompassIcon);
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
			webdriver.takeScreenShot();
			Thread.sleep(5000);
			webdriver.getLogger().info("Verify the Links menu disappeared");
			Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

			//click Add Widget icon
			webdriver.getLogger().info("Verify if Add Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Add");

			webdriver.getLogger().info("Click the Add Widgets icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the Add Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Add Widgets", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Add Widgets button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Add");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			webdriver.getLogger().info("Verify the select category drop-down list in Add Widgets button is displayed");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sCategorySelect));

			//Add a widget
			webdriver.getLogger().info("Select a widget and add it to the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(15000);
			webdriver.getLogger().info("Click Add button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(5000);
			webdriver.getLogger().info("Close the Add Widget window");
			webdriver.click(UIControls.sCloseWidget);
			webdriver.takeScreenShot();
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the widget has been added to main page");
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
		public void testDashboardPage_withAllPara_Admin() throws Exception
		{

			String testName = this.getClass().getName() + ".testDashboardPage_withAllPara_Admin";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=Dashboard&isAdmin=true");
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
			webdriver.getLogger().info("The App is:  " + sAppName);
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), sAppName);
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
			webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
			Assert.assertEquals(webdriver.getText(UIControls.sPageText),
					"Sample page for OMC UI Framework components testing only");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.click(UIControls.sCompassIcon);
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
			webdriver.takeScreenShot();
			Thread.sleep(5000);
			webdriver.getLogger().info("Verify the Links menu disappeared");
			Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

			//click Add Widget icon
			webdriver.getLogger().info("Verify if Add Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Add");

			webdriver.getLogger().info("Click the Add Widgets icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the Add Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Add Widgets", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Add Widgets button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Add");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Thread.sleep(10000);
			//Add a widget
			webdriver.getLogger().info("Select a widget and add it to the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(15000);
			webdriver.getLogger().info("Click Add button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(15000);
			webdriver.getLogger().info("Close the Add Widget window");
			webdriver.click(UIControls.sCloseWidget);
			webdriver.takeScreenShot();
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the widget has been added to main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);
		}

		@Test
		public void testDashboardPage_withAllPara_notAdmin() throws Exception
		{

			String testName = this.getClass().getName() + ".testDashboardPage_withAllPara_notAdmin";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?appId=Dashboard&isAdmin=false");
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
			webdriver.getLogger().info("The App is:  " + sAppName);
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), sAppName);
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
			webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
			Assert.assertEquals(webdriver.getText(UIControls.sPageText),
					"Sample page for OMC UI Framework components testing only");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.click(UIControls.sCompassIcon);
			webdriver.takeScreenShot();

			//verify the menus
			webdriver.getLogger().info("Verify the Links menu displayed");
			webdriver.getLogger().info("The Link menu is:  " + webdriver.getAttribute(UIControls.sLinksMenu + "@style"));
			Assert.assertNotEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

			Assert.assertTrue(webdriver.isDisplayed(UIControls.sHome));
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sHomeIcon));
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sHomeLabel));
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sHomeLink));

			Assert.assertTrue(webdriver.isDisplayed(UIControls.sCloudService));
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sCloudServiceIcon));
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sCloudServiceLabel));
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sCloudServiceLink));

			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAnalyzer));
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAnalyzerIcon));
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAnalyzerLabel));
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAnalyzerLink));

			//Assert.assertEquals(webdriver.isDisplayed(UIControls.sAdmin), false);
			//Assert.assertFalse(webdriver.isDisplayed(UIControls.sAdminIcon));
			//Assert.assertFalse(webdriver.isDisplayed(UIControls.sAdminLabel));
			//Assert.assertFalse(webdriver.isDisplayed(UIControls.sAdminLink));

			Thread.sleep(10000);

			webdriver.getLogger().info("Click the Application navigator icon again");
			webdriver.click(UIControls.sCompassIcon);
			webdriver.takeScreenShot();
			Thread.sleep(5000);
			webdriver.getLogger().info("Verify the Links menu disappeared");
			webdriver.getLogger().info("The Link menu is:  " + webdriver.getAttribute(UIControls.sLinksMenu + "@style"));
			Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

			//click Add Widget icon
			webdriver.getLogger().info("Verify if Add Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Add");

			webdriver.getLogger().info("Click the Add Widgets icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the Add Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Add Widgets", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Add Widgets button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Add");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Thread.sleep(10000);
			//Add a widget
			webdriver.getLogger().info("Select a widget and add it to the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(15000);
			webdriver.getLogger().info("Click Add button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(15000);
			webdriver.getLogger().info("Close the Add Widget window");
			webdriver.click(UIControls.sCloseWidget);
			webdriver.takeScreenShot();
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the widget has been added to main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);
		}

		@Test
		public void testDashboardPage_withOnePara_Admin() throws Exception
		{

			String testName = this.getClass().getName() + ".testDashboardPage_withOnePara_Admin";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?isAdmin=true");
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
			webdriver.getLogger().info("The App is:  " + sAppName);
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), sAppName);
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
			webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
			Assert.assertEquals(webdriver.getText(UIControls.sPageText),
					"Sample page for OMC UI Framework components testing only");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.click(UIControls.sCompassIcon);
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
			webdriver.takeScreenShot();
			Thread.sleep(5000);
			webdriver.getLogger().info("Verify the Links menu disappeared");
			Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

			//click Add Widget icon
			webdriver.getLogger().info("Verify if Add Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Add");

			webdriver.getLogger().info("Click the Add Widgets icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the Add Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Add Widgets", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Add Widgets button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Add");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Thread.sleep(10000);
			//Add a widget
			webdriver.getLogger().info("Select a widget and add it to the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(15000);
			webdriver.getLogger().info("Click Add button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(15000);
			webdriver.getLogger().info("Close the Add Widget window");
			webdriver.click(UIControls.sCloseWidget);
			webdriver.takeScreenShot();
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the widget has been added to main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);
		}

		@Test
		public void testDashboardPage_withOnePara_notAdmin() throws Exception
		{

			String testName = this.getClass().getName() + ".testDashboardPage_withOnePara_notAdmin";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, "?isAdmin=false");
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
			webdriver.getLogger().info("The App is:  " + sAppName);
			Assert.assertEquals(webdriver.getText(UIControls.sAppText), sAppName);
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sPageText));
			webdriver.getLogger().info("The page content is:  " + webdriver.getText(UIControls.sPageText));
			Assert.assertEquals(webdriver.getText(UIControls.sPageText),
					"Sample page for OMC UI Framework components testing only");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sCompassIcon));
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sAddWidgetIcon));

			//click the compass icon
			webdriver.getLogger().info("Click the Application navigator icon");
			webdriver.click(UIControls.sCompassIcon);
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

			//		Assert.assertFalse(webdriver.isDisplayed(UIControls.sAdmin));
			//		Assert.assertFalse(webdriver.isDisplayed(UIControls.sAdminIcon));
			//		Assert.assertFalse(webdriver.isDisplayed(UIControls.sAdminLabel));
			//		Assert.assertFalse(webdriver.isDisplayed(UIControls.sAdminLink));

			Thread.sleep(10000);

			webdriver.getLogger().info("Click the Application navigator icon again");
			webdriver.click(UIControls.sCompassIcon);
			webdriver.takeScreenShot();
			Thread.sleep(5000);
			webdriver.getLogger().info("Verify the Links menu disappeared");
			Assert.assertEquals(webdriver.getAttribute(UIControls.sLinksMenu + "@style"), "display: none;");

			//click Add Widget icon
			webdriver.getLogger().info("Verify if Add Widgets icon displayed");
			Assert.assertTrue(webdriver.isDisplayed(UIControls.sAddWidgetIcon));
			webdriver.getLogger().info("The buton is:  " + webdriver.getText(UIControls.sAddWidgetIcon));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetIcon), "Add");

			webdriver.getLogger().info("Click the Add Widgets icon");
			webdriver.click(UIControls.sAddWidgetIcon);
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the Add Widgets window is opened");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidgetWindowTitle));
			webdriver.getLogger().info("The window title is:  " + webdriver.getText(UIControls.sWidgetWindowTitle));
			Assert.assertTrue(webdriver.isTextPresent("Add Widgets", UIControls.sWidgetWindowTitle));
			webdriver.takeScreenShot();
			webdriver.getLogger().info("Verify the Add Widgets button is disabled");
			webdriver.getLogger().info("The button is:  " + webdriver.getText(UIControls.sAddWidgetBtn));
			Assert.assertEquals(webdriver.getText(UIControls.sAddWidgetBtn), "Add");
			webdriver.getLogger().info("The button has been:  " + webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Assert.assertNotNull(webdriver.getAttribute(UIControls.sAddWidgetBtn + "@disabled"));
			Thread.sleep(10000);
			//Add a widget
			webdriver.getLogger().info("Select a widget and add it to the main page");
			webdriver.getLogger().info("Select a widget");
			webdriver.click(UIControls.sWidgetSelct);
			Thread.sleep(15000);
			webdriver.getLogger().info("Click Add button");
			webdriver.click(UIControls.sAddWidgetBtn);
			webdriver.takeScreenShot();
			Thread.sleep(15000);
			webdriver.getLogger().info("Close the Add Widget window");
			webdriver.click(UIControls.sCloseWidget);
			webdriver.takeScreenShot();
			Thread.sleep(15000);

			webdriver.getLogger().info("Verify the widget has been added to main page");
			Assert.assertTrue(webdriver.isElementPresent(UIControls.sWidget));
			webdriver.takeScreenShot();

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);
		}
	 */
}
