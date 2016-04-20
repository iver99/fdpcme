package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DelayedPressEnterThread;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardBuilderUtil
{
	public static final String REFRESH_DASHBOARD_SETTINGS_OFF = "Off";
	public static final String REFRESH_DASHBOARD_SETTINGS_5MIN = "On (Every 5 Minutes)";

	public static void addWidgetByRightDrawer(WebDriver driver, String searchString) throws Exception
	{
		Validator.notNull("widgetName", searchString);
		Validator.notEmptyString("widgetName", searchString);

		if (searchString == null) {
			return;
		}

		driver.waitForElementPresent("css="+DashBoardPageId.RightDrawerCSS);
		driver.getLogger().info("[DashboardHomeUtil] call addWidgetByRightDrawer with search string as " + searchString);

		//show right drawer if it is hidden
		DashboardBuilderUtil.showRightDrawer(driver);

		WebElement searchInput = driver.getElement("css=" + DashBoardPageId.RightDrawerSearchInputCSS);
		searchInput.click();
		searchInput.clear();
		searchInput.sendKeys(searchString);
		driver.takeScreenShot();

		WebElement searchButton = driver.getElement("css=" + DashBoardPageId.RightDrawerSearchButtonCSS);
        driver.waitForElementPresent("css="+DashBoardPageId.RightDrawerSearchButtonCSS);
        searchButton.click();
		//wait for ajax resolved
		Thread.sleep(DashBoardPageId.Delaytime_short);
		driver.takeScreenShot();

		driver.getLogger().info("[DashboardHomeUtil] start to add widget from right drawer");
		List<WebElement> matchingWidgets = driver.getWebDriver()
				.findElements(By.cssSelector(DashBoardPageId.RightDrawerWidgetCSS));
		if (matchingWidgets == null && matchingWidgets.size() == 0) {
			throw new NoSuchElementException("Right drawer widget for search string =" + searchString + " is not found");
		}

		//drag and drop not working
		//      WebElement tilesContainer = driver.getElement("css=" + DashBoardPageId.RightDrawerWidgetToAreaCSS);
		//      CommonActions.dragAndDropElement(driver, matchingWidgets.get(0), tilesContainer);

		// focus to  the first matching  widget
		new Actions(driver.getWebDriver()).moveToElement(searchButton).build().perform();
		searchButton.sendKeys(Keys.TAB);
		driver.takeScreenShot();

		// press enter to add widget
		driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
		driver.takeScreenShot();

		driver.getLogger().info("[DashboardHomeUtil] finish adding widget from right drawer");

        hideRightDrawer(driver);// hide drawer;
	}

	public static Boolean toggleHome(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.asHomeOption started");

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		boolean homeElem = driver.isDisplayed("css=" + DashBoardPageId.BuilderOptionsSetHomeLocatorCSS);
		driver.takeScreenShot();

		if (homeElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsSetHomeLocatorCSS);
			driver.click("css=" + DashBoardPageId.BuilderOptionsSetHomeLocatorCSS);
			driver.takeScreenShot();
			boolean comfirmDialog = driver.isDisplayed(DashBoardPageId.BuilderOptionsSetHomeComfirmCSS);
			if (comfirmDialog) {
				driver.click(DashBoardPageId.BuilderOptionsSetHomeComfirmCSS);
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

	public static void deleteDashboard(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.deleteDashboard started");

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsDeleteLocator);
		driver.click(DashBoardPageId.BuilderOptionsDeleteMenuLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderDeleteDialogLocator);
		driver.click(DashBoardPageId.BuilderDeleteDialogDeleteBtnLocator);
		driver.takeScreenShot();
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);

		driver.getLogger().info("DashboardBuilderUtil.deleteDashboard completed");
	}

	public static void deleteDashboardSet(WebDriver driver) throws Exception
	{

	}

	public static void duplicate(WebDriver driver, String name, String descriptions) throws Exception
	{
		Validator.notNull("duplicatename", name);
		Validator.notEmptyString("duplicatename", name);
		Validator.notEmptyString("duplicatedescription", descriptions);

		Validator.notEmptyString("duplicatename", name);

		driver.getLogger().info("DashboardBuilderUtil.duplicate started");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);
		driver.click("css=" + DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);
		driver.takeScreenShot();
		driver.waitForElementPresent("id=" + DashBoardPageId.BuilderOptionsDuplicateNameCSS);

		//add name and description
		driver.getElement("id=" + DashBoardPageId.BuilderOptionsDuplicateNameCSS).clear();
		driver.click("id=" + DashBoardPageId.BuilderOptionsDuplicateNameCSS);
		By locatorOfDuplicateNameEl = By.id(DashBoardPageId.BuilderOptionsDuplicateNameCSS);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), DashBoardPageId.Delaytime_builder_short);
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
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsDuplicateSaveCSS);
		driver.click(DashBoardPageId.BuilderOptionsDuplicateSaveCSS);
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.duplicate completed");
	}

	public static void editDashboard(WebDriver driver, String name, String descriptions) throws Exception
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		driver.getLogger().info("DashboardBuilderUtil.edit started");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.takeScreenShot();
		driver.waitForElementPresent("id=" + DashBoardPageId.BuilderOptionsEditNameCSS);

		//wait for 900s
		By locatorOfEditDesEl = By.id(DashBoardPageId.BuilderOptionsEditDescriptionCSS);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), DashBoardPageId.Delaytime_builder_short);

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
		driver.waitForElementPresent("id=" + DashBoardPageId.BuilderOptionsEditSaveCSS);
		driver.click("id=" + DashBoardPageId.BuilderOptionsEditSaveCSS);
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.edit complete");
	}

	public static void editDashboardSet(WebDriver driver, String name, String descriptions, Boolean shareOption) throws Exception
	{

	}

	public static Boolean favoriteOption(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.favoriteOption started");

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

	private static void hideRightDrawer(WebDriver driver) throws Exception
	{
        driver.waitForElementPresent("css="+DashBoardPageId.RightDrawerCSS);
        if (DashboardBuilderUtil.isRightDrawerVisible(driver) == true) {
			driver.click("css=" + DashBoardPageId.RightDrawerToggleBtnCSS);
			driver.getLogger().info("[DashboardBuilderUtil] triggered hideRightDrawer.");
		}
		driver.takeScreenShot();
	}

	public static boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsAutoRefreshLocator);
		driver.click(DashBoardPageId.BuilderOptionsAutoRefreshLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsAutoRefreshOffLocator);
		if (REFRESH_DASHBOARD_SETTINGS_OFF.equals(refreshSettings)) {
			boolean checked = driver.isDisplayed(DashBoardPageId.BuilderAutoRefreshOffSelectedLocator);
			driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked completed, return result is " + checked);
			return checked;
		}
		else {//REFRESH_DASHBOARD_PARAM_5MIN:
			boolean checked = driver.isDisplayed(DashBoardPageId.BuilderAutoRefreshOn5MinSelectedLocator);
			driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked completed, return result is " + checked);
			return checked;
		}
	}

	//	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	//	{
	//		driver = webDriver;
	//	}

	public static void print(WebDriver driver) throws Exception
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

	public static void refreshDashboard(WebDriver driver, String refreshSettings) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboard started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

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
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboard completed");

	}

	public static void refreshDashboardSet(WebDriver driver, String refreshSettings) throws Exception
	{

	}

	public static void save(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.save started");
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardSaveCSS);
		driver.click("css=" + DashBoardPageId.DashboardSaveCSS);
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.save compelted");
	}

	private static void showRightDrawer(WebDriver driver) throws Exception
	{
        driver.waitForElementPresent("css="+DashBoardPageId.RightDrawerCSS);
        if (DashboardBuilderUtil.isRightDrawerVisible(driver) == false) {
			driver.click("css=" + DashBoardPageId.RightDrawerToggleBtnCSS);
			driver.getLogger().info("[DashboardBuilderUtil] triggered showRightDrawer.");
		}
		driver.takeScreenShot();
	}

	public static Boolean toggleShareDashboard(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.favoriteOption started");
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

	public static boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector)
	{
		driver.getLogger().info("DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardName + "\", description=\""
				+ description + "\", showTimeSelector=\"" + showTimeSelector + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		driver.waitForElementPresent(DashBoardPageId.BuilderNameTextLocator);
		driver.click(DashBoardPageId.BuilderNameTextLocator);
		//		driver.takeScreenShot();
		String realName = driver.getElement(DashBoardPageId.BuilderNameTextLocator).getAttribute("title");
		if (!dashboardName.equals(realName)) {
			driver.getLogger()
					.info("DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected dashboard name is "
							+ dashboardName + ", actual dashboard name is " + realName);
			return false;
		}

		driver.waitForElementPresent(DashBoardPageId.BuilderDescriptionTextLocator);
		String realDesc = driver.getElement(DashBoardPageId.BuilderDescriptionTextLocator).getAttribute("title");
		if (description == null || description.equals("")) {
			if (realDesc != null && !realDesc.trim().equals("")) {
				driver.getLogger()
						.info("DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
								+ description + ", actual dashboard description is " + realDesc);
				return false;
			}
		}
		else {
			if (!description.equals(realDesc)) {
				driver.getLogger()
						.info("DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
								+ description + ", actual dashboard description is " + realDesc);
				return false;
			}
		}

		boolean actualTimeSelectorShown = driver.isDisplayed(DashBoardPageId.BuilderDateTimePickerLocator);
		if (actualTimeSelectorShown != showTimeSelector) {
			driver.getLogger()
					.info("DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected showTimeSelector is "
							+ showTimeSelector + ", actual dashboard showTimeSelector is " + actualTimeSelectorShown);
			return false;
		}

		driver.getLogger().info("DashboardBuilderUtil.verifyDashboard compelted and returns true");
		return true;
	}

	private static boolean isRightDrawerVisible(WebDriver driver)
	{
		WebElement rightDrawerPanel = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.RightDrawerPanelCSS));
		boolean isDisplayed = rightDrawerPanel.getCssValue("display").equals("none") != true;
		driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isDisplayed value is "+isDisplayed);

		boolean isWidthValid = rightDrawerPanel.getCssValue("width").equals("0px") != true;
		driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isWidthValid value is "+isWidthValid);

		return isDisplayed && isWidthValid;
	}

}
