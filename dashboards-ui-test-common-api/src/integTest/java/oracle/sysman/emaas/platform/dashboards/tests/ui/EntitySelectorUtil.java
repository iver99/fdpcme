/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IEntitySelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author cawei
 */
public class EntitySelectorUtil
{

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public static int getCategories(WebDriver driver)
	//	{
	//		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
	//		return esu.getCategories(driver);
	//
	//	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public int getNumberOfPills(WebDriver driver)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		return esu.getNumberOfPills(driver);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void openEntitySelector(WebDriver driver)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.openEntitySelector(driver);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void removePill(WebDriver driver, int index)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.removePill(driver, index);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void searchText(WebDriver driver, String text)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.searchText(driver, text);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void selectCompositeEntity(WebDriver driver, String text)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.selectCompositeEntity(driver, text);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void selectEntity(WebDriver driver, String text)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.selectEntity(driver, text);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void selectFirstSuggestionByCategory(WebDriver driver, String category)
	//	{
	//		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
	//		esu.selectFirstSuggestionByCategory(driver, category);
	//	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void selectItemById(WebDriver driver, int index)
	//	{
	//		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
	//		esu.selectItemById(driver, index);
	//	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public boolean validateReadOnlyMode(WebDriver driver)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		return esu.validateReadOnlyMode(driver);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void verifyCategoryIsVisible(WebDriver driver, String text)
	//	{
	//		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
	//		esu.verifyCategoryIsVisible(driver, text);
	//	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void verifyCategoryNotVisible(WebDriver driver, List<String> categoryNames)
	//	{
	//		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
	//		esu.verifyCategoryNotVisible(driver, categoryNames);
	//	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	//	public void waitForNewPill(WebDriver driver, int prevPillCount)
	//	{
	//		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
	//		esu.waitForNewPill(driver, prevPillCount);
	//	}
}
