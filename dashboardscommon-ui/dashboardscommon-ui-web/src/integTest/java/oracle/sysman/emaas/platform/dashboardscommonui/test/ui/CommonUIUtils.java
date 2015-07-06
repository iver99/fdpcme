/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboardscommonui.test.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author shangwan
 */
public class CommonUIUtils
{
	static String sTenantId = CommonUIUtils.getEmaasPropertyValue("TENANT_ID");
	static String sUrl = CommonUIUtils.getEmaasPropertyValue("COMMONUI_URL");
	static String sRegistryUrl = CommonUIUtils.getEmaasPropertyValue("COMMONUI_REGISTRY_URL");
	static String sSsoUserName = CommonUIUtils.getEmaasPropertyValue("SSO_USERNAME");
	static String sSsoPassword = CommonUIUtils.getEmaasPropertyValue("SSO_PASSWORD");

	static String sCommonUiUrlSuffix = CommonUIUtils.getEmaasPropertyValue("COMMON_UI_URL_SUFFIX");

	public static void commonUITestLog(String sDesc)
	{
		String sStr = "*** Dashboards Common UI TestLog ***:  " + sDesc;
		System.out.println(sStr);
	}

	public static String getEmaasPropertyValue(String sProperty)
	{

		File emaasPropertiesFile = new File(System.getenv("T_WORK") + "/emaas.properties.log");

		Properties emaasProp = new Properties();

		String sUrl = "";

		String sPropertyValue = "";

		InputStream input = null;

		try {
			input = new FileInputStream(emaasPropertiesFile);

			emaasProp.load(input);

			CommonUIUtils.commonUITestLog("Get the " + sProperty + " property value.");

			if (sProperty.equals("TENANT_ID")) {
				CommonUIUtils.commonUITestLog("Get the TENANT_ID property value.");
				sPropertyValue = emaasProp.getProperty("TENANT_ID");
				if (sPropertyValue == null) {
					CommonUIUtils.commonUITestLog("The TENANT_ID property value is null ... set it to a different value.");
					sPropertyValue = "emaastesttenant1";
				}
			}
			//			else if (sProperty.equals("OHS_REGISTRY_URL")) {
			//				sPropertyValue = emaasProp.getProperty("OHS_REGISTRY_URL");
			//				if (sPropertyValue == null) {
			//					CommonUIUtils.commonUITestLog("The OHS_REGISTRY_URL property value is null ... set it to a different value.");
			//					sUrl = CommonUIUtils.getEmaasPropertyValue("COMMONUI_URL");
			//					if (sUrl == null) {
			//						sPropertyValue = null;
			//					}
			//					else {
			//						CommonUIUtils.commonUITestLog("The OHS_URL property is '" + sOhsUrl + "'.");
			//						sPropertyValue = sOhsUrl + "/registry";
			//					}
			//				}
			//}
			else if (sProperty.equals("SSO_USERNAME")) {
				sPropertyValue = emaasProp.getProperty("SSO_USERNAME");
				if (sPropertyValue == null) {
					CommonUIUtils
							.commonUITestLog("The SSO_USERNAME property value is null ... set it to a different value -- 'emcsadmin'.");
					sPropertyValue = "emcsadmin";
				}
			}
			else if (sProperty.equals("SSO_PASSWORD")) {
				sPropertyValue = emaasProp.getProperty("SSO_PASSWORD");
				if (sPropertyValue == null) {
					CommonUIUtils
							.commonUITestLog("The SSO_PASSWORD property value is null ... set it to a different value -- 'Welcome1!'.");
					sPropertyValue = "Welcome1!";
				}
			}
			//			else if (sProperty.equals("COMMON_UI_URL_SUFFIX")) {
			//				sPropertyValue = emaasProp.getProperty("COMMON_UI_URL_SUFFIX");
			//				if (sPropertyValue == null) {
			//					CommonUIUtils
			//					.commonUITestLog("The COMMON_UI_URL_SUFFIX property value is null ... set it to a different value -- '/emsaasui/emcpdfcommonui/home.html'.");
			//					sPropertyValue = "/emsaasui/emcpdfcommonui/home.html";
			//				}
			//			}
			else {
				sPropertyValue = emaasProp.getProperty(sProperty);
			}

		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (sPropertyValue == null) {
			CommonUIUtils.commonUITestLog("WARNING:  The property value for " + sProperty + " is null.");
		}
		else {
			CommonUIUtils.commonUITestLog("The property value for " + sProperty + " is '" + sPropertyValue + "'.");
		}

		return sPropertyValue;

	}

	public static Boolean loginCommonUI(WebDriver driver)
	{

		String sCommonUiUrl = "";

		if (sUrl == null) {
			driver.getLogger().info("sUrl is null ... return false from loginCommonUI().");
			return false;
		}
		else {
			sCommonUiUrl = sUrl;
			driver.getLogger().info("sCommonUiUrl is " + sCommonUiUrl);
		}

		driver.getLogger().info("Supply credentials and doLogin()");
		LoginUtils.doLogin(driver, sSsoUserName, sSsoPassword, sTenantId, sCommonUiUrl);

		return true;

	}

	public static Boolean loginCommonUI(WebDriver driver, String parameters)
	{

		String sCommonUiUrl = "";

		if (sUrl == null) {
			driver.getLogger().info("sUrl is null ... return false from loginCommonUI().");
			return false;
		}
		else {
			sCommonUiUrl = sUrl + parameters;
			driver.getLogger().info("sCommonUiUrl is " + sCommonUiUrl);
		}

		driver.getLogger().info("Supply credentials and doLogin()");
		LoginUtils.doLogin(driver, sSsoUserName, sSsoPassword, sTenantId, sCommonUiUrl);

		return true;

	}

	public static void logoutCommonUI(WebDriver driver)
	{
		if (driver != null) {
			LoginUtils.doLogout(driver);
			driver.shutdownBrowser(true);
		}
	}
}
