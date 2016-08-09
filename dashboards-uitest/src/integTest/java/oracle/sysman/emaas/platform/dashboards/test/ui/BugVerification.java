package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version
 * @author charles.c.chen
 * @since release specific (what release of product did this appear in)
 */

public class BugVerification extends LoginAndLogout
{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test
	public void testEMPCDF_812_1() throws Exception
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
	public void testEMPCDF_832_1() throws Exception
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
