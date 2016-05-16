package oracle.sysman.emaas.platform.dashboards.test.DPdashboard;


import org.testng.Assert;

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
       private String dbName_noWidgetGrid = "";
       

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}


          @Test
	  	public void testCreateDashboad_noWidget_GridView() throws Exception
	  	{
	  		dbName_noWidgetGrid = "AAA_testDashboard" + generateTimeStamp();;
	  		String dbDesc = "AAA_testDashBoard desc";
	  
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
                      webd.getLogger().info("Create a dashboard: with description, time refresh");
	         	DashboardHomeUtil.createDashboard(webd, dbName_noWidgetGrid, dbDesc, DashboardHomeUtil.DASHBOARD);
	        	webd.getLogger().info("verify the dashboard created Successfully");
	        	Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_noWidgetGrid, dbDesc, true),
	  			"Create dashboard failed!");
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
		DashboardHomeUtil.search(webd, "AAA_testDashboard");
		DashboardHomeUtil.selectDashboard(webd, "AAA_testDashboard");
		DashboardBuilderUtil.editDashboard(webd, "AAA_testDashboard_modify",
				"AAA_testDashboard_desc_modify");
	}

	@Test(dependsOnMethods = { "testCreateDashboad_noWidget_GridView","testModifyDashboard_namedesc" })
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
		webd.getLogger().info("Add Widget 'Top Hosts by Log Entries' into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_1);
		webd.getLogger().info("Add Widget 'Top 10 Listeners by Load' into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_2);
		webd.getLogger().info("Add Widget 'Top Hosts by Log Entries' into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, WidgetName_1);
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}
 
         private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

}

