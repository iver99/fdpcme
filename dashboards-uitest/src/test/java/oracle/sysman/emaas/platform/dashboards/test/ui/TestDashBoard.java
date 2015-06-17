package oracle.sysman.emaas.platform.dashboards.test.ui;

import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.*;

import java.util.Set;
import org.testng.Assert;

/**
 *  @version
 *  @author  charles.c.chen
 *  @since   release specific (what release of product did this appear in)
 */

public class TestDashBoard extends LoginAndLogout{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName()+"."+testName);
		DashBoardUtils.loadWebDriver(webd);
	}
	
	@Test
	public void testHomepage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");	
		DashBoardUtils.checkBrandingBarLink();
				
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Application_Performance_Monitoring_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Performance_Analytics_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Resource_Planning_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Garbage_Collection_Overhead_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Host_Inventory_By_Platform_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Configuration_and_Storage_By_Version_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.WebLogic_Servers_by_JDK_Version_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_Databases_by_Resource_Consumption_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_WebLogic_Servers_by_Heap_Usage_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_WebLogic_Servers_by_Load_ID));
		//Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Health_Summary_ID));
		//Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.WebLogic_Health_Summary_ID));
		//Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Host_Health_Summary_ID));
		//sort func
		DashBoardUtils.clickToSortByLastAccessed();
		//check box
		DashBoardUtils.clickCheckBox();
		
	}
	
	@Test
	public void testCreateDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testCreateDashBoard");
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
				
		DashBoardUtils.openDBCreatePage();
		DashBoardUtils.inputDashBoardInfo();
		
		DashBoardUtils.waitForMilliSeconds(500);
		
		DashBoardUtils.clickOKButton();		
					
		//add widget
		DashBoardUtils.addWidget(1,parentWindow);
				
		DashBoardUtils.waitForMilliSeconds(500);
		
		webd.takeScreenShot();
			
	}
	
	
	@Test
	public void testModifyDashBoard() throws Exception
	{
					
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testModifyDashBoard");
		String parentWindow = webd.getWebDriver().getWindowHandle();
		//open dashboard	
		DashBoardUtils.waitForMilliSeconds(500);
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("AAA_testDashboard");
		DashBoardUtils.waitForMilliSeconds(500);
		webd.takeScreenShot();
		DashBoardUtils.clickDashBoard();
		//String parentWindow = webd.getWebDriver().getWindowHandle();
		DashBoardUtils.waitForMilliSeconds(500);
		//add a new widget
		DashBoardUtils.addWidget(0,parentWindow);
		
		DashBoardUtils.waitForMilliSeconds(500);
		
		webd.takeScreenShot();
	}
	
	
	
	@Test
	public void testNavigateWidget() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testModifyDashBoard");
		String parentWindow = webd.getWebDriver().getWindowHandle();
		//open dashboard	
		DashBoardUtils.waitForMilliSeconds(500);
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("AAA_testDashboard");
		DashBoardUtils.waitForMilliSeconds(500);
		webd.takeScreenShot();
		DashBoardUtils.clickDashBoard();
		
		DashBoardUtils.waitForMilliSeconds(500);
		
		DashBoardUtils.navigateWidget(parentWindow);
		
	}
	
	@Test
	public void testRemoveDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashBoard");	
		//focus a dashboard
		DashBoardUtils.waitForMilliSeconds(500);
		webd.takeScreenShot();
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("AAA_testDashboard");
		DashBoardUtils.waitForMilliSeconds(500);
		webd.takeScreenShot();
		WebElement mainelement = webd.getElement(DashBoardPageId.ElementID);
		WebElement deletebutton = webd.getElement(DashBoardPageId.DeleteBtnID);
        Actions builder = new Actions(webd.getWebDriver());
        builder.moveToElement(mainelement).moveToElement(deletebutton).click().perform();
        
		//click delete button
		DashBoardUtils.clickDeleteButton();
		DashBoardUtils.waitForMilliSeconds(500);
		
		webd.takeScreenShot();
					
	}
	
	
	
}
