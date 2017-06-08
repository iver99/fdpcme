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

public class TestSuiteLicensing_SECSMA extends LoginAndLogout
{
	private final String tenant_SECSMA = DashBoardUtils.getTenantName("df_secsma");
	private final String tenant_username = "emcsadmin";
	private final String UDEWidget = "Analytics Line";
	private String dbName_Security = "";

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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to remove the test data...");

		DashBoardUtils.deleteDashboard(webd, dsbName);
		DashBoardUtils.deleteDashboard(webd, dsbsetName);
		DashBoardUtils.deleteDashboard(webd, dbName_ude);
		DashBoardUtils.deleteDashboard(webd, dbName_willDelete);
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
	public void testBrandingBar_SECSMA()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in Branding Bar--with Security Edition");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.SECSMA);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_SECSMA()
	{
		dbName_Security = "Dashboard OSMACC Security-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OSMACC Security";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in Builder Pager--with Security Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Security, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testHomepage_SECSMA()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in Home Page --with Security Edition");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.SECSMA);
	}

	@Test(alwaysRun = true)
	public void testHomePage_SECSMA_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test OOB in Home Page --with Security Edition");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		webd.getLogger().info("Verify the OOB dashboards display in home page");
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.SECSMA);

		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.SECSMA);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_SECSMA()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in test Welcome Page --with Security Edition");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.SECSMA);
	}
	
	@Test(alwaysRun = true)
	public void verifyDatabaseSecurity_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyDatabaseSecurity_GridView_SECSMA");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Oracle Database Security
		webd.getLogger().info("Open the OOB dashboard---Oracle Database Security");
		DashboardHomeUtil.selectDashboard(webd, "Oracle Database Security");

		//verify Database Security
		VerifyOOBUtil.verifyDatabaseSecurity();
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseSecurity_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyDatabaseSecurity_ListView_SECSMA()");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Oracle Database Security
		webd.getLogger().info("Open the OOB dashboard---Oracle Database Security");
		DashboardHomeUtil.selectDashboard(webd, "Oracle Database Security");

		//verify Database Security
		VerifyOOBUtil.verifyDatabaseSecurity();
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseSecurity_WithFilter_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyDatabaseSecurity_WithFilter_GridView_SECSMA()");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Oracle Database Security
		webd.getLogger().info("Open the OOB dashboard---Oracle Database Security");
		DashboardHomeUtil.selectDashboard(webd, "Oracle Database Security");

		//verify Database Security
		VerifyOOBUtil.verifyDatabaseSecurity();
	}

	@Test(alwaysRun = true)
	public void verifyDatabaseSecurity_WithFilter_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyDatabaseSecurity_WithFilter_ListView_SECSMA()");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Oracle Database Security
		webd.getLogger().info("Open the OOB dashboard---Oracle Database Security");
		DashboardHomeUtil.selectDashboard(webd, "Oracle Database Security");

		//verify Database Security
		VerifyOOBUtil.verifyDatabaseSecurity();
	}
	
	@Test(alwaysRun = true)
	public void verifyDNS_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyDNS_GridView_SECSMA()");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---DNS");
		DashboardHomeUtil.selectDashboard(webd, "DNS");

		//verify DNS
		VerifyOOBUtil.verifyDNS();
	}

	@Test(alwaysRun = true)
	public void verifyDNS_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyDNS_ListView_SECSMA()");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---DNS");
		DashboardHomeUtil.selectDashboard(webd, "DNS");

		//verify DNS
		VerifyOOBUtil.verifyDNS();
	}

	@Test(alwaysRun = true)
	public void verifyDNS_WithFilter_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyDNS_WithFilter_GridView_SECSMA()");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---DNS");
		DashboardHomeUtil.selectDashboard(webd, "DNS");

		//verify DNS
		VerifyOOBUtil.verifyDNS();
	}

	@Test(alwaysRun = true)
	public void verifyDNS_WithFilter_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyDNS_WithFilter_ListView_SECSMA()");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---DNS");
		DashboardHomeUtil.selectDashboard(webd, "DNS");

		//verify DNS
		VerifyOOBUtil.verifyDNS();
	}
	
	@Test(alwaysRun = true)
	public void verifyFirewall_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyFirewall_GridView_SECSMA()");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Firewall");
		DashboardHomeUtil.selectDashboard(webd, "Firewall");

		//verify Firewall
		VerifyOOBUtil.verifyFirewall();
	}

	@Test(alwaysRun = true)
	public void verifyFirewall_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyFirewall_ListView_SECSMA()");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Firewall");
		DashboardHomeUtil.selectDashboard(webd, "Firewall");

		//verify Firewall
		VerifyOOBUtil.verifyFirewall();
	}

	@Test(alwaysRun = true)
	public void verifyFirewall_WithFilter_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyFirewall_WithFilter_GridView_SECSMA()");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Firewall");
		DashboardHomeUtil.selectDashboard(webd, "Firewall");

		//verify Firewall
		VerifyOOBUtil.verifyFirewall();
	}

	@Test(alwaysRun = true)
	public void verifyFirewall_WithFilter_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("start to test in verifyFirewall_WithFilter_ListView_SECSMA()");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Firewall");
		DashboardHomeUtil.selectDashboard(webd, "Firewall");

		//verify Firewall
		VerifyOOBUtil.verifyFirewall();
	}


	@Test(alwaysRun = true)
	public void verifyEnterpriseHealth_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_GridView_SECSMA()");

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
	public void verifyEnterpriseHealth_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_ListView_SECSMA()");

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
	public void verifyEnterpriseHealth_WithFilter_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_WithFilter_GridView_SECSMA()");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

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
	public void verifyEnterpriseHealth_WithFilter_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_WithFilter_ListView_SECSMA()");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");
		
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
	public void verifyExadataHealth_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyExadataHealth_GridView_SECSMA()");

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
	public void verifyExadataHealth_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyExadataHealth_ListView_SECSMA()");

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
	public void verifyExadataHealth_WithFilter_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyExadataHealth_WithFilter_GridView_SECSMA()");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

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
	public void verifyExadataHealth_WithFilter_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyExadataHealth_WithFilter_ListView_SECSMA()");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

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
	public void verifyUIGallery_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyUIGallery_GridView_SECSMA()");

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
	public void verifyUIGallery_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyUIGallery_ListView_SECSMA()");

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
	public void verifyUIGallery_WithFilter_GridView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyUIGallery_WithFilter_GridView_SECSMA()");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

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
	public void verifyUIGallery_WithFilter_ListView_SECSMA()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test in verifyUIGallery_WithFilter_ListView_SECSMA");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Security Analytics");
		DashboardHomeUtil.filterOptions(webd, "security");

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
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboard");

		TestGlobalContextUtil.CreateDashboardWithGC(webd, dsbName, gccontent,DashboardHomeUtil.DASHBOARD);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextCreateDashboardSet()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testGlobalContextCreateDashboardSet");

		TestGlobalContextUtil.CreateDashboardWithGC(webd, dsbsetName, gccontent,DashboardHomeUtil.DASHBOARDSET);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextDashboardHome()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testGlobalContextDashboardHome");

		TestGlobalContextUtil.visitDashboardPageWithGC(webd, "dashboards");
	}

	@Test(alwaysRun = true)
	public void testGlobalContextOOBDashboardSet()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testGlobalContextOOBDashboardSet");

		TestGlobalContextUtil.visitOOBWithGC(webd, "Enterprise Health", gccontent);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextUDE()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testGlobalContextUDE");

		TestGlobalContextUtil.visitApplicationVisualAnalyzer(webd, BrandingBarUtil.NAV_LINK_TEXT_DATAEXPLORER, gccontent);
	}

	@Test(alwaysRun = true)
	public void testGlobalContextWelcomePage()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testWelcomePage");

		TestGlobalContextUtil.visitDashboardPageWithGC(webd, "welcome");
	}	

	@Test(alwaysRun = true)
	public void testomcCtx_OpenITAWidget()
	{
		dbName_ude = "selfDb-" + DashBoardUtils.generateTimeStamp();
	
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");
	
		TestGlobalContextUtil.openWidgetWithGC(webd, dbName_ude, "Analytics Line", gccontent);
	}
	
	@Test(alwaysRun = true)
	public void testomcCtx_DeleteDashboard()
	{
		dbName_willDelete = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testGlobalContextDeleteDashboard");

		TestGlobalContextUtil.deleteDashboardWithGC(webd, dbName_willDelete, dbName_WithGC,gccontent);			
	}

	@Test(alwaysRun = true)
	public void testomcCtx_DeleteDashboardSet()
	{
		dbSetName_willDelete = "selfDbSet-" + DashBoardUtils.generateTimeStamp();
		dbName_inSet = "inDbSet-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testGlobalContextDeleteDashboard");

		TestGlobalContextUtil.deleteDashboardSetWithGC(webd, dbSetName_willDelete, dbName_inSet, gccontent);
	}

	@Test(alwaysRun = true)
	public void tesTGlobalContext_SwitchEntity()
	{
		dbName_tailsTest = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSMA);
		webd.getLogger().info("Start the test case: testomcCtx_OpenITAWidget");

		TestGlobalContextUtil.switchEntity(webd, dbName_tailsTest, gccontent);
	}
}
