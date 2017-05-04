/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.timepicker.test.ui.util;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * @author shangwan
 */
public class CommonUIUtils
{
	private static WebDriver driver;

	public static void loadWebDriver(WebDriver webDriver)
	{
		driver = webDriver;
	}
}
