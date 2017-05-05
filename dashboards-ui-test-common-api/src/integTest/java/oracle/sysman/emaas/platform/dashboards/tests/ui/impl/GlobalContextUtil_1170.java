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


/**
 * @author miao
 *
 */
public class GlobalContextUtil_1170 extends GlobalContextUtil_1160
{
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IGlobalContextUtil#isGlobalContextExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isGlobalContextExisted(WebDriver driver)
	{
		Boolean isGlobalContextExists = false;
		if (driver.isDisplayed(GLBCTXTID)) {
			driver.getLogger().info("the global context bar is  visible. Continue valiation");
			if (driver.isDisplayed(GLBCTX_TOPBTN) && driver.isDisplayed(GLBCTX_CTXSEL_TEXT)) {
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
}
