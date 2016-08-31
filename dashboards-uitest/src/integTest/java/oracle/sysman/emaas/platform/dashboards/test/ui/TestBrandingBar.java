package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version
 * @author
 * @since release specific (what release of product did this appear in)
 */

public class TestBrandingBar extends LoginAndLogout
{

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	public void initTestCustom(String testName, String Username)
	{
		customlogin(this.getClass().getName() + "." + testName, Username);
		DashBoardUtils.loadWebDriver(webd);

	}

	//@Test
	public void testAdminConsoleLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAdminConsoleLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Administration link for Administration Console UI
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ADMINCONSOLE);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "admin-console/ac/adminConsole.html");
	}

	@Test
	public void testAdminLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAdminLink");
		WaitUtil.waitForPageFullyLoaded(webd);
	}

	@Test
	public void testAdminLinkAPMAdmin()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_apm_admin1");
		webd.getLogger().info("start to test in testAdminLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// navigate to APM page
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "apmUi/index.html");
		Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
		WaitUtil.waitForPageFullyLoaded(webd);
		// Agents link
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT);
		WaitUtil.waitForPageFullyLoaded(webd);
		url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "tenantmgmt/services/customersoftware");
		Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));

		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
		WaitUtil.waitForPageFullyLoaded(webd);
		// Alert link
		url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "eventUi/rules/html/rules-dashboard.html");
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

	@Test
	public void testAdminLinkAPMUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_apm_user1");
		webd.getLogger().info("start to test in testAdminLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "apmUi/index.html");
		Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
		BrandingBarUtil.visitDashboardHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
	}

	@Test
	public void testAdminLinkITAAdmin()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_ita_admin1");
		webd.getLogger().info("start to test in testAdminLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Navigate to one of the subscribed applications firstly (e.g. APM),
		// then navigate back to ensure user roles are synced up from OPC in Authorization storage
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA);
		WaitUtil.waitForPageFullyLoaded(webd);
		Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));

		// Visit ITA

		WaitUtil.waitForPageFullyLoaded(webd);

		// Agents link
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "tenantmgmt/services/customersoftware");

		Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));

		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
		WaitUtil.waitForPageFullyLoaded(webd);
		// Alert link
		url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "eventUi/rules/html/rules-dashboard.html");
		Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));

		BrandingBarUtil.visitDashboardHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
	}

	@Test
	public void testAdminLinkITAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_ita_user1");
		webd.getLogger().info("start to test in testAdminLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Navigate to one of the subscribed applications firstly (e.g. APM),
		// then navigate back to ensure user roles are synced up from OPC in Authorization storage
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emcpdfui/home.html?filter=ita");
		Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
		BrandingBarUtil.visitDashboardHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
	}

	@Test
	public void testAdminLinkLAAdmin()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_la_admin1");
		webd.getLogger().info("start to test in testAdminLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emlacore/html/log-analytics-search.html");

		Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
		// Agents link
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT);
		WaitUtil.waitForPageFullyLoaded(webd);
		url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "tenantmgmt/services/customersoftware");
		Assert.assertTrue(BrandingBarUtil.isAdmin(webd));

		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT));
		Assert.assertTrue(BrandingBarUtil.isAdminLinkExisted(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT));
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT);
		WaitUtil.waitForPageFullyLoaded(webd);
		// Alert link
		url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "eventUi/rules/html/rules-dashboard.html");
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

	@Test
	public void testAdminLinkLAUser()
	{
		initTestCustom(Thread.currentThread().getStackTrace()[1].getMethodName(), "emaastesttenant1_la_user1");
		WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("start to test in testAdminLink");
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emlacore/html/log-analytics-search.html");
		Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
		BrandingBarUtil.visitDashboardHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		Assert.assertFalse(BrandingBarUtil.isAdmin(webd));
	}

	@Test
	public void testAgentsLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAgentsLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Agents link
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "tenantmgmt/services/customersoftware");
	}

	@Test
	public void testAPMLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAPMLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// APM link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "apmUi/index.html");
	}

	@Test
	public void testDashBoardHomeLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testDashBoardHomeLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// dashboardHome link
		BrandingBarUtil.visitDashboardHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emcpdfui/home.html");
	}

	@Test
	public void testHomeLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomeLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Home link
		BrandingBarUtil.visitMyHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emcpdfui/welcome.html");
	}

	@Test
	public void testITALink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testITALink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// IT Analytics link,check checkbox
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emcpdfui/home.html?filter=ita");
	}

	@Test
	public void testLALink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLALink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Log Analytics link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emlacore/html/log-analytics-search.html");
	}

	@Test
	public void testLogLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLogLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Log link
		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, BrandingBarUtil.NAV_LINK_TEXT_VA_LA);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emlacore/html/log-analytics-search.html");
	}

	@Test
	public void testSearchLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testSearchLink");
		WaitUtil.waitForPageFullyLoaded(webd);

		// Target link
		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, BrandingBarUtil.NAV_LINK_TEXT_VA_TA);
		WaitUtil.waitForPageFullyLoaded(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		String sub_str = url.substring(url.indexOf("emsaasui") + 9);
		Assert.assertEquals(sub_str.substring(0, 23), "emcta/ta/analytics.html");
	}
}
