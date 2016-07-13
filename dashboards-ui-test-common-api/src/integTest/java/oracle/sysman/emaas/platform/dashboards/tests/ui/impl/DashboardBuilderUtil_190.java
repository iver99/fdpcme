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

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
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

import java.util.List;

public class DashboardBuilderUtil_190 extends DashboardBuilderUtil_175
{

    private static final String DASHBOARD_SELECTION_TAB_NAME = "Dashboard";
    private static final String DUP_DASHBOARD_NODSUBMENU = "duplicate_noSubMenu";
    private static final String DUP_DASHBOARD_TOSET = "duplicate_addToSet";
    private static final String DUP_SHBOARDSET_NOTTOSET="duplicate_notAddToSet";

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

    @Override
    public void addNewDashboardToSet(WebDriver driver, String dashboardName) throws Exception
    {
        driver.getLogger().info("DashboardBuilderUtil.addNewDashboardToSet started for name=\"" + dashboardName + "\"");
        Validator.notEmptyString("dashboardName", dashboardName);

        WebElement dashboardSetContainer = driver.getWebDriver().findElement(
                By.cssSelector(DashBoardPageId.DashboardSetNavsContainerCSS));
        if (dashboardSetContainer == null) {
            throw new NoSuchElementException(
                    "DashboardBuilderUtil.addNewDashboardToSet: the dashboard navigator container is not found");
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
    public  void createDashboardInsideSet(WebDriver driver, String name, String descriptions) throws Exception
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
        WaitUtil.waitForPageFullyLoaded(driver);
        driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
        driver.getLogger().info("DashboardBuilderUtil.createDashboardInsideSet completed");
    }

    private static void duplicateDashboardCommonUse(WebDriver driver, String name, String descriptions ,String operationName) throws Exception
    {
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        Validator.notNull("duplicatename", name);
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
        //wait for direct
        if(operationName.equals(DUP_DASHBOARD_TOSET)){
            WaitUtil.waitForPageFullyLoaded(driver);
            driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsMenuLocator);
        }else{
            String newTitleLocator = ".dbd-display-hover-area h1[title='"+name+"']";
            driver.getLogger().info("DashboardBuilderUtil.duplicate : " + newTitleLocator);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(newTitleLocator)));
            WaitUtil.waitForPageFullyLoaded(driver);
        }
        driver.takeScreenShot();
        driver.getLogger().info("DashboardBuilderUtil.duplicate completed");
    }


    @Override
    public  void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions,boolean addToSet) throws Exception {
        if(addToSet){
            duplicateDashboardCommonUse(driver,name, descriptions ,DUP_DASHBOARD_TOSET);
        }else{
            duplicateDashboardCommonUse(driver,name, descriptions ,DUP_SHBOARDSET_NOTTOSET);
        }
    }

    @Override
    public  void editDashboard(WebDriver driver, String name, String descriptions, Boolean toShowDscptn) throws Exception
    {
        Validator.notNull("editname", name);
        Validator.notEmptyString("editname", name);

        driver.getLogger().info("DashboardBuilderUtil.edit started");
        WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
        driver.waitForElementPresent("css="+ DashBoardPageId.BuilderOptionsMenuLocator);
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

    @Override
    public void removeDashboardFromSet(WebDriver driver, String dashboardName)
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
                WaitUtil.waitForPageFullyLoaded(driver);
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


    @Override
    public void selectDashboardInsideSet(WebDriver driver,String dashboardName) throws Exception
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


}
