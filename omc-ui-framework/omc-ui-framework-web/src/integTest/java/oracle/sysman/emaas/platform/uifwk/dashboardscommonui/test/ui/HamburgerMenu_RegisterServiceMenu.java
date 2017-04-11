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
public class HamburgerMenu_RegisterServiceMenu extends LoginAndLogout
{
	private static final Logger LOGGER = LogManager.getLogger(HamburgerMenu_RegisterServiceMenu.class);
	private static String sAppName = "";
	private static boolean roles[] = { false, false, false, false };

	public void initTest(String testName, String queryString)
	{

		loginHamburgerMenu(this.getClass().getName() + "." + testName, queryString);
		CommonUIUtils.loadWebDriver(webd);
	}

	@Test
	public void verifyRegisterServiceMenu()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName(), "?appId=LogAnalytics");
		webd.getLogger().info("Start the test case: verifyRegisterServiceMenu");

		//click the 'Show Composite' button
		webd.getLogger().info("Click the 'Show Composite' button");
		String indicator = UIControls.SGENERATEMENUBTN.replace("_name_", "Register Service Menu");
		webd.click("css=" + indicator);

		//check the hamburger menu
		webd.getLogger().info("Check the menu items in hamburger menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Log Analytics"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Log Analytics"));

		//check the current header
		webd.getLogger().info("Check the Menu Header");
		Assert.assertEquals(BrandingBarUtil.getCurrentMenuHeader(webd), "OMC Management Cloud");

		//click Log Analytics to expand sub menu
		webd.getLogger().info("Click Log Analytics to expand sub menu");
		BrandingBarUtil.clickMenuItem(webd, "Log Analytics");

		//check the menu
		webd.getLogger().info("Check the menu items in Log Analytics menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Service menu 1"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Service menu 2"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Service menu 3"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Sample Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Service menu 1"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Service menu 2"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Service menu 3"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Sample Admin"));

		//click Sample Admin to expand sub menu
		webd.getLogger().info("Click Sample Admins to expan sub menu");
		BrandingBarUtil.clickMenuItem(webd, "Sample Admin");

		//check the menu
		webd.getLogger().info("Check the menu items in Sample Admin menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Sample admin menu 1"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Sample admin menu 1"));

		//Click Hierarchical to back to root menu
		webd.getLogger().info("Click Hierarchical to back to root menu");
		BrandingBarUtil.clickHierarchicalMenu(webd, "OMC Management Cloud");

		//check the current header
		webd.getLogger().info("Check the Menu Header");
		Assert.assertEquals(BrandingBarUtil.getCurrentMenuHeader(webd), "OMC Management Cloud");

		//click Adminitration
		BrandingBarUtil.clickMenuItem(webd, "Administration");

		webd.getLogger().info("Check the menu items in Administration menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Log Admin"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Log Admin"));

		BrandingBarUtil.clickMenuItem(webd, "Log Admin");

		//check the menu
		webd.getLogger().info("Check the menu items in Sample Admin menu");
		Assert.assertTrue(BrandingBarUtil.isMenuItemExisted(webd, "Sample admin menu 1"));
		Assert.assertTrue(BrandingBarUtil.isMenuItemEnabled(webd, "Sample admin menu 1"));

	}
}
