package oracle.sysman.emaas.platform.dashboards.test.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;

/**
 * @version
 * @author charles.c.chen
 * @since release specific (what release of product did this appear in)
 */

public class TestDashBoard extends LoginAndLogout
{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test
	public void testCreateDashboad_noDesc_GridView() throws Exception
	{
		String dbName = "Test Dashboard no Desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testCreateDashboad_noWidget_GridView");

		//reset the home page
		webd.getLogger().info("reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, null, true);

		DashboardBuilderUtil.verifyDashboard(webd, dbName, null, true);

		//verify the refresh option
		webd.getLogger().info("Verify the default refersh option is 5 mins");
		Assert.assertTrue(
				DashboardBuilderUtil.isRefreshSettingChecked(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN));

		webd.getLogger().info("turn off the refresh option and check the option is checked");
		DashboardBuilderUtil.refreshDashboard(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);
		Assert.assertTrue(
				DashboardBuilderUtil.isRefreshSettingChecked(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF));

		webd.getLogger().info("switch the refresh option to 5 mins and check the option is checked");
		DashboardBuilderUtil.refreshDashboard(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN);
		Assert.assertTrue(
				DashboardBuilderUtil.isRefreshSettingChecked(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN));

		//delete the dashboard
		webd.getLogger().info("start to delete dashboard in builder page");
		DashboardBuilderUtil.deleteDashboard(webd);
	}

	@Test
	public void testCreateDashboad_noWidget_GridView() throws Exception
	{
		String dbName = "Test_Dashboard_no_Widget_GridView";
		String dbDesc = "Test Dashboard no Widget description";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testCreateDashboad_noWidget_GridView");

		//reset the home page
		webd.getLogger().info("reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, dbDesc, true);

		DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc, true);

	}

	@Test
	public void testCreateDashboard_noTimeRefresh() throws Exception
	{
		String dbName = "Test Dashboard no timeselector";
		String dbDesc = "Test Dashboard no timeselector description";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testCreateDashboard_noTimeRefresh");

		//reset the home page
		webd.getLogger().info("reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, dbDesc, false);

		DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc, false);

		//delete the dashboard
		webd.getLogger().info("start to delete dashboard in builder page");
		DashboardBuilderUtil.deleteDashboard(webd);

	}

	@Test
	public void testCreateDashboard_noWidget_ListView() throws Exception
	{
		String dbName = "Test_Dashboard_no_Widget_ListView";
		String dbDesc = "Test Dashboard no Widget description";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testCreateDashboad_noWidget_GridView");

		//reset the home page
		webd.getLogger().info("reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("switch to list view");
		DashboardHomeUtil.listView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, dbDesc, true);

		DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc, true);

	}

	@Test
	public void testCreateDashboard_withWidget_GridView() throws Exception
	{
		String dbName = "AAA_testDashboard";
		String dbDesc = "AAA_testDashBoard desc";

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testCreateDashBoard");

		//reset the home page
		webd.getLogger().info("reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName, dbDesc, true);

		//verify dashboard in builder page
		DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc, true);

		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetByRightDrawer(webd, "Database Errors Trend");
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.save(webd);

		//open the widget
		webd.getLogger().info("open the widget");
		DashboardBuilderUtil.openWidget(webd, "Database Errors Trend");
		//verify the result

		//		String currentWindow = webd.getWebDriver().getWindowHandle();
		//		Set<String> handles = webd.getWebDriver().getWindowHandles();
		//		Iterator<String> it = handles.iterator();
		//		while (it.hasNext()) {
		//			webd.getLogger().info("new window");
		//			if (currentWindow == it.next()) {
		//				continue;
		//			}
		//		}
		//		webd.getWebDriver().switchTo().window(it.next());
		webd.switchToWindow();

		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9)
				.contains("emlacore/html/log-analytics-search.html?widgetId=2013&dashboardId")) {
			Assert.fail("not open the correct widget");
		}

	}

	@Test
	public void testDeleteOOB() throws Exception
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");

		//reset all filter options
		webd.getLogger().info("reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);
		//switch to grid view
		webd.getLogger().info("switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//check OOB delete protection
		DashboardHomeUtil.search(webd, "Application Performance");

		webd.click(DashBoardPageId.InfoBtnID);
		WebElement removeButton = webd.getWebDriver().findElement(By.xpath(DashBoardPageId.RmBtnID));
		Assert.assertFalse(removeButton.isEnabled());
	}

	@Test
	public void testFavorite() throws Exception
	{
		String dbName = "favorite_testDashboard";
		String dbDesc = "favorite_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testFavorite");

		//reset the home page
		webd.getLogger().info("reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, dbDesc, true);

		//set it to favorite
		webd.getLogger().info("set the dashboard to favorite");
		Assert.assertTrue(DashboardBuilderUtil.favoriteOption(webd));

		//verify the dashboard is favorite
		webd.getLogger().info("visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);

		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		//DashboardHomeUtil.isFilterOptionSelected(webd, "favorites");
		WebElement el = webd.getWebDriver().findElement(By.id(DashBoardPageId.Favorite_BoxID));
		Assert.assertTrue(el.isSelected());

		webd.getLogger().info("Verfiy the dashboard is favorite");
		DashboardHomeUtil.search(webd, dbName);

		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName);

		DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc, true);

		//set it to not favorite
		webd.getLogger().info("set the dashboard to not favorite");
		Assert.assertFalse(DashboardBuilderUtil.favoriteOption(webd));

		//verify the dashboard is not favoite
		webd.getLogger().info("visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);
		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		el = webd.getWebDriver().findElement(By.id(DashBoardPageId.Favorite_BoxID));
		Assert.assertTrue(el.isSelected());
		webd.getLogger().info("Verfiy the dashboard is not favorite");
		DashboardHomeUtil.search(webd, dbName);
		Assert.assertEquals(webd.getAttribute(DashBoardPageId.DashboardSerachResult_panelID + "@childElementCount"), "0");
		webd.getLogger().info("no favorite dashboard");

		//delete the dashboard
		webd.getLogger().info("start to delete the dashboard");
		el = webd.getWebDriver().findElement(By.id(DashBoardPageId.Favorite_BoxID));
		if (el.isSelected()) {
			el.click();
		}
		DashboardHomeUtil.deleteDashboard(webd, dbName, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("the dashboard has been deleted");
	}

	@Test(dependsOnMethods = { "testCreateDashboad_noWidget_GridView" })
	public void testModifyDashboard_namedesc() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testModifyDashBoard");

		//edit dashboard
		webd.getLogger().info("start to edit dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, "Test_Dashboard_no_Widget_GridView");
		DashboardHomeUtil.selectDashboard(webd, "Test_Dashboard_no_Widget_GridView");
		DashboardBuilderUtil.editDashboard(webd, "Test_Dashboard_no_Widget_GridView modify",
				"Test_Dashboard_no_Widget_GridView desc modify");
	}

	@Test(dependsOnMethods = { "testCreateDashboard_withWidget_GridView" })
	public void testModifyDashboard_widget() throws Exception
	{
		String WidgetName_1 = "Top Hosts by Log Entries";
		String WidgetName_2 = "Top 10 Listeners by Load";

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testModifyDashboard_descwidget");
		//search the dashboard want to modify
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, "AAA_testDashboard");

		//open the dashboard in the builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "AAA_testDashboard");

		//add the widget into the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");
		//DashboardBuilderUtil.showRightDrawer(webd);
		DashboardBuilderUtil.addWidgetByRightDrawer(webd, WidgetName_1);
		DashboardBuilderUtil.addWidgetByRightDrawer(webd, WidgetName_2);
		DashboardBuilderUtil.addWidgetByRightDrawer(webd, WidgetName_1);
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.save(webd);
	}

	@Test(dependsOnMethods = { "testSetHome" })
	public void testRemoveDashboard() throws Exception
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in removeDashboard");

		//delete dashboard
		//WelcomeUtil.visitDashboards(webd);
		webd.getLogger().info("switch to grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.search(webd, "SetHome_testDashboard");
		DashboardHomeUtil.deleteDashboard(webd, "SetHome_testDashboard", DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("the dashboard has been deleted");
	}

	@Test(dependsOnMethods = { "testCreateDashboad_noWidget_GridView", "testModifyDashboard_namedesc", "testShareDashboard",
			"testStopShareDashboard" })
	public void testRemoveDashboard_GridView() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashBoard");
		//search dashboard
		webd.getLogger().info("search the dashboard want to delete in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, "Test_Dashboard_no_Widget_GridView modify");
		//delte dashboard
		webd.getLogger().info("start to delete dashboard in grid view");
		DashboardHomeUtil.deleteDashboard(webd, "Test_Dashboard_no_Widget_GridView modify",
				DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
	}

	@Test(dependsOnMethods = { "testCreateDashboard_noWidget_ListView" })
	public void testRemoveDashboard_ListView() throws Exception
	{

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashBoard");
		//search dashboard
		webd.getLogger().info("search the dashboard want to delete in grid view");
		DashboardHomeUtil.listView(webd);
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, "Test_Dashboard_no_Widget_ListView");
		//delte dashboard
		webd.getLogger().info("start to delete dashboard in grid view");
		DashboardHomeUtil.deleteDashboard(webd, "Test_Dashboard_no_Widget_ListView", DashboardHomeUtil.DASHBOARDS_LIST_VIEW);
	}

	@Test(dependsOnMethods = { "testCreateDashboard_withWidget_GridView", "testModifyDashboard_widget",
			"testWidgetConfiguration" })
	public void testRemoveDashboardInBuilderPage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashBoard");
		//search dashboard
		webd.getLogger().info("search the dashboard want to delete in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, "AAA_testDashboard");
		//open the dashboard in builder page
		DashboardHomeUtil.selectDashboard(webd, "AAA_testDashboard");
		//delte the dashboard in builder page
		webd.getLogger().info("start to delete dashboard in builder page");
		DashboardBuilderUtil.deleteDashboard(webd);
	}

	@Test
	public void testSetHome() throws Exception
	{
		String dbName = "SetHome_testDashboard";
		String dbDesc = "SetHome_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testCreateDashboad_noWidget_GridView");

		//reset the home page
		webd.getLogger().info("reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, dbDesc, true);

		//set it as home
		Assert.assertTrue(DashboardBuilderUtil.toggleHome(webd));

		//check home page
		webd.getLogger().info("access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		DashboardBuilderUtil.verifyDashboard(webd, dbName, dbDesc, true);

		//set it not home
		Assert.assertFalse(DashboardBuilderUtil.toggleHome(webd));

		//check home page
		webd.getLogger().info("access to the home page");
		BrandingBarUtil.visitMyHome(webd);
		webd.getLogger().info("Verfiy the home page");
		WelcomeUtil.isServiceExistedInWelcome(webd, WelcomeUtil.SERVICE_NAME_DASHBOARDS);
	}

	@Test(dependsOnMethods = { "testCreateDashboad_noWidget_GridView", "testModifyDashboard_namedesc" })
	public void testShareDashboard() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testshareddashboard");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "Test_Dashboard_no_Widget_GridView modify");
		webd.getLogger().info("verify the dashboard is existed");
		DashboardHomeUtil.isDashboardExists(webd, "Test_Dashboard_no_Widget_GridView modify");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Test_Dashboard_no_Widget_GridView modify");

		//sharing dashbaord
		Assert.assertTrue(DashboardBuilderUtil.toggleShareDashboard(webd));
	}

	@Test(dependsOnMethods = { "testShareDashboard" })
	public void testStopShareDashboard() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testshareddashboard");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "Test_Dashboard_no_Widget_GridView modify");
		webd.getLogger().info("verify the dashboard is existed");
		DashboardHomeUtil.isDashboardExists(webd, "Test_Dashboard_no_Widget_GridView modify");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Test_Dashboard_no_Widget_GridView modify");

		//stop sharing dashbaord
		Assert.assertFalse(DashboardBuilderUtil.toggleShareDashboard(webd));
	}

	//@Test
	public void testTimePicker() throws Exception
	{
		String dbName = "Test Dashboard timeselector";
		String dbDesc = "Test Dashboard timeselector description";

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testTimePicker");

		//reset the home page
		webd.getLogger().info("reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName, dbDesc, true);

		//Add the widget to the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetByRightDrawer(webd, "Database Errors Trend");
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.save(webd);

		//set time range for time picker
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last15Mins);
		TimeSelectorUtil.setCustomTime(webd, "04/06/2015 07:22 PM", "04/07/2016 09:22 PM");

		//open the widget
		DashboardBuilderUtil.openWidget(webd, "Database Errors Trend");

		//verify the url
		webd.switchToWindow();

		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains("startTime")) {
			Assert.fail("not open the correct widget");
		}

	}

	@Test(dependsOnMethods = { "testCreateDashboard_withWidget_GridView", "testModifyDashboard_widget" })
	public void testWidgetConfiguration() throws Exception
	{
		String WidgetName_1 = "Database Errors Trend";
		String WidgetName_2 = "Top 10 Listeners by Load";

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testWidgetConfiguration");
		//search the dashboard want to modify
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, "AAA_testDashboard");

		//open the dashboard in the builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "AAA_testDashboard");

		//widget operation
		webd.getLogger().info("hide/show the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, WidgetName_1, false);
		DashboardBuilderUtil.showWidgetTitle(webd, WidgetName_2, true);
		webd.getLogger().info("resize the widget title");
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_WIDER);
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_1, DashboardBuilderUtil.TILE_NARROWER);
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_2, DashboardBuilderUtil.TILE_TALLER);
		DashboardBuilderUtil.resizeWidget(webd, WidgetName_2, DashboardBuilderUtil.TILE_SHORTER);
		webd.getLogger().info("remove the widget title");
		DashboardBuilderUtil.removeWidget(webd, WidgetName_1);
		DashboardBuilderUtil.removeWidget(webd, WidgetName_2);

		//save the dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.save(webd);

	}

}
