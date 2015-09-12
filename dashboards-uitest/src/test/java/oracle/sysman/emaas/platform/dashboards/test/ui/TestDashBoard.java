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
		//DashBoardUtils.checkBrandingBarLink();
		DashBoardUtils.waitForMilliSeconds(9000);	
		
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
		//DashBoardUtils.clickCheckBox();
		
	}
	
	@Test
	public void testCreateDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testCreateDashBoard");
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
				
		DashBoardUtils.openDBCreatePage();
		String dbName="AAA_testDashboard";
		DashBoardUtils.inputDashBoardInfo(dbName);
		//verify input info's existence
		//Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.DashBoardNameBoxID),"AAA_testDashboard");
		webd.getLogger().info("Name = "+DashBoardUtils.getTextByID(DashBoardPageId.DashBoardNameBoxID));
		DashBoardUtils.waitForMilliSeconds(500);
		
		DashBoardUtils.clickOKButton();		
		
		webd.takeScreenShot();
		//add widget
		DashBoardUtils.addWidget(1,parentWindow);
				
		DashBoardUtils.waitForMilliSeconds(500);
		
		webd.takeScreenShot();
			
	}
	
	
	@Test(dependsOnMethods = { "testCreateDashBoard" })
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
	
	
	
	@Test(dependsOnMethods = { "testModifyDashBoard" })
	public void testNavigateWidget() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testNavigateDashBoard");
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
	
	@Test(dependsOnMethods = { "testCreateDashBoard","testModifyDashBoard","testNavigateWidget"})
	//@Test
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
		
		webd.click(DashBoardPageId.InfoBtnID);
		webd.click(DashBoardPageId.RmBtnID);
		
		//click delete button
		DashBoardUtils.clickDeleteButton();
		DashBoardUtils.waitForMilliSeconds(500);
		
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
		DashBoardUtils.inputDashBoardInfo(dbName);
		DashBoardUtils.waitForMilliSeconds(5000);
		//verify input info's existence
		//Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.DashBoardNameBoxID),"AAA_testDashboard");
		webd.getLogger().info("Name = "+DashBoardUtils.getTextByID(DashBoardPageId.DashBoardNameBoxID));
		DashBoardUtils.waitForMilliSeconds(500);
		
		DashBoardUtils.clickOKButton();		
		
		webd.takeScreenShot();
		String widgetName = "Database Errors Trend";
		//add widget
		DashBoardUtils.addWidget(1,parentWindow,widgetName);
				
		DashBoardUtils.waitForMilliSeconds(500);
		
		webd.takeScreenShot();
			
	}
	
	@Test(dependsOnMethods = { "testCreateSpecialDashBoard" })
	//@Test
	public void testRemoveSpecialDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashBoard");	
		//focus a dashboard
		DashBoardUtils.waitForMilliSeconds(500);
		webd.takeScreenShot();
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("DBA_Name_Modify");
		DashBoardUtils.waitForMilliSeconds(500);
		webd.takeScreenShot();
		
		webd.click(DashBoardPageId.InfoBtnID);
		webd.click(DashBoardPageId.RmBtnID);
		
		//click delete button
		DashBoardUtils.clickDeleteButton();
		DashBoardUtils.waitForMilliSeconds(500);
		
		webd.takeScreenShot();
					
	}
	
	@Test(dependsOnMethods = { "testHomepage" })
	public void testUserMenu() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");
		
		//check OOB delete protection
		DashBoardUtils.searchDashBoard("Application Performance");
		DashBoardUtils.waitForMilliSeconds(2000);
		
		webd.click(DashBoardPageId.InfoBtnID);
		DashBoardUtils.waitForMilliSeconds(2000);
		WebElement removeButton = webd.getWebDriver().findElement(By.xpath(DashBoardPageId.RmBtnID));
		Assert.assertFalse(removeButton.isEnabled());
		
		webd.click(DashBoardPageId.MenuBtnID);
		//about menu
		webd.click(DashBoardPageId.AboutID);
		DashBoardUtils.waitForMilliSeconds(5000);
		Assert.assertEquals(webd.getWebDriver().findElement(By.xpath(DashBoardPageId.AboutContentID)).getText(),"Warning: Unauthorized access is strictly prohibited.");
		webd.click(DashBoardPageId.AboutCloseID);
		
		//help menu
		//webd.click(DashBoardPageId.MenuBtnID);
		//webd.click(DashBoardPageId.HelpID);
		//DashBoardUtils.waitForMilliSeconds(5000);
		//Assert.assertEquals(webd.getWebDriver().findElement(By.xpath(DashBoardPageId.HelpContentID)).getText(),"Get Started");
		
		//signout menu
		//webd.click(DashBoardPageId.MenuBtnID);
		//webd.click(DashBoardPageId.SignOutID);
	}
	
	@Test
	public void testHomeLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomeLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//Home link
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcpdfui/welcome.html");
		
	}
	
	@Test
	public void testITALink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testITALink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//IT Analytics link,check checkbox
		webd.click(DashBoardPageId.ITALinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcpdfui/home.html?filter=ita");
				
	}
	
	@Test
	public void testLALink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLALink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//Log Analytics link
		webd.click(DashBoardPageId.LALinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emlacore/html/log-analytics-search.html");
		
	}
	
	@Test
	public void testAPMLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAPMLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//APM link
		webd.click(DashBoardPageId.APMLinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"apmUi/index.html");
		
	}
	
	@Test
	public void testLogLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLogLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//Log link
		webd.click(DashBoardPageId.LOGLinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emlacore/html/log-analytics-search.html");
		
	}
	
	@Test
	public void testAWRLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAWRLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//AWR Analytics link		
		webd.click(DashBoardPageId.AWRALinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/flex-analyzer/html/displaying/new-chart-config.html");
		
	}
	
	@Test
	public void testFlexLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testFlexLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//Flex link
		webd.click(DashBoardPageId.FlexLinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/db-awrviewer-war/html/db-awr-analytics.html");
		
	}

	@Test
	public void testTargetLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testTargetLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//Target link
		webd.click(DashBoardPageId.TargetLinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcta/ta/analytics.html?q=eyJzIjoiW10iLCJpxIPEhn0%3D&u=eyJzIjoie30iLCJpxINbXX0%3D");
		
	}
	
	@Test
	public void testSoftwareLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testSoftwareLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//Customer Software link
		webd.click(DashBoardPageId.CustomLinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"tenantmgmt/services/customersoftware");
		
	}
	
	@Test
	public void testAdminLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAdminLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(5000);
		//IT Analytics Administration link
		webd.click(DashBoardPageId.ITA_Admin_LinkID);
		DashBoardUtils.waitForMilliSeconds(2000);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/warehouseadmin/html/admin-sources.html");
		
	}
	
	@Test
	public void testEMPCDF_812_1() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_812");
		
		//check ita box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(5000);
		
		//check la box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(5000);
		
		//signout menu
		webd.click(DashBoardPageId.MenuBtnID);
		webd.click(DashBoardPageId.SignOutID);
		
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_812");
		
		//check ita box
		Assert.assertTrue(webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).isSelected());
		DashBoardUtils.waitForMilliSeconds(5000);
		
		//check la box
		Assert.assertTrue(webd.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).isSelected());
		DashBoardUtils.waitForMilliSeconds(5000);
		
		//check ita box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(5000);
		
		//check la box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(5000);
		
	}
	
	//https://slc05mwm.us.oracle.com:4443/emsaasui/emcpdfui/error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG
	@Test
	public void testEMPCDF_832_1() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_832");
		
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		
		webd.takeScreenShot();
		webd.getWebDriver().navigate().to(url.substring(0,url.indexOf("emsaasui"))+"emsaasui/emcpdfui/error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG");
		DashBoardUtils.waitForMilliSeconds(5000);
		webd.click("//*[@id='errorMain']/div[2]/button");
		webd.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(5000);
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_832");
		webd.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(50000);
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.WelcomeID),"Welcome to Oracle Management Cloud");
		//Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Application_Performance_Monitoring_ID));
		webd.takeScreenShot();
		webd.getLogger().info("start to test in testEMPCDF_8322222");
	}
	/*
	@Test
	public void testCreateLVDashBoard() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_832");
		
		DashBoardUtils.clickLVButton();
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
		
		DashBoardUtils.openDBCreatePage();
		String dbName="LV_DashBoard";
		DashBoardUtils.inputDashBoardInfo(dbName);
		DashBoardUtils.waitForMilliSeconds(5000);
		//verify input info's existence
		//Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.DashBoardNameBoxID),"AAA_testDashboard");
		webd.getLogger().info("Name = "+DashBoardUtils.getTextByID(DashBoardPageId.DashBoardNameBoxID));
		DashBoardUtils.waitForMilliSeconds(500);
		
		DashBoardUtils.clickOKButton();		
		
		webd.takeScreenShot();
		String widgetName = "Database Errors Trend";
		//add widget
		DashBoardUtils.addWidget(1,parentWindow,widgetName);
				
		DashBoardUtils.waitForMilliSeconds(500);
		
		webd.takeScreenShot();
	}

	@Test
	public void testModifyLVDashBoard() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_832");
		
		DashBoardUtils.clickLVButton();
		
		String parentWindow = webd.getWebDriver().getWindowHandle();
		//open dashboard	
		DashBoardUtils.waitForMilliSeconds(500);
		//DashBoardUtils.clickToSortByLastAccessed();
		DashBoardUtils.searchDashBoard("LV_DashBoard");
		DashBoardUtils.waitForMilliSeconds(500);
		webd.takeScreenShot();
		DashBoardUtils.clickLVDashBoard();
		//String parentWindow = webd.getWebDriver().getWindowHandle();
		DashBoardUtils.waitForMilliSeconds(500);
		//add a new widget
		DashBoardUtils.addWidget(0,parentWindow);
		
		DashBoardUtils.waitForMilliSeconds(500);
		
		webd.takeScreenShot();
	}

	@Test
	public void testRemoveLVDashBoard() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveLVDashBoard");
		
		DashBoardUtils.clickLVButton();
		
		DashBoardUtils.searchDashBoard("LV_DashBoard");
		DashBoardUtils.waitForMilliSeconds(500);
		webd.takeScreenShot();
		
		webd.click(DashBoardPageId.DashBoardInfoID);
		webd.click(DashBoardPageId.DashBoardDeleteID);
		
		//click delete button
		DashBoardUtils.clickLVDeleteButton();
		DashBoardUtils.waitForMilliSeconds(500);
		
		webd.takeScreenShot();
	}
	*/

}
