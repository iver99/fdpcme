package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.ArrayList;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.NoSuchElementException;

public class DashboardHomeUtil
{
	public static final String DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC = "access_date_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC = "access_date_dsc";
	public static final String DASHBOARD_QUERY_ORDER_BY_NAME_ASC = "name_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_NAME_DSC = "name_dsc";
	public static final String DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC = "creation_date_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC = "creation_date_dsc";
	public static final String DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC = "last_modification_date_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC = "last_modification_date_dsc";
	public static final String DASHBOARD_QUERY_ORDER_BY_OWNER_ASC = "owner_asc";
	public static final String DASHBOARD_QUERY_ORDER_BY_OWNER_DSC = "owner_dsc";

	private static WebDriver driver;

	public static void createDashboard(String name, String descriptions, Boolean displayDesc, Boolean selectorRefreshcontrol)
			throws Exception
	{

	}

	public static void createDashboardSet(String name, String descriptions, Boolean displayDesc, Boolean selectorRefreshcontrol)
			throws Exception
	{

	}

	public static void delete(String dashboardName, String view) throws Exception
	{

	}

	public static void exploreData(String option) throws Exception
	{

	}

	public static void filterOptions(String filter) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call filterOptions");
		if (filter == null) {
			return;
		}
		String[] fs = filter.split(",");
		ArrayList<String> trimedFs = new ArrayList<String>();
		for (String s : fs) {
			trimedFs.add(s.trim());
		}
		if (trimedFs.contains("apm")) {
			driver.waitForElementPresent(DashBoardPageId.FilterApmLocator);
			driver.click(DashBoardPageId.FilterApmLocator);
		}
		if (trimedFs.contains("la")) {
			driver.waitForElementPresent(DashBoardPageId.FilterLaLocator);
			driver.click(DashBoardPageId.FilterLaLocator);
		}
		if (trimedFs.contains("ita")) {
			driver.waitForElementPresent(DashBoardPageId.FilterItaLocator);
			driver.click(DashBoardPageId.FilterItaLocator);
		}
		if (trimedFs.contains("oracle")) {
			driver.waitForElementPresent(DashBoardPageId.FilterOracleLocator);
			driver.click(DashBoardPageId.FilterOracleLocator);
		}
		if (trimedFs.contains("share")) {
			driver.waitForElementPresent(DashBoardPageId.FilterShareLocator);
			driver.click(DashBoardPageId.FilterShareLocator);
		}
		if (trimedFs.contains("me")) {
			driver.waitForElementPresent(DashBoardPageId.FilterMeLocator);
			driver.click(DashBoardPageId.FilterMeLocator);
		}
		if (trimedFs.contains("favorites")) {
			driver.waitForElementPresent(DashBoardPageId.FilterFavoriteLocator);
			driver.click(DashBoardPageId.FilterFavoriteLocator);
		}
	}

	public static void gridView() throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call gridView");
		driver.waitForElementPresent(DashBoardPageId.DashboardsGridViewLocator);
		driver.click(DashBoardPageId.DashboardsGridViewLocator);
	}

	public static void listView() throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call listView");
		driver.waitForElementPresent(DashBoardPageId.DashboardsListViewLocator);
		driver.click(DashBoardPageId.DashboardsListViewLocator);
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void search(String searchString) throws Exception
	{
		if (searchString == null) {
			return;
		}
		driver.getLogger().info("[DashboardHomeUtil] call search");
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);
		driver.getElement(DashBoardPageId.SearchDashboardInputLocator).clear();
		driver.click(DashBoardPageId.SearchDashboardInputLocator);
		driver.sendKeys(DashBoardPageId.SearchDashboardInputLocator, searchString);
	}

	public static void selectDashboard(String dashboardName) throws Exception
	{
		String indicator = DashBoardPageId.DashboardLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			throw new NoSuchElementException("Dashboard not exists. Name: " + dashboardName);
		}
		driver.click(indicator);
	}

	public static void selectOOB(String dashboardName) throws Exception
	{
		String indicator = DashBoardPageId.OOBDashboardLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			throw new NoSuchElementException("Dashboard not exists. Name: " + dashboardName);
		}
		driver.click(indicator);
	}

	public static void sortBy(String option) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call listView");
		driver.waitForElementPresent(DashBoardPageId.SortBySelectLocator);
		driver.click(DashBoardPageId.SortBySelectLocator);

		if ("default".equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByDefaultLocator);
			driver.click(DashBoardPageId.SortByDefaultLocator);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByNameASCLocator);
			driver.click(DashBoardPageId.SortByNameASCLocator);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByNameDSCLocator);
			driver.click(DashBoardPageId.SortByNameDSCLocator);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreatedByASCLocator);
			driver.click(DashBoardPageId.SortByCreatedByASCLocator);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreatedByDSCLocator);
			driver.click(DashBoardPageId.SortByCreatedByDSCLocator);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreateDateASCLocator);
			driver.click(DashBoardPageId.SortByCreateDateASCLocator);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreateDateDSCLocator);
			driver.click(DashBoardPageId.SortByCreateDateDSCLocator);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastModifiedASCLocator);
			driver.click(DashBoardPageId.SortByLastModifiedASCLocator);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastModifiedDSCLocator);
			driver.click(DashBoardPageId.SortByLastModifiedDSCLocator);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastAccessASCLocator);
			driver.click(DashBoardPageId.SortByLastAccessASCLocator);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastAccessDSCLocator);
			driver.click(DashBoardPageId.SortByLastAccessDSCLocator);
		}
		else {
			throw new IllegalArgumentException("Unknow Sort by option: " + option);
		}

	}

	public static void waitForMilliSeconds(long millisSec) throws Exception
	{
		Thread.sleep(millisSec);
	}

}
