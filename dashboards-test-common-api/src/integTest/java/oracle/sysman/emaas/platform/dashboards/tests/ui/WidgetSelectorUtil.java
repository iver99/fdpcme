package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.XPathLiteral;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WidgetSelectorUtil
{
	private static WebDriver driver;

	public static void addWidget(String widgetName) throws Exception
	{
		driver.getLogger().info("WidgetSelectorUtil.addWidget started, widgetName=" + widgetName);
		Validator.notEmptyString("widgetName", widgetName);

		WidgetSelectorUtil.searchWidget(widgetName);
		WidgetSelectorUtil.getWidgetElementByTitle(widgetName, 0);

		driver.waitForElementPresent(DashBoardPageId.WIDGET_SELECTOR_OK_BTN_LOCATOR);
		driver.click(DashBoardPageId.WIDGET_SELECTOR_OK_BTN_LOCATOR);
		driver.takeScreenShot();

		driver.getLogger().info("WidgetSelectorUtil.addWidget completed");
	}

	public static void closeDialog() throws Exception
	{
		driver.getLogger().info("WidgetSelectorUtil.closeDialog started");
		driver.waitForElementPresent(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);
		driver.takeScreenShot();
		driver.click(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);
		driver.takeScreenShot();
		driver.waitForNotElementPresent(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);

		driver.getLogger().info("WidgetSelectorUtil.closeDialog completed");
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void page(int pageNo) throws Exception
	{

	}

	public static void pagingNext() throws Exception
	{

	}

	public static void pagingPrevious() throws Exception
	{

	}

	private static WebElement getWidgetElementByTitle(String widgetName, int index)
	{
		String xpath = XPathLiteral.getXPath(widgetName, driver.getLogger());
		String widgetItemByNameLocator = String.format(DashBoardPageId.WIDGET_SELECTOR_WIDGET_ITEMS_BY_TITLE, xpath);
		driver.waitForElementPresent(widgetItemByNameLocator);
		driver.click(widgetItemByNameLocator);
		driver.takeScreenShot();

		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(widgetItemByNameLocator));
		if (tileTitles == null || tileTitles.size() <= index) {
			throw new NoSuchElementException("Widget with widgetName=" + widgetName + ", index=" + index + " is not found");
		}
		tileTitles.get(index).click();
		driver.takeScreenShot();
		return tileTitles.get(index);
	}

	private static void searchWidget(String widgetName) throws Exception
	{
		driver.waitForElementPresent(DashBoardPageId.WIDGET_SELECTOR_WIDGET_AREA);
		driver.clear(DashBoardPageId.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR);
		driver.takeScreenShot();

		driver.sendKeys(DashBoardPageId.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR, widgetName);
		driver.click(DashBoardPageId.WIDGET_SELECTOR_SEARCH_BTN);
		driver.takeScreenShot();
	}
}
