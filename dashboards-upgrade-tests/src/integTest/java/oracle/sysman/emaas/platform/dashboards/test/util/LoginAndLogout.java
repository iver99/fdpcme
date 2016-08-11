package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.testng.annotations.AfterMethod;

public class LoginAndLogout
{
	public static WebDriver webd = null;

	@AfterMethod
	public static void logout_method()
	{
		if (webd != null) {
			LoginUtils.doLogout(webd);
			try{
                             webd.shutdownBrowser(true);
                        }catch(Exception e){
                             webd.getLogger().warning("Failed to shutdown browser"+e.getMessage());
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
			tenantID = "emaastesttenant1";
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
		String tenantID = null, username = null, password = null;
		try {
			tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		}
		catch (Exception e) {
			tenantID = "emaastesttenant1";
		}

		try {
			username = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");

		}
		catch (Exception e) {
			username = "emcsadmin";
		}
		try {
			password = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_PASSWORD");

		}
		catch (Exception e) {
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
			url = oracle.sysman.emsaas.login.utils.Utils.getProperty("OMCS_DASHBOARD_URL");
		}

		String testPropertiesFile = System.getenv("EMAAS_PROPERTIES_FILE");
		webd.getLogger().info("url is " + url + "   properties file is " + testPropertiesFile);
		webd.getLogger().info("after::start to test in LoginAndOut");
		// if the ui have been login, do not login ,again
		if (!webd.getWebDriver().getCurrentUrl().equals(url)) {

			LoginUtils.doLogin(webd, username, password, tenantId, url);
		}
	}

}
