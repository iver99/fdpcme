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

import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
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
public class HamburgerMenu_ShowCurrentMenu extends LoginAndLogout
{
	private static final Logger LOGGER = LogManager.getLogger(HamburgerMenu_ShowCurrentMenu.class);
	private static String sAppName = "";
	private static boolean roles[] = { false, false, false, false };

	public void initTest(String testName, String queryString)
	{

		loginHamburgerMenu(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test
	public void verifyCurrentMenu()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "");
		webd.getLogger().info("Start the test case: verifyCurrentMenu");

		//click the 'Show Composite' button
		webd.getLogger().info("Click the 'Set Current Menu' button");
		String indicator = UIControls.SGENERATEMENUBTN.replace("_name_", "Set Current Menu");
		webd.click("css=" + indicator);

		//check the hamburger menu
		webd.getLogger().info("Check the menu items in hamburger menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Alert Rules"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Agents"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Entities Configuration"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "APM Admin"));

		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Infrastructure Monitoring Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Log Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Security Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Compliance Admin"));
		//check the current header
		webd.getLogger().info("Check the Menu Header");
		Assert.assertEquals(BrandingBarUtil.getCurrentMenuHeader(webd), "Administration");
	}
}
