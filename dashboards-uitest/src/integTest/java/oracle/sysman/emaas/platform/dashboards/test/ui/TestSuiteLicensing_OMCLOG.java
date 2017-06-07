package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestSuiteLicensing_OMCLOG extends LoginAndLogout
{
	private final String tenant_OMC_Log = DashBoardUtils.getTenantName("df_omclog");
	private final String UDEWidget = "Analytics Line";
	private String dbName_Log = "";

	private final String tenant_username = "emcsadmin";
	
	public static final String GLBCTXTID = "emaas-appheader-globalcxt";
	public static final String GLBCTXTFILTERPILL = "globalBar_pillWrapper";
	public static final String GLBCTXTBUTTON = "buttonShowTopology";
	public static final String DSBNAME = "DASHBOARD_GLOBALTESTING";
	public static final String DSBSETNAME = "DASHBOARDSET_GLOBALTESTING";

	private String dbName_la = "";
	private String dbName_ude = "";
	private String dbName_willDelete = "";
	private String dbSetName_willDelete = "";

	private String dbName_tailsTest = "";

	public void initTest(String testName, String customUser, String tenantname)
	{
		customlogin(this.getClass().getName() + "." + testName, customUser, tenantname);
		DashBoardUtils.loadWebDriver(webd);
		
		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OMCLOG()
	{
		//verify Data Explorer link in branding bar
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in Branding Bar--with OMC Log");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.OMCLOG);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OMCLOG()
	{
		dbName_Log = "Dashboard OMC Log-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OMC Log";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in Builder Page -- with OMC Log");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Log, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMCLOG()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in Home Page -- with OMC Log");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.OMCLOG);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMCLOG_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test OOB in Home Page -- with OMC Log Edition");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		webd.getLogger().info("Verify the OOB dashboards display in home page");
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OMCLOG);

		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OMCLOG);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OMCLOG()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in test Welcome Page -- OMC Log Edition");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.OMCLOG);
	}
	
	@Test(alwaysRun = true)
	public void verifyDatabaseOperations_GridView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in verifyDatabaseOperations_GridView");

		//lick on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Database Operations
		verifyDatabaseOperations();
		verifyDatabaseOperations_Details();
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseOperations_ListView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in verifyDatabaseOperations_ListView");

		//lick on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Database Operations
		verifyDatabaseOperations();
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseOperations_WithFilter_GridView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in verifyDatabaseOperations_WithFilter_GridView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//lick on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Database Operations
		verifyDatabaseOperations();
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseOperations_WithFilter_ListView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in verifyDatabaseOperations_WithFilter_ListView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//lick on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Database Operations
		verifyDatabaseOperations();
	}
	@Test(alwaysRun = true)
	public void verifyHostOperations_GridView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in verifyHostOperations_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Host Operations
		verifyHostOperations();
		verifyHostOperations_Details();
	}

	@Test(alwaysRun = true)
	public void verifyHostOperations_ListView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in verifyHostOperations_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Host Operations
		verifyHostOperations();
	}

	@Test(alwaysRun = true)
	public void verifyHostOperations_WithFilter_GridView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in verifyHostOperations_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Host Operations
		verifyHostOperations();
	}

	@Test(alwaysRun = true)
	public void verifyHostOperations_WithFilter_ListView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in verifyHostOperations_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Host Operations
		verifyHostOperations();
	}

	@Test(alwaysRun = true)
	public void verifyMiddlewareOperations_GridView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Middleware Operations
		verifyMiddlewareOperations();
		verifyMiddlewareOperations_Details();
	}

	@Test(alwaysRun = true)
	public void verifyMiddlewareOperations_ListView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Middleware Operations
		verifyMiddlewareOperations();
	}

	@Test(alwaysRun = true)
	public void verifyMiddlewareOperations_WithFilter_GridView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Middleware Operations
		verifyMiddlewareOperations();
	}

	@Test(alwaysRun = true)
	public void verifyMiddlewareOperations_WithFilter_ListView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Middleware Operations
		verifyMiddlewareOperations();
	}
	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_GridView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Enterprise Health
		verifyEnterpriseHealth();
		verifyEnterpriseHealth_Details();
	}

	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_ListView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");
		
		//verify Enterprise Health
		verifyEnterpriseHealth();
	}	

	@Test(alwaysRun = true)
	public void verifyExadataHealth_GridView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyExadataHealth_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		verifyExadataHealth();
		verifyExadataHealth_Details();
	}

	@Test(alwaysRun = true)
	public void verifyExadataHealth_ListView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyExadataHealth_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		verifyExadataHealth();
	}
		
	@Test(alwaysRun = true)
	public void verifyUIGallery_GridView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyUIGallery_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		verifyUIGallery();
		verifyUIGallery_Details();
	}

	@Test(alwaysRun = true)
	public void verifyUIGallery_ListView_OMCLOG()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test in verifyUIGallery_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		verifyUIGallery();
	}
	
	private void verifyEnterpriseHealth()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=31");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Enterprise Health opened correctly");

		webd.getLogger().info("Verify the dashboard set titile...");
		DashboardBuilderUtil.verifyDashboardSet(webd, "Enterprise Health");
	}

	private void verifyEnterpriseHealth_Details()
	{
		webd.getLogger().info("Verify the icon in dashboard set -- <Enterprise Health>");
		verifyIconInOobDashboardSet();

		//verify the dashboards in set
		webd.getLogger().info("Verify the dashboards in set");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Summary");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Hosts");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Databases");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Application Servers");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Entities");

		//verify each dashboard
		webd.getLogger().info("Verify Dashboard <Summary> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Summary");
		verifySummary();

		webd.getLogger().info("Verify Dashboard <Hosts> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Hosts");
		verifyHosts();

		webd.getLogger().info("Verify Dashboard <Databases> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Databases");
		verifyDatabases();

		webd.getLogger().info("Verify Dashboard <Application Servers> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Application Servers");
		verifyApplicationServers();

		webd.getLogger().info("Verify Dashboard <Entities> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Entities");
		verifyEntities();

		webd.getLogger().info("Verification end...");
	}
	private void verifyApplicationServers()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Application Servers opened correctly");

		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Status");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server: Fatal Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server: Critical Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server: Warning Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Application Servers: Top 5 by Alert Count");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Performance Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Average Memory Usage");
		DashboardBuilderUtil.verifyWidget(webd, "CPU Utilization by Application Server Type");
		DashboardBuilderUtil.verifyWidget(webd, "Application Servers Grouped by Web Request Rate");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Log Records");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Application Server Status");
		verifyIconInWidget(webd, "Application Server: Fatal Alerts");
		verifyIconInWidget(webd, "Application Server: Critical Alerts");
		verifyIconInWidget(webd, "Application Server: Warning Alerts");
		verifyIconInWidget(webd, "Application Servers: Top 5 by Alert Count");
		verifyIconInWidget(webd, "Application Server Performance Metrics");
		verifyIconInWidget(webd, "Average Memory Usage");
		verifyIconInWidget(webd, "CPU Utilization by Application Server Type");
		verifyIconInWidget(webd, "Application Servers Grouped by Web Request Rate");
		verifyIconInWidget(webd, "Application Server Log Records");

		webd.getLogger().info("Verification end...");

	}
	private void verifyDatabases()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Databases opened correctly");

		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Database Status");
		DashboardBuilderUtil.verifyWidget(webd, "Databases: Fatal Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Databases: Critical Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Databases: Warning Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Databases: Top 5 by Alert Count");
		DashboardBuilderUtil.verifyWidget(webd, "Database Performance Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Database Log Trends");
		DashboardBuilderUtil.verifyWidget(webd, "Space Used by Database Type");
		DashboardBuilderUtil.verifyWidget(webd, "Databases Grouped by Transactions");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Database Status");
		verifyIconInWidget(webd, "Databases: Fatal Alerts");
		verifyIconInWidget(webd, "Databases: Critical Alerts");
		verifyIconInWidget(webd, "Databases: Warning Alerts");
		verifyIconInWidget(webd, "Databases: Top 5 by Alert Count");
		verifyIconInWidget(webd, "Database Performance Metrics");
		verifyIconInWidget(webd, "Database Log Trends");
		verifyIconInWidget(webd, "Space Used by Database Type");
		verifyIconInWidget(webd, "Databases Grouped by Transactions");

		webd.getLogger().info("Verification end...");
	}
	private void verifyHosts()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Hosts opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Host Status");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Fatal Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Critical Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Warning Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Top 5 by Alert Count");
		DashboardBuilderUtil.verifyWidget(webd, "Host Performance Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts Grouped by Memory Utilization");
		DashboardBuilderUtil.verifyWidget(webd, "CPU Utilization by Host Type");
		DashboardBuilderUtil.verifyWidget(webd, "Avg Disk I/O Request Rate");
		DashboardBuilderUtil.verifyWidget(webd, "Host Log Trend");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Host Status");
		verifyIconInWidget(webd, "Hosts: Fatal Alerts");
		verifyIconInWidget(webd, "Hosts: Critical Alerts");
		verifyIconInWidget(webd, "Hosts: Warning Alerts");
		verifyIconInWidget(webd, "Hosts: Top 5 by Alert Count");
		verifyIconInWidget(webd, "Host Performance Metrics");
		verifyIconInWidget(webd, "Hosts Grouped by Memory Utilization");
		verifyIconInWidget(webd, "CPU Utilization by Host Type");
		verifyIconInWidget(webd, "Avg Disk I/O Request Rate");
		verifyIconInWidget(webd, "Host Log Trend");

		webd.getLogger().info("Verification end...");
	}
	private void verifySummary()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Summary opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Entity Status");
		DashboardBuilderUtil.verifyWidget(webd, "Entity Count");
		DashboardBuilderUtil.verifyWidget(webd, "Entities: Fatal Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Entities: Critical Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Entities: Warning Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Host Status by Type");
		DashboardBuilderUtil.verifyWidget(webd, "Host CPU Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Database Status by Type");
		DashboardBuilderUtil.verifyWidget(webd, "Database I/O Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Status by Type");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Load Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Load Balancer Status by Type");
		DashboardBuilderUtil.verifyWidget(webd, "Load Balancer Performance Metrics");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Entity Status");
		verifyIconInWidget(webd, "Entity Count");
		verifyIconInWidget(webd, "Entities: Fatal Alerts");
		verifyIconInWidget(webd, "Entities: Critical Alerts");
		verifyIconInWidget(webd, "Entities: Warning Alerts");
		verifyIconInWidget(webd, "Host Status by Type");
		verifyIconInWidget(webd, "Host CPU Metrics");
		verifyIconInWidget(webd, "Database Status by Type");
		verifyIconInWidget(webd, "Database I/O Metrics");
		verifyIconInWidget(webd, "Application Server Status by Type");
		verifyIconInWidget(webd, "Application Server Load Metrics");
		verifyIconInWidget(webd, "Load Balancer Status by Type");
		verifyIconInWidget(webd, "Load Balancer Performance Metrics");

		webd.getLogger().info("Verification end...");
	}
	private void verifyEntities()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Entities opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Entities");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Entities");

		webd.getLogger().info("Verification end...");
	}

	private void verifyExadataHealth()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=28");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Exadata Health opened correctly");

		webd.getLogger().info("Verify the dashboard set titile...");
		DashboardBuilderUtil.verifyDashboardSet(webd, "Exadata Health");
	}

	private void verifyExadataHealth_Details()
	{
		webd.getLogger().info("Verify the icon in dashboard set -- <Exadata Health>");
		verifyIconInOobDashboardSet();

		//verify the dashboards in set
		webd.getLogger().info("Verify the dashboards in set");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Overview");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Performance");

		//verify each dashboard
		webd.getLogger().info("Verify Dashboard <Overview> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Overview");
		verifyOverview();

		webd.getLogger().info("Verify Dashboard <Performance> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Performance");
		verifyPerformance();

		webd.getLogger().info("Verification end...");
	}
	private void verifyOverview()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Overview opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Entities by Database Machine");
		DashboardBuilderUtil.verifyWidget(webd, "Exadata Inventory");
		DashboardBuilderUtil.verifyWidget(webd, "Entity Types in Database Machines");
		DashboardBuilderUtil.verifyWidget(webd, "Exadata Capacity by Disk Type");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Entities by Database Machine");
		verifyIconInWidget(webd, "Exadata Inventory");
		verifyIconInWidget(webd, "Entity Types in Database Machines");
		verifyIconInWidget(webd, "Exadata Capacity by Disk Type");

		webd.getLogger().info("Verification end...");
	}
	private void verifyPerformance()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Performance opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Read Response Time");
		DashboardBuilderUtil.verifyWidget(webd, "Top 5 Databases by Active Sessions");
		DashboardBuilderUtil.verifyWidget(webd, "Host CPU Utilization and Memory Utilization");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Infiniband Network Performance");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Write Response Time");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Read/Write Response Times");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server I/O Utilization by DB Machine");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server Read Response Time");
		verifyIconInWidget(webd, "Top 5 Databases by Active Sessions");
		verifyIconInWidget(webd, "Host CPU Utilization and Memory Utilization");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server Infiniband Network Performance");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server Write Response Time");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server Read/Write Response Times");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server I/O Utilization by DB Machine");

		webd.getLogger().info("Verification end...");
	}
	private void verifyUIGallery()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=24");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - UI Gallery opened correctly");

		webd.getLogger().info("Verify the dashboard set titile...");
		DashboardBuilderUtil.verifyDashboardSet(webd, "UI Gallery");
	}

	private void verifyUIGallery_Details()
	{
		webd.getLogger().info("Verify the icon in dashboard set --<UIGallery>");
		verifyIconInOobDashboardSet();

		//verify the dashboards in set
		webd.getLogger().info("Verify the dashboards in set");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Timeseries - Line Basic");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Timeseries - Line Advanced");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Timeseries - Area");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Categorical - Basic");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Categorical - Advanced");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Trend and Forecasting");
		//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Others");

		//verify each dashboard
		webd.getLogger().info("Verify Dashboard <Timeseries - Line Basic> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Timeseries - Line Basic");
		verifyTimeseriesLineBasic();

		webd.getLogger().info("Verify Dashboard <Timeseries - Line Advanced> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Timeseries - Line Advanced");
		verifyTimeseriesLineAdvanced();

		webd.getLogger().info("Verify Dashboard <Timeseries - Area> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Timeseries - Area");
		verifyTimeseriesArea();

		webd.getLogger().info("Verify Dashboard <Categorical - Basic> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Categorical - Basic");
		verifyCategoricalBasic();

		webd.getLogger().info("Verify Dashboard <Categorical - Advanced> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Categorical - Advanced");
		verifyCategoricalAdvanced();

		webd.getLogger().info("Verify Dashboard <Trend and Forecasting> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Trend and Forecasting");
		verifyTrendAndForecasting();
		//		webd.getLogger().info("Verify Dashboard <Others> in set");
		//		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Others");
		//		verifyOthers();

		webd.getLogger().info("Verification end...");
	}
	private void verifyCategoricalBasic()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Categorical - Basic opened correctly");

		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Donut");
		DashboardBuilderUtil.verifyWidget(webd, "Treemap");
		DashboardBuilderUtil.verifyWidget(webd, "Histogram");
		DashboardBuilderUtil.verifyWidget(webd, "Analytics Line - Categorical");
		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Bar Chart");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Donut");
		verifyIconInWidget(webd, "Treemap");
		verifyIconInWidget(webd, "Histogram");
		verifyIconInWidget(webd, "Analytics Line - Categorical");
		verifyIconInWidget(webd, "Bar Chart");
		verifyIconInWidget(webd, "Stacked Bar Chart");

		webd.getLogger().info("Verification end...");
	}

	private void verifyCategoricalAdvanced()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Categorical - Advanced opened correctly");

		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart with Top N");
		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart with Color and Group by option");
		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart with Color option");
		DashboardBuilderUtil.verifyWidget(webd, "Pareto Chart");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Bar Chart with Top N");
		verifyIconInWidget(webd, "Bar Chart with Color and Group by option");
		verifyIconInWidget(webd, "Bar Chart with Color option");
		verifyIconInWidget(webd, "Pareto Chart");

		webd.getLogger().info("Verification end...");
	}

	private void verifyTrendAndForecasting()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard -Trend and Forecasting opened correctly");

		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Analytics Line with Trend and Forecasting");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Trend and Forecasting");
		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Analytics Line with Trend and Forecasting");
		verifyIconInWidget(webd, "Line Chart with Trend and Forecasting");

		webd.getLogger().info("Verification end...");
	}	
	private void verifyIconInOobDashboardSet()
	{
		//verify the edit menu & save icon are not displayed in OOB
		webd.getLogger().info("Verify the save icon is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSAVECSS), "Save icon is displayed in OOB");

		WebDriverWait wait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(PageId.DASHBOARDSETOPTIONS_CSS)));
		webd.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		webd.takeScreenShot();

		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + PageId.DASHBOARDSETOPTIONSEDIT_CSS), "Edit menu is displayed in OOB");
	}
	private void verifyTimeseriesLineBasic()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Timeseries - Line Basic opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart");
		DashboardBuilderUtil.verifyWidget(webd, "Analytics Line");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Shared Y-axis");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Reference Line");


		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Line Chart");
		verifyIconInWidget(webd, "Analytics Line");
		verifyIconInWidget(webd, "Line Chart with Shared Y-axis");
		verifyIconInWidget(webd, "Line Chart with Reference Line");

		webd.getLogger().info("the verification end...");
	}

	private void verifyTimeseriesLineAdvanced()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Timeseries - Line Advanced opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Color");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Line Chart with Group By");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Line Chart with Color and Group by");


		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Line Chart with Color");
		verifyIconInWidget(webd, "Stacked Line Chart with Group By");
		verifyIconInWidget(webd, "Stacked Line Chart with Color and Group by");

		webd.getLogger().info("the verification end...");
	}

	private void verifyTimeseriesArea()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Timeseries - Area opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Area Chart");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Area Chart");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Area Chart with Group By");


		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Area Chart");
		verifyIconInWidget(webd, "Stacked Area Chart");
		verifyIconInWidget(webd, "Stacked Area Chart with Group By");

		webd.getLogger().info("the verification end...");
	}
	private void verifyDatabaseOperations()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=15");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Database Operations opened correctly");

		webd.getLogger().info("Verify the dashboard titile...");
		DashboardBuilderUtil.verifyDashboard(webd, "Database Operations", "", true);

		webd.getLogger().info("Verify the OOB Dashboard - Database Operations opened finished");
	}

	private void verifyDatabaseOperations_Details()
	{
		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard -- <Database Operations>");
		DashboardBuilderUtil.verifyWidget(webd, "Database Log Trends");
		DashboardBuilderUtil.verifyWidget(webd, "Database Critical Incidents by Target Type");
		DashboardBuilderUtil.verifyWidget(webd, "Top Database Targets with Log Errors");
		DashboardBuilderUtil.verifyWidget(webd, "Database Top Errors");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Database Log Trends");
		verifyIconInWidget(webd, "Database Critical Incidents by Target Type");
		verifyIconInWidget(webd, "Top Database Targets with Log Errors");
		verifyIconInWidget(webd, "Database Top Errors");

		webd.getLogger().info("Verification end...");
	}
	private void verifyHostOperations()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=16");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Host Operations opened correctly");

		webd.getLogger().info("Verify the dashboard titile...");
		DashboardBuilderUtil.verifyDashboard(webd, "Host Operations", "", true);

		webd.getLogger().info("Verify the OOB Dashboard - Host Operations opened finished");
	}

	private void verifyHostOperations_Details()
	{
		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard -- <Host Operations>");
		DashboardBuilderUtil.verifyWidget(webd, "Host Logs Trend");
		DashboardBuilderUtil.verifyWidget(webd, "Top Host Log Sources");
		DashboardBuilderUtil.verifyWidget(webd, "Top Host Log Entries by Service");
		DashboardBuilderUtil.verifyWidget(webd, "Top SUDO Users");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Host Logs Trend");
		verifyIconInWidget(webd, "Top Host Log Sources");
		verifyIconInWidget(webd, "Top Host Log Entries by Service");
		verifyIconInWidget(webd, "Top SUDO Users");

		webd.getLogger().info("Verification end...");
	}
	private void verifyMiddlewareOperations()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=17");

		//verify the Middleware Operations open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Middleware Operations opened correctly");

		webd.getLogger().info("Verify the dashboard titile...");
		DashboardBuilderUtil.verifyDashboard(webd, "Entities", "", true);

		webd.getLogger().info("Verify the OOB Dashboard - Middleware Operations opened finished");
	}

	private void verifyMiddlewareOperations_Details()
	{
		webd.getLogger().info("Verify all the widgets in dashboard  -- <Middleware Operations>");
		DashboardBuilderUtil.verifyWidget(webd, "Middleware Logs Trend");
		DashboardBuilderUtil.verifyWidget(webd, "Top Middleware Error Codes");
		DashboardBuilderUtil.verifyWidget(webd, "Top Middleware Targets with Errors");
		DashboardBuilderUtil.verifyWidget(webd, "Web Server Top Accessed Pages");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Middleware Logs Trend");
		verifyIconInWidget(webd, "Top Middleware Error Codes");
		verifyIconInWidget(webd, "Top Middleware Targets with Errors");
		verifyIconInWidget(webd, "Web Server Top Accessed Pages");

		webd.getLogger().info("Verification end...");
	}
	private void verifyIconInWidget(WebDriver driver, String widgetname)
	{
		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		driver.click(DashBoardPageId_190.BUILDERTILESEDITAREA);
		driver.takeScreenShot();

		String titleTitlesLocator = String.format(DashBoardPageId.BUILDERTILETITLELOCATOR, widgetname);
		WebElement tileTitle = driver.getWebDriver().findElement(By.xpath(titleTitlesLocator));

		tileTitle.click();
		driver.takeScreenShot();

		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		driver.takeScreenShot();

		//verify the config icon not exist
		Assert.assertFalse(driver.isDisplayed(DashBoardPageId.BUILDERTILECONFIGLOCATOR),
				"widiget configuration icon is displayed");
	}
	private void verifyIconInOobDashboard()
	{
		webd.getLogger().info("Verify the save icon is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSAVECSS), "Save icon is displayed in OOB Dashboard");

		WebDriverWait wait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(PageId.DASHBOARDOPTIONS_CSS)));
		webd.click("css=" + PageId.DASHBOARDOPTIONS_CSS);
		webd.takeScreenShot();

		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + DashBoardPageId.BUILDEROPTIONSEDITLOCATORCSS),
				"Edit menu is displayed in OOB Dashboard");
	}

	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to remove the test data...");

		DashBoardUtils.deleteDashboard(webd, DSBNAME);
		DashBoardUtils.deleteDashboard(webd, DSBSETNAME);
		DashBoardUtils.deleteDashboard(webd, dbName_la);
		DashBoardUtils.deleteDashboard(webd, dbName_ude);
		DashBoardUtils.deleteDashboard(webd, dbName_willDelete);
		DashBoardUtils.deleteDashboard(webd, dbName_tailsTest);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test(alwaysRun = true)
	public void testGlobalContextCreateDashboard()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboard");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, DSBNAME, null);
		DashboardBuilderUtil.verifyDashboard(webd, DSBNAME, null, false);
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in builder Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextCreateDashboardSet()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboardSet");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboardSet(webd, DSBSETNAME, null);
		DashboardBuilderUtil.verifyDashboardSet(webd, DSBSETNAME);
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in builder Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextDashboardHome()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextDashboardHome");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exist in DashboardHome");

	}

	@Test(alwaysRun = true)
	public void testGlobalContextLA()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextLA");

		BrandingBarUtil.visitApplicationCloudService(webd, "Log Analytics");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in LA Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextOOBDashboard()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextOOBDashboard");

		//visit home page

		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.selectOOB(webd, "Host Operations");
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exist in OOB dashboard");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}
	
	@Test(alwaysRun = true)
	public void testGlobalContextOOBDashboardSet()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextOOBDashboardSet");

		//visit home page

		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in OOBDashboard Set");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextUDE()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextUDE");

		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, "Search");

		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in UDE Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextWelcomePage()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testWelcomePage");

		//visit welcome page
		webd.getLogger().info("Visit Welcome Page");
		BrandingBarUtil.visitWelcome(webd);
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in Welcome Page");

	}

	@Test(groups = "test_omcCtx")
	public void testomcCtx_OpenLAWidget()
	{
		dbName_la = "selfDb-" + DashBoardUtils.generateTimeStamp();
	
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testomcCtx_OpenLAWidget");
	
		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_la, null);
		DashboardBuilderUtil.verifyDashboard(webd, dbName_la, null, false);
		
		DashboardBuilderUtil.addWidgetToDashboard(webd, "All Logs Trend");
		DashboardBuilderUtil.saveDashboard(webd);	
		DashboardBuilderUtil.openWidget(webd, "All Logs Trend");
		
		webd.switchToWindow();
		webd.getLogger().info("Wait for the widget loading....");
		//wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='srchSrch']")));
		webd.waitForElementPresent(PageId.RUNBUTTON_LA, WaitUtil.WAIT_TIMEOUT);
		
		//verify the open url
		DashBoardUtils.verifyURL_WithPara(webd, "omcCtx=");	
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd),"The global context isn't exists in LA widget");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"All Entities");
	}

	@Test(groups = "test_omcCtx")
	public void testomcCtx_OpenITAWidget()
	{
		dbName_ude = "selfDb-" + DashBoardUtils.generateTimeStamp();
	
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");
	
		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_ude, null);
		DashboardBuilderUtil.verifyDashboard(webd, dbName_ude, null, false);
		
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Analytics Line");
		DashboardBuilderUtil.saveDashboard(webd);	
		DashboardBuilderUtil.openWidget(webd, "Analytics Line");
		
		webd.switchToWindow();
		webd.getLogger().info("Wait for the widget loading....");
		//wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='save_widget_btn']")));
		webd.waitForElementPresent(PageId.SAVEBUTTON_UDE, WaitUtil.WAIT_TIMEOUT);
		
		//verify the open url
		DashBoardUtils.verifyURL_WithPara(webd, "omcCtx=");	
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd),"The global context isn't exists in ITA widget");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"All Entities");
	}
	
	@Test(groups = "test_omcCtx")
	public void testomcCtx_DeleteDashboard()
	{
		dbName_willDelete = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextDeleteDashboard");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_willDelete, null);
		DashboardBuilderUtil.verifyDashboard(webd, dbName_willDelete, null, false);
		
		DashboardBuilderUtil.deleteDashboard(webd);

		//verify omcCtx exist in the url
		String url1 = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("start to verify omcCtx exist in the dashboard home url");
		Assert.assertTrue(url1.contains("omcCtx="), "The global context infomation in URL is lost");
		
		//open the dashboard, eg: Host Operations in the home page, then verify omcCtx exist in the url
		webd.getLogger().info("open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");		
		
		String url2 = webd.getWebDriver().getCurrentUrl();		
		webd.getLogger().info("start to verify omcCtx exist in the OOB dashboard url");	
		Assert.assertTrue(url2.contains("omcCtx="), "The global context infomation in URL is lost in OOB dashboard page");		
	}

	@Test(groups = "test_omcCtx", dependsOnMethods = { "testomcCtx_DeleteDashboard" })
	public void testomcCtx_DeleteDashboardSet()
	{
		dbSetName_willDelete = "selfDbSet-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextDeleteDashboard");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboardSet(webd, dbSetName_willDelete, null);
		DashboardBuilderUtil.verifyDashboardSet(webd, dbSetName_willDelete);
		
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_willDelete, null);
		
		DashboardBuilderUtil.deleteDashboardSet(webd);
	
		//verify omcCtx exist in the url
		String url1 = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("start to verify omcCtx exist in the dashboard home url");
		Assert.assertTrue(url1.contains("omcCtx="), "The global context infomation in URL is lost");
		
		//open the dashboard, eg: Host Operations in the home page, then verify omcCtx exist in the url
		webd.getLogger().info("open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");		
		
		String url2 = webd.getWebDriver().getCurrentUrl();		
		webd.getLogger().info("start to verify omcCtx exist in the OOB dashboard url");	
		Assert.assertTrue(url2.contains("omcCtx="), "The global context infomation in URL is lost in OOB dashboard page");		
	}

	@Test(alwaysRun = true)
	public void tesTGlobalContext_SwitchEntity()
	{
		dbName_tailsTest = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_tailsTest, null);
		DashboardBuilderUtil.verifyDashboard(webd, dbName_tailsTest, null, false);
		
		DashboardBuilderUtil.addWidgetToDashboard(webd, "All Logs Trend");
		DashboardBuilderUtil.saveDashboard(webd);	
		
		//find "GC entities" radio button, then select it
		DashboardBuilderUtil.respectGCForEntity(webd);
		
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context isn't exists when select GC entities filter");
		//Assert.assertTrue(webd.isDisplayed(PageId.ENTITYBUTTON),"All Entities button isn't display on the top-left cornor, when select GC entities filter");
	
		//find "Use dashboard entities" radio button, then select it
		DashboardBuilderUtil.showEntityFilter(webd, true);	
		
		webd.waitForElementPresent(PageId.ENTITYBUTTON);
		
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd),"The global context isn't exists when select dashboard entities filter");
		Assert.assertTrue(webd.isDisplayed(PageId.ENTITYBUTTON),"All Entities button isn't display on the top-left cornor, when select dashboard entities");
		
		//find "Use entities defined by content" radio button, then select it
		DashboardBuilderUtil.showEntityFilter(webd, false);
		
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd),"The global context isn't exists when select disable entities filter");
		Assert.assertFalse(webd.isDisplayed(PageId.ENTITYBUTTON), "All Entities button is present on the top-left cornor, when select disable entities fileter");
	}
}
