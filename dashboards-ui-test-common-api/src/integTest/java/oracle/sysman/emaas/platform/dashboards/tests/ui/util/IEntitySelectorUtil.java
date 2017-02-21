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
	public void removePill(WebDriver driver, int indexOfPillToRemove);
        
        /**
         * 
         * @param driver
         * @param pillIndex
         * @param entityName
         * @param entityType
         * @param category 
         */
        public void replacePillSelection(WebDriver driver, int pillIndex, String entityName, String entityType, String category);

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
	public void selectCompositeEntity(WebDriver driver, String entityName, String entityType);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void selectEntity(WebDriver driver, String entityName, String entityType);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public boolean validateReadOnlyMode(WebDriver driver);
        
        /**
         * 
         * @param driver 
         */
        public void clearContext(WebDriver driver);
}
