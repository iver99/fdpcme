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

public class TestSuiteLicensing_SECSE extends LoginAndLogout
{
	private final String tenant_SECSE = DashBoardUtils.getTenantName("df_secse");
	private final String tenant_username = "emcsadmin";
	private final String UDEWidget = "Analytics Line";

	private String dbName_Compliance = "";

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
}
