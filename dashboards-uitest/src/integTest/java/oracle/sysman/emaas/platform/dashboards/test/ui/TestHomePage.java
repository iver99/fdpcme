package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestHomePage extends LoginAndLogout
{

	@BeforeClass
	public void createDashboard() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to create a dashboard for test");

		//create a dashboard
		DashboardHomeUtil.createDashboard(webd, "ADashboard Test", "", DashboardHomeUtil.DASHBOARD);
		DashboardBuilderUtil.verifyDashboard(webd, "ADashboard Test", "", false);
	}

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@AfterClass
	public void removeDashboard() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to create a dashboard for test");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//remove the dashboard
		DashboardHomeUtil.deleteDashboard(webd, "ADashboard Test", DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

	}

	@Test
	public void testExploreData_LALink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testExploreData_LALink");

		DashboardHomeUtil.gotoDataExplorer(webd, DashboardHomeUtil.EXPLOREDATA_MENU_LOG);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emlacore/html/log-analytics-search.html");
	}

	@Test
	public void testExploreData_SearchLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testExploreData_SearchLink");

		DashboardHomeUtil.gotoDataExplorer(webd, DashboardHomeUtil.EXPLOREDATA_MENU_SEARCH);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		String sub_str = url.substring(url.indexOf("emsaasui") + 9);
		Assert.assertEquals(sub_str.substring(0, 23), "emcta/ta/analytics.html");
	}

	@Test
	public void TestSortBy_createdBy_ListView() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by in list view");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify the sort by is Default
		Assert.assertEquals(webd.getText(PageId.SortbyID).trim(), "Default");

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by created by Ascending");
		DashboardHomeUtil.sortListViewByCreateBy(webd);

		//WaitUtil.waitForPageFullyLoaded(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Ascending");
		Assert.assertEquals(webd.getText(PageId.SortbyID).trim(), "Created By Ascending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		//webd.waitForElementPresent(PageId.ADashboardTestByAriaLabel);
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");

		//sort the dashboard by name Descending
		webd.getLogger().info("Sort the dashboard by created by Descending");
		DashboardHomeUtil.sortListViewByCreateBy(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Descending");
		Assert.assertEquals(webd.getText(PageId.SortbyID).trim(), "Created By Descending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		//webd.waitForElementPresent(PageId.ADashboardTestByAriaLabel);
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Created By Decending' is " + names.get(names.size() - 1));

	}

	@Test
	public void testSortBy_createdByAscending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by created by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
	}

	@Test
	public void testSortBy_createdByDecending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by decending");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by created by Decending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Created By Decending' is " + names.get(names.size() - 1));
	}

	@Test
	public void testSortBy_creationDateAscending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with creation date by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by creation date by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC);

		//verify the result
//		webd.getLogger().info("Verify the sort result");
//		DashboardHomeUtil.waitForDashboardPresent(webd, "Enterprise Overview");
//		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
//		Assert.assertEquals(names.get(0), "Enterprise Overview");
//		webd.getLogger().info("The first dashboard sorted by 'Creation Date Ascending' is " + names.get(0));
	}

	@Test
	public void testSortBy_creationDateDecending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with creation date by decending");

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by creation date by Decending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "Enterprise Overview");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "Enterprise Overview");
		webd.getLogger().info("The first dashboard sorted by 'Creation Date Ascending' is " + names.get(names.size() - 1));

	}

	@Test
	public void testSortBy_lastAccessedAscending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		DashboardHomeUtil.closeOverviewPage(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Last Access Ascending' is " + names.get(names.size() - 1));
	}

	@Test
	public void testSortBy_lastAccessedDecending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		DashboardHomeUtil.closeOverviewPage(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Decending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
		webd.getLogger().info("The first dashboard sorted by 'Last Access Decending' is " + names.get(0));
	}

	@Test
	public void TestSortBy_LastModified_ListView() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with last modified in list view");

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//edit the dashboard in builder page
		DashboardBuilderUtil.editDashboard(webd, "ADashboard Test", "ADashboard Test desc2",false);
		DashboardBuilderUtil.saveDashboard(webd);

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		DashboardHomeUtil.closeOverviewPage(webd);

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify the sort by is Default
		Assert.assertEquals(webd.getText(PageId.SortbyID).trim(), "Default");

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last modified Ascending");
		DashboardHomeUtil.sortListViewByLastModified(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Ascending");
		Assert.assertEquals(webd.getText(PageId.SortbyID).trim(), "Last Modified Ascending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Created By Decending' is " + names.get(names.size() - 1));

		//sort the dashboard by name Descending
		webd.getLogger().info("Sort the dashboard by last modifiedi Descending");
		DashboardHomeUtil.sortListViewByLastModified(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Descending");
		Assert.assertEquals(webd.getText(PageId.SortbyID).trim(), "Last Modified Descending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
	}

	@Test
	public void testSortBy_lastModifiedAscending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//edit the dashboard in builder page
		DashboardBuilderUtil.editDashboard(webd, "ADashboard Test", "ADashboard Test desc1",false);
		DashboardBuilderUtil.saveDashboard(webd);

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		//WaitUtil.waitForPageFullyLoaded(webd);

		DashboardHomeUtil.closeOverviewPage(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Last Modified Ascending' is " + names.get(names.size() - 1));
	}

	@Test
	public void testSortBy_lastModifiedDecending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with created by ascendingt");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "ADashboard Test");

		//open the dashboard in builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "ADashboard Test");

		//edit the dashboard in builder page
		DashboardBuilderUtil.editDashboard(webd, "ADashboard Test", "ADashboard Test desc2",false);
		DashboardBuilderUtil.saveDashboard(webd);

		//back to home page
		webd.getLogger().info("back to the dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		//WaitUtil.waitForPageFullyLoaded(webd);

		DashboardHomeUtil.closeOverviewPage(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by last accessed by Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
		webd.getLogger().info("The first dashboard sorted by 'Last Modified Decending' is " + names.get(0));
	}

	@Test
	public void TestSortBy_Name_ListView() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with name in list view");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify the sort by is Default
		Assert.assertEquals(webd.getText(PageId.SortbyID).trim(), "Default");

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by name Ascending");
		DashboardHomeUtil.sortListViewByName(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Ascending");
		Assert.assertEquals(webd.getText(PageId.SortbyID).trim(), "Name Ascending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");

		//sort the dashboard by name Descending
		webd.getLogger().info("Sort the dashboard by name Descending");
		DashboardHomeUtil.sortListViewByName(webd);

		//verify the result
		webd.getLogger().info("Verify the sort result -- Descending");
		Assert.assertEquals(webd.getText(PageId.SortbyID).trim(), "Name Descending");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");

		names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Created By Decending' is " + names.get(names.size() - 1));

	}

	@Test
	public void testSortBy_nameAscending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with name ascending");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by name Ascending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_ASC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(0), "ADashboard Test");
		webd.getLogger().info("The first dashboard sorted by 'Name Ascending' is " + names.get(0));
	}

	@Test
	public void testSortBy_nameDecending() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test sort by dashboards with name decending");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//sort the dashboard by name Ascending
		webd.getLogger().info("Sort the dashboard by name Decending");
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_DSC);

		//verify the result
		webd.getLogger().info("Verify the sort result");
		DashboardHomeUtil.waitForDashboardPresent(webd, "ADashboard Test");
		List<String> names = DashboardHomeUtil.listDashboardNames(webd);
		Assert.assertEquals(names.get(names.size() - 1), "ADashboard Test");
		webd.getLogger().info("The last dashboard sorted by 'Name Decending' is " + names.get(names.size() - 1));
	}

	@Test
	public void testUserMenu() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");

		BrandingBarUtil.userMenuOptions(webd, BrandingBarUtil.USERMENU_OPTION_ABOUT);

	}

	@Test
	public void verify_allOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_allOOB_GridView");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		DashBoardUtils.APM_OOB_GridView();
		DashBoardUtils.ITA_OOB_GridView();
		DashBoardUtils.LA_OOB_GridView();
	}

	@Test
	public void verify_APMOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_APMOOB_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify APM oob display
		DashBoardUtils.APM_OOB_GridView();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test
	public void verify_CreatedBy_Me_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Me_GridView");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Me");
		DashboardHomeUtil.filterOptions(webd, "me");

		//verify all the oob not exsit
		DashBoardUtils.noOOBCheck_GridView();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test
	public void verify_CreatedBy_Oracle_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Oracle_GridView");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.gridView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashboardHomeUtil.filterOptions(webd, "oracle");

		//verify all the oob display
		//DashboardHomeUtil.waitForDashboardPresent(webd, "Database Health Summary");
		DashBoardUtils.APM_OOB_GridView();
		DashBoardUtils.ITA_OOB_GridView();
		DashBoardUtils.LA_OOB_GridView();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test
	public void verify_ITAOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_ITAOOB_GridView");

		//select Cloud Services as IT Analytics
		webd.getLogger().info("select Cloud Services as IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify ITA oob display
		DashBoardUtils.ITA_OOB_GridView();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test
	public void verify_LAOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_LAOOB_GridView");

		//select Cloud Services as Log Analytics
		webd.getLogger().info("select Cloud Services as Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify LA oob display
		DashBoardUtils.LA_OOB_GridView();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}
}
