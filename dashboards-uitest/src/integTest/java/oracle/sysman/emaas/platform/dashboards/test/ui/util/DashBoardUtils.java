package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DashBoardUtils
{
	private static WebDriver driver;

	public static void APM_OOB_GridView() throws Exception
	{
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.waitForDashboardPresent(driver, "Application Performance Monitoring");

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Monitoring"));
	}

	public static void clickDashboardLinkInBrandingBar(WebDriver webdriver) throws Exception
	{
		webdriver.getLogger().info("Click Compass icon to display menu of branding bar");
		webdriver.getWebDriver().findElement(By.xpath(PageId.CompassIcon)).click();
		WebDriverWait wait = new WebDriverWait(webdriver.getWebDriver(), 900L);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PageId.DashboardLink)));
		webdriver.takeScreenShot();
		webdriver.getLogger().info("Click Dashboard link to back to dashboard home page");
		webdriver.getWebDriver().findElement(By.xpath(PageId.DashboardLink)).click();
		webdriver.takeScreenShot();
	}

	public static void closeOverviewPage() throws Exception
	{
		driver.getLogger().info("before clicking overview button");
		driver.click(PageId.OverviewCloseID);
		driver.getLogger().info("after clicking overview button");
	}

	public static void handleAlert(WebDriver webdriver) throws Exception
	{
		WebDriverWait wait = new WebDriverWait(webdriver.getWebDriver(), 900L);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());

		//Accepting alert.
		webdriver.getLogger().info("foucus on the alert");
		alert = webdriver.getWebDriver().switchTo().alert();
		webdriver.takeScreenShot();
		webdriver.getLogger().info("click button on the dialog, should navigate to the home page");
		alert.accept();
		webdriver.takeScreenShot();
	}

	public static void ITA_OOB_GridView() throws Exception
	{
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.waitForDashboardPresent(driver, "Performance Analytics: Database");

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Analytics"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Availability Analytics"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Host"));
		//Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Health Summary"));
		//Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Health Summary"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics Application Server"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));
		//Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Health Summary"));
		//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Configuration and Storage By Version"));
		//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Overview"));
		//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Inventory By Platform"));
		//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 Databases by Resource Consumption"));
		//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Heap Usage"));
		//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Load"));
		//		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Servers by JDK Version"));
	}

	public static void LA_OOB_GridView() throws Exception
	{
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.waitForDashboardPresent(driver, "Database Operations");

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Middleware Operations"));
	}

	public static void loadWebDriver(WebDriver webDriver) throws Exception
	{
		driver = webDriver;

		if (driver.isDisplayed(PageId.OverviewCloseID)) {
			DashBoardUtils.closeOverviewPage();
		}

		Assert.assertFalse(driver.isDisplayed(PageId.OverviewCloseID));

		driver.takeScreenShot();
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void noOOBCheck_GridView() throws Exception
	{
		//verify all the oob dashboard not exsit
		driver.getLogger().info("verify all the oob dashboard not exsit");
		Assert.assertFalse(driver.isElementPresent(PageId.Application_Performance_Monitoring_ID));
		//Assert.assertFalse(driver.isElementPresent(PageId.Database_Health_Summary_ID));
		//Assert.assertFalse(driver.isElementPresent(PageId.Host_Health_Summary_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Database_Performance_Analytics_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Middleware_Performance_Analytics_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Database_Resource_Analytics_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Middleware_Resource_Analytics_ID));
		//Assert.assertFalse(driver.isElementPresent(PageId.WebLogic_Health_Summary_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Database_Configuration_and_Storage_By_Version_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Enterprise_OverView_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Host_Inventory_By_Platform_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Top_25_Databases_by_Resource_Consumption_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Top_25_WebLogic_Servers_by_Heap_Usage_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Top_25_WebLogic_Servers_by_Load_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.WebLogic_Servers_by_JDK_Version_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Database_Operations_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Host_Operations_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Middleware_Operations_ID));
	}

	public static boolean verfiyShareOptionDisabled() throws Exception
	{
		driver.getLogger().info("Click the option icon of Dashboard Set");
		driver.waitForElementPresent("css=" + PageId.DashboardSetOptions_Css);
		driver.click("css=" + PageId.DashboardSetOptions_Css);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.getLogger().info("Click Edit icon");
		driver.waitForElementPresent("css=" + PageId.DashboardSetOptionsEdit_CSS);
		driver.click("css=" + PageId.DashboardSetOptionsEdit_CSS);
		driver.takeScreenShot();

		driver.getLogger().info("Expand Share options");
		driver.waitForElementPresent("css=" + PageId.RightDrawerEditSingleDBShare_CSS);
		driver.click("css=" + PageId.RightDrawerEditSingleDBShare_CSS);
		driver.takeScreenShot();

		driver.getLogger().info("Verify the options are disabled or not");
		WebElement ShareOption = driver.getWebDriver().findElement(By.cssSelector(PageId.DashboardSetShare_Css));
		WebElement NotShareOption = driver.getWebDriver().findElement(By.cssSelector(PageId.DashboardSetNotShare_Css));
		if (!ShareOption.isEnabled() & !NotShareOption.isEnabled()) {
			driver.getLogger().info("The Options are disabled!");
			return true;
		}
		else {
			driver.getLogger().info("Have the option enabled");
			return false;
		}
	}
}
