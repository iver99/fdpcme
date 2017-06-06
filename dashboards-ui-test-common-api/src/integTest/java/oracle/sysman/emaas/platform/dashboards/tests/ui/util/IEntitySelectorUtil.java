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

import java.util.ArrayList;
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
	 * @param logger
	 * @return 
	 */
	public int getNumberOfPills(WebDriver driver, Logger logger);
        
        /**
         * @param driver
         * @param logger
         * @return 
         */
        public ArrayList<String> getPillContents(WebDriver driver, Logger logger);

	/**
	 * @param driver
	 * @param logger
	 */
	public void openEntitySelector(WebDriver driver, Logger logger);

	/**
	 * @param driver
         * @param logger
	 * @param indexOfPillToRemove
	 */
	public void removePill(WebDriver driver, Logger logger, int indexOfPillToRemove);
        
        /**
         * @param driver
         * @param logger
         * @param pillIndex
         * @param entityName
         * @param entityType
         * @param category 
         */
        public void replacePillSelection(WebDriver driver, Logger logger, int pillIndex, String entityName, String entityType, String category);

	/**
	 * @param driver
         * @param logger
	 * @param entityName
         * @param entityType
         * @param category
	 */
	public void searchText(WebDriver driver, Logger logger, String entityName, String entityType, String category);

	/**
	 * @param driver
         * @param logger
	 * @param entityName
	 * @param entityType
	 */
	public void selectCompositeEntity(WebDriver driver, Logger logger, String entityName, String entityType);

	/**
	 * @param driver
         * @param logger
	 * @param entityName
	 * @param entityType
	 */
	public void selectEntity(WebDriver driver, Logger logger, String entityName, String entityType);

	/**
	 * @param driver
	 * @param logger
	 * @return
	 */
	public boolean validateReadOnlyMode(WebDriver driver, Logger logger);
        
        /**
         * @param driver
         * @param logger
         * @param displayName 
         */
        public void verifyCompositePillContent(WebDriver driver, Logger logger, String displayName);
        
        /**
         * @param driver
         * @param logger
         * @param displayName 
         */
        public void verifyEntityPillContent(WebDriver driver, Logger logger, String displayName);
        
        /**
         * @param driver
         * @param logger
         * @param displayName 
         */
        public void verifyPillContains(WebDriver driver, Logger logger, String displayName);
        
        /**
         * @param driver
         * @param logger
         * @param pillIndex
         * @param displayName 
         */
        public void verifyPillContentByIndex(WebDriver driver, Logger logger, int pillIndex, String displayName);
        
        /**
         * @param driver
         * @param logger
         * @param displayName
         * @return 
         */
        public boolean verifyPillExistsByDisplayName(WebDriver driver, Logger logger, String displayName);
        
        /**
         * @param driver
         * @param logger
         * @param pillIndex
         * @param displayName
         * @return 
         */
        public boolean verifyPillExistsByIndex(WebDriver driver, Logger logger, int pillIndex, String displayName);
}
