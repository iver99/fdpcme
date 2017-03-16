package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterMethod;

public class LoginAndLogout
{
	protected static WebDriver webd = null;

	@AfterMethod
	public static void logoutMethod()
	{
		if (webd != null) {
			((JavascriptExecutor) webd.getWebDriver()).executeScript("scroll(0,0)");
			LoginUtils.doLogout(webd);
			try {
				webd.shutdownBrowser(true);
			}
			catch (Exception e) {
				e.printStackTrace();
				webd.getLogger().warning("Failed to shutdown browser" + e.getMessage());
			}
		}
	}

	public void customlogin(String testName, String customUser)
	{
		String tenantID = null, username = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {
			e.printStackTrace();
			tenantID = "emaastesttenant1";
		}
		username = customUser;
		login(testName, username, "Welcome1!", tenantID, "home", "Dashboard-UI");

	}

	//added by Iris begin
	public void customlogin(String testName, String customUser, String newTenantID)
	{
		String tenantID = null, username = null;
		try {
			tenantID = newTenantID;
		}
		catch (Exception e) {
			e.printStackTrace();
			tenantID = "emaastesttenant1";
		}
		username = customUser;
		login(testName, username, "Welcome1!", tenantID, "home", "Dashboard-UI");

	}

	//added by Iris end

	public void login(String testName)
	{
		login(testName, "home");
	}

	public void login(String testName, String rel)
	{
		String tenantID = null, username = null, password = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {
			e.printStackTrace();
			tenantID = "emaastesttenant1";
		}

		try {
			username = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");

		}
		catch (Exception e) {
			e.printStackTrace();
			username = "emcsadmin";
		}
		try {
			password = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_PASSWORD");

		}
		catch (Exception e) {
			e.printStackTrace();
			password = "Welcome1!";
		}

		login(testName, username, password, tenantID, rel, "Dashboard-UI");
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
			e.printStackTrace();
			url = oracle.sysman.emsaas.login.utils.Utils.getProperty("OMCS_DASHBOARD_URL");
		}

		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		webd.getLogger().info("after::start to test in LoginAndOut");
		// if the ui have been login, do not login ,again
		//		if (!webd.getWebDriver().getCurrentUrl().equals(url)) {
		if (!webd.getWebDriver().getCurrentUrl().equals(url) && !webd.getWebDriver().getCurrentUrl().contains("omcCtx=")) {
			//Append omc context into login url
			if (!url.contains("omcCtx=")) {
				url = url
						+ (url.indexOf("?") > 0 ? "&" : "?")
						+ "omcCtx=compositeType%3DSystem%2520%28Generic%29%26compositeName%3D%252FSOA1213_base_domain%252Fbase_domain%252Fsoa_server1%252Fsoa-infra_System%26compositeMEID%3D8426448730BDF663A9806A69AA2C445B";
				webd.getLogger().info("New url with OMC global context appended is: " + url);
			}
			LoginUtils.doLogin(webd, username, password, tenantId, url);
		}
	}

	public void loginErrorPage(String testName, String url)
	{
		String tenantID = null, username = null, password = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {
			e.printStackTrace();
			tenantID = "emaastesttenant1";
		}

		try {
			username = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");

		}
		catch (Exception e) {
			e.printStackTrace();
			username = "emcsadmin";
		}
		try {
			password = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_PASSWORD");

		}
		catch (Exception e) {
			e.printStackTrace();
			password = "Welcome1!";
		}

		webd = WebDriverUtils.initWebDriver(testName);
		//		String url = null;
		//		webd.getLogger().info("before::start to test in LoginAndOut");
		//		try {
		//			url = PageUtils.getServiceLink(tenantID, "home", "Dashboard-UI");
		//		}
		//		catch (Exception e) {
		//
		//		}
		//
		//		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		//		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		webd.getLogger().info("after::start to test in LoginAndOut");
		// if the ui have been login, do not login ,again
		//		if (!webd.getWebDriver().getCurrentUrl().equals(url)) {
		if (!webd.getWebDriver().getCurrentUrl().equals(url) && !webd.getWebDriver().getCurrentUrl().contains("omcCtx=")) {
			//Append omc context into login url
			if (!url.contains("omcCtx=")) {
				url = url
						+ (url.indexOf("?") > 0 ? "&" : "?")
						+ "omcCtx=startTime%3D1478045700000%26endTime%3D1478240100000%26compositeMEID%3DEE380645A9711FFF881A146A00C98324";
				webd.getLogger().info("New url with OMC global context appended is: " + url);
			}
			LoginUtils.doLogin(webd, username, password, tenantID, url);
		}

	}

}
