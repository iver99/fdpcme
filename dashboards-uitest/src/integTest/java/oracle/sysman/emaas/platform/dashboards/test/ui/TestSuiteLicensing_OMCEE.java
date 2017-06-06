package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
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
	
	public static final String GLBCTXTID = "emaas-appheader-globalcxt";
	public static final String GLBCTXTFILTERPILL = "globalBar_pillWrapper";
	public static final String GLBCTXTBUTTON = "buttonShowTopology";
	public static final String DSBNAME = "DASHBOARD_GLOBALTESTING";
	public static final String DSBSETNAME = "DASHBOARDSET_GLOBALTESTING";
	
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

		verifyAPM();
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

		verifyAPM();
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
		verifyAPM();
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
		verifyAPM();
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
		verifyOrchestration();
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
		verifyOrchestration();
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
		verifyOrchestration();
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
		verifyOrchestration();
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
		verifyApplicationPerfAnalytics();
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
		verifyApplicationPerfAnalytics();
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
		verifyApplicationPerfAnalytics();
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
		verifyApplicationPerfAnalytics();
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
		verifyAvailabilityAnalytics();
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
		verifyAvailabilityAnalytics();
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
		verifyAvailabilityAnalytics();
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
		verifyAvailabilityAnalytics();
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
		verifyEnterpriseHealth();
		verifyEnterpriseHealth_Details();
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
		verifyEnterpriseHealth();
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
		verifyEnterpriseHealth();
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
		verifyEnterpriseHealth();
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
		verifyExadataHealth();
		verifyExadataHealth_Details();
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
		verifyExadataHealth();
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
		verifyExadataHealth();
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
		verifyExadataHealth();
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
		verifyPerfAnalyticsApplication();
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
		verifyPerfAnalyticsApplication();
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
		verifyPerfAnalyticsApplication();
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
		verifyPerfAnalyticsApplication();
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
		verifyPerfAnalyticsDatabase();
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
		verifyPerfAnalyticsDatabase();
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
		verifyPerfAnalyticsDatabase();
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
		verifyPerfAnalyticsDatabase();
	}

	//@Test(alwaysRun = true)
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
		verifyResourceAnalyticsDatabase();
	}

	//@Test(alwaysRun = true)
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
		verifyResourceAnalyticsDatabase();
	}

	//@Test(alwaysRun = true)
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
		verifyResourceAnalyticsDatabase();
	}

	//@Test(alwaysRun = true)
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
		verifyResourceAnalyticsDatabase();
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
		verifyResourceAnalyticsHost();
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
		verifyResourceAnalyticsHost();
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
		verifyResourceAnalyticsHost();
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
		verifyResourceAnalyticsHost();
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
		verifyResourceAnalyticsMiddleware();
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
		verifyResourceAnalyticsMiddleware();
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
		verifyResourceAnalyticsMiddleware();
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
		verifyResourceAnalyticsMiddleware();
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
		verifyUIGallery();
		verifyUIGallery_Details();
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
		verifyUIGallery();
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
		verifyUIGallery();
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
		verifyUIGallery();
	}
	private void verifyOrchestration()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=37");
	
		DashboardBuilderUtil.verifyWidget(webd, "Overview");
		DashboardBuilderUtil.verifyWidget(webd, "Execution Details");

		webd.getLogger().info("Verification end...");
	}
	private void verifyAPM()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "apmUi/index.html");
		//verify the APM open correctly
		//TODO

		webd.getLogger().info("Verification end...");
	}
	private void verifyApplicationPerfAnalytics()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/ap-analytics-war/html/ap-perf-analytics.html");

		//verify the dashboard title & time picker
		webd.getLogger().info("Verify the dashboard title");
		webd.isTextPresent("Application Performance Analytics", "css=" + PageId.DASHBOARDTITLE_CSS);
		webd.getLogger().info("Verify the time picker");
		webd.isDisplayed("css=" + PageId.DATETIMEPICKER_OOB_CSS);

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verify end...");
	}
	private void verifyAvailabilityAnalytics()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/avail-analytics-war/html/avail-analytics-home.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
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

	private void verifyPerfAnalyticsApplication()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/mw-analytics-war/html/mw-perf-dashboard.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	private void verifyPerfAnalyticsDatabase()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/db-performance-analytics/html/db-performance-analytics.html");

		//verify all the widgets displayed
		//TODO

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

	private void verifyResourceAnalyticsDatabase()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/db-analytics-war/html/db-analytics-resource-planner.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	private void verifyResourceAnalyticsHost()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/resource-analytics/html/server-resource-analytics.html");

		//verify all the widgets displayed
		//TODO
		webd.getLogger().info("Verification end...");
	}

	private void verifyResourceAnalyticsMiddleware()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/mw-analytics-war/html/mw-analytics-resource-planner.html");

		//verify all the widgets displayed
		//TODO

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

		DashBoardUtils.deleteDashboard(webd, DSBNAME);
		DashBoardUtils.deleteDashboard(webd, DSBSETNAME);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextDashboardHome");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exist in DashboardHome");

	}

	//@Test(alwaysRun = true) 
	//commented out because of welcome page api version control's known issue
	public void testGlobalContextITA()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextITA");

		BrandingBarUtil.visitWelcome(webd);
		WelcomeUtil.visitITA(webd, "performanceAnalyticsDatabase");
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

	@Test(alwaysRun = true)
	public void testGlobalContextMonitoring()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextMonitoring");

		//visit welcome page
		webd.getLogger().info("Visit Welcome Page");
		BrandingBarUtil.visitWelcome(webd);
		BrandingBarUtil.visitApplicationCloudService(webd, "Infrastructure Monitoring");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exists in Monitoring Page");
		//	Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextOOBAPMDashboard()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextOOBAPMDashboard");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.selectOOB(webd, "Application Performance Monitoring");
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exist in APM");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextOOBDashboardSet()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testGlobalContextUDE");

		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, "Search");

		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in UDE Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextWelcomePage()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testWelcomePage");

		//visit welcome page
		webd.getLogger().info("Visit Welcome Page");
		BrandingBarUtil.visitWelcome(webd);
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in Welcome Page");

	}
	

	@Test(groups = "test_omcCtx")
	public void testomcCtx_OpenITAWidget()
	{
		dbName_ude = "selfDb-" + DashBoardUtils.generateTimeStamp();
	
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_tailsTest, null);
		DashboardBuilderUtil.verifyDashboard(webd, dbName_tailsTest, null, false);
		
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Analytics Line");
		DashboardBuilderUtil.saveDashboard(webd);	
		
		//find "GC entities" radio button, then select it
		DashboardBuilderUtil.respectGCForEntity(webd);
		
		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context isn't exists when select GC entities filter");
		//Assert.assertTrue(webd.isDisplayed(PageId.ENTITYBUTTON),"All Entities button isn't display on the top-left cornor, when select GC entities filter");
	
		//find "Use dashboard entities" radio button, then select it
		DashboardBuilderUtil.showEntityFilter(webd, true);	
		
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd),"The global context isn't exists when select dashboard entities filter");
		Assert.assertTrue(webd.isDisplayed(PageId.ENTITYBUTTON),"All Entities button isn't display on the top-left cornor, when select dashboard entities");
		
		//find "Use entities defined by content" radio button, then select it
		DashboardBuilderUtil.showEntityFilter(webd, false);
		
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd),"The global context isn't exists when select disable entities filter");
		Assert.assertFalse(webd.isDisplayed(PageId.ENTITYBUTTON), "All Entities button is present on the top-left cornor, when select disable entities fileter");
	}
}

