package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

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

		DashboardHomeUtil.exploreData(webd, PageId.ExploreDataMenu_Analyze);
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

		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		DashBoardUtils.clickExploreDataButton();
		DashBoardUtils.clickExploreDataMenuItem(PageId.ExploreDataMenu_Log);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//Log Analytics link
		//webd.click(DashBoardPageId.LALinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emlacore/html/log-analytics-search.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void testExploreData_SearchLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testTargetLink");

		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		DashBoardUtils.clickExploreDataButton();
		DashBoardUtils.clickExploreDataMenuItem(PageId.ExploreDataMenu_Search);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		//Target link
		//webd.click(DashBoardPageId.TargetLinkID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		String sub_str = url.substring(url.indexOf("emsaasui") + 9);
		Assert.assertEquals(sub_str.substring(0, 23), "emcta/ta/analytics.html");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void testUserMenu() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");

		//		DashBoardUtils.clickGVButton();
		//		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//
		//		//check OOB delete protection
		//		DashBoardUtils.searchDashBoard("Application Performance");
		//		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//
		//		webd.click(DashBoardPageId.InfoBtnID);
		//		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//		WebElement removeButton = webd.getWebDriver().findElement(By.xpath(DashBoardPageId.RmBtnID));
		//		Assert.assertFalse(removeButton.isEnabled());

		webd.click(PageId.MenuBtnID);
		//about menu
		webd.click(PageId.AboutID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		Assert.assertEquals(webd.getWebDriver().findElement(By.xpath(PageId.AboutContentID)).getText(),
				"Warning: Unauthorized access is strictly prohibited.");
		webd.click(PageId.AboutCloseID);

		//help menu
		//webd.click(DashBoardPageId.MenuBtnID);
		//webd.click(DashBoardPageId.HelpID);
		//DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Assert.assertEquals(webd.getWebDriver().findElement(By.xpath(DashBoardPageId.HelpContentID)).getText(),"Get Started");

		//signout menu
		//webd.click(DashBoardPageId.MenuBtnID);
		//webd.click(DashBoardPageId.SignOutID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void verify_allOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		DashBoardUtils.APM_OOB_GridView();
		DashBoardUtils.ITA_OOB_GridView();
		DashBoardUtils.LA_OOB_GridView();

		//sort func
		DashBoardUtils.clickToSortByLastAccessed();
		//check box
		//DashBoardUtils.clickCheckBox();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void verify_APMOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashBoardUtils.clickCheckBox(PageId.APM_BoxID);
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//verify APM oob display
		DashBoardUtils.APM_OOB_GridView();

		//reset cloud services checkbox
		DashBoardUtils.resetCheckBox(PageId.APM_BoxID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void verify_CreatedBy_Me_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Me");
		DashBoardUtils.clickCheckBox(PageId.Other_BoxID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//verify all the oob not exsit
		DashBoardUtils.noOOBCheck_GridView();

		//sort func
		DashBoardUtils.clickToSortByLastAccessed();
		//reset Created By checkbox
		DashBoardUtils.resetCheckBox(PageId.Other_BoxID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void verify_CreatedBy_Oracle_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashBoardUtils.clickCheckBox(PageId.Oracle_BoxID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//verify all the oob display
		DashBoardUtils.APM_OOB_GridView();
		DashBoardUtils.ITA_OOB_GridView();
		DashBoardUtils.LA_OOB_GridView();

		//sort func
		DashBoardUtils.clickToSortByLastAccessed();
		//reset Created By checkbox
		DashBoardUtils.resetCheckBox(PageId.Oracle_BoxID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void verify_ITAOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//select Cloud Services as IT Analytics
		webd.getLogger().info("select Cloud Services as IT Analytics");
		DashBoardUtils.clickCheckBox(PageId.ITA_BoxID);
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//verify APM oob display
		DashBoardUtils.ITA_OOB_GridView();

		//reset cloud services checkbox
		DashBoardUtils.resetCheckBox(PageId.ITA_BoxID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}

	@Test
	public void verify_LAOOB_GridView() throws Exception
	{
		//login the dashboard
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//select Cloud Services as Log Analytics
		webd.getLogger().info("select Cloud Services as Log Analytics");
		DashBoardUtils.clickCheckBox(PageId.LA_BoxID);
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);

		//verify APM oob display
		DashBoardUtils.LA_OOB_GridView();

		//reset cloud services checkbox
		DashBoardUtils.resetCheckBox(PageId.LA_BoxID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
	}
}
