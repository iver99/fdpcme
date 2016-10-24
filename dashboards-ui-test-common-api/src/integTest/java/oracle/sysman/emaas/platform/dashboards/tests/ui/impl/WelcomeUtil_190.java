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
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class WelcomeUtil_190 extends WelcomeUtil_175
{

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitCompliance(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitCompliance(WebDriver driver)
	{
		driver.getLogger().info("Visiting Compliance from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_COMPLIANCEID);
		driver.click("id=" + DashBoardPageId.WELCOME_COMPLIANCEID);
		driver.takeScreenShot();

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitOrchestration(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitOrchestration(WebDriver driver)
	{
		driver.getLogger().info("Visiting Orchestration from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_ORCHESTRATIONID);
		driver.click("id=" + DashBoardPageId.WELCOME_ORCHESTRATIONID);
		driver.takeScreenShot();

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWelcomeUtil#visitSecurity(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void visitSecurity(WebDriver driver)
	{
		driver.getLogger().info("Visiting Security from Welcome Page...");
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_SECURITYANALYTICSID);
		driver.click("id=" + DashBoardPageId.WELCOME_ORCHESTRATIONID);
		driver.takeScreenShot();

	}
}
