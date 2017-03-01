package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.qatool.uifwk.webdriver.DefaultJetPageLoadDetector;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * Created by xiadai on 2/19/17.
 */
public class BuildPageLoadDetector extends DefaultJetPageLoadDetector {
    private WebDriver webdriver = null;
    public BuildPageLoadDetector(oracle.sysman.qatool.uifwk.webdriver.WebDriver webdriver) {
        super(webdriver);
        this.webdriver = webdriver;
    }
    @Override
    public boolean isPageLoadComplete() {
        boolean isComplete;
        isComplete = webdriver.isDisplayed("css="+ DashBoardPageId.BRANDINGBARICON);
        webdriver.getLogger().info(" isComplete: "+ isComplete);
        return isComplete;
    }
}
