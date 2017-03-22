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
import java.util.logging.Logger;

/**
 * @author cawei
 */
public interface IEntitySelectorUtil extends IUiTestCommonAPI
{
        public static final String CATEGORY_COMPOSITE = "Composite Entities";
	public static final String CATEGORY_ENTITIES = "Entities";
        
        /**
         * @param driver 
         * @param logger
         */
        public void clearContext(WebDriver driver, Logger logger);
        
	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public int getNumberOfPills(WebDriver driver, Logger logger);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void openEntitySelector(WebDriver driver, Logger logger);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void removePill(WebDriver driver, Logger logger, int indexOfPillToRemove);
        
        /**
         * @param driver
         * @param pillIndex
         * @param entityName
         * @param entityType
         * @param category 
         */
        public void replacePillSelection(WebDriver driver, Logger logger, int pillIndex, String entityName, String entityType, String category);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void searchText(WebDriver driver, Logger logger, String text);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void selectCompositeEntity(WebDriver driver, Logger logger, String entityName, String entityType);

	/**
	 * @param driver
	 * @param
	 * @return
	 */
	public void selectEntity(WebDriver driver, Logger logger, String entityName, String entityType);

	/**
	 * @param driver
	 * @param 
	 * @return
	 */
	public boolean validateReadOnlyMode(WebDriver driver, Logger logger);
        
        /**
         * @param driver
         * @param logger
         * @param text 
         */
        public void verifyCompositePillContent(WebDriver driver, Logger logger, String displayName);
        
        /**
         * @param driver
         * @param logger
         * @param text 
         */
        public void verifyEntityPillContent(WebDriver driver, Logger logger, String displayName);
        
        /**
         * @param driver
         * @param logger
         * @param text 
         */
        public void verifyPillContains(WebDriver driver, Logger logger, String displayName);
        
        /**
         * @param driver
         * @param logger
         * @param pillIndex
         * @param text 
         */
        public void verifyPillContentByIndex(WebDriver driver, Logger logger, int pillIndex, String displayName);
}
