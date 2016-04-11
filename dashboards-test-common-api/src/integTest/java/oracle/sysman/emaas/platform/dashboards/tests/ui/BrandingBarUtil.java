package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class BrandingBarUtil
{

	private static WebDriver driver;

	public static boolean isAdminLinkExisted(String adminLinkName)
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isAdminLinkExisted] start to check if admin link is existed in navigation bar. Link name: "
						+ adminLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted("admin", adminLinkName);
		driver.getLogger().info(
				"[BrandingBarUtil.isAdminLinkExisted] existence check for admin link is completed. Result: " + isExisted);
		return isExisted;
	}

	public static boolean isCloudServiceLinkExisted(String cloudServiceLinkName)
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isCloudServiceLinkExisted] start to check if cloud service link is existed in navigation bar. Link name: "
						+ cloudServiceLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted("cs", cloudServiceLinkName);
		driver.getLogger().info(
				"[BrandingBarUtil.isCloudServiceLinkExisted] existence check for cloud service link is completed. Result: "
						+ isExisted);
		return isExisted;
	}

	public static boolean isDashboardHomeLinkExisted()
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isDashboardHomeLinkExisted] start to check if 'Dashboards' link is existed in navigation bar.");
		boolean isExisted = false;
		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed();
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed(DashBoardPageId.BrandingBarDashboardHomeLinkID);
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.getLogger().info(
				"[BrandingBarUtil.isDashboardHomeLinkExisted] existence check for 'Dashboards' link is completed. Result: "
						+ isExisted);
		return isExisted;
	}

	public static boolean isHomeLinkExisted(String homeLinkName)
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isHomeLinkExisted] start to check if home link is existed in navigation bar. Link name: "
						+ homeLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted("home", homeLinkName);
		driver.getLogger().info(
				"[BrandingBarUtil.isHomeLinkExisted] existence check for home link is completed. Result: " + isExisted);
		return isExisted;
	}

	public static boolean isMyFavoritesLinkExisted()
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isMyFavoritesLinkExisted] start to check if 'My Favorites' link is existed in navigation bar.");
		boolean isExisted = false;
		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed();
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed(DashBoardPageId.BrandingBarMyFavoritesLinkID);
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.getLogger().info(
				"[BrandingBarUtil.isMyFavoritesLinkExisted] existence check for 'My Favorites' link is completed. Result: "
						+ isExisted);
		return isExisted;
	}

	public static boolean isMyHomeLinkExisted()
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isMyHomeLinkExisted] start to check if 'Home' link is existed in navigation bar.");
		boolean isExisted = false;
		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed();
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed(DashBoardPageId.BrandingBarMyHomeLinkID);
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.getLogger().info(
				"[BrandingBarUtil.isMyHomeLinkExisted] existence check for 'Home' link is completed. Result: " + isExisted);
		return isExisted;
	}

	public static boolean isVisualAnalyzerLinkExisted(String visualAnalyzerLinkName)
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isVisualAnalyzerLinkExisted] start to check if visual analyzer link is existed in navigation bar. Link name: "
						+ visualAnalyzerLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted("va", visualAnalyzerLinkName);
		driver.getLogger().info(
				"[BrandingBarUtil.isVisualAnalyzerLinkExisted] existence check for visual analyzer link is completed. Result: "
						+ isExisted);
		return isExisted;
	}

	public static boolean isWelcomeLinkExisted()
	{
		driver.getLogger().info(
				"[BrandingBarUtil.isWelcomeLinkExisted] start to check if 'Welcome' link is existed in navigation bar.");
		boolean isExisted = false;
		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed();
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed(DashBoardPageId.BrandingBarWelcomeLinkID);
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.getLogger().info(
				"[BrandingBarUtil.isWelcomeLinkExisted] existence check for 'Welcome' link is completed. Result: " + isExisted);
		return isExisted;
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void userMenuOptions(String option) throws Exception
	{
		if (option == null) {
			return;
		}
		driver.getLogger().info("Click brand bar user menur option:" + option);
		driver.waitForElementPresent(DashBoardPageId.Brand_Bar_User_Menu);
		driver.click(DashBoardPageId.Brand_Bar_User_Menu);
		switch (option) {
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

	public static void visitApplicationAdministration(String adminLinkName) throws Exception
	{
		if (adminLinkName != null) {
			driver.getLogger().info(
					"[BrandingBarUtil.visitApplicationAdministration] start to visit admin link from branding bar. Link name: "
							+ adminLinkName);
			BrandingBarUtil.visitApplicationLink("admin", adminLinkName);
		}
		else {
			driver.getLogger()
					.info("[BrandingBarUtil.visitApplicationAdministration] can not visit admin link from branding bar because the given link name is null.");
		}
	}

	public static void visitApplicationCloudService(String cloudServiceLinkName) throws Exception
	{
		if (cloudServiceLinkName != null) {
			driver.getLogger().info(
					"[BrandingBarUtil.visitApplicationCloudService] start to visit cloud service link from branding bar. Link name: "
							+ cloudServiceLinkName);
			BrandingBarUtil.visitApplicationLink("cs", cloudServiceLinkName);
		}
		else {
			driver.getLogger()
					.info("[BrandingBarUtil.visitApplicationCloudService] can not visit cloud service link from branding bar because the given link name is null.");
		}
	}

	public static void visitApplicationHome(String homeLinkName) throws Exception
	{
		if (homeLinkName != null) {
			driver.getLogger().info(
					"[BrandingBarUtil.visitApplicationHome] start to visit home link from branding bar. Link name: "
							+ homeLinkName);
			BrandingBarUtil.visitApplicationLink("home", homeLinkName);
		}
		else {
			driver.getLogger()
					.info("[BrandingBarUtil.visitApplicationHome] can not visit home link from branding bar because the given link name is null.");
		}
	}

	public static void visitApplicationVisualAnalyzer(String visualAnalyzerLinkName) throws Exception
	{
		if (visualAnalyzerLinkName != null) {
			driver.getLogger().info(
					"[BrandingBarUtil.visitApplicationVisualAnalyzer] start to visit visual analyzer link from branding bar. Link name: "
							+ visualAnalyzerLinkName);
			BrandingBarUtil.visitApplicationLink("va", visualAnalyzerLinkName);
		}
		else {
			driver.getLogger()
					.info("[BrandingBarUtil.visitApplicationVisualAnalyzer] can not visit visual analyzer link from branding bar because the given link name is null.");
		}
	}

	public static void visitDashboardHome() throws Exception
	{
		driver.getLogger().info("[BrandingBarUtil.visitDashboardHome] start to visit 'Dashboards' link from branding bar.");
		if (!BrandingBarUtil.isNavigationBarDisplayed()) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.waitForElementPresent(DashBoardPageId.BrandingBarDashboardLinkLocator);
		driver.check(DashBoardPageId.BrandingBarDashboardLinkLocator);
		driver.takeScreenShot();
	}

	public static void visitMyFavorites() throws Exception
	{
		driver.getLogger().info("[BrandingBarUtil.visitMyFavorites] start to visit 'My Favorites' link from branding bar.");
		if (!BrandingBarUtil.isNavigationBarDisplayed()) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.waitForElementPresent(DashBoardPageId.BrandingBarMyFavoritesLinkID);
		driver.click(DashBoardPageId.BrandingBarMyFavoritesLinkID);
	}

	public static void visitMyHome() throws Exception
	{
		driver.getLogger().info("[BrandingBarUtil.visitMyHome] start to visit 'Home' link from branding bar.");
		if (!BrandingBarUtil.isNavigationBarDisplayed()) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.click(DashBoardPageId.BrandingBarMyHomeLinkID);
		driver.takeScreenShot();
	}

	public static void visitWelcome() throws Exception
	{
		driver.getLogger().info("[BrandingBarUtil.visitWelcome] start to visit 'Welcome' link from branding bar.");
		if (!BrandingBarUtil.isNavigationBarDisplayed()) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.click(DashBoardPageId.BrandingBarWelcomeLinkID);
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

	private static boolean isApplicationLinkExisted(String linkType, String linkName)
	{
		boolean isExisted = false;
		String parentId = BrandingBarUtil.getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";

		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed();
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		isExisted = driver.isDisplayed(xpath);
		if (!isNavBarDisplayed) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		return isExisted;
	}

	private static boolean isNavigationBarDisplayed()
	{
		return driver.isDisplayed(DashBoardPageId.BrandingBarNavLinksId);
	}

	private static void visitApplicationLink(String linkType, String linkName)
	{
		String parentId = BrandingBarUtil.getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";
		if (!BrandingBarUtil.isNavigationBarDisplayed()) {
			driver.click(DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		driver.click(xpath);
		driver.takeScreenShot();
	}
}
