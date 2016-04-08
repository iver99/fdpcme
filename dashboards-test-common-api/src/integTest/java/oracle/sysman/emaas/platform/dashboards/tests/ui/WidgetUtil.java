package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ParameterValidators;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WidgetUtil
{
	private static WebDriver driver;

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void remove(String widgetName) throws Exception
	{
		WidgetUtil.remove(widgetName, 0);
	}

	public static void remove(String widgetName, int index) throws Exception
	{
		WebElement widgetEl = WidgetUtil.getWidgetByName(widgetName, index);
		if (null == widgetEl) {
			driver.getLogger().info("Fail to find the widget titled with " + widgetName);
			return;
		}

		WidgetUtil.focusOnWidgetHeader(widgetEl);

		widgetEl.findElement(By.cssSelector(DashBoardPageId.ConfigTileCSS)).click();
		driver.click("css=" + DashBoardPageId.RemoveTileCSS);
		driver.getLogger().info("Remove the widget");
	}

	public static void resizeOptions(String widgetName, int index, String resizeOptions) throws Exception
	{
		WebElement widgetEl = WidgetUtil.getWidgetByName(widgetName, index);
		if (null == widgetEl) {
			driver.getLogger().info("Fail to find the widget titled with " + widgetName);
			return;
		}

		WidgetUtil.focusOnWidgetHeader(widgetEl);

		String tileResizeCSS = null;
		switch (resizeOptions) {
			case DashBoardPageId.TILE_WIDER:
				tileResizeCSS = DashBoardPageId.WiderTileCSS;
				break;
			case DashBoardPageId.TILE_NARROWER:
				tileResizeCSS = DashBoardPageId.NarrowerTileCSS;
				break;
			case DashBoardPageId.TILE_SHORTER:
				tileResizeCSS = DashBoardPageId.ShorterTileCSS;
				break;
			case DashBoardPageId.TILE_TALLER:
				tileResizeCSS = DashBoardPageId.TallerTileCSS;
				break;
			default:
				break;
		}
		if (null == tileResizeCSS) {
			return;
		}

		widgetEl.findElement(By.cssSelector(DashBoardPageId.ConfigTileCSS)).click();
		driver.click("css=" + tileResizeCSS);
		driver.getLogger().info("Resize the widget");

	}

	public static void resizeOptions(String widgetName, String resizeOptions) throws Exception
	{
		WidgetUtil.resizeOptions(widgetName, 0, resizeOptions);
	}

	public static void title(String widgetName, boolean visibility) throws Exception
	{
		WidgetUtil.title(widgetName, 0, visibility);
	}

	public static void title(String widgetName, int index, boolean visibility) throws Exception
	{
		driver.getLogger().info(
				"WidgetUtil.title started for widgetName=" + widgetName + ", index=" + index + ", visibility=" + visibility);

		ParameterValidators.notEmptyString("widgetName", widgetName);
		ParameterValidators.equalOrLargerThan0("index", index);
		WidgetUtil.clickTileConfigButton(widgetName, index);
		if (visibility) {
			if (driver.isDisplayed(DashBoardPageId.BuilderTileHideLocator)) {
				driver.getLogger().info("WidgetUtil.title completed as title is shown already");
				return;
			}
			driver.click(DashBoardPageId.BuilderTileShowLocator);
		}
		else {
			if (driver.isDisplayed(DashBoardPageId.BuilderTileShowLocator)) {
				driver.getLogger().info("WidgetUtil.title completed as title is hidden already");
				return;
			}
			driver.click(DashBoardPageId.BuilderTileHideLocator);
		}
		driver.getLogger().info("WidgetUtil.title completed");
	}

	public static void udeRedirect(String widgetName) throws Exception
	{
		WidgetUtil.udeRedirect(widgetName, 0);
	}

	public static void udeRedirect(String widgetName, int index) throws Exception
	{
		driver.getLogger().info("WidgetUtil.udeRedirect started for widgetName=" + widgetName + ", index=" + index);

		ParameterValidators.notEmptyString("widgetName", widgetName);
		ParameterValidators.equalOrLargerThan0("index", index);
		WidgetUtil.clickTileUDEExploreButton(widgetName, index);

		driver.getLogger().info("WidgetUtil.udeRedirect completed");
	}

	private static WebElement clickTileConfigButton(String widgetName, int index)
	{
		WebElement tileTitle = WidgetUtil.getTileTitleElement(widgetName, index);
		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId.BuilderTileConfigLocator));
		if (tileConfig == null) {
			throw new NoSuchElementException("Tile config menu for title=" + widgetName + ", index=" + index + " is not found");
		}
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		builder.moveToElement(tileConfig).click().perform();
		return tileConfig;
	}

	private static void clickTileUDEExploreButton(String widgetName, int index)
	{
		WebElement tileTitle = WidgetUtil.getTileTitleElement(widgetName, index);
		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId.BuilderTileDataExploreLocator));
		if (tileConfig == null) {
			throw new NoSuchElementException(
					"Tile data explore link for title=" + widgetName + ", index=" + index + " is not found");
		}
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		builder.moveToElement(tileConfig).click().perform();
	}

	private static void focusOnWidgetHeader(WebElement widgetElement)
	{
		if (null == widgetElement) {
			return;
		}

		WebElement widgetHeader = widgetElement.findElement(By.cssSelector(DashBoardPageId.TileTitleCSS));
		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(widgetHeader).build().perform();
		driver.getLogger().info("Focus to the widget");
	}

	private static WebElement getTileTitleElement(String widgetName, int index)
	{
		driver.waitForElementPresent(DashBoardPageId.BuilderTilesEditArea);
		driver.click(DashBoardPageId.BuilderTilesEditArea);
		String titleTitlesLocator = String.format(DashBoardPageId.BuilderTileTitleLocator, widgetName);
		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(titleTitlesLocator));
		if (tileTitles == null || tileTitles.size() <= index) {
			throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
		}
		tileTitles.get(index).click();
		return tileTitles.get(index);
	}

	private static WebElement getWidgetByName(String widgetName, int index) throws InterruptedException
	{
		if (widgetName == null) {
			return null;
		}

		List<WebElement> widgets = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.WidgetTitleCSS));
		WebElement widget = null;
		int counter = 0;
		for (WebElement widgetElement : widgets) {
			WebElement widgetTitle = widgetElement.findElement(By.cssSelector(DashBoardPageId.TileTitleCSS));
			if (widgetTitle != null && widgetTitle.getText().trim().equals(widgetName)) {
				if (counter == index) {
					widget = widgetElement;
					break;
				}
				counter++;
			}
		}

		return widget;
	}
}
