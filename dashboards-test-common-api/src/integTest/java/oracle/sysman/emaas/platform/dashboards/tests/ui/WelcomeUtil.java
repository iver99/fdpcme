package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WelcomeUtil
{
	public static void dataExplorers(WebDriver driver, String selection) throws Exception
	{
		String eleXpath = null;
		driver.getLogger().info("Visiting Data Explorer-" + selection + " from Welcome Page...");
		driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.Welcome_DataExp_SelectID);
		driver.click("id=oj-select-choice-" + DashBoardPageId.Welcome_DataExp_SelectID);
		driver.takeScreenShot();
		switch (selection) {
			case "log":
				eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.Welcome_DataExp_SelectID,
						DashBoardPageId.Welcome_DataExp_Log);
				break;
			case "analyze":
				eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.Welcome_DataExp_SelectID,
						DashBoardPageId.Welcome_DataExp_Analyze);
				break;
			case "search":
				eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.Welcome_DataExp_SelectID,
						DashBoardPageId.Welcome_DataExp_Search);
				break;
		}
		driver.getWebDriver().findElement(By.xpath(eleXpath)).click();
		driver.takeScreenShot();
	}

	public static String getExpectedText(String serviceName)
	{
		String expectedName = null;
		switch (serviceName) {
			case "APM":
				expectedName = "Application Performance Monitoring";
				break;
			case "LA":
				expectedName = "Log Analytics";
				break;
			case "ITA":
				expectedName = "IT Analytics";
				break;
			case "dashboards":
				expectedName = "Dashboards";
				break;
			case "dataExplorers":
				expectedName = "Data Explorers";
				break;
			case "getStarted":
				expectedName = "How to get started";
				break;
			case "videos":
				expectedName = "Videos";
				break;
			case "serviceOfferings":
				expectedName = "Service Offerings";
				break;
		}
		return expectedName;
	}

	public static String getLearnMoreItemId(String itemName)
	{
		String itemId = null;
		switch (itemName) {
			case "getStarted":
				itemId = DashBoardPageId.Welcome_LearnMore_getStarted;
				break;
			case "videos":
				itemId = DashBoardPageId.Welcome_LearnMore_Videos;
				break;
			case "serviceOfferings":
				itemId = DashBoardPageId.Welcome_LearnMore_ServiceOffering;
				break;
		}
		return itemId;
	}

	public static String getOptionXpath(WebDriver driver, String selectId, String optionId) throws Exception
	{
		String optionXpath;
		WebElement li = driver.getWebDriver().findElement(By.id(optionId));
		List<WebElement> list = driver.getWebDriver().findElements(By.xpath("//select[@id='" + selectId + "']/option"));
		int index = list.indexOf(li);
		optionXpath = "//ul[@id='oj-listbox-results-" + selectId + "']/li[" + (index + 1) + "]/div";
		return optionXpath;
	}

	public static String getServiceWrapperId(String serviceName)
	{
		String serviceWrapperId = null;
		switch (serviceName) {
			case "APM":
				serviceWrapperId = DashBoardPageId.Welcome_APMLinkCSS;
				break;
			case "LA":
				serviceWrapperId = DashBoardPageId.Welcome_LALinkCSS;
				break;
			case "ITA":
				serviceWrapperId = DashBoardPageId.Welcome_ITALinkID;
				break;
			case "dashboards":
				serviceWrapperId = DashBoardPageId.Welcome_DashboardsLinkID;
				break;
			case "dataExplorers":
				serviceWrapperId = DashBoardPageId.Welcome_DataExp;
				break;
		}
		return serviceWrapperId;
	}

	public static boolean isLearnMoreItemExisted(WebDriver driver, String itemName)
	{
		driver.getLogger().info("Start to check if learn more item: " + itemName + " is existed in welcome page...");
		boolean isExisted = false;
		String itemId = WelcomeUtil.getLearnMoreItemId(itemName);
		String nameExpected = WelcomeUtil.getExpectedText(itemName);
		driver.waitForElementPresent("id=" + itemId);
		driver.waitForText("id=" + itemId, nameExpected);
		String nameDisplayed = driver.getWebDriver().findElement(By.id(itemId)).getText();
		if (nameDisplayed.equals(nameExpected)) {
			isExisted = true;
		}
		driver.getLogger().info("Check of learn more item: " + itemName + " existence is finished! Result: " + isExisted);
		return isExisted;
	}

	public static boolean isServiceExistedInWelcome(WebDriver driver, String serviceName) throws Exception
	{
		driver.getLogger().info("Start to check if service: " + serviceName + " is existed in welcome page...");
		boolean isExisted = false;
		String serviceWrapperId = WelcomeUtil.getServiceWrapperId(serviceName);
		String xpath = "//*[@id='"
				+ serviceWrapperId
				+ "']/div[@class='service-box-wrapper']/div[@class='landing-home-box-content']/div[@class='landing-home-box-content-head']";

		String nameExpected = WelcomeUtil.getExpectedText(serviceName);
		driver.waitForElementPresent(xpath);
		driver.waitForText(xpath, nameExpected);
		String nameDisplayed = driver.getWebDriver().findElement(By.xpath(xpath)).getText();
		if (nameDisplayed.equals(nameExpected)) {
			isExisted = true;
		}
		driver.getLogger().info(
				"Check of service: " + serviceName + " existence in welcome page is finished! Result: " + isExisted);
		return isExisted;
	}

	public static void learnMoreHow(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visiting 'Learn More-How to get started' from Welcome Page...");
		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_LearnMore_getStarted);
		driver.click("id=" + DashBoardPageId.Welcome_LearnMore_getStarted);
		driver.takeScreenShot();
	}

	public static void learnMoreServiceOffering(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visiting 'Learn More-Service Offerings' from Welcome Page...");
		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_LearnMore_ServiceOffering);
		driver.click("id=" + DashBoardPageId.Welcome_LearnMore_ServiceOffering);
		driver.takeScreenShot();
	}

	public static void learnMoreVideo(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visiting 'Learn More-Videos' from Welcome Page...");
		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_LearnMore_Videos);
		driver.click("id=" + DashBoardPageId.Welcome_LearnMore_Videos);
		driver.takeScreenShot();
	}

	public static void visitAPM(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visit APM from Welcome Page...");
		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_APMLinkCSS);
		driver.click("id=" + DashBoardPageId.Welcome_APMLinkCSS);
		driver.takeScreenShot();
	}

	public static void visitDashboards(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visit Dashboards from Welcome Page...");
		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_DashboardsLinkID);
		driver.click("id=" + DashBoardPageId.Welcome_DashboardsLinkID);
		driver.takeScreenShot();
	}

	public static void visitITA(WebDriver driver, String selection) throws Exception
	{
		driver.getLogger().info("Visiting ITA-" + selection + " from Welcome Page...");

		if (selection.equals("default")) {
			driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_ITALinkID);
			driver.click("id=" + DashBoardPageId.Welcome_ITALinkID);
			driver.takeScreenShot();
		}
		else {
			String eleXpath = null;
			driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.Welcome_ITA_SelectID);
			driver.click("id=oj-select-choice-" + DashBoardPageId.Welcome_ITA_SelectID);
			driver.takeScreenShot();
			switch (selection) {
				case "performanceAnalyticsDatabase":
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_PADatabase);
					break;
				case "performanceAnalyticsMiddleware":
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_PAMiddleware);
					break;
				case "resourceAnalyticsDatabase":
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_RADatabase);
					break;
				case "resourceAnalyticsMiddleware":
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_RAMiddleware);
					break;
				case "dataExplorerAnalyze":
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_DEAnalyze);
					break;
				case "dataExplorer":
					eleXpath = WelcomeUtil.getOptionXpath(driver, DashBoardPageId.Welcome_ITA_SelectID,
							DashBoardPageId.Welcome_ITA_DE);
					break;
			}
			driver.click(eleXpath);
			driver.takeScreenShot();
		}
	}

	public static void visitLA(WebDriver driver) throws Exception
	{
		driver.getLogger().info("Visiting LA from Welcome Page...");
		driver.waitForElementPresent("id=" + DashBoardPageId.Welcome_LALinkCSS);
		driver.click("id=" + DashBoardPageId.Welcome_LALinkCSS);
		driver.takeScreenShot();
	}

}
