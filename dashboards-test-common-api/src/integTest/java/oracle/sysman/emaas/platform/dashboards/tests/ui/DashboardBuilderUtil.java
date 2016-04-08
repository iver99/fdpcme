package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.logging.Level;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DelayedPressEnterThread;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardBuilderUtil
{

	private static WebDriver driver;

	public static final String REFRESH_DASHBOARD_PARAM_OFF = "Off";
	public static final String REFRESH_DASHBOARD_PARAM_5MIN = "On (Every 5 Minutes)";

	public static Boolean asHomeOption() throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.asHomeOption started");

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		boolean homeElem=driver.isDisplayed("css="+DashBoardPageId.BuilderOptionsSetHomeLocatorCSS);

		if(homeElem){
			driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsSetHomeLocatorCSS);
			driver.click("css="+DashBoardPageId.BuilderOptionsSetHomeLocatorCSS);
			boolean comfirmDialog =driver.isDisplayed(DashBoardPageId.BuilderOptionsSetHomeComfirmCSS);
			if(comfirmDialog){
				driver.click(DashBoardPageId.BuilderOptionsSetHomeComfirmCSS);
			}
			driver.getLogger().info("DashboardBuilderUtil set home completed");
			return true;
		}else{
			driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsRemoveHomeLocatorCSS);
			driver.click("css="+DashBoardPageId.BuilderOptionsRemoveHomeLocatorCSS);
			driver.getLogger().info("DashboardBuilderUtil remove home completed");
			return false;
		}

	}

	public static void deleteDashboard() throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.deleteDashboard started");

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsDeleteLocator);
		driver.click(DashBoardPageId.BuilderOptionsDeleteMenuLocator);
		driver.waitForElementPresent(DashBoardPageId.BuilderDeleteDialogLocator);
		driver.click(DashBoardPageId.BuilderDeleteDialogDeleteBtnLocator);
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);

		driver.getLogger().info("DashboardBuilderUtil.deleteDashboard completed");
	}

	public static void deleteDashboardSet() throws Exception
	{

	}

	public static void duplicate(String name, String descriptions) throws Exception
	{
		if (name == null && descriptions==null) {
			return;
		}
		driver.getLogger().info("DashboardBuilderUtil.duplicate started");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);
		driver.click("css="+DashBoardPageId.BuilderOptionsDuplicateLocatorCSS);
		driver.waitForElementPresent("id="+DashBoardPageId.BuilderOptionsDuplicateNameCSS);

		//add name and description
		driver.getElement("id="+DashBoardPageId.BuilderOptionsDuplicateNameCSS).clear();
		driver.click("id="+DashBoardPageId.BuilderOptionsDuplicateNameCSS);
		Long timeOutInSecond = 10L;
		By locatorOfDuplicateNameEl =  By.id(DashBoardPageId.BuilderOptionsDuplicateNameCSS);
		WebDriverWait wait = new WebDriverWait (driver.getWebDriver(),timeOutInSecond);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
		driver.sendKeys("id="+DashBoardPageId.BuilderOptionsDuplicateNameCSS, name);
		driver.getElement("id="+DashBoardPageId.BuilderOptionsDuplicateDescriptionCSS).clear();
		driver.click("id="+DashBoardPageId.BuilderOptionsDuplicateDescriptionCSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfDuplicateNameEl));
		driver.sendKeys("id="+DashBoardPageId.BuilderOptionsDuplicateDescriptionCSS, descriptions);

		//press ok button
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsDuplicateSaveCSS);
		driver.click(DashBoardPageId.BuilderOptionsDuplicateSaveCSS);

	}

	public static void editDashboard(String name, String descriptions) throws Exception
	{
		if (name == null && descriptions==null) {
			return;
		}
		driver.getLogger().info("DashboardBuilderUtil.edit started");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.click("css="+DashBoardPageId.BuilderOptionsEditLocatorCSS);
		driver.waitForElementPresent("id="+DashBoardPageId.BuilderOptionsEditNameCSS);

		//wait for 10l
		Long timeOutInSecond = 10L;
		By locatorOfEditDesEl = By.id(DashBoardPageId.BuilderOptionsEditDescriptionCSS);
		WebDriverWait wait = new WebDriverWait (driver.getWebDriver(),timeOutInSecond);

		//add name and description
		driver.getElement("id="+DashBoardPageId.BuilderOptionsEditNameCSS).clear();
		driver.click("id="+DashBoardPageId.BuilderOptionsEditNameCSS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.sendKeys("id="+DashBoardPageId.BuilderOptionsEditNameCSS, name);

		driver.getElement("id="+DashBoardPageId.BuilderOptionsEditDescriptionCSS).clear();
		driver.click("id="+DashBoardPageId.BuilderOptionsEditDescriptionCSS);

		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));
		driver.sendKeys("id="+DashBoardPageId.BuilderOptionsEditDescriptionCSS, descriptions);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfEditDesEl));

		//press ok button
		driver.waitForElementPresent("id="+ DashBoardPageId.BuilderOptionsEditSaveCSS);
		driver.click("id="+DashBoardPageId.BuilderOptionsEditSaveCSS);

	}

	public static void editDashboardSet(String name, String descriptions, Boolean shareOption) throws Exception
	{

	}

	public static Boolean favoriteOption() throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.favoriteOption started");

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		boolean favoriteElem=driver.isDisplayed("css="+DashBoardPageId.BuilderOptionsFavoriteLocatorCSS);
		if(favoriteElem){
			driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsFavoriteLocatorCSS);
			driver.click("css="+DashBoardPageId.BuilderOptionsFavoriteLocatorCSS);
			driver.getLogger().info("DashboardBuilderUtil add favorite completed");
			return true;
		}else{
			driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsRemoveFavoriteLocatorCSS);
			driver.click("css="+DashBoardPageId.BuilderOptionsRemoveFavoriteLocatorCSS);
			driver.getLogger().info("DashboardBuilderUtil remove favorite completed");
			return false;
		}
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void print() throws Exception
	{
		driver.getLogger().info("print dashboard");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsPrintLocatorCSS);
     	DelayedPressEnterThread thr = new DelayedPressEnterThread( "DelayedPressEnterThread",5000) ;
		driver.getWebDriver().findElement(By.cssSelector(DashBoardPageId.BuilderOptionsPrintLocatorCSS)).click();
		driver.getLogger().info("print completed");
	}

	public static void refreshDashboard(String refreshSettings) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboard started for refreshSettings=" + refreshSettings);

		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsAutoRefreshLocator);
		driver.click(DashBoardPageId.BuilderOptionsAutoRefreshLocator);
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsAutoRefreshOffLocator);
		switch (refreshSettings) {
			case REFRESH_DASHBOARD_PARAM_OFF:
				driver.check(DashBoardPageId.BuilderOptionsAutoRefreshOffLocator);
				driver.waitForElementPresent(DashBoardPageId.BuilderAutoRefreshOffSelectedLocator);
				break;
			case REFRESH_DASHBOARD_PARAM_5MIN:
				driver.click(DashBoardPageId.BuilderOptionsAutoRefreshOn5MinLocator);
				driver.waitForElementPresent(DashBoardPageId.BuilderAutoRefreshOn5MinSelectedLocator);
				break;
			default:
				driver.getLogger().log(Level.SEVERE, "Input parameter " + refreshSettings + " is invalid. ");
				return;
		}
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboard completed");

	}

	public static void refreshDashboardSet(String refreshSettings) throws Exception
	{

	}

	public static void save() throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.save started");
		driver.waitForElementPresent("css="+DashBoardPageId.DashboardSaveCSS);
		driver.click("css="+DashBoardPageId.DashboardSaveCSS);
		driver.getLogger().info("DashboardBuilderUtil.save compelted");
	}

	public static Boolean toggleShareDashboard() throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.favoriteOption started");
		driver.waitForElementPresent(DashBoardPageId.BuilderOptionsMenuLocator);
		driver.click(DashBoardPageId.BuilderOptionsMenuLocator);
		boolean shareElem=driver.isDisplayed("css="+DashBoardPageId.BuilderOptionsShareLocatorCSS);
		if(shareElem){
			driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsShareLocatorCSS);
			driver.click("css="+DashBoardPageId.BuilderOptionsShareLocatorCSS);
			driver.getLogger().info("DashboardBuilderUtil share dashboard");
			return true;
		}else{
			driver.waitForElementPresent("css="+DashBoardPageId.BuilderOptionsUnShareLocatorCSS);
			driver.click("css="+DashBoardPageId.BuilderOptionsUnShareLocatorCSS);
			driver.getLogger().info("DashboardBuilderUtil unshare dashboard");
			return false;
		}
	}

}
