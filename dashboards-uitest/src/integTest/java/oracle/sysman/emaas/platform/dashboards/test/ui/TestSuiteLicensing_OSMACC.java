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

public class TestSuiteLicensing_OSMACC extends LoginAndLogout
{
	private final String tenant_OSMACC_Trail = "df_osmacc_trial";
	private final String tenant_OSMACC_Compliance = "df_osmacc_compliance";
	private final String tenant_OSMACC_Security = "df_osmacc_security";
	private final String tenant_username = "emcsadmin";
	private final String UDEWidget = "Analytics Line";
	private String dbName_Trail = "";
	private String dbName_Compliance = "";
	private String dbName_Security = "";

	public void initTest(String testName, String customUser, String tenantname)
	{
		customlogin(this.getClass().getName() + "." + testName, customUser, tenantname);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OSMACC_Compliance()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Compliance);
		webd.getLogger().info("start to test in Branding Bar--with OSMACC Compliance");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.SECSE);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OSMACC_Security()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Security);
		webd.getLogger().info("start to test in Branding Bar--with OSMACC Security");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.SECSMA);
	}

	@Test(alwaysRun = true)
	public void testBrandingBar_OSMACC_Trail()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Trail);
		webd.getLogger().info("start to test in Branding Bar--with OSMACC Trail");

		DashBoardUtils.verifyBrandingBarWithTenant(webd, DashBoardUtils.OSMACCTrail);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OSMACC_Compliance()
	{
		dbName_Compliance = "Dashboard OSMACC Compliance-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OSMACC Compliance";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Compliance);
		webd.getLogger().info("start to test in Builder Page -- with Compliance Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Compliance, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OSMACC_Security()
	{
		dbName_Security = "Dashboard OSMACC Security-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OSMACC Security";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Security);
		webd.getLogger().info("start to test in Builder Page -- with Security Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Security, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testBuilderPage_OSMACC_Trail()
	{
		dbName_Trail = "Dashboard OSMACC Trail-" + DashBoardUtils.generateTimeStamp();
		String dsbDesc = "Dashboard for OSMACC Trail";
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Trail);
		webd.getLogger().info("start to test in Builder Page -- with OSMACC Trail Edition");

		DashBoardUtils.verifyBuilderPageWithTenant(webd, dbName_Trail, dsbDesc, UDEWidget);
	}

	@Test(alwaysRun = true)
	public void testHomePage_OSMACC_Compliance_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Compliance);
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
	public void testHomePage_OSMACC_Security_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Security);
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
	public void testHomePage_OSMACC_Trail_OOBCheck()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Trail);
		webd.getLogger().info("start to test OOB in Home Page --with OSMACC Trail Edition");

		//switch to grid view
		webd.getLogger().info("Switch to Grid View");
		DashboardHomeUtil.gridView(webd);

		//verify all the oob display
		webd.getLogger().info("Verify the OOB dashboards display in home page");
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OSMACCTrail);

		webd.getLogger().info("Switch to List View");
		DashboardHomeUtil.listView(webd);

		//verify all the oob display
		DashBoardUtils.verifyOOBInHomeWithTenant(webd, DashBoardUtils.OSMACCTrail);
	}

	@Test(alwaysRun = true)
	public void testHompage_OSMACC_Compliance()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Compliance);
		webd.getLogger().info("start to test in Home Page -- with OSMACC Compliance");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.SECSE);
	}

	@Test(alwaysRun = true)
	public void testHompage_OSMACC_Security()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Security);
		webd.getLogger().info("start to test in Home Page -- with OSMACC Security");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.SECSMA);
	}

	@Test(alwaysRun = true)
	public void testHompage_OSMACC_Trail()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Trail);
		webd.getLogger().info("start to test in Home Page -- with OSMACC Trail");

		DashBoardUtils.verifyHomePageWithTenant(webd, DashBoardUtils.OSMACCTrail);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OSMACC_Compliance()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Compliance);
		webd.getLogger().info("start to test in test Welcome Page -- with OSMACC Compliance");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.SECSE);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OSMACC_Security()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Security);
		webd.getLogger().info("start to test in test Welcome Page -- with OSMACC Security");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.SECSMA);
	}

	@Test(alwaysRun = true)
	public void testWelcomepage_OSMACC_Trail()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), tenant_username, tenant_OSMACC_Trail);
		webd.getLogger().info("start to test in test Welcome Page -- with OSMACC Trail");
		BrandingBarUtil.visitWelcome(webd);

		DashBoardUtils.verifyWelcomePageWithTenant(webd, DashBoardUtils.OSMACCTrail);
	}
}