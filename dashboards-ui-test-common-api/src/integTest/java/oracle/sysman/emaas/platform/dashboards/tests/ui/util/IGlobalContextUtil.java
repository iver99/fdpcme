/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import java.util.Date;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public interface IGlobalContextUtil extends IUiTestCommonAPI
{

	/**
	 * Generate Global Context Url
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public String generateUrlWithGlobalContext(WebDriver driver, String baseurl, String compositeMeid, String timePeriod,
			Date startTime, Date endTime);

	/**
	 * Get GlobalContext Meid
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public String getGlobalContextMeid(WebDriver driver, String baseurl);

	/**
	 * Get the name of global context
	 *
	 * @param driver
	 *            WebDriver instance
	 */

	public String getGlobalContextName(WebDriver driver);

	/**
	 * Hide Topology
	 *
	 * @param driver
	 *            WebDriver instance
	 */

	public void hideTopology(WebDriver driver);

	/**
	 * Check whether Global Context Existed
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isGlobalContextExisted(WebDriver driver);

	/**
	 * Show Topology
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */

	public void showTopology(WebDriver driver);

}
