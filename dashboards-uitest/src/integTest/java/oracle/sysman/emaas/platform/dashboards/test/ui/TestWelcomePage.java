package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version
 * @author charles.c.chen
 * @since release specific (what release of product did this appear in)
 */

public class TestWelcomePage extends LoginAndLogout
{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test
	public void testOpenAPMPage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening APM in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitAPM(webd);
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open APM by url: " + tmpUrl);
		urlVerification(tmpUrl, "apmUi/index.html");
		webd.getLogger().info("Test open APM in welcome page finished!!!");
	}

	@Test
	public void testOpenDashboardPage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Dashboards in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitDashboards(webd);
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open Dashboards by url: " + tmpUrl);
		urlVerification(tmpUrl, "emcpdfui/home.html");
		webd.getLogger().info("Test open dashboards in welcome page finished!!!");
	}

	@Test
	public void testOpenDE_LAPage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Data Explorers-Log in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.dataExplorers(webd, "log");
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open Data Explorers-Log by url: " + tmpUrl);
		urlVerification(tmpUrl, "emlacore/html/log-analytics-search.html");
		webd.getLogger().info("Test opening Data Explorers-Log in welcome page finished!!!");
	}

	@Test
	public void testOpenDE_SearchPage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening Data Explorers-Search in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.dataExplorers(webd, "search");
		String url = "emcta/ta/analytics.html";
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open Data Explorers-Search by url: " + tmpUrl);
		Assert.assertTrue(tmpUrl.contains(url));
		webd.getLogger().info("Test opening Data Explorers-Search in welcome page finished!!!");
	}

	//	@Test
	//	public void testOpenGetStartedPage() throws Exception
	//	{
	//		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
	//		webd.getLogger().info("Start to test opening 'How to get started' in welcome page...");
	//
	//		BrandingBarUtil.visitWelcome(webd);
	//		WelcomeUtil.learnMoreHow(webd);
	//		webd.switchToWindow();
	//		String tmpUrl = webd.getWebDriver().getCurrentUrl();
	//		webd.getLogger().info("Open 'How to get started' by url: " + tmpUrl);
	//		Assert.assertEquals(tmpUrl, "http://docs.oracle.com/cloud/latest/em_home/index.html");
	//		webd.getLogger().info("Test opening 'How to get started' in welcome page finished!!!");
	//	}


//	@Test
//	public void testOpenInfraMonitoring() throws Exception
//	{
//		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
//		webd.getLogger().info("Start to test open Infrastructure Monitoring in welcome page...");
//		
//		BrandingBarUtil.visitWelcome(webd);
//		WelcomeUtil.visitInfraMonitoring(webd);
//		String tmpUrl = webd.getWebDriver().getCurrentUrl();
//		webd.getLogger().info("Open Infrastructure Monitoring by url: " + tmpUrl);
//		urlVerification(tmpUrl, "monitoringservicesui/cms/index.html");
//		webd.getLogger().info("Test open Infrastructure Monitoring in welcome page finished!!!");
//	}
	
	@Test
	public void testOpenITA_DEPage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Data Explorer in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "dataExplorer");
		String url = "emcta/ta/analytics.html";
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open ITA: Data Explorer by url: " + tmpUrl);
		Assert.assertTrue(tmpUrl.contains(url));
		webd.getLogger().info("Test opening ITA: Data Explorer in welcome page finished!!!");
	}

	@Test
	public void testOpenITA_PADatabasePage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Performance Analytics-Database in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "performanceAnalyticsDatabase");
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open ITA: Performance Analytics-Database by url: " + tmpUrl);
		urlVerification(tmpUrl, "emcitas/db-analytics-war/html/db-performance-analytics.html");
		webd.getLogger().info("Test opening ITA: Performance Analytics-Database in welcome page finished!!!");
	}

	//@Test
	public void testOpenITA_PAMiddlewarePage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Performance Analytics-Middleware in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "performanceAnalyticsMiddleware");
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open ITA: Performance Analytics-Middleware by url: " + tmpUrl);
		urlVerification(tmpUrl, "emcitas/mw-analytics-war/html/mw-perf-analytics.html");
		webd.getLogger().info("Test opening ITA: Performance Analytics-Middleware in welcome page finished!!!");
	}

	@Test
	public void testOpenITA_RADatabasePage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Resource Analytics-Database in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "resourceAnalyticsDatabase");
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open ITA: Resource Analytics-Database by url: " + tmpUrl);
		urlVerification(tmpUrl, "emcitas/db-analytics-war/html/db-analytics-resource-planner.html");
		webd.getLogger().info("Test opening ITA: Resource Analytics-Database in welcome page finished!!!");
	}
	
	@Test
	public void testOpenITA_RAHostPage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Resource Analytics-Host in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "resourceAnalyticsHost");
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open ITA: Resource Analytics-Host by url: " + tmpUrl);
		urlVerification(tmpUrl, "emcitas/resource-analytics/html/server-resource-analytics.html");
		webd.getLogger().info("Test opening ITA: Resource Analytics-Host in welcome page finished!!!");
	}

	@Test
	public void testOpenITA_RAMiddlewarePage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA: Resource Analytics-Middleware in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "resourceAnalyticsMiddleware");
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open ITA: Resource Analytics-Middleware by url: " + tmpUrl);
		urlVerification(tmpUrl, "emcitas/mw-analytics-war/html/mw-analytics-resource-planner.html");
		webd.getLogger().info("Test opening ITA: Resource Analytics-Middleware in welcome page finished!!!");
	}

	@Test
	public void testOpenITAPage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening ITA in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "default");
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open ITA by url: " + tmpUrl);
		urlVerification(tmpUrl, "emcpdfui/home.html?filter=ita");

		WebElement el = webd.getWebDriver().findElement(By.id(PageId.ITA_BOXID));
		Assert.assertTrue(el.isSelected());
		DashBoardUtils.ITA_OOB_GridView();
		webd.getLogger().info("Test open ITA in welcome page finished!!!");
	}

	@Test
	public void testOpenLAPage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test opening LA in welcome page...");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitLA(webd);
		String tmpUrl = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("Open LA by url: " + tmpUrl);
		urlVerification(tmpUrl, "emlacore/html/log-analytics-search.html");
		webd.getLogger().info("Test open LA in welcome page finished!!!");
	}

	//	@Test
	//	public void testOpenServiceOfferingPage() throws Exception
	//	{
	//		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
	//		webd.getLogger().info("Start to test opening 'Service Offerings' in welcome page...");
	//
	//		BrandingBarUtil.visitWelcome(webd);
	//		WelcomeUtil.learnMoreServiceOffering(webd);
	//		webd.switchToWindow();
	//		String tmpUrl = webd.getWebDriver().getCurrentUrl();
	//		webd.getLogger().info("Open 'Service Offerings' by url: " + tmpUrl);
	//		Assert.assertEquals(tmpUrl, "https://cloud.oracle.com/management");
	//		webd.getLogger().info("Test opening 'Service Offerings' in welcome page finished!!!");
	//	}

	//	@Test
	//	public void testOpenVideosPage() throws Exception
	//	{
	//		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
	//		webd.getLogger().info("Start to test opening 'Videos' in welcome page...");
	//
	//		BrandingBarUtil.visitWelcome(webd);
	//		WelcomeUtil.learnMoreVideo(webd);
	//		webd.switchToWindow();
	//		String tmpUrl = webd.getWebDriver().getCurrentUrl();
	//		webd.getLogger().info("Open 'Videos' by url: " + tmpUrl);
	//		Assert.assertEquals(tmpUrl, "http://docs.oracle.com/cloud/latest/em_home/em_home_videos.htm");
	//		webd.getLogger().info("Test opening 'Videos' in welcome page finished!!!");
	//	}

	@Test
	public void testWelcomepage() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in test Welcome Page");
		BrandingBarUtil.visitWelcome(webd);
		webd.takeScreenShot();

		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "APM"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "LA"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "ITA"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "dashboards"));
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, "dataExplorers"));

		Assert.assertTrue(WelcomeUtil.isLearnMoreItemExisted(webd, "getStarted"));
		Assert.assertTrue(WelcomeUtil.isLearnMoreItemExisted(webd, "videos"));
		Assert.assertTrue(WelcomeUtil.isLearnMoreItemExisted(webd, "serviceOfferings"));
	}

	public void urlVerification(String pageUrl, String expectedUrl)
	{
		Assert.assertEquals(pageUrl.substring(pageUrl.indexOf("emsaasui") + 9), expectedUrl);
	}
}
