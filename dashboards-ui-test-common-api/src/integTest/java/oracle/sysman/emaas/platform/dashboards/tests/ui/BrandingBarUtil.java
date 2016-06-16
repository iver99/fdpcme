package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class BrandingBarUtil
{
	// values for 'option' for user menu
	public static final String USERMENU_OPTION_HELP = "help";
	public static final String USERMENU_OPTION_ABOUT = "about";
	public static final String USERMENU_OPTION_SIGNOUT = "signout";

	//Branding bar link text
	//Cloud Services
	public static final String NAV_LINK_TEXT_CS_ITA = "IT Analytics";
	public static final String NAV_LINK_TEXT_CS_APM = "APM";
	public static final String NAV_LINK_TEXT_CS_LA = "Log Analytics";
	//Visual Analyzers
	public static final String NAV_LINK_TEXT_VA_ITA = "Analyze";
	public static final String NAV_LINK_TEXT_VA_TA = "Search";
	public static final String NAV_LINK_TEXT_VA_LA = "Log";
	//Administration
	public static final String NAV_LINK_TEXT_ADMIN_AGENT = "Agents";
	public static final String NAV_LINK_TEXT_ADMIN_ITA = "IT Analytics Administration";
	public static final String NAV_LINK_TEXT_ADMIN_ALERT = "Alert Rules";

	/**
	 * Check whether admin field exist
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public static boolean isAdmin(WebDriver driver) throws Exception
	{
		driver.getLogger().info("start validating admin tab");
		//Open the navigation bar if it's not displayed
		BrandingBarUtil.openNavigationBar(driver);
		boolean isDisplayed = driver.isDisplayed("id=" + DashBoardPageId.BrandingBarAdminLinksID);
		return isDisplayed;
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
		Validator.notEmptyString("adminLinkName in [BrandingBarUtil.isAdminLinkExisted]", adminLinkName);
		driver.getLogger().info("Start to check if admin link is existed in navigation bar. Link name: " + adminLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, "admin", adminLinkName);
		driver.getLogger().info("Existence check for admin link is completed. Result: " + isExisted);
		return isExisted;
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
		Validator.notEmptyString("cloudServiceLinkName in [BrandingBarUtil.isCloudServiceLinkExisted]", cloudServiceLinkName);
		driver.getLogger().info(
				"Start to check if cloud service link is existed in navigation bar. Link name: " + cloudServiceLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, "cs", cloudServiceLinkName);
		driver.getLogger().info("Existence check for cloud service link is completed. Result: " + isExisted);
		return isExisted;
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
		driver.getLogger().info("Start to check if 'Dashboards' link is existed in navigation bar.");
		boolean isExisted = false;
		String locator = "id=" + DashBoardPageId.BrandingBarDashboardHomeLinkID;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'Dashboards' link is completed. Result: " + isExisted);
		return isExisted;

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
		Validator.notEmptyString("homeLinkName in [BrandingBarUtil.isHomeLinkExisted]", homeLinkName);
		driver.getLogger().info("Start to check if home link is existed in navigation bar. Link name: " + homeLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, "home", homeLinkName);
		driver.getLogger().info("Existence check for home link is completed. Result: " + isExisted);
		return isExisted;
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
		driver.getLogger().info("Start to check if 'My Favorites' link is existed in navigation bar.");
		boolean isExisted = false;
		String locator = "id=" + DashBoardPageId.BrandingBarMyFavoritesLinkID;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'My Favorites' link is completed. Result: " + isExisted);
		return isExisted;
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
		driver.getLogger().info("Start to check if 'Home' link is existed in navigation bar.");
		boolean isExisted = false;
		String locator = "id=" + DashBoardPageId.BrandingBarMyHomeLinkID;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'Home' link is completed. Result: " + isExisted);
		return isExisted;
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
		Validator.notEmptyString("visualAnalyzerLinkName in [BrandingBarUtil.isVisualAnalyzerLinkExisted]",
				visualAnalyzerLinkName);
		driver.getLogger().info(
				"Start to check if visual analyzer link is existed in navigation bar. Link name: " + visualAnalyzerLinkName);
		boolean isExisted = false;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, "va", visualAnalyzerLinkName);
		driver.getLogger().info("Existence check for visual analyzer link is completed. Result: " + isExisted);
		return isExisted;
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
		driver.getLogger().info("Start to check if 'Welcome' link is existed in navigation bar.");
		boolean isExisted = false;
		String locator = "id=" + DashBoardPageId.BrandingBarWelcomeLinkID;
		isExisted = BrandingBarUtil.isApplicationLinkExisted(driver, locator);
		driver.getLogger().info("Existence check for 'Welcome' link is completed. Result: " + isExisted);
		return isExisted;
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
		Validator.fromValidValues("option", option, USERMENU_OPTION_HELP, USERMENU_OPTION_ABOUT, USERMENU_OPTION_SIGNOUT);

		driver.getLogger().info("Start to do branding bar user menu option: " + option);
		driver.waitForElementPresent(DashBoardPageId.Brand_Bar_User_Menu);
		driver.getLogger().info("Click branding bar user menu button.");
		driver.click(DashBoardPageId.Brand_Bar_User_Menu);

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 900L);
		By locator = By.id(DashBoardPageId.UserMenuPopupId);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		driver.takeScreenShot();
		Assert.assertTrue(driver.isDisplayed("id=" + DashBoardPageId.UserMenuPopupId));
		driver.getLogger().info("User menu popup is displayed.");

		switch (option) {
			case USERMENU_OPTION_HELP:
				driver.takeScreenShot();
				driver.getLogger().info("Click Help menu.");
				driver.click(DashBoardPageId.Option_Help);
				break;
			case USERMENU_OPTION_ABOUT:
				driver.takeScreenShot();
				driver.getLogger().info("Click About menu.");
				driver.click(DashBoardPageId.Option_About);
				driver.takeScreenShot();
				driver.getLogger().info("Close the About dialog.");
				driver.click(DashBoardPageId.AboutDialogClose);
				break;
			case USERMENU_OPTION_SIGNOUT:
				driver.getLogger().info("Click Sign Out menu.");
				driver.click(DashBoardPageId.Option_Logout);
				break;
		}
		driver.takeScreenShot();
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
		Validator.notEmptyString("adminLinkName in [BrandingBarUtil.visitApplicationAdministration]", adminLinkName);
		driver.getLogger().info("Start to visit admin link from branding bar. Link name: " + adminLinkName);
		BrandingBarUtil.visitApplicationLink(driver, "admin", adminLinkName);
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
		Validator.notEmptyString("cloudServiceLinkName in [BrandingBarUtil.visitApplicationCloudService]", cloudServiceLinkName);
		driver.getLogger().info("Start to visit cloud service link from branding bar. Link name: " + cloudServiceLinkName);
		BrandingBarUtil.visitApplicationLink(driver, "cs", cloudServiceLinkName);
	}

	public static void visitApplicationHome(WebDriver driver, String homeLinkName) throws Exception
	{
		Validator.notEmptyString("homeLinkName in [BrandingBarUtil.visitApplicationHome]", homeLinkName);
		driver.getLogger().info("Start to visit home link from branding bar. Link name: " + homeLinkName);
		BrandingBarUtil.visitApplicationLink(driver, "home", homeLinkName);
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
		Validator.notEmptyString("visualAnalyzerLinkName in [BrandingBarUtil.visitApplicationVisualAnalyzer]",
				visualAnalyzerLinkName);
		driver.getLogger().info("Start to visit visual analyzer link from branding bar. Link name: " + visualAnalyzerLinkName);
		BrandingBarUtil.visitApplicationLink(driver, "va", visualAnalyzerLinkName);
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
		driver.getLogger().info("Start to visit 'Dashboards' link from branding bar.");
		BrandingBarUtil.visitApplicationLink(driver, DashBoardPageId.BrandingBarDashboardLinkLocator);
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
		driver.getLogger().info("[BrandingBarUtil.visitMyFavorites] start to visit 'My Favorites' link from branding bar.");
		BrandingBarUtil.visitApplicationLink(driver, DashBoardPageId.BrandingBarMyFavoritesLinkID);
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
		driver.getLogger().info("[BrandingBarUtil.visitMyHome] start to visit 'Home' link from branding bar.");
		BrandingBarUtil.visitApplicationLink(driver, DashBoardPageId.BrandingBarMyHomeLinkID);
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
		driver.getLogger().info("[BrandingBarUtil.visitWelcome] start to visit 'Welcome' link from branding bar.");
		BrandingBarUtil.visitApplicationLink(driver, DashBoardPageId.BrandingBarWelcomeLinkID);
	}

	/**
	 * Check the visibility of the navigation links panel
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visible
	 *            The visibility of the navigation links panel
	 * @return
	 */
	private static void checkLinksMenuVisibility(WebDriver driver, boolean visible)
	{
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), 900L);
		By locator = By.id(DashBoardPageId.BrandingBarNavLinksId);
		if (visible) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			driver.takeScreenShot();
			Assert.assertTrue(driver.isDisplayed("id=" + DashBoardPageId.BrandingBarNavLinksId));
		}
		else {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			driver.takeScreenShot();
			Assert.assertFalse(driver.isDisplayed("id=" + DashBoardPageId.BrandingBarNavLinksId));
		}

	}

	/**
	 * Find the parent div id for the specified link type
	 *
	 * @param linkType
	 *            Type of the link, should be one of "home", "cs", "va", "admin"
	 * @return
	 */
	private static String getApplicationLinkParentId(String linkType)
	{
		String parentId = null;
		switch (linkType) {
			//Home links
			case "home":
				parentId = DashBoardPageId.BrandingBarHomeLinksID;
				break;
			//Cloud service links
			case "cs":
				parentId = DashBoardPageId.BrandingBarCloudServiceLinksID;
				break;
			//Visual analyzer links
			case "va":
				parentId = DashBoardPageId.BrandingBarVisualAnalyzerLinksID;
				break;
			//Administration links
			case "admin":
				parentId = DashBoardPageId.BrandingBarAdminLinksID;
				break;
		}
		return parentId;
	}

	/**
	 * Check if the specified link is exited or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param locator
	 *            Locator of the link
	 * @return
	 */
	private static boolean isApplicationLinkExisted(WebDriver driver, String locator)
	{
		Validator.notEmptyString("locator in [BrandingBarUtil.isApplicationLinkExisted]", locator);
		boolean isExisted = false;
		//Check if navigation bar is already displayed or not
		boolean isNavBarDisplayed = BrandingBarUtil.isNavigationBarDisplayed(driver);
		//If not, open the navigation bar, if yes, just keep it as it is
		BrandingBarUtil.openNavigationBar(driver);
		//Wait until the links menu is displayed
		BrandingBarUtil.checkLinksMenuVisibility(driver, true);
		isExisted = driver.isDisplayed(locator);
		//Recover the navigation bar to the original status
		if (!isNavBarDisplayed) {
			driver.click("id=" + DashBoardPageId.LinkID);
		}
		driver.takeScreenShot();
		return isExisted;
	}

	/**
	 * Check if the specified link is exited or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param linkType
	 *            Type of the link, should be one of "home", "cs", "va", "admin"
	 * @param linkName
	 *            Name of the link
	 * @return
	 */
	private static boolean isApplicationLinkExisted(WebDriver driver, String linkType, String linkName)
	{
		String parentId = BrandingBarUtil.getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";
		return BrandingBarUtil.isApplicationLinkExisted(driver, xpath);
	}

	/**
	 * Check if a element is stale or not
	 *
	 * @param element
	 *            WebElement instance
	 * @return
	 */
	private static boolean isElementStale(WebElement element)
	{
		try {
			element.getText();
			return false;
		}
		catch (StaleElementReferenceException e) {
			return true;
		}
	}

	/**
	 * Check if the navigation bar is displayed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	private static boolean isNavigationBarDisplayed(WebDriver driver)
	{
		boolean isDisplayed = driver.isDisplayed("id=" + DashBoardPageId.BrandingBarNavLinksId);
		driver.getLogger().info("Check if the navigation bar is displayed or not. Result: " + isDisplayed);
		return isDisplayed;
	}

	/**
	 * Open the navigation bar
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	private static void openNavigationBar(WebDriver driver)
	{
		//Open the navigation bar if it's not displayed
		if (!BrandingBarUtil.isNavigationBarDisplayed(driver)) {
			String navBarId = "id=" + DashBoardPageId.LinkID;
			driver.waitForElementPresent(navBarId);
			driver.click(navBarId);
			WaitUtil.waitForPageFullyLoaded(driver);
		}
		driver.takeScreenShot();
	}

	/**
	 * Navigate to the specified application page by locator
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param locator
	 *            Locator the link
	 * @return
	 */
	private static void visitApplicationLink(WebDriver driver, final String locator)
	{
		Validator.notEmptyString("locator in [BrandingBarUtil.visitApplicationLink]", locator);
		// Open navigation bar if it's not displayed
		BrandingBarUtil.openNavigationBar(driver);
		//Wait until the links menu is displayed
		BrandingBarUtil.checkLinksMenuVisibility(driver, true);
		driver.waitForElementPresent(locator);
		//Add try catch to handle StaleElementReferenceException
		try {
			driver.click(locator);
		}
		catch (StaleElementReferenceException e) {
			driver.getLogger().info("StaleElementReferenceException thrown, wait for element becoming not stale");
			// wait until element is not stale
			new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT).until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(org.openqa.selenium.WebDriver d)
				{
					WebElement element = d.findElement(By.xpath(locator));
					Boolean isStale = BrandingBarUtil.isElementStale(element);
					return !isStale;
				}
			});
			driver.click(locator);
		}

		driver.takeScreenShot();
	}

	/**
	 * Navigate to the specified application page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param linkType
	 *            Type of the link, should be one of "home", "cs", "va", "admin"
	 * @param linkName
	 *            Name of the link
	 * @return
	 */
	private static void visitApplicationLink(WebDriver driver, String linkType, String linkName)
	{
		String parentId = BrandingBarUtil.getApplicationLinkParentId(linkType);
		//Find link element by parent id and link name
		String xpath = "//div[@id='" + parentId + "']/descendant::a[text()='" + linkName + "']";
		BrandingBarUtil.visitApplicationLink(driver, xpath);
	}
}
