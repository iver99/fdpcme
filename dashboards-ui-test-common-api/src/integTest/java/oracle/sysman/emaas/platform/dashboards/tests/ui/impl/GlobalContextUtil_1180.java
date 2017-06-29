/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class GlobalContextUtil_1180 extends GlobalContextUtil_1170
{
	public static final String GLBCTXTBUTTON = "//div[@id='topologyButtonWrapper']/span/label";
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#showTopology(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void showTopology(WebDriver driver)
	{
		if (driver.isDisplayed(GLBCTXTPLDIV)) {
			driver.getLogger().info("the topology already got displayed");
		}
		else {
			driver.click(GLBCTXTBUTTON);
		}
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#hideTopology(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void hideTopology(WebDriver driver)
	{
		if (driver.isDisplayed(GLBCTXTPLDIV)) {
			driver.click(GLBCTXTBUTTON);
		}
		else {
			driver.getLogger().info("the topology already got hidden");
		}
	}
}
