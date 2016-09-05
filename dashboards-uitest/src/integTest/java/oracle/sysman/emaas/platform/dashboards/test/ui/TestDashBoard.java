package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashBoard extends LoginAndLogout
{
	private String dbName_noDesc = "";
	private String dbName_noWidgetGrid = "";
	private String dbName_noWidgetList = "";
	private String dbName_withWidgetGrid = "";
	private String dbName_setHome = "";
	private String dbName_favorite = "";
	private String dbName_timepicker = "";
	private String dbName_columncheck = "";
	private final String dbName_ITADashboard = "";
	private final String dbName_LADashboard = "";

	private final String WidgetName_1 = "Top Hosts by Log Entries";
	private final String WidgetName_2 = "Access Log Error Status Codes";

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to remove the test data...");
		DashBoardUtils.deleteDashboard(webd, dbName_setHome);
		DashBoardUtils.deleteDashboard(webd, dbName_timepicker);
		DashBoardUtils.deleteDashboard(webd, "TestSaveConfirmation");
		DashBoardUtils.deleteDashboard(webd, "Test_Dashboard_duplicate");
		DashBoardUtils.deleteDashboard(webd, dbName_columncheck);
		DashBoardUtils.deleteDashboard(webd, dbName_ITADashboard);
		DashBoardUtils.deleteDashboard(webd, dbName_LADashboard);

		webd.getLogger().info("All test data have been removed");
	}

	//@Test(dependsOnMethods = { "testCreateDashboard_noWidget_ListView" })
	/*
	    public void Test_targetselector()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards  in list view");

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "noWidgetListView");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetList);

		//edit the dashboard in Target selector page
		DashboardBuilderUtil.EditDashboard_targetselctor(webd, "noWidgetListView", "noWidgetListView desc2");

	}
	 */

	@Test(groups = "Group8", dependsOnMethods = { "testDeleteHomeDashboard" })
	public void testAfterHomeDashboardRemoved()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDeleteHomeDashboard");
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the current page is error page
		webd.getLogger().info("Verify the error page displayed");
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("current url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains("error.html?msg=DBS_ERROR_HOME_PAGE_NOT_FOUND_MSG")) {
			Assert.fail("not open the error page");
		}
		else {
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS),
					"The dashboard which you have set as Home page no longer exists or you don't have access to it.");
			webd.getLogger().info(webd.getText("css=" + PageId.DEFAULTHOME_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.DEFAULTHOME_CSS), "Click  here  to go to the default Home page");
		}

		//click "here" link to go to default home page
		webd.getLogger().info("Click 'here' link to go to default home page");
		webd.waitForElementEnabled("css=" + PageId.DEFAULTHOMEURL_CSS);
		webd.click("css=" + PageId.DEFAULTHOMEURL_CSS);

		//verify the current page is default hoem page
		webd.getLogger().info("Verify the current page is default hoem page");
		url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("current url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains("welcome.html")) {
			Assert.fail("not open the error page");
		}
	}

	@Test(groups = "Group1")
	public void testCreateDashboad_noDesc_GridView()
	{
		dbName_noDesc = "NoDesc-" + generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboad_noDesc_GridView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: no description, with time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_noDesc, null, DashboardHomeUtil.DASHBOARD);
		webd.getLogger().info("verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noDesc, null, true), "Create dashboard failed!");

		//verify the refresh option
		webd.getLogger().info("Verify the default refersh option is 5 mins");
		Assert.assertTrue(
				DashboardBuilderUtil.isRefreshSettingChecked(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN),
				"Refresh option is NOT set to 5 mins!");

		webd.getLogger().info("Turn off the refresh option and check the option is checked");
		DashboardBuilderUtil.refreshDashboard(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);
		Assert.assertTrue(
				DashboardBuilderUtil.isRefreshSettingChecked(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF),
				"Refresh option is NOT set to OFF!");

		webd.getLogger().info("Turn on the refresh option to 5 mins and check the option is checked");
		DashboardBuilderUtil.refreshDashboard(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN);
		Assert.assertTrue(
				DashboardBuilderUtil.isRefreshSettingChecked(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN),
				"Refresh option is NOT set to 5 mins!");

		//delete the dashboard
		webd.getLogger().info("start to delete dashboard in builder page");
		DashboardBuilderUtil.deleteDashboard(webd);
	}

	@Test(groups = "Group4", dependsOnGroups = { "Group3" })
	public void testCreateDashboad_noWidget_GridView()
	{
		dbName_noWidgetGrid = "NoWidgetGridView-" + generateTimeStamp();
		String dbDesc = "Test Dashboard no Widget description";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testCreateDashboad_noWidget_GridView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_noWidgetGrid, dbDesc, DashboardHomeUtil.DASHBOARD);
		webd.getLogger().info("verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid, dbDesc, true),
				"Create dashboard failed!");
	}

	@Test(groups = "Group2", dependsOnGroups = { "Group1" })
	public void testCreateDashboard_noWidget_ListView()
	{
		dbName_noWidgetList = "noWidgetListView-" + generateTimeStamp();
		String dbDesc = "Test Dashboard no Widget description";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testCreateDashboard_noWidget_ListView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to list view");
		DashboardHomeUtil.listView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_noWidgetList, dbDesc, DashboardHomeUtil.DASHBOARD);
		webd.getLogger().info("verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetList, dbDesc, true),
				"Create dashboard failed!");

	}

	@Test(groups = "Group3", dependsOnGroups = { "Group2" })
	public void testCreateDashboard_withWidget_GridView()
	{
		dbName_withWidgetGrid = "withWidget-" + generateTimeStamp();
		String dbDesc = "AAA_testDashBoard desc";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testCreateDashBoard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_withWidgetGrid, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_withWidgetGrid, dbDesc, true),
				"Create dashboard failed!");

		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Database Errors Trend");
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "Database Errors Trend"),
				"Widget 'Database Errors Trend' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//open the widget
		webd.getLogger().info("open the widget");
		DashboardBuilderUtil.openWidget(webd, "Database Errors Trend");

		webd.switchToWindow();
		//WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Wait for the widget loading....");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='srchSrch']")));

		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains(
				"emlacore/html/log-analytics-search.html?widgetId=2013&dashboardId")) {
			Assert.fail("NOT open the correct widget");
		}
	}

	@Test(groups = "Group7", dependsOnGroups = { "Group6" })
	public void testDashboardWith12Columns()
	{
		dbName_columncheck = "DashboardWith12Columns-" + generateTimeStamp();
		String desc = "Description for " + dbName_columncheck;

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDashboardWith12Columns");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_columncheck, desc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_columncheck, desc, true),
				"Failed to verify the created dashboard. Probably the creation failed, or name/description is wrongly specified!");

		String widgetName = "Database Errors Trend";
		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		webd.getLogger().info("Add widget finished");

		webd.getLogger().info("Get narrower widgets");
		for (int i = 1; i <= 4; i++) {
			DashboardBuilderUtil.resizeWidget(webd, widgetName, DashboardBuilderUtil.TILE_NARROWER);
			webd.takeScreenShot();
		}
		webd.getLogger().info("Finished to get narrower widgets");

		webd.getLogger().info("Get wider widgets");
		for (int i = 1; i <= 10; i++) {
			DashboardBuilderUtil.resizeWidget(webd, widgetName, DashboardBuilderUtil.TILE_WIDER);
			webd.takeScreenShot();
		}
		webd.getLogger().info("Finished to get wider widgets");
	}

	@Test(groups = "Group8", dependsOnGroups = { "Group7" })
	public void testDeleteHomeDashboard()
	{
		String dbHomeDashboard = "HomeDashboard-" + generateTimeStamp();
		String dbDesc = "Set the dashboard as home";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDeleteHomeDashboard");
		WaitUtil.waitForPageFullyLoaded(webd);
		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create a dashboard & set it as home
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbHomeDashboard, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbHomeDashboard, dbDesc, true), "Create dashboard failed!");

		webd.getLogger().info("Set home page");
		Assert.assertTrue(DashboardBuilderUtil.toggleHome(webd), "Set the dasbhoard as Home failed!");

		//go to the home page & verify the home page
		webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		String originalUrl = webd.getWebDriver().getCurrentUrl();
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbHomeDashboard, dbDesc, true), "It is NOT the home page!");

		//delete the dashboard
		webd.getLogger().info("Delete the dashboard");
		DashboardBuilderUtil.deleteDashboard(webd);

		//go to the home page
		webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);

		webd.getLogger().info("Verify the error page displayed");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(PageId.ERRORMESSAGE_CSS)));
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("current url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains("error.html?invalidUrl=")) {
			Assert.fail("not open the error page");
		}
		else {
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORMESSAGE_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORMESSAGE_CSS),
					"Sorry, the page you have requested either doesn't exist or you do not have access to it.");
			webd.getLogger().info(webd.getText("css=" + PageId.ERRORURL_CSS));
			Assert.assertEquals(webd.getText("css=" + PageId.ERRORURL_CSS), "Requested URL is: " + originalUrl);
		}
	}

	@Test(groups = "Group7", dependsOnGroups = { "Group6" })
	public void testDeleteOOB()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDeleteOOB");

		//reset all filter options
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);
		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//check OOB delete protection
		webd.getLogger().info("Verfiy if the OOB dashboard can be deleted");
		DashboardHomeUtil.search(webd, "Application Performance Monitoring");
		webd.click(DashBoardPageId.INFOBTNID);
		WebElement removeButton = webd.getWebDriver().findElement(By.xpath(DashBoardPageId.RMBTNID));
		Assert.assertFalse(removeButton.isEnabled(), "delete is enabled for OOB dashboard");
	}

	@Test(groups = "Group5", dependsOnGroups = { "Group4" })
	public void testDuplicateDashboard()
	{
		String dbName = "Test_Dashboard_duplicate";
		String dbDesc = "Test_Dashboard_duplicate_desc";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testDuplicateDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found");

		//open the dashboard
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid + "-modify");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify-not displayed", true), "Dashboard NOT found");

		//Duplicate dashbaord
		DashboardBuilderUtil.duplicateDashboard(webd, dbName, dbDesc);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc, true));

	}

	@Test(groups = "Group7", dependsOnGroups = { "Group6" })
	public void testFavorite()
	{
		dbName_favorite = "favoriteDashboard-" + generateTimeStamp();
		String dbDesc = "favorite_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testFavorite");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_favorite, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_favorite, dbDesc, true), "Create dashboard failed!");

		//set it to favorite
		webd.getLogger().info("Set the dashboard to favorite");
		Assert.assertTrue(DashboardBuilderUtil.favoriteOption(webd));

		//verify the dashboard is favorite
		webd.getLogger().info("Visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);

		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");
		//		WebElement el = webd.getWebDriver().findElement(By.id(DashBoardPageId.Favorite_BoxID));
		//		Assert.assertTrue(el.isSelected());

		webd.getLogger().info("Verfiy the dashboard is favorite");
		//DashboardHomeUtil.search(webd, dbName_favorite);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_favorite), "Can not find the dashboard");

		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_favorite);
		webd.getLogger().info("Verify the dashboard in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_favorite, dbDesc, true), "Create dashboard failed!");

		//set it to not favorite
		webd.getLogger().info("set the dashboard to not favorite");
		Assert.assertFalse(DashboardBuilderUtil.favoriteOption(webd), "Set to not my favorite dashboard failed!");

		//verify the dashboard is not favoite
		webd.getLogger().info("visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);
		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");
		//		el = webd.getWebDriver().findElement(By.id(DashBoardPageId.Favorite_BoxID));
		//		Assert.assertTrue(el.isSelected());
		webd.getLogger().info("Verfiy the dashboard is not favorite");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_favorite),
				"The dashboard is still my favorite dashboard");
		//		DashboardHomeUtil.search(webd, dbName_favorite);
		//		Assert.assertEquals(webd.getAttribute(DashBoardPageId.DashboardSerachResult_panelID + "@childElementCount"), "0");
		//		webd.getLogger().info("no favorite dashboard");

		//delete the dashboard
		webd.getLogger().info("start to delete the dashboard");

		WebElement el = webd.getWebDriver().findElement(By.id(DashBoardPageId.FAVORITE_BOXID));
		if (el.isSelected()) {
			el.click();
		}
		DashboardHomeUtil.deleteDashboard(webd, dbName_favorite, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("the dashboard has been deleted");
	}

	//	@Test(groups = "Group7", dependsOnGroups = { "Group6" })
	//	public void testFilterITADashboard()
	//	{
	//		dbName_ITADashboard = "ITADashboard-" + generateTimeStamp();
	//		String dbDesc = "test filter ITA works for custom dashboard";
	//		//initialize the test
	//		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
	//		webd.getLogger().info("Start to test in testTimePicker");
	//
	//		//reset the home page
	//		webd.getLogger().info("Reset all filter options in the home page");
	//		DashboardHomeUtil.resetFilterOptions(webd);
	//
	//		//switch to Grid View
	//		webd.getLogger().info("Switch to grid view");
	//		DashboardHomeUtil.gridView(webd);
	//
	//		//create dashboard
	//		webd.getLogger().info("Create a dashboard: with description, time refresh");
	//		DashboardHomeUtil.createDashboard(webd, dbName_ITADashboard, dbDesc, DashboardHomeUtil.DASHBOARD);
	//
	//		//verify dashboard in builder page
	//		webd.getLogger().info("Verify the dashboard created Successfully");
	//		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_ITADashboard, dbDesc, true),
	//				"Create dashboard failed!");
	//
	//		//Add the widget to the dashboard
	//		webd.getLogger().info("Start to add Widget into the dashboard");
	//		DashboardBuilderUtil.addWidgetToDashboard(webd, "Analytics Line - Categorical");
	//		webd.getLogger().info("Add widget finished");
	//
	//		//save dashboard
	//		webd.getLogger().info("Save the dashboard");
	//		DashboardBuilderUtil.saveDashboard(webd);
	//
	//		//back to home page
	//		webd.getLogger().info("Back to dashboard home page");
	//		BrandingBarUtil.visitDashboardHome(webd);
	//
	//		//set filter option, cloud services="IT Analytics" created by ME
	//		webd.getLogger().info("set filter option, cloud services='IT Analytics' and Created by ME");
	//		DashboardHomeUtil.filterOptions(webd, "ita");
	//		DashboardHomeUtil.filterOptions(webd, "me");
	//		webd.getLogger().info("Verify the created dashboard exists");
	//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_ITADashboard), "The dashboard NOT exists");
	//
	//		//reset filter options
	//		webd.getLogger().info("Reset filter options");
	//		DashboardHomeUtil.resetFilterOptions(webd);
	//	}
	//
	//	@Test(groups = "Group7", dependsOnGroups = { "Group6" })
	//	public void testFilterLADashboard()
	//	{
	//		dbName_LADashboard = "LADashboard-" + generateTimeStamp();
	//		String dbDesc = "test filter LA works for custom dashboard";
	//		//initialize the test
	//		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
	//		webd.getLogger().info("Start to test in testFilterLADashboard");
	//
	//		//reset the home page
	//		webd.getLogger().info("Reset all filter options in the home page");
	//		DashboardHomeUtil.resetFilterOptions(webd);
	//
	//		//switch to Grid View
	//		webd.getLogger().info("Switch to grid view");
	//		DashboardHomeUtil.gridView(webd);
	//
	//		//create dashboard
	//		webd.getLogger().info("Create a dashboard: with description, time refresh");
	//		DashboardHomeUtil.createDashboard(webd, dbName_LADashboard, dbDesc, DashboardHomeUtil.DASHBOARD);
	//
	//		//verify dashboard in builder page
	//		webd.getLogger().info("Verify the dashboard created Successfully");
	//		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_LADashboard, dbDesc, true),
	//				"Create dashboard failed!");
	//
	//		//Add the widget to the dashboard
	//		webd.getLogger().info("Start to add Widget into the dashboard");
	//		DashboardBuilderUtil.addWidgetToDashboard(webd, "Database Errors Trend");
	//		webd.getLogger().info("Add widget finished");
	//
	//		//save dashboard
	//		webd.getLogger().info("Save the dashboard");
	//		DashboardBuilderUtil.saveDashboard(webd);
	//
	//		//back to home page
	//		webd.getLogger().info("Back to dashboard home page");
	//		BrandingBarUtil.visitDashboardHome(webd);
	//
	//		//set filter option, cloud services="IT Analytics" created by ME
	//		webd.getLogger().info("set filter option, cloud services='LT Analytics' and Created by ME");
	//		DashboardHomeUtil.filterOptions(webd, "la");
	//		DashboardHomeUtil.filterOptions(webd, "me");
	//		webd.getLogger().info("Verify the created dashboard exists");
	//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_LADashboard), "The dashboard NOT exists");
	//
	//		//reset filter options
	//		webd.getLogger().info("Reset filter options");
	//		DashboardHomeUtil.resetFilterOptions(webd);
	//	}

	@Test(groups = "Group5", dependsOnGroups = { "Group4" })
	public void testHideEntityFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHideEntityFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid + "-modify");

		//hide entity filter in dashboard
		webd.getLogger().info("hide entity filter");
		Assert.assertFalse(DashboardBuilderUtil.showEntityFilter(webd, false), "hide entity filter failed");

		//verify the target selector not displayed in the page
		webd.getLogger().info("Verify the target selecotr not diplayed in the page");
		Assert.assertFalse(webd.isDisplayed("css=" + PageId.TARGETSELECTOR_CSS), "The target selector is in the page");
	}

	@Test(groups = "Group5", dependsOnGroups = { "Group4" })
	public void testHideTimeRangeFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHideTimeRangeFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid + "-modify");

		//hide time range filter in dashboard
		webd.getLogger().info("hide time range filter");
		Assert.assertFalse(DashboardBuilderUtil.showTimeRangeFilter(webd, false), "hide time range filter failed");

		//verify the time range filter not displayed in the page
		webd.getLogger().info("Verify the time range filter not diplayed in the page");
		Assert.assertFalse(webd.isDisplayed("css=" + PageId.DATETIMEPICK_CSS), "The time range filter is in the page");
	}

	@Test(groups = "Group4", dependsOnMethods = { "testCreateDashboad_noWidget_GridView" })
	public void testModifyDashboard_namedesc()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testModifyDashboard_namedesc");

		//edit dashboard
		webd.getLogger().info("Start to edit dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid), "Dashboard NOT found!");

		//Open the dashboard
		webd.getLogger().info("Open the dashboard to edit name and description");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid);

		//edit the dashboard
		webd.getLogger().info("Edit the dashboard's name and description, display the description");
		DashboardBuilderUtil.editDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify-displayed", true);

		//Verify the dashboard edited successfully
		webd.getLogger().info("Verify the dashboard edited successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify-displayed", true), "Dashboard edit failed!");

		//edit the dashboard
		webd.getLogger().info("Edit the dashboard's name and description, display the description");
		DashboardBuilderUtil.editDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify-not displayed", false);

		//Verify the dashboard edited successfully
		webd.getLogger().info("Verify the dashboard edited successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid + "-modify",
				"Test_Dashboard_no_Widget_GridView desc modify-not displayed", true), "Dashboard edit failed!");
	}

	@Test(groups = "Group3", dependsOnMethods = { "testCreateDashboard_withWidget_GridView" })
	public void testModifyDashboard_widget()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testModifyDashboard_widget");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard want to modify
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_withWidgetGrid);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_withWidgetGrid), "Dashboard NOT found!");

		//open the dashboard in the builder page
		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_withWidgetGrid);

		//add the widget into the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");

		webd.getLogger().info("Add Widget '" + WidgetName_1 + "' into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_1);
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_1), "Widget '" + WidgetName_1 + "' not found");

		webd.getLogger().info("Add Widget '" + WidgetName_2 + "' into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_2);
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_2), "Widget '" + WidgetName_2 + "' not found");

		webd.getLogger().info("Add Widget '" + WidgetName_1 + "' into the dashboard again");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_1);
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_1, 1), "The second Widget '" + WidgetName_1
				+ "' not found");

		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}

	@Test(groups = "Group6", dependsOnGroups = { "Group5" })
	public void testRemoveDashboard_GridView()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashboard_GridView");

		//search dashboard
		webd.getLogger().info("Search the dashboard want to delete in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found!");

		//delte dashboard
		webd.getLogger().info("Start to delete dashboard in grid view");
		DashboardHomeUtil.deleteDashboard(webd, dbName_noWidgetGrid + "-modify", DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

		//verify the dashboard deleted
		webd.getLogger().info("Verify the dashboard is deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Delete failed!");

	}

	@Test(groups = "Group2", dependsOnMethods = { "testCreateDashboard_noWidget_ListView" })
	public void testRemoveDashboard_ListView()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashboard_ListView");

		//search dashboard
		webd.getLogger().info("Search the dashboard want to delete in grid view");
		DashboardHomeUtil.listView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetList);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetList), "Dashboard NOT found!");

		//delte dashboard
		webd.getLogger().info("Start to delete dashboard in grid view");
		DashboardHomeUtil.deleteDashboard(webd, dbName_noWidgetList, DashboardHomeUtil.DASHBOARDS_LIST_VIEW);

		//verify the dashboard deleted
		webd.getLogger().info("Verify the dashboard is deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Delete failed!");

	}

	@Test(groups = "Group3", dependsOnMethods = { "testWidgetConfiguration" })
	public void testRemoveDashboardInBuilderPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashboardInBuilderPage");

		//search dashboard
		webd.getLogger().info("Search the dashboard want to delete in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("Search dashboard");
		DashboardHomeUtil.search(webd, dbName_withWidgetGrid);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_withWidgetGrid), "Dashboard NOT found!");

		//open the dashboard in builder page
		DashboardHomeUtil.selectDashboard(webd, dbName_withWidgetGrid);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_withWidgetGrid, "AAA_testDashBoard desc", true),
				"Dashboard NOT found!");

		//delte the dashboard in builder page
		webd.getLogger().info("start to delete dashboard in builder page");
		DashboardBuilderUtil.deleteDashboard(webd);

		//verify if in the home page
		webd.getLogger().info("verify delete successfully and back to the home page");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(PageId.DASHBOARDDISPLAYPANELCSS)));
	}

	@Test(groups = "Group7", dependsOnGroups = { "Group6" })
	public void testSaveConfirmation()
	{
		String dbName = "TestSaveConfirmation";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testSaveConfimation");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create a dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, null, DashboardHomeUtil.DASHBOARD);
		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, null, true), "Create dashboard failed!");

		//edit the dashboard
		webd.getLogger().info("add a widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Database Errors Trend");

		//leave the builder page
		webd.getLogger().info("return to dashboard home page");
		DashBoardUtils.clickDashboardLinkInBrandingBar(webd);

		webd.getLogger().info("the warning dialog pop up");
		DashBoardUtils.handleAlert(webd);

		//verify if in the home page
		webd.getLogger().info("Verify if in the home page");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(PageId.DASHBOARDDISPLAYPANELCSS)));
	}

	@Test(groups = "Group8", dependsOnMethods = { "testAfterHomeDashboardRemoved" })
	public void testSetHome()
	{
		dbName_setHome = "setHomeDashboard-" + generateTimeStamp();
		String dbDesc = "SetHome_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testSetHome");
		WaitUtil.waitForPageFullyLoaded(webd);
		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_setHome, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_setHome, dbDesc, true), "Create dashboard failed!");

		//set it as home
		webd.getLogger().info("Set home page");
		Assert.assertTrue(DashboardBuilderUtil.toggleHome(webd), "Set the dasbhoard as Home failed!");

		//check home page
		/*webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_setHome, dbDesc, true), "It is NOT the home page!");*/

		//set it not home
		/*webd.getLogger().info("Set not home page");
		Assert.assertFalse(DashboardBuilderUtil.toggleHome(webd), "Remove the dasbhoard as Home failed!");*/

		//check home page
		/*webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, WelcomeUtil.SERVICE_NAME_DASHBOARDS),
				"It is NOT the home page!");*/
	}

	@Test(groups = "Group8", dependsOnMethods = { "testSetHome" })
	public void testSetHome_verify()
	{
		String dbDesc = "SetHome_testDashboard desc";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("testSetHome-Login again and verify if the home is set");

		//Verifying sethome page for above dashbaoard
		//     webd.getLogger().info("Access to the home page");
		//	BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_setHome, dbDesc, true), "It is NOT the home page!");

		//set it not home
		webd.getLogger().info("Set not home page");
		Assert.assertFalse(DashboardBuilderUtil.toggleHome(webd), "Remove the dasbhoard as Home failed!");

		//check home page
		webd.getLogger().info("Access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, WelcomeUtil.SERVICE_NAME_DASHBOARDS),
				"It is NOT the home page!");
	}

	@Test(groups = "Group5", dependsOnGroups = { "Group4" })
	public void testShareDashboard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testshareddashboard");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid + "-modify");

		//sharing dashbaord
		webd.getLogger().info("Share the dashboard");
		Assert.assertTrue(DashboardBuilderUtil.toggleShareDashboard(webd), "Share dashboard failed!");

	}

	@Test(groups = "Group5", dependsOnGroups = { "Group4" })
	public void testShowEntityFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testShowEntityFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid + "-modify");

		//show entity filter in dashboard
		webd.getLogger().info("show entity filter");
		Assert.assertTrue(DashboardBuilderUtil.showEntityFilter(webd, true), "show entity filter failed");

		//verify the target selector not displayed in the page
		webd.getLogger().info("Verify the target selecotr diplayed in the page");
		Assert.assertTrue(webd.isDisplayed("css=" + PageId.TARGETSELECTOR_CSS), "The target selector is NOT in the page");
	}

	@Test(groups = "Group5", dependsOnGroups = { "Group4" })
	public void testShowTimeRangeFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testShowTimeRangeFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid + "-modify");

		//show time range filter in dashboard
		webd.getLogger().info("Show time range filter");
		Assert.assertTrue(DashboardBuilderUtil.showTimeRangeFilter(webd, true), "Show time range filter failed");

		//verify the time range filter displayed in the page
		webd.getLogger().info("Verify the time range filter diplayed in the page");
		Assert.assertTrue(webd.isDisplayed("css=" + PageId.DATETIMEPICK_CSS), "The time range filter is NOT in the page");
	}

	@Test(groups = "Group5", dependsOnMethods = { "testShareDashboard" })
	public void testStopShareDashboard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testshareddashboard");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_noWidgetGrid + "-modify");
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_noWidgetGrid + "-modify"), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_noWidgetGrid + "-modify");

		//stop sharing dashbaord
		webd.getLogger().info("Stop share the dashboard");
		Assert.assertFalse(DashboardBuilderUtil.toggleShareDashboard(webd), "Stop sharing the dashboard failed!");
	}

	@Test(groups = "Group7", dependsOnGroups = { "Group6" })
	public void testTimePicker()
	{
		dbName_timepicker = "TestDashboardTimeselector-" + generateTimeStamp();
		String dbDesc = "Test Dashboard timeselector description";

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testTimePicker");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_timepicker, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_timepicker, dbDesc, true), "Create dashboard failed!");

		//disable time selector
		DashboardBuilderUtil.showTimeRangeFilter(webd, false);
		//verify the dashboard, the time selector no display
		webd.getLogger().info("Verify the time selector is not diplayed in dashboard");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_timepicker, dbDesc, false),
				"Time selector component is still displayed!");
		//enable time selector
		DashboardBuilderUtil.showTimeRangeFilter(webd, true);
		//verify the dashboard, the time selector is displayed
		webd.getLogger().info("Verify the time selector is diplayed in dashboard");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_timepicker, dbDesc, true),
				"Time selector component is not displayed!");

		//Add the widget to the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Database Errors Trend");
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//set time range for time picker
		webd.getLogger().info("Set the time range");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last15Mins);
		String returnTimeRange = TimeSelectorUtil.setCustomTime(webd, "04/06/2015 07:22 PM", "04/07/2016 09:22 PM");

		//convert date to timestamp
		SimpleDateFormat fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = returnTimeRange.substring(7);

		String[] tmpDate = tmpReturnDate.split("-");

		String tmpStartDate = tmpDate[0].trim();
		String tmpEndDate = tmpDate[1].trim();

		try {
			dTmpStart = fmt.parse(tmpStartDate);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dTmpEnd = fmt.parse(tmpEndDate);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String tmpStartDateNew = df.format(dTmpStart);
		String tmpEndDateNew = df.format(dTmpEnd);
		webd.getLogger().info(tmpStartDateNew);
		webd.getLogger().info(tmpEndDateNew);

		long StartTimeStamp = Timestamp.valueOf(tmpStartDateNew).getTime();
		long EndTimeStamp = Timestamp.valueOf(tmpEndDateNew).getTime();

		webd.getLogger().info(String.valueOf(StartTimeStamp));
		webd.getLogger().info(String.valueOf(EndTimeStamp));

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, "Database Errors Trend");

		//verify the url
		webd.switchToWindow();
		//WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Wait for the widget loading....");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='srchSrch']")));

		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains(
				"startTime=" + String.valueOf(StartTimeStamp) + "&endTime=" + String.valueOf(EndTimeStamp))) {
			Assert.fail("not open the correct widget");
		}

	}

	@Test(groups = "Group3", dependsOnMethods = { "testModifyDashboard_widget" })
	public void testWidgetConfiguration()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testWidgetConfiguration");

		//search the dashboard want to modify
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, dbName_withWidgetGrid);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_withWidgetGrid), "Dashboard NOT found!");

		//open the dashboard in the builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_withWidgetGrid);

		//widget operation
		webd.getLogger().info("hide/show the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, WidgetName_1, false);
		DashboardBuilderUtil.showWidgetTitle(webd, WidgetName_2, true);

		webd.getLogger().info("Resize the widge");
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_WIDER);
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_NARROWER);
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_2, DashboardBuilderUtil.TILE_TALLER);
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_2, DashboardBuilderUtil.TILE_SHORTER);

		webd.getLogger().info("Move the widget");
		DashboardBuilderUtil.moveWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_RIGHT);
		DashboardBuilderUtil.moveWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_LEFT);
		DashboardBuilderUtil.moveWidget(webd, WidgetName_2, DashboardBuilderUtil.TILE_DOWN);
		DashboardBuilderUtil.moveWidget(webd, WidgetName_2, DashboardBuilderUtil.TILE_UP);

		webd.getLogger().info("Remove the widget");
		DashboardBuilderUtil.removeWidget(webd, WidgetName_1);
		DashboardBuilderUtil.removeWidget(webd, WidgetName_2);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//verify the current bashboard has one widget
		webd.getLogger().info("Verify the dashboard");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, WidgetName_1), "Widget '" + WidgetName_1 + "' not found");
		Assert.assertFalse(DashboardBuilderUtil.verifyWidget(webd, WidgetName_2), "Widget '" + WidgetName_2 + "' found");
	}

	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

}
