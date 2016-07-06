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

/**
 * @author miao
 */
public interface IUiTestCommonAPI
{
	/**
	 * The html element attribute to read api version
	 */
	public static final String VERSION_ATTR = "data-testapiversion";

	/**
	 * Detect the API version to call at run-time, different API should have different way to determine version
	 * <p>
	 * version in html element attribute is in format: x.y.z, the api should return xyz (remove .) e.g.
	 * <p>
	 * According to <html lang="en" data-testapiversion="1.7.5"> , this api should return 175
	 *
	 * @param wdriver
	 * @return
	 */
	public String getApiVersion(WebDriver wdriver);
}
