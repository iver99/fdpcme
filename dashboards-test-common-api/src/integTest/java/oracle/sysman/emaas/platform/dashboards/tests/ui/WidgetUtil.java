package oracle.sysman.emaas.platform.dashboards.tests.ui;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class WidgetUtil
{
	private static WebDriver driver;

    private static WebElement getWidgetByName(String widgetName, int index) throws InterruptedException {
        if(widgetName == null) {
            return null;
        }

        List<WebElement> widgets = driver.getWebDriver().findElements(By.cssSelector(DashBoardPageId.WidgetTitleCSS));
        WebElement widget = null;
        int counter = 0;
        for(WebElement widgetElement : widgets) {
            WebElement widgetTitle = widgetElement.findElement(By.cssSelector(DashBoardPageId.TileTitleCSS));
            if(widgetTitle != null && widgetTitle.getText().trim().equals(widgetName)){
                if(counter == index) {
                    widget = widgetElement;
                    break;
                }
                counter ++;
            }
        }

        return widget;
    }

    private static void focusOnWidgetHeader(WebElement widgetElement) {
        if( null == widgetElement) {
            return;
        }

        WebElement widgetHeader = widgetElement.findElement(By.cssSelector(DashBoardPageId.TileTitleCSS));
        Actions actions = new Actions(driver.getWebDriver());
        actions.moveToElement(widgetHeader).build().perform();
        driver.getLogger().info("Focus to the widget");
    }

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void remove(String widgetName) throws Exception
	{
		WidgetUtil.remove(widgetName, 0);
	}

	public static void remove(String widgetName, int index) throws Exception {
        WebElement widgetEl = getWidgetByName(widgetName,index);
        if(null == widgetEl) {
            driver.getLogger().info("Fail to find the widget titled with " + widgetName);
            return;
        }

        focusOnWidgetHeader(widgetEl);

        widgetEl.findElement(By.cssSelector(DashBoardPageId.ConfigTileCSS)).click();
        driver.click("css="+DashBoardPageId.RemoveTileCSS);
        driver.getLogger().info("Remove the widget");
    }

	public static void resizeOptions(String widgetName, int index, String resizeOptions) throws Exception{
        WebElement widgetEl = getWidgetByName(widgetName,index);
        if(null == widgetEl) {
            driver.getLogger().info("Fail to find the widget titled with " + widgetName);
            return;
        }

        focusOnWidgetHeader(widgetEl);

        String tileResizeCSS = null;
        switch (resizeOptions){
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
        if( null == tileResizeCSS) {
            return;
        }

        widgetEl.findElement(By.cssSelector(DashBoardPageId.ConfigTileCSS)).click();
        driver.click("css="+tileResizeCSS);
        driver.getLogger().info("Resize the widget");

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

	}

	public static void udeRedirect(String widgetName) throws Exception
	{
		WidgetUtil.udeRedirect(widgetName, 0);

	}

	public static void udeRedirect(String widgetName, int index) throws Exception
	{

	}

}
