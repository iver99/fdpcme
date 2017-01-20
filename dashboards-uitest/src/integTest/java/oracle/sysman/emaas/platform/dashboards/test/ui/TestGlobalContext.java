/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @author cawei
 */
public class TestGlobalContext extends LoginAndLogout
{

	public static final String GLBCTXTID = "emaas-appheader-globalcxt";
	public static final String GLBCTXTFILTERPILL = "globalBar_pillWrapper";
	public static final String GLBCTXTBUTTON = "buttonShowTopology";
	public static final String DSBNAME = "DASHBOARD_GLOBALTESTING";
	public static final String DSBSETNAME = "DASHBOARDSET_GLOBALTESTING";

	private String dbName_tailsTest = "";
	
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

	}

	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to remove the test data...");

		DashBoardUtils.deleteDashboard(webd, DSBNAME);
		DashBoardUtils.deleteDashboard(webd, DSBSETNAME);
		DashBoardUtils.deleteDashboard(webd, dbName_tailsTest);
		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test
	public void testGlobalContextCreateDashboard()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboard");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, DSBNAME, null);
		DashboardBuilderUtil.verifyDashboard(webd, DSBNAME, null, false);
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in builder Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test
	public void testGlobalContextCreateDashboardSet()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboardSet");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboardSet(webd, DSBSETNAME, null);
		DashboardBuilderUtil.verifyDashboardSet(webd, DSBSETNAME);
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in builder Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test
	public void testGlobalContextDashboardHome()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextDashboardHome");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exist in DashboardHome");

	}

	//@Test commented out because of welcome page api version control's known issue
	public void testGlobalContextITA()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextITA");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "performanceAnayticsDatabase");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in ITA Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "performanceAnalyticsMiddleware");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in ITA Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "resourceAnalyticsDatabase");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in ITA Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "resourceAnalyticsMiddleware");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in ITA Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "resourceAnalyticsHost");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in ITA Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "dataExplorerAnalyze");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in ITA Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "dataExplorer");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in ITA Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test
	public void testGlobalContextLA()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextLA");

		BrandingBarUtil.visitApplicationCloudService(webd, "Log Analytics");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in LA Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	//@Test
	public void testGlobalContextMonitoring()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextMonitoring");

		//visit welcome page
		webd.getLogger().info("Visit Welcome Page");
		BrandingBarUtil.visitWelcome(webd);
		BrandingBarUtil.visitApplicationCloudService(webd, "Infrastructure Monitoring");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exists in Monitoring Page");
		//	Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test
	public void testGlobalContextOOBAPMDashboard()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextOOBAPMDashboard");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.selectOOB(webd, "Application Performance Monitoring");
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exist in APM");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test
	public void testGlobalContextOOBDashboard()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextOOBDashboard");

		//visit home page

		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.selectOOB(webd, "Host Operations");
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exist in OOB dashboard");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test
	public void testGlobalContextOOBDashboardSet()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextOOBDashboardSet");

		//visit home page

		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");
		//Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in OOBDashboard Set");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test
	public void testGlobalContextUDE()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testGlobalContextUDE");

		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, "Search");

		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in UDE Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test
	public void testGlobalContextWelcomePage()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testWelcomePage");

		//visit welcome page
		webd.getLogger().info("Visit Welcome Page");
		BrandingBarUtil.visitWelcome(webd);
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in Welcome Page");

	}
	
	@Test
	public void tesTGlobalContext_SwitchEntity()
	{
		dbName_tailsTest = "selfDb-" + generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_tailsTest, null);
		DashboardBuilderUtil.verifyDashboard(webd, dbName_tailsTest, null, false);
		
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Analytics Line");
		DashboardBuilderUtil.saveDashboard(webd);	
		
		//find edit button and click it
		WebElement editButton = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDEDITBUTTON));		
		Assert.assertTrue(editButton.isDisplayed(), "Edit button isn't displayed in self dashboard");
		webd.click(PageId.DASHBOARDEDITBUTTON);
		
		//find the dashboard name and click it
		WebElement dashboardName = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDNAME));		
		Assert.assertTrue(dashboardName.isDisplayed(), "dashboardName isn't displayed in self dashboard");
		webd.click("css="+ PageId.DASHBOARDNAME_CSS);
		
		//find Dashboard Filters and click it
		WebElement dashboardFilters = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDFILTERS));
		Assert.assertTrue(dashboardFilters.isDisplayed(), "Dashboard Filters isn't displayed in self dashboard");
		webd.click(PageId.DASHBOARDFILTERS);
		
		//make sure Entities label is displayed
		WebElement entitiesLabel = webd.getWebDriver().findElement(By.xpath(PageId.DASHBOARDENTITIES));
		Assert.assertTrue(entitiesLabel.isDisplayed(), "Entities Label isn't displayed in self dashboard");

		//find "Use dashboard entities" radio button, then select it
		WebElement useDbEntities = webd.getWebDriver().findElement(By.xpath(PageId.ENABLEENTITYFILTER));
		Assert.assertTrue(useDbEntities.isDisplayed(), "Use dashboard entities isn't displayed in self dashboard");
		webd.click(PageId.ENABLEENTITYFILTER);		
		
		Assert.assertFalse(webd.isDisplayed(GLBCTXTID),"emaas-appheader-globalcxt is displayed on the top");
		Assert.assertTrue(webd.isDisplayed(PageId.ENTITYBUTTON),"All Entities button isn't display on the top-left cornor, when select dashboard entities");
		
		//find "GC entities" radio button, then select it
		WebElement useGCEntities = webd.getWebDriver().findElement(By.xpath(PageId.ENABLEGCENTITYFILTER));
		Assert.assertTrue(useGCEntities.isDisplayed(), "GC entities filter isn't displayed in self dashboard");
		webd.click(PageId.ENABLEGCENTITYFILTER);
		
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context isn't exists when select GC entities filter");
		Assert.assertTrue(webd.isDisplayed(PageId.ENTITYBUTTON),"All Entities button isn't display on the top-left cornor, when select GC entities filter");
	
		//find "" radio button, then select it
		WebElement disableEntity = webd.getWebDriver().findElement(By.xpath(PageId.DISABLEENTITYFILTER));
		Assert.assertTrue(disableEntity.isDisplayed(), "Disable entities filter isn't displayed in self dashboard");
		webd.click(PageId.DISABLEENTITYFILTER);
		
		Assert.assertFalse(webd.isDisplayed(GLBCTXTID),"emaas-appheader-globalcxt is displayed on the top");
		Assert.assertFalse(webd.isElementPresent(PageId.ENTITYBUTTON), "All Entities button is present on the top-left cornor, when select disable entities fileter");
	}
	
	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
