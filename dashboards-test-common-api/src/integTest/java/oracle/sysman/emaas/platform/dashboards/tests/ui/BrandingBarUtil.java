package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class BrandingBarUtil
{
	// values for 'option' for user menu
	public static final String USERMENU_OPTION_HELP = "help";
	public static final String USERMENU_OPTION_ABOUT = "about";
	public static final String USERMENU_OPTION_SIGNOUT = "signout";

	public static boolean isAdminLinkExisted(WebDriver driver, String adminLinkName)
	{
		driver.getLogger()
				.info("[BrandingBarUtil.isAdminLinkExisted] start to check if admin link is existed in navigation bar. Link name: "
						+ adminLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, "admin", adminLinkName);
		driver.getLogger()
				.info("[BrandingBarUtil.isAdminLinkExisted] existence check for admin link is completed. Result: " + isExisted);
		return isExisted;
	}

	public static boolean isCloudServiceLinkExisted(WebDriver driver, String cloudServiceLinkName)
	{
		driver.getLogger()
				.info("[BrandingBarUtil.isCloudServiceLinkExisted] start to check if cloud service link is existed in navigation bar. Link name: "
						+ cloudServiceLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, "cs", cloudServiceLinkName);
		driver.getLogger()
				.info("[BrandingBarUtil.isCloudServiceLinkExisted] existence check for cloud service link is completed. Result: "
						+ isExisted);
		return isExisted;
	}

	public static boolean isDashboardHomeLinkExisted(WebDriver driver)
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isDashboardHomeLinkExisted] start to check if 'Dashboards' link is existed in navigation bar.");
		boolean isExisted = false;
		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed(driver);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed("id="+DashBoardPageId.BrandingBarDashboardHomeLinkID);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.getLogger()
				.info("[BrandingBarUtil.isDashboardHomeLinkExisted] existence check for 'Dashboards' link is completed. Result: "
						+ isExisted);
		return isExisted;
	}

	public static boolean isHomeLinkExisted(WebDriver driver, String homeLinkName)
	{
		driver.getLogger()
				.info("[BrandingBarUtil.isHomeLinkExisted] start to check if home link is existed in navigation bar. Link name: "
						+ homeLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, "home", homeLinkName);
		driver.getLogger()
				.info("[BrandingBarUtil.isHomeLinkExisted] existence check for home link is completed. Result: " + isExisted);
		return isExisted;
	}

	public static boolean isMyFavoritesLinkExisted(WebDriver driver)
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isMyFavoritesLinkExisted] start to check if 'My Favorites' link is existed in navigation bar.");
		boolean isExisted = false;
		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed(driver);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed("id="+DashBoardPageId.BrandingBarMyFavoritesLinkID);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.getLogger()
				.info("[BrandingBarUtil.isMyFavoritesLinkExisted] existence check for 'My Favorites' link is completed. Result: "
						+ isExisted);
		return isExisted;
	}

	public static boolean isMyHomeLinkExisted(WebDriver driver)
	{
		driver.getLogger()
				.info("[BrandingBarUtil.isMyHomeLinkExisted] start to check if 'Home' link is existed in navigation bar.");
		boolean isExisted = false;
		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed(driver);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed("id="+DashBoardPageId.BrandingBarMyHomeLinkID);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.getLogger()
				.info("[BrandingBarUtil.isMyHomeLinkExisted] existence check for 'Home' link is completed. Result: " + isExisted);
		return isExisted;
	}

	public static boolean isVisualAnalyzerLinkExisted(WebDriver driver, String visualAnalyzerLinkName)
	{
		driver.getLogger()
				.info("[BrandingBarUtil.isVisualAnalyzerLinkExisted] start to check if visual analyzer link is existed in navigation bar. Link name: "
						+ visualAnalyzerLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, "va", visualAnalyzerLinkName);
		driver.getLogger()
				.info("[BrandingBarUtil.isVisualAnalyzerLinkExisted] existence check for visual analyzer link is completed. Result: "
						+ isExisted);
		return isExisted;
	}

	public static boolean isWelcomeLinkExisted(WebDriver driver)
	{
		driver.getLogger()
				.info("[BrandingBarUtil.isWelcomeLinkExisted] start to check if 'Welcome' link is existed in navigation bar.");
		boolean isExisted = false;
		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed(driver);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed("id="+DashBoardPageId.BrandingBarWelcomeLinkID);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.getLogger().info(
				"[BrandingBarUtil.isWelcomeLinkExisted] existence check for 'Welcome' link is completed. Result: " + isExisted);
		return isExisted;
	}

	public static void userMenuOptions(WebDriver driver, String option) throws Exception
	{
		Validator.fromValidValues("option", option, USERMENU_OPTION_HELP, USERMENU_OPTION_ABOUT, USERMENU_OPTION_SIGNOUT);

		driver.getLogger().info("Click brand bar user menur option:" + option);
		driver.waitForElementPresent(DashBoardPageId.Brand_Bar_User_Menu);
		driver.click(DashBoardPageId.Brand_Bar_User_Menu);
		switch (option) {
			case USERMENU_OPTION_HELP:
				driver.takeScreenShot();
				driver.click(DashBoardPageId.Option_Help);
				break;
			case USERMENU_OPTION_ABOUT:
				driver.takeScreenShot();
				driver.click(DashBoardPageId.Option_About);
				driver.takeScreenShot();
				driver.click(DashBoardPageId.AboutDialogClose);
				break;
			case USERMENU_OPTION_SIGNOUT:
				driver.click(DashBoardPageId.Option_Logout);
				break;
		}
		driver.takeScreenShot();
	}

	public static void visitApplicationAdministration(WebDriver driver, String adminLinkName) throws Exception
	{
		if (adminLinkName != null) {
			driver.getLogger()
					.info("[BrandingBarUtil.visitApplicationAdministration] start to visit admin link from branding bar. Link name: "
							+ adminLinkName);
			BrandingBarUtil.visitApplicationLink(driver, "admin", adminLinkName);
		}
		else {
			driver.getLogger().info(
					"[BrandingBarUtil.visitApplicationAdministration] can not visit admin link from branding bar because the given link name is null.");
		}
	}

	public static void visitApplicationCloudService(WebDriver driver, String cloudServiceLinkName) throws Exception
	{
		if (cloudServiceLinkName != null) {
			driver.getLogger()
					.info("[BrandingBarUtil.visitApplicationCloudService] start to visit cloud service link from branding bar. Link name: "
							+ cloudServiceLinkName);
			BrandingBarUtil.visitApplicationLink(driver, "cs", cloudServiceLinkName);
		}
		else {
			driver.getLogger().info(
					"[BrandingBarUtil.visitApplicationCloudService] can not visit cloud service link from branding bar because the given link name is null.");
		}
	}

	public static void visitApplicationHome(WebDriver driver, String homeLinkName) throws Exception
	{
		if (homeLinkName != null) {
			driver.getLogger()
					.info("[BrandingBarUtil.visitApplicationHome] start to visit home link from branding bar. Link name: "
							+ homeLinkName);
			BrandingBarUtil.visitApplicationLink(driver, "home", homeLinkName);
		}
		else {
			driver.getLogger().info(
					"[BrandingBarUtil.visitApplicationHome] can not visit home link from branding bar because the given link name is null.");
		}
	}

	public static void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName) throws Exception
	{
		if (visualAnalyzerLinkName != null) {
			driver.getLogger()
					.info("[BrandingBarUtil.visitApplicationVisualAnalyzer] start to visit visual analyzer link from branding bar. Link name: "
							+ visualAnalyzerLinkName);
			BrandingBarUtil.visitApplicationLink(driver, "va", visualAnalyzerLinkName);
		}
		else {
			driver.getLogger().info(
					"[BrandingBarUtil.visitApplicationVisualAnalyzer] can not visit visual analyzer link from branding bar because the given link name is null.");
		}
	}

	public static void visitDashboardHome(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[BrandingBarUtil.visitDashboardHome] start to visit 'Dashboards' link from branding bar.");
		if (!BrandingBarUtil.isNavigationBarDisplayed(driver)) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.waitForElementPresent(DashBoardPageId.BrandingBarDashboardLinkLocator);
		driver.check(DashBoardPageId.BrandingBarDashboardLinkLocator);
		driver.takeScreenShot();
	}

	public static void visitMyFavorites(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[BrandingBarUtil.visitMyFavorites] start to visit 'My Favorites' link from branding bar.");
		if (!BrandingBarUtil.isNavigationBarDisplayed(driver)) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.waitForElementPresent("id="+DashBoardPageId.BrandingBarMyFavoritesLinkID);
		driver.click("id="+DashBoardPageId.BrandingBarMyFavoritesLinkID);
		driver.takeScreenShot();
	}

	public static void visitMyHome(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[BrandingBarUtil.visitMyHome] start to visit 'Home' link from branding bar.");
		if (!BrandingBarUtil.isNavigationBarDisplayed(driver)) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.click("id="+DashBoardPageId.BrandingBarMyHomeLinkID);
		driver.takeScreenShot();
	}

	public static void visitWelcome(WebDriver driver) throws Exception
	{
		driver.getLogger().info("[BrandingBarUtil.visitWelcome] start to visit 'Welcome' link from branding bar.");
		if (!BrandingBarUtil.isNavigationBarDisplayed(driver)) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.click("id="+DashBoardPageId.BrandingBarWelcomeLinkID);
		driver.takeScreenShot();
	}

	private static String getApplicationLinkParentId(String linkType)
	{
		String parentId = null;
		switch (linkType) {
			case "home":
				parentId = DashBoardPageId.BrandingBarHomeLinksID;
				break;
			case "cs":
				parentId = DashBoardPageId.BrandingBarCloudServiceLinksID;
				break;
			case "va":
				parentId = DashBoardPageId.BrandingBarVisualAnalyzerLinksID;
				break;
			case "admin":
				parentId = DashBoardPageId.BrandingBarAdminLinksID;
				break;
		}
		return parentId;
	}

	private static boolean isApplicationLinkExisted(WebDriver driver, String linkType, String linkName)
	{
		boolean isExisted = false;
		String parentId = BrandingBarUtil.getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";

		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed(driver);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed(xpath);
		if (!isNavBarDisplayed) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		return isExisted;
	}

	private static boolean isNavigationBarDisplayed(WebDriver driver)
	{
		return driver.isDisplayed("id="+DashBoardPageId.BrandingBarNavLinksId);
	}

	private static void visitApplicationLink(WebDriver driver, String linkType, String linkName)
	{
		String parentId = BrandingBarUtil.getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";
		if (!BrandingBarUtil.isNavigationBarDisplayed(driver)) {
			driver.click("id="+DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.click(xpath);
		driver.takeScreenShot();
	}
}
