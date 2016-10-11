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
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IErrorPageUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class ErrorPageUtil_171 extends ErrorPageUtil_Version implements IErrorPageUtil
{

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IErrorPageUtil#signOut(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void signOut(WebDriver driver) 
	{
		driver.getLogger().info("ErrorPageUtil click signOut button started");
		driver.waitForElementPresent("id=" + DashBoardPageId.ERRORSIGNOUTBUTTONID);
		driver.takeScreenShot();
		driver.click("id=" + DashBoardPageId.ERRORSIGNOUTBUTTONID);
		driver.takeScreenShot();
		driver.getLogger().info("ErrorUtil click signOut button completed!");
	}

}
