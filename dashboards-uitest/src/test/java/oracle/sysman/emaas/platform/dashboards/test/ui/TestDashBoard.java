package oracle.sysman.emaas.platform.dashboards.test.ui;

import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.*;



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
	public void testCreateDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testCreateDashBoard");
		
		DashBoardUtils.openDBCreatePage();
		DashBoardUtils.inputDashBoardInfo();
		
		DashBoardUtils.waitForMilliSeconds(5000);
		
		DashBoardUtils.clickOKButton();		
		String parentWindow = webd.getWebDriver().getWindowHandle();
		DashBoardUtils.waitForMilliSeconds(5000);
		
		//add widget
		DashBoardUtils.addWidget(1,parentWindow);
		
		DashBoardUtils.waitForMilliSeconds(50000);
		
		webd.takeScreenShot();
			
	}
	
	
	@Test
	public void testModifyDashBoard() throws Exception
	{
					
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testModifyDashBoard");
		String parentWindow = webd.getWebDriver().getWindowHandle();
		//open dashboard	
		DashBoardUtils.clickDashBoard();
		
		DashBoardUtils.waitForMilliSeconds(50000);
		//add a new widget
		DashBoardUtils.addWidget(0,parentWindow);
		
		DashBoardUtils.waitForMilliSeconds(50000);
		
		webd.takeScreenShot();
	}
	
	
	public void testRemoveDashBoard() throws Exception
	{
				
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testRemoveDashBoard");	
		//focus a dashboard
		DashBoardUtils.waitForMilliSeconds(50000);
		webd.takeScreenShot();
		WebElement mainelement = webd.getElement(DashBoardPageId.ElementID);
		WebElement deletebutton = webd.getElement(DashBoardPageId.DeleteBtnID);
        Actions builder = new Actions(webd.getWebDriver());
        builder.moveToElement(mainelement).moveToElement(deletebutton).click().perform();
        
		//click delete button
		DashBoardUtils.clickDeleteButton();
		DashBoardUtils.waitForMilliSeconds(50000);
		
		webd.takeScreenShot();
					
	}
	
}
