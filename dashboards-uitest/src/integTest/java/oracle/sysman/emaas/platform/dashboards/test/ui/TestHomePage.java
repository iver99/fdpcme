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

public class TestHomePage extends LoginAndLogout{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName()+"."+testName);
		DashBoardUtils.loadWebDriver(webd);
		
		//reset all the checkboxes
		DashBoardUtils.resetCheckBox(DashBoardPageId.APM_BoxID);
		DashBoardUtils.resetCheckBox(DashBoardPageId.ITA_BoxID);
		DashBoardUtils.resetCheckBox(DashBoardPageId.LA_BoxID);
		DashBoardUtils.resetCheckBox(DashBoardPageId.Oracle_BoxID);
		DashBoardUtils.resetCheckBox(DashBoardPageId.Other_BoxID);
		DashBoardUtils.resetCheckBox(DashBoardPageId.Share_BoxID);		
	}
	
	
	@Test
	public void verify_allOOB_GridView() throws Exception
	{
		//login the dashboard
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");	
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
				
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");	
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//verify all the oob display
		DashBoardUtils.APM_OOB_GridView();
		DashBoardUtils.ITA_OOB_GridView();
		DashBoardUtils.LA_OOB_GridView();
		
		//sort func
		DashBoardUtils.clickToSortByLastAccessed();
		//check box
		//DashBoardUtils.clickCheckBox();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	
	@Test
	public void verify_APMOOB_GridView() throws Exception
	{
		//login the dashboard
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");	
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
		
		//select Cloud Services as APM
		webd.getLogger().info("select Cloud Services as APM");
		DashBoardUtils.clickCheckBox(DashBoardPageId.APM_BoxID);
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");	
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
				
		//verify APM oob display
		DashBoardUtils.APM_OOB_GridView();
		
		//reset cloud services checkbox
		DashBoardUtils.resetCheckBox(DashBoardPageId.APM_BoxID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);		
	}
	
	@Test
	public void verify_ITAOOB_GridView() throws Exception
	{
		//login the dashboard
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");	
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
				
		//select Cloud Services as IT Analytics
		webd.getLogger().info("select Cloud Services as IT Analytics");
		DashBoardUtils.clickCheckBox(DashBoardPageId.ITA_BoxID);
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");	
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
						
		//verify APM oob display
		DashBoardUtils.ITA_OOB_GridView();
		
		//reset cloud services checkbox
		DashBoardUtils.resetCheckBox(DashBoardPageId.ITA_BoxID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
	}
	
	@Test
	public void verify_LAOOB_GridView() throws Exception
	{
		//login the dashboard
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");	
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
				
		//select Cloud Services as Log Analytics
		webd.getLogger().info("select Cloud Services as Log Analytics");
		DashBoardUtils.clickCheckBox(DashBoardPageId.LA_BoxID);
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");	
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
						
		//verify APM oob display
		DashBoardUtils.LA_OOB_GridView();
		
		//reset cloud services checkbox
		DashBoardUtils.resetCheckBox(DashBoardPageId.LA_BoxID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
	}
	
	@Test
	public void verify_CreatedBy_Oracle_GridView() throws Exception
	{
		//login the dashboard
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");	
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
				
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");	
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Oracle");
		DashBoardUtils.clickCheckBox(DashBoardPageId.Oracle_BoxID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//verify all the oob display
		DashBoardUtils.APM_OOB_GridView();
		DashBoardUtils.ITA_OOB_GridView();
		DashBoardUtils.LA_OOB_GridView();
		
		//sort func
		DashBoardUtils.clickToSortByLastAccessed();
		//reset Created By checkbox
		DashBoardUtils.resetCheckBox(DashBoardPageId.Oracle_BoxID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
	}
	
	@Test
	public void verify_CreatedBy_Me_GridView() throws Exception
	{
		//login the dashboard
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomePage");	
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
				
		//click Grid View icon
		webd.getLogger().info("click Grid View icon");	
		DashBoardUtils.clickGVButton();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//click Created By Oracle checkbox
		webd.getLogger().info("select Created By as Me");
		DashBoardUtils.clickCheckBox(DashBoardPageId.Other_BoxID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		
		//verify all the oob not exsit
		DashBoardUtils.noOOBCheck_GridView();
		
		//sort func
		DashBoardUtils.clickToSortByLastAccessed();
		//reset Created By checkbox
		DashBoardUtils.resetCheckBox(DashBoardPageId.Other_BoxID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);	
	}
	
	@Test
	public void testUserMenu() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testUserMenu");
		
//		DashBoardUtils.clickGVButton();
//		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
//		
//		//check OOB delete protection
//		DashBoardUtils.searchDashBoard("Application Performance");
//		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
//		
//		webd.click(DashBoardPageId.InfoBtnID);
//		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
//		WebElement removeButton = webd.getWebDriver().findElement(By.xpath(DashBoardPageId.RmBtnID));
//		Assert.assertFalse(removeButton.isEnabled());
		
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
	}
	
	@Test
	public void testExploreData_LALink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLALink");
		
		DashBoardUtils.clickExploreDataButton();
		DashBoardUtils.clickExploreDataMenuItem(DashBoardPageId.ExploreDataMenu_Log_ID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Log Analytics link
		//webd.click(DashBoardPageId.LALinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emlacore/html/log-analytics-search.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	
	@Test
	public void testExploreData_SearchLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testTargetLink");
		
		DashBoardUtils.clickExploreDataButton();
		DashBoardUtils.clickExploreDataMenuItem(DashBoardPageId.ExploreDataMenu_Search_ID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Target link
		//webd.click(DashBoardPageId.TargetLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		String sub_str = url.substring(url.indexOf("emsaasui")+9);
		Assert.assertEquals(sub_str.substring(0,23),"emcta/ta/analytics.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
	
	@Test
	public void testExploreData_AnalyzeLink() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAWRLink");
		
		DashBoardUtils.clickExploreDataButton();
		DashBoardUtils.clickExploreDataMenuItem(DashBoardPageId.ExploreDataMenu_Analyze_ID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//AWR Analytics link		
		//webd.click(DashBoardPageId.AWRALinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui")+9),"emcitas/flex-analyzer/html/displaying/new-chart-config.html");
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
}
