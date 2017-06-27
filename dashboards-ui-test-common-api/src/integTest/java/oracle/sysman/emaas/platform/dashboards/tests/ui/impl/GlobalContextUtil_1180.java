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

import java.util.ArrayList;
import java.util.logging.Logger;

import org.testng.Assert;

import oracle.sysman.emaas.platform.dashboards.tests.ui.EntitySelectorUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class GlobalContextUtil_1180 extends GlobalContextUtil_1170
{	
	@Override
	public String getGlobalContextName(WebDriver driver)
	{
		//Assert.assertTrue(false, "This method is not available in the current version, please use EntitySelectorUtil.getPillContents(WebDriver driver, Logger logger)");
		driver.getLogger().warning("This method not supported in the current version, please use EntitySelectorUtil.getPillContents(WebDriver driver, Logger logger)");
		Logger logger = driver.getLogger();
		
		ArrayList<String> gcname = EntitySelectorUtil.getPillContents(driver, logger);
		
		if (gcname.size()>0)
		{
			driver.getLogger().info("return the first global context pill: " + gcname.get(0));
			return gcname.get(0);
		}
		else
		{
			return "";
		}		
	}
}