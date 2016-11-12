/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class GlobalContextUtil_1130 extends GlobalContextUtil_Version implements IGlobalContextUtil
{
	public static final String GLBCTXTID = "emaas-appheader-globalcxt";
	public static final String GLBCTXTFILTERPILL = "globalBar_pillWrapper";
	public static final String GLBCTXTBUTTON = "buttonShowTopology";
	public static final String DSBNAME = "DASHBOARD_GLOBALTESTING";
	public static final String DSBSETNAME = "DASHBOARDSET_GLOBALTESTING";

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#generateGlbalContextUrl(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public String generateGlbalContextUrl(WebDriver driver, String baseurl, String meid)
	{
		String url = baseurl + "compositeMEID%" + meid;

		return url;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getGlbalContextMeid(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public String getGlbalContextMeid(WebDriver driver, String baseurl)
	{
		String meid = baseurl.substring(baseurl.indexOf("MEID") + 5, baseurl.indexOf("MEID") + 39);
		return meid;

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#getGlobalContextName(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getGlobalContextName(WebDriver driver)
	{
		return driver.getText(GLBCTXTFILTERPILL);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#hideTopology(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void hideTopology(WebDriver driver)
	{
		//need an identifier whether topology is hide or not
		driver.click(GLBCTXTBUTTON);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#isGlobalContextExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isGlobalContextExisted(WebDriver driver)
	{
		Boolean isGlobalContextExists = false;
		if (driver.isDisplayed(GLBCTXTID)) {
			driver.getLogger().info("the global context bar is  visible. Continue valiation");
			if (driver.isDisplayed(GLBCTXTFILTERPILL) && driver.isDisplayed(GLBCTXTFILTERPILL)) {
				isGlobalContextExists = true;
			}
			else {
				driver.getLogger().info("the global context bar items are not visible");
			}
		}
		else {
			driver.getLogger().info("the global context bar is not visible");
		}
		return isGlobalContextExists;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#showTopology(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void showTopology(WebDriver driver)
	{
		//need an identifier whether topology is hide or not
		driver.click(GLBCTXTBUTTON);

	}

}
