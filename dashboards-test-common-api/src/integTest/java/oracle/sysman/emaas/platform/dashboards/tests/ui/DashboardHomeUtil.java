package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

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

	public static void createDashboard(WebDriver driver, String name, String descriptions, Boolean displayDesc,
			Boolean selectorRefreshcontrol) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call createDashboard : " + name);
		driver.click(DashboardHomeUtil.convertID(DashBoardPageId.CreateDSButtonID));

		if (name != null && !name.isEmpty()) {
			driver.sendKeys(DashboardHomeUtil.convertID(DashBoardPageId.DashBoardNameBoxID), name);
		}
		if (descriptions != null && !descriptions.isEmpty()) {
			driver.sendKeys(DashboardHomeUtil.convertID(DashBoardPageId.DashBoardDescBoxID), descriptions);
		}
		if (selectorRefreshcontrol) {
			driver.check(DashboardHomeUtil.convertID(DashBoardPageId.DashBoardTimeRangeChecker));
		}
		else {
			driver.uncheck(DashboardHomeUtil.convertID(DashBoardPageId.DashBoardTimeRangeChecker));
		}

		driver.takeScreenShot();
		driver.click(DashboardHomeUtil.convertID(DashBoardPageId.DashOKButtonID));
	}

	public static void createDashboardSet(WebDriver driver, String name, String descriptions, Boolean displayDesc,
			Boolean selectorRefreshcontrol) throws Exception
	{

	}

	/**
	 * @param dashboardName
	 * @param view
	 *            DashboardsGridViewLocator | DashboardsListViewLocator
	 * @throws Exception
	 */
	public static void delete(WebDriver driver, String dashboardName, String view) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call delete dashboardName : " + dashboardName);
		if (dashboardName == null || dashboardName.isEmpty()) {
			return;
		}
		DashboardHomeUtil.search(driver, dashboardName);
		if (DashBoardPageId.DashboardsGridViewLocator.equals(view)) {
			DashboardHomeUtil.gridView(driver);
			driver.takeScreenShot();
			DashboardHomeUtil.deleteDashboardInGrid(driver, dashboardName);
		}

		if (DashBoardPageId.DashboardsListViewLocator.equals(view)) {
			DashboardHomeUtil.listView(driver);
			driver.takeScreenShot();
			DashboardHomeUtil.deleteDashboardInList(driver, dashboardName);
		}
		driver.takeScreenShot();
	}

	/**
	 * @param option
	 *            ExploreDataMenu_Analyze | ExploreDataMenu_Log | ExploreDataMenu_Search
	 * @throws Exception
	 */
	public static void exploreData(WebDriver driver, String option) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call exploreData -> " + option);
		
		if(option == null || option.isEmpty()) {
			return;
		}
		
		driver.click(DashboardHomeUtil.convertID(DashBoardPageId.ExploreDataBtnID));
		WebElement menu = driver.getElement(DashboardHomeUtil.convertID(DashBoardPageId.ExploreDataMenu));
		List<WebElement> menuList = menu.findElements(By.tagName("li"));
		for(WebElement menuItem : menuList) {
			if(option.equals(menuItem.getText())) {
				menuItem.click();
				break;
			}
		}
	}

	public static void filterOptions(WebDriver driver, String filter) throws Exception
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

	public static void gridView(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call gridView");
		driver.waitForElementPresent(DashBoardPageId.DashboardsGridViewLocator);
		driver.takeScreenShot();
		driver.click(DashBoardPageId.DashboardsGridViewLocator);
	}

	public static void listView(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call listView");
		driver.waitForElementPresent(DashBoardPageId.DashboardsListViewLocator);
		driver.takeScreenShot();
		driver.click(DashBoardPageId.DashboardsListViewLocator);
	}

	public static void resetFilterOptions(WebDriver driver) throws Exception
	{
		driver.waitForElementPresent(DashBoardPageId.FilterApmLocator);
		WebElement el = driver.getElement(DashBoardPageId.FilterApmLocator);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FilterLaLocator);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FilterItaLocator);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FilterOracleLocator);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FilterShareLocator);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FilterMeLocator);
		if (el.isSelected()) {
			el.click();
		}
		el = driver.getElement(DashBoardPageId.FilterFavoriteLocator);
		if (el.isSelected()) {
			el.click();
		}
	}

	public static void search(WebDriver driver, String searchString) throws Exception
	{
		if (searchString == null) {
			return;
		}
		driver.getLogger().info("[DashboardHomeUtil] call search");
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);
		driver.getElement(DashBoardPageId.SearchDashboardInputLocator).clear();
		driver.click(DashBoardPageId.SearchDashboardInputLocator);
		driver.sendKeys(DashBoardPageId.SearchDashboardInputLocator, searchString);
		driver.click(DashBoardPageId.SearchDashboardSearchBtnLocator);
	}

	public static void selectDashboard(WebDriver driver, String dashboardName) throws Exception
	{
		String indicator = DashBoardPageId.DashboardLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			throw new NoSuchElementException("Dashboard not exists. Name: " + dashboardName);
		}
		driver.click(indicator);
	}

	public static void selectOOB(WebDriver driver, String dashboardName) throws Exception
	{
		String indicator = DashBoardPageId.OOBDashboardLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			throw new NoSuchElementException("Dashboard not exists. Name: " + dashboardName);
		}
		driver.click(indicator);
	}

	public static void sortBy(WebDriver driver, String option) throws Exception
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

	private static String convertID(String id)
	{
		return "id=" + id;
	}

	private static void deleteDashboardInGrid(WebDriver driver, String dashboardName)
	{
		WebElement gridTable = driver.getElement(DashboardHomeUtil.convertID(DashBoardPageId.DashboardTableID));
		List<WebElement> dashboardList = gridTable.findElements(By.tagName("div"));
		for (WebElement dashboard : dashboardList) {
			if (dashboardName.equals(dashboard.getAttribute("aria-label"))) {
				dashboard.findElement(By.cssSelector("button")).click(); // click "i" button
				driver.click(DashBoardPageId.DASHBOARD_HOME_DELETE_BUTTON); // click delete
				driver.click(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM); // confirm to delete
				break;
			}
		}
	}

	private static void deleteDashboardInList(WebDriver driver, String dashboardName)
	{
		// find table
		WebElement listTable = driver.getElement(DashboardHomeUtil.convertID(DashBoardPageId.DASHBOARD_LIST_TABLE));
		// find the column index of both "Name" & button
		WebElement headRow = listTable.findElement(By.tagName("thead")).findElement(By.tagName("tr"));
		List<WebElement> headColList = headRow.findElements(By.tagName("th"));
		int buttonColIndex = headColList.size() - 1;
		int nameColIndex = 0;
		for (int i = 0; i < headColList.size(); i++) {
			WebElement headCol = headColList.get(i);
			if ("Name".equals(headCol.getAttribute("title"))) {
				nameColIndex = i;
				break;
			}
		}
		// find the row whose name is equal with dashboardName and click the delete
		List<WebElement> rowList = listTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
		for (WebElement row : rowList) {
			List<WebElement> rowColList = row.findElements(By.tagName("td"));
			if (dashboardName.equals(rowColList.get(nameColIndex).getText())) {
				rowColList.get(buttonColIndex).findElement(By.tagName("button")).click(); // click "i" button
				driver.click(DashBoardPageId.DASHBOARD_HOME_DELETE_BUTTON); // click delete
				driver.click(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM); // confirm to delete
				break;
			}
		}
	}
}
