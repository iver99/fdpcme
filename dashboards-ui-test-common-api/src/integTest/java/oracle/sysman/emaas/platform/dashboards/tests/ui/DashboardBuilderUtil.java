package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IDashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardBuilderUtil
{
	public static final String REFRESH_DASHBOARD_SETTINGS_OFF = IDashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF;
	public static final String REFRESH_DASHBOARD_SETTINGS_5MIN = IDashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN;
	public static final String TILE_WIDER = IDashboardBuilderUtil.TILE_WIDER;
	public static final String TILE_NARROWER = IDashboardBuilderUtil.TILE_NARROWER;
	public static final String TILE_TALLER = IDashboardBuilderUtil.TILE_TALLER;
	public static final String TILE_SHORTER = IDashboardBuilderUtil.TILE_SHORTER;

	public static void addNewDashboardToSet(WebDriver driver, String dashboardName) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.addNewDashboardToSet(driver, dashboardName);
	}

	public static void addWidgetToDashboard(WebDriver driver, String searchString) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.addWidgetToDashboard(driver, searchString);
	}

	public static void createDashboardInsideSet(WebDriver driver, String name, String descriptions) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.createDashboardInsideSet(driver, name, descriptions);
	}


	public static void deleteDashboard(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.deleteDashboard(driver);
	}

	public static void deleteDashboardSet(WebDriver driver)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.deleteDashboardSet(driver);
	}

	public static void duplicateDashboard(WebDriver driver, String name, String descriptions) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.duplicateDashboard(driver, name, descriptions);
	}

	public static void duplicateDashboardInsideSet(WebDriver driver, String name, String descriptions,boolean addToSet) throws Exception {
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.duplicateDashboardInsideSet(driver, name, descriptions,addToSet);
	}

	public static void editDashboard(WebDriver driver, String name, String descriptions) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.editDashboard(driver, name, descriptions);
	}

	public static void editDashboard(WebDriver driver, String name, String descriptions, Boolean showDesc) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.editDashboard(driver, name, descriptions,showDesc);
	}


        /*
        public static void EditDashboard_targetselctor(WebDriver driver, String name, String descriptions) throws Exception
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
             
                //selctor filetr entity
		driver.waitForElementPresent(DashBoardPageId.EntityfilterLocator);
		driver.click(DashBoardPageId.EntityfilterLocator);

		//press ok button
		driver.waitForElementPresent("css=" + DashBoardPageId.BuilderOptionsEditSaveCSS);
		driver.click("css=" + DashBoardPageId.BuilderOptionsEditSaveCSS);
		driver.takeScreenShot();
		driver.getLogger().info("DashboardBuilderUtil.edit complete");
	}
	*/

	public static void editDashboardSet(WebDriver driver, String name, String descriptions) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.editDashboardSet(driver, name, descriptions);
	}

	public static Boolean favoriteOption(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.favoriteOption(driver);
	}

	public static Boolean favoriteOptionDashboardSet(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.favoriteOptionDashboardSet(driver);
	}

	public static void gridView(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.gridView(driver);
	}

	public static boolean isRefreshSettingChecked(WebDriver driver, String refreshSettings) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.isRefreshSettingChecked(driver, refreshSettings);
	}

	public static boolean isRefreshSettingCheckedForDashbaordSet(WebDriver driver, String refreshSettings)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.isRefreshSettingCheckedForDashbaordSet(driver, refreshSettings);
	}

	public static void listView(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.listView(driver);
	}

	public static void openWidget(WebDriver driver, String widgetName) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.openWidget(driver, widgetName);
	}

	public static void openWidget(WebDriver driver, String widgetName, int index) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.openWidget(driver, widgetName, index);
	}

	public static void printDashboard(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.printDashboard(driver);
	}

	public static void printDashboardSet(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.printDashboardSet(driver);
	}

	public static void refreshDashboard(WebDriver driver, String refreshSettings)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.refreshDashboard(driver, refreshSettings);
	}

	public static void refreshDashboardSet(WebDriver driver, String refreshSettings)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.refreshDashboardSet(driver, refreshSettings);
	}

	public static void removeDashboardInSet(WebDriver driver, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.removeDashboardInSet(driver, dashboardName);
	}

	public static void removeWidget(WebDriver driver, String widgetName) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.removeWidget(driver, widgetName);
	}

	public static void removeWidget(WebDriver driver, String widgetName, int index) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.removeWidget(driver, widgetName, index);
	}

	public void removeDashboardFromSet(WebDriver driver, String dashboardName) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.removeDashboardFromSet(driver, dashboardName);
	}

	public static void resizeWidget(WebDriver driver, String widgetName, int index, String resizeOptions) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.resizeWidget(driver, widgetName, index, resizeOptions);
	}

	public static void resizeWidget(WebDriver driver, String widgetName, String resizeOptions) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.resizeWidget(driver, widgetName, resizeOptions);
	}

	public static void saveDashboard(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.saveDashboard(driver);
	}

	public static void search(WebDriver driver, String searchString) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.search(driver, searchString);
	}

	public static void selectDashboard(WebDriver driver, String dashboardName) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.selectDashboard(driver, dashboardName);
	}

	public static void selectDashboardInsideSet(WebDriver driver, String dashboardName) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.selectDashboardInsideSet(driver, dashboardName);
	}


	public static void showWidgetTitle(WebDriver driver, String widgetName, boolean visibility) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.showWidgetTitle(driver, widgetName, visibility);
	}

	public static void showWidgetTitle(WebDriver driver, String widgetName, int index, boolean visibility) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.showWidgetTitle(driver, widgetName, index, visibility);
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
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		dbu.sortBy(driver, option);
	}

	public static Boolean toggleHome(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.toggleHome(driver);
	}

	public static Boolean toggleHomeDashboardSet(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.toggleHomeDashboardSet(driver);
	}

	public static Boolean toggleShareDashboard(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.toggleShareDashboard(driver);
	}

	public static Boolean toggleShareDashboardset(WebDriver driver) throws Exception
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.toggleShareDashboardset(driver);
	}

	public static boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyDashboard(driver, dashboardName, description, showTimeSelector);
	}

	public static boolean verifyDashboardInsideSet(WebDriver driver, String dashboardName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyDashboardInsideSet(driver, dashboardName);
	}

	public static boolean verifyDashboardSet(WebDriver driver, String dashboardSetName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyDashboardSet(driver, dashboardSetName);
	}

	public static boolean verifyWidget(WebDriver driver, String widgetName)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyWidget(driver, widgetName);
	}

	public static boolean verifyWidget(WebDriver driver, String widgetName, int index)
	{
		IDashboardBuilderUtil dbu = new UtilLoader<IDashboardBuilderUtil>().loadUtil(driver, IDashboardBuilderUtil.class);
		return dbu.verifyWidget(driver, widgetName, index);
	}



}
