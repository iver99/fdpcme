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

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class DashboardHommeUtil_190 extends DashboardHomeUtil_175
{
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#filterOptions(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void filterOptions(WebDriver driver, String filter)
	{
		driver.getLogger().info("[DashboardHomeUtil] call filterOptions filter: " + filter);
		Validator.notEmptyString("filter", filter);
		String[] fs = filter.split(",");
		List<String> trimedFs = new ArrayList<String>();
		for (String s : fs) {
			trimedFs.add(s.trim());
		}
		if (trimedFs.contains("apm")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERAPMLOCATOR);
			driver.click(DashBoardPageId.FILTERAPMLOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("la")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERLALOCATOR);
			driver.click(DashBoardPageId.FILTERLALOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("ita")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERITALOCATOR);
			driver.click(DashBoardPageId.FILTERITALOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("orchestration")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERORCHESTRATIONLOCATOR);
			driver.click(DashBoardPageId.FILTERORCHESTRATIONLOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("oracle")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERORACLELOCATOR);
			driver.click(DashBoardPageId.FILTERORACLELOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("share")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERSHARELOCATOR);
			driver.click(DashBoardPageId.FILTERSHARELOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("me")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERMELOCATOR);
			driver.click(DashBoardPageId.FILTERMELOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("favorites")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERFAVORITELOCATOR);
			driver.click(DashBoardPageId.FILTERFAVORITELOCATOR);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
	}

	@Override
	public boolean isFilterOptionSelected(WebDriver driver, String filter)
	{
		driver.getLogger().info("[DashboardHomeUtil] call isFilterOptionSelected filter: " + filter);
		Validator.notEmptyString("filter", filter);
		if ("orchestration".equals(filter)) {
			return driver.getElement(DashBoardPageId.FILTERORCHESTRATIONLOCATOR).isSelected();
		}
		else {
			return super.isFilterOptionSelected(driver, filter);
		}

	}
}
