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

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IUiTestCommonAPI;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author miao
 */
public class WelcomeUtil_Version implements IUiTestCommonAPI
{

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IUiTestCommonAPI#getApiVersion(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getApiVersion(WebDriver wdriver)
	{
		String version = wdriver.getElement("//html").getAttribute(VERSION_ATTR);
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
