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
		String dbName="testDashboard";
		String dbDesc="testDashBoard desc";
		
		driver.sendKeys(DashBoardPageId.DashBoardNameBoxID, dbName);
		driver.sendKeys(DashBoardPageId.DashBoardDescBoxID, dbDesc);
	}
	
	public static void clickOKButton() throws Exception
	{
		driver.click(DashBoardPageId.DashOKButtonID);
	}
	
	public static void clickAddButton(String parentWindow) throws Exception
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
		
		String parentWindow = parentHandle;
		Set<String> handles =  driver.getWebDriver().getWindowHandles();
		for(String windowHandle  : handles)
		{
		    if(!windowHandle.equals(parentWindow))
		    {
		        driver.getWebDriver().switchTo().window(windowHandle);
		         
		        driver.getLogger().info("start to test in addWidget");
				waitForMilliSeconds(5000);
				driver.waitForElementPresent(DashBoardPageId.WidgetAddButtonID);
				driver.click(DashBoardPageId.WidgetAddButtonID);
				widgetName = WidgetPageId.widgetName;
				
				widgetAddPage = new WidgetAddPage(driver);
				
				//search widget
				widgetAddPage.searchWidget(widgetName);
				waitForMilliSeconds(5000);
				//select widget
				widgetAddPage.clickWidgetOnTable(widgetName);
				
				waitForMilliSeconds(5000);
				//click to add widget
				//if(i == 1)  
						//clickAddButton(windowHandle);
				//if(i == 0)  clickAddButton_final();
		 		
				//save dashboard
		 		clickSaveButton();
		 		
		        driver.getWebDriver().close(); //closing child window
		        driver.getWebDriver().switchTo().window(parentWindow); //cntrl to parent window
		     }
		}
		
		
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
	public static void clickDashBoard() throws Exception
	{
		driver.click(DashBoardPageId.DashBoardID);
	}
	 
}
