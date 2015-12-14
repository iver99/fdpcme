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
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
		
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
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
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
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
	/*
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
	*/
/*
	@Test(dependsOnMethods = { "testHomepage" })
	public void testUserMenu() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");
		
		//check OOB delete protection
		DashBoardUtils.searchDashBoard("Application Performance");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		webd.click(DashBoardPageId.InfoBtnID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		WebElement removeButton = webd.getWebDriver().findElement(By.xpath(DashBoardPageId.RmBtnID));
		Assert.assertFalse(removeButton.isEnabled());
		
		webd.click(DashBoardPageId.MenuBtnID);
		//about menu
		webd.click(DashBoardPageId.AboutID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		Assert.assertEquals(webd.getWebDriver().findElement(By.xpath(DashBoardPageId.AboutContentID)).getText(),"Warning: Unauthorized access is strictly prohibited.");
		webd.click(DashBoardPageId.AboutCloseID);
		
		//help menu
		//webd.click(DashBoardPageId.MenuBtnID);
		//webd.click(DashBoardPageId.HelpID);
		//DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Assert.assertEquals(webd.getWebDriver().findElement(By.xpath(DashBoardPageId.HelpContentID)).getText(),"Get Started");
		
		//signout menu
		//webd.click(DashBoardPageId.MenuBtnID);
		//webd.click(DashBoardPageId.SignOutID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}*/
	
	@Test
	public void testHomeLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomeLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Home link
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcpdfui/welcome.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	
	@Test
	public void testDashBoardHomeLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testDashBoardHomeLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//dashboardHome link
		webd.click(DashBoardPageId.DashBoardHomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcpdfui/home.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	
	@Test
	public void testITALink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testITALink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//IT Analytics link,check checkbox
		webd.click(DashBoardPageId.ITALinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcpdfui/home.html?filter=ita");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);		
	}
	
	@Test
	public void testLALink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLALink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Log Analytics link
		webd.click(DashBoardPageId.LALinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emlacore/html/log-analytics-search.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	
	@Test
	public void testAPMLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAPMLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//APM link
		webd.click(DashBoardPageId.APMLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"apmUi/index.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	
	@Test
	public void testLogLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLogLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Log link
		webd.click(DashBoardPageId.LOGLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emlacore/html/log-analytics-search.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	
	/*@Test
	public void testAWRLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAWRLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//AWR Analytics link		
		webd.click(DashBoardPageId.AWRALinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/flex-analyzer/html/displaying/new-chart-config.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}

	
	@Test
	public void testFlexLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testFlexLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Flex link
		webd.click(DashBoardPageId.FlexLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/db-awrviewer-war/html/db-awr-analytics.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
*/
	@Test
	public void testTargetLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testTargetLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Target link
		webd.click(DashBoardPageId.TargetLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		String sub_str = url.substring(url.indexOf("emsaasui")+9);
		Assert.assertEquals(sub_str.substring(0,23),"emcta/ta/analytics.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	/*
	@Test
	public void testSoftwareLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testSoftwareLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Customer Software link
		webd.click(DashBoardPageId.CustomLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"tenantmgmt/services/customersoftware");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	
	@Test
	public void testAdminLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAdminLink");
		
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//IT Analytics Administration link
		webd.click(DashBoardPageId.ITA_Admin_LinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/warehouseadmin/html/admin-sources.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	*/
	@Test
	public void testEMPCDF_812_1() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_812");
		
		//check ita box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//check la box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//signout menu
		webd.click(DashBoardPageId.MenuBtnID);
		webd.click(DashBoardPageId.SignOutID);
		
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_812");
		
		//check ita box
		Assert.assertTrue(webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).isSelected());
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//check la box
		Assert.assertTrue(webd.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).isSelected());
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//check ita box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//check la box
		webd.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
	}
	
	//https://slc05mwm.us.oracle.com:4443/emsaasui/emcpdfui/error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG
	@Test
	public void testEMPCDF_832_1() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testEMPCDF_832");
		
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("current url = "+url);
		
		webd.takeScreenShot();
		webd.getWebDriver().navigate().to(url.substring(0,url.indexOf("emsaasui"))+"emsaasui/emcpdfui/error.html?msg=DBS_ERROR_PAGE_NOT_FOUND_MSG");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
                webd.takeScreenShot();
		webd.click("//*[@id='errorMain']/div[2]/button");
                webd.getLogger().info("ok button is clicked");
		webd.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
        //login(this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName()+"-relogin","sso.welcome");
        //DashBoardUtils.loadWebDriverOnly(webd);
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
        //webd.getLogger().info("welcome page is being loaded, going to to verify...");
		webd.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		DashBoardUtils.clickGVButton();
		//Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.WelcomeID),"Welcome to Oracle Management Cloud");
		webd.getLogger().info("welcome page is verified successfully");
                Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Application_Performance_Monitoring_ID));
		webd.takeScreenShot();
		webd.getLogger().info("complete testing in testEMPCDF_832");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
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
	
	@Test
	public void testWelcomepage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testWelcomepage");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Home link
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_APMLinkID),"Application Performance Monitoring");
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_LALinkID),"Log Analytics");
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_ITALinkID),"IT Analytics");
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_DataExp),"Data Explorers");
		
	}
	

}
