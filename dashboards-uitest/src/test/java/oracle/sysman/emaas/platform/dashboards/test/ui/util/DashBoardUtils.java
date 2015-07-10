package oracle.sysman.emaas.platform.dashboards.test.ui.util;

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

public class DashBoardUtils {

	private static WebDriver driver;
	
	public static void loadWebDriver(WebDriver webDriver) throws Exception
	{
		driver=webDriver;
		if(doesWebElementExist(DashBoardPageId.OverviewCloseID))  closeOverviewPage();
		waitForMilliSeconds(5000);

	}
	
	public static void closeOverviewPage() throws Exception
	{
		driver.click(DashBoardPageId.OverviewCloseID);
	}
	
	public static void openDBCreatePage() throws Exception
	{
	//TODO: Add page check

		if(driver==null)
			throw new Exception("The WebDriver variable has not been initialized,please initialize it first");
		driver.click(DashBoardPageId.CreateDSButtonID);
	}
	
	public static void inputDashBoardInfo() throws Exception
	{
		String dbName="AAA_testDashboard";
		String dbDesc="AAA_testDashBoard desc";
		
		driver.sendKeys(DashBoardPageId.DashBoardNameBoxID, dbName);
		driver.sendKeys(DashBoardPageId.DashBoardDescBoxID, dbDesc);
	}
	
	public static void clickOKButton() throws Exception
	{
		driver.click(DashBoardPageId.DashOKButtonID);
	}
	
	public static void clickNavigatorLink() throws Exception
	{
		driver.click(DashBoardPageId.LinkID);
	}
	
	public static void clickAddButton() throws Exception
	{		
		driver.click(DashBoardPageId.AddBtn);	
	}
	
	public static void clickAddButton_final() throws Exception
	{
		driver.click(DashBoardPageId.AddBtn);
	}
	
	public static void clickDeleteButton() throws Exception
	{
		//add verify if we are into deleting dialog
		//Assert.assertEquals("//div/p[text()=Do you want to delete the selected dashboard 'test'?", "Do you want to delete the selected dashboard 'test'?","we are not in delete dialog");
		driver.click(DashBoardPageId.DeleteBtnID_Dialog);
	}
	
	public static void clickCloseButton() throws Exception
	{
		driver.click(DashBoardPageId.closeBtnID);
	}
	
	public static void clickSaveButton() throws Exception
	{
		boolean exist = doesWebElementExist(DashBoardPageId.DashBoardSaveID);
		if(!exist) return;
		driver.click(DashBoardPageId.DashBoardSaveID);
	}
	
	public static void clickDashBoardName() throws Exception
	{
		driver.click(DashBoardPageId.DashBoardName);
	}
	
	public static  void addWidget(int i,String parentHandle) throws Exception
	{
		WidgetAddPage widgetAddPage;
		String widgetName;
				
		waitForMilliSeconds(500);
		driver.waitForElementPresent(DashBoardPageId.WidgetAddButtonID);
		
		//verify title and desc of dashboard
		/*if( getText(DashBoardPageId.DashboardNameID) == null)
		{
			Assert.assertEquals(getText(DashBoardPageId.MDashboardNameID),"AAA_testDashboard");
			Assert.assertEquals(getText(DashBoardPageId.MDashboardDescID),"AAA_testDashBoard desc");
		}
		else*/{
			Assert.assertEquals(getText(DashBoardPageId.DashboardNameID),"AAA_testDashboard");
			Assert.assertEquals(getText(DashBoardPageId.DashboardDescID),"AAA_testDashBoard desc");
		}
				
		
		driver.click(DashBoardPageId.WidgetAddButtonID);
				
		widgetName = WidgetPageId.widgetName;
		
		widgetAddPage = new WidgetAddPage(driver);

		//search widget
		widgetAddPage.searchWidget(widgetName);
		waitForMilliSeconds(2000);			
		
				
		//select widget
		widgetAddPage.clickWidgetOnTable(widgetName);
		waitForMilliSeconds(2000);
		clickAddButton();
		waitForMilliSeconds(2000);
		clickCloseButton();
		waitForMilliSeconds(2000);		
		
		//save dashboard
		clickSaveButton();
 		 		
	}
	
	public static void navigateWidget(String parentHandle) throws Exception
	{
			WidgetAddPage widgetAddPage;
			String widgetName;			       
			         
			driver.getLogger().info("start to test in navigateWidget");
			waitForMilliSeconds(500);
			
			//verify title and desc of dashboard
			/*if( getText(DashBoardPageId.DashboardNameID) == null)
			{
				Assert.assertEquals(getText(DashBoardPageId.MDashboardNameID),"AAA_testDashboard");
				Assert.assertEquals(getText(DashBoardPageId.MDashboardDescID),"AAA_testDashBoard desc");
			}
			else*/{
				Assert.assertEquals(getText(DashBoardPageId.DashboardNameID),"AAA_testDashboard");
				Assert.assertEquals(getText(DashBoardPageId.DashboardDescID),"AAA_testDashBoard desc");
			}
	
			driver.waitForElementPresent(DashBoardPageId.WidgetAddButtonID);
			driver.click(DashBoardPageId.WidgetAddButtonID);					
			
			driver.getLogger().info("before select");
			WebElement Box = driver.getWebDriver().findElement(By.xpath(WidgetPageId.dropListID));//*[@id='oj-listbox-drop']"));//));
			Box.click();

			DashBoardUtils.waitForMilliSeconds(500);
			
			driver.takeScreenShot();
			WebElement DivisionList = driver.getWebDriver().findElement(By.xpath(WidgetPageId.LAListID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
			DivisionList.click();
			DashBoardUtils.waitForMilliSeconds(500);
			//get text and grab number,then determine how many pages we should navigate 
			WebElement leftButton = driver.getWebDriver().findElement(By.xpath(WidgetPageId.leftNavigatorBtnID));
			WebElement rightButton = driver.getWebDriver().findElement(By.xpath(WidgetPageId.rightNavigatorBtnID));
			driver.getLogger().info("after select");
								
			while(rightButton.isEnabled()){
				rightButton.click();
			}
			driver.getLogger().info("after right btn");
			Assert.assertFalse(rightButton.isEnabled());
			Assert.assertTrue(leftButton.isEnabled());
			driver.getLogger().info("after enabled 1");
			//navigate to left
			while(leftButton.isEnabled()){
				leftButton.click();
			}
			driver.getLogger().info("after left btn");		
			Assert.assertFalse(leftButton.isEnabled());
			Assert.assertTrue(rightButton.isEnabled());
			driver.getLogger().info("after enabled 2");
			clickCloseButton();
			
	}
		
		
	
	
		
	public static void saveWidget() throws Exception
	{
		clickSaveButton();
	}
	
	public static void waitForMilliSeconds(long millisSec) throws Exception
	{
		Thread.sleep(millisSec);
	}
	
	public static void clickDBOnTable(String dbID) throws Exception
	{
		driver.click("//div[@aria-dashboard="+dbID+"]");
	}
	
	public static boolean doesWebElementExist(String selector)
	{
		                             
		        WebElement el=driver.getWebDriver().findElement(By.id(selector));
			
		       if(el.isDisplayed()){
		    	   driver.getLogger().info("can get element");
		    	   return true;              
		       }		                                    
		       else{
		 
		    	   driver.getLogger().info("can not get element");
		    	   return false;              
		       }         

	}
	
	public static boolean doesWebElementExistByXPath(String xpath)
	{
		                             
		        WebElement el=driver.getWebDriver().findElement(By.xpath(xpath));
			
		       if(el.isDisplayed()){
		    	   driver.getLogger().info("can get element");
		    	   return true;              
		       }		                                    
		       else{
		 
		    	   driver.getLogger().info("can not get element");
		    	   return false;              
		       }         

	}
	
	public static void clickDashBoard() throws Exception
	{
		driver.click(DashBoardPageId.DashBoardID);
	}
	 
	public static void clickToSortByLastAccessed() throws Exception
	{		
		WebElement Box = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.SortDropListID));//*[@id='oj-listbox-drop']"));//));
		Box.click();

		DashBoardUtils.waitForMilliSeconds(500);
		
		driver.takeScreenShot();
		WebElement DivisionList = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.Access_Date_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList.click();
		DashBoardUtils.waitForMilliSeconds(500);
		
		driver.takeScreenShot();
	}
	public static void searchDashBoard(String board) throws Exception
	{
		driver.sendKeys(DashBoardPageId.SearchDSBoxID, board);
		driver.click("/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[1]/button[2]");
		
	}
	public static void checkBrandingBarLink() throws Exception
	{
		clickNavigatorLink();
		waitForMilliSeconds(500);
		//Home link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.HomeLinkID)).getText(),"Home");
		//IT Analytics link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.ITALinkID)).getText(),"IT Analytics");
		//Log Analytics link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.LALinkID)).getText(),"Log Analytics");
		//APM link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.APMLinkID)).getText(),"APM");
		//Log link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.LOGLinkID)).getText(),"Log");
		//AWR Analytics link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.AWRALinkID)).getText(),"AWR Analytics");
		//Flex link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.FlexLinkID)).getText(),"Flex");
		//Target link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.TargetLinkID)).getText(),"Target");
		//Customer Software link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.CustomLinkID)).getText(),"Customer Software");
		//IT Analytics Administration link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.ITA_Admin_LinkID)).getText(),"IT Analytics Administration");
	}
	public static void clickCheckBox() throws Exception
	{
		//check APM cloud service 
		driver.getWebDriver().findElement(By.id(DashBoardPageId.APM_BoxID)).click();
		Assert.assertTrue(doesWebElementExistByXPath(DashBoardPageId.Application_Performance_Monitoring_ID));
		Assert.assertFalse(doesWebElementExistByXPath(DashBoardPageId.Database_Performance_Analytics_ID));
		driver.getWebDriver().findElement(By.id(DashBoardPageId.APM_BoxID)).click();
		//check ita box
		driver.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).click();
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Performance_Analytics_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Resource_Planning_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Garbage_Collection_Overhead_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Host_Inventory_By_Platform_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Database_Configuration_and_Storage_By_Version_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.WebLogic_Servers_by_JDK_Version_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_Databases_by_Resource_Consumption_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_WebLogic_Servers_by_Heap_Usage_ID));
		Assert.assertTrue(DashBoardUtils.doesWebElementExistByXPath(DashBoardPageId.Top_25_WebLogic_Servers_by_Load_ID));
		Assert.assertFalse(doesWebElementExistByXPath(DashBoardPageId.Application_Performance_Monitoring_ID));
		driver.getWebDriver().findElement(By.id(DashBoardPageId.ITA_Check_BoxID)).click();
		//check la box
		driver.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		driver.getWebDriver().findElement(By.id(DashBoardPageId.LA_BoxID)).click();
		//check oracle created
		driver.getWebDriver().findElement(By.id(DashBoardPageId.Oracle_BoxID)).click();
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
		driver.getWebDriver().findElement(By.id(DashBoardPageId.Oracle_BoxID)).click();
		//check me created
		driver.getWebDriver().findElement(By.id(DashBoardPageId.Other_BoxID)).click();
		driver.getWebDriver().findElement(By.id(DashBoardPageId.Other_BoxID)).click();
		
		
	}
	public static String getText(String id)
	{
		WebElement we = driver.getWebDriver().findElement(By.xpath(id));
		return we.getText();
	}
	public static String getTextByID(String id)
	{
		WebElement we = driver.getWebDriver().findElement(By.id(id));
		return we.getText();
	}
}
