package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @version
 * @author charles.c.chen
 * @since release specific (what release of product did this appear in)
 */

public class TestBrandingBar extends LoginAndLogout
{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@Test
	public void testAdminLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAdminLink");

		//IT Analytics Administration link
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_ITA);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emcitas/warehouseadmin/html/admin-sources.html");
	}

	@Test
	public void testAgentsLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAgentsLink");

		//Agents link
		BrandingBarUtil.visitApplicationAdministration(webd, BrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "tenantmgmt/services/customersoftware");
	}


	@Test
	public void testAPMLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testAPMLink");

		//APM link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_APM);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "apmUi/index.html");
	}

	@Test
	public void testDashBoardHomeLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testDashBoardHomeLink");

		//dashboardHome link
		BrandingBarUtil.visitDashboardHome(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emcpdfui/home.html");
	}

	@Test
	public void testHomeLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHomeLink");

		//Home link
		BrandingBarUtil.visitMyHome(webd);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emcpdfui/welcome.html");
	}

	@Test
	public void testITALink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testITALink");

		//IT Analytics link,check checkbox
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_ITA);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emcpdfui/home.html?filter=ita");
	}

	@Test
	public void testLALink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLALink");

		//Log Analytics link
		BrandingBarUtil.visitApplicationCloudService(webd, BrandingBarUtil.NAV_LINK_TEXT_CS_LA);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emlacore/html/log-analytics-search.html");
	}

	@Test
	public void testLogLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testLogLink");

		//Log link
		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, BrandingBarUtil.NAV_LINK_TEXT_VA_LA);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		Assert.assertEquals(url.substring(url.indexOf("emsaasui") + 9), "emlacore/html/log-analytics-search.html");
	}

	@Test
	public void testSearchLink() throws Exception
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testSearchLink");

		//Target link
		BrandingBarUtil.visitApplicationVisualAnalyzer(webd, BrandingBarUtil.NAV_LINK_TEXT_VA_TA);
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		String sub_str = url.substring(url.indexOf("emsaasui") + 9);
		Assert.assertEquals(sub_str.substring(0, 23), "emcta/ta/analytics.html");
	}
}
