/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

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

	static String sRolesUrl = CommonUIUtils.getEmaasPropertyValue("TARGETMODEL_SERVICE_SHARD_ENDPOINT");

	static String sCommonUiUrlSuffix = CommonUIUtils.getEmaasPropertyValue("COMMON_UI_URL_SUFFIX");

	static String sAppName = "";

	static Boolean isAPMAdmin = false;
	static Boolean isITAAdmin = false;
	static Boolean isLAAdmin = false;
	static Boolean isDSAdmin = false;

	public static void addWidget(WebDriver driver) throws Exception
	{
		//click Add Widget icon
		driver.getLogger().info("Verify if Add Widgets icon displayed");
		Assert.assertTrue(driver.isDisplayed(UIControls.SADDWIDGETICON));
		driver.getLogger().info("The buton is:  " + driver.getText(UIControls.SADDWIDGETICON));
		Assert.assertEquals(driver.getText(UIControls.SADDWIDGETICON), "Add");

		driver.getLogger().info("Click the Add Widgets icon");
		driver.waitForElementPresent(UIControls.SADDWIDGETICON);
		driver.click(UIControls.SADDWIDGETICON);

		driver.getLogger().info("Verify the Add Widgets window is opened");
		Assert.assertTrue(driver.isElementPresent(UIControls.SWIDGETWINDOWTITLE));
		driver.getLogger().info("The window title is:  " + driver.getText(UIControls.SWIDGETWINDOWTITLE));
		Assert.assertTrue(driver.isTextPresent("Add Widgets", UIControls.SWIDGETWINDOWTITLE));
		driver.takeScreenShot();
		driver.getLogger().info("Verify the Add Widgets button is disabled");
		driver.getLogger().info("The button is:  " + driver.getText(UIControls.SADDWIDGETBTN));
		Assert.assertEquals(driver.getText(UIControls.SADDWIDGETBTN), "Add");
		driver.getLogger().info("The button has been:  " + driver.getAttribute(UIControls.SADDWIDGETBTN + "@disabled"));
		Assert.assertNotNull(driver.getAttribute(UIControls.SADDWIDGETBTN + "@disabled"));
		driver.getLogger().info("Verify the select category drop-down list in Add Widgets button is displayed");
		Assert.assertTrue(driver.isElementPresent(UIControls.SCATEGORYSELECT));

		//Add a widget
		driver.getLogger().info("Select a widget and add it to the main page");
		driver.getLogger().info("Select a widget");
		driver.waitForElementPresent(UIControls.SWIDGETSELECT);
		driver.click(UIControls.SWIDGETSELECT);
		driver.getLogger().info("Click Add button");
		driver.waitForElementPresent(UIControls.SADDWIDGETBTN);
		driver.click(UIControls.SADDWIDGETBTN);
		driver.takeScreenShot();

		driver.getLogger().info("Close the Add Widget window");
		driver.waitForElementPresent(UIControls.SCLOSEWIDGET);
		driver.click(UIControls.SCLOSEWIDGET);
		driver.takeScreenShot();

		driver.getLogger().info("Verify the widget has been added to main page");
		Assert.assertTrue(driver.isElementPresent(UIControls.SWIDGET));
		driver.takeScreenShot();
	}

	public static void commonUITestLog(String sDesc)
	{
		String sStr = "*** Dashboards Common UI TestLog ***:  " + sDesc;
		System.out.println(sStr);
	}

	public static String getAppName(String sTenant, String sUser)
	{
		//		//String sAppName = "";
		//
		//		RestAssured.useRelaxedHTTPSValidation();
		//		RestAssured.baseURI = sAPIUrl;
		//
		//		Response res1 = RestAssured
		//				.given()
		//				.contentType(ContentType.JSON)
		//				.log()
		//				.everything()
		//				.headers("X-USER-IDENTITY-DOMAIN-NAME", sTenant, "X-REMOTE-USER", sTenant + "." + sUser, "Authorization",
		//						sAuthToken).when().get("/subscribedapps");
		//		CommonUIUtils.commonUITestLog("The statu code is:" + res1.getStatusCode() + res1.jsonPath().get("applications"));
		//		String s_appname = res1.jsonPath().getString("applications");
		//		//The response content contains "[" and "]", need to remove them before storing into array
		//		CommonUIUtils.commonUITestLog("The response content is:" + s_appname);
		//		s_appname = s_appname.replaceAll("\\[", "").replaceAll("\\]", "");
		//		String[] ls_appname = s_appname.split(",");
		//		for (int i = 0; i < ls_appname.length; i++) {
		//			//Trim the whitespace
		//			ls_appname[i] = ls_appname[i].trim();
		//		}
		//		Arrays.sort(ls_appname);
		//		for (int i = 0; i < ls_appname.length; i++) {
		//			CommonUIUtils.commonUITestLog(i + " : " + ls_appname[i]);
		//			if (ls_appname[i].contains("APM")) {
		//				if (sAppName.equals("")) {
		//					sAppName = "Application Performance Monitoring";
		//				}
		//				else {
		//					sAppName = sAppName + " | Application Performance Monitoring";
		//				}
		//			}
		//			else if (ls_appname[i].contains("ITAnalytics")) {
		//				if (sAppName.equals("")) {
		//					sAppName = "IT Analytics";
		//				}
		//				else {
		//					sAppName = sAppName + " | IT Analytics";
		//				}
		//			}
		//			else if (ls_appname[i].contains("LogAnalytics")) {
		//				if (sAppName.equals("")) {
		//					sAppName = "Log Analytics";
		//				}
		//				else {
		//					sAppName = sAppName + " | Log Analytics";
		//				}
		//			}
		//		}
		sAppName = ""; //With fix to EMCPDF-1220, the application name in Dashboard pages will be blank
		CommonUIUtils.commonUITestLog("The App Name is:" + sAppName);
		return sAppName;

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
					//	below hard coded password is being using in tests/dev mode only
					sPropertyValue = "Welcome1!";
				}
			}
			else if (sProperty.equals("COMMON_UI_URL_SUFFIX")) {
				sPropertyValue = emaasProp.getProperty("COMMON_UI_URL_SUFFIX");
				if (sPropertyValue == null) {
					CommonUIUtils
							.commonUITestLog("The COMMON_UI_URL_SUFFIX property value is null ... set it to a different value -- '/emsaasui/uifwk/test.html'.");
					sPropertyValue = "/emsaasui/uifwk/test.html";
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
			else if (sProperty.equals("TARGETMODEL_SERVICE_SHARD_ENDPOINT")) {
				sPropertyValue = emaasProp.getProperty("TARGETMODEL_SERVICE_SHARD_ENDPOINT");
				if (sPropertyValue == null) {
					CommonUIUtils
							.commonUITestLog("The TARGETMODEL_SERVICE_SHARD_ENDPOINT property value is null ... set it to a different value.");
					sRolesUrl = CommonUIUtils.getEmaasPropertyValue("EMCS_NODE2_HOSTNAME");
					if (sRolesUrl == null) {
						sPropertyValue = null;
					}
					else {
						CommonUIUtils.commonUITestLog("The TARGETMODEL_SERVICE_SHARD_ENDPOINT property is '" + sRolesUrl + "'.");

						sPropertyValue = "http://" + sRolesUrl + ":7004/tm-mgmt/api/v1.1";
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

	public static void getRoles(String sTenant, String sUser)
	{
		CommonUIUtils.commonUITestLog("The Roles URL is:" + sRolesUrl);

		//String sTempRolsUrl = sRolesUrl.substring(0, sRolesUrl.length() - 16);
		String sTempRolsUrl = sRolesUrl.substring(0, sRolesUrl.indexOf("tm-mgmt"));

		CommonUIUtils.commonUITestLog("The Roles URL is:" + sTempRolsUrl);
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI = sTempRolsUrl + "authorization/ws/api/v1/";
		CommonUIUtils.commonUITestLog("The base URL is:" + sTempRolsUrl + "authorization/ws/api/v1/");

		Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
				.headers("OAM_REMOTE_USER", sTenant + "." + sUser, "Authorization", sAuthToken).when()
				.get("/roles/grants/getRoles?grantee=" + sTenant + "." + sUser);
		CommonUIUtils.commonUITestLog("The statu code is:" + res1.getStatusCode() + ", The response content is "
				+ res1.jsonPath().get("roleNames"));
		String s_rolename = res1.jsonPath().getString("roleNames");
		//CommonUIUtils.commonUITestLog("The response content is:" + s_rolename);
		String[] ls_rolename = s_rolename.split(",");
		for (int i = 0; i < ls_rolename.length; i++) {
			CommonUIUtils.commonUITestLog(i + " : " + ls_rolename[i]);
			if (ls_rolename[i].contains("APM Administrator")) {
				isAPMAdmin = true;
			}
			else if (ls_rolename[i].contains("IT Analytics Administrator")) {
				isITAAdmin = true;
			}
			else if (ls_rolename[i].contains("Log Analytics Administrator")) {
				isLAAdmin = true;
			}
		}
		if (isAPMAdmin || isITAAdmin || isLAAdmin) {
			isDSAdmin = true;
		}
	}

	public static Boolean loginCommonUI(WebDriver driver, String sTenant, String sUser, String sPassword)
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
		LoginUtils.doLogin(driver, sUser, sPassword, sTenant, sCommonUiUrl);

		return true;

	}

	public static Boolean loginCommonUI(WebDriver driver, String parameters, String sTenant, String sUser, String sPassword)
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
		LoginUtils.doLogin(driver, sUser, sPassword, sTenant, sCommonUiUrl);

		return true;

	}

	public static void logoutCommonUI(WebDriver driver)
	{
		if (driver != null) {
			LoginUtils.doLogout(driver);
			driver.shutdownBrowser(true);
		}
	}

	public static void openWidget(WebDriver driver, boolean isEnabled) throws Exception
	{
		if (isEnabled) {
			//click Open Widget icon
			driver.getLogger().info("Verify if Open Widgets icon displayed");
			Assert.assertTrue(driver.isDisplayed(UIControls.SADDWIDGETICON));
			driver.getLogger().info("The buton is:  " + driver.getText(UIControls.SADDWIDGETICON));
			Assert.assertEquals(driver.getText(UIControls.SADDWIDGETICON), "Open");

			driver.getLogger().info("Click the Open icon");
			driver.waitForElementPresent(UIControls.SADDWIDGETICON);
			driver.click(UIControls.SADDWIDGETICON);

			driver.getLogger().info("Verify the Open Widgets window is opened");
			Assert.assertTrue(driver.isElementPresent(UIControls.SWIDGETWINDOWTITLE));
			driver.getLogger().info("The window title is:  " + driver.getText(UIControls.SWIDGETWINDOWTITLE));
			Assert.assertTrue(driver.isTextPresent("Open", UIControls.SWIDGETWINDOWTITLE));
			driver.takeScreenShot();
			driver.getLogger().info("Verify the Open button is disabled");
			driver.getLogger().info("The button is:  " + driver.getText(UIControls.SADDWIDGETBTN));
			Assert.assertEquals(driver.getText(UIControls.SADDWIDGETBTN), "Open");
			driver.getLogger().info("The button has been:  " + driver.getAttribute(UIControls.SADDWIDGETBTN + "@disabled"));
			Assert.assertNotNull(driver.getAttribute(UIControls.SADDWIDGETBTN + "@disabled"));
			driver.getLogger().info("Verify the select category drop-down list in Add Widgets button is displayed");
			try {
				driver.getLogger().info("the category display is: " + driver.isDisplayed(UIControls.SCATEGORYSELECT));
			}
			catch (RuntimeException re) {
				Assert.fail(re.getLocalizedMessage());
			}
			//Assert.assertFalse(driver.isElementPresent(UIControls.sCategorySelect));

			//Open a widget
			if (!driver.getAttribute(UIControls.SWIDGETDISPLAY + "@childElementCount").equals("0")) {
				driver.getLogger().info("Select a widget and open it in the main page");
				driver.getLogger().info("Select a widget");
				driver.waitForElementPresent(UIControls.SWIDGETSELECT);
				driver.click(UIControls.SWIDGETSELECT);

				driver.getLogger().info("Click Open button");
				driver.waitForElementPresent(UIControls.SADDWIDGETBTN);
				driver.click(UIControls.SADDWIDGETBTN);
				driver.takeScreenShot();

				driver.getLogger().info("Verify the widget has been opened in main page");
				Assert.assertTrue(driver.isElementPresent(UIControls.SWIDGET));
				driver.takeScreenShot();
			}
		}
		else {
			//verify the Open widget icon is disabled
			driver.getLogger().info("Verify the Open widget icon");
			Assert.assertTrue(driver.isDisplayed(UIControls.SADDWIDGETICON));
			driver.getLogger().info("The buton is:  " + driver.getText(UIControls.SADDWIDGETICON));
			Assert.assertEquals(driver.getText(UIControls.SADDWIDGETICON), "Open");
			driver.getLogger().info("Verify the Open widget icon is disabled");
			driver.getLogger().info("The icon has been:  " + driver.getAttribute(UIControls.SADDWIDGETICON + "@disabled"));
			Assert.assertNotNull(driver.getAttribute(UIControls.SADDWIDGETICON + "@disabled"));
		}
	}

	public static void verifyMenu(WebDriver driver, boolean isAdmin) throws Exception
	{
		//verify the menus
		driver.getLogger().info("Verify the Links menu displayed");
		driver.getLogger().info("The Link menu is:  " + driver.getAttribute(UIControls.SLINKSMENU + "@style"));
		Assert.assertNotEquals(driver.getAttribute(UIControls.SLINKSMENU + "@style"), "display: none;");

		if (isAdmin) {

			Assert.assertTrue(driver.isElementPresent(UIControls.SHOME));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMEICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMELABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMELINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICE));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICEICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICELABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICELINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZER));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERLABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERLINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SADMIN));
			Assert.assertTrue(driver.isElementPresent(UIControls.SADMINICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SADMINLABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SADMINLINK));
		}
		else {
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOME));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMEICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMELABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SHOMELINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICE));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICEICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICELABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SCLOUDSERVICELINK));

			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZER));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERICON));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERLABEL));
			Assert.assertTrue(driver.isElementPresent(UIControls.SANALYZERLINK));

			Assert.assertFalse(driver.isElementPresent(UIControls.SADMIN));
			Assert.assertFalse(driver.isElementPresent(UIControls.SADMINICON));
			Assert.assertFalse(driver.isElementPresent(UIControls.SADMINLABEL));
			Assert.assertFalse(driver.isElementPresent(UIControls.SADMINLINK));
		}
	}

	public static void verifyNoLinksMenu(WebDriver driver) throws Exception
	{
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 900L);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(UIControls.SLINKSMENU)));
		Assert.assertEquals(driver.getAttribute(UIControls.SLINKSMENU + "@style"), "display: none;");

	}

	public static void verifyPageContent(WebDriver driver, String sAppName) throws Exception
	{
		//verify the product name,app name,content of page
		driver.getLogger().info("Verify the page content");
		//Oracle logo
		Assert.assertTrue(driver.isElementPresent(UIControls.SORACLEIMAGE));
		Assert.assertEquals(driver.getAttribute(UIControls.SORACLEIMAGE + "@alt"), "Oracle");
		//Product title
		Assert.assertTrue(driver.isElementPresent(UIControls.SPRODUCTTEXT));
		String productTitle = "Management Cloud";
		driver.waitForText(UIControls.SPRODUCTTEXT, productTitle);
		driver.getLogger().info("The Product is:  " + driver.getText(UIControls.SPRODUCTTEXT));
		Assert.assertEquals(driver.getText(UIControls.SPRODUCTTEXT), productTitle);
		//Application names
		Assert.assertTrue(driver.isElementPresent(UIControls.SAPPTEXT));
		driver.waitForText(UIControls.SAPPTEXT, sAppName);
		driver.getLogger().info("The App is:  " + sAppName);
		Assert.assertEquals(driver.getText(UIControls.SAPPTEXT), sAppName);
		//Page title
		Assert.assertTrue(driver.isElementPresent(UIControls.SPAGETEXT));
		String pageTitle = "Sample page for OMC UI Framework components testing only";
		driver.waitForText(UIControls.SPAGETEXT, pageTitle);
		driver.getLogger().info("The page content is:  " + driver.getText(UIControls.SPAGETEXT));
		Assert.assertEquals(driver.getText(UIControls.SPAGETEXT), pageTitle);
		//Buttons
		Assert.assertTrue(driver.isElementPresent(UIControls.SCOMPASSICON));
		Assert.assertTrue(driver.isElementPresent(UIControls.SADDWIDGETICON));
	}
}
