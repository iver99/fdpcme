package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class BrandingBarUtil
{

	private static WebDriver driver;

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void userMenuOptions(String option) throws Exception
	{

	}

	public static void visitApplicationAdministration(String AdministrationName) throws Exception
	{

	}

	public static void visitApplicationCloudServices(String cloudServiceName) throws Exception
	{

	}

	public static void visitApplicationDefault(String applicationName) throws Exception
	{

	}

	public static void visitApplicationVisualAnalyzers(String visualAnalyzersName) throws Exception
	{

	}

	public static void visitDashboardHome() throws Exception
	{
		driver.click(DashBoardPageId.LinkID);
		driver.waitForElementPresent(DashBoardPageId.BrandingBarDashboardLinkLocator);
		driver.check(DashBoardPageId.BrandingBarDashboardLinkLocator);
	}

}
