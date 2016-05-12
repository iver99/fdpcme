package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardHomeUtil
{
	public static final String DASHBOARD = "dashboard";
	public static final String DASHBOARDSET = "dashboardSet";
	public static final String EXPLOREDATA_MENU_ANALYZE = "Analyze";
	public static final String EXPLOREDATA_MENU_LOG = "Log Visual Analyzer";
	public static final String EXPLOREDATA_MENU_SEARCH = "Search";
	public static final String DASHBOARDS_GRID_VIEW = "dashboards_grid_view";
	public static final String DASHBOARDS_LIST_VIEW = "dashboards_list_view";
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

	public static void closeOverviewPage(WebDriver driver) throws Exception
	{
		if (driver.isDisplayed(DashBoardPageId.OverviewCloseID)) {
			driver.getLogger().info("before clicking overview button");
			driver.click(DashBoardPageId.OverviewCloseID);
		}
	}

	/**
	 * Create one Dashboard or Dashbaord Set
	 *
	 * @param driver
	 * @param name
	 *            dashboard name
	 * @param descriptions
	 *            dashboard description(optional)
	 * @param type
	 *            dashboard | dashboardSet
	 * @throws Exception
	 */
	public static void createDashboard(WebDriver driver, String name, String descriptions, String type)
			throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call createDashboard : " + name);
		driver.click(DashboardHomeUtil.convertID(DashBoardPageId.CreateDSButtonID));

		if (name != null && !name.isEmpty()) {
			driver.sendKeys(DashboardHomeUtil.convertID(DashBoardPageId.DashBoardNameBoxID), name);
		}
		if (descriptions != null && !descriptions.isEmpty()) {
			driver.sendKeys(DashboardHomeUtil.convertID(DashBoardPageId.DashBoardDescBoxID), descriptions);
		}
		if (DashboardHomeUtil.DASHBOARD.equalsIgnoreCase(type)) {
			driver.check(DashboardHomeUtil.convertID(DashBoardPageId.DashBoardType_Single));
		} else if (DashboardHomeUtil.DASHBOARDSET.equalsIgnoreCase(type)) {
			driver.check(DashboardHomeUtil.convertID(DashBoardPageId.DashBoardType_Set));
		}
		driver.takeScreenShot();
		driver.click(DashboardHomeUtil.convertID(DashBoardPageId.DashOKButtonID));
	}

	/**
	 * Delete one dashboard by name
	 *
	 * @param dashboardName
	 *            dashboard name
	 * @param view
	 *            dashboards_grid_view | dashboards_list_view
	 * @throws Exception
	 */
	public static void deleteDashboard(WebDriver driver, String dashboardName, String view) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call delete dashboardName : " + dashboardName);
		Validator.notEmptyString("dashboardName", dashboardName);
		DashboardHomeUtil.search(driver, dashboardName);
		if (DashboardHomeUtil.DASHBOARDS_GRID_VIEW.equals(view)) {
			DashboardHomeUtil.gridView(driver);
			driver.takeScreenShot();
			DashboardHomeUtil.deleteDashboardInGrid(driver, dashboardName);
		}

		if (DashboardHomeUtil.DASHBOARDS_LIST_VIEW.equals(view)) {
			DashboardHomeUtil.listView(driver);
			driver.takeScreenShot();
			DashboardHomeUtil.deleteDashboardInList(driver, dashboardName);
		}
		driver.takeScreenShot();
	}

	/**
	 * add filter
	 *
	 * @param driver
	 * @param filter
	 *            filter name - apm,la,ita,oracle,share,me,favorites(multiple choice and split with comma)
	 * @throws Exception
	 */
	public static void filterOptions(WebDriver driver, String filter) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call filterOptions filter: " + filter);
		Validator.notEmptyString("filter", filter);
		String[] fs = filter.split(",");
		ArrayList<String> trimedFs = new ArrayList<String>();
		for (String s : fs) {
			trimedFs.add(s.trim());
		}
		if (trimedFs.contains("apm")) {
			driver.waitForElementPresent(DashBoardPageId.FilterApmLocator);
			driver.click(DashBoardPageId.FilterApmLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("la")) {
			driver.waitForElementPresent(DashBoardPageId.FilterLaLocator);
			driver.click(DashBoardPageId.FilterLaLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("ita")) {
			driver.waitForElementPresent(DashBoardPageId.FilterItaLocator);
			driver.click(DashBoardPageId.FilterItaLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("oracle")) {
			driver.waitForElementPresent(DashBoardPageId.FilterOracleLocator);
			driver.click(DashBoardPageId.FilterOracleLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("share")) {
			driver.waitForElementPresent(DashBoardPageId.FilterShareLocator);
			driver.click(DashBoardPageId.FilterShareLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("me")) {
			driver.waitForElementPresent(DashBoardPageId.FilterMeLocator);
			driver.click(DashBoardPageId.FilterMeLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		if (trimedFs.contains("favorites")) {
			driver.waitForElementPresent(DashBoardPageId.FilterFavoriteLocator);
			driver.click(DashBoardPageId.FilterFavoriteLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
	}

	//	public static void createDashboardSet(WebDriver driver, String name, String descriptions, Boolean displayDesc,
	//			Boolean selectorRefreshcontrol) throws Exception
	//	{
	//
	//	}

	/**
	 * goto the link in Data Explorer by displayed name
	 *
	 * @param option
	 *            Analyze | Log Visual Analyzer | Search
	 * @throws Exception
	 */
	public static void gotoDataExplorer(WebDriver driver, String option) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call exploreData -> " + option);

		Validator.notEmptyString("option", option);

		driver.click(DashboardHomeUtil.convertName(DashBoardPageId.ExploreDataBtnID));
		WebElement menu = driver.getElement(DashboardHomeUtil.convertName(DashBoardPageId.ExploreDataMenu));
		List<WebElement> menuList = menu.findElements(By.tagName("li"));
		for (WebElement menuItem : menuList) {
			if (option.equals(menuItem.getText())) {
				menuItem.click();
				break;
			}
		}
	}

	/**
	 * choose grid view
	 *
	 * @param driver
	 * @throws Exception
	 */
	public static void gridView(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call gridView");
		driver.waitForElementPresent(DashBoardPageId.DashboardsGridViewLocator);
		driver.takeScreenShot();
		driver.click(DashBoardPageId.DashboardsGridViewLocator);
	}

	/**
	 * check if the dashboard is existing or not by name
	 *
	 * @param driver
	 * @param dashboardName
	 * @return
	 * @throws Exception
	 */
	public static boolean isDashboardExisted(WebDriver driver, String dashboardName) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call isDashboardExists dashboardName: " + dashboardName);
		Validator.notEmptyString("dashboardName", dashboardName);
		String indicator = DashBoardPageId.DashboardNameLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			return false;
		}
		return true;
	}

	/**
	 * check if the filter is selected by filter name
	 *
	 * @param driver
	 * @param filter
	 *            filer name - apm, la , ita, oracle, share, me favorites(single choice)
	 * @return
	 * @throws Exception
	 */
	public static boolean isFilterOptionSelected(WebDriver driver, String filter) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call isFilterOptionSelected filter: " + filter);
		Validator.notEmptyString("filter", filter);
		if ("apm".equals(filter)) {
			return driver.getElement(DashBoardPageId.FilterApmLocator).isSelected();
		}
		else if ("la".equals(filter)) {
			return driver.getElement(DashBoardPageId.FilterLaLocator).isSelected();
		}
		else if ("ita".equals(filter)) {
			return driver.getElement(DashBoardPageId.FilterItaLocator).isSelected();
		}
		else if ("oracle".equals(filter)) {
			return driver.getElement(DashBoardPageId.FilterOracleLocator).isSelected();
		}
		else if ("share".equals(filter)) {
			return driver.getElement(DashBoardPageId.FilterShareLocator).isSelected();
		}
		else if ("me".equals(filter)) {
			return driver.getElement(DashBoardPageId.FilterMeLocator).isSelected();
		}
		else if ("favorites".equals(filter)) {
			return driver.getElement(DashBoardPageId.FilterFavoriteLocator).isSelected();
		}
		else {
			throw new IllegalArgumentException("Unkonw filter option: " + filter);
		}
	}

	public static List<String> listDashboardNames(WebDriver driver) throws Exception
	{
		List<String> names = new ArrayList<String>();
		List<WebElement> eles = driver.getWebDriver().findElements(By.xpath(DashBoardPageId.DashboardNameContainers));
		for (WebElement e : eles) {
			//String name = e.getAttribute("aria-label");
			if (e.getAttribute("aria-label") != null) {
				names.add(e.getAttribute("aria-label"));
			}
			else {
				names.add(e.getText());
			}
		}
		return names;
	}

	/**
	 * choose the list view
	 *
	 * @param driver
	 * @throws Exception
	 */
	public static void listView(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call listView");
		driver.waitForElementPresent(DashBoardPageId.DashboardsListViewLocator);
		driver.takeScreenShot();
		driver.click(DashBoardPageId.DashboardsListViewLocator);
	}

	/**
	 * reset the filters
	 *
	 * @param driver
	 * @throws Exception
	 */
	public static void resetFilterOptions(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call resetFilterOptions");
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
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/**
	 * search dashboard
	 *
	 * @param driver
	 * @param searchString
	 * @throws Exception
	 */
	public static void search(WebDriver driver, String searchString) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call search searchString: " + searchString);
		Validator.notEmptyString("searchString", searchString);
		driver.getLogger().info("[DashboardHomeUtil] call search");
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);
		driver.getElement(DashBoardPageId.SearchDashboardInputLocator).clear();
		driver.click(DashBoardPageId.SearchDashboardInputLocator);
		driver.sendKeys(DashBoardPageId.SearchDashboardInputLocator, searchString);
		driver.click(DashBoardPageId.SearchDashboardSearchBtnLocator);
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/**
	 * Select an any-type dashboard by name, including OOB dashboard & user created dashboard
	 *
	 * @param driver
	 * @param dashboardName
	 * @throws Exception
	 */
	public static void selectDashboard(WebDriver driver, String dashboardName) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call selectDashboard dashboardName: " + dashboardName);
		String indicator = DashBoardPageId.DashboardNameLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			throw new NoSuchElementException("Dashboard not exists. Name: " + dashboardName);
		}
		driver.click(indicator);
	}

	/**
	 * Select an OOB dashboard by name
	 *
	 * @param driver
	 * @param dashboardName
	 * @throws Exception
	 */
	public static void selectOOB(WebDriver driver, String dashboardName) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call selectOOB dashboardName: " + dashboardName);
		Validator.notEmptyString("dashboardName", dashboardName);
		String indicator = DashBoardPageId.OOBDashboardNameLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			throw new NoSuchElementException("Dashboard not exists. Name: " + dashboardName);
		}
		driver.click(indicator);
	}

	/**
	 * sort dashboards
	 *
	 * @param driver
	 * @param option
	 *            sort by - default, access_date_asc, access_date_dsc, name_asc, name_dsc, creation_date_asc, creation_date_dsc,
	 *            last_modification_date_asc, last_modification_date_dsc, owner_asc, owner_dsc
	 * @throws Exception
	 */
	public static void sortBy(WebDriver driver, String option) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call sortBy option: " + option);
		Validator.notEmptyString("option", option);
		driver.waitForElementPresent(DashBoardPageId.SortBySelectLocator);
		driver.click(DashBoardPageId.SortBySelectLocator);

		if ("default".equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByDefaultLocator);
			driver.click(DashBoardPageId.SortByDefaultLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByNameASCLocator);
			driver.click(DashBoardPageId.SortByNameASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_NAME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByNameDSCLocator);
			driver.click(DashBoardPageId.SortByNameDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreatedByASCLocator);
			driver.click(DashBoardPageId.SortByCreatedByASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_OWNER_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreatedByDSCLocator);
			driver.click(DashBoardPageId.SortByCreatedByDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreateDateASCLocator);
			driver.click(DashBoardPageId.SortByCreateDateASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreateDateDSCLocator);
			driver.click(DashBoardPageId.SortByCreateDateDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastModifiedASCLocator);
			driver.click(DashBoardPageId.SortByLastModifiedASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastModifiedDSCLocator);
			driver.click(DashBoardPageId.SortByLastModifiedDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastAccessASCLocator);
			driver.click(DashBoardPageId.SortByLastAccessASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DashboardHomeUtil.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastAccessDSCLocator);
			driver.click(DashBoardPageId.SortByLastAccessDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else {
			throw new IllegalArgumentException("Unknow Sort by option: " + option);
		}

	}

	/**
	 * @param driver
	 * @throws Exception
	 */
	public static void sortListViewByCreateBy(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call clickListViewTableCreatedByHeader");
		driver.waitForElementPresent(DashBoardPageId.ListViewTableCreatedByHeaderLocator);
		driver.click(DashBoardPageId.ListViewTableCreatedByHeaderLocator);
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/**
	 * @param driver
	 * @throws Exception
	 */
	public static void sortListViewByLastModified(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call clickListViewTableLastModifiedHeader");
		driver.waitForElementPresent(DashBoardPageId.ListViewTableLastModifiedHeaderLocator);
		driver.click(DashBoardPageId.ListViewTableLastModifiedHeaderLocator);
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/**
	 * @param driver
	 * @throws Exception
	 */
	public static void sortListViewByName(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call clickListViewTableNameHeader");
		driver.waitForElementPresent(DashBoardPageId.ListViewTableNameHeaderLocator);
		driver.click(DashBoardPageId.ListViewTableNameHeaderLocator);
		WaitUtil.waitForPageFullyLoaded(driver);
	}

	/**
	 * wait the dashboard by name
	 *
	 * @param driver
	 * @param dashboardName
	 * @throws Exception
	 */
	public static void waitForDashboardPresent(WebDriver driver, String dashboardName) throws Exception
	{
		Validator.notEmptyString("dashboardName", dashboardName);
		driver.getLogger().info("[DashboardHomeUtil] call waitForDashboardPresent dashboardName: " + dashboardName);
		String indicator = DashBoardPageId.DashboardNameLocator.replace("_name_", dashboardName);
		driver.waitForElementPresent(indicator);
	}

	private static String convertID(String id)
	{
		return "id=" + id;
	}
	
	private static String convertName(String name) {
		return "name=" + name;
	}

	private static void deleteDashboardInGrid(WebDriver driver, String dashboardName)
	{
		WebElement gridTable = driver.getElement(DashboardHomeUtil.convertID(DashBoardPageId.DashboardTableID));
		List<WebElement> dashboardList = gridTable.findElements(By.tagName("div"));
		for (WebElement dashboard : dashboardList) {
			if (dashboardName.equals(dashboard.getAttribute("aria-label"))) {
				dashboard.findElement(By.cssSelector("button")).click(); // click "i" button
				driver.click(DashBoardPageId.DASHBOARD_HOME_DELETE_BUTTON); // click delete
				driver.waitForElementPresent(DashBoardPageId.BuilderDeleteDialogLocator);
				driver.click(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM); // confirm to delete

				WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(DashBoardPageId.BuilderDeleteDialogLocator)));
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
				driver.waitForElementPresent(DashBoardPageId.BuilderDeleteDialogLocator);

				driver.click(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM); // confirm to delete
				WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(DashBoardPageId.BuilderDeleteDialogLocator)));
				break;
			}
		}
	}
}
