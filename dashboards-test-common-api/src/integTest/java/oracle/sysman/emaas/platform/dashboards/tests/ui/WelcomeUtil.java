package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;

public class WelcomeUtil
{

	private static WebDriver driver;

	public static void dataExplorers(WebDriver webd, String selection) throws Exception
	{
		webd.getLogger().info("Visiting Data Explorer-" + selection + " from Welcome Page...");
		webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_DataExp_SelectID)).click();
		switch (selection) {
			case "log":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_DataExp_Log)).click();
				break;
			case "analyze":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_DataExp_Analyze)).click();
				break;
			case "search":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_DataExp_Search)).click();
				break;
		}
		webd.takeScreenShot();
	}

	public static void learnMoreHow(WebDriver webd) throws Exception
	{
		webd.getLogger().info("Visiting 'Learn More-How to get started' from Welcome Page...");
		webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_LearnMore_getStarted)).click();
		webd.takeScreenShot();
	}

	public static void learnMoreServiceOffering(WebDriver webd) throws Exception
	{
		webd.getLogger().info("Visiting 'Learn More-Service Offerings' from Welcome Page...");
		webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_LearnMore_ServiceOffering)).click();
		webd.takeScreenShot();
	}

	public static void learnMoreVideo(WebDriver webd) throws Exception
	{
		webd.getLogger().info("Visiting 'Learn More-Videos' from Welcome Page...");
		webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_LearnMore_Videos)).click();
		webd.takeScreenShot();
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void visitAPM(WebDriver webd) throws Exception
	{
		webd.getLogger().info("Visit APM from Welcome Page...");
		webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_APMLinkCSS)).click();
		webd.takeScreenShot();
	}

	public static void visitDashboards(WebDriver webd) throws Exception
	{
		webd.getLogger().info("Visit Dashboards from Welcome Page...");
		webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_DashboardsLinkID)).click();
		webd.takeScreenShot();
	}

	public static void visitITA(WebDriver webd, String selection) throws Exception
	{
		webd.getLogger().info("Visiting ITA-" + selection + " from Welcome Page...");
		webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_ITA_SelectID)).click();
		switch (selection) {
			case "default":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_ITALinkID)).click();
				break;
			case "performanceAnalyticsDatabase":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_ITA_PADatabase)).click();
				break;
			case "performanceAnalyticsMiddleware":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_ITA_PAMiddleware)).click();
				break;
			case "resourceAnalyticsDatabase":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_ITA_RADatabase)).click();
				break;
			case "resourceAnalyticsMiddleware":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_ITA_RAMiddleware)).click();
				break;
			case "dataExplorerAnalyze":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_ITA_DEAnalyze)).click();
				break;
			case "dataExplorer":
				webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_ITA_DE)).click();
				break;
		}
		webd.takeScreenShot();
	}

	public static void visitLA(WebDriver webd) throws Exception
	{
		webd.getLogger().info("Visiting LA from Welcome Page...");
		webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.Welcome_LALinkCSS)).click();
		webd.takeScreenShot();
	}
}