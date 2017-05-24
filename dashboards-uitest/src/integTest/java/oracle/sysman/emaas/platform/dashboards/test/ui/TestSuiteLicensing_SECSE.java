package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestSuiteLicensing_SECSE extends LoginAndLogout
{
	private final String tenant_SECSE = DashBoardUtils.getTenantName("df_secse");
	private final String tenant_username = "emcsadmin";
	private final String UDEWidget = "Analytics Line";

	private String dbName_Compliance = "";
	
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
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_SECSE()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
		webd.getLogger().info("start to test in Branding Bar--with Compliance Edition");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.SECSE);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_SECSE()
	{
		dbName_Compliance = "Dashboard OSMACC Compliance-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OSMACC Compliance";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
		webd.getLogger().info("start to test in Builder Pager--with Compliance Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Compliance, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testHomepage_SECSE()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
		webd.getLogger().info("start to test in Home Page --with Compliance Edition");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.SECSE);
	}

	@Test(alwaysRun = true)
	public void testHomePage_SECSE_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
		webd.getLogger().info("start to test OOB in Home Page --with Compliance Edition");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		webd.getLogger().info("Verify the OOB dashboards display in home page");
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.SECSE);

		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.SECSE);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_SECSE()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
		webd.getLogger().info("start to test in test Welcome Page --with Compliance Edition");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.SECSE);
	}
	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
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

	@Test
	public void testGlobalContextCreateDashboard()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
		webd.getLogger().info("Start the test case: testGlobalContextDashboardHome");

		//visit home page
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd), "The global context doesn't exist in DashboardHome");

	}

	@Test
	public void testGlobalContextOOBDashboardSet()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
		webd.getLogger().info("Start the test case: testGlobalContextUDE");

		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, "Search");

		Assert.assertTrue(GlobalContextUtil.isGlobalContextExisted(webd), "The global context exists in UDE Page");
		//Assert.assertEquals(GlobalContextUtil.getGlobalContextName(webd),"/SOA1213_base_domain/base_domain/soa_server1/soa-infra_System");
	}

	@Test
	public void testGlobalContextWelcomePage()
	{

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
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
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
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

	@Test
	public void tesTGlobalContext_SwitchEntity()
	{
		dbName_tailsTest = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_SECSE);
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
		
		webd.waitForElementPresent(PageId.ENTITYBUTTON);
		
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd),"The global context isn't exists when select dashboard entities filter");
		Assert.assertTrue(webd.isDisplayed(PageId.ENTITYBUTTON),"All Entities button isn't display on the top-left cornor, when select dashboard entities");
		
		//find "Use entities defined by content" radio button, then select it
		DashboardBuilderUtil.showEntityFilter(webd, false);
		
		Assert.assertFalse(GlobalContextUtil.isGlobalContextExisted(webd),"The global context isn't exists when select disable entities filter");
		Assert.assertFalse(webd.isDisplayed(PageId.ENTITYBUTTON), "All Entities button is present on the top-left cornor, when select disable entities fileter");
	}
}
