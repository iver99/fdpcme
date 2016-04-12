package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.XPathLiteral;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class WidgetSelectorUtil
{
	public static void addWidget(WebDriver driver, String widgetName) throws Exception
	{
		driver.getLogger().info("WidgetSelectorUtil.addWidget started, widgetName=" + widgetName);
		Validator.notEmptyString("widgetName", widgetName);

		WidgetSelectorUtil.searchWidget(driver, widgetName);
		WidgetSelectorUtil.getWidgetElementByTitle(driver, widgetName, 0);

		driver.waitForElementPresent(DashBoardPageId.WIDGET_SELECTOR_OK_BTN_LOCATOR);
		driver.click(DashBoardPageId.WIDGET_SELECTOR_OK_BTN_LOCATOR);
		driver.takeScreenShot();

		driver.getLogger().info("WidgetSelectorUtil.addWidget completed");
	}

	public static void closeDialog(WebDriver driver) throws Exception
	{
		driver.getLogger().info("WidgetSelectorUtil.closeDialog started");
		driver.waitForElementPresent(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);
		driver.takeScreenShot();
		driver.click(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);
		driver.takeScreenShot();
		driver.waitForNotElementPresent(DashBoardPageId.WIDGET_SELECTOR_CLOSE_BTN_LOCATOR);

		driver.getLogger().info("WidgetSelectorUtil.closeDialog completed");
	}

	public static void page(WebDriver driver, int pageNo) throws Exception
	{

	}

	public static void pagingNext(WebDriver driver) throws Exception
	{

	}

	public static void pagingPrevious(WebDriver driver) throws Exception
	{

	}

	private static WebElement getWidgetElementByTitle(WebDriver driver, String widgetName, int index)
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

	private static void searchWidget(WebDriver driver, String widgetName) throws Exception
	{
		driver.waitForElementPresent(DashBoardPageId.WIDGET_SELECTOR_WIDGET_AREA);
		driver.clear(DashBoardPageId.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR);
		driver.takeScreenShot();

		driver.sendKeys(DashBoardPageId.WIDGET_SELECTOR_SEARCH_INPUT_LOCATOR, widgetName);
		driver.click(DashBoardPageId.WIDGET_SELECTOR_SEARCH_BTN);
		driver.takeScreenShot();
	}
}
