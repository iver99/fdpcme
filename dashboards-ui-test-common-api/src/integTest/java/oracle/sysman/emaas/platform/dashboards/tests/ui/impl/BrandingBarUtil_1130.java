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

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class BrandingBarUtil_1130 extends BrandingBarUtil_175
{
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#isBrandingBarServiceNamePresents(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public boolean isBrandingBarServiceNamePresents(WebDriver driver, String servicename)
	{
		driver.getLogger().info("Start to check if 'service' link is existed in navigation bar.");
		boolean isExisted = false;
		isExisted = servicename.equalsIgnoreCase(driver.getText("css=" + DashBoardPageId.BRANDINGBARSERVICENAMECSS));
		driver.getLogger().info("Existence check for 'service' link is completed. Result: " + isExisted);
		return isExisted;
	}
}
