package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.TestGlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.VerifyOOBUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
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

public class TestSuiteLicensing_OMCEE extends LoginAndLogout
{
	private final String tenant_OMC_Enterprise = DashBoardUtils.getTenantName("df_omcee");

	private final String UDEWidget = "Analytics Line";
	private String dbName_Enterprise = "";

	private final String tenant_username = "emcsadmin";
	
	private String dsbName = "DASHBOARD_GLOBALTESTING";
	private String dsbsetName = "DASHBOARDSET_GLOBALTESTING";	
	private String dbName_WithGC = "TestDashboardWithGC";		
	private String dbName_ude = "";
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to remove the test data...");

		DashBoardUtils.deleteDashboard(webd, dsbName);
		DashBoardUtils.deleteDashboard(webd, dsbsetName);
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
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OMCEE()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in Branding Bar--with OMC Enterprise");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.OMCEE);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OMCEE()
	{
		dbName_Enterprise = "Dashboard OMC Enterprise-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OMC Enterprise";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in Builder Pager--with Enterprise Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Enterprise, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMCEE()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in Home Page -- with OMC Enterprise");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.OMCEE);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMCEE_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test OOB in Home Page -- with OMC Enterprise");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		webd.getLogger().info("Verify the OOB dashboards display in home page");
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OMCEE);

		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OMCEE);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OMCEE()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in test Welcome Page -- OMC Enterprise Edition");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.OMCEE);
	}
	
	@Test(alwaysRun = true)
	public void verifyAPM_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start to test in verifyAPM_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		VerifyOOBUtil.verifyAPM();
	}

	@Test(alwaysRun = true)
	public void verifyAPM_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start to test in verifyAPM_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		VerifyOOBUtil.verifyAPM();
	}

	@Test(alwaysRun = true)
	public void verifyAPM_withFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyAPM_withFilter_GridView");

		//click Filter-APM
		webd.getLogger().info("Click Cloud Services - APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		//verify APM
		VerifyOOBUtil.verifyAPM();
	}

	@Test(alwaysRun = true)
	public void verifyAPM_withFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyAPM_withFilter_ListView");

		//click Filter-APM
		webd.getLogger().info("Click Cloud Services - APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		//verify APM
		VerifyOOBUtil.verifyAPM();
	}
	
	@Test(alwaysRun = true)
	public void verifyOrchestration_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyOrchestration_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Orchestration Workflows
		VerifyOOBUtil.verifyOrchestration();
	}
	
	@Test(alwaysRun = true)
	public void verifyOrchestration_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyOrchestration_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Orchestration Workflows
		VerifyOOBUtil.verifyOrchestration();
	}
	
	@Test(alwaysRun = true)
	public void verifyOrchestration_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyOrchestration_GridView");

		//click Filter-orchestration
		webd.getLogger().info("Click Cloud Services - orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Orchestration Workflows
		VerifyOOBUtil.verifyOrchestration();
	}
	
	@Test(alwaysRun = true)
	public void verifyOrchestration_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyOrchestration_ListView");

		//click Filter-orchestration
		webd.getLogger().info("Click Cloud Services - orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Orchestration Workflows
		VerifyOOBUtil.verifyOrchestration();
	}
	
	@Test(alwaysRun = true)
	public void verifyApplicationPerfAnalytics_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		VerifyOOBUtil.verifyApplicationPerfAnalytics();
	}

	@Test(alwaysRun = true)
	public void verifyApplicationPerfAnalytics_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		VerifyOOBUtil.verifyApplicationPerfAnalytics();
	}

	@Test(alwaysRun = true)
	public void verifyApplicationPerfAnalytics_withFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_withFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		VerifyOOBUtil.verifyApplicationPerfAnalytics();
	}

	@Test(alwaysRun = true)
	public void verifyApplicationPerfAnalytics_withFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_withFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		VerifyOOBUtil.verifyApplicationPerfAnalytics();
	}

	@Test(alwaysRun = true)
	public void verifyAvailabilityAnalytics_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability nalytics
		VerifyOOBUtil.verifyAvailabilityAnalytics();
	}

	@Test(alwaysRun = true)
	public void verifyAvailabilityAnalytics_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability nalytics
		VerifyOOBUtil.verifyAvailabilityAnalytics();
	}

	@Test(alwaysRun = true)
	public void verifyAvailabilityAnalytics_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_WithFilter_GridView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability nalytics
		VerifyOOBUtil.verifyAvailabilityAnalytics();
	}

	@Test(alwaysRun = true)
	public void verifyAvailabilityAnalytics_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_WithFilter_ListView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability Analytics
		VerifyOOBUtil.verifyAvailabilityAnalytics();
	}
	
	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Enterprise Health
		VerifyOOBUtil.verifyEnterpriseHealth();
		VerifyOOBUtil.verifyEnterpriseHealth_Details();
	}

	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Enterprise Health
		VerifyOOBUtil.verifyEnterpriseHealth();
	}

	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Enterprise Health
		VerifyOOBUtil.verifyEnterpriseHealth();
	}

	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Enterprise Health
		VerifyOOBUtil.verifyEnterpriseHealth();
	}

	@Test(alwaysRun = true)
	public void verifyExadataHealth_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyExadataHealth_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		VerifyOOBUtil.verifyExadataHealth();
		VerifyOOBUtil.verifyExadataHealth_Details();
	}

	@Test(alwaysRun = true)
	public void verifyExadataHealth_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyExadataHealth_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		VerifyOOBUtil.verifyExadataHealth();
	}

	@Test(alwaysRun = true)
	public void verifyExadataHealth_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyExadataHealth_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		VerifyOOBUtil.verifyExadataHealth();
	}
	
	@Test(alwaysRun = true)
	public void verifyExadataHealth_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyExadataHealth_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		VerifyOOBUtil.verifyExadataHealth();
	}
	
	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsApplication_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		VerifyOOBUtil.verifyPerfAnalyticsApplication();
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsApplication_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		VerifyOOBUtil.verifyPerfAnalyticsApplication();
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsApplication_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		VerifyOOBUtil.verifyPerfAnalyticsApplication();
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsApplication_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		VerifyOOBUtil.verifyPerfAnalyticsApplication();
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsDatabase_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		VerifyOOBUtil.verifyPerfAnalyticsDatabase();
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsDatabase_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		VerifyOOBUtil.verifyPerfAnalyticsDatabase();
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsDatabase_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		VerifyOOBUtil.verifyPerfAnalyticsDatabase();
	}

	@Test(alwaysRun = true)
	public void verifyPerfAnalyticsDatabase_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		VerifyOOBUtil.verifyPerfAnalyticsDatabase();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsDatabase_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		VerifyOOBUtil.verifyResourceAnalyticsDatabase();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsDatabase_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		VerifyOOBUtil.verifyResourceAnalyticsDatabase();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsDatabase_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		VerifyOOBUtil.verifyResourceAnalyticsDatabase();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsDatabase_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		VerifyOOBUtil.verifyResourceAnalyticsDatabase();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsHost_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify esource Analytics Host
		VerifyOOBUtil.verifyResourceAnalyticsHost();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsHost_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify esource Analytics Host
		VerifyOOBUtil.verifyResourceAnalyticsHost();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsHost_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify esource Analytics Host
		VerifyOOBUtil.verifyResourceAnalyticsHost();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsHost_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify esource Analytics Host
		VerifyOOBUtil.verifyResourceAnalyticsHost();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsMiddleware_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		VerifyOOBUtil.verifyResourceAnalyticsMiddleware();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsMiddleware_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		VerifyOOBUtil.verifyResourceAnalyticsMiddleware();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsMiddleware_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		VerifyOOBUtil.verifyResourceAnalyticsMiddleware();
	}

	@Test(alwaysRun = true)
	public void verifyResourceAnalyticsMiddleware_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		VerifyOOBUtil.verifyResourceAnalyticsMiddleware();
	}

	@Test(alwaysRun = true)
	public void verifyUIGallery_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyUIGallery_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		VerifyOOBUtil.verifyUIGallery();
		VerifyOOBUtil.verifyUIGallery_Details();
	}

	@Test(alwaysRun = true)
	public void verifyUIGallery_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyUIGallery_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		VerifyOOBUtil.verifyUIGallery();
	}

	@Test(alwaysRun = true)
	public void verifyUIGallery_WithFilter_GridView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyUIGallery_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		VerifyOOBUtil.verifyUIGallery();
	}

	@Test(alwaysRun = true)
	public void verifyUIGallery_WithFilter_ListView_OMCEE()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test in verifyUIGallery_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		VerifyOOBUtil.verifyUIGallery();
	}	

	@Test(alwaysRun = true)
	public void testGlobalContextCreateDashboard()
	{
		dsbName = dsbName + DashBoardUtils.generateTimeStamp();
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboard");

		TestGlobalContextUtil.CreateDashboardWithGC(webd, dsbName, gccontent,DashboardHomeUtil.DASHBOARD);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextCreateDashboardSet()
	{
		dsbsetName = dsbsetName + DashBoardUtils.generateTimeStamp();
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboardSet");
		
		TestGlobalContextUtil.CreateDashboardWithGC(webd, dsbsetName, gccontent,DashboardHomeUtil.DASHBOARDSET);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextDashboardHome()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextDashboardHome");

		TestGlobalContextUtil.visitDashboardPageWithGC(webd, "dashboards");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextMonitoring()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextMonitoring");

		TestGlobalContextUtil.visitServiceWithGC(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_IM, gccontent);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextOOBAPMDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextOOBAPMDashboard");

		TestGlobalContextUtil.visitOOBWithGC(webd, "Application Performance Monitoring", gccontent);
	}
	
	@Test(alwaysRun = true)
	public void testGlobalContextOOBDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextOOBDashboard");
		
		TestGlobalContextUtil.visitOOBWithGC(webd, "Availability Analytics", gccontent);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextOOBDashboardSet()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextOOBDashboardSet");

		TestGlobalContextUtil.visitOOBWithGC(webd, "Enterprise Health", gccontent);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextUDE()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextUDE");

		TestGlobalContextUtil.visitApplicationVisualAnalyzer(webd, BrandingBarUtil.NAV_LINK_TEXT_DATAEXPLORER, gccontent);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextWelcomePage()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testWelcomePage");

		TestGlobalContextUtil.visitDashboardPageWithGC(webd, "welcome");
	}
	
	@Test(alwaysRun = true)
	public void testGlobalContextITAPage()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextITAPage");
		
		TestGlobalContextUtil.visitITAWithGC(webd, "performanceAnalyticsDatabase", gccontent);	
		TestGlobalContextUtil.visitITAWithGC(webd, "performanceAnalyticsApplicationServer", gccontent);
		TestGlobalContextUtil.visitITAWithGC(webd, "resourceAnalyticsDatabase", gccontent);
		TestGlobalContextUtil.visitITAWithGC(webd, "resourceAnalyticsMiddleware", gccontent);
		TestGlobalContextUtil.visitITAWithGC(webd, "resourceAnalyticsHost", gccontent);
		TestGlobalContextUtil.visitITAWithGC(webd, "applicationPerformanceAnalytic", gccontent);
		TestGlobalContextUtil.visitITAWithGC(webd, "availabilityAnalytics", gccontent);
		TestGlobalContextUtil.visitITAWithGC(webd, "dataExplorer", gccontent);
	}
	
	@Test(alwaysRun = true)
	public void testomcCtx_OpenITAWidget()
	{
		dbName_ude = "selfDb-" + DashBoardUtils.generateTimeStamp();
	
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");
	
		TestGlobalContextUtil.openWidgetWithGC(webd, dbName_ude, "Analytics Line", gccontent);
	}

	@Test(alwaysRun = true)
	public void testomcCtx_DeleteDashboard()
	{
		dbName_willDelete = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextDeleteDashboard");

		TestGlobalContextUtil.deleteDashboardWithGC(webd, dbName_willDelete, dbName_WithGC,gccontent);	
	}

	@Test(alwaysRun = true)
	public void testomcCtx_DeleteDashboardSet()
	{
		dbSetName_willDelete = "selfDbSet-" + DashBoardUtils.generateTimeStamp();
		dbName_inSet = "inDbSet-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextDeleteDashboard");

		TestGlobalContextUtil.deleteDashboardSetWithGC(webd, dbSetName_willDelete, dbName_inSet, gccontent);	
	}

	@Test(alwaysRun = true)
	public void testGlobalContext_SwitchEntity()
	{
		dbName_tailsTest = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");

		TestGlobalContextUtil.switchEntity(webd, dbName_tailsTest, gccontent);
	}
}