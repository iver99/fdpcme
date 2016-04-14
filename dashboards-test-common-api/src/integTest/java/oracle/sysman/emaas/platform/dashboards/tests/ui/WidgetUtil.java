package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class WidgetUtil
{
	public static final String TILE_WIDER = "wider";
	public static final String TILE_NARROWER = "narrower";
	public static final String TILE_TALLER = "taller";
	public static final String TILE_SHORTER = "shorter";

	public static void remove(WebDriver driver, String widgetName) throws Exception
	{
		WidgetUtil.remove(driver, widgetName, 0);
	}

	public static void remove(WebDriver driver, String widgetName, int index) throws Exception
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);

		WebElement widgetEl = WidgetUtil.getWidgetByName(driver, widgetName, index);
		if (null == widgetEl) {
			driver.getLogger().info("Fail to find the widget titled with " + widgetName);
			throw new NoSuchElementException("Tile config menu for title=" + widgetName + ", index=" + index + " is not found");
		}

		WidgetUtil.focusOnWidgetHeader(driver, widgetEl);
		driver.takeScreenShot();

		widgetEl.findElement(By.cssSelector(DashBoardPageId.ConfigTileCSS)).click();
		driver.click("css=" + DashBoardPageId.RemoveTileCSS);
		driver.getLogger().info("Remove the widget");
		driver.takeScreenShot();

	}

	public static void resizeOptions(WebDriver driver, String widgetName, int index, String resizeOptions) throws Exception
	{
		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		Validator.fromValidValues("resizeOptions",resizeOptions,WidgetUtil.TILE_NARROWER,WidgetUtil.TILE_WIDER,WidgetUtil.TILE_SHORTER,WidgetUtil.TILE_TALLER);

		WebElement widgetEl = WidgetUtil.getWidgetByName(driver, widgetName, index);
		if (null == widgetEl) {
			driver.getLogger().info("Fail to find the widget titled with " + widgetName);
			throw new NoSuchElementException("Tile config menu for title=" + widgetName + ", index=" + index + " is not found");
		}

		WidgetUtil.focusOnWidgetHeader(driver, widgetEl);
		driver.takeScreenShot();

		String tileResizeCSS = null;
		switch (resizeOptions) {
			case WidgetUtil.TILE_WIDER:
				tileResizeCSS = DashBoardPageId.WiderTileCSS;
				break;
			case WidgetUtil.TILE_NARROWER:
				tileResizeCSS = DashBoardPageId.NarrowerTileCSS;
				break;
			case WidgetUtil.TILE_SHORTER:
				tileResizeCSS = DashBoardPageId.ShorterTileCSS;
				break;
			case WidgetUtil.TILE_TALLER:
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
		driver.takeScreenShot();

	}

	public static void resizeOptions(WebDriver driver, String widgetName, String resizeOptions) throws Exception
	{
		WidgetUtil.resizeOptions(driver, widgetName, 0, resizeOptions);
	}

	public static void title(WebDriver driver, String widgetName, boolean visibility) throws Exception
	{
		WidgetUtil.title(driver, widgetName, 0, visibility);
	}

	public static void title(WebDriver driver, String widgetName, int index, boolean visibility) throws Exception
	{
		driver.getLogger().info(
				"WidgetUtil.title started for widgetName=" + widgetName + ", index=" + index + ", visibility=" + visibility);

		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		WidgetUtil.clickTileConfigButton(driver, widgetName, index);

		if (visibility) {
			if (driver.isDisplayed(DashBoardPageId.BuilderTileHideLocator)) {
				driver.takeScreenShot();
				driver.getLogger().info("WidgetUtil.title completed as title is shown already");
				return;
			}
			driver.click(DashBoardPageId.BuilderTileShowLocator);
			driver.takeScreenShot();
		}
		else {
			if (driver.isDisplayed(DashBoardPageId.BuilderTileShowLocator)) {
				driver.takeScreenShot();
				driver.getLogger().info("WidgetUtil.title completed as title is hidden already");
				return;
			}
			driver.click(DashBoardPageId.BuilderTileHideLocator);
			driver.takeScreenShot();
		}
		driver.getLogger().info("WidgetUtil.title completed");
	}

	public static void udeRedirect(WebDriver driver, String widgetName) throws Exception
	{
		WidgetUtil.udeRedirect(driver, widgetName, 0);
	}

	public static void udeRedirect(WebDriver driver, String widgetName, int index) throws Exception
	{
		driver.getLogger().info("WidgetUtil.udeRedirect started for widgetName=" + widgetName + ", index=" + index);

		Validator.notEmptyString("widgetName", widgetName);
		Validator.equalOrLargerThan0("index", index);
		WidgetUtil.clickTileUDEExploreButton(driver, widgetName, index);

		driver.getLogger().info("WidgetUtil.udeRedirect completed");
	}

	private static WebElement clickTileConfigButton(WebDriver driver, String widgetName, int index)
	{
		WebElement tileTitle = WidgetUtil.getTileTitleElement(driver, widgetName, index);
		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId.BuilderTileConfigLocator));
		if (tileConfig == null) {
			throw new NoSuchElementException("Tile config menu for title=" + widgetName + ", index=" + index + " is not found");
		}
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		builder.moveToElement(tileConfig).click().perform();
		return tileConfig;
	}

	private static void clickTileUDEExploreButton(WebDriver driver, String widgetName, int index)
	{
		WebElement tileTitle = WidgetUtil.getTileTitleElement(driver, widgetName, index);
		WebElement tileConfig = tileTitle.findElement(By.xpath(DashBoardPageId.BuilderTileDataExploreLocator));
		if (tileConfig == null) {
			throw new NoSuchElementException("Tile data explore link for title=" + widgetName + ", index=" + index
					+ " is not found");
		}
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		builder.moveToElement(tileConfig).click().perform();
		driver.takeScreenShot();
	}

	private static void focusOnWidgetHeader(WebDriver driver, WebElement widgetElement)
	{
		if (null == widgetElement) {
			return;
		}

		WebElement widgetHeader = widgetElement.findElement(By.cssSelector(DashBoardPageId.TileTitleCSS));
		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(widgetHeader).build().perform();
		driver.getLogger().info("Focus to the widget");
	}

	private static WebElement getTileTitleElement(WebDriver driver, String widgetName, int index)
	{
		driver.waitForElementPresent(DashBoardPageId.BuilderTilesEditArea);
		driver.click(DashBoardPageId.BuilderTilesEditArea);
		driver.takeScreenShot();

		String titleTitlesLocator = String.format(DashBoardPageId.BuilderTileTitleLocator, widgetName);
		List<WebElement> tileTitles = driver.getWebDriver().findElements(By.xpath(titleTitlesLocator));
		if (tileTitles == null || tileTitles.size() <= index) {
			throw new NoSuchElementException("Tile with title=" + widgetName + ", index=" + index + " is not found");
		}
		tileTitles.get(index).click();
		driver.takeScreenShot();
		return tileTitles.get(index);
	}

	private static WebElement getWidgetByName(WebDriver driver, String widgetName, int index) throws InterruptedException
	{
		if (widgetName == null) {
			return null;
		}

		List<WebElement> widgets = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.WidgetTitleCSS));
		WebElement widget = null;
		int counter = 0;
		for (WebElement widgetElement : widgets) {
			WebElement widgetTitle = widgetElement.findElement(By.cssSelector(DashBoardPageId.TileTitleCSS));
			if (widgetTitle != null && widgetTitle.getAttribute("data-tile-title").trim().equals(widgetName)) {
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
