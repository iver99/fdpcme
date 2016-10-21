package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class BugVerification extends LoginAndLogout
{

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@AfterClass
	public void removeTestData()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: removeTestData");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to the grid view
		webd.getLogger().info("Swtich to the grid view");
		DashboardHomeUtil.gridView(webd);

		//remove the test data
		webd.getLogger().info("Start to remove the test data...");

		DashBoardUtils.deleteDashboard(webd, "Dashboard_EMCPDF2040");

		webd.getLogger().info("All test data have been removed");
	}

	@Test
	public void testEMCPDF_2040()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testEMCPDF_2040");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: no description, with time refresh");
		DashboardHomeUtil.createDashboard(webd, "Dashboard_EMCPDF2040", null, DashboardHomeUtil.DASHBOARD);
		webd.getLogger().info("verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, "Dashboard_EMCPDF2040", null, true),
				"Create dashboard failed!");

		//set the timepicker as Custom
		webd.getLogger().info("Set the custom date time");
		Assert.assertNotNull(TimeSelectorUtil.setCustomTime(webd, "04/14/2016 12:00 AM", "04/14/2016 12:30 PM"),
				"The restun date time is null");

	}

	@Test
	public void testEMPCDF_812_1()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_812");

		//reset all filter options
		DashboardHomeUtil.resetFilterOptions(webd);

		//check ita box
		DashboardHomeUtil.filterOptions(webd, "ita");

		//check la box
		DashboardHomeUtil.filterOptions(webd, "la");

		//signout menu
		webd.click(PageId.MENUBTNID);
		webd.click(PageId.SIGNOUTID);

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_812");

		//check ita box
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "ita"));

		//check la box
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "la"));

		//check ita box
		DashboardHomeUtil.filterOptions(webd, "ita");

		//check la box
		DashboardHomeUtil.filterOptions(webd, "la");

	}

	@Test
	public void testEMPCDF_832_1()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_832");

		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("current url = " + url);

		webd.getWebDriver().navigate()
				.to(url.substring(0, url.indexOf("emsaasui")) + "emsaasui/emcpdfui/error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG");
		webd.waitForElementPresent("css=" + PageId.ERRORPAGESINGOUTBTNCSS);
		webd.takeScreenShot();

		webd.click("css=" + PageId.ERRORPAGESINGOUTBTNCSS);
		webd.getLogger().info("Sing out button is clicked");
		webd.takeScreenShot();

		//initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		login(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName(), "welcome");
		webd.getLogger().info("welcome page is being loaded, going to to verify...");

		//DashboardHomeUtil.gridView(webd);
		//Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.WelcomeID),"Welcome to Oracle Management Cloud");

		WelcomeUtil.isServiceExistedInWelcome(webd, WelcomeUtil.SERVICE_NAME_DASHBOARDS);
		webd.getLogger().info("welcome page is verified successfully");
		webd.getLogger().info("complete testing in testEMPCDF_832");
	}

}
