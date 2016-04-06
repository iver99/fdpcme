package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
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

	}

	public static void resizeOptions(String widgetName, int index, String resizeOptions) throws Exception
	{
	}

	public static void resizeOptions(String widgetName, String resizeOptions) throws Exception
	{
		WidgetUtil.resizeOptions(widgetName, 0, resizeOptions);
	}

	public static void title(String widgetName, Boolean visibility) throws Exception
	{
		WidgetUtil.title(widgetName, 0, visibility);
	}

	public static void title(String widgetName, int index, Boolean visibility) throws Exception
	{
		driver.getLogger().info("WidgetUtil.title started for widgetName=" + widgetName + ", index=" + index + ", visibility");

		WidgetUtil.clickTileConfigButton(widgetName, index);
		if (visibility == null) {
			throw new IllegalArgumentException("Unexpected null value for input parameter visibility");
		}
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
		driver.getLogger()
				.info("WidgetUtil.udeRedirect started for widgetName=" + widgetName + ", index=" + index + ", visibility");

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
}
