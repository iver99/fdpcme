package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ParameterValidators;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class DashboardBuilderUtil
{

	private static WebDriver driver;

	public static final String REFRESH_DASHBOARD_PARAM_OFF = "Off";
	public static final String REFRESH_DASHBOARD_PARAM_5MIN = "On (Every 5 Minutes)";

	public static void asHomeOption(Boolean home) throws Exception
	{

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

	}

	public static void editDashboard(String name, String descriptions) throws Exception
	{

	}

	public static void editDashboardSet(String name, String descriptions, Boolean shareOption) throws Exception
	{

	}

	public static void favoriteOption(Boolean favorite) throws Exception
	{

	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void print() throws Exception
	{

	}

	public static void refreshDashboard(String refreshSettings) throws Exception
	{
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboard started for refreshSettings=" + refreshSettings);

		ParameterValidators.fromValidValues("refreshSettings", refreshSettings, REFRESH_DASHBOARD_PARAM_OFF,
				REFRESH_DASHBOARD_PARAM_5MIN);

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
		}
		driver.getLogger().info("DashboardBuilderUtil.refreshDashboard completed");

	}

	public static void refreshDashboardSet(String refreshSettings) throws Exception
	{

	}

	public static void save() throws Exception
	{

	}

	public static void shareDashboard() throws Exception
	{

	}

	public static void unshareDashboard() throws Exception
	{

	}

}
