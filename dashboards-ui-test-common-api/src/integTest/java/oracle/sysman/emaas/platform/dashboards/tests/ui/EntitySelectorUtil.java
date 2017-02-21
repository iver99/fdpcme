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

	public static void clearContext(WebDriver driver)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.clearContext(driver);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public static int getNumberOfPills(WebDriver driver)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		return esu.getNumberOfPills(driver);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public static void openEntitySelector(WebDriver driver)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.openEntitySelector(driver);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public static void removePill(WebDriver driver, int indexOfPillToRemove)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.removePill(driver, indexOfPillToRemove);
	}
        
        /**
         * 
         * @param driver
         * @param pillIndex
         * @param entityName
         * @param entityType
         * @param category 
         */
        public static void replacePillSelection(WebDriver driver, int pillIndex, String entityName, String entityType, String category) {
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
                esu.replacePillSelection(driver, pillIndex, entityName, entityType, category);
        }

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public static void searchText(WebDriver driver, String text)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.searchText(driver, text);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public static void selectCompositeEntity(WebDriver driver, String entityName, String entityType)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.selectCompositeEntity(driver, entityName, entityType);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public static void selectEntity(WebDriver driver, String entityName, String entityType)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.selectEntity(driver, entityName, entityType);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public static boolean validateReadOnlyMode(WebDriver driver)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		return esu.validateReadOnlyMode(driver);
	}

}
