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

import org.testng.Assert;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class GlobalContextUtil_1180 extends GlobalContextUtil_1170
{	
	@Override
	public String getGlobalContextName(WebDriver driver)
	{
		Assert.assertTrue(false, "This method is not available in the current version, please use EntitySelectorUtil.getPillContents(WebDriver driver, Logger logger)");
		driver.getLogger().info("Method not available in the current version");
		return "";
	}
}