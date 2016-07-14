package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.UtilLoader;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class BrandingBarUtil
{
	// values for 'option' for user menu
	public static final String USERMENU_OPTION_HELP = IBrandingBarUtil.USERMENU_OPTION_HELP;
	public static final String USERMENU_OPTION_ABOUT = IBrandingBarUtil.USERMENU_OPTION_ABOUT;
	public static final String USERMENU_OPTION_SIGNOUT = IBrandingBarUtil.USERMENU_OPTION_SIGNOUT;

	//Branding bar link text
	//Cloud Services
	public static final String NAV_LINK_TEXT_CS_ITA = IBrandingBarUtil.NAV_LINK_TEXT_CS_ITA;
	public static final String NAV_LINK_TEXT_CS_APM = IBrandingBarUtil.NAV_LINK_TEXT_CS_APM;
	public static final String NAV_LINK_TEXT_CS_LA = IBrandingBarUtil.NAV_LINK_TEXT_CS_LA;
	//Visual Analyzers
	public static final String NAV_LINK_TEXT_VA_ITA = IBrandingBarUtil.NAV_LINK_TEXT_VA_ITA;
	public static final String NAV_LINK_TEXT_VA_TA = IBrandingBarUtil.NAV_LINK_TEXT_VA_TA;
	public static final String NAV_LINK_TEXT_VA_LA = IBrandingBarUtil.NAV_LINK_TEXT_VA_LA;
	//Administration
	public static final String NAV_LINK_TEXT_ADMIN_AGENT = IBrandingBarUtil.NAV_LINK_TEXT_ADMIN_AGENT;
	public static final String NAV_LINK_TEXT_ADMIN_ITA = IBrandingBarUtil.NAV_LINK_TEXT_ADMIN_ITA;
	public static final String NAV_LINK_TEXT_ADMIN_ALERT = IBrandingBarUtil.NAV_LINK_TEXT_ADMIN_ALERT;

	/**
	 * Check whether admin field exist
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isAdmin(WebDriver driver) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isAdmin(driver);
	}

	/**
	 * Check if the specified administration link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param adminLinkName
	 *            Name of the link
	 * @return
	 */
	public static boolean isAdminLinkExisted(WebDriver driver, String adminLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isAdminLinkExisted(driver, adminLinkName);
	}

	/**
	 * Check if the specified cloud service link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param cloudServiceLinkName
	 *            Name of the link
	 * @return
	 */
	public static boolean isCloudServiceLinkExisted(WebDriver driver, String cloudServiceLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isCloudServiceLinkExisted(driver, cloudServiceLinkName);
	}

	/**
	 * Check if the dashboard home link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isDashboardHomeLinkExisted(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isDashboardHomeLinkExisted(driver);
	}

	/**
	 * Check if the specified home link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param homeLinkName
	 *            Name of the link
	 * @return
	 */
	public static boolean isHomeLinkExisted(WebDriver driver, String homeLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isHomeLinkExisted(driver, homeLinkName);
	}

	/**
	 * Check if the favorites link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isMyFavoritesLinkExisted(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isMyFavoritesLinkExisted(driver);
	}

	/**
	 * Check if the Home link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isMyHomeLinkExisted(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isMyHomeLinkExisted(driver);
	}

	/**
	 * Check if the specified visual analyzer link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visualAnalyzerLinkName
	 *            Name of the link
	 * @return
	 */
	public static boolean isVisualAnalyzerLinkExisted(WebDriver driver, String visualAnalyzerLinkName)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isVisualAnalyzerLinkExisted(driver, visualAnalyzerLinkName);
	}

	/**
	 * Check if the welcome link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isWelcomeLinkExisted(WebDriver driver)
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		return bu.isWelcomeLinkExisted(driver);
	}

	/**
	 * Do the specified user option by the given option name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param option
	 *            Name of the option, should one of "about", "help", "signout"
	 * @return
	 */
	public static void userMenuOptions(WebDriver driver, String option) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.userMenuOptions(driver, option);
	}

	/**
	 * Navigate to the specified administration page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param adminLinkName
	 *            Name of the link
	 * @return
	 */
	public static void visitApplicationAdministration(WebDriver driver, String adminLinkName) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitApplicationAdministration(driver, adminLinkName);
	}

	/**
	 * Navigate to the specified cloud service page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param cloudServiceLinkName
	 *            Name of the link
	 * @return
	 */
	public static void visitApplicationCloudService(WebDriver driver, String cloudServiceLinkName) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitApplicationCloudService(driver, cloudServiceLinkName);
	}

	public static void visitApplicationHome(WebDriver driver, String homeLinkName) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitApplicationHome(driver, homeLinkName);
	}

	/**
	 * Navigate to the specified visual analyzer page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visualAnalyzerLinkName
	 *            Name of the link
	 * @return
	 */
	public static void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitApplicationVisualAnalyzer(driver, visualAnalyzerLinkName);
	}

	/**
	 * Navigate to the dashboard home page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static void visitDashboardHome(WebDriver driver) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitDashboardHome(driver);
	}

	/**
	 * Navigate to My Favorites page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static void visitMyFavorites(WebDriver driver) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitMyFavorites(driver);
	}

	/**
	 * Navigate to my home page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static void visitMyHome(WebDriver driver) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitMyHome(driver);
	}

	/**
	 * Navigate to the welcome page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static void visitWelcome(WebDriver driver) throws Exception
	{
		IBrandingBarUtil bu = new UtilLoader<IBrandingBarUtil>().loadUtil(driver, IBrandingBarUtil.class);
		bu.visitWelcome(driver);
	}

}

