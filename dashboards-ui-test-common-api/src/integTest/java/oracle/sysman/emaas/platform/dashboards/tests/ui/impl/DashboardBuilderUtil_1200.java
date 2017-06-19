package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class DashboardBuilderUtil_1200 extends DashboardBuilderUtil_1190
{
	@Override
	public void addTextWidgetToDashboard(WebDriver driver)
	{
		driver.getLogger().info("add text widget started");
		driver.waitForElementPresent("css=" + DashBoardPageId.DASHBOARDADDTEXTWIDGETCSS);
		
		WaitUtil.waitForPageFullyLoaded(driver);
		WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
		WebElement textButton = selectedDashboardEl.findElement(By.cssSelector(DashBoardPageId.DASHBOARDADDTEXTWIDGETCSS));
		textButton.click();
		driver.takeScreenShot();
		driver.getLogger().info("add text widget compelted");
	}
	
	@Override
	public void editTextWidgetAddContent(WebDriver driver, int index, String content)
	{
		driver.getLogger().info("editTextWidgetAddContent started");
		//find current dashboard
		WebElement selectedDashboardEl = getSelectedDashboardEl(driver);
		//click content wrapper area to load ckeditor
		driver.waitForElementPresent("css=" + DashBoardPageId.TEXTWIDGETCONTENTCSS);
		WebElement widget = selectedDashboardEl.findElements(By.cssSelector(DashBoardPageId.TEXTWIDGETCONTENTCSS)).get(index-1);
		widget.click();
		//input text string to editor area
		driver.waitForElementPresent("css=" + DashBoardPageId.TEXTWIDGETEDITORCSS);
		widget = selectedDashboardEl.findElements(By.cssSelector(DashBoardPageId.TEXTWIDGETEDITORCSS)).get(index-1);
		widget.clear();
		widget.click();
		widget.sendKeys(content);
		
		driver.takeScreenShot();
		driver.getLogger().info("editTextWidgetAddContent completed");
	}
}
