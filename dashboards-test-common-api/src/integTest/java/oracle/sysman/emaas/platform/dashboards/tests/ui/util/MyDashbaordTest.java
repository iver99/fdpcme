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

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class MyDashbaordTest extends LocalEmaasWebDriverLoader
{

	@Test
	public void testSearch() throws Exception
	{
		runEmaasWebDriver();
		WebDriver wd = myWebDriver();
		wd.getWebDriver().get("http://localhost:8383/emsaasui/emcpdfui/home.html");

		//DashboardHomeUtil.loadWebDriverOnly(wd);
		DashboardHomeUtil.search(wd, "Host");
		DashboardHomeUtil.listView(wd);
		DashboardHomeUtil.gridView(wd);
		// Now submit the form. WebDriver will find the form for us from the element
		//element.submit();
	}

}
