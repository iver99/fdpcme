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

public class TestWelcomePage extends LoginAndLogout{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName()+"."+testName);
		DashBoardUtils.loadWebDriver(webd);
	}
	
	public void pageVerification(WebDriver webDriver, String pageID, String url) throws Exception
	{
		webDriver.getLogger().info("Open page");
		webDriver.click(pageID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webDriver.takeScreenShot();
		String tmpurl = webDriver.getWebDriver().getCurrentUrl();
		webDriver.getLogger().info("url = "+tmpurl);
		Assert.assertEquals(tmpurl.substring(tmpurl.indexOf("emsaasui")+9),url);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	}
			
	@Test
	public void testWelcomepage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		//Home link
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_APMLinkID),"Application Performance Monitoring");
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_LALinkID),"Log Analytics");
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_ITALinkID),"IT Analytics");
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_DashboardsLinkID),"Dashboards");
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_DataExp),"Data Explorers");
		
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_LearnMore_getStarted),"How to get started");
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_LearnMore_Videos),"Videos");
		Assert.assertEquals(DashBoardUtils.getText(DashBoardPageId.Welcome_LearnMore_ServiceOffering),"Service Offerings");
	}
	
	@Test
	public void testOpenAPMPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//APM link		
		pageVerification(webd,DashBoardPageId.Welcome_APMLinkID,"apmUi/index.html");			
	}
	
	@Test
	public void testOpenLAPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//LA link
		pageVerification(webd,DashBoardPageId.Welcome_LALinkID,"emlacore/html/log-analytics-search.html");	
	}
		
	@Test
	public void testOpenITAPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//ITA link
		pageVerification(webd,DashBoardPageId.Welcome_ITALinkID,"emcpdfui/home.html?filter=ita");	
				
		WebElement el=webd.getWebDriver().findElement(By.id(DashBoardPageId.ITA_BoxID));
		Assert.assertTrue(el.isSelected());		
		DashBoardUtils.ITA_OOB_GridView();
	}
	
	@Test
	public void testOpenDashboardPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//Dashboard link
		pageVerification(webd,DashBoardPageId.Welcome_DashboardsLinkID,"emcpdfui/home.html");			
	}
	
	@Test
	public void testOpenGetStartedPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//get started link
		webd.getLogger().info("Open How to get started page");
		webd.click(DashBoardPageId.Welcome_LearnMore_getStarted);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url,"docs.oracle.com/cloud/latest/em_home/index.html");	
	}
	
	@Test
	public void testOpenVideosPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//videos link
		webd.getLogger().info("Open Videos page");
		webd.click(DashBoardPageId.Welcome_LearnMore_Videos);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url,"docs.oracle.com/cloud/latest/em_home/em_home_videos.htm");	
	}
	
	@Test
	public void testOpenServiceOfferingPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//service offerings link
		webd.getLogger().info("Open Videos page");
		webd.click(DashBoardPageId.Welcome_LearnMore_ServiceOffering);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+url);
		Assert.assertEquals(url,"https://cloud.oracle.com/management");	
	}
	
	@Test
	public void testOpenITA_PADatabasePage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//ITA link
		webd.click(DashBoardPageId.Welcome_ITA_SelectID);
		pageVerification(webd,DashBoardPageId.Welcome_ITA_PADatabase,"emcitas/db-analytics-war/html/db-performance-analytics.html");			
	}
	
	@Test
	public void testOpenITA_PAMiddlewarePage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//ITA link
		webd.click(DashBoardPageId.Welcome_ITA_SelectID);
		pageVerification(webd,DashBoardPageId.Welcome_ITA_PAMiddleware,"emcitas/mw-analytics-war/html/mw-perf-analytics.html");			
	}
	
	@Test
	public void testOpenITA_RADatabasePage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//ITA link
		webd.click(DashBoardPageId.Welcome_ITA_SelectID);
		pageVerification(webd,DashBoardPageId.Welcome_ITA_RADatabase,"emcitas/db-analytics-war/html/db-analytics-resource-planner.html");			
	}
	
	@Test
	public void testOpenITA_RAMiddlewarePage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//ITA link
		webd.click(DashBoardPageId.Welcome_ITA_SelectID);
		pageVerification(webd,DashBoardPageId.Welcome_ITA_RAMiddleware,"emcitas/mw-analytics-war/html/mw-analytics-resource-planner.html");			
	}
	
	@Test
	public void testOpenITA_DEAnalyzePage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//ITA link
		webd.click(DashBoardPageId.Welcome_ITA_SelectID);
		pageVerification(webd,DashBoardPageId.Welcome_ITA_DEAnalyze,"emcitas/flex-analyzer/html/displaying/new-chart-config.html");			
	}
	
	@Test
	public void testOpenITA_DEPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//ITA link
		webd.click(DashBoardPageId.Welcome_ITA_SelectID);
		pageVerification(webd,DashBoardPageId.Welcome_ITA_DE,"emcta/ta/analytics.html");			
	}
	
	@Test
	public void testOpenDE_LAPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//Data Explorer link
		webd.click(DashBoardPageId.Welcome_DataExp_SelectID);
		pageVerification(webd,DashBoardPageId.Welcome_DataExp_Log,"emlacore/html/log-analytics-search.html");			
	}
	
	@Test
	public void testOpenDE_AnalyzerPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//Data Explorer link
		webd.click(DashBoardPageId.Welcome_DataExp_SelectID);
		pageVerification(webd,DashBoardPageId.Welcome_DataExp_Analyze,"emcitas/flex-analyzer/html/displaying/new-chart-config.html");			
	}
	
	@Test
	public void testOpenDE_SearchPage() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		//Home link
		webd.getLogger().info("Open Welcome Page");
		webd.click(DashBoardPageId.HomeLinkID);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		
		//Data Explorer link
		webd.click(DashBoardPageId.Welcome_DataExp_SelectID);
		webd.getLogger().info("Open page");
		webd.click(DashBoardPageId.Welcome_DataExp_Search);
		DashBoardUtils.waitForMilliSeconds(DashBoardPageId.Delaytime_long);
		webd.takeScreenShot();
		String url = "emcta/ta/analytics.html";
		String tmpurl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = "+tmpurl);
		Assert.assertTrue(tmpurl.substring(tmpurl.indexOf("emsaasui")+9).contains(url));		
	}
}
