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

import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestGlobalContext extends CommonUIUtils
{
	private static final Logger LOGGER = LogManager.getLogger(TestAnalyzerPage.class);

	@BeforeClass
	public static void initValue()
	{
		//CommonUIUtils.getAppName(sTenantId,sSsoUserName);
		CommonUIUtils.getRoles(sTenantId, sSsoUserName);
	}

	@Test
	public void testAddWidget()
	{
		try {
			CommonUIUtils.commonUITestLog("This is to test Log Analyzer Page");

			String testName = this.getClass().getName() + ".testAddWidget";
			WebDriver webdriver = WebDriverUtils.initWebDriver(testName);

			//login
			Boolean bLoginSuccessful = CommonUIUtils.loginCommonUI(webdriver, sTenantId, sSsoUserName, sSsoPassword);
			webdriver.getLogger().info("Assert that common UI login was successfuly");
			Assert.assertTrue(bLoginSuccessful);

			//Add a widget
			CommonUIUtils.addWidget(webdriver);

			//verify the result
			String timeRange = GlobalContextUtil.getTimeRangeLabel(webdriver).trim();
			Assert.assertTrue(timeRange.contains("Last year"));

			String currurl = webdriver.getWebDriver().getCurrentUrl();

			webdriver.getLogger().info("the origin url = " + currurl);

			String tmpurl = currurl.substring(currurl.indexOf("emsaasui") + 9);

			webdriver.getLogger().info("the url want to compare = " + tmpurl);
			String url = "uifwk/test.html?omcCtx=timePeriod%3DLAST_1_YEAR";

			Assert.assertTrue(tmpurl.contains(url), "Not open the expected url: " + url);

			//logout
			webdriver.getLogger().info("Logout");
			CommonUIUtils.logoutCommonUI(webdriver);

		}
		catch (Exception ex) {
			LOGGER.info("context", ex);
			Assert.fail(ex.getLocalizedMessage());
		}
	}
}
