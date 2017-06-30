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
    	boolean isComplete = false;
    	try {
    	    isComplete = webdriver.isDisplayed("css="+ DashBoardPageId.BRANDINGBARICON);
    	} catch (Exception ex) {
    	    webdriver.getLogger().warning("Exception during isDisplayed check, but will continue..");
    	} finally {
    	    webdriver.getLogger().info(" isComplete: "+ isComplete);
    	}
    	return isComplete;
    	}
}
