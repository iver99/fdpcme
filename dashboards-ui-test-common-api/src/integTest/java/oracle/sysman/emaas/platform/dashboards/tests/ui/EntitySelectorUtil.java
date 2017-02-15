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
	public void removePill(WebDriver driver, int indexOfPillToRemove)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.removePill(driver, indexOfPillToRemove);
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
	public void selectCompositeEntity(WebDriver driver, String text, String entityType)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.selectCompositeEntity(driver, text, entityType);
	}

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void selectEntity(WebDriver driver, String text, String entityType)
	{
		IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
		esu.selectEntity(driver, text, entityType);
	}

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
        
        public void clearContext(WebDriver driver)
        {
            IEntitySelectorUtil esu = new UtilLoader<IEntitySelectorUtil>().loadUtil(driver, IEntitySelectorUtil.class);
            esu.clearContext(driver);
        }

}
