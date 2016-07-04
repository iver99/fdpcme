package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DelayedPressEnterThread;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DashboardBuilderUtil
{
	public static final String REFRESH_DASHBOARD_SETTINGS_OFF = "OFF";
	public static final String REFRESH_DASHBOARD_SETTINGS_5MIN = "On (Every 5 Minutes)";

	public static final String TILE_WIDER = "wider";

	public static final String TILE_NARROWER = "narrower";

	public static final String TILE_TALLER = "taller";

	public static final String TILE_SHORTER = "shorter";

	private static final String DASHBOARD_SELECTION_TAB_NAME = "Dashboard";

	public static final String PENCIL = "pencil";
	public static final String WRENCH = "wrench";

	public static final String DUP_DASHBOARD_NODSUBMENU = "duplicate_noSubMenu";
	public static final String DUP_DASHBOARD_TOSET = "duplicate_addToSet";
	public static final String DUP_SHBOARDSET_NOTTOSET="duplicate_notAddToSet";

	public static void createDashboardInsideSet(WebDriver driver, String name, String descriptions) throws Exception
	{
		driver.waitForElementPresent("id="+DashBoardPageId.DashboardsetOptionsMenuID);
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.getLogger().info("DashboardBuilderUtil.createDashboardInsideSet : " + name);
		Validator.notEmptyString("name", name);
		driver.click("css="+DashBoardPageId.DASHBOARD_HOME_CREATINSET_BUTTON);

		if (name != null && !name.isEmpty()) {
			driver.sendKeys("id="+DashBoardPageId.DashBoardNameBoxID, name);
		}
		if (descriptions != null && !descriptions.isEmpty()) {
			driver.sendKeys("id="+DashBoardPageId.DashBoardDescBoxID, descriptions);
		}
		driver.takeScreenShot();
		driver.click("id="+DashBoardPageId.DashOKButtonID);
		driver.getLogger().info("DashboardBuilderUtil.createDashboardInsideSet completed");
	}

	public static void addNewDashboardToSet(WebDriver driver, String dashboardName) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
				By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.removeDashboardFromSet: the dashboard navigator container is not found");
		}

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		driver.takeScreenShot();

		boolean isSelectionTabExist = false;
		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetNavsCSS));
		if (navs == null || navs.size() == 0) {
			throw new NoSuchElementException("DashboardBuilderUtil.addNewDashboardToSet: the dashboard navigators is not found");
		}

		for (WebElement nav : navs) {
			if (nav.getAttribute("data-tabs-name").trim().equals(DASHBOARD_SELECTION_TAB_NAME)) {
				isSelectionTabExist = true;
				nav.click();
				WaitUtil.waitForPageFullyLoaded(driver);
				driver.takeScreenShot();
				driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet has click on the dashboard selection tab");
				break;
			}
		}

		if (isSelectionTabExist == false) {
			WebElement addNav = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.DashboardSetNavAddBtnCSS));
			if (addNav == null) {
				throw new NoSuchElementException(
						"DashboardBuilderUtil.removeDashboardFromSet: the dashboard 'add' button  is not found");
			}
			addNav.click();
			WaitUtil.waitForPageFullyLoaded(driver); // wait for all dashboards loaded
		}

		driver.takeScreenShot();
		DashboardHomeUtil.selectDashboard(driver, dashboardName);
		driver.getLogger().info(
				"DashboardBuilderUtil.removeDashboardFromSet has selected the dashboard named with \"" + dashboardName + "\"");

		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet completed and returns true");
	}

	public static void addWidgetToDashboard(WebDriver driver, String searchString) throws Exception
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
		DashboardBuilderUtil.showRightDrawer(driver, WRENCH);

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
		List<WebElement> matchingWidgets = driver.getWebDriver().findElements(
				By.cssSelector(DashBoardPageId.RightDrawerWidgetCSS));
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

		DashboardBuilderUtil.hideRightDrawer(driver);// hide drawer;
	}

	public static void deleteDashboard(WebDriver driver) throws Exception {
		driver.getLogger().info("DashboardBuilderUtil.deleteDashboard started");
		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		WaitUtil.waitForPageFullyLoaded(driver);

		WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
		WebElement editOption = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId.BuilderOptionsMenuLocator));
		editOption.click();
		driver.takeScreenShot();

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.click("css="+DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.takeScreenShot();

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsDeleteLocator);
		driver.click("css="+DashBoardPageId.BuilderOptionsDeleteLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.BuilderDeleteDialogLocator);
		driver.click(DashBoardPageId.BuilderDeleteDialogDeleteBtnLocator);
		driver.takeScreenShot();
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);

		driver.getLogger().info("DashboardBuilderUtil.deleteDashboard completed");
	}

	public static void deleteDashboardSet(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.deleteDashboardSet started");

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(DashBoardPageId.DashboardsetOptionsMenuID)));
		WaitUtil.waitForPageFullyLoaded(driver);

        //open settings menu
        driver.click("id="+DashBoardPageId.DashboardsetOptionsMenuID);

        // click edit option
        driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
        driver.click("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId.DashboardSetOptionsDeleteLocator);
		driver.click(DashBoardPageId.DashboardSetOptionsDeleteLocator);
		driver.takeScreenShot();

		driver.waitForElementPresent(DashBoardPageId.DashboardSetDeleteDialogLocator);
		driver.click(DashBoardPageId.DashboardSetDeleteDialogDeleteBtnLocator);
		driver.takeScreenShot();
        // wait until page redirect to dashboard home
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);

		driver.getLogger().info("DashboardBuilderUtil.deleteDashboardSet completed");
	}

	public static void duplicateDashboard(WebDriver driver, String name, String descriptions) throws Exception {
		duplicateDashboardCommonUse(driver,name, descriptions ,DUP_DASHBOARD_NODSUBMENU);
	}
	public static void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions,boolean addToSet) throws Exception {
		if(addToSet){
			duplicateDashboardCommonUse(driver,name, descriptions ,DUP_DASHBOARD_TOSET);
		}else{
			duplicateDashboardCommonUse(driver,name, descriptions ,DUP_SHBOARDSET_NOTTOSET);
		}
	}

	public static void editDashboard(WebDriver driver, String name, String descriptions, Boolean toShowDscptn) throws Exception
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		driver.getLogger().info("DashboardBuilderUtil.edit started");
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		WaitUtil.waitForPageFullyLoaded(driver);

		WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
		WebElement editOption = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId.BuilderOptionsMenuLocator));
		editOption.click();
		driver.takeScreenShot();

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.click("css="+DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.takeScreenShot();

		//wait for 900s
		By locatorOfEditDesEl = By.cssSelector(DashBoardPageId.BuilderOptionsEditDescriptionCSS);
//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);

		//add name and description
		driver.getElement("css=" + DashBoardPageId.BuilderOptionsEditNameCSS).clear();
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditNameCSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.sendKeys("css=" + DashBoardPageId.BuilderOptionsEditNameCSS, name);

		driver.getElement("css=" + DashBoardPageId.BuilderOptionsEditDescriptionCSS).clear();
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditDescriptionCSS);

		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.sendKeys("css=" + DashBoardPageId.BuilderOptionsEditDescriptionCSS, descriptions);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.takeScreenShot();

		driver.getElement("css=" + DashBoardPageId.BuilderOptionsEditShowDescriptionCSS);
		if(toShowDscptn) {
			driver.check("css=" + DashBoardPageId.BuilderOptionsEditShowDescriptionCSS);
		}else{
			driver.uncheck("css=" + DashBoardPageId.BuilderOptionsEditShowDescriptionCSS);
		}

		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.edit complete");
	}


        public static void EditDashboard_targetselctor(WebDriver driver, String name, String descriptions) throws Exception
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

		driver.getLogger().info("DashboardBuilderUtil.edit started");
		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.takeScreenShot();
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsEditNameCSS);

		//wait for 900s
		By locatorOfEditDesEl = By.cssSelector(DashBoardPageId.BuilderOptionsEditDescriptionCSS);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);

		//add name and description
		driver.getElement("css=" + DashBoardPageId.BuilderOptionsEditNameCSS).clear();
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditNameCSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.sendKeys("css=" + DashBoardPageId.BuilderOptionsEditNameCSS, name);

		driver.getElement("css=" + DashBoardPageId.BuilderOptionsEditDescriptionCSS).clear();
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditDescriptionCSS);

		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.sendKeys("css=" + DashBoardPageId.BuilderOptionsEditDescriptionCSS, descriptions);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.takeScreenShot();
             
                //selctor filetr entity
		driver.waitForElementPresent(DashBoardPageId.EntityfilterLocator);
		driver.click(DashBoardPageId.EntityfilterLocator);

		//press ok button
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsEditSaveCSS);
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditSaveCSS);
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.edit complete");
	}
	

          

	public static void editDashboardSet(WebDriver driver, String name, String descriptions) throws Exception
	{
		Validator.notNull("editname", name);
		Validator.notEmptyString("editname", name);

        driver.waitForElementPresent("id="+DashBoardPageId.DashboardsetOptionsMenuID);
		driver.getLogger().info("DashboardBuilderUtil.editDashboardSet started");
		WaitUtil.waitForPageFullyLoaded(driver);

        //open settings menu
		driver.click("id="+DashBoardPageId.DashboardsetOptionsMenuID);

        // click edit option
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
        driver.takeScreenShot();

		By locatorOfEditDesEl = By.cssSelector(DashBoardPageId.DashboardsetOptionsEditNameCSS);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
        driver.takeScreenShot();

        //edit name
		driver.getLogger().info("DashboardBuilderUtil.editDashboardSet start editing name");
		driver.getElement("css=" + DashBoardPageId.DashboardsetOptionsEditNameCSS).clear();
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsEditNameCSS);
		driver.sendKeys("css=" + DashBoardPageId.DashboardsetOptionsEditNameCSS, name);

		//edit description
		driver.getLogger().info("DashboardBuilderUtil.editDashboardSet start editing description");
		driver.getElement("css=" + DashBoardPageId.DashboardsetOptionsEditDescriptionCSS).clear();
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsEditDescriptionCSS);
		driver.sendKeys("css=" + DashBoardPageId.DashboardsetOptionsEditDescriptionCSS, descriptions);
		driver.takeScreenShot();

		//hide settings panel
		hideRightDrawer(driver);
	}

	public static Boolean favoriteOption(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.favoriteOption started");

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click("css="+DashBoardPageId.BuilderOptionsMenuLocator);
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

	public static Boolean favoriteOptionDashboardSet(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.favoriteOptionDashboardSet started");
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

	public static void gridView(WebDriver driver) throws Exception
	{
		DashboardHomeUtil.gridView(driver);
	}

	public static boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(DashBoardPageId.BuilderOptionsMenuLocator)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click("css="+DashBoardPageId.BuilderOptionsMenuLocator);
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

	public static boolean isRefreshSettingCheckedForDashbaordSet(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet started for refreshSettings=" + refreshSettings);

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
			driver.getLogger().info(
					"DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
			return checked;
		}
		else {// REFRESH_DASHBOARD_SETTINGS_5MIN:
			boolean checked = driver.isDisplayed(DashBoardPageId.DashboardSetAutoRefreshOn5MinSelectedLocator);
			driver.getLogger().info(
					"DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
			return checked;
		}
	}

	//	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	//	{
	//		driver = webDriver;
	//	}

	public static void listView(WebDriver driver) throws Exception
	{
		DashboardHomeUtil.listView(driver);
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
		driver.waitForElementPresent("css="+ DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsPrintLocatorCSS);
		driver.takeScreenShot();
		DelayedPressEnterThread thr = new DelayedPressEnterThread("DelayedPressEnterThread", 5000);
		driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.BuilderOptionsPrintLocatorCSS)).click();
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil print completed");
	}

	public static void printDashboardSet(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil print dashboard set started");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.waitForElementPresent("id=" + DashBoardPageId.DashboardsetOptionsMenuID);
		int waitTime = 5000;

		//click all tabs
		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
				By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.removeDashboardFromSet: the dashboard navigator container is not found");
		}

		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetNavsCSS));
		if (navs == null || navs.size() == 0) {
			throw new NoSuchElementException("DashboardBuilderUtil.addNewDashboardToSet: the dashboard navigators is not found");
		}
		for (WebElement nav : navs) {
			nav.click();
			WaitUtil.waitForPageFullyLoaded(driver);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil.printDashboardSet has click on the dashboard selection tab named");
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

	public static void refreshDashboard(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboard started for refreshSettings=" + refreshSettings);

		Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
				REFRESH_DASHBOARD_SETTINGS_5MIN);

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(DashBoardPageId.BuilderOptionsMenuLocator)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click("css="+DashBoardPageId.BuilderOptionsMenuLocator);
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

	public static void refreshDashboardSet(WebDriver driver, String refreshSettings)
	{
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboardSet started for refreshSettings=" + refreshSettings);

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
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboardSet completed");
	}

	public static void removeDashboardFromSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("DashboardBuilderUtil.removeDashboardFromSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
				By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.removeDashboardFromSet: the dashboard navigator container is not found");
		}

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		driver.takeScreenShot();

		boolean hasFound = false;
		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetNavsCSS));
		if (navs == null || navs.size() == 0) {
			throw new NoSuchElementException("DashboardBuilderUtil.removeDashboardFromSet: the dashboard navigators is not found");
		}

		for (WebElement nav : navs) {
			if (nav.getAttribute("data-tabs-name").trim().equals(dashboardName)) {
				hasFound = true;
				nav.findElement(By.cssSelector(DashBoardPageId.DashboardSetNavRemoveBtnCSS)).click();
				driver.getLogger().info(
						"DashboardBuilderUtil.removeDashboardFromSet has found and removed the dashboard named with \""
								+ dashboardName + "\"");
				driver.takeScreenShot();
				break;
			}
		}

		if (hasFound == false) {
			throw new NoSuchElementException("DashboardBuilderUtil.removeDashboardFromSet can not find the dashboard named with \""
					+ dashboardName + "\"");
		}

		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.removeDashboardFromSet completed");
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

	public static void search(WebDriver driver, String searchString) throws Exception
	{
		DashboardHomeUtil.search(driver, searchString);
	}

	public static void selectDashboard(WebDriver driver, String dashboardName) throws Exception
	{
		DashboardHomeUtil.selectDashboard(driver, dashboardName);
	}

	public static void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility) throws Exception
	{
		DashboardBuilderUtil.showWidgetTitle(driver, widgetName, 0, visibility);
	}

	public static void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility) throws Exception
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.showWidgetTitle started for widgetName=" + widgetName + ", index=" + index
				+ ", visibility=" + visibility);
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		driver.waitForElementPresent(DashBoardPageId.BuilderTilesEditArea);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId.BuilderTilesEditArea)));
		WaitUtil.waitForPageFullyLoaded(driver);

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
		DashboardHomeUtil.sortBy(driver, option);
	}

	public static Boolean toggleHome(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.asHomeOption started");

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click("css="+DashBoardPageId.BuilderOptionsMenuLocator);
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

	public static Boolean toggleHomeDashboardSet(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.toggleHomeOptionDashboardSet started");
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

	public static Boolean toggleShareDashboard(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.sharedashboard started");
		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		WaitUtil.waitForPageFullyLoaded(driver);

		WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
		WebElement editOption = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId.BuilderOptionsMenuLocator));
		editOption.click();
		driver.takeScreenShot();

		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.click("css="+DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.takeScreenShot();

		driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerEditSingleDBShareCSS);
		driver.click("css=" + DashBoardPageId.RightDrawerEditSingleDBShareCSS);

		boolean shareFlagElem = driver.isDisplayed("css=" + DashBoardPageId.RightDrawerEditSingleDBToShareSelectedCSS);
		if (shareFlagElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerEditSingleDBNotShareCSS);
			driver.click("css=" + DashBoardPageId.RightDrawerEditSingleDBNotShareCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil unshare dashboardset");
			return false;
		}
		else {
			driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerEditSingleDBToShareCSS);
			driver.click("css=" + DashBoardPageId.RightDrawerEditSingleDBToShareCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil share dashboardset");
			return true;
		}
	}

	/**
	 *
	 * @param driver
	 * @return true if toggle to share the dashboardset
	 * @throws Exception
     */
	public static Boolean toggleShareDashboardset(WebDriver driver) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.toggleShareDashboardset started");
		WaitUtil.waitForPageFullyLoaded(driver);

		//open the edit/share dialog
		driver.getLogger().info("DashboardBuilderUtil.toggleShareDashboardset open share/edit dialog");
		driver.waitForElementPresent("id="+DashBoardPageId.DashboardsetOptionsMenuID);
		driver.click("id="+DashBoardPageId.DashboardsetOptionsMenuID);
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
		driver.click("css=" + DashBoardPageId.DashboardsetOptionsEditCSS);
		driver.takeScreenShot();

		//open share collapsible
		boolean editShareElem = driver.isDisplayed("css=" + DashBoardPageId.DashboardsetOptionsShareContentCSS);
		if (!editShareElem) {
			driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsShareCollapsibleCSS);
			driver.click("css=" + DashBoardPageId.DashboardsetOptionsShareCollapsibleCSS);
		}
		driver.getLogger().info("DashboardBuilderUtil.toggleShareDashboardset sharing form has opened");

		//toggle share dashboardset
		driver.waitForElementPresent("css=" + DashBoardPageId.DashboardsetOptionsUnshareCSS);
		boolean isSharedSelected = driver.isDisplayed("css=" + DashBoardPageId.DashboardsetOptionsShareStatusCSS);
		if (isSharedSelected) {
			driver.click("css=" + DashBoardPageId.DashboardsetOptionsUnshareCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil unshare dashboardset");
			return false;
		} else {
			driver.click("css=" + DashBoardPageId.DashboardsetOptionsShareCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil share dashboardset");
			return true;
		}
	}

	public static void selectDashboardInsideSet(WebDriver driver,String dashboardName) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.selectDashboardInsideSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
				By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.selectDashboardInsideSet: the dashboard navigator container is not found");
		}

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.takeScreenShot();

		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetNavsCSS));
		if (navs == null || navs.size() == 0) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.selectDashboardInsideSet: the dashboard navigators is not found");
		}

		for (WebElement nav : navs) {
			if (nav.getAttribute("data-dashboard-name-in-set") != null
					&& nav.getAttribute("data-dashboard-name-in-set").trim().equals(dashboardName)) {
				driver.getLogger().info("DashboardBuilderUtil.selectDashboardInsideSet has found the corresponding name");
				nav.click();
				break;
			}
		}
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.selectDashboardInsideSet completed");
	}

	public static boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardName + "\", description=\"" + description
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
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected dashboard name is "
							+ dashboardName + ", actual dashboard name is " + realName);
			return false;
		}

		driver.waitForElementPresent(DashBoardPageId.BuilderDescriptionTextLocator);
		String realDesc = driver.getElement(DashBoardPageId.BuilderDescriptionTextLocator).getAttribute("title");
		if (description == null || description.equals("")) {
			if (realDesc != null && !realDesc.trim().equals("")) {
				driver.getLogger().info(
						"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
								+ description + ", actual dashboard description is " + realDesc);
				return false;
			}
		}
		else {
			if (!description.equals(realDesc)) {
				driver.getLogger().info(
						"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
								+ description + ", actual dashboard description is " + realDesc);
				return false;
			}
		}

		boolean actualTimeSelectorShown = driver.isDisplayed(DashBoardPageId.BuilderDateTimePickerLocator);
		if (actualTimeSelectorShown != showTimeSelector) {
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected showTimeSelector is "
							+ showTimeSelector + ", actual dashboard showTimeSelector is " + actualTimeSelectorShown);
			return false;
		}

		driver.getLogger().info("DashboardBuilderUtil.verifyDashboard compelted and returns true");
		return true;
	}

	public static boolean verifyDashboardInsideSet(WebDriver driver, String dashboardName)
	{
		driver.getLogger().info("DashboardBuilderUtil.verifyDashboardInsideSet started for name=\"" + dashboardName + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		WebElement dashboardSetContainer = driver.getWebDriver().findElement(
				By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
		if (dashboardSetContainer == null) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.verifyDashboardInsideSet: the dashboard navigator container is not found");
		}

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.takeScreenShot();

		boolean hasFound = false;
		List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetNavsCSS));
		if (navs == null || navs.size() == 0) {
			throw new NoSuchElementException(
					"DashboardBuilderUtil.verifyDashboardInsideSet: the dashboard navigators is not found");
		}

		for (WebElement nav : navs) {
			if (nav.getAttribute("data-dashboard-name-in-set") != null
					&& nav.getAttribute("data-dashboard-name-in-set").trim().equals(dashboardName)) {
				hasFound = true;
				break;
			}
		}

		if (hasFound) {
			driver.getLogger().info("DashboardBuilderUtil.verifyDashboardInsideSet name=\"" + dashboardName + "\" has found");
		}
		else {
			driver.getLogger().info("DashboardBuilderUtil.verifyDashboardInsideSet name=\"" + dashboardName + "\" has not found");
		}
		driver.getLogger().info("DashboardBuilderUtil.removeDashboardFromSet completed");
		return hasFound;
	}

	public static boolean verifyDashboardSet(WebDriver driver, String dashboardSetName)
	{
		driver.getLogger().info("DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardSetName + "\"");
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
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboardSet compelted and returns false. Expected dashboard set name is "
							+ dashboardSetName + ", actual dashboard set name is " + realName);
			return false;
		}

		driver.getLogger().info("DashboardBuilderUtil.verifyDashboardSet compelted and returns true");
		return true;
	}

	public static boolean verifyWidget(WebDriver driver, String widgetName)
	{
		return DashboardBuilderUtil.verifyWidget(driver, widgetName, 0);
	}

	public static boolean verifyWidget(WebDriver driver, String widgetName, int index)
	{
		driver.getLogger().info(
				"DashboardBuilderUtil.verifyWidget started for name=\"" + widgetName + "\", index=\"" + index + "\"");
		Validator.notEmptyString("dashboardName", widgetName);

		WebElement we = null;
		try {
			WaitUtil.waitForPageFullyLoaded(driver);
			we = DashboardBuilderUtil.getTileTitleElement(driver, widgetName, index);
		}
		catch (NoSuchElementException e) {
			driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns false");
			return false;
		}
		if (we == null) {
			driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns false");
			return false;
		}

		driver.getLogger().info("DashboardBuilderUtil.verifyWidget compelted and returns true");
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
		driver.getLogger().info("Start to find widget with widgetName=" + widgetName + ", index=" + index);
		WebElement widgetTitle = DashboardBuilderUtil.getTileTitleElement(driver, widgetName, index);
		if (widgetTitle == null) {
			throw new NoSuchElementException("Widget with title=" + widgetName + ", index=" + index + " is not found");
		}
		driver.getLogger().info("Found widget with name=" + widgetName + ", index =" + index + " before opening widget link");
		WebElement widgetDataExplore = widgetTitle.findElement(By.xpath(DashBoardPageId.BuilderTileDataExploreLocator));
		if (widgetDataExplore == null) {
			throw new NoSuchElementException("Widget data explorer link for title=" + widgetName + ", index=" + index
					+ " is not found");
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
			driver.click("css=" + DashBoardPageId.RightDrawerTogglePencilBtnCSS);
			if(DashboardBuilderUtil.isRightDrawerVisible(driver) == true){
				driver.click("css=" + DashBoardPageId.RightDrawerTogglePencilBtnCSS);
			}
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

	//to open right drawer and show build dashboard
	private static void showRightDrawer(WebDriver driver,String buttonName) throws Exception
	{
		driver.waitForElementPresent("css=" + DashBoardPageId.RightDrawerCSS);
		if (DashboardBuilderUtil.isRightDrawerVisible(driver) != false) {
			hideRightDrawer(driver);
		}
		switch (buttonName){
			case PENCIL:
				driver.click("css=" + DashBoardPageId.RightDrawerTogglePencilBtnCSS);
				break;
			case WRENCH:
				driver.click("css=" + DashBoardPageId.RightDrawerToggleWrenchBtnCSS);
				break;
			default:
				driver.takeScreenShot();
				return;
		}
		driver.getLogger().info("[DashboardBuilderUtil] triggered showRightDrawer and show build dashboard.");
		WaitUtil.waitForPageFullyLoaded(driver);
		driver.takeScreenShot();
	}

	private static void duplicateDashboardCommonUse(WebDriver driver, String name, String descriptions ,String operationName) throws Exception
	{
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		Validator.notNull("duplicatename", name);
		Validator.notEmptyString("duplicatename", name);
		Validator.notEmptyString("duplicatedescription", descriptions);

		Validator.notEmptyString("duplicatename", name);

		driver.getLogger().info("DashboardBuilderUtil.duplicate started");
		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		WaitUtil.waitForPageFullyLoaded(driver);
		WebElement visibleContainer=getSelectedDashboardEl(driver);
		WebElement visbleOptionMenu =visibleContainer.findElement(By.cssSelector(DashBoardPageId.BuilderOptionsMenuLocator));
		visbleOptionMenu.click();
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);

		// add to set or not,or no dropdownmenu just add
		switch (operationName){
			case DUP_DASHBOARD_NODSUBMENU:
				driver.click("css=" + DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);
				break;
			case DUP_DASHBOARD_TOSET:
				driver.click("css=" + DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);
				driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsDuplicateToSetCSS);
				driver.click("css="+DashBoardPageId.BuilderOptionsDuplicateToSetCSS);
				break;
			case DUP_SHBOARDSET_NOTTOSET:
				driver.click("css=" + DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);
				driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsDuplicateNotToSetCSS);
				driver.click("css="+DashBoardPageId.BuilderOptionsDuplicateNotToSetCSS);
				break;
		}

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
		driver.getLogger().info("DashboardBuilderUtil.duplicate save button has been focused");

		driver.click("css=" + DashBoardPageId.BuilderOptionsDuplicateSaveCSS);
		driver.takeScreenShot();
		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.duplicate completed");

	}

	/**
	 * @param driver
	 * @return current visible dashboard container
     */
	private static WebElement getSelectedDashboardEl(WebDriver driver) {
		List<WebElement> dashboardContainers = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.DashboardSetContainerCSS));
		for (WebElement container : dashboardContainers) {
			if(false == container.getCssValue("display").equals("none")){
				driver.getLogger().info("[DashboardBuilderUtil] triggered getSelectedDashboardEl and get the dashboard successfully!");
				return container;
			}
		}

		driver.getLogger().info("[DashboardBuilderUtil] triggered getSelectedDashboardEl and fail to find visible dashboard!");
		return null;

	}
}
