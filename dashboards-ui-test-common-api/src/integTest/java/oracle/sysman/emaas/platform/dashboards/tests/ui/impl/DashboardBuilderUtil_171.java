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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DelayedPressEnterThread;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardBuilderUtil_171 extends DashboardBuilderUtil_Version implements IDashboardBuilderUtil
{
	private final String DASHBOARD_SELECTION_TAB_NAME = "Dashboard";

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#addNewDashboardToSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void addNewDashboardToSet(WebDriver driver, String dashboardName) throws Exception
	{
		driver.getLogger().info("addNewDashboardToSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		WebElement dashboardSetContainer = driver.getWebDriver()
				.findElement(By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException("removeDashboardInSet: the dashboard navigator container is not found");
		}

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		driver.takeScreenShot();

		boolean isSelectionTabExist = false;
		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetNavsCSS));
		if (navs == null || navs.size() == 0) {
			throw new NoSuchElementException("addNewDashboardToSet: the dashboard navigators is not found");
		}

		for (WebElement nav : navs) {
			if (nav.getAttribute("data-tabs-name").trim().equals(DASHBOARD_SELECTION_TAB_NAME)) {
				isSelectionTabExist = true;
				nav.click();
				WaitUtil.waitForPageFullyLoaded(driver);
				driver.takeScreenShot();
				driver.getLogger().info("addNewDashboardToSet has click on the dashboard selection tab");
				break;
			}
		}

		if (isSelectionTabExist == false) {
			WebElement addNav = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.DashboardSetNavAddBtnCSS));
			if (addNav == null) {
				throw new NoSuchElementException("removeDashboardInSet: the dashboard 'add' button  is not found");
			}
			addNav.click();
			WaitUtil.waitForPageFullyLoaded(driver); // wait for all dashboards loaded
		}

		driver.takeScreenShot();
		DashboardHomeUtil.selectDashboard(driver, dashboardName);
		driver.getLogger().info("removeDashboardInSet has selected the dashboard named with \"" + dashboardName + "\"");

		driver.takeScreenShot();
		driver.getLogger().info("addNewDashboardToSet completed and returns true");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#addWidgetToDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void addWidgetToDashboard(WebDriver driver, String searchString) throws Exception
	{
		Validator.notNull("widgetName", searchString);
		Validator.notEmptyString("widgetName", searchString);

		if (searchString == null) {
			return;
		}

		By locatorOfKeyEl = By.cssSelector(DashBoardPageId.RightDrawerCSS);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfKeyEl));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.getLogger().info("[DashboardHomeUtil] call addWidgetToDashboard with search string as " + searchString);

		//show right drawer if it is hidden
		showRightDrawer(driver);

		WebElement searchInput = driver.getElement("css=" + DashBoardPageId.RightDrawerSearchInputCSS);
		// focus to search input box
		wait.until(ExpectedConditions.elementToBeClickable(searchInput));

		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(searchInput).build().perform();
		searchInput.clear();
		actions.moveToElement(searchInput).build().perform();
		driver.click("css=" + DashBoardPageId.RightDrawerSearchInputCSS);
		searchInput.sendKeys(searchString);
		driver.takeScreenShot();
		//verify input box value
		Assert.assertEquals(searchInput.getAttribute("value"), searchString);

		WebElement searchButton = driver.getElement("css=" + DashBoardPageId.RightDrawerSearchButtonCSS);
		driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerSearchButtonCSS);
		searchButton.click();
		//wait for ajax resolved
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.takeScreenShot();

		driver.getLogger().info("[DashboardHomeUtil] start to add widget from right drawer");
		List<WebElement> matchingWidgets = driver.getWebDriver()
				.findElements(By.cssSelector(DashBoardPageId.RightDrawerWidgetCSS));
		if (matchingWidgets == null || matchingWidgets.size() == 0) {
			throw new NoSuchElementException("Right drawer widget for search string =" + searchString + " is not found");
		}

		//drag and drop not working
		//      WebElement tilesContainer = driver.getElement("css=" + DashBoardPageId.RightDrawerWidgetToAreaCSS);
		//      CommonActions.dragAndDropElement(driver, matchingWidgets.get(0), tilesContainer);

		// focus to  the first matching  widget
		driver.getLogger().info("Focus on the searched widget");
		driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.TAB);
		driver.takeScreenShot();

		// check if the searched widget has the focus
		driver.getLogger().info("Check if the searched widget get the focus");

		if (driver.getWebDriver().switchTo().activeElement()
				.equals(driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.RightDrawerWidgetCSS)))) {
			driver.getLogger().info("Press Enter button...");
			driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
			driver.takeScreenShot();
		}
		else {
			driver.getLogger().info("Widget didn't get the focus, need to focus on it");
			driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.TAB);
			driver.takeScreenShot();
			driver.getLogger().info("Press Enter button...");
			driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
			driver.takeScreenShot();

		}

		driver.getLogger().info("[DashboardHomeUtil] finish adding widget from right drawer");

		hideRightDrawer(driver);// hide drawer;
	}

	@Override
	public void createDashboardInsideSet(WebDriver driver, String name, String descriptions) throws Exception {
		Assert.assertTrue(false,"This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#deleteDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void deleteDashboard(WebDriver driver)
	{
		driver.getLogger().info("deleteDashboard started");
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BuilderOptionsMenuLocator)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsDeleteLocator);
		driver.click(DashBoardPageId.BuilderOptionsDeleteLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderDeleteDialogLocator);
		driver.click(DashBoardPageId.BuilderDeleteDialogDeleteBtnLocator);
		driver.takeScreenShot();
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);

		driver.getLogger().info("deleteDashboard completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#deleteDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void deleteDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("deleteDashboardSet started");

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.DashboardSetOptionBtn)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionBtn);
		driver.click(DashBoardPageId.DashboardSetOptionBtn);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionsDeleteLocator);
		driver.click(DashBoardPageId.DashboardSetOptionsDeleteLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.DashboardSetDeleteDialogLocator);
		driver.click(DashBoardPageId.DashboardSetDeleteDialogDeleteBtnLocator);
		driver.takeScreenShot();
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);

		driver.getLogger().info("deleteDashboardSet completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#duplicateDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void duplicateDashboard(WebDriver driver, String name, String descriptions) throws Exception
	{
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		Validator.notNull("duplicatename", name);
		Validator.notEmptyString("duplicatename", name);
		Validator.notEmptyString("duplicatedescription", descriptions);

		Validator.notEmptyString("duplicatename", name);

		driver.getLogger().info("duplicate started");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);
		driver.click("css=" + DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);
		driver.takeScreenShot();
		driver.waitForElementPresent("id=" + DashBoardPageId.BuilderOptionsDuplicateNameCSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ojDialogWrapper-duplicateDsbDialog")));
		//add name and description
		driver.getElement("id=" + DashBoardPageId.BuilderOptionsDuplicateNameCSS).clear();
		driver.click("id=" + DashBoardPageId.BuilderOptionsDuplicateNameCSS);
		By locatorOfDuplicateNameEl = By.id(DashBoardPageId.BuilderOptionsDuplicateNameCSS);

		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
		driver.sendKeys("id=" + DashBoardPageId.BuilderOptionsDuplicateNameCSS, name);
		driver.getElement("id=" + DashBoardPageId.BuilderOptionsDuplicateDescriptionCSS).clear();
		driver.click("id=" + DashBoardPageId.BuilderOptionsDuplicateDescriptionCSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
		if (descriptions == null) {
			driver.sendKeys("id=" + DashBoardPageId.BuilderOptionsDuplicateDescriptionCSS, "");
		}
		else {
			driver.sendKeys("id=" + DashBoardPageId.BuilderOptionsDuplicateDescriptionCSS, descriptions);
		}
		driver.takeScreenShot();

		//press ok button
		By locatorOfDuplicateSaveEl = By.cssSelector(DashBoardPageId.BuilderOptionsDuplicateSaveCSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateSaveEl));
		wait.until(ExpectedConditions.elementToBeClickable(locatorOfDuplicateSaveEl));

		By locatorOfDuplicateDesEl = By.id(DashBoardPageId.BuilderOptionsDuplicateDescriptionCSS);
		driver.getWebDriver().findElement(locatorOfDuplicateDesEl).sendKeys(Keys.TAB);

		WebElement saveButton = driver.getElement("css=" + DashBoardPageId.BuilderOptionsDuplicateSaveCSS);
		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(saveButton).build().perform();

		driver.takeScreenShot();
		driver.getLogger().info("duplicate save button has been focused");

		driver.click("css=" + DashBoardPageId.BuilderOptionsDuplicateSaveCSS);

		//wait for redirect
		String newTitleLocator = ".dbd-display-hover-area h1[title='" + name + "']";
		driver.getLogger().info("DashboardBuilderUtil.duplicate : wait for redirect" + newTitleLocator);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(newTitleLocator)));

		driver.takeScreenShot();
		driver.getLogger().info("duplicate completed");

	}

	@Override
	public void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions, boolean addToSet) throws Exception {
		Assert.assertTrue(false,"This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void editDashboard(WebDriver driver, String name, String descriptions) throws Exception
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		driver.getLogger().info("edit started");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.takeScreenShot();
		driver.waitForElementPresent("id=" + DashBoardPageId.BuilderOptionsEditNameCSS);

		//wait for 900s
		By locatorOfEditDesEl = By.id(DashBoardPageId.BuilderOptionsEditDescriptionCSS);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);

		//add name and description
		driver.getElement("id=" + DashBoardPageId.BuilderOptionsEditNameCSS).clear();
		driver.click("id=" + DashBoardPageId.BuilderOptionsEditNameCSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.sendKeys("id=" + DashBoardPageId.BuilderOptionsEditNameCSS, name);

		driver.getElement("id=" + DashBoardPageId.BuilderOptionsEditDescriptionCSS).clear();
		driver.click("id=" + DashBoardPageId.BuilderOptionsEditDescriptionCSS);

		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.sendKeys("id=" + DashBoardPageId.BuilderOptionsEditDescriptionCSS, descriptions);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.takeScreenShot();

		//press ok button
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsEditSaveCSS);
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditSaveCSS);
		driver.takeScreenShot();
		driver.getLogger().info("edit complete");
	}

	@Override
	public void editDashboard(WebDriver driver, String name, String descriptions, Boolean toShowDscptn) throws Exception {
		Assert.assertTrue(false,"This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#editDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void editDashboardSet(WebDriver driver, String name, String descriptions) throws Exception
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		//open the edit dialog
		driver.getLogger().info("editDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		driver.click("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
		driver.takeScreenShot();
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsEditDialogID);

		//edit name and description
		boolean editNameDescriptionElem = driver.isDisplayed("css=" + DashBoardPageId.DashboardsetOptionsNameCollapsibleCSS);
		if (!editNameDescriptionElem) {
			driver.click("id=" + DashBoardPageId.DashboardsetOptionsNameCollapsibleCSS);
		}
		//wait obj
		By locatorOfEditDesEl = By.cssSelector(DashBoardPageId.DashboardsetOptionsEditNameCSS);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));

		//edit name
		driver.getLogger().info("editDashboardSet start editing name");
		driver.getElement("css=" + DashBoardPageId.DashboardsetOptionsEditNameCSS).clear();
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsEditNameCSS);
		driver.sendKeys("css=" + DashBoardPageId.DashboardsetOptionsEditNameCSS, name);

		//edit description
		driver.getLogger().info("editDashboardSet start editing description");
		driver.getElement("css=" + DashBoardPageId.DashboardsetOptionsEditDescriptionCSS).clear();
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsEditDescriptionCSS);
		driver.sendKeys("css=" + DashBoardPageId.DashboardsetOptionsEditDescriptionCSS, descriptions);
		driver.takeScreenShot();

		//press save button
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsEditSaveID);
		driver.click("id=" + DashBoardPageId.DashboardsetOptionsEditSaveID);
		driver.getLogger().info("editDashboardSet complete");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#favoriteOption(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean favoriteOption(WebDriver driver) throws Exception
	{
		driver.getLogger().info("favoriteOption started");

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.takeScreenShot();
		boolean favoriteElem = driver.isDisplayed("css=" + DashBoardPageId.BuilderOptionsFavoriteLocatorCSS);
		if (favoriteElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsFavoriteLocatorCSS);
			driver.click("css=" + DashBoardPageId.BuilderOptionsFavoriteLocatorCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil add favorite completed");
			return true;
		}
		else {
			driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsRemoveFavoriteLocatorCSS);
			driver.click("css=" + DashBoardPageId.BuilderOptionsRemoveFavoriteLocatorCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil remove favorite completed");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#favoriteOptionDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean favoriteOptionDashboardSet(WebDriver driver) throws Exception
	{
		driver.getLogger().info("favoriteOptionDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		driver.click("id=" + DashBoardPageId.DashboardsetOptionsMenuID);

		boolean dashboardsetFavoriteElem = driver.isDisplayed("css=" + DashBoardPageId.DashboardsetOptionsRemoveFavoriteCSS);
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsfavoriteCSS);
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsfavoriteCSS);
		driver.takeScreenShot();
		if (dashboardsetFavoriteElem) {
			driver.getLogger().info("DashboardBuilderUtil remove favorite dashboardset completed");
			return false;
		}
		else {
			driver.getLogger().info("DashboardBuilderUtil add favorite dashboardset completed");
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#gridView(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void gridView(WebDriver driver) throws Exception
	{
		DashboardHomeUtil.gridView(driver);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#isRefreshSettingChecked(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings) throws Exception
	{
		driver.getLogger().info("isRefreshSettingChecked started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BuilderOptionsMenuLocator)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsAutoRefreshLocator);
		driver.click(DashBoardPageId.BuilderOptionsAutoRefreshLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsAutoRefreshOffLocator);
		if (REFRESH_DASHBOARD_SETTINGS_OFF.equals(refreshSettings)) {
			boolean checked = driver.isDisplayed(DashBoardPageId.BuilderAutoRefreshOffSelectedLocator);
			driver.getLogger().info("isRefreshSettingChecked completed, return result is " + checked);
			return checked;
		}
		else {//REFRESH_DASHBOARD_PARAM_5MIN:
			boolean checked = driver.isDisplayed(DashBoardPageId.BuilderAutoRefreshOn5MinSelectedLocator);
			driver.getLogger().info("isRefreshSettingChecked completed, return result is " + checked);
			return checked;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#isRefreshSettingCheckedForDashbaordSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean isRefreshSettingCheckedForDashbaordSet(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("isRefreshSettingCheckedForDashbaordSet started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshDashboardSet", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionBtn);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.DashboardSetOptionBtn)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionBtn);
		driver.click(DashBoardPageId.DashboardSetOptionBtn);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionsAutoRefreshLocator);
		driver.click(DashBoardPageId.DashboardSetOptionsAutoRefreshLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionsAutoRefreshOffLocator);
		driver.takeScreenShot();
		if (REFRESH_DASHBOARD_SETTINGS_OFF.equals(refreshSettings)) {
			boolean checked = driver.isDisplayed(DashBoardPageId.DashboardSetAutoRefreshOffSelectedLocator);
			driver.getLogger().info("isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
			return checked;
		}
		else {// REFRESH_DASHBOARD_SETTINGS_5MIN:
			boolean checked = driver.isDisplayed(DashBoardPageId.DashboardSetAutoRefreshOn5MinSelectedLocator);
			driver.getLogger().info("isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
			return checked;
		}
	}

	//	public void loadWebDriverOnly(WebDriver webDriver) throws Exception
	//	{
	//		driver = webDriver;
	//	}
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#listView(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void listView(WebDriver driver) throws Exception
	{
		DashboardHomeUtil.listView(driver);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#openWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void openWidget(WebDriver driver, String widgetName) throws Exception
	{
		openWidget(driver, widgetName, 0);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#openWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int)
	 */
	@Override
	public void openWidget(WebDriver driver, String widgetName, int index) throws Exception
	{
		driver.getLogger().info("openWidget started for widgetName=" + widgetName + ", index=" + index);

		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		clickTileOpenInDataExplorerButton(driver, widgetName, index);

		driver.getLogger().info("openWidget completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#printDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void printDashboard(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil print dashboard started");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsPrintLocatorCSS);
		driver.takeScreenShot();
		DelayedPressEnterThread thr = new DelayedPressEnterThread("DelayedPressEnterThread", 5000);
		driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.BuilderOptionsPrintLocatorCSS)).click();
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil print completed");
	}
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#printDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */

	@Override
	public void printDashboardSet(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil print dashboard set started");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		int waitTime = 5000;

		//click all tabs
		WebElement dashboardSetContainer = driver.getWebDriver()
				.findElement(By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException("removeDashboardInSet: the dashboard navigator container is not found");
		}

		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetNavsCSS));
		if (navs == null || navs.size() == 0) {
			throw new NoSuchElementException("addNewDashboardToSet: the dashboard navigators is not found");
		}
		for (WebElement nav : navs) {
			nav.click();
			WaitUtil.waitForPageFullyLoaded(driver);
			driver.takeScreenShot();
			driver.getLogger().info("printDashboardSet has click on the dashboard selection tab named");
		}

		//click print
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		driver.click("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsPrintCSS);
		DelayedPressEnterThread thr = new DelayedPressEnterThread("DelayedPressEnterThread", waitTime);
		driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.DashboardsetOptionsPrintCSS)).click();
		//have to use thread sleep to wait for the print window(windows dialog) to appear
		Thread.sleep(waitTime);
		driver.getLogger().info("DashboardBuilderUtil print set completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#refreshDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void refreshDashboard(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("refreshDashboard started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BuilderOptionsMenuLocator)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsAutoRefreshLocator);
		driver.click(DashBoardPageId.BuilderOptionsAutoRefreshLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsAutoRefreshOffLocator);
		switch (refreshSettings) {
			case REFRESH_DASHBOARD_SETTINGS_OFF:
				driver.check(DashBoardPageId.BuilderOptionsAutoRefreshOffLocator);
				driver.waitForElementPresent(DashBoardPageId.BuilderAutoRefreshOffSelectedLocator);
				driver.takeScreenShot();
				break;
			case REFRESH_DASHBOARD_SETTINGS_5MIN:
				driver.check(DashBoardPageId.BuilderOptionsAutoRefreshOn5MinLocator);
				driver.waitForElementPresent(DashBoardPageId.BuilderAutoRefreshOn5MinSelectedLocator);
				driver.takeScreenShot();
				break;
		}
		driver.getLogger().info("refreshDashboard completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#refreshDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void refreshDashboardSet(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("refreshDashboardSet started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshDashboardSet", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionBtn);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DashBoardPageId.DashboardSetOptionBtn)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionBtn);
		driver.click(DashBoardPageId.DashboardSetOptionBtn);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionsAutoRefreshLocator);
		driver.click(DashBoardPageId.DashboardSetOptionsAutoRefreshLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionsAutoRefreshOffLocator);
		switch (refreshSettings) {
			case REFRESH_DASHBOARD_SETTINGS_OFF:
				driver.check(DashBoardPageId.DashboardSetOptionsAutoRefreshOffLocator);
				break;
			case REFRESH_DASHBOARD_SETTINGS_5MIN:
				driver.check(DashBoardPageId.DashboardSetOptionsAutoRefreshOn5MinLocator);
				break;
		}
		driver.getLogger().info("refreshDashboardSet completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#removeDashboardInSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void removeDashboardInSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("removeDashboardInSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		WebElement dashboardSetContainer = driver.getWebDriver()
				.findElement(By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException("removeDashboardInSet: the dashboard navigator container is not found");
		}

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		driver.takeScreenShot();

		boolean hasFound = false;
		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetNavsCSS));
		if (navs == null || navs.size() == 0) {
			throw new NoSuchElementException("removeDashboardInSet: the dashboard navigators is not found");
		}

		for (WebElement nav : navs) {
			if (nav.getAttribute("data-tabs-name").trim().equals(dashboardName)) {
				hasFound = true;
				nav.findElement(By.cssSelector(DashBoardPageId.DashboardSetNavRemoveBtnCSS)).click();
				driver.getLogger()
						.info("removeDashboardInSet has found and removed the dashboard named with \"" + dashboardName + "\"");
				driver.takeScreenShot();
				break;
			}
		}

		if (hasFound == false) {
			throw new NoSuchElementException(
					"removeDashboardInSet can not find the dashboard named with \"" + dashboardName + "\"");
		}

		driver.takeScreenShot();
		driver.getLogger().info("removeDashboardInSet completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#removeWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void removeWidget(WebDriver driver, String widgetName) throws Exception
	{
		removeWidget(driver, widgetName, 0);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#removeWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int)
	 */
	@Override
	public void removeWidget(WebDriver driver, String widgetName, int index) throws Exception
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		WebElement widgetEl = getWidgetByName(driver, widgetName, index);

		focusOnWidgetHeader(driver, widgetEl);
		driver.takeScreenShot();

		widgetEl.findElement(By.cssSelector(DashBoardPageId.ConfigTileCSS)).click();
		driver.click("css=" + DashBoardPageId.RemoveTileCSS);
		driver.getLogger().info("Remove the widget");
		driver.takeScreenShot();

	}

	@Override
	public void removeDashboardFromSet(WebDriver driver, String dashboardName) throws Exception {
		Assert.assertTrue(false,"This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#resizeWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int, java.lang.String)
	 */
	@Override
	public void resizeWidget(WebDriver driver, String widgetName, int index, String resizeOptions) throws Exception
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		Validator.fromValidValues("resizeOptions", resizeOptions, TILE_NARROWER, TILE_WIDER, TILE_SHORTER, TILE_TALLER);

		WebElement widgetEl = getWidgetByName(driver, widgetName, index);

		focusOnWidgetHeader(driver, widgetEl);
		driver.takeScreenShot();

		String tileResizeCSS = null;
		switch (resizeOptions) {
			case TILE_WIDER:
				tileResizeCSS = DashBoardPageId.WiderTileCSS;
				break;
			case TILE_NARROWER:
				tileResizeCSS = DashBoardPageId.NarrowerTileCSS;
				break;
			case TILE_SHORTER:
				tileResizeCSS = DashBoardPageId.ShorterTileCSS;
				break;
			case TILE_TALLER:
				tileResizeCSS = DashBoardPageId.TallerTileCSS;
				break;
			default:
				break;
		}
		if (null == tileResizeCSS) {
			return;
		}

		widgetEl.findElement(By.cssSelector(DashBoardPageId.ConfigTileCSS)).click();
		driver.click("css=" + tileResizeCSS);
		driver.getLogger().info("Resize the widget");
		driver.takeScreenShot();

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#resizeWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String)
	 */
	@Override
	public void resizeWidget(WebDriver driver, String widgetName, String resizeOptions) throws Exception
	{
		resizeWidget(driver, widgetName, 0, resizeOptions);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#saveDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public void saveDashboard(WebDriver driver) throws Exception
	{
		driver.getLogger().info("save started");
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardSaveCSS);
		driver.click("css=" + DashBoardPageId.DashboardSaveCSS);
		driver.takeScreenShot();
		driver.getLogger().info("save compelted");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#search(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void search(WebDriver driver, String searchString) throws Exception
	{
		DashboardHomeUtil.search(driver, searchString);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#selectDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void selectDashboard(WebDriver driver, String dashboardName) throws Exception
	{
		DashboardHomeUtil.selectDashboard(driver, dashboardName);
	}

	@Override
	public void selectDashboardInsideSet(WebDriver driver, String dashboardName) throws Exception {
		Assert.assertTrue(false,"This method is not available in the current version");
		driver.getLogger().info("Method not available in the current version");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#showWidgetTitle(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, boolean)
	 */
	@Override
	public void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility) throws Exception
	{
		showWidgetTitle(driver, widgetName, 0, visibility);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#showWidgetTitle(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int, boolean)
	 */
	@Override
	public void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility) throws Exception
	{
		driver.getLogger()
				.info("showWidgetTitle started for widgetName=" + widgetName + ", index=" + index + ", visibility=" + visibility);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		driver.waitForElementPresent(DashBoardPageId.BuilderTilesEditArea);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BuilderTilesEditArea)));
		WaitUtil.waitForPageFullyLoaded(driver);

		clickTileConfigButton(driver, widgetName, index);

		if (visibility) {
			if (driver.isDisplayed(DashBoardPageId.BuilderTileHideLocator)) {
				driver.takeScreenShot();
				driver.getLogger().info("showWidgetTitle completed as title is shown already");
				return;
			}
			driver.click(DashBoardPageId.BuilderTileShowLocator);
			driver.takeScreenShot();
		}
		else {
			if (driver.isDisplayed(DashBoardPageId.BuilderTileShowLocator)) {
				driver.takeScreenShot();
				driver.getLogger().info("showWidgetTitle completed as title is hidden already");
				return;
			}
			driver.click(DashBoardPageId.BuilderTileHideLocator);
			driver.takeScreenShot();
		}
		driver.getLogger().info("showWidgetTitle completed");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#sortBy(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void sortBy(WebDriver driver, String option) throws Exception
	{
		DashboardHomeUtil.sortBy(driver, option);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleHome(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean toggleHome(WebDriver driver) throws Exception
	{
		driver.getLogger().info("asHomeOption started");

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		boolean homeElem = driver.isDisplayed("css=" + DashBoardPageId.BuilderOptionsSetHomeLocatorCSS);
		driver.takeScreenShot();

		if (homeElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsSetHomeLocatorCSS);
			driver.click("css=" + DashBoardPageId.BuilderOptionsSetHomeLocatorCSS);
			driver.takeScreenShot();
			boolean comfirmDialog = driver.isDisplayed("css=" + DashBoardPageId.BuilderOptionsSetHomeSaveCSS);
			System.out.println("dialog home " + comfirmDialog);
			if (comfirmDialog) {
				driver.click("css=" + DashBoardPageId.BuilderOptionsSetHomeSaveCSS);
				driver.takeScreenShot();
			}
			;
			driver.getLogger().info("DashboardBuilderUtil set home completed");
			return true;
		}
		else {
			driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsRemoveHomeLocatorCSS);
			driver.click("css=" + DashBoardPageId.BuilderOptionsRemoveHomeLocatorCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil remove home completed");
			return false;
		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleHomeDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean toggleHomeDashboardSet(WebDriver driver) throws Exception
	{
		driver.getLogger().info("toggleHomeOptionDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		driver.click("id=" + DashBoardPageId.DashboardsetOptionsMenuID);

		boolean homeElem = driver.isDisplayed("css=" + DashBoardPageId.DashboardsetOptionsAddHomeCSS);
		driver.takeScreenShot();
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsHomeCSS);
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsHomeCSS);
		driver.takeScreenShot();

		if (homeElem) {
			driver.getLogger().info("DashboardBuilderUtil set home in dashboard set completed");
			return true;
		}
		else {
			driver.getLogger().info("DashboardBuilderUtil remove home in dashboard set completed");
			return false;
		}

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleShareDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean toggleShareDashboard(WebDriver driver) throws Exception
	{
		driver.getLogger().info("sharedashboard started");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.takeScreenShot();
		boolean shareElem = driver.isDisplayed("css=" + DashBoardPageId.BuilderOptionsShareLocatorCSS);
		if (shareElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsShareLocatorCSS);
			driver.click("css=" + DashBoardPageId.BuilderOptionsShareLocatorCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil share dashboard");
			return true;
		}
		else {
			driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsUnShareLocatorCSS);
			driver.click("css=" + DashBoardPageId.BuilderOptionsUnShareLocatorCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil unshare dashboard");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#toggleShareDashboardset(oracle.sysman.qatool.uifwk.webdriver.WebDriver)
	 */
	@Override
	public Boolean toggleShareDashboardset(WebDriver driver) throws Exception
	{
		driver.getLogger().info("toggleShareDashboardset started");
		WaitUtil.waitForPageFullyLoaded(driver);

		//open the edit/share dialog
		driver.getLogger().info("toggleShareDashboardset open share/edit dialog");
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		driver.click("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
		driver.takeScreenShot();
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsEditDialogID);

		//open share collapsible
		boolean editShareElem = driver.isDisplayed("css=" + DashBoardPageId.DashboardsetOptionsShareDiaOpenCSS);

		if (!editShareElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsShareCollapsibleCSS);
			driver.click("css=" + DashBoardPageId.DashboardsetOptionsShareCollapsibleCSS);
		}
		driver.getLogger().info("toggleShareDashboardset dialog has opened");

		//toggle share dashboardset
		boolean shareFlagElem = driver.isDisplayed("css=" + DashBoardPageId.DashboardsetOptionsShareOnJudgeCSS);
		if (shareFlagElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsUnshareCSS);
			driver.click("css=" + DashBoardPageId.DashboardsetOptionsUnshareCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil unshare dashboardset");
			driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsEditSaveID);
			driver.click("id=" + DashBoardPageId.DashboardsetOptionsEditSaveID);
			driver.getLogger().info("DashboardBuilderUtil toggleShareDashboardset completed");
			return false;
		}
		else {
			driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsShareCSS);
			driver.click("css=" + DashBoardPageId.DashboardsetOptionsShareCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil share dashboardset");
			driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsEditSaveID);
			driver.click("id=" + DashBoardPageId.DashboardsetOptionsEditSaveID);
			driver.getLogger().info("DashboardBuilderUtil toggleShareDashboardset completed");
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyDashboard(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector)
	{
		driver.getLogger().info("verifyDashboard started for name=\"" + dashboardName + "\", description=\"" + description
				+ "\", showTimeSelector=\"" + showTimeSelector + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		driver.waitForElementPresent(DashBoardPageId.BuilderNameTextLocator);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BuilderNameTextLocator)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId.BuilderNameTextLocator);
		driver.click(DashBoardPageId.BuilderNameTextLocator);
		driver.takeScreenShot();
		String realName = driver.getElement(DashBoardPageId.BuilderNameTextLocator).getAttribute("title");
		if (!dashboardName.equals(realName)) {
			driver.getLogger().info("verifyDashboard compelted and returns false. Expected dashboard name is " + dashboardName
					+ ", actual dashboard name is " + realName);
			return false;
		}

		driver.waitForElementPresent(DashBoardPageId.BuilderDescriptionTextLocator);
		String realDesc = driver.getElement(DashBoardPageId.BuilderDescriptionTextLocator).getAttribute("title");
		if (description == null || description.equals("")) {
			if (realDesc != null && !realDesc.trim().equals("")) {
				driver.getLogger().info("verifyDashboard compelted and returns false. Expected description is " + description
						+ ", actual dashboard description is " + realDesc);
				return false;
			}
		}
		else {
			if (!description.equals(realDesc)) {
				driver.getLogger().info("verifyDashboard compelted and returns false. Expected description is " + description
						+ ", actual dashboard description is " + realDesc);
				return false;
			}
		}

		boolean actualTimeSelectorShown = driver.isDisplayed(DashBoardPageId.BuilderDateTimePickerLocator);
		if (actualTimeSelectorShown != showTimeSelector) {
			driver.getLogger().info("verifyDashboard compelted and returns false. Expected showTimeSelector is "
					+ showTimeSelector + ", actual dashboard showTimeSelector is " + actualTimeSelectorShown);
			return false;
		}

		driver.getLogger().info("verifyDashboard compelted and returns true");
		return true;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyDashboardInsideSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean verifyDashboardInsideSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("verifyDashboardInsideSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		WebElement dashboardSetContainer = driver.getWebDriver()
				.findElement(By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException("verifyDashboardInsideSet: the dashboard navigator container is not found");
		}

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.takeScreenShot();

		boolean hasFound = false;
		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetNavsCSS));
		if (navs == null || navs.size() == 0) {
			throw new NoSuchElementException("verifyDashboardInsideSet: the dashboard navigators is not found");
		}

		for (WebElement nav : navs) {
			if (nav.getAttribute("data-dashboard-name-in-set") != null
					&& nav.getAttribute("data-dashboard-name-in-set").trim().equals(dashboardName)) {
				hasFound = true;
				break;
			}
		}

		if (hasFound) {
			driver.getLogger().info("verifyDashboardInsideSet name=\"" + dashboardName + "\" has found");
		}
		else {
			driver.getLogger().info("verifyDashboardInsideSet name=\"" + dashboardName + "\" has not found");
		}
		driver.getLogger().info("removeDashboardInSet completed");
		return hasFound;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyDashboardSet(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean verifyDashboardSet(WebDriver driver, String dashboardSetName)
	{
		driver.getLogger().info("verifyDashboard started for name=\"" + dashboardSetName + "\"");
		Validator.notEmptyString("dashboardSetName", dashboardSetName);

		driver.waitForElementPresent(DashBoardPageId.DashboardSetNameTextLocator);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.DashboardSetNameTextLocator)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId.DashboardSetNameTextLocator);
		driver.click(DashBoardPageId.DashboardSetNameTextLocator);
		driver.takeScreenShot();
		String realName = driver.getElement(DashBoardPageId.DashboardSetNameTextLocator).getText();
		if (!dashboardSetName.equals(realName)) {
			driver.getLogger().info("verifyDashboardSet compelted and returns false. Expected dashboard set name is "
					+ dashboardSetName + ", actual dashboard set name is " + realName);
			return false;
		}

		driver.getLogger().info("verifyDashboardSet compelted and returns true");
		return true;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public boolean verifyWidget(WebDriver driver, String widgetName)
	{
		return verifyWidget(driver, widgetName, 0);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil#verifyWidget(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String, int)
	 */
	@Override
	public boolean verifyWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("verifyWidget started for name=\"" + widgetName + "\", index=\"" + index + "\"");
		Validator.notEmptyString("dashboardName", widgetName);

		WebElement we = null;
		try {
			WaitUtil.waitForPageFullyLoaded(driver);
			we = getTileTitleElement(driver, widgetName, index);
		}
		catch (NoSuchElementException e) {
			driver.getLogger().info("verifyWidget compelted and returns false");
			return false;
		}
		if (we == null) {
			driver.getLogger().info("verifyWidget compelted and returns false");
			return false;
		}

		driver.getLogger().info("verifyWidget compelted and returns true");
		return true;
	}

	private WebElement clickTileConfigButton(WebDriver driver, String widgetName, int index)
	{
		WebElement tileTitle = getTileTitleElement(driver, widgetName, index);
		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId.BuilderTileConfigLocator));
		if (tileConfig == null) {
			throw new NoSuchElementException("Tile config menu for title=" + widgetName + ", index=" + index + " is not found");
		}
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		builder.moveToElement(tileConfig).click().perform();
		return tileConfig;
	}

	private void clickTileOpenInDataExplorerButton(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info("Start to find widget with widgetName=" + widgetName + ", index=" + index);
		WebElement widgetTitle = getTileTitleElement(driver, widgetName, index);
		if (widgetTitle == null) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		WebElement widgetDataExplore = widgetTitle.findElement(By.xpath(DashBoardPageId.BuilderTileDataExploreLocator));
		if (widgetDataExplore == null) {
			throw new NoSuchElementException(
					"Widget data explorer link for title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget configure button");
		Actions builder = new Actions(driver.getWebDriver());
		driver.getLogger().info("Now moving to the widget title bar");
		builder.moveToElement(widgetTitle).perform();
		driver.takeScreenShot();
		driver.getLogger().info("and clicks the widget config button");
		//		builder.moveToElement(widgetDataExplore).click().perform();
		widgetDataExplore.click();
		driver.takeScreenShot();
	}

	private void focusOnWidgetHeader(WebDriver driver, WebElement widgetElement)
	{
		if (null == widgetElement) {
			driver.getLogger().info("Fail to find the widget element");
			driver.takeScreenShot();
			throw new NoSuchElementException("Widget config menu is not found");
		}

		WebElement widgetHeader = widgetElement.findElement(By.cssSelector(DashBoardPageId.TileTitleCSS));
		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(widgetHeader).build().perform();
		driver.getLogger().info("Focus to the widget");
	}

	private WebElement getTileTitleElement(WebDriver driver, String widgetName, int index)
	{
		driver.waitForElementPresent(DashBoardPageId.BuilderTilesEditArea);
		driver.click(DashBoardPageId.BuilderTilesEditArea);
		driver.takeScreenShot();

		String titleTitlesLocator = String.format(DashBoardPageId.BuilderTileTitleLocator, widgetName);
		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(titleTitlesLocator));
		if (tileTitles == null || tileTitles.size() <= index) {
			throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
		}
		tileTitles.get(index).click();
		driver.takeScreenShot();
		return tileTitles.get(index);
	}

	private WebElement getWidgetByName(WebDriver driver, String widgetName, int index) throws InterruptedException
	{
		if (widgetName == null) {
			return null;
		}

		List<WebElement> widgets = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.WidgetTitleCSS));
		WebElement widget = null;
		int counter = 0;
		for (WebElement widgetElement : widgets) {
			WebElement widgetTitle = widgetElement.findElement(By.cssSelector(DashBoardPageId.TileTitleCSS));
			Validator.notNull("widgetTitle", widgetTitle);
			String widgetAttribute = widgetTitle.getAttribute("data-tile-title");
			Validator.notNull("widgetTitleAttribute", widgetAttribute);

			if (widgetAttribute.trim().equals(widgetName)) {
				if (counter == index) {
					widget = widgetElement;
					break;
				}
				counter++;
			}
		}

		return widget;
	}

	protected void hideRightDrawer(WebDriver driver) throws Exception
	{
		driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerCSS);
		if (isRightDrawerVisible(driver) == true) {
			driver.click("css=" + DashBoardPageId.RightDrawerToggleBtnCSS);
			driver.getLogger().info("[DashboardBuilderUtil] triggered hideRightDrawer.");
		}
		driver.takeScreenShot();
	}

	private boolean isRightDrawerVisible(WebDriver driver)
	{
		WebElement rightDrawerPanel = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.RightDrawerPanelCSS));
		boolean isDisplayed = rightDrawerPanel.getCssValue("display").equals("none") != true;
		driver.getLogger().info("isRightDrawerVisible,the isDisplayed value is " + isDisplayed);

		boolean isWidthValid = rightDrawerPanel.getCssValue("width").equals("0px") != true;
		driver.getLogger().info("isRightDrawerVisible,the isWidthValid value is " + isWidthValid);

		return isDisplayed && isWidthValid;
	}

	private void showRightDrawer(WebDriver driver) throws Exception
	{
		driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerCSS);
		if (isRightDrawerVisible(driver) == false) {
			driver.click("css=" + DashBoardPageId.RightDrawerToggleBtnCSS);
			driver.getLogger().info("[DashboardBuilderUtil] triggered showRightDrawer.");
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		driver.takeScreenShot();
	}
}
