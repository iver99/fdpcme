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

	public static void APM_OOB_Exist() throws Exception
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Application Performance Monitoring");

		driver.getLogger().info("Verify below APM OOB dashboard exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Monitoring"));
	}

	public static void APM_OOB_NotExist() throws Exception
	{
		driver.getLogger().info("Verify below APM OOB don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Monitoring"));
	}

	public static void clickDashboardLinkInBrandingBar(WebDriver webdriver) throws Exception
	{
		webdriver.getLogger().info("Click Compass icon to display menu of branding bar");
		webdriver.getWebDriver().findElement(By.xpath(PageId.COMPASSICON)).click();
		WebDriverWait wait = new WebDriverWait(webdriver.getWebDriver(), 900L);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PageId.DASHBOARDLINK)));
		webdriver.takeScreenShot();
		webdriver.getLogger().info("Click Dashboard link to back to dashboard home page");
		webdriver.getWebDriver().findElement(By.xpath(PageId.DASHBOARDLINK)).click();
		webdriver.takeScreenShot();
	}

	public static void closeOverviewPage() throws Exception
	{
		driver.getLogger().info("before clicking overview button");
		driver.click(PageId.OVERVIEWCLOSEID);
		driver.getLogger().info("after clicking overview button");
	}

	public static void deleteDashboard(WebDriver webdriver, String DashboardName) throws Exception
	{
		if (DashboardHomeUtil.isDashboardExisted(webdriver, DashboardName)) {
			webdriver.getLogger().info("Start to delete the dashboard: " + DashboardName);
			DashboardHomeUtil.deleteDashboard(webdriver, DashboardName, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
			webdriver.getLogger().info("Verify the dashboard: " + DashboardName + " has been deleted");
			Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webdriver, DashboardName), "Delete dashboard "
					+ DashboardName + " failed!");
			webdriver.getLogger().info("Delete the dashboard: " + DashboardName + " finished");
		}

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

	public static void ITA_OOB_Exist() throws Exception
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Performance Analytics: Database");

		driver.getLogger().info("Verify below IT Analytics OOB dashboard Set and Dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "UI Gallery"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Analytics"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Availability Analytics"));

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics Application Server"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Host"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Categorical"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Others"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Overview"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Timeseries"));
		;
	}

	public static void ITA_OOB_NotExist() throws Exception
	{
		driver.getLogger().info("Verify below IT Analytics OOB dashboard Set and Dashboards don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "UI Gallery"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Analytics"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Availability Analytics"));

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics Application Server"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Host"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Categorical"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Others"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Overview"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Performance"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Timeseries"));

	}

	public static void LA_OOB_Exist() throws Exception
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Database Operations");

		driver.getLogger().info("Verify below Log Analytics OOB dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Middleware Operations"));
	}

	public static void LA_OOB_NotExist() throws Exception
	{
		driver.getLogger().info("Verify below Log Analytics OOB don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Database Health Summary"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Host Health Summary"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Health Summary"));

	}

	public static void loadWebDriver(WebDriver webDriver) throws Exception
	{
		driver = webDriver;

		if (driver.isDisplayed(PageId.OVERVIEWCLOSEID)) {
			DashBoardUtils.closeOverviewPage();
		}

		Assert.assertFalse(driver.isDisplayed(PageId.OVERVIEWCLOSEID));

		driver.takeScreenShot();
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void noOOBCheck() throws Exception
	{
		//verify all the oob dashboard not exsit
		driver.getLogger().info("verify all the oob dashboard not exsit");
		DashBoardUtils.APM_OOB_NotExist();
		DashBoardUtils.ITA_OOB_NotExist();
		DashBoardUtils.LA_OOB_NotExist();
		DashBoardUtils.Outdate_OOB();

	}

	public static void Outdate_OOB() throws Exception
	{
		driver.getLogger().info("Verify below IT Analytics OOB don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Database Configuration and Storage By Version"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Overview"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Host Inventory by Platform"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 Databases by Resource Consumption"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Heap Usage"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Load"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Servers by JDK Version"));
	}

	public static boolean verfiyShareOptionDisabled() throws Exception
	{
		driver.getLogger().info("Click the option icon of Dashboard Set");
		driver.waitForElementPresent("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		driver.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.getLogger().info("Click Edit icon");
		driver.waitForElementPresent("css=" + PageId.DASHBOARDSETOPTIONSEDIT_CSS);
		driver.click("css=" + PageId.DASHBOARDSETOPTIONSEDIT_CSS);
		driver.takeScreenShot();

		driver.getLogger().info("Expand Share options");
		driver.waitForElementPresent("css=" + PageId.RIGHTDRAWEREDITSINGLEDBSHARE_CSS);
		driver.click("css=" + PageId.RIGHTDRAWEREDITSINGLEDBSHARE_CSS);
		driver.takeScreenShot();

		driver.getLogger().info("Verify the options are disabled or not");
		WebElement ShareOption = driver.getWebDriver().findElement(By.cssSelector(PageId.DASHBOARDSETSHARE_CSS));
		WebElement NotShareOption = driver.getWebDriver().findElement(By.cssSelector(PageId.DASHBOARDSETNOTSHARE_CSS));
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
