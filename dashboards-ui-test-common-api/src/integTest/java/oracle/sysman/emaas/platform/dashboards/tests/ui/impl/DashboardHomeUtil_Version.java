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


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IUiTestCommonAPI;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author miao
 */
public class DashboardHomeUtil_Version implements IUiTestCommonAPI
{
	private static final Logger logger = LogManager.getLogger(DashboardHomeUtil_Version.class);

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IUiTestCommonAPI#getApiVersion(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getApiVersion(WebDriver wdriver)
	{
                //since 1.9.0, we use new xpath to get verison of home.html
		String version = null;
                try{
                    version = wdriver.getElement("//div[contains(@data-bind,'df-oracle-dashboard-list')]//div[boolean(@data-testapiversion)]").getAttribute(VERSION_ATTR);
                }catch(Exception e){
                	logger.info("context",e);
                }
                //compatible with 1.7.5
                if (version == null || "".equals(version.trim())) {
                      version = wdriver.getElement("//html").getAttribute(VERSION_ATTR);
                }
		if (version == null || "".equals(version.trim())) {
			//1.7.1 or earlier
			return "171";
		}
		else {
			version = version.replace(".", "");
		}
		return version;
	}
}
