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
        if(option ==null){
            return;
        }
        driver.getLogger().info("Click brand bar user menur option:"+option);
        driver.waitForElementPresent(DashBoardPageId.Brand_Bar_User_Menu);
        driver.click(DashBoardPageId.Brand_Bar_User_Menu);
        switch(option){
            case DashBoardPageId.Brand_Bar_User_Menu_Help_Option:
                driver.takeScreenShot();
                driver.click(DashBoardPageId.Option_Help);
                break;
            case DashBoardPageId.Brand_Bar_User_Menu_About_Option:
                driver.takeScreenShot();
                driver.click(DashBoardPageId.Option_About);
                driver.takeScreenShot();
                driver.click(DashBoardPageId.AboutDialogClose);
                break;
            case DashBoardPageId.Brand_Bar_User_Menu_Signout_Option:
                driver.click(DashBoardPageId.Option_Logout);
                break;
        }
        driver.takeScreenShot();
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
