package oracle.sysman.emaas.platform.dashboards.test.util;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WidgetAddPage {
	
	private WebDriver driver;
		
	public WidgetAddPage(WebDriver driver) {
		super();
		this.driver = driver;
	}
	
	public void clickWidgetOnTable(String widgetName) throws Exception
	{
		//driver.click("//span[text()='"+widgetName+"']");
		driver.getLogger().info("after clicking css");
		driver.click("css=img.widget-selector-screenshot");
		driver.getLogger().info("after clicking css");
	}
	
	
	public void closeWidgetDiag() throws Exception
	{
		driver.click(DashBoardPageId.WidgetDialogCloseButtonID);
	}
	
	public void addOfWidgetDetails() throws Exception
	{
		driver.click(DashBoardPageId.WidgetAddButton);
	}
	
	public void searchWidget(String widget) throws Exception
	{
		driver.getElement(WidgetPageId.searchBoxID).clear();
		driver.sendKeys(WidgetPageId.searchBoxID, widget);
	}
	
}

