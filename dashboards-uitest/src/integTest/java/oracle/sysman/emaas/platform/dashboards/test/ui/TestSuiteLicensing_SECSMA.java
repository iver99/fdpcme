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

public class TestSuiteLicensing_SECSMA extends LoginAndLogout
{
	private final String tenant_SECSMA = DashBoardUtils.getTenantName("df_secsma");
	private final String tenant_username = "emcsadmin";
	private final String UDEWidget = "Analytics Line";
	private String dbName_Security = "";

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
}
