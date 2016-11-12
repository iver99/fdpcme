/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class GlobalContextUtil
{
	/**
	 * Generate Global Context Url
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static String generateGlbalContextUrl(WebDriver driver, String baseurl, String meid)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		return gcu.generateGlbalContextUrl(driver, baseurl, meid);
	}

	/**
	 * Get GlobalContext Meid
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static String getGlbalContextMeid(WebDriver driver, String baseurl)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		return gcu.getGlbalContextMeid(driver, baseurl);
	}

	/**
	 * Get the name of global context
	 *
	 * @param driver
	 *            WebDriver instance
	 */

	public static String getGlobalContextName(WebDriver driver)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		return gcu.getGlobalContextName(driver);
	}

	/**
	 * Hide Topology
	 *
	 * @param driver
	 *            WebDriver instance
	 */

	public static void hideTopology(WebDriver driver)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		gcu.hideTopology(driver);
	}

	/**
	 * Check whether Global Context Existed
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isGlobalContextExisted(WebDriver driver)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		return gcu.isGlobalContextExisted(driver);
	}

	/**
	 * Show Topology
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public static void showTopology(WebDriver driver)
	{
		IGlobalContextUtil gcu = new UtilLoader<IGlobalContextUtil>().loadUtil(driver, IGlobalContextUtil.class);
		gcu.showTopology(driver);
	}

	private void GobalContextUtil()
	{
	}
}
