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

import org.openqa.selenium.WebElement;

public class DashboardHomeUtil_1170 extends DashboardHomeUtil_1150
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

		}
		if (trimedFs.contains("la")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERLALOCATOR);
			driver.click(DashBoardPageId.FILTERLALOCATOR);

		}
		if (trimedFs.contains("ita")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERITALOCATOR);
			driver.click(DashBoardPageId.FILTERITALOCATOR);

		}
		if (trimedFs.contains("orchestration")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERORCHESTRATIONLOCATOR);
			driver.click(DashBoardPageId.FILTERORCHESTRATIONLOCATOR);

		}
		if (trimedFs.contains("security")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERSECURITYLOCATOR);
			driver.click(DashBoardPageId.FILTERSECURITYLOCATOR);

		}
		if (trimedFs.contains("oracle")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERORACLELOCATOR);
			driver.click(DashBoardPageId.FILTERORACLELOCATOR);

		}
		if (trimedFs.contains("share")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERSHARELOCATOR);
			driver.click(DashBoardPageId.FILTERSHARELOCATOR);

		}
		if (trimedFs.contains("me")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERMELOCATOR);
			driver.click(DashBoardPageId.FILTERMELOCATOR);

		}
		if (trimedFs.contains("favorites")) {
			driver.waitForElementPresent(DashBoardPageId.FILTERFAVORITELOCATOR);
			driver.click(DashBoardPageId.FILTERFAVORITELOCATOR);

		}

	}

	@Override
	public boolean isFilterOptionSelected(WebDriver driver, String filter)
	{
		driver.getLogger().info("[DashboardHomeUtil] call isFilterOptionSelected filter: " + filter);
		Validator.notEmptyString("filter", filter);
		if ("security".equals(filter)) {
			return driver.getElement(DashBoardPageId.FILTERSECURITYLOCATOR).isSelected();
		}
		else {
			return super.isFilterOptionSelected(driver, filter);
		}

	}

	@Override
	public void resetFilterOptions(WebDriver driver)
	{

		driver.getLogger().info("[DashboardHomeUtil] call resetFilterOptions");
		driver.waitForElementPresent(DashBoardPageId.FILTERAPMLOCATOR);
		WebElement el = driver.getElement(DashBoardPageId.FILTERAPMLOCATOR);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FILTERLALOCATOR);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FILTERITALOCATOR);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FILTERORCHESTRATIONLOCATOR);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FILTERSECURITYLOCATOR);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FILTERORACLELOCATOR);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FILTERSHARELOCATOR);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FILTERMELOCATOR);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FILTERFAVORITELOCATOR);
		if (el.isSelected()) {
			el.click();
		}
		WaitUtil.waitForPageFullyLoaded(driver);
	}
}
