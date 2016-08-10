/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.timepicker.test.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author shangwan
 */
public class CommonUIUtils
{
	static String sTenantId = CommonUIUtils.getEmaasPropertyValue("TENANT_ID");
	static String sOhsUrl = CommonUIUtils.getEmaasPropertyValue("OHS_URL");
	static String sRegistryUrl = CommonUIUtils.getEmaasPropertyValue("OHS_REGISTRY_URL");
	static String sSsoUserName = CommonUIUtils.getEmaasPropertyValue("SSO_USERNAME");
	static String sSsoPassword = CommonUIUtils.getEmaasPropertyValue("SSO_PASSWORD");
	static String sAuthToken = CommonUIUtils.getEmaasPropertyValue("SAAS_AUTH_TOKEN");
	static String sAPIUrl = CommonUIUtils.getEmaasPropertyValue("DASHBOARD_API_ENDPOINT");

	static String sCommonUiUrlSuffix = CommonUIUtils.getEmaasPropertyValue("COMMON_UI_URL_SUFFIX");

	static String sAppName = "";

	public static void commonUITestLog(String sDesc)
	{
		String sStr = "*** Dashboards Common UI TestLog ***:  " + sDesc;
		//System.out.println(sStr);
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
			else if (sProperty.equals("OHS_REGISTRY_URL")) {
				sPropertyValue = emaasProp.getProperty("OHS_REGISTRY_URL");
				if (sPropertyValue == null) {
					CommonUIUtils.commonUITestLog("The OHS_REGISTRY_URL property value is null ... set it to a different value.");
					sOhsUrl = CommonUIUtils.getEmaasPropertyValue("OHS_URL");
					if (sOhsUrl == null) {
						sPropertyValue = null;
					}
					else {
						CommonUIUtils.commonUITestLog("The OHS_URL property is '" + sOhsUrl + "'.");
						sPropertyValue = sOhsUrl + "/registry";
					}
				}
			}
			else if (sProperty.equals("SSO_USERNAME")) {
				sPropertyValue = emaasProp.getProperty("SSO_USERNAME");
				if (sPropertyValue == null) {
					CommonUIUtils
							.commonUITestLog("The SSO_USERNAME property value is null ... set it to a different value -- 'emcsadmin'.");
					sPropertyValue = "emcsadmin";
				}
			}
			else if (sProperty.equals("SSO_PASSWORD")) {
				//	below password is being using in tests/dev mode only
				sPropertyValue = emaasProp.getProperty("SSO_PASSWORD");
				if (sPropertyValue == null) {
					CommonUIUtils
							.commonUITestLog("The SSO_PASSWORD property value is null ... set it to a different value -- 'Welcome1!'.");
					//below hard coded password is being using in tests/dev mode only
					sPropertyValue = "Welcome1!";
				}
			}
			else if (sProperty.equals("COMMON_UI_URL_SUFFIX")) {
				sPropertyValue = emaasProp.getProperty("COMMON_UI_URL_SUFFIX");
				if (sPropertyValue == null) {
					CommonUIUtils
							.commonUITestLog("The COMMON_UI_URL_SUFFIX property value is null ... set it to a different value -- '/emsaasui/uifwk/datetimePickerIndex.html'.");
					sPropertyValue = "/emsaasui/uifwk/datetimePickerIndex.html";
				}
			}
			else if (sProperty.equals("SAAS_AUTH_TOKEN")) {
				sPropertyValue = emaasProp.getProperty("SAAS_AUTH_TOKEN");
				if (sPropertyValue == null) {
					CommonUIUtils
					.commonUITestLog("The DASHBOARD_API_ENDPOINT property value is null ... set it to a different value -- 'welcome1'.");
					sPropertyValue = "Basic d2VibG9naWM6d2VsY29tZTE=";

				}
				else if (sProperty.equals("DASHBOARD_API_ENDPOINT")) {
					sPropertyValue = emaasProp.getProperty("DASHBOARD_API_ENDPOINT");
					if (sPropertyValue == null) {
						CommonUIUtils
						.commonUITestLog("The SAAS_AUTH_TOKEN property value is null ... set it to a different value .");
						sPropertyValue = sOhsUrl + "/emcpdf/api/v1/";
					}
				}
			}
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

	public static void InitValue()
	{
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI = sAPIUrl;

		Response res1 = RestAssured
				.given()
				.contentType(ContentType.JSON)
				.log()
				.everything()
				.headers("X-USER-IDENTITY-DOMAIN-NAME", sTenantId, "X-REMOTE-USER", sTenantId + "." + sSsoUserName,
						"Authorization", sAuthToken).when().get("/subscribedapps");
		CommonUIUtils.commonUITestLog("The statu code is:" + res1.getStatusCode() + res1.jsonPath().get("applications"));
		String s_appname = res1.jsonPath().getString("applications");
		CommonUIUtils.commonUITestLog("The response content is:" + s_appname);
		String[] ls_appname = s_appname.split(",");
		for (int i = 0; i < ls_appname.length; i++) {
			CommonUIUtils.commonUITestLog(i + " : " + ls_appname[i]);
			if (ls_appname[i].contains("APM")) {
				if (sAppName.equals("")) {
					sAppName = "Application Performance Monitoring";
				}
				else {
					sAppName = sAppName + " | Application Performance Monitoring";
				}
			}
			else if (ls_appname[i].contains("LogAnalytics")) {
				if (sAppName.equals("")) {
					sAppName = "Log Analytics";
				}
				else {
					sAppName = sAppName + " | Log Analytics";
				}
			}
			else if (ls_appname[i].contains("ITAnalytics")) {
				if (sAppName.equals("")) {
					sAppName = "IT Analytics";
				}
				else {
					sAppName = sAppName + " | IT Analytics";
				}
			}
		}
		CommonUIUtils.commonUITestLog("The App Name is:" + sAppName);

	}

	public static Boolean loginCommonUI(WebDriver driver)
	{

		String sCommonUiUrl = "";

		if (sOhsUrl == null) {
			driver.getLogger().info("sUrl is null ... return false from loginCommonUI().");
			return false;
		}
		else {
			sCommonUiUrl = sOhsUrl + sCommonUiUrlSuffix;
			driver.getLogger().info("sCommonUiUrl is " + sCommonUiUrl);
		}

		driver.getLogger().info("Supply credentials and doLogin()");
		LoginUtils.doLogin(driver, sSsoUserName, sSsoPassword, sTenantId, sCommonUiUrl);

		return true;

	}

	public static Boolean loginCommonUI(WebDriver driver, String parameters)
	{

		String sCommonUiUrl = "";

		if (sOhsUrl == null) {
			driver.getLogger().info("sUrl is null ... return false from loginCommonUI().");
			return false;
		}
		else {
			sCommonUiUrl = sOhsUrl + sCommonUiUrlSuffix + parameters;
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
