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
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1130;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardHomeUtil_1130 extends DashboardHomeUtil_175
{
	@Override
	public void closeOverviewPage(WebDriver driver)
	{

		driver.getLogger().info("This API is deprecated");

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#filterOptions(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#gotoDataExplorer(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void gotoDataExplorer(WebDriver driver, String option)
	{
		driver.getLogger().info("[DashboardHomeUtil] call exploreData -> " + option);

		Validator.notEmptyString("option", option);

		driver.click(convertName(DashBoardPageId.EXPLOREDATABTNID));
		//WebElement menu = driver.getElement(convertName(DashBoardPageId.EXPLOREDATAMENU));

		if (IDashboardHomeUtil.EXPLOREDATA_MENU_LOG.equals(option)) {
			driver.click(DashBoardPageId_1130.EXPLORE_LOG);
		}
		else {
			driver.click(DashBoardPageId.EXPLORE_Search);
		}

	}

}
