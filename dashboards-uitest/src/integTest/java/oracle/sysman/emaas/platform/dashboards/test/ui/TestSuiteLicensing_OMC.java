package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestSuiteLicensing_OMC extends LoginAndLogout
{
	private final String tenant_OMC_Trail = DashBoardUtils.getTenantName("df_omc_trial");
	private final String tenant_OMC_Standard = DashBoardUtils.getTenantName("df_omc_standard");
	private final String tenant_OMC_Enterprise = DashBoardUtils.getTenantName("df_omc_enterprise");
	private final String tenant_OMC_Log = DashBoardUtils.getTenantName("df_omc_log");
	private final String UDEWidget = "Analytics Line";
	private String dbName_Trail = "";
	private String dbName_Standard = "";
	private String dbName_Enterprise = "";
	private String dbName_Log = "";

	private final String tenant_username = "emcsadmin";

	public void initTest(String testName, String customUser, String tenantname)
	{
		customlogin(this.getClass().getName() + "." + testName, customUser, tenantname);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OMC_Enterprise()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start to services verification in Branding Bar -- with OMC Enterprise Edition");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.OMCEE);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OMC_Log()
	{
		//verify Data Explorer link in branding bar
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in Branding Bar -- with OMC Log Edition");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.OMCLOG);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OMC_Standard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Standard);
		webd.getLogger().info("start to test in Branding Bar -- with OMC Standard Edition");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.OMCSE);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OMC_Trail()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Trail);
		webd.getLogger().info("start to test in Branding Bar -- with OMC Trail Edition");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.OMCTrail);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OMC_Enterprise()
	{
		dbName_Enterprise = "Dashboard OMC Enterprise-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OMC Enterprise";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in Builder Page -- with OMC Enterprise Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Enterprise, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OMC_Log()
	{
		dbName_Log = "Dashboard OMC Log-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OMC Log";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in Builder Page -- with OMC Log Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Log, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OMC_Standard()
	{
		dbName_Standard = "Dashboard OMC Standard-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OMC Standard";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Standard);
		webd.getLogger().info("start to test in Builder Pager--with OMC Standard Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Standard, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OMC_Trail()
	{
		dbName_Trail = "Dashboard OMC Trail-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OMC Trail";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Trail);
		webd.getLogger().info("start to test in Builder Page -- with OMC Trail Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Trail, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMC_Enterprise()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in Home Page -- with OMC Enterprise");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.OMCEE);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMC_Enterprise_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("Start to test OOB in Home Page -- with OMC Enterprise Edition");

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
	public void testHomePage_OMC_Log()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in Home Page -- with OMC Log Edition");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.OMCLOG);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMC_Log_OOBCheck()
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
	public void testHomePage_OMC_Standard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Standard);
		webd.getLogger().info("start to test in Home Page -- with OMC Standard");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.OMCSE);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMC_Standard_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Standard);
		webd.getLogger().info("start to test OOB in Home Page -- with OMC Standard");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		webd.getLogger().info("Verify the OOB dashboards display in home page");
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OMCSE);

		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OMCSE);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMC_Trail()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Trail);
		webd.getLogger().info("start to test in Home Page -- with OMC Trail");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.OMCTrail);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OMC_Trail_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Trail);
		webd.getLogger().info("Start to test OOB in Home Page -- with OMC Trail Edition");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		webd.getLogger().info("Verify the OOB dashboards display in home page");
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OMCTrail);

		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OMCTrail);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OMC_Enterprise()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Enterprise);
		webd.getLogger().info("start to test in test Welcome Page -- OMC Enterprise Edition");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.OMCEE);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OMC_Log()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Log);
		webd.getLogger().info("start to test in test Welcome Page -- OMC Log Edition");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.OMCLOG);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OMC_Standard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Standard);
		webd.getLogger().info("start to test in test Welcome Page -- OMC Standard Edition");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.OMCSE);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OMC_Trail()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OMC_Trail);
		webd.getLogger().info("start to test in test Welcome Page -- OMC Trail Edition");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.OMCTrail);
	}
}
