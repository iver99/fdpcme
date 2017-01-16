/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public interface IEntitySelectorUtil extends IUiTestCommonAPI
{
	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public int getCategories(WebDriver driver);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public int getNumberOfPills(WebDriver driver);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void openEntitySelector(WebDriver driver);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void removePill(WebDriver driver, int index);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void searchText(WebDriver driver, String text);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void selectCompositeEntity(WebDriver driver, String text);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void selectEntity(WebDriver driver, String text);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void selectFirstSuggestionByCategory(WebDriver driver, String category);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void selectItemById(WebDriver driver, int index);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public boolean validateReadOnlyMode(WebDriver driver);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void verifyCategoryIsVisible(WebDriver driver, String text);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void verifyCategoryNotVisible(WebDriver driver, List<String> categoryNames);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void waitForNewPill(WebDriver driver, int prevPillCount);

}
