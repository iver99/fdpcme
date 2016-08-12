package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IWidgetSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WidgetSelectorUtil
{
	public static void addWidget(WebDriver driver, String widgetName) 
	{
		IWidgetSelectorUtil wsu = new UtilLoader<IWidgetSelectorUtil>().loadUtil(driver, IWidgetSelectorUtil.class);
		wsu.addWidget(driver, widgetName);
	}

	public static void page(WebDriver driver, int pageNo) throws IllegalAccessException 
	{
		IWidgetSelectorUtil wsu = new UtilLoader<IWidgetSelectorUtil>().loadUtil(driver, IWidgetSelectorUtil.class);
		wsu.page(driver, pageNo);
	}

	public static void pagingNext(WebDriver driver) throws IllegalAccessException 
	{
		IWidgetSelectorUtil wsu = new UtilLoader<IWidgetSelectorUtil>().loadUtil(driver, IWidgetSelectorUtil.class);
		wsu.pagingNext(driver);
	}

	public static void pagingPrevious(WebDriver driver) throws IllegalAccessException 
	{
		IWidgetSelectorUtil wsu = new UtilLoader<IWidgetSelectorUtil>().loadUtil(driver, IWidgetSelectorUtil.class);
		wsu.pagingPrevious(driver);
	}

}
