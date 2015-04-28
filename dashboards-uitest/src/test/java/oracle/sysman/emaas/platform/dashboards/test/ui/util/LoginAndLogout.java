package oracle.sysman.emaas.platform.dashboards.test.ui.util;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import oracle.sysman.qatool.uifwk.webdriver.*;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.utils.Utils;

public class LoginAndLogout {
	public static WebDriver webd=null;
	
  public  void login(String testName,String username,String password,String tenantId,String rel,String servicename)
	{
		webd = WebDriverUtils.initWebDriver(testName);
		String url= PageUtils.getServiceLink(tenantId,rel,servicename);
		
		//String url = "https://slc07hcn.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
		//String url = "https://slc07dgg.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
		//String url = "https://slc07ptb.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
		webd.getLogger().info("start to test in LoginAndOut");	
		//if the ui have been login, do not login ,again
		if(!webd.getWebDriver().getCurrentUrl().equals(url))
		 LoginUtils.doLogin(webd, username, password, tenantId, url);
		
	}
	
	public  void login(String testName)
	{
		String tenantID = oracle.sysman.emsaas.login.utils.Utils.getProperty("TENANT_ID");
		String username = oracle.sysman.emsaas.login.utils.Utils.getProperty("SSO_USERNAME");
		login(testName,username, "Welcome1!",tenantID, "home", "Dashboard-UI");
		//login(testName,"emaasadmin", "Welcome1!","TenantOPC1", "home", "Dashboard-UI");
		
	}
	
	@AfterTest
	public static void logout()
	{
		if(webd!=null)
		{
		//LoginUtils.doLogout(webd);
			webd.shutdownBrowser(true);
		}
	}

}
