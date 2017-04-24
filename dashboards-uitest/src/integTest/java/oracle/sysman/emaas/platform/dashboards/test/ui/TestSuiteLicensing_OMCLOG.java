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

public class TestSuiteLicensing_OMCLOG extends LoginAndLogout
{
	private final String tenant_OMC_Log = DashBoardUtils.getTenantName("df_omclog");
	private final String UDEWidget = "Analytics Line";
	private String dbName_Log = "";

	private final String tenant_username = "emcsadmin";

	public void initTest(String testName, String customUser, String tenantname)
	{
		customlogin(this.getClass().getName() + "." + testName, customUser, tenantname);
		DashBoardUtils.loadWebDriver(webd);
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
}
