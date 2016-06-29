package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;

public class LoginAndLogout
{
	public static WebDriver webd = null;

	@AfterTest
	public static void logout()
	{
		if (webd != null) {
			LoginUtils.doLogout(webd);
			//webd.shutdownBrowser(true);
		}
	}

	@AfterMethod
	public static void logout_method()
	{
		if (webd != null) {
			LoginUtils.doLogout(webd);
		}
	}

	public void customlogin(String testName, String customUser)
	{
		String tenantID = null, username = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {
			tenantID = "emaastesttenant1";// site59tenant1";//";//site46tenant1";"emaastesttenant1";id0816b";//
		}
		username = customUser;
		login(testName, username, "Welcome1!", tenantID, "home", "Dashboard-UI");

	}

	public void login(String testName)
	{
		login(testName, "home");
	}

	public void login(String testName, String rel)
	{
		String tenantID = null, username = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {
			tenantID = "emaastesttenant1";// site59tenant1";//";//site46tenant1";"emaastesttenant1";id0816b";//
		}

		try {
			username = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");

		}
		catch (Exception e) {
			username = "emcsadmin";// juan.zhang@oracle.com";//
		}

		login(testName, username, "Welcome1!", tenantID, rel, "Dashboard-UI");
		// login(testName,"emaasadmin", "Welcome1!","TenantOPC1", "home",
		// "Dashboard-UI");
	}

	public void login(String testName, String username, String password, String tenantId, String rel, String servicename)
	{
		webd = WebDriverUtils.initWebDriver(testName);
		String url = null;
		webd.getLogger().info("before::start to test in LoginAndOut");
		try {
			url = PageUtils.getServiceLink(tenantId, rel, servicename);
		}
		catch (Exception e) {
			// url =
			// "https://slc04lec.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
			// url =
			// "https://slc07psz.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
			// url =
			// "https://slc00rjx.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
			url = "https://slc05mwm.us.oracle.com:4443/emsaasui/emcpdfui/home.html";// https://id0816b.itom.dc1.c9edgga.oraclecorp.com/emsaasui/emcpdfui/home.html";//";
			// url =
			// "https://slc04lec.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
			// url =
			// "https://slc09shg.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
			// url =
			// "https://slc08twq.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
		}

		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		// String url =
		// "https://slc07hcn.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
		// String url =
		// "https://slc07dgg.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
		// String url =
		// "https://slc07ptb.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
		webd.getLogger().info("after::start to test in LoginAndOut");
		// if the ui have been login, do not login ,again
		if (!webd.getWebDriver().getCurrentUrl().equals(url)) {
			try {
				LoginUtils.doLogin(webd, username, password, tenantId, url);
			}
			catch (Exception e) {
				webd.getLogger().info("LogUtils is null");
			}
		}
	}

}
