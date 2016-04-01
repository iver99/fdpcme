package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardHomeUtil
{

	private static WebDriver driver;

	public static void createDashboard(String name, String descriptions, Boolean displayDesc, Boolean selectorRefreshcontrol)
			throws Exception
	{

	}

	public static void createDashboardSet(String name, String descriptions, Boolean displayDesc, Boolean selectorRefreshcontrol)
			throws Exception
	{

	}

	public static void delete(String dashboardName, String view) throws Exception
	{

	}

	public static void exploreData(String option) throws Exception
	{

	}

	public static void filterOptions(String cloudServices, String createdBy, String Navigation, Boolean Favorites)
			throws Exception
	{

	}

	public static void gridView() throws Exception
	{
		driver.click(DashBoardPageId.DashboardsGridViewLocator);//("/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[1]/button[2]");
		DashboardHomeUtil.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
	}

	public static void listView() throws Exception
	{
		driver.click(DashBoardPageId.DashboardsListViewLocator);//("/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[1]/button[2]");
		DashboardHomeUtil.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void search(String searchString) throws Exception
	{
		if (searchString == null) {
			return;
		}
		driver.getLogger().info("[DashboardHomeUtil] call search");
		driver.waitForElementPresent(DashBoardPageId.SearchDashboardInputLocator);
		driver.getElement(DashBoardPageId.SearchDashboardInputLocator).clear();
		driver.click(DashBoardPageId.SearchDashboardInputLocator);
		DashboardHomeUtil.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
		driver.sendKeys(DashBoardPageId.SearchDashboardInputLocator, searchString);
		DashboardHomeUtil.waitForMilliSeconds(DashBoardPageId.Delaytime_short);
	}

	public static void selectOOB(String dashboardName) throws Exception
	{

	}

	public static void sortBy(String option) throws Exception
	{

	}

	public static void waitForMilliSeconds(long millisSec) throws Exception
	{
		Thread.sleep(millisSec);
	}

}
