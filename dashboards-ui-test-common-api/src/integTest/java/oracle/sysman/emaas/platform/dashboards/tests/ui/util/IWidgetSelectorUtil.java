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

public interface IWidgetSelectorUtil extends IUiTestCommonAPI
{
	public void addWidget(WebDriver driver, String widgetName) throws Exception;

	public void page(WebDriver driver, int pageNo) throws Exception;

	public void pagingNext(WebDriver driver) throws Exception;

	public void pagingPrevious(WebDriver driver) throws Exception;
}
