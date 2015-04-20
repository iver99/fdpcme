package oracle.sysman.emaas.platform.dashboards.test.ui.util;

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
		driver.click("css=img.widget-selector-screenshot");
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
		driver.sendKeys(WidgetPageId.searchBoxID, widget);
	}
	
}
