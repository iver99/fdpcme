package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WidgetUtil
{
	private static WebDriver driver;

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void remove(String widgetName) throws Exception
	{
		WidgetUtil.remove(widgetName, 0);
	}

	public static void remove(String widgetName, int index) throws Exception
	{

	}

	public static void resizeOptions(String widgetName, int index, String resizeOptions) throws Exception
	{
	}

	public static void resizeOptions(String widgetName, String resizeOptions) throws Exception
	{
		WidgetUtil.resizeOptions(widgetName, 0, resizeOptions);
	}

	public static void title(String widgetName, Boolean visibility) throws Exception
	{
		WidgetUtil.title(widgetName, 0, visibility);
	}

	public static void title(String widgetName, int index, Boolean visibility) throws Exception
	{

	}

	public static void udeRedirect(String widgetName) throws Exception
	{
		WidgetUtil.udeRedirect(widgetName, 0);

	}

	public static void udeRedirect(String widgetName, int index) throws Exception
	{

	}

}
