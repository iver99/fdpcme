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

	public static final String TILE_WIDER = "wider";

	public static final String TILE_NARROWER = "narrower";

	public static final String TILE_TALLER = "taller";

	public static final String TILE_SHORTER = "shorter";

	public static void addWidgetByRightDrawer(WebDriver driver, String searchString) throws Exception
	{
		Validator.notNull("widgetName", searchString);
		Validator.notEmptyString("widgetName", searchString);

		if (searchString == null) {
			return;
		}

		driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerCSS);
		driver.getLogger().info("[DashboardHomeUtil] call addWidgetByRightDrawer with search string as " + searchString);

		//show right drawer if it is hidden
		DashboardBuilderUtil.showRightDrawer(driver);

		WebElement searchInput = driver.getElement("css=" + DashBoardPageId.RightDrawerSearchInputCSS);
		searchInput.click();
		searchInput.clear();
		searchInput.sendKeys(searchString);
		driver.takeScreenShot();

		WebElement searchButton = driver.getElement("css=" + DashBoardPageId.RightDrawerSearchButtonCSS);
		driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerSearchButtonCSS);
		searchButton.click();
		//wait for ajax resolved
		Thread.sleep(DashBoardPageId.Delaytime_short);
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
		new Actions(driver.getWebDriver()).moveToElement(searchButton).build().perform();
		searchButton.sendKeys(Keys.TAB);
		driver.takeScreenShot();

		// press enter to add widget
		driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
		driver.takeScreenShot();

		driver.getLogger().info("[DashboardHomeUtil] finish adding widget from right drawer");

		DashboardBuilderUtil.hideRightDrawer(driver);// hide drawer;
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

	public static void duplicateDashboard(WebDriver driver, String name, String descriptions) throws Exception
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

	//	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	//	{
	//		driver = webDriver;
	//	}

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

	public static void openWidget(WebDriver driver, String widgetName) throws Exception
	{
		DashboardBuilderUtil.openWidget(driver, widgetName, 0);
	}

	public static void openWidget(WebDriver driver, String widgetName, int index) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.openWidget started for widgetName=" + widgetName + ", index=" + index);

		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		DashboardBuilderUtil.clickTileOpenInDataExplorerButton(driver, widgetName, index);

		driver.getLogger().info("DashboardBuilderUtil.openWidget completed");
	}

	public static void printDashboard(WebDriver driver) throws Exception
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

	public static void removeWidget(WebDriver driver, String widgetName) throws Exception
	{
		DashboardBuilderUtil.removeWidget(driver, widgetName, 0);
	}

	public static void removeWidget(WebDriver driver, String widgetName, int index) throws Exception
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		WebElement widgetEl = DashboardBuilderUtil.getWidgetByName(driver, widgetName, index);

		DashboardBuilderUtil.focusOnWidgetHeader(driver, widgetEl);
		driver.takeScreenShot();

		widgetEl.findElement(By.cssSelector(DashBoardPageId.ConfigTileCSS)).click();
		driver.click("css=" + DashBoardPageId.RemoveTileCSS);
		driver.getLogger().info("Remove the widget");
		driver.takeScreenShot();

	}

	public static void resizeWidget(WebDriver driver, String widgetName, int index, String resizeOptions) throws Exception
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		Validator.fromValidValues("resizeOptions", resizeOptions, DashboardBuilderUtil.TILE_NARROWER,
				DashboardBuilderUtil.TILE_WIDER, DashboardBuilderUtil.TILE_SHORTER, DashboardBuilderUtil.TILE_TALLER);

		WebElement widgetEl = DashboardBuilderUtil.getWidgetByName(driver, widgetName, index);

		DashboardBuilderUtil.focusOnWidgetHeader(driver, widgetEl);
		driver.takeScreenShot();

		String tileResizeCSS = null;
		switch (resizeOptions) {
			case DashboardBuilderUtil.TILE_WIDER:
				tileResizeCSS = DashBoardPageId.WiderTileCSS;
				break;
			case DashboardBuilderUtil.TILE_NARROWER:
				tileResizeCSS = DashBoardPageId.NarrowerTileCSS;
				break;
			case DashboardBuilderUtil.TILE_SHORTER:
				tileResizeCSS = DashBoardPageId.ShorterTileCSS;
				break;
			case DashboardBuilderUtil.TILE_TALLER:
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

	public static void resizeWidget(WebDriver driver, String widgetName, String resizeOptions) throws Exception
	{
		DashboardBuilderUtil.resizeWidget(driver, widgetName, 0, resizeOptions);
	}

	public static void saveDashboard(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.save started");
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardSaveCSS);
		driver.click("css=" + DashBoardPageId.DashboardSaveCSS);
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.save compelted");
	}

	public static void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility) throws Exception
	{
		DashboardBuilderUtil.showWidgetTitle(driver, widgetName, 0, visibility);
	}

	public static void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.showWidgetTitle started for widgetName=" + widgetName + ", index=" + index
				+ ", visibility=" + visibility);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		DashboardBuilderUtil.clickTileConfigButton(driver, widgetName, index);

		if (visibility) {
			if (driver.isDisplayed(DashBoardPageId.BuilderTileHideLocator)) {
				driver.takeScreenShot();
				driver.getLogger().info("DashboardBuilderUtil.showWidgetTitle completed as title is shown already");
				return;
			}
			driver.click(DashBoardPageId.BuilderTileShowLocator);
			driver.takeScreenShot();
		}
		else {
			if (driver.isDisplayed(DashBoardPageId.BuilderTileShowLocator)) {
				driver.takeScreenShot();
				driver.getLogger().info("DashboardBuilderUtil.showWidgetTitle completed as title is hidden already");
				return;
			}
			driver.click(DashBoardPageId.BuilderTileHideLocator);
			driver.takeScreenShot();
		}
		driver.getLogger().info("DashboardBuilderUtil.showWidgetTitle completed");
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

	private static WebElement clickTileConfigButton(WebDriver driver, String widgetName, int index)
	{
		WebElement tileTitle = DashboardBuilderUtil.getTileTitleElement(driver, widgetName, index);
		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId.BuilderTileConfigLocator));
		if (tileConfig == null) {
			throw new NoSuchElementException("Tile config menu for title=" + widgetName + ", index=" + index + " is not found");
		}
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		builder.moveToElement(tileConfig).click().perform();
		return tileConfig;
	}

	private static void clickTileOpenInDataExplorerButton(WebDriver driver, String widgetName, int index)
	{
		WebElement tileTitle = DashboardBuilderUtil.getTileTitleElement(driver, widgetName, index);
		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId.BuilderTileDataExploreLocator));
		if (tileConfig == null) {
			throw new NoSuchElementException(
					"Tile data explorer link for title=" + widgetName + ", index=" + index + " is not found");
		}
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		builder.moveToElement(tileConfig).click().perform();
		driver.takeScreenShot();
	}

	private static void focusOnWidgetHeader(WebDriver driver, WebElement widgetElement)
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

	private static WebElement getTileTitleElement(WebDriver driver, String widgetName, int index)
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

	private static WebElement getWidgetByName(WebDriver driver, String widgetName, int index) throws InterruptedException
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

	private static void hideRightDrawer(WebDriver driver) throws Exception
	{
		driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerCSS);
		if (DashboardBuilderUtil.isRightDrawerVisible(driver) == true) {
			driver.click("css=" + DashBoardPageId.RightDrawerToggleBtnCSS);
			driver.getLogger().info("[DashboardBuilderUtil] triggered hideRightDrawer.");
		}
		driver.takeScreenShot();
	}

	private static boolean isRightDrawerVisible(WebDriver driver)
	{
		WebElement rightDrawerPanel = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.RightDrawerPanelCSS));
		boolean isDisplayed = rightDrawerPanel.getCssValue("display").equals("none") != true;
		driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isDisplayed value is " + isDisplayed);

		boolean isWidthValid = rightDrawerPanel.getCssValue("width").equals("0px") != true;
		driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isWidthValid value is " + isWidthValid);

		return isDisplayed && isWidthValid;
	}

	private static void showRightDrawer(WebDriver driver) throws Exception
	{
		driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerCSS);
		if (DashboardBuilderUtil.isRightDrawerVisible(driver) == false) {
			driver.click("css=" + DashBoardPageId.RightDrawerToggleBtnCSS);
			driver.getLogger().info("[DashboardBuilderUtil] triggered showRightDrawer.");
		}
		driver.takeScreenShot();
	}
}
