package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.annotations.Test;

public class TestHomePage_BasicTest extends LoginAndLogout
{
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void testExploreData_LALink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testExploreData_LALink");

		DashboardHomeUtil.gotoDataExplorer(webd, DashboardHomeUtil.EXPLOREDATA_MENU_LOG);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");
	}

	@Test(alwaysRun = true)
	public void testExploreData_SearchLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testExploreData_SearchLink");

		DashboardHomeUtil.gotoDataExplorer(webd, DashboardHomeUtil.EXPLOREDATA_MENU_SEARCH);
		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcta/ta/analytics.html");
	}

	@Test(alwaysRun = true)
	public void testUserMenu()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");

		BrandingBarUtil.userMenuOptions(webd, BrandingBarUtil.USERMENU_OPTION_ABOUT);

	}

	@Test(alwaysRun = true)
	public void verify_allOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_allOOB_GridView");

		//switch to list view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);
	}

	@Test(alwaysRun = true)
	public void verify_allOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_allOOB_ListView");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);
	}

	@Test(alwaysRun = true)
	public void verify_APMOOB_GridView()
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
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_APMOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_APMOOB_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify APM oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Me_GridView()
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
		DashBoardUtils.noOOBCheck(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Me_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Me_ListView");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Me");
		DashboardHomeUtil.filterOptions(webd, "me");

		//verify all the oob not exsit
		DashBoardUtils.noOOBCheck(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Oracle_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Oracle_GridView");

		//switch to list view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashboardHomeUtil.filterOptions(webd, "oracle");

		//verify all the oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_CreatedBy_Oracle_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_CreatedBy_Oracle_ListView");

		//switch to list view
		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashboardHomeUtil.filterOptions(webd, "oracle");

		//verify all the oob display
		DashBoardUtils.apmOobExist(webd);
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);
		DashBoardUtils.securityOobExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_ITAOOB_GridView()
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
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_ITAOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_ITAOOB_GridView");

		//select Cloud Services as IT Analytics
		webd.getLogger().info("select Cloud Services as IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify ITA oob display
		DashBoardUtils.itaOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_LAOOB_GridView()
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
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_LAOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_LAOOB_GridView");

		//select Cloud Services as Log Analytics
		webd.getLogger().info("select Cloud Services as Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify LA oob display
		DashBoardUtils.laOobExist(webd);
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.outDateOob(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_OrchestrationOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_OrchestrationOOB_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify Orchestration oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_OrchestrationOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_OrchestrationOOB_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify Orchestration oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.securityOobNotExist(webd);
		DashBoardUtils.orchestrationOobExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_SecurityOOB_GridView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_SecurityOOB_GridView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Security");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify security oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void verify_SecurityOOB_ListView()
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verify_SecurityOOB_ListView");

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as Security");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click Grid View icon
		webd.getLogger().info("click List View icon");
		DashboardHomeUtil.listView(webd);

		//verify APM oob display
		DashBoardUtils.apmOobNotExist(webd);
		DashBoardUtils.itaOobNotExist(webd);
		DashBoardUtils.laOobNotExist(webd);
		DashBoardUtils.orchestrationOobNotExist(webd);
		DashBoardUtils.securityOobExist(webd);

		//reset cloud services checkbox
		DashboardHomeUtil.resetFilterOptions(webd);
	}
}