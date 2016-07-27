package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DelayedPressEnterThread;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DashboardBuilderUtil_190 extends DashboardBuilderUtil_175
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

    @Override
    public void createDashboardInsideSet(WebDriver driver, String name, String descriptions) throws Exception
    {
        driver.waitForElementPresent("id="+DashBoardPageId_190.DashboardsetOptionsMenuID);
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.getLogger().info("DashboardBuilderUtil.createDashboardInsideSet : " + name);
        Validator.notEmptyString("name", name);
        driver.click("css="+DashBoardPageId_190.DASHBOARD_HOME_CREATINSET_BUTTON);

        if (name != null && !name.isEmpty()) {
            driver.sendKeys("id="+DashBoardPageId_190.DashBoardNameBoxID, name);
        }
        if (descriptions != null && !descriptions.isEmpty()) {
            driver.sendKeys("id="+DashBoardPageId_190.DashBoardDescBoxID, descriptions);
        }
        driver.takeScreenShot();
        driver.click("id="+DashBoardPageId_190.DashOKButtonID);

        String newTabLocator = ".other-nav[data-dashboard-name-in-set='"+name+"']";
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(newTabLocator)));
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.getLogger().info("DashboardBuilderUtil.createDashboardInsideSet completed");
    }

    @Override
    public void addNewDashboardToSet(WebDriver driver, String dashboardName) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet started for name=\"" + dashboardName + "\"");
        Validator.notEmptyString("dashboardName", dashboardName);

        WebElement dashboardSetContainer = driver.getWebDriver().findElement(
                By.cssSelector(DashBoardPageId_190.DashboardSetNavsContainerCSS));
        if (dashboardSetContainer == null) {
            throw new NoSuchElementException(
                    "DashboardBuilderUtil.addNewDashboardToSet: the dashboard navigator container is not found");
        }

        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
        driver.takeScreenShot();

        boolean isSelectionTabExist = false;
        List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DashboardSetNavsCSS));
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
            WebElement addNav = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId_190.DashboardSetNavAddBtnCSS));
            if (addNav == null) {
                throw new NoSuchElementException(
                        "DashboardBuilderUtil.addNewDashboardToSet: the dashboard 'add' button  is not found");
            }
            addNav.click();
            WaitUtil.waitForPageFullyLoaded(driver); // wait for all dashboards loaded
        }

        driver.takeScreenShot();
        DashboardHomeUtil.selectDashboard(driver, dashboardName);
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.getLogger().info(
                "DashboardBuilderUtil.addNewDashboardToSet has selected the dashboard named with \"" + dashboardName + "\"");

        driver.takeScreenShot();
        driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet completed and returns true");
    }

    @Override
    public void addWidgetToDashboard(WebDriver driver, String searchString) throws Exception
    {
        Validator.notNull("widgetName", searchString);
        Validator.notEmptyString("widgetName", searchString);

        if (searchString == null) {
            return;
        }

        By locatorOfKeyEl = By.cssSelector(DashBoardPageId_190.RightDrawerCSS);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfKeyEl));
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.getLogger().info("[DashboardHomeUtil] call addWidgetToDashboard with search string as " + searchString);

        //show right drawer if it is hidden
        showRightDrawer(driver, WRENCH);

        WebElement searchInput = driver.getElement("css=" + DashBoardPageId_190.RightDrawerSearchInputCSS);
        // focus to search input box
        wait.until(ExpectedConditions.elementToBeClickable(searchInput));

        Actions actions = new Actions(driver.getWebDriver());
        actions.moveToElement(searchInput).build().perform();
        searchInput.clear();
        actions.moveToElement(searchInput).build().perform();
        driver.click("css=" + DashBoardPageId_190.RightDrawerSearchInputCSS);
        searchInput.sendKeys(searchString);
        driver.takeScreenShot();
        //verify input box value
        Assert.assertEquals(searchInput.getAttribute("value"), searchString);

        WebElement searchButton = driver.getElement("css=" + DashBoardPageId_190.RightDrawerSearchButtonCSS);
        driver.waitForElementPresent("css=" + DashBoardPageId_190.RightDrawerSearchButtonCSS);
        searchButton.click();
        //wait for ajax resolved
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.takeScreenShot();

        driver.getLogger().info("[DashboardHomeUtil] start to add widget from right drawer");
        List<WebElement> matchingWidgets = driver.getWebDriver().findElements(
                By.cssSelector(DashBoardPageId_190.RightDrawerWidgetCSS));
        if (matchingWidgets == null || matchingWidgets.size() == 0) {
            throw new NoSuchElementException("Right drawer widget for search string =" + searchString + " is not found");
        }

        //drag and drop not working
        //      WebElement tilesContainer = driver.getElement("css=" + DashBoardPageId_190.RightDrawerWidgetToAreaCSS);
        //      CommonActions.dragAndDropElement(driver, matchingWidgets.get(0), tilesContainer);

        // focus to  the first matching  widget
        driver.getLogger().info("Focus on the searched widget");
        driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.TAB);
        driver.takeScreenShot();

        // check if the searched widget has the focus
        driver.getLogger().info("Check if the searched widget get the focus");

        if (driver.getWebDriver().switchTo().activeElement()
                .equals(driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId_190.RightDrawerWidgetCSS)))) {
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
    public void deleteDashboard(WebDriver driver) {
        driver.getLogger().info("DashboardBuilderUtil.deleteDashboard started");
        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        WaitUtil.waitForPageFullyLoaded(driver);

        WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
        WebElement editOption = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId_190.BuilderOptionsMenuLocator));
        editOption.click();
        driver.takeScreenShot();

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.takeScreenShot();

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsDeleteLocator);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsDeleteLocator);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.BuilderDeleteDialogLocator);
        driver.click(DashBoardPageId_190.BuilderDeleteDialogDeleteBtnLocator);
        driver.takeScreenShot();
        driver.waitForElementPresent(DashBoardPageId_190.SearchDashboardInputLocator);

        driver.getLogger().info("DashboardBuilderUtil.deleteDashboard completed");
    }

    @Override
    public void deleteDashboardSet(WebDriver driver)
    {
        driver.getLogger().info("DashboardBuilderUtil.deleteDashboardSet started");

        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(DashBoardPageId_190.DashboardsetOptionsMenuID)));
        WaitUtil.waitForPageFullyLoaded(driver);

        //wait for scrollbar to display
        //TODO replace with more reliable way
        try{
            Thread.sleep(5000L);
        }catch(Exception e){
        }

        //open settings menu
        driver.click("id="+DashBoardPageId_190.DashboardsetOptionsMenuID);

        // click edit option
        driver.waitForElementPresent("css=" + DashBoardPageId_190.DashboardsetOptionsEditCSS);
        driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsEditCSS);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetOptionsDeleteLocator);
        driver.click(DashBoardPageId_190.DashboardSetOptionsDeleteLocator);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetDeleteDialogLocator);
        driver.click(DashBoardPageId_190.DashboardSetDeleteDialogDeleteBtnLocator);
        driver.takeScreenShot();
        // wait until page redirect to dashboard home
        driver.waitForElementPresent(DashBoardPageId_190.SearchDashboardInputLocator);

        driver.getLogger().info("DashboardBuilderUtil.deleteDashboardSet completed");
    }

    @Override
    public void deleteDashboardInsideSet(WebDriver driver)
    {
        driver.getLogger().info("DashboardBuilderUtil.deleteDashboardInsideSet started");
        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        WaitUtil.waitForPageFullyLoaded(driver);

        WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
        WebElement editOption = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId_190.BuilderOptionsMenuLocator));
        editOption.click();
        driver.takeScreenShot();

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.takeScreenShot();

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsDeleteLocator);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsDeleteLocator);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.BuilderDeleteDialogLocator);
        driver.click(DashBoardPageId_190.BuilderDeleteDialogDeleteBtnLocator);
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.takeScreenShot();
        driver.getLogger().info("DashboardBuilderUtil.deleteDashboardInsideSet completed");
    }
    
    @Override
    public void duplicateDashboard(WebDriver driver, String name, String descriptions) throws Exception {
        duplicateDashboardCommonUse(driver,name, descriptions ,DUP_DASHBOARD_NODSUBMENU);
    }

    @Override
    public void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions,boolean addToSet) throws Exception {
        if(addToSet){
            duplicateDashboardCommonUse(driver,name, descriptions ,DUP_DASHBOARD_TOSET);
        }else{
            duplicateDashboardCommonUse(driver,name, descriptions ,DUP_SHBOARDSET_NOTTOSET);
        }
    }

    @Override
    public void editDashboard(WebDriver driver, String name, String descriptions, Boolean toShowDscptn) throws Exception
    {
        Validator.notNull("editname", name);
        Validator.notEmptyString("editname", name);

        driver.getLogger().info("DashboardBuilderUtil.edit started");
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        WaitUtil.waitForPageFullyLoaded(driver);

        WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
        WebElement editOption = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId_190.BuilderOptionsMenuLocator));
        editOption.click();
        driver.takeScreenShot();

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.takeScreenShot();

        //wait for 900s
        By locatorOfEditDesEl = By.cssSelector(DashBoardPageId_190.BuilderOptionsEditDescriptionCSS);
//		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);

        //add name and description
        driver.getElement("css=" + DashBoardPageId_190.BuilderOptionsEditNameCSS).clear();
        driver.click("css=" + DashBoardPageId_190.BuilderOptionsEditNameCSS);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
        driver.sendKeys("css=" + DashBoardPageId_190.BuilderOptionsEditNameCSS, name);

        driver.getElement("css=" + DashBoardPageId_190.BuilderOptionsEditDescriptionCSS).clear();
        driver.click("css=" + DashBoardPageId_190.BuilderOptionsEditDescriptionCSS);

        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
        driver.sendKeys("css=" + DashBoardPageId_190.BuilderOptionsEditDescriptionCSS, descriptions);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
        driver.takeScreenShot();

        driver.getElement("css=" + DashBoardPageId_190.BuilderOptionsEditShowDescriptionCSS);
        if(toShowDscptn) {
            driver.check("css=" + DashBoardPageId_190.BuilderOptionsEditShowDescriptionCSS);
        }else{
            driver.uncheck("css=" + DashBoardPageId_190.BuilderOptionsEditShowDescriptionCSS);
        }

        driver.takeScreenShot();
        driver.getLogger().info("DashboardBuilderUtil.edit complete");
    }

    /**
     * @deprecated
     * @param driver
     * @param name
     * @param descriptions
     * @throws Exception
     */
    public void EditDashboard_targetselctor(WebDriver driver, String name, String descriptions) throws Exception
    {
        Validator.notNull("editname", name);
        Validator.notEmptyString("editname", name);

        driver.getLogger().info("DashboardBuilderUtil.edit started");
        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.click("css=" + DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.takeScreenShot();
        driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsEditNameCSS);

        //wait for 900s
        By locatorOfEditDesEl = By.cssSelector(DashBoardPageId_190.BuilderOptionsEditDescriptionCSS);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);

        //add name and description
        driver.getElement("css=" + DashBoardPageId_190.BuilderOptionsEditNameCSS).clear();
        driver.click("css=" + DashBoardPageId_190.BuilderOptionsEditNameCSS);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
        driver.sendKeys("css=" + DashBoardPageId_190.BuilderOptionsEditNameCSS, name);

        driver.getElement("css=" + DashBoardPageId_190.BuilderOptionsEditDescriptionCSS).clear();
        driver.click("css=" + DashBoardPageId_190.BuilderOptionsEditDescriptionCSS);

        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
        driver.sendKeys("css=" + DashBoardPageId_190.BuilderOptionsEditDescriptionCSS, descriptions);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
        driver.takeScreenShot();

        //selctor filetr entity
        driver.waitForElementPresent(DashBoardPageId_190.EntityfilterLocator);
        driver.click(DashBoardPageId_190.EntityfilterLocator);

        //press ok button
        driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsEditSaveCSS);
        driver.click("css=" + DashBoardPageId_190.BuilderOptionsEditSaveCSS);
        driver.takeScreenShot();
        driver.getLogger().info("DashboardBuilderUtil.edit complete");
    }

    @Override
    public void editDashboardSet(WebDriver driver, String name, String descriptions) throws Exception
    {
        Validator.notNull("editname", name);
        Validator.notEmptyString("editname", name);

        driver.waitForElementPresent("id="+DashBoardPageId_190.DashboardsetOptionsMenuID);
        driver.getLogger().info("DashboardBuilderUtil.editDashboardSet started");
        WaitUtil.waitForPageFullyLoaded(driver);
        
        //wait for scrollbar to display
        //TODO replace with more reliable way
        Thread.sleep(5000L);

        //open settings menu
        driver.click("id="+DashBoardPageId_190.DashboardsetOptionsMenuID);

        // click edit option
        driver.waitForElementPresent("css=" + DashBoardPageId_190.DashboardsetOptionsEditCSS);
        driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsEditCSS);
        driver.takeScreenShot();

        By locatorOfEditDesEl = By.cssSelector(DashBoardPageId_190.DashboardsetOptionsEditNameCSS);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
        driver.takeScreenShot();

        //edit name
        driver.getLogger().info("DashboardBuilderUtil.editDashboardSet start editing name");
        driver.getElement("css=" + DashBoardPageId_190.DashboardsetOptionsEditNameCSS).clear();
        driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsEditNameCSS);
        driver.sendKeys("css=" + DashBoardPageId_190.DashboardsetOptionsEditNameCSS, name);

        //edit description
        driver.getLogger().info("DashboardBuilderUtil.editDashboardSet start editing description");
        driver.getElement("css=" + DashBoardPageId_190.DashboardsetOptionsEditDescriptionCSS).clear();
        driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsEditDescriptionCSS);
        driver.sendKeys("css=" + DashBoardPageId_190.DashboardsetOptionsEditDescriptionCSS, descriptions);
        driver.takeScreenShot();

        //hide settings panel
        hideRightDrawer(driver);
    }

    @Override
    public  Boolean favoriteOption(WebDriver driver) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.favoriteOption started");

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.takeScreenShot();
        boolean favoriteElem = driver.isDisplayed("css=" + DashBoardPageId_190.BuilderOptionsFavoriteLocatorCSS);
        if (favoriteElem) {
            driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsFavoriteLocatorCSS);
            driver.click("css=" + DashBoardPageId_190.BuilderOptionsFavoriteLocatorCSS);
            driver.takeScreenShot();
            driver.getLogger().info("DashboardBuilderUtil add favorite completed");
            return true;
        }
        else {
            driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsRemoveFavoriteLocatorCSS);
            driver.click("css=" + DashBoardPageId_190.BuilderOptionsRemoveFavoriteLocatorCSS);
            driver.takeScreenShot();
            driver.getLogger().info("DashboardBuilderUtil remove favorite completed");
            return false;
        }
    }

    @Override
    public  Boolean favoriteOptionDashboardSet(WebDriver driver) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.favoriteOptionDashboardSet started");
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.waitForElementPresent("id=" + DashBoardPageId_190.DashboardsetOptionsMenuID);
        driver.click("id=" + DashBoardPageId_190.DashboardsetOptionsMenuID);

        boolean dashboardsetFavoriteElem = driver.isDisplayed("css=" + DashBoardPageId_190.DashboardsetOptionsRemoveFavoriteCSS);
        driver.waitForElementPresent("css=" + DashBoardPageId_190.DashboardsetOptionsfavoriteCSS);
        driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsfavoriteCSS);
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

    @Override
    public void gridView(WebDriver driver) throws Exception
    {
        DashboardHomeUtil.gridView(driver);
    }

    @Override
    public boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked started for refreshSettings=" + refreshSettings);

        Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
                REFRESH_DASHBOARD_SETTINGS_5MIN);

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(DashBoardPageId_190.BuilderOptionsMenuLocator)));
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.BuilderOptionsAutoRefreshLocator);
        driver.click(DashBoardPageId_190.BuilderOptionsAutoRefreshLocator);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.BuilderOptionsAutoRefreshOffLocator);
        if (REFRESH_DASHBOARD_SETTINGS_OFF.equals(refreshSettings)) {
            boolean checked = driver.isDisplayed(DashBoardPageId_190.BuilderAutoRefreshOffSelectedLocator);
            driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked completed, return result is " + checked);
            return checked;
        }
        else {//REFRESH_DASHBOARD_PARAM_5MIN:
            boolean checked = driver.isDisplayed(DashBoardPageId_190.BuilderAutoRefreshOn5MinSelectedLocator);
            driver.getLogger().info("DashboardBuilderUtil.isRefreshSettingChecked completed, return result is " + checked);
            return checked;
        }
    }

    @Override
    public boolean isRefreshSettingCheckedForDashbaordSet(WebDriver driver, String refreshSettings)
    {
        driver.getLogger().info(
                "DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet started for refreshSettings=" + refreshSettings);

        Validator.fromValidValues("refreshDashboardSet", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
                REFRESH_DASHBOARD_SETTINGS_5MIN);

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetOptionBtn);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.DashboardSetOptionBtn)));
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetOptionBtn);
        driver.click(DashBoardPageId_190.DashboardSetOptionBtn);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetOptionsAutoRefreshLocator);
        driver.click(DashBoardPageId_190.DashboardSetOptionsAutoRefreshLocator);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetOptionsAutoRefreshOffLocator);
        driver.takeScreenShot();
        if (REFRESH_DASHBOARD_SETTINGS_OFF.equals(refreshSettings)) {
            boolean checked = driver.isDisplayed(DashBoardPageId_190.DashboardSetAutoRefreshOffSelectedLocator);
            driver.getLogger().info(
                    "DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
            return checked;
        }
        else {// REFRESH_DASHBOARD_SETTINGS_5MIN:
            boolean checked = driver.isDisplayed(DashBoardPageId_190.DashboardSetAutoRefreshOn5MinSelectedLocator);
            driver.getLogger().info(
                    "DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet completed, return result is " + checked);
            return checked;
        }
    }

    //	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
    //	{
    //		driver = webDriver;
    //	}

    @Override
    public void listView(WebDriver driver) throws Exception
    {
        DashboardHomeUtil.listView(driver);
    }

    @Override
    public void openWidget(WebDriver driver, String widgetName) throws Exception
    {
        openWidget(driver, widgetName, 0);
    }
    
    @Override
    public void setEntitySupport(WebDriver driver, String mode) throws Exception
    {
    	driver.getLogger().info("DashboardBuilderUtil.setEntitySupport started, the param of mode is: " + mode);
    	driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsMenuLocator);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		OpenFiltersInRightPanel(driver);
		
		driver.getElement("css=" + DashBoardPageId_190.RightDrawerEditDBEntitySupportCSS);
		if("MULTIPLE".equals(mode)) {
			driver.check("css=" + DashBoardPageId_190.RightDrawerEditDBEntitySupportCSS);
			driver.getLogger().info("DashboardBuilderUtil.setEntitySupport checked entity support");
		}else {
			driver.uncheck("css=" + DashBoardPageId_190.RightDrawerEditDBEntitySupportCSS);
			driver.getLogger().info("DashboardBuilderUtil.setEntitySupport unchecked entity support");
		}
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.setEntitySupport finished!!!!");
    }

    @Override
    public boolean showEntityFilter(WebDriver driver, boolean showEntityFilter) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.showEntityFilter started, the param of showEntityFilter is: " + showEntityFilter);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsMenuLocator);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		OpenFiltersInRightPanel(driver);
		
		if(showEntityFilter) {
			driver.waitForElementPresent("css="+DashBoardPageId_190.RightDrawerEditDBEnableEntityFilterCSS);
			driver.click("css=" + DashBoardPageId_190.RightDrawerEditDBEnableEntityFilterCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil show entity filter finished!!!");
			return true;
		}else {
			driver.waitForElementPresent("css="+DashBoardPageId_190.RightDrawerEditDBDisableEntityFilterCSS);
			driver.click("css=" + DashBoardPageId_190.RightDrawerEditDBDisableEntityFilterCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil hide entity filter finished!!!");
			return false;
		}
	}
	
    @Override
	public boolean showTimeRangeFilter(WebDriver driver, boolean showTimeRangeFilter) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.showTimeRangeFilter started, the param of showTimeRangeFilter is: " + showTimeRangeFilter);
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsMenuLocator);
		WaitUtil.waitForPageFullyLoaded(driver);
		
		OpenFiltersInRightPanel(driver);
		
		if(showTimeRangeFilter) {
			driver.waitForElementPresent("css="+DashBoardPageId_190.RightDrawerEditDBEnableTimeRangeFilterCSS);
			driver.click("css=" + DashBoardPageId_190.RightDrawerEditDBEnableTimeRangeFilterCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil show time range filter finished!!!");
			return true;
		}else {
			driver.waitForElementPresent("css="+DashBoardPageId_190.RightDrawerEditDBDisableTimeRangeFilterCSS);
			driver.click("css=" + DashBoardPageId_190.RightDrawerEditDBDisableTimeRangeFilterCSS);
			driver.takeScreenShot();
			driver.getLogger().info("DashboardBuilderUtil hide time range filter finished!!!");
			return false;
		}
	}
    
    private void OpenFiltersInRightPanel(WebDriver driver)
	{
		driver.getLogger().info("DashboardBuilderUtil.openFiltersInRightPanel start");
		//click Options to open Options menu
		WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
		WebElement editOption = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId_190.BuilderOptionsMenuLocator));
		editOption.click();
		driver.takeScreenShot();

		//click Options->edit to open right panel
		driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
		driver.click("css=" + DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
		driver.takeScreenShot();
				
		//click Right panel->filters
		driver.waitForElementPresent("css=" + DashBoardPageId_190.RightDrawerEditDBFilterCSS);
		driver.click("css=" + DashBoardPageId_190.RightDrawerEditDBFilterCSS);
		driver.takeScreenShot();
	}

    @Override
    public void openWidget(WebDriver driver, String widgetName, int index) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.openWidget started for widgetName=" + widgetName + ", index=" + index);

        Validator.notEmptyString("widgetName", widgetName);
        Validator.equalOrLargerThan0("index", index);
        clickTileOpenInDataExplorerButton(driver, widgetName, index);

        driver.getLogger().info("DashboardBuilderUtil.openWidget completed");
    }

    @Override
    public void printDashboard(WebDriver driver) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil print dashboard started");
        driver.waitForElementPresent("css="+ DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsPrintLocatorCSS);
        driver.takeScreenShot();
        DelayedPressEnterThread thr = new DelayedPressEnterThread("DelayedPressEnterThread", 5000);
        driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId_190.BuilderOptionsPrintLocatorCSS)).click();
        driver.takeScreenShot();
        driver.getLogger().info("DashboardBuilderUtil print completed");
    }

    @Override
    public void printDashboardSet(WebDriver driver) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil print dashboard set started");
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.waitForElementPresent("id=" + DashBoardPageId_190.DashboardsetOptionsMenuID);
        int waitTime = 5000;

        //click all tabs
        WebElement dashboardSetContainer = driver.getWebDriver().findElement(
                By.cssSelector(DashBoardPageId_190.DashboardSetNavsContainerCSS));
        if (dashboardSetContainer == null) {
            throw new NoSuchElementException(
                    "DashboardBuilderUtil.printDashboardSet: the dashboard navigator container is not found");
        }

        List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DashboardSetNavsCSS));
        if (navs == null || navs.size() == 0) {
            throw new NoSuchElementException("DashboardBuilderUtil.printDashboardSet: the dashboard navigators is not found");
        }
        for (WebElement nav : navs) {
            nav.click();
            WaitUtil.waitForPageFullyLoaded(driver);
            driver.takeScreenShot();
            driver.getLogger().info("DashboardBuilderUtil.printDashboardSet has click on the dashboard selection tab named");
        }

        //click print
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.waitForElementPresent("id=" + DashBoardPageId_190.DashboardsetOptionsMenuID);
        driver.click("id=" + DashBoardPageId_190.DashboardsetOptionsMenuID);
        driver.waitForElementPresent("css=" + DashBoardPageId_190.DashboardsetOptionsPrintCSS);
        DelayedPressEnterThread thr = new DelayedPressEnterThread("DelayedPressEnterThread", waitTime);
        driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId_190.DashboardsetOptionsPrintCSS)).click();
        //have to use thread sleep to wait for the print window(windows dialog) to appear
        Thread.sleep(waitTime);
        driver.getLogger().info("DashboardBuilderUtil.printDashboardSet: print set completed");
    }

    @Override
    public void refreshDashboard(WebDriver driver, String refreshSettings)
    {
        driver.getLogger().info("DashboardBuilderUtil.refreshDashboard started for refreshSettings=" + refreshSettings);

        Validator.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
                REFRESH_DASHBOARD_SETTINGS_5MIN);

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(DashBoardPageId_190.BuilderOptionsMenuLocator)));
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.BuilderOptionsAutoRefreshLocator);
        driver.click(DashBoardPageId_190.BuilderOptionsAutoRefreshLocator);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.BuilderOptionsAutoRefreshOffLocator);
        switch (refreshSettings) {
            case REFRESH_DASHBOARD_SETTINGS_OFF:
                driver.check(DashBoardPageId_190.BuilderOptionsAutoRefreshOffLocator);
                driver.waitForElementPresent(DashBoardPageId_190.BuilderAutoRefreshOffSelectedLocator);
                driver.takeScreenShot();
                break;
            case REFRESH_DASHBOARD_SETTINGS_5MIN:
                driver.check(DashBoardPageId_190.BuilderOptionsAutoRefreshOn5MinLocator);
                driver.waitForElementPresent(DashBoardPageId_190.BuilderAutoRefreshOn5MinSelectedLocator);
                driver.takeScreenShot();
                break;
        }
        driver.getLogger().info("DashboardBuilderUtil.refreshDashboard completed");
    }

    @Override
    public void refreshDashboardSet(WebDriver driver, String refreshSettings)
    {
        driver.getLogger().info("DashboardBuilderUtil.refreshDashboardSet started for refreshSettings=" + refreshSettings);

        Validator.fromValidValues("refreshDashboardSet", refreshSettings, REFRESH_DASHBOARD_SETTINGS_OFF,
                REFRESH_DASHBOARD_SETTINGS_5MIN);

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetOptionBtn);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DashBoardPageId_190.DashboardSetOptionBtn)));
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetOptionBtn);
        driver.click(DashBoardPageId_190.DashboardSetOptionBtn);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetOptionsAutoRefreshLocator);
        driver.click(DashBoardPageId_190.DashboardSetOptionsAutoRefreshLocator);
        driver.takeScreenShot();

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetOptionsAutoRefreshOffLocator);
        switch (refreshSettings) {
            case REFRESH_DASHBOARD_SETTINGS_OFF:
                driver.check(DashBoardPageId_190.DashboardSetOptionsAutoRefreshOffLocator);
                break;
            case REFRESH_DASHBOARD_SETTINGS_5MIN:
                driver.check(DashBoardPageId_190.DashboardSetOptionsAutoRefreshOn5MinLocator);
                break;
        }
        driver.getLogger().info("DashboardBuilderUtil.refreshDashboardSet completed");
    }

    @Override
    public void removeDashboardFromSet(WebDriver driver, String dashboardName)
    {
        driver.getLogger().info("DashboardBuilderUtil.removeDashboardFromSet started for name=\"" + dashboardName + "\"");
        Validator.notEmptyString("dashboardName", dashboardName);
        WaitUtil.waitForPageFullyLoaded(driver);

        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(DashBoardPageId_190.DashboardSetNavsContainerCSS)));
        driver.takeScreenShot();

        WebElement targetTab = null;
        List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DashboardSetNavsCSS));
        if (navs == null || navs.size() == 0) {
            throw new NoSuchElementException("DashboardBuilderUtil.removeDashboardFromSet: the dashboard navigators is not found");
        }

        for (int i = 0; i < navs.size(); i++) {
            WebElement nav = navs.get(i);
            if (nav.getAttribute("data-tabs-name").trim().equals(dashboardName)) {
                targetTab = nav;
            }
        }

        if (null == targetTab) {
            throw new NoSuchElementException("DashboardBuilderUtil.removeDashboardFromSet can not find the dashboard named with \""
                    + dashboardName + "\"");
        }

        driver.getLogger().info(
                "DashboardBuilderUtil.removeDashboardFromSet has found and removed the dashboard named with \""
                        + dashboardName + "\"");

        String closeBtnLocator = DashBoardPageId_190.DashboardSetTabNameCSS.replace("_name_",dashboardName);
        driver.waitForElementPresent("css="+closeBtnLocator);
        driver.evalJavascript("$(\""+closeBtnLocator+"\").click()");

        WaitUtil.waitForPageFullyLoaded(driver);
        driver.takeScreenShot();

        WaitUtil.waitForPageFullyLoaded(driver);
        driver.getLogger().info("DashboardBuilderUtil.removeDashboardFromSet completed");
    }

    @Override
    public void removeWidget(WebDriver driver, String widgetName) throws Exception
    {
        removeWidget(driver, widgetName, 0);
    }

    @Override
    public void removeWidget(WebDriver driver, String widgetName, int index) throws Exception
    {
        Validator.notEmptyString("widgetName", widgetName);
        Validator.equalOrLargerThan0("index", index);

        WebElement widgetEl = getWidgetByName(driver, widgetName, index);

        focusOnWidgetHeader(driver, widgetEl);
        driver.takeScreenShot();

        widgetEl.findElement(By.cssSelector(DashBoardPageId_190.ConfigTileCSS)).click();
        driver.click("css=" + DashBoardPageId_190.RemoveTileCSS);
        driver.getLogger().info("Remove the widget");
        driver.takeScreenShot();

    }

    @Override
    public void resizeWidget(WebDriver driver, String widgetName, int index, String resizeOptions) throws Exception
    {
        Validator.notEmptyString("widgetName", widgetName);
        Validator.equalOrLargerThan0("index", index);
        Validator.fromValidValues("resizeOptions", resizeOptions, TILE_NARROWER,
                TILE_WIDER, TILE_SHORTER, TILE_TALLER);

        WebElement widgetEl = getWidgetByName(driver, widgetName, index);

        focusOnWidgetHeader(driver, widgetEl);
        driver.takeScreenShot();

        String tileResizeCSS = null;
        switch (resizeOptions) {
            case TILE_WIDER:
                tileResizeCSS = DashBoardPageId_190.WiderTileCSS;
                break;
            case TILE_NARROWER:
                tileResizeCSS = DashBoardPageId_190.NarrowerTileCSS;
                break;
            case TILE_SHORTER:
                tileResizeCSS = DashBoardPageId_190.ShorterTileCSS;
                break;
            case TILE_TALLER:
                tileResizeCSS = DashBoardPageId_190.TallerTileCSS;
                break;
            default:
                break;
        }
        if (null == tileResizeCSS) {
            return;
        }

        widgetEl.findElement(By.cssSelector(DashBoardPageId_190.ConfigTileCSS)).click();
        driver.click("css=" + tileResizeCSS);
        driver.getLogger().info("Resize the widget");
        driver.takeScreenShot();

    }

    @Override
    public void resizeWidget(WebDriver driver, String widgetName, String resizeOptions) throws Exception
    {
        resizeWidget(driver, widgetName, 0, resizeOptions);
    }

    @Override
    public void saveDashboard(WebDriver driver) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.save started");
        driver.waitForElementPresent("css=" + DashBoardPageId_190.DashboardSaveCSS);
        driver.click("css=" + DashBoardPageId_190.DashboardSaveCSS);
        driver.takeScreenShot();
        driver.getLogger().info("save compelted");
    }

    @Override
    public void search(WebDriver driver, String searchString) throws Exception
    {
        DashboardHomeUtil.search(driver, searchString);
    }

    @Override
    public void selectDashboard(WebDriver driver, String dashboardName) throws Exception
    {
        DashboardHomeUtil.selectDashboard(driver, dashboardName);
    }

    @Override
    public void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility) throws Exception
    {
        showWidgetTitle(driver, widgetName, 0, visibility);
    }

    @Override
    public void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility) throws Exception
    {
        driver.getLogger().info(
                "DashboardBuilderUtil.showWidgetTitle started for widgetName=" + widgetName + ", index=" + index
                        + ", visibility=" + visibility);
        Validator.notEmptyString("widgetName", widgetName);
        Validator.equalOrLargerThan0("index", index);

        driver.waitForElementPresent(DashBoardPageId_190.BuilderTilesEditArea);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.BuilderTilesEditArea)));
        WaitUtil.waitForPageFullyLoaded(driver);

        clickTileConfigButton(driver, widgetName, index);

        if (visibility) {
            if (driver.isDisplayed(DashBoardPageId_190.BuilderTileHideLocator)) {
                driver.takeScreenShot();
                driver.getLogger().info("DashboardBuilderUtil.showWidgetTitle completed as title is shown already");
                return;
            }
            driver.click(DashBoardPageId_190.BuilderTileShowLocator);
            driver.takeScreenShot();
        }
        else {
            if (driver.isDisplayed(DashBoardPageId_190.BuilderTileShowLocator)) {
                driver.takeScreenShot();
                driver.getLogger().info("DashboardBuilderUtil.showWidgetTitle completed as title is hidden already");
                return;
            }
            driver.click(DashBoardPageId_190.BuilderTileHideLocator);
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
    @Override
    public void sortBy(WebDriver driver, String option) throws Exception
    {
        DashboardHomeUtil.sortBy(driver, option);
    }

    @Override
    public Boolean toggleHome(WebDriver driver) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.asHomeOption started");

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        boolean homeElem = driver.isDisplayed("css=" + DashBoardPageId_190.BuilderOptionsSetHomeLocatorCSS);
        driver.takeScreenShot();

        if (homeElem) {
            driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsSetHomeLocatorCSS);
            driver.click("css=" + DashBoardPageId_190.BuilderOptionsSetHomeLocatorCSS);
            driver.takeScreenShot();
            boolean comfirmDialog = driver.isDisplayed("css=" + DashBoardPageId_190.BuilderOptionsSetHomeSaveCSS);
            System.out.println("dialog home " + comfirmDialog);
            if (comfirmDialog) {
                driver.click("css=" + DashBoardPageId_190.BuilderOptionsSetHomeSaveCSS);
                driver.takeScreenShot();
            }
            ;
            driver.getLogger().info("DashboardBuilderUtil set home completed");
            return true;
        }
        else {
            driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsRemoveHomeLocatorCSS);
            driver.click("css=" + DashBoardPageId_190.BuilderOptionsRemoveHomeLocatorCSS);
            driver.takeScreenShot();
            driver.getLogger().info("DashboardBuilderUtil remove home completed");
            return false;
        }

    }

    @Override
    public Boolean toggleHomeDashboardSet(WebDriver driver) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.toggleHomeOptionDashboardSet started");
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.waitForElementPresent("id=" + DashBoardPageId_190.DashboardsetOptionsMenuID);
        driver.click("id=" + DashBoardPageId_190.DashboardsetOptionsMenuID);

        boolean homeElem = driver.isDisplayed("css=" + DashBoardPageId_190.DashboardsetOptionsAddHomeCSS);
        driver.takeScreenShot();
        driver.waitForElementPresent("css=" + DashBoardPageId_190.DashboardsetOptionsHomeCSS);
        driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsHomeCSS);
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

    @Override
    public Boolean toggleShareDashboard(WebDriver driver) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.sharedashboard started");
        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        WaitUtil.waitForPageFullyLoaded(driver);

        WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
        WebElement editOption = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId_190.BuilderOptionsMenuLocator));
        editOption.click();
        driver.takeScreenShot();

        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.click("css="+DashBoardPageId_190.BuilderOptionsEditLocatorCSS);
        driver.takeScreenShot();

        driver.waitForElementPresent("css=" + DashBoardPageId_190.RightDrawerEditSingleDBShareCSS);
        driver.click("css=" + DashBoardPageId_190.RightDrawerEditSingleDBShareCSS);

        boolean shareFlagElem = driver.isDisplayed("css=" + DashBoardPageId_190.RightDrawerEditSingleDBToShareSelectedCSS);
        if (shareFlagElem) {
            driver.waitForElementPresent("css=" + DashBoardPageId_190.RightDrawerEditSingleDBNotShareCSS);
            driver.click("css=" + DashBoardPageId_190.RightDrawerEditSingleDBNotShareCSS);
            driver.takeScreenShot();
            driver.getLogger().info("DashboardBuilderUtil unshare dashboardset");
            return false;
        }
        else {
            driver.waitForElementPresent("css=" + DashBoardPageId_190.RightDrawerEditSingleDBToShareCSS);
            driver.click("css=" + DashBoardPageId_190.RightDrawerEditSingleDBToShareCSS);
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
    @Override
    public Boolean toggleShareDashboardset(WebDriver driver) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.toggleShareDashboardset started");
        WaitUtil.waitForPageFullyLoaded(driver);

        //open the edit/share dialog
        driver.getLogger().info("DashboardBuilderUtil.toggleShareDashboardset open share/edit dialog");
        driver.waitForElementPresent("id="+DashBoardPageId_190.DashboardsetOptionsMenuID);
        driver.click("id="+DashBoardPageId_190.DashboardsetOptionsMenuID);
        driver.waitForElementPresent("css=" + DashBoardPageId_190.DashboardsetOptionsEditCSS);
        driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsEditCSS);
        driver.takeScreenShot();

        //open share collapsible
        boolean editShareElem = driver.isDisplayed("css=" + DashBoardPageId_190.DashboardsetOptionsShareContentCSS);
        if (!editShareElem) {
            driver.waitForElementPresent("css=" + DashBoardPageId_190.DashboardsetOptionsShareCollapsibleCSS);
            driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsShareCollapsibleCSS);
        }
        driver.getLogger().info("DashboardBuilderUtil.toggleShareDashboardset sharing form has opened");

        //toggle share dashboardset
        driver.waitForElementPresent("css=" + DashBoardPageId_190.DashboardsetOptionsUnshareCSS);
        boolean isSharedSelected = driver.isDisplayed("css=" + DashBoardPageId_190.DashboardsetOptionsShareStatusCSS);
        if (isSharedSelected) {
            driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsUnshareCSS);
            driver.takeScreenShot();
            driver.getLogger().info("DashboardBuilderUtil unshare dashboardset");
            return false;
        } else {
            driver.click("css=" + DashBoardPageId_190.DashboardsetOptionsShareCSS);
            driver.takeScreenShot();
            driver.getLogger().info("DashboardBuilderUtil share dashboardset");
            return true;
        }
    }

    @Override
    public void selectDashboardInsideSet(WebDriver driver,String dashboardName) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.selectDashboardInsideSet started for name=\"" + dashboardName + "\"");
        Validator.notEmptyString("dashboardName", dashboardName);

        WebElement dashboardSetContainer = driver.getWebDriver().findElement(
                By.cssSelector(DashBoardPageId_190.DashboardSetNavsContainerCSS));
        if (dashboardSetContainer == null) {
            throw new NoSuchElementException(
                    "DashboardBuilderUtil.selectDashboardInsideSet: the dashboard navigator container is not found");
        }

        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.takeScreenShot();

        List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DashboardSetNavsCSS));
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

    @Override
    public boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector)
    {
        driver.getLogger().info(
                "DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardName + "\", description=\"" + description
                        + "\", showTimeSelector=\"" + showTimeSelector + "\"");
        Validator.notEmptyString("dashboardName", dashboardName);

        driver.waitForElementPresent(DashBoardPageId_190.BuilderNameTextLocator);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.BuilderNameTextLocator)));
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.waitForElementPresent(DashBoardPageId_190.BuilderNameTextLocator);
        driver.click(DashBoardPageId_190.BuilderNameTextLocator);
        driver.takeScreenShot();
        String realName = driver.getElement(DashBoardPageId_190.BuilderNameTextLocator).getAttribute("title");
        if (!dashboardName.equals(realName)) {
            driver.getLogger().info(
                    "DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected dashboard name is "
                            + dashboardName + ", actual dashboard name is " + realName);
            return false;
        }

        driver.waitForElementPresent(DashBoardPageId_190.BuilderDescriptionTextLocator);
        String realDesc = driver.getElement(DashBoardPageId_190.BuilderDescriptionTextLocator).getAttribute("title");
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

        boolean actualTimeSelectorShown = driver.isDisplayed(DashBoardPageId_190.BuilderDateTimePickerLocator);
        if (actualTimeSelectorShown != showTimeSelector) {
            driver.getLogger().info(
                    "DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected showTimeSelector is "
                            + showTimeSelector + ", actual dashboard showTimeSelector is " + actualTimeSelectorShown);
            return false;
        }

        driver.getLogger().info("DashboardBuilderUtil.verifyDashboard compelted and returns true");
        return true;
    }

    @Override
    public boolean verifyDashboardInsideSet(WebDriver driver, String dashboardName)
    {
        driver.getLogger().info("DashboardBuilderUtil.verifyDashboardInsideSet started for name=\"" + dashboardName + "\"");
        Validator.notEmptyString("dashboardName", dashboardName);

        WebElement dashboardSetContainer = driver.getWebDriver().findElement(
                By.cssSelector(DashBoardPageId_190.DashboardSetNavsContainerCSS));
        if (dashboardSetContainer == null) {
            throw new NoSuchElementException(
                    "DashboardBuilderUtil.verifyDashboardInsideSet: the dashboard navigator container is not found");
        }

        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOf(dashboardSetContainer));
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.takeScreenShot();

        boolean hasFound = false;
        List<WebElement> navs = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DashboardSetNavsCSS));
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
        driver.getLogger().info("DashboardBuilderUtil.verifyDashboardInsideSet completed");
        return hasFound;
    }

    @Override
    public boolean verifyDashboardSet(WebDriver driver, String dashboardSetName)
    {
        driver.getLogger().info("DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardSetName + "\"");
        Validator.notEmptyString("dashboardSetName", dashboardSetName);

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetNameTextLocator);
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.DashboardSetNameTextLocator)));
        WaitUtil.waitForPageFullyLoaded(driver);

        driver.waitForElementPresent(DashBoardPageId_190.DashboardSetNameTextLocator);
        driver.click(DashBoardPageId_190.DashboardSetNameTextLocator);
        driver.takeScreenShot();
        String realName = driver.getElement(DashBoardPageId_190.DashboardSetNameTextLocator).getText();
        if (!dashboardSetName.equals(realName)) {
            driver.getLogger().info(
                    "DashboardBuilderUtil.verifyDashboardSet compelted and returns false. Expected dashboard set name is "
                            + dashboardSetName + ", actual dashboard set name is " + realName);
            return false;
        }

        driver.getLogger().info("DashboardBuilderUtil.verifyDashboardSet compelted and returns true");
        return true;
    }

    @Override
    public boolean verifyWidget(WebDriver driver, String widgetName)
    {
        return verifyWidget(driver, widgetName, 0);
    }

    @Override
    public boolean verifyWidget(WebDriver driver, String widgetName, int index)
    {
        driver.getLogger().info(
                "DashboardBuilderUtil.verifyWidget started for name=\"" + widgetName + "\", index=\"" + index + "\"");
        Validator.notEmptyString("dashboardName", widgetName);

        WebElement we = null;
        try {
            WaitUtil.waitForPageFullyLoaded(driver);
            we = getTileTitleElement(driver, widgetName, index);
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

    private WebElement clickTileConfigButton(WebDriver driver, String widgetName, int index)
    {
        WebElement tileTitle = getTileTitleElement(driver, widgetName, index);
        WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId_190.BuilderTileConfigLocator));
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
        WebElement widgetDataExplore = widgetTitle.findElement(By.xpath(DashBoardPageId_190.BuilderTileDataExploreLocator));
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

    private void focusOnWidgetHeader(WebDriver driver, WebElement widgetElement)
    {
        if (null == widgetElement) {
            driver.getLogger().info("Fail to find the widget element");
            driver.takeScreenShot();
            throw new NoSuchElementException("Widget config menu is not found");
        }

        WebElement widgetHeader = widgetElement.findElement(By.cssSelector(DashBoardPageId_190.TileTitleCSS));
        Actions actions = new Actions(driver.getWebDriver());
        actions.moveToElement(widgetHeader).build().perform();
        driver.getLogger().info("Focus to the widget");
    }

    private  WebElement getTileTitleElement(WebDriver driver, String widgetName, int index)
    {
        driver.waitForElementPresent(DashBoardPageId_190.BuilderTilesEditArea);
        driver.click(DashBoardPageId_190.BuilderTilesEditArea);
        driver.takeScreenShot();

        String titleTitlesLocator = String.format(DashBoardPageId_190.BuilderTileTitleLocator, widgetName);
        List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(titleTitlesLocator));
        if (tileTitles == null || tileTitles.size() <= index) {
            throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
        }
        tileTitles.get(index).click();
        driver.takeScreenShot();
        return tileTitles.get(index);
    }

    private  WebElement getWidgetByName(WebDriver driver, String widgetName, int index) throws InterruptedException
    {
        if (widgetName == null) {
            return null;
        }

        List<WebElement> widgets = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.WidgetTitleCSS));
        WebElement widget = null;
        int counter = 0;
        for (WebElement widgetElement : widgets) {
            WebElement widgetTitle = widgetElement.findElement(By.cssSelector(DashBoardPageId_190.TileTitleCSS));
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

    @Override
     protected void hideRightDrawer(WebDriver driver) throws Exception
    {
        driver.waitForElementPresent("css=" + DashBoardPageId_190.RightDrawerCSS);
        if (isRightDrawerVisible(driver) == true) {
            driver.click("css=" + DashBoardPageId_190.RightDrawerTogglePencilBtnCSS);
            if(isRightDrawerVisible(driver) == true){
                driver.click("css=" + DashBoardPageId_190.RightDrawerTogglePencilBtnCSS);
            }
            driver.getLogger().info("[DashboardBuilderUtil] triggered hideRightDrawer.");
        }
        driver.takeScreenShot();
    }

    private boolean isRightDrawerVisible(WebDriver driver)
    {
        WebElement rightDrawerPanel = driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId_190.RightDrawerPanelCSS));
        boolean isDisplayed = rightDrawerPanel.getCssValue("display").equals("none") != true;
        driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isDisplayed value is " + isDisplayed);

        boolean isWidthValid = rightDrawerPanel.getCssValue("width").equals("0px") != true;
        driver.getLogger().info("DashboardBuilderUtil.isRightDrawerVisible,the isWidthValid value is " + isWidthValid);

        return isDisplayed && isWidthValid;
    }

    //to open right drawer and show build dashboard
    private void showRightDrawer(WebDriver driver,String buttonName) throws Exception
    {
        driver.waitForElementPresent("css=" + DashBoardPageId_190.RightDrawerCSS);
        if (isRightDrawerVisible(driver) != false) {
            hideRightDrawer(driver);
        }
        switch (buttonName){
            case PENCIL:
                driver.click("css=" + DashBoardPageId_190.RightDrawerTogglePencilBtnCSS);
                break;
            case WRENCH:
                driver.click("css=" + DashBoardPageId_190.RightDrawerToggleWrenchBtnCSS);
                break;
            default:
                driver.takeScreenShot();
                return;
        }
        driver.getLogger().info("[DashboardBuilderUtil] triggered showRightDrawer and show build dashboard.");
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.takeScreenShot();
    }

    private void duplicateDashboardCommonUse(WebDriver driver, String name, String descriptions ,String operationName) throws Exception
    {
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        Validator.notNull("duplicatename", name);
        Validator.notEmptyString("duplicatename", name);

        driver.getLogger().info("DashboardBuilderUtil.duplicate started");
        driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        WaitUtil.waitForPageFullyLoaded(driver);
        WebElement visibleContainer=getSelectedDashboardEl(driver);
        WebElement visbleOptionMenu =visibleContainer.findElement(By.cssSelector(DashBoardPageId_190.BuilderOptionsMenuLocator));
        visbleOptionMenu.click();
        driver.waitForElementPresent("css=" + DashBoardPageId_190.BuilderOptionsDuplicateLocatorCSS);

        // add to set or not,or no dropdownmenu just add
        switch (operationName){
            case DUP_DASHBOARD_NODSUBMENU:
                driver.click("css=" + DashBoardPageId_190.BuilderOptionsDuplicateLocatorCSS);
                break;
            case DUP_DASHBOARD_TOSET:
                driver.click("css=" + DashBoardPageId_190.BuilderOptionsDuplicateLocatorCSS);
                driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsDuplicateToSetCSS);
                driver.click("css="+DashBoardPageId_190.BuilderOptionsDuplicateToSetCSS);
                break;
            case DUP_SHBOARDSET_NOTTOSET:
                driver.click("css=" + DashBoardPageId_190.BuilderOptionsDuplicateLocatorCSS);
                driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsDuplicateNotToSetCSS);
                driver.click("css="+DashBoardPageId_190.BuilderOptionsDuplicateNotToSetCSS);
                break;
        }

        driver.takeScreenShot();
        driver.waitForElementPresent("id=" + DashBoardPageId_190.BuilderOptionsDuplicateNameCSS);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ojDialogWrapper-duplicateDsbDialog")));
        //add name and description
        driver.getElement("id=" + DashBoardPageId_190.BuilderOptionsDuplicateNameCSS).clear();
        driver.click("id=" + DashBoardPageId_190.BuilderOptionsDuplicateNameCSS);
        By locatorOfDuplicateNameEl = By.id(DashBoardPageId_190.BuilderOptionsDuplicateNameCSS);

        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
        driver.sendKeys("id=" + DashBoardPageId_190.BuilderOptionsDuplicateNameCSS, name);
        driver.getElement("id=" + DashBoardPageId_190.BuilderOptionsDuplicateDescriptionCSS).clear();
        driver.click("id=" + DashBoardPageId_190.BuilderOptionsDuplicateDescriptionCSS);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
        if (descriptions == null) {
            driver.sendKeys("id=" + DashBoardPageId_190.BuilderOptionsDuplicateDescriptionCSS, "");
        }
        else {
            driver.sendKeys("id=" + DashBoardPageId_190.BuilderOptionsDuplicateDescriptionCSS, descriptions);
        }
        driver.takeScreenShot();

        //press ok button
        By locatorOfDuplicateSaveEl = By.cssSelector(DashBoardPageId_190.BuilderOptionsDuplicateSaveCSS);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateSaveEl));
        wait.until(ExpectedConditions.elementToBeClickable(locatorOfDuplicateSaveEl));

        By locatorOfDuplicateDesEl = By.id(DashBoardPageId_190.BuilderOptionsDuplicateDescriptionCSS);
        driver.getWebDriver().findElement(locatorOfDuplicateDesEl).sendKeys(Keys.TAB);

        WebElement saveButton = driver.getElement("css=" + DashBoardPageId_190.BuilderOptionsDuplicateSaveCSS);
        Actions actions = new Actions(driver.getWebDriver());
        actions.moveToElement(saveButton).build().perform();

        driver.takeScreenShot();
        driver.getLogger().info("DashboardBuilderUtil.duplicate save button has been focused");

        driver.click("css=" + DashBoardPageId_190.BuilderOptionsDuplicateSaveCSS);
        driver.takeScreenShot();
        //wait for direct
        if(operationName.equals(DUP_DASHBOARD_TOSET)){
            WaitUtil.waitForPageFullyLoaded(driver);
            driver.waitForElementPresent("css="+DashBoardPageId_190.BuilderOptionsMenuLocator);
        }else{
            String newTitleLocator = ".dbd-display-hover-area h1[title='"+name+"']";
            driver.getLogger().info("DashboardBuilderUtil.duplicate : " + newTitleLocator);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(newTitleLocator)));
            WaitUtil.waitForPageFullyLoaded(driver);
        }
        driver.takeScreenShot();
        driver.getLogger().info("DashboardBuilderUtil.duplicate completed");
    }

    /**
     * @param driver
     * @return current visible dashboard container
     */
    private WebElement getSelectedDashboardEl(WebDriver driver) {
        List<WebElement> dashboardContainers = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId_190.DashboardSetContainerCSS));
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
