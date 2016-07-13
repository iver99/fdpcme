/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public interface IErrorPageUtil extends IUiTestCommonAPI
{
	/**
	 * click error page sign out button
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void signOut(WebDriver driver) throws Exception;
}
