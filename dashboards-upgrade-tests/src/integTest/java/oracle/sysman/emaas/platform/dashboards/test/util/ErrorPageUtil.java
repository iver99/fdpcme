package oracle.sysman.emaas.platform.dashboards.test.util;

import oracle.sysman.emaas.platform.dashboards.test.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class ErrorPageUtil
{
	//error page sign out button
	public static void signOut(WebDriver driver) throws Exception
	{
		driver.getLogger().info("ErrorPageUtil click signOut button started");
		driver.waitForElementPresent("id=" + DashBoardPageId.ErrorSignOutButtonId);
		driver.takeScreenShot();
		driver.click("id=" + DashBoardPageId.ErrorSignOutButtonId);
		driver.takeScreenShot();
		driver.getLogger().info("ErrorUtil click signOut button completed!");
	}

}

