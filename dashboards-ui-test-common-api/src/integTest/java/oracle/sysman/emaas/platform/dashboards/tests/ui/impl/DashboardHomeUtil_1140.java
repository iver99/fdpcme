package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1140;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * Created by qiqia on 2016/12/13.
 */
public class DashboardHomeUtil_1140 extends DashboardHomeUtil_1130{
    private DashboardHomeUtil_1140(){}
    public static final String EXPLOREDATA_MENU_LOG = "Log Explorer";
    public static final String EXPLOREDATA_MENU_SEARCH = "Data Explorer";

    @Override
    public void gotoDataExplorer(WebDriver driver, String option)
    {
        driver.getLogger().info("[DashboardHomeUtil] call exploreData -> " + option);

        Validator.notEmptyString("option", option);

        driver.click(convertName(DashBoardPageId.EXPLOREDATABTNID));
        //WebElement menu = driver.getElement(convertName(DashBoardPageId.EXPLOREDATAMENU));

        if (EXPLOREDATA_MENU_LOG.equals(option)||"Log Visual Analyzer".equals(option)) {
            driver.click(DashBoardPageId_1140.EXPLORE_LOG);
        } else {
            driver.click(DashBoardPageId_1140.EXPLORE_Search);
        }


    }

}
