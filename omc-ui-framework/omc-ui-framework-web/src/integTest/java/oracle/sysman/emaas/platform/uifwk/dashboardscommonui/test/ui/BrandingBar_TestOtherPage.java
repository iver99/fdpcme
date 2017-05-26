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
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class BrandingBar_TestOtherPage extends LoginAndLogout
{

	private static final Logger LOGGER = LogManager.getLogger(BrandingBar_TestOtherPage.class);
	private static boolean roles[] = { false, false, false, false };

	public void initTest(String testName, String queryString)
	{
		loginBrandingBar(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);

		roles = CommonUIUtils.getRoles();
	}

	@Test
	public void testAPMPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=APM");
		webd.getLogger().info("Start the test case: testAPMPage");

		CommonUIUtils.verifyPageContent(webd, "Application Performance Monitoring");

		//click the compass icon
		webd.getLogger().info("Click the Application navigator icon");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		CommonUIUtils.verifyMenu(webd, roles[0]);

		//click the compass icon again
		webd.getLogger().info("Click the Application navigator icon again");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		webd.getLogger().info("Verify the Links menu disappeared");
		CommonUIUtils.verifyNoLinksMenu(webd);

		//Open a widget
		CommonUIUtils.openWidget(webd, false);

	}

	@Test
	public void testErrorPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=Error");
		webd.getLogger().info("Start the test case: testErrorPage");

		//page loading
		webd.getLogger().info("Wait for the common UI page loading");
		webd.waitForElementPresent("toolbar-left");
		webd.takeScreenShot();

		//verify the product name,app name,content of page
		webd.getLogger().info("Verify the page content");
		Assert.assertTrue(webd.isElementPresent("css=" + UIControls.SORACLEIMAGE));
		Assert.assertEquals(webd.getAttribute("css=" + UIControls.SORACLEIMAGE + "@alt"), "Oracle");
		Assert.assertTrue(webd.isElementPresent("css=" + UIControls.SPRODUCTTEXT_CSS));
		webd.getLogger().info("The Product is:  " + webd.getText("css=" + UIControls.SPRODUCTTEXT_CSS));
		Assert.assertEquals(webd.getText("css=" + UIControls.SPRODUCTTEXT_CSS), "MANAGEMENT CLOUD");
		//Assert.assertTrue(webd.isElementPresent(UIControls.sAppText));
		Assert.assertTrue(webd.isElementPresent(UIControls.SPAGETEXT));
		webd.getLogger().info("The page content is:  " + webd.getText(UIControls.SPAGETEXT));
		Assert.assertEquals(webd.getText(UIControls.SPAGETEXT), "Sample page for OMC UI Framework components testing only");
		//Assert.assertTrue(webd.isElementPresent(UIControls.sCompassIcon));
		Assert.assertTrue(webd.isElementPresent(UIControls.SADDWIDGETICON));

		//click the compass icon
		webd.getLogger().info("Click the Application navigator icon");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		CommonUIUtils.verifyMenu(webd, roles[3]);

		//click the compass icon again
		webd.getLogger().info("Click the Application navigator icon again");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		webd.getLogger().info("Verify the Links menu disappeared");
		CommonUIUtils.verifyNoLinksMenu(webd);

		//Open a widget
		CommonUIUtils.openWidget(webd, false);
	}

	@Test
	public void testTenantPage()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=TenantManagement");
		webd.getLogger().info("Start the test case: testErrorPage");

		CommonUIUtils.verifyPageContent(webd, "Set up Oracle Management Cloud");

		//click the compass icon
		webd.getLogger().info("Click the Application navigator icon");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		CommonUIUtils.verifyMenu(webd, roles[3]);

		//click the compass icon again
		webd.getLogger().info("Click the Application navigator icon again");
		webd.waitForElementPresent(UIControls.SCOMPASSICON);
		webd.click(UIControls.SCOMPASSICON);

		webd.getLogger().info("Verify the Links menu disappeared");
		CommonUIUtils.verifyNoLinksMenu(webd);

		//Open a widget
		CommonUIUtils.openWidget(webd, false);
	}
}
