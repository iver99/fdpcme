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

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardHomeUtil_171 extends DashboardHomeUtil_Version implements IDashboardHomeUtil
{

	//	public static void createDashboardSet(WebDriver driver, String name, String descriptions, Boolean displayDesc,
	//			Boolean selectorRefreshcontrol) throws Exception
	//	{
	//
	//	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#closeOverviewPage(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void closeOverviewPage(WebDriver driver) throws Exception
	{

		if (driver.isDisplayed(DashBoardPageId.OverviewCloseID)) {
			driver.getLogger().info("before clicking overview button");
			driver.click(DashBoardPageId.OverviewCloseID);
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#createDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDashboard(WebDriver driver, String name, String descriptions) throws Exception
	{
		createDashboard(driver, name, descriptions, TYPE_DASHBOARD);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#createDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDashboard(WebDriver driver, String name, String descriptions, String type) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call createDashboard : " + name);
		driver.click(convertID(DashBoardPageId.CreateDSButtonID));

		if (name != null && !name.isEmpty()) {
			driver.sendKeys(convertID(DashBoardPageId.DashBoardNameBoxID), name);
		}
		if (descriptions != null && !descriptions.isEmpty()) {
			driver.sendKeys(convertID(DashBoardPageId.DashBoardDescBoxID), descriptions);
		}
		if (DASHBOARD.equalsIgnoreCase(type)) {
			driver.check(convertID(DashBoardPageId.DashBoardType_Single));
		}
		else if (DASHBOARDSET.equalsIgnoreCase(type)) {
			driver.check(convertID(DashBoardPageId.DashBoardType_Set));
		}
		driver.takeScreenShot();
		driver.click(convertID(DashBoardPageId.DashOKButtonID));

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#createDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void createDashboardSet(WebDriver driver, String name, String descriptions) throws Exception
	{
		createDashboard(driver, name, descriptions, TYPE_DASHBOARDSET);

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#deleteDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteDashboard(WebDriver driver, String dashboardName, String view) throws Exception
	{

		driver.getLogger().info("[DashboardHomeUtil] call delete dashboardName : " + dashboardName);
		Validator.notEmptyString("dashboardName", dashboardName);
		search(driver, dashboardName);
		if (DASHBOARDS_GRID_VIEW.equals(view)) {
			gridView(driver);
			driver.takeScreenShot();
			deleteDashboardInGrid(driver, dashboardName);
		}

		if (DASHBOARDS_LIST_VIEW.equals(view)) {
			listView(driver);
			driver.takeScreenShot();
			deleteDashboardInList(driver, dashboardName);
		}
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#filterOptions(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void filterOptions(WebDriver driver, String filter) throws Exception
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

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IUiTestCommonAPI#getApiVersion(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public String getApiVersion(WebDriver wdriver)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#gotoDataExplorer(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void gotoDataExplorer(WebDriver driver, String option) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call exploreData -> " + option);

		Validator.notEmptyString("option", option);

		driver.click(convertName(DashBoardPageId.ExploreDataBtnID));
		WebElement menu = driver.getElement(convertName(DashBoardPageId.ExploreDataMenu));
		List<WebElement> menuList = menu.findElements(By.tagName("li"));
		for (WebElement menuItem : menuList) {
			if (option.equals(menuItem.getText())) {
				menuItem.click();
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#gridView(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void gridView(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call gridView");
		driver.waitForElementPresent(DashBoardPageId.DashboardsGridViewLocator);
		driver.takeScreenShot();
		driver.click(DashBoardPageId.DashboardsGridViewLocator);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#isDashboardExisted(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isDashboardExisted(WebDriver driver, String dashboardName) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call isDashboardExists dashboardName: " + dashboardName);
		Validator.notEmptyString("dashboardName", dashboardName);
		String indicator = DashBoardPageId.DashboardNameLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#isFilterOptionSelected(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isFilterOptionSelected(WebDriver driver, String filter) throws Exception
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

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#listDashboardNames(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public List<String> listDashboardNames(WebDriver driver) throws Exception
	{
		List<String> names = new ArrayList<String>();
		List<WebElement> eles = driver.getWebDriver().findElements(By.xpath(DashBoardPageId.DashboardNameContainers));
		for (int i = 1; i <= eles.size(); i++) {
			driver.getLogger().info("Get dahsbord name for: "
					+ DashBoardPageId.DashboardNameIndexLocator.replaceFirst("_index_", String.valueOf(i)));
			WebElement ele = driver
					.getElement(DashBoardPageId.DashboardNameIndexLocator.replaceFirst("_index_", String.valueOf(i)));
			String name = getElementAttribtue(ele, "aria-label");
			if (name == null) {
				name = ele.getText();
			}
			names.add(name);
		}
		/*
		for (WebElement e : eles) {
			//String name = e.getAttribute("aria-label");
			if (isAttribtuePresent(e, "aria-label")) {
				names.add(e.getAttribute("aria-label"));
			}
			else {
				names.add(e.getText());
			}
		}*/
		return names;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#listView(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void listView(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call listView");
		driver.waitForElementPresent(DashBoardPageId.DashboardsListViewLocator);
		driver.takeScreenShot();
		driver.click(DashBoardPageId.DashboardsListViewLocator);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#resetFilterOptions(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void resetFilterOptions(WebDriver driver) throws Exception
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

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#search(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void search(WebDriver driver, String searchString) throws Exception
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

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#selectDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectDashboard(WebDriver driver, String dashboardName) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call selectDashboard dashboardName: " + dashboardName);
		String indicator = DashBoardPageId.DashboardNameLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			throw new NoSuchElementException("Dashboard not exists. Name: " + dashboardName);
		}
		driver.click(indicator);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#selectOOB(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectOOB(WebDriver driver, String dashboardName) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call selectOOB dashboardName: " + dashboardName);
		Validator.notEmptyString("dashboardName", dashboardName);
		String indicator = DashBoardPageId.OOBDashboardNameLocator.replace("_name_", dashboardName);
		if (!driver.isElementPresent(indicator)) {
			throw new NoSuchElementException("Dashboard not exists. Name: " + dashboardName);
		}
		driver.click(indicator);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#sortBy(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void sortBy(WebDriver driver, String option) throws Exception
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
		else if (DASHBOARD_QUERY_ORDER_BY_NAME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByNameASCLocator);
			driver.click(DashBoardPageId.SortByNameASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_NAME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByNameDSCLocator);
			driver.click(DashBoardPageId.SortByNameDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_OWNER_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreatedByASCLocator);
			driver.click(DashBoardPageId.SortByCreatedByASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_OWNER_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreatedByDSCLocator);
			driver.click(DashBoardPageId.SortByCreatedByDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreateDateASCLocator);
			driver.click(DashBoardPageId.SortByCreateDateASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByCreateDateDSCLocator);
			driver.click(DashBoardPageId.SortByCreateDateDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastModifiedASCLocator);
			driver.click(DashBoardPageId.SortByLastModifiedASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastModifiedDSCLocator);
			driver.click(DashBoardPageId.SortByLastModifiedDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastAccessASCLocator);
			driver.click(DashBoardPageId.SortByLastAccessASCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else if (DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC.equals(option)) {
			driver.waitForElementPresent(DashBoardPageId.SortByLastAccessDSCLocator);
			driver.click(DashBoardPageId.SortByLastAccessDSCLocator);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		else {
			throw new IllegalArgumentException("Unknow Sort by option: " + option);
		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#sortListViewByCreateBy(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void sortListViewByCreateBy(WebDriver driver) throws Exception
	{

		driver.getLogger().info("[DashboardHomeUtil] call clickListViewTableCreatedByHeader");
		driver.waitForElementPresent(DashBoardPageId.ListViewTableCreatedByHeaderLocator);

		WebElement tableHeader = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.ListViewTableCreatedByHeaderLocator));
		WebElement tableSort = tableHeader.findElement(By.cssSelector(DashBoardPageId.ListViewSortLocatorCss));

		Actions actions = new Actions(driver.getWebDriver());
		driver.getLogger().info("Focus to the table header");
		actions.moveToElement(tableHeader).build().perform();
		driver.takeScreenShot();

		driver.getLogger().info("Click Sort icon");
		actions.moveToElement(tableSort).click().perform();

		driver.takeScreenShot();

		WaitUtil.waitForPageFullyLoaded(driver);
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#sortListViewByLastModified(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void sortListViewByLastModified(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call clickListViewTableLastModifiedHeader");
		driver.waitForElementPresent(DashBoardPageId.ListViewTableLastModifiedHeaderLocator);

		WebElement tableHeader = driver.getWebDriver()
				.findElement(By.xpath(DashBoardPageId.ListViewTableLastModifiedHeaderLocator));
		WebElement tableSort = tableHeader.findElement(By.cssSelector(DashBoardPageId.ListViewSortLocatorCss));

		Actions actions = new Actions(driver.getWebDriver());
		driver.getLogger().info("Focus to the table header");
		actions.moveToElement(tableHeader).build().perform();
		driver.takeScreenShot();

		driver.getLogger().info("Click Sort icon");
		actions.moveToElement(tableSort).click().perform();

		driver.takeScreenShot();

		WaitUtil.waitForPageFullyLoaded(driver);
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#sortListViewByName(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void sortListViewByName(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[DashboardHomeUtil] call clickListViewTableNameHeader");
		driver.waitForElementPresent(DashBoardPageId.ListViewTableNameHeaderLocator);

		WebElement tableHeader = driver.getWebDriver().findElement(By.xpath(DashBoardPageId.ListViewTableNameHeaderLocator));
		WebElement tableSort = tableHeader.findElement(By.cssSelector(DashBoardPageId.ListViewSortLocatorCss));

		Actions actions = new Actions(driver.getWebDriver());
		driver.getLogger().info("Focus to the table header");
		actions.moveToElement(tableHeader).build().perform();
		driver.takeScreenShot();

		driver.getLogger().info("Click Sort icon");
		actions.moveToElement(tableSort).click().perform();

		driver.takeScreenShot();

		WaitUtil.waitForPageFullyLoaded(driver);
		driver.takeScreenShot();
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardHomeUtil#waitForDashboardPresent(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void waitForDashboardPresent(WebDriver driver, String dashboardName) throws Exception
	{
		Validator.notEmptyString("dashboardName", dashboardName);
		driver.getLogger().info("[DashboardHomeUtil] call waitForDashboardPresent dashboardName: " + dashboardName);
		String indicator = DashBoardPageId.DashboardNameLocator.replace("_name_", dashboardName);
		driver.waitForElementPresent(indicator);
	}

	private String convertCss(String cssName)
	{
		return "css=" + cssName;
	}

	private String convertID(String id)
	{
		return "id=" + id;
	}

	private String convertName(String name)
	{
		return "name=" + name;
	}

	private void deleteDashboardInGrid(WebDriver driver, String dashboardName)
	{
		WebElement gridTable = driver.getElement(convertCss(DashBoardPageId.DASHBOARD_GRID_TABLE_CSS));
		List<WebElement> dashboardList = gridTable.findElements(By.tagName("div"));
		for (WebElement dashboard : dashboardList) {
			if (dashboardName.equals(dashboard.getAttribute("aria-label"))) {
				dashboard.findElement(By.cssSelector("button")).click(); // click "i" button

				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_BUTTON)); // click delete

				driver.waitForElementPresent(convertCss(DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG));
				driver.getLogger().info("foucus on the delete button");
				driver.getWebDriver().findElement(By.name(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM)).sendKeys(Keys.TAB);

				//driver.focus(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM); //focus on the delete button
				driver.getLogger().info("click on the delete button");
				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM)); // confirm to delete

				driver.getLogger().info("wait for the popup dialog close");
				WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
				wait.until(ExpectedConditions
						.invisibilityOfElementLocated(By.cssSelector(DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG)));
				break;
			}
		}
	}

	private void deleteDashboardInList(WebDriver driver, String dashboardName)
	{
		// find table
		WebElement listTable = driver.getElement(convertCss(DashBoardPageId.DASHBOARD_LIST_TABLE));
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
				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_BUTTON)); // click delete

				driver.waitForElementPresent(convertCss(DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG));
				driver.getLogger().info("foucus on the delete button");
				driver.getWebDriver().findElement(By.name(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM)).sendKeys(Keys.TAB);

				driver.getLogger().info("click on the delete button");
				driver.click(convertName(DashBoardPageId.DASHBOARD_HOME_DELETE_CONFIRM)); // confirm to delete
				WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
				wait.until(ExpectedConditions
						.invisibilityOfElementLocated(By.cssSelector(DashBoardPageId.DASHBOARD_HOME_DELETE_DIALOG)));
				break;
			}
		}
	}

	private String getElementAttribtue(WebElement element, String attribute)
	{
		try {
			return element.getAttribute(attribute);
		}
		catch (Exception e) {
		}

		return null;
	}

	private boolean isAttribtuePresent(WebElement element, String attribute)
	{
		Boolean result = false;
		try {
			String value = element.getAttribute(attribute);
			if (value != null) {
				result = true;
			}
		}
		catch (Exception e) {
		}

		return result;
	}

}
