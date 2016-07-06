package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IErrorPageUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class ErrorPageUtil
{
	/**
	 * click error page sign out button
	 *
	 * @param driver
	 * @throws Exception
	 */
	public static void signOut(WebDriver driver) throws Exception
	{
		IErrorPageUtil epu = new UtilLoader<IErrorPageUtil>().loadUtil(driver, IErrorPageUtil.class);
		epu.signOut(driver);
	}

}
