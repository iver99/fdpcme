package oracle.sysman.emaas.platform.dashboards.test.ui;

import org.testng.Assert;
import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

public class TestHomePage extends LoginAndLogout
{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test
	public void testExploreData_AnalyzeLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAWRLink");

		DashboardHomeUtil.exploreData(webd, DashboardHomeUtil.EXPLOREDATA_MENU_ANALYZE);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9),
				"emcitas/flex-analyzer/html/displaying/new-chart-config.html");
		//DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}

	@Test
	public void testExploreData_LALink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLALink");

		DashboardHomeUtil.exploreData(webd, DashboardHomeUtil.EXPLOREDATA_MENU_LOG);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emlacore/html/log-analytics-search.html");
		//DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void testExploreData_SearchLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testTargetLink");

		DashboardHomeUtil.exploreData(webd, DashboardHomeUtil.EXPLOREDATA_MENU_SEARCH);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		String sub_str = url.substring(url.indexOf("emsaasui") + 9);
		Assert.assertEquals(sub_str.substring(0, 23), "emcta/ta/analytics.html");
		//DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void testUserMenu() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");

		BrandingBarUtil.userMenuOptions(webd, BrandingBarUtil.USERMENU_OPTION_ABOUT);

		//		BrandingBarUtil.userMenuOptions(webd, DashBoardPageId.Brand_Bar_User_Menu_About_Option);
		//		Assert.assertEquals(webd.getWebDriver().findElement(By.xpath(PageId.AboutContentID)).getText(),
		//				"Warning: Unauthorized access is strictly prohibited.");
		//		webd.click(PageId.AboutCloseID);

	}

	@Test
	public void verify_allOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");
		//click Grid View icon
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		DashBoardUtils.APM_OOB_GridView();
		DashBoardUtils.ITA_OOB_GridView();
		DashBoardUtils.LA_OOB_GridView();

		//sort func
		DashboardHomeUtil.sortBy(webd, DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC);
		//check box
		//DashBoardUtils.clickCheckBox();
		//DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void verify_APMOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");
		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashboardHomeUtil.filterOptions(webd, "apm");
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);
		//DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

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
		webd.getLogger().info("start to test in testHomePage");
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
		webd.getLogger().info("start to test in testHomePage");

		//click Grid View icon
		DashboardHomeUtil.gridView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashboardHomeUtil.filterOptions(webd, "oracle");

		//verify all the oob display
		DashboardHomeUtil.waitForDashboardPresent(webd, "Database Health Summary");
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
		webd.getLogger().info("start to test in testHomePage");

		//select Cloud Services as IT Analytics
		webd.getLogger().info("select Cloud Services as IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashBoardUtils.clickGVButton();
		//verify APM oob display
		DashboardHomeUtil.gridView(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test
	public void verify_LAOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");

		//select Cloud Services as Log Analytics
		webd.getLogger().info("select Cloud Services as Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashBoardUtils.clickGVButton();

		//verify APM oob display
		DashBoardUtils.LA_OOB_GridView();

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}
}
