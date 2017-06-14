package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.TestGlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.VerifyOOBUtil;
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
import org.testng.annotations.BeforeClass;
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
	
	private String dsbName = "DASHBOARD_GLOBALTESTING";
	private String dsbsetName = "DASHBOARDSET_GLOBALTESTING";	
	private String dbName_WithGC = "TestDashboardWithGC";		
	private String dbName_ude = "";
	private String dbName_la = "";
	private String dbName_willDelete = "";
	private String dbSetName_willDelete = "";
	private String dbName_inSet = "";
	private String dbName_tailsTest = "";
	
	private String gccontent = "Composite: /SOA1213_base_domain/base_domain/soa_server1/soa-infra_System";
	
	@BeforeClass 
	public void CreateTestData()
	{
		dbName_WithGC = dbName_WithGC + DashBoardUtils.generateTimeStamp();
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start to create test data");

		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to create the test data...");
		
		DashboardHomeUtil.createDashboard(webd, dbName_WithGC, null, DashboardHomeUtil.DASHBOARD);		
		DashboardBuilderUtil.verifyDashboard(webd, dbName_WithGC, null, false);
		
		webd.getLogger().info("Respect GC");
		DashboardBuilderUtil.respectGCForEntity(webd);		
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

		DashBoardUtils.deleteDashboard(webd, dsbName);
		DashBoardUtils.deleteDashboard(webd, dsbsetName);
		DashBoardUtils.deleteDashboard(webd, dbName_la);
		DashBoardUtils.deleteDashboard(webd, dbName_ude);
		DashBoardUtils.deleteDashboard(webd, dbName_tailsTest);
		DashBoardUtils.deleteDashboard(webd, dbName_WithGC);
		DashBoardUtils.deleteDashboard(webd, dbName_inSet);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

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
		VerifyOOBUtil.verifyDatabaseOperations(webd);
		VerifyOOBUtil.verifyDatabaseOperations_Details(webd);
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
		VerifyOOBUtil.verifyDatabaseOperations(webd);
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
		VerifyOOBUtil.verifyDatabaseOperations(webd);
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
		VerifyOOBUtil.verifyDatabaseOperations(webd);
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
		VerifyOOBUtil.verifyHostOperations(webd);
		VerifyOOBUtil.verifyHostOperations_Details(webd);
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
		VerifyOOBUtil.verifyHostOperations(webd);
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
		VerifyOOBUtil.verifyHostOperations(webd);
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
		VerifyOOBUtil.verifyHostOperations(webd);
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
		VerifyOOBUtil.verifyMiddlewareOperations(webd);
		VerifyOOBUtil.verifyMiddlewareOperations_Details(webd);
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
		VerifyOOBUtil.verifyMiddlewareOperations(webd);
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
		VerifyOOBUtil.verifyMiddlewareOperations(webd);
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
		VerifyOOBUtil.verifyMiddlewareOperations(webd);
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
		VerifyOOBUtil.verifyEnterpriseHealth(webd);
		VerifyOOBUtil.verifyEnterpriseHealth_Details(webd);
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
		VerifyOOBUtil.verifyEnterpriseHealth(webd);
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
		VerifyOOBUtil.verifyExadataHealth(webd);
		VerifyOOBUtil.verifyExadataHealth_Details(webd);
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
		VerifyOOBUtil.verifyExadataHealth(webd);
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
		VerifyOOBUtil.verifyUIGallery(webd);
		VerifyOOBUtil.verifyUIGallery_Details(webd);
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
		VerifyOOBUtil.verifyUIGallery(webd);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextCreateDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboard");

		TestGlobalContextUtil.CreateDashboardWithGC(webd, dsbName, gccontent,DashboardHomeUtil.DASHBOARD);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextCreateDashboardSet()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboardSet");

		TestGlobalContextUtil.CreateDashboardWithGC(webd, dsbsetName, gccontent,DashboardHomeUtil.DASHBOARDSET);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextDashboardHome()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextDashboardHome");

		TestGlobalContextUtil.visitDashboardPageWithGC(webd, "dashboards");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextLA()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextLA");

		TestGlobalContextUtil.visitServiceWithGC(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA, gccontent);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextOOBDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextOOBDashboard");

		TestGlobalContextUtil.visitOOBWithGC(webd, "Host Operations", gccontent);
	}
	
	@Test(alwaysRun = true)
	public void testGlobalContextOOBDashboardSet()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextOOBDashboardSet");

		TestGlobalContextUtil.visitOOBWithGC(webd, "Enterprise Health", gccontent);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextUDE()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextUDE");

		TestGlobalContextUtil.visitApplicationVisualAnalyzer(webd, BrandingBarUtil.NAV_LINK_TEXT_DATAEXPLORER, gccontent);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextWelcomePage()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testWelcomePage");

		TestGlobalContextUtil.visitDashboardPageWithGC(webd, "welcome");
	}

	@Test(alwaysRun = true)
	public void testomcCtx_OpenLAWidget()
	{
		dbName_la = "selfDb-" + DashBoardUtils.generateTimeStamp();
	
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testomcCtx_OpenLAWidget");
	
		TestGlobalContextUtil.openWidgetWithGC(webd, dbName_la, "All Logs Trend", gccontent);
	}

	@Test(alwaysRun = true)
	public void testomcCtx_OpenITAWidget()
	{
		dbName_ude = "selfDb-" + DashBoardUtils.generateTimeStamp();
	
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");
	
		TestGlobalContextUtil.openWidgetWithGC(webd, dbName_ude, "Analytics Line", gccontent);
	}
	
	@Test(alwaysRun = true)
	public void testomcCtx_DeleteDashboard()
	{
		dbName_willDelete = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextDeleteDashboard");

		TestGlobalContextUtil.deleteDashboardWithGC(webd, dbName_willDelete, dbName_WithGC,gccontent);	
	}

	@Test(alwaysRun = true)
	public void testomcCtx_DeleteDashboardSet()
	{
		dbSetName_willDelete = "selfDbSet-" + DashBoardUtils.generateTimeStamp();
		dbName_inSet = "inDbSet-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testGlobalContextDeleteDashboard");

		TestGlobalContextUtil.deleteDashboardSetWithGC(webd, dbSetName_willDelete, dbName_inSet, gccontent);		
	}

	@Test(alwaysRun = true)
	public void tesTGlobalContext_SwitchEntity()
	{
		dbName_tailsTest = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");

		TestGlobalContextUtil.switchEntity(webd, dbName_tailsTest, gccontent);
	}
}