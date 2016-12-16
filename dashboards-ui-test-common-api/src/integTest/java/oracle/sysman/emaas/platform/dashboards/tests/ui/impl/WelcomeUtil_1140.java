package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1140;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;

/**
 * Created by qiqia on 2016/12/13.
 */
public class WelcomeUtil_1140 extends WelcomeUtil_175 {
    private WelcomeUtil_1140(){}
    public static final String SERVICE_NAME_DATA_EXPLORERS_EXPECTEDNAME = "Explorers";
    @Override
    public void dataExplorers(WebDriver driver, String selection)
    {
        String eleXpath = null;
        driver.getLogger().info("Visiting Data Explorer-" + selection + " from Welcome Page...");

        Validator.fromValidValues("dataExplorersSelection", selection, DATA_EXPLORERS_LOG, DATA_EXPLORERS_ANALYZE,
                DATA_EXPLORERS_SEARCH);

        WaitUtil.waitForPageFullyLoaded(driver);

        driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.WELCOME_DATAEXP_SELECTID);
        driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_DATAEXP_SELECTID);
        driver.takeScreenShot();
        switch (selection) {
            case DATA_EXPLORERS_LOG:
                eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_DATAEXP_SELECTID, DashBoardPageId.WELCOME_DATAEXP_LOG);
                break;
            case DATA_EXPLORERS_ANALYZE:
                eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_DATAEXP_SELECTID,
                        DashBoardPageId.WELCOME_DATAEXP_ANALYZE);
                break;
            case DATA_EXPLORERS_SEARCH:
                eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_DATAEXP_SELECTID,
                        DashBoardPageId_1140.WELCOME_DATAEXP_SEARCH);
                break;
            default:
                break;

        }
        driver.getWebDriver().findElement(By.xpath(eleXpath)).click();
        driver.takeScreenShot();
    }
}
