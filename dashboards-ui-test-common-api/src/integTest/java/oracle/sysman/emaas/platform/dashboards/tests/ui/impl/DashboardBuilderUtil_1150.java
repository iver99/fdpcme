package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1150;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;


public class DashboardBuilderUtil_1150 {
    @Override
    public void removeWidget(WebDriver driver, String widgetName)
    {
        removeWidget(driver, widgetName, 0);
    }

    @Override
    public void removeWidget(WebDriver driver, String widgetName, int index)
    {
        Validator.notEmptyString("widgetName", widgetName);
        Validator.equalOrLargerThan0("index", index);

        WebElement widgetEl = null;
        try {
            widgetEl = getWidgetByName(driver, widgetName, index);
        }
        catch (InterruptedException e) {
            LOGGER.info("context", e);
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
