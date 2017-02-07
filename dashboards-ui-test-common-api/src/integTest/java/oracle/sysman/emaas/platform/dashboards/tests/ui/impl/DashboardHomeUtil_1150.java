package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_1150;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * Created by qiqia on 2016/12/13.
 */
public class DashboardHomeUtil_1150 extends DashboardHomeUtil_1130{

    @Override
    public void gotoDataExplorer(WebDriver driver, String option)
    {
        driver.getLogger().info("[DashboardHomeUtil] call exploreData -> " + option);

        Validator.notEmptyString("option", option);

        driver.click(convertName(DashBoardPageId.EXPLOREDATABTNID));
        //WebElement menu = driver.getElement(convertName(DashBoardPageId.EXPLOREDATAMENU));
        //backward compatible mode
        if (EXPLOREDATA_MENU_LOGEXPLORER.equals(option) || EXPLOREDATA_MENU_LOG.equals(option)) {
            driver.click(DashBoardPageId_1150.XPATH_EXPLORE_LOG);
        } else if (EXPLOREDATA_MENU_DATAEXPLORER.equals(option) || EXPLOREDATA_MENU_SEARCH.equals(option)){
            driver.click(DashBoardPageId_1150.XPATH_EXPLORE_Search);
        } else{
        	throw new IllegalArgumentException("Not supported option: "+option);
        }


    }

}
