package oracle.sysman.emaas.platform.dashboards.test.DPdashboard;

//import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
//import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
//import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
//import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
//import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
//import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
//import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil.TimeRange;
//import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
//import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;

//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
import org.testng.Assert;
//import org.testng.annotations.Test;

import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import oracle.sysman.emaas.platform.dashboards.test.util.*;


import java.util.Set;
import org.testng.Assert;




/**
 * @version
 * @author siakula
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
		DashboardBuilderUtil.saveDashboard(webd);

		//open the widget
		webd.getLogger().info("open the widget");
		DashboardBuilderUtil.openWidget(webd, "Database Errors Trend");
		
		webd.switchToWindow();

		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains(
				"emlacore/html/log-analytics-search.html?widgetId=2013&dashboardId")) {
			Assert.fail("not open the correct widget");
		}

	}

	

	@Test(dependsOnMethods = { "testCreateDashboard_withWidget_GridView" })
	public void testModifyDashboard_namedesc() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testModifyDashBoard");

		//edit dashboard
		webd.getLogger().info("start to edit dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, "AAA_testDashboard");
		DashboardHomeUtil.selectDashboard(webd, "AAA_testDashboard");
		DashboardBuilderUtil.editDashboard(webd, "AAA_testDashboard_modify",
				"AAA_testDashboard_desc_modify");
	}

	@Test(dependsOnMethods = { "testCreateDashboard_withWidget_GridView","testModifyDashboard_namedesc" })
	public void testModifyDashboard_widget() throws Exception
	{
		String WidgetName_1 = "Top Hosts by Log Entries";
		String WidgetName_2 = "Top 10 Listeners by Load";

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testModifyDashboard_descwidget");
		//search the dashboard want to modify
		webd.getLogger().info("search dashboard");
		DashboardHomeUtil.search(webd, "AAA_testDashboard_modify");

		//open the dashboard in the builder page
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "AAA_testDashboard_modify");

		//add the widget into the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");
		//DashboardBuilderUtil.showRightDrawer(webd);
		DashboardBuilderUtil.addWidgetByRightDrawer(webd, WidgetName_1);
		DashboardBuilderUtil.addWidgetByRightDrawer(webd, WidgetName_2);
		DashboardBuilderUtil.addWidgetByRightDrawer(webd, WidgetName_1);
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}
}
