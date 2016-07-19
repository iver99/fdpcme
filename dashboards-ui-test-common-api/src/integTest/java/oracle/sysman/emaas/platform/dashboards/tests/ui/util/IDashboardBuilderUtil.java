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

public interface IDashboardBuilderUtil extends IUiTestCommonAPI
{
	public static final String REFRESH_DASHBOARD_SETTINGS_OFF = "OFF";
	public static final String REFRESH_DASHBOARD_SETTINGS_5MIN = "On (Every 5 Minutes)";
	public static final String TILE_WIDER = "wider";
	public static final String TILE_NARROWER = "narrower";
	public static final String TILE_TALLER = "taller";
	public static final String TILE_SHORTER = "shorter";

	public void addNewDashboardToSet(WebDriver driver, String dashboardName) throws Exception;

	public void addWidgetToDashboard(WebDriver driver, String searchString) throws Exception;

    public void createDashboardInsideSet(WebDriver driver, String name, String descriptions) throws Exception;

	public void deleteDashboard(WebDriver driver);

	public void deleteDashboardSet(WebDriver driver);

	public void duplicateDashboard(WebDriver driver, String name, String descriptions) throws Exception;

    public void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions, boolean addToSet) throws Exception;

    public void editDashboard(WebDriver driver, String name, String descriptions) throws Exception;

	public void editDashboard(WebDriver driver, String name, String descriptions, Boolean toShowDscptn) throws Exception;

    public void editDashboardSet(WebDriver driver, String name, String descriptions) throws Exception;

	public Boolean favoriteOption(WebDriver driver) throws Exception;

	public Boolean favoriteOptionDashboardSet(WebDriver driver) throws Exception;

	public void gridView(WebDriver driver) throws Exception;

	public boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings) throws Exception;

	public boolean isRefreshSettingCheckedForDashbaordSet(WebDriver driver, String refreshSettings);

	//	public void loadWebDriverOnly(WebDriver webDriver) throws Exception
	//	{
	//		driver = webDriver;
	//	}

	public void listView(WebDriver driver) throws Exception;

	public void openWidget(WebDriver driver, String widgetName) throws Exception;

	public void openWidget(WebDriver driver, String widgetName, int index) throws Exception;

	public void printDashboard(WebDriver driver) throws Exception;

	public void printDashboardSet(WebDriver driver) throws Exception;

	public void refreshDashboard(WebDriver driver, String refreshSettings);

	public void refreshDashboardSet(WebDriver driver, String refreshSettings);

	public void removeWidget(WebDriver driver, String widgetName) throws Exception;

	public void removeWidget(WebDriver driver, String widgetName, int index) throws Exception;

    public void removeDashboardFromSet(WebDriver driver, String dashboardName) throws Exception;

    public void resizeWidget(WebDriver driver, String widgetName, int index, String resizeOptions) throws Exception;

	public void resizeWidget(WebDriver driver, String widgetName, String resizeOptions) throws Exception;

	public void saveDashboard(WebDriver driver) throws Exception;

	public void search(WebDriver driver, String searchString) throws Exception;

	public void selectDashboard(WebDriver driver, String dashboardName) throws Exception;

    public void selectDashboardInsideSet(WebDriver driver,String dashboardName) throws Exception;

    public void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility) throws Exception;

	public void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility) throws Exception;

	/**
	 * sort dashboards
	 *
	 * @param driver
	 * @param option
	 *            sort by - default, access_date_asc, access_date_dsc, name_asc, name_dsc, creation_date_asc, creation_date_dsc,
	 *            last_modification_date_asc, last_modification_date_dsc, owner_asc, owner_dsc
	 * @throws Exception
	 */
	public void sortBy(WebDriver driver, String option) throws Exception;

	public Boolean toggleHome(WebDriver driver) throws Exception;

	public Boolean toggleHomeDashboardSet(WebDriver driver) throws Exception;

	public Boolean toggleShareDashboard(WebDriver driver) throws Exception;

	public Boolean toggleShareDashboardset(WebDriver driver) throws Exception;

	public boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector);

	public boolean verifyDashboardInsideSet(WebDriver driver, String dashboardName);

	public boolean verifyDashboardSet(WebDriver driver, String dashboardSetName);

	public boolean verifyWidget(WebDriver driver, String widgetName);

	public boolean verifyWidget(WebDriver driver, String widgetName, int index);
	
	public void setEntitySupport(WebDriver driver, String mode) throws Exception;

	public boolean showEntityFilter(WebDriver driver, boolean showEntityFilter) throws Exception;
	
	public boolean showTimeRangeFilter(WebDriver driver, boolean showTimeRangeFilter) throws Exception;
	

}
