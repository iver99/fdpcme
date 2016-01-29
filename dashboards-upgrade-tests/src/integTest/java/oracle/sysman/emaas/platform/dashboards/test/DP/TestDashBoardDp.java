package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import org.testng.Assert;




import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.*;

import java.util.Set;
import org.testng.Assert;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtil;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardPageId;


public class TestDashBoard extends LoginAndLogout{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName()+"."+testName);
		DashBoardUtils.loadWebDriver(webd);
	}		
	
	@Test
	public void testCreateDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testCreateDashBoard");
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
				
		DashBoardUtils.openDBCreatePage();
		String dbName="AAA_testDashboard";
		String dbDesc="AAA_testDashBoard desc";
		DashBoardUtils.inputDashBoardInfo(dbName,dbDesc);
		//verify input info's existence
		//Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.DashBoardNameBoxID),"AAA_testDashboard");
		webd.getLogger().info("Name = "+DashBoardUtils.getTextByID(DashBoardPageId.DashBoardNameBoxID));
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		
		DashBoardUtils.clickOKButton();		
		
		webd.takeScreenShot();
		//add widget
		DashBoardUtils.addWidget(1,parentWindow,"AAA_testDashboard","AAA_testDashBoard desc");
				
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		
		webd.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
	}
	
	
	@Test(dependsOnMethods = { "testCreateDashBoard" })
	public void testModifyDashBoard() throws Exception
	{
					
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testModifyDashBoard");
		
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);;
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
		//open dashboard	
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("AAA_testDashboard");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		webd.takeScreenShot();
		DashBoardUtils.clickDashBoard();
		//String parentWindow = webd.getWebDriver().getWindowHandle();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		//add a new widget
		DashBoardUtils.addWidget(0,parentWindow,"AAA_testDashboard","AAA_testDashBoard desc");
		
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		webd.takeScreenShot();
	}	
	
	@Test(dependsOnMethods = { "testModifyDashBoard" })
	public void testNavigateWidget() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testNavigateDashBoard");
		
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
		//open dashboard	
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("AAA_testDashboard");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		webd.takeScreenShot();
		DashBoardUtils.clickDashBoard();
		
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		DashBoardUtils.navigateWidget(parentWindow);
		DashBoardUtils.waitForMilliSeconds(2*DashBoardPageId.Delaytime_long);
	}
	
	@Test(dependsOnMethods = { "testCreateDashBoard","testModifyDashBoard","testNavigateWidget"})
	//@Test
	public void testRemoveDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashBoard");	
		//focus a dashboard
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		webd.takeScreenShot();
		
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("AAA_testDashboard");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.click(DashBoardPageId.InfoBtnID);
		webd.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.click(DashBoardPageId.RmBtnID);
		webd.takeScreenShot();
		
		//click delete button
		DashBoardUtils.clickDeleteButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		webd.takeScreenShot();
					
	}
	
	@Test
	public void testCreateSpecialDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testCreateSpecialDashBoard");
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
				
		DashBoardUtils.openDBCreatePage();
		String dbName="testDashboard_Spec";
		String dbDesc="testDashboard_Spec_Desc";
		DashBoardUtils.inputDashBoardInfo(dbName,dbDesc);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//verify input info's existence
		//Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.DashBoardNameBoxID),"AAA_testDashboard");
		webd.getLogger().info("Name = "+DashBoardUtils.getTextByID(DashBoardPageId.DashBoardNameBoxID));
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		
		DashBoardUtils.clickOKButton();		
		
		webd.takeScreenShot();
		String widgetName = "Database Errors Trend";
		//add widget
		DashBoardUtils.addWidget(1,parentWindow,widgetName,"DBA_Name_Modify","DBA_DESC_MODIFY");
				
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		webd.takeScreenShot();
			
	}
	
	@Test(dependsOnMethods = { "testCreateSpecialDashBoard" })
	//@Test
	public void testRemoveSpecialDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashBoard");	
		
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//focus a dashboard
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		webd.takeScreenShot();
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("DBA_Name_Modify");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		webd.click(DashBoardPageId.InfoBtnID);
		webd.click(DashBoardPageId.RmBtnID);
		
		//click delete button
		DashBoardUtils.clickDeleteButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		webd.takeScreenShot();
					
	}	


	@Test
	public void testCreateLVDashBoard() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testCreateLVDashBoard");
		
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		DashBoardUtils.clickLVButton();
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
		
		DashBoardUtils.openDBCreatePage();
		
		String dbName="LV_DashBoard";
		String dbDesc = "LV_DashBoard Desc";
		DashBoardUtils.inputDashBoardInfo(dbName,dbDesc);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//verify input info's existence
		//Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.DashBoardNameBoxID),"AAA_testDashboard");
		webd.getLogger().info("Name = "+DashBoardUtils.getTextByID(DashBoardPageId.DashBoardNameBoxID));
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		DashBoardUtils.clickOKButton();		
		
		webd.takeScreenShot();
		String widgetName = "Database Errors Trend";
		//add widget
		DashBoardUtils.addWidget(1,parentWindow,dbName,dbDesc);
				
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		webd.takeScreenShot();
	}

	@Test(dependsOnMethods = { "testCreateLVDashBoard" })
	public void testModifyLVDashBoard() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testModifyLVDashBoard");
		
		DashBoardUtils.clickLVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
		//open dashboard	
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("LV_DashBoard");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		DashBoardUtils.clickLVDashBoard();
		//String parentWindow = webd.getWebDriver().getWindowHandle();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//String widgetName = "Database Errors Trend";
		//add a new widget
		DashBoardUtils.addWidget(0,parentWindow,"LV_DashBoard","LV_DashBoard Desc");
		
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		webd.takeScreenShot();
	}

	@Test(dependsOnMethods = { "testCreateLVDashBoard","testModifyLVDashBoard" })
	public void testRemoveLVDashBoard() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveLVDashBoard");
		
		DashBoardUtils.clickLVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		DashBoardUtils.searchDashBoard("LV_DashBoard");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		webd.click(DashBoardPageId.DashBoardInfoID);
		webd.click(DashBoardPageId.DashBoardDeleteID);
		
		//click delete button
		DashBoardUtils.clickLVDeleteButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		webd.takeScreenShot();
	}
	

      
	 
	
}

