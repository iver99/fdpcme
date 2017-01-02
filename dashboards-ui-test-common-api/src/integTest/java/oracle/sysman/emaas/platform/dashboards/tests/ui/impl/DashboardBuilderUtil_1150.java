package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1150;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class DashboardBuilderUtil_1150 extends DashboardBuilderUtil_1120  {
    @Override
    public void removeWidget(WebDriver driver, String widgetName)
    {
        removeWidget(driver, widgetName, 0);
    }

    @Override
    public void removeWidget(WebDriver driver, String widgetName, int index)
    {
        driver.getLogger().info(
                "DashboardBuilderUtil.removeWidget started for name=\"" + widgetName + "\"");
        Validator.notEmptyString("widgetName", widgetName);
        Validator.equalOrLargerThan0("index", index);

        WebElement widgetEl = null;
        try {
            widgetEl = getWidgetByName(driver, widgetName, index);
        }
        catch (InterruptedException e) {
            driver.getLogger().info("context "+ e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        focusOnWidgetHeader(driver, widgetEl);
        driver.takeScreenShot();

        widgetEl.findElement(By.cssSelector(DashBoardPageId_190.CONFIGTILECSS)).click();
        driver.click("css=" + DashBoardPageId_1150.EDITTILECSS);
        driver.waitForElementPresent("css=" + DashBoardPageId_1150.REMOVETILECSS);
        driver.click("css=" + DashBoardPageId_1150.REMOVETILECSS);
        driver.getLogger().info("Remove the widget");
        driver.takeScreenShot();
    }
}
