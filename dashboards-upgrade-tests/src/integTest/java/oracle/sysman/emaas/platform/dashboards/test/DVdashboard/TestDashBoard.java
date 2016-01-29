package oracle.sysman.emaas.platform.dashboards.test.DVdashboard;

import org.testng.annotations.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

//import oracle.sysman.emaas.platform.dashboards.test.util.*;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.WidgetAddPage;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.WidgetPageId;


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

      
                //sharing dashbaord after cretion of dashbaord
		       
		       @Test(dependsOnMethods = { "testCreateDashBoard" })
		       		public void testshareddashboard() throws Exception
		       		{
		       			this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		       			webd.getLogger().info("start to test in testshareddashboard");
		       			
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
		       			DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		       			
		       			DashBoardUtils.navigateWidget(parentWindow);
		       			DashBoardUtils.waitForMilliSeconds(2*DashBoardPageId.Delaytime_long);
                                        //sharing dashbaord
		       			DashBoardUtils.sharedashboard();
		       			DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
							
					DashBoardUtils.navigateWidget(parentWindow);
		                       DashBoardUtils.waitForMilliSeconds(2*DashBoardPageId.Delaytime_long);
		       	}

                     //Stopping sharing dashbaord
	     
	          @Test(dependsOnMethods = { "testshareddashboard" })
	                public void teststopsharing() throws Exception
			     		{
			       			this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
			       			webd.getLogger().info("start to test in teststopsharing");
					       			
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
                                                   //stopp sharing dashbaord
					       			DashBoardUtils.sharestopping();
			       			DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
										
							DashBoardUtils.navigateWidget(parentWindow);
			                       DashBoardUtils.waitForMilliSeconds(2*DashBoardPageId.Delaytime_long);
					       	}
	 
	
}

