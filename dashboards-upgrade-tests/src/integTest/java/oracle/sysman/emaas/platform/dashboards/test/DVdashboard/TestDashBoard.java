package oracle.sysman.emaas.platform.dashboards.test.DVdashboard;

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
         //private String dbName_withWidgetGrid = "";
         private String dbName_setHome = "";
         private String dbName_noWidgetGrid = "";


	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}


        @Test
         public void testSetHome() throws Exception
		{
			dbName_setHome = "setHomeDashboard-" + generateTimeStamp();
			String dbDesc = "SetHome_testDashboard desc";
	
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

	
			//verify dashboard in builder page
			webd.getLogger().info("Verify the dashboard created Successfully");
			Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_setHome, dbDesc, true), "Create dashboard failed!");
	
			//set it as home
			webd.getLogger().info("Set home page");
			Assert.assertTrue(DashboardBuilderUtil.toggleHome(webd), "Set the dasbhoard as Home failed!");
	
			//check home page
			webd.getLogger().info("Access to the home page");
			BrandingBarUtil.visitMyHome(webd);
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

	@Test
	public void testShareDashboard() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testshareddashboard");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, "AAA_testDashboard_modify");
		webd.getLogger().info("verify the dashboard is existed");
		DashboardHomeUtil.isDashboardExisted(webd, "AAA_testDashboard_modify");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "AAA_testDashboard_modify");

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
		DashboardHomeUtil.search(webd, "AAA_testDashboard_modify");
		webd.getLogger().info("verify the dashboard is existed");
		DashboardHomeUtil.isDashboardExisted(webd, "AAA_testDashboard_modify");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "AAA_testDashboard_modify");

		//stop sharing dashbaord
		Assert.assertFalse(DashboardBuilderUtil.toggleShareDashboard(webd));
	}

      private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

	

}


