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

	public static void apmOobExist()
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Application Performance Monitoring");

		driver.getLogger().info("Verify below APM OOB dashboard exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Monitoring"));
	}

	public static void apmOobNotExist()
	{
		driver.getLogger().info("Verify below APM OOB don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Monitoring"));
	}

	public static void clickDashboardLinkInBrandingBar(WebDriver webdriver)
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

	public static void closeOverviewPage()
	{
		driver.getLogger().info("before clicking overview button");
		driver.click(PageId.OVERVIEWCLOSEID);
		driver.getLogger().info("after clicking overview button");
	}

	public static void deleteDashboard(WebDriver webdriver, String DashboardName)
	{
		DashboardHomeUtil.search(webdriver, DashboardName);
		if (DashboardHomeUtil.isDashboardExisted(webdriver, DashboardName)) {
			webdriver.getLogger().info("Start to delete the dashboard: " + DashboardName);
			DashboardHomeUtil.deleteDashboard(webdriver, DashboardName, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
			webdriver.getLogger().info("Verify the dashboard: " + DashboardName + " has been deleted");
			Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webdriver, DashboardName), "Delete dashboard "
					+ DashboardName + " failed!");
			webdriver.getLogger().info("Delete the dashboard: " + DashboardName + " finished");
		}

	}

	public static void handleAlert(WebDriver webdriver)
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

	public static void itaOobExist()
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Performance Analytics: Database");

		driver.getLogger().info("Verify below IT Analytics OOB dashboard Set and Dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Health"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "UI Gallery"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Application Performance Analytics"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Availability Analytics"));

		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics Application Server"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Performance Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Host"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Resource Analytics: Middleware"));
	}

	public static void itaOobNotExist()
	{
		driver.getLogger().info("Verify below IT Analytics OOB dashboard Set and Dashboards don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Exadata Health"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "UI Gallery"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Health"));
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

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Entities"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Summary"));

	}

	public static void laOobExist()
	{
		driver.getLogger().info("Wait for dashboards loading...");
		DashboardHomeUtil.waitForDashboardPresent(driver, "Database Operations");

		driver.getLogger().info("Verify below Log Analytics OOB dashboards exist...");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Database Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Host Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(driver, "Middleware Operations"));
	}

	public static void laOobNotExist()
	{
		driver.getLogger().info("Verify below Log Analytics OOB don't exist...");

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Database Operations"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Host Operations"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Middleware Operations"));
	}

	public static void loadWebDriver(WebDriver webDriver)
	{
		driver = webDriver;

		if (driver.isDisplayed(PageId.OVERVIEWCLOSEID)) {
			DashBoardUtils.closeOverviewPage();
		}

		Assert.assertFalse(driver.isDisplayed(PageId.OVERVIEWCLOSEID));

		driver.takeScreenShot();
	}

	public static void loadWebDriverOnly(WebDriver webDriver)
	{
		driver = webDriver;
	}

	public static void noOOBCheck()
	{
		//verify all the oob dashboard not exsit
		driver.getLogger().info("verify all the oob dashboard not exsit");
		DashBoardUtils.apmOobNotExist();
		DashBoardUtils.itaOobNotExist();
		DashBoardUtils.laOobNotExist();
		DashBoardUtils.outDateOob();

	}

	public static void outDateOob()
	{
		driver.getLogger().info("Verify below IT Analytics OOB don't exist...");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Database Configuration and Storage By Version"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Enterprise Overview"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Host Inventory by Platform"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 Databases by Resource Consumption"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Heap Usage"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Top 25 WebLogic Servers by Load"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Servers by JDK Version"));

		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Database Health Summary"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "Host Health Summary"));
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(driver, "WebLogic Health Summary"));
	}

	public static boolean verfiyShareOptionDisabled()
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

	public static void verifyURL(WebDriver webdriver, String url)
	{
		String currurl = webdriver.getWebDriver().getCurrentUrl();

		webdriver.getLogger().info("the origin url = " + currurl);

		String tmpurl = DashBoardUtils.trimUrlParameters(currurl.substring(currurl.indexOf("emsaasui") + 9));

		webdriver.getLogger().info("the url without para = " + tmpurl);

		Assert.assertEquals(tmpurl, url);

	}

	public static void verifyURL_WithPara(WebDriver webdriver, String url)
	{
		String currurl = webdriver.getWebDriver().getCurrentUrl();

		webdriver.getLogger().info("the origin url = " + currurl);

		String tmpurl = currurl.substring(currurl.indexOf("emsaasui") + 9);

		webdriver.getLogger().info("the url want to compare = " + tmpurl);

		Assert.assertEquals(tmpurl, url);
	}

	private static String trimUrlParameters(String url)
	{
		String baseUrl = null;
		if (url != null) {
			String[] urlComponents = url.split("\\#|\\?");
			baseUrl = urlComponents[0];
		}

		return baseUrl;
	}

	private DashBoardUtils()
	{
	}
}
