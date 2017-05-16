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

import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.CommonUIUtils;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.uifwk.dashboardscommonui.test.ui.util.UIControls;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class BrandingBar_TestAnalyzerPage extends LoginAndLogout
{
	private static final Logger LOGGER = LogManager.getLogger(BrandingBar_TestAnalyzerPage.class);
	private static boolean roles[] = { false, false, false, false };

	public void initTest(String testName, String queryString)
	{
		loginBrandingBar(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);

		roles = CommonUIUtils.getRoles();
	}

	@Test
	public void testLogAnalyzerPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=LogAnalytics");
		webd.getLogger().info("Start the test case: testLogAnalyzerPage");

		CommonUIUtils.verifyPageContent(webd, "Log Analytics");

		//click the compass icon
		webd.getLogger().info("Click the Application navigator icon");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		webd.getLogger().info("isAPMAdmin:" + Boolean.toString(roles[0]));
		webd.getLogger().info("isITAAdmin:" + Boolean.toString(roles[1]));
		webd.getLogger().info("isLAAdmin:" + Boolean.toString(roles[2]));
		webd.getLogger().info("isDSAdmin:" + Boolean.toString(roles[3]));

		CommonUIUtils.verifyMenu(webd, roles[2]);

		//click the compass icon again
		webd.getLogger().info("Click the Application navigator icon again");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		webd.getLogger().info("Verify the Links menu disappeared");
		CommonUIUtils.verifyNoLinksMenu(webd);

		//Open a widget
		CommonUIUtils.openWidget(webd, true);
	}

	@Test
	public void testTargetAnalyzerPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=ITAnalytics");
		webd.getLogger().info("Start the test case: testTargetAnalyzerPage");

		CommonUIUtils.verifyPageContent(webd, "IT Analytics");

		//click the compass icon
		webd.getLogger().info("Click the Application navigator icon");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		CommonUIUtils.verifyMenu(webd, roles[1]);

		//click the compass icon again
		webd.getLogger().info("Click the Application navigator icon again");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		webd.getLogger().info("Verify the Links menu disappeared");
		CommonUIUtils.verifyNoLinksMenu(webd);

		//Open a widget
		CommonUIUtils.openWidget(webd, true);
	}
}
