/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * Replace LoginAndLogout with LoginAndLogoutMockit, then you are able to run UI tests in your laptop
 */
public class LoginAndLogoutMockit extends LocalEmaasWebDriverLoader
{
	private static WebDriver webd = null;

	public static final String T_WORK_PATH = "/Users/wenjzhu/Documents/Temp";

	@AfterTest
	public static void logout()
	{
		if (webd != null) {
			// LoginUtils.doLogout(webd);
			webd.shutdownBrowser(true);
		}
	}

	@AfterMethod
	public static void logoutMethod()
	{
		if (webd != null) {
			//LoginUtils.doLogout(webd);
			webd.switchToParentPage();
			webd.click("id=menubutton");
			webd.click("id=emcpdf_oba_logout");
			webd.shutdownBrowser(true);
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
		runEmaasWebDriver();
		webd = myWebDriver();
		//TODO replace below url with your dev env
		String url = "https://den00zyr.us.oracle.com:4443/emsaasui/emcpdfui/home.html";
		//		webd.getLogger().info("before::start to test in LoginAndOut");
		//		webd.getLogger().info("url is " + url + "   properties file is <devMode>");
		// if the ui have been login, do not login ,again
		if (!webd.getWebDriver().getCurrentUrl().equals(url)) {
			webd.getWebDriver().get(url);
			webd.sendKeys("tenantName", tenantId);
			webd.click("signin");
			webd.sendKeys("username", username);
			webd.sendKeys("password", password);
			webd.click("signin");
		}
	}
}
