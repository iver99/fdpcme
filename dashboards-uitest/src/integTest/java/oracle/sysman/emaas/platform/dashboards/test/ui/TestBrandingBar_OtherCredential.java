package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestBrandingBar_OtherCredential extends LoginAndLogout
{

	public void initTestCustom(String testName, String Username)
	{
		customlogin(this.getClass().getName() + "." + testName, Username);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test(alwaysRun = true)
	public void testAdminConsoleLinkAPMUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_apm_user1");
		webd.getLogger().info("start to test in testAdminConsoleLink with normal APM user");
		WaitUtil.waitForPageFullyLoaded(webd);
		// Administration link for Administration Console UI
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ENTITIES_CFG);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "admin-console/ac/adminConsole.html");
		}
		else {
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			webd.getLogger().info(
					"Admin Console(Adminitration Link) should be unvisible in BrandingBar if login with normal user");
		}
	}

	@Test(alwaysRun = true)
	public void testAdminConsoleLinkITAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_ita_user1");
		webd.getLogger().info("start to test in testAdminConsoleLink with normal ITA user");
		WaitUtil.waitForPageFullyLoaded(webd);
		// Administration link for Administration Console UI
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ENTITIES_CFG);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "admin-console/ac/adminConsole.html");
		}
		else {
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			webd.getLogger().info(
					"Admin Console(Adminitration Link) should be unvisible in BrandingBar if login with normal user");
		}
	}

	@Test(alwaysRun = true)
	public void testAdminConsoleLinkLAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_la_user1");
		webd.getLogger().info("start to test in testAdminConsoleLink with normal LA user");
		WaitUtil.waitForPageFullyLoaded(webd);
		// Administration link for Administration Console UI
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ENTITIES_CFG);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "admin-console/ac/adminConsole.html");
		}
		else {
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			webd.getLogger().info(
					"Admin Console(Adminitration Link) should be unvisible in BrandingBar if login with normal user");
		}
	}

	@Test(alwaysRun = true)
	public void testAdminLinkAPMAdmin()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_apm_admin1");
		webd.getLogger().info("start to test in testAdminLinkAPMAdmin");
		WaitUtil.waitForPageFullyLoaded(webd);
		if (!DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			//validate admin link from Home page
			webd.getLogger().info("test admin link from dashboard home page");

			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
			BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);

			// navigate to APM page
			webd.getLogger().info("Nagivate to APM page");
			BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
			WaitUtil.waitForPageFullyLoaded(webd);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "apmUi/index.html");

			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
			BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
			WaitUtil.waitForPageFullyLoaded(webd);

			// Agents link
			webd.getLogger().info("Nagivate to Agents page");
			BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT);
			WaitUtil.waitForPageFullyLoaded(webd);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "tenantmgmt/services/customersoftware");

			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));

			webd.getLogger().info("Nagivate to Alert page");
			BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
			WaitUtil.waitForPageFullyLoaded(webd);
			// Alert link

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "eventUi/rules/html/rules-dashboard.html");
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
			BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
			WaitUtil.waitForPageFullyLoaded(webd);

			// Test From Home Page
			BrandingBarUtil.visitDashboardHome(webd);
			WaitUtil.waitForPageFullyLoaded(webd);
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
			BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
		}
	}

	@Test(alwaysRun = true)
	public void testAdminLinkAPMUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_apm_user1");
		webd.getLogger().info("start to test in testAdminLinkAPMUser");
		WaitUtil.waitForPageFullyLoaded(webd);
		if (!DashBoardUtils.isHamburgerMenuEnabled(webd)) {

			BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
			WaitUtil.waitForPageFullyLoaded(webd);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "apmUi/index.html");

			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			BrandingBarUtil.visitDashboardHome(webd);
			WaitUtil.waitForPageFullyLoaded(webd);
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
		}
	}

	@Test(alwaysRun = true)
	public void testAdminLinkITAAdmin()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_ita_admin1");
		webd.getLogger().info("start to test in testAdminLinkITAAdmin");
		WaitUtil.waitForPageFullyLoaded(webd);
		if (!DashBoardUtils.isHamburgerMenuEnabled(webd)) {

			// Navigate to one of the subscribed applications firstly (e.g. APM),
			// then navigate back to ensure user roles are synced up from OPC in Authorization storage
			BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
			WaitUtil.waitForPageFullyLoaded(webd);

			BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA);
			WaitUtil.waitForPageFullyLoaded(webd);
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));

			// Visit ITA
			WaitUtil.waitForPageFullyLoaded(webd);

			// Agents link
			BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT);
			WaitUtil.waitForPageFullyLoaded(webd);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "tenantmgmt/services/customersoftware");

			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));

			BrandingBarUtil.visitDashboardHome(webd);
			WaitUtil.waitForPageFullyLoaded(webd);
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
		}
	}

	@Test(alwaysRun = true)
	public void testAdminLinkITAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_ita_user1");
		webd.getLogger().info("start to test in testAdminLinkITAUser");
		WaitUtil.waitForPageFullyLoaded(webd);
		if (!DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			// Navigate to one of the subscribed applications firstly (e.g. APM),
			// then navigate back to ensure user roles are synced up from OPC in Authorization storage
			BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
			WaitUtil.waitForPageFullyLoaded(webd);

			BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA);
			WaitUtil.waitForPageFullyLoaded(webd);

			//verify the url of opened page
			DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/home.html?filter=ita");

			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			BrandingBarUtil.visitDashboardHome(webd);
			WaitUtil.waitForPageFullyLoaded(webd);
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
		}
	}

	@Test(alwaysRun = true)
	public void testAdminLinkLAAdmin()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_la_admin1");
		webd.getLogger().info("start to test in testAdminLinkLAAdmin");
		WaitUtil.waitForPageFullyLoaded(webd);
		if (!DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			DashboardHomeUtil.createDashboard(webd, "test_adminlink", null);
			webd.waitForServer();
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			DashboardBuilderUtil.deleteDashboard(webd);

			BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA);
			WaitUtil.waitForPageFullyLoaded(webd);
			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");

			//verify the Admin field
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));

			// Agents link
			BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT);
			WaitUtil.waitForPageFullyLoaded(webd);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "tenantmgmt/services/customersoftware");
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
			BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
			WaitUtil.waitForPageFullyLoaded(webd);
			// Alert link

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "eventUi/rules/html/rules-dashboard.html");
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
			// Test From Home Page
			BrandingBarUtil.visitDashboardHome(webd);
			WaitUtil.waitForPageFullyLoaded(webd);
			Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
			Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
		}
	}

	@Test(alwaysRun = true)
	public void testAdminLinkLAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_la_user1");
		WaitUtil.waitForPageFullyLoaded(webd);
		if (!DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			webd.getLogger().info("start to test in testAdminLinkLAUser");
			BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA);
			WaitUtil.waitForPageFullyLoaded(webd);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "emlacore/html/log-analytics-search.html");

			//verify if the Admin filed exists
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));

			//go to Dashboard home page & verify the admin field
			BrandingBarUtil.visitDashboardHome(webd);
			WaitUtil.waitForPageFullyLoaded(webd);
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
		}
	}

	@Test(alwaysRun = true)
	public void testAgentsLinkAPMUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_apm_user1");
		webd.getLogger().info("start to test in testAgentsLink with APM normal user");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Agents link
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_AGENTS);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "tenantmgmt/services/customersoftware");
		}
		else {
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			webd.getLogger().info("Agents Link should be unvisible in BrandingBar if login with normal user");
		}
	}

	@Test(alwaysRun = true)
	public void testAgentsLinkITAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_ita_user1");
		webd.getLogger().info("start to test in testAgentsLink with ITA normal user");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Agents link
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_AGENTS);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "tenantmgmt/services/customersoftware");
		}
		else {
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			webd.getLogger().info("Agents Link should be unvisible in BrandingBar if login with normal user");
		}
	}

	@Test(alwaysRun = true)
	public void testAgentsLinkLAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_la_user1");
		webd.getLogger().info("start to test in testAgentsLink with LA normal user");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Agents link
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_AGENTS);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "tenantmgmt/services/customersoftware");
		}
		else {
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			webd.getLogger().info("Agents Link should be unvisible in BrandingBar if login with normal user");
		}
	}

	@Test(alwaysRun = true)
	public void testAlertRulesAPMUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_apm_user1");
		webd.getLogger().info("start to test in testAlertRules with normal APM user");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Alert Rules link
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ALERT_RULES);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "eventUi/rules/html/rules-dashboard.html");
		}
		else {
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			webd.getLogger().info("Alert Rules Link should be unvisible in BrandingBar if login with normal user");
		}
	}

	@Test(alwaysRun = true)
	public void testAlertRulesITAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_ita_user1");
		webd.getLogger().info("start to test in testAlertRules with normal ITA user");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Alert Rules link
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ALERT_RULES);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "eventUi/rules/html/rules-dashboard.html");
		}
		else {
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			webd.getLogger().info("Alert Rules Link should be unvisible in BrandingBar if login with normal user");
		}
	}

	@Test(alwaysRun = true)
	public void testAlertRulesLAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_la_user1");
		webd.getLogger().info("start to test in testAlertRules with normal LA user");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Alert Rules link
		if (DashBoardUtils.isHamburgerMenuEnabled(webd)) {
			BrandingBarUtil.expandSubMenu(webd, BrandingBarUtil.ROOT_MENU_ADMIN);
			BrandingBarUtil.clickMenuItem(webd, BrandingBarUtil.GLOBAL_ADMIN_MENU_ALERT_RULES);

			//verify the url of opened page
			DashBoardUtils.verifyURL(webd, "eventUi/rules/html/rules-dashboard.html");
		}
		else {
			Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
			webd.getLogger().info("Alert Rules Link should be unvisible in BrandingBar if login with normal user");
		}
	}
}
