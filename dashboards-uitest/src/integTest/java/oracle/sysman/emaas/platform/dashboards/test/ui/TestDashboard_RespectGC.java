package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestDashboard_RespectGC extends LoginAndLogout
{
	private String dbRespectGCTimeRangeName = "";
	private final String dbRespectGCEntityName = "";

	private final String widgetName_LA = "Database Errors Trend";
	private final String widgetName_UDE = "Area Chart";

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);
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
		DashBoardUtils.deleteDashboard(webd, dbRespectGCTimeRangeName);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test
	public void testRespectGCTimeRange() throws InterruptedException
	{
		dbRespectGCTimeRangeName = "respectGC TimeRange-" + generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger()
				.info("start to test the performance when creating dashboard with tenant that doesn't have ITA privilege");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbRespectGCTimeRangeName, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbRespectGCTimeRangeName, null, true),
				"Create dashboard failed!");

		//add widget to the dashboard
		webd.getLogger().info("Add widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName_UDE);

		//respect GC for TimeRange
		webd.getLogger().info("Respect GC for Time Range");
		DashboardBuilderUtil.respectGCForTimeRange(webd);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//change the time range, verify the url will be changed
		webd.getLogger().info("Change the time range, verify the url will be changed");
		GlobalContextUtil.setTimeRange(webd, TimeRange.Last7Days);

		String currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_7_DAY"), true);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, widgetName_UDE);

		WaitUtil.waitForPageFullyLoaded(webd);

		Assert.assertEquals(GlobalContextUtil.getTimeRangeLabel(webd).contains("Last 7 days"), true);
	}

	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
