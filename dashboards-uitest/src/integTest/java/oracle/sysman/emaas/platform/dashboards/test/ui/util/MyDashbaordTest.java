/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emaas.platform.dashboards.test.ui.TestWelcomePage;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
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
		LocalEmaasWebDriverLoader.T_WORK_PATH = "D:/git/emcpdf/oracle/work";
		runEmaasWebDriver();
		WebDriver wd = myWebDriver();
		
		wd.getWebDriver().get("https://slc10uam.us.oracle.com:4443/emsaasui/emcpdfui/welcome.html");
		WelcomeUtil.loadWebDriverOnly(wd);
		TestWelcomePage.loadWebDriverOnly(wd);
		BrandingBarUtil.loadWebDriverOnly(wd);
		TestWelcomePage testWelcomePage = new TestWelcomePage();
		
		testWelcomePage.testOpenAPMPage();

//		DashboardHomeUtil.loadWebDriverOnly(wd);
//		DashboardHomeUtil.search("Host");
//		DashboardHomeUtil.listView();
//		DashboardHomeUtil.gridView();
		// Now submit the form. WebDriver will find the form for us from the element
		//element.submit();
	}

}
