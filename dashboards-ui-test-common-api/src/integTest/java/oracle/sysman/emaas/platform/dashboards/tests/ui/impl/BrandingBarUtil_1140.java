package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

/**
 * Created by qiqia on 2016/12/13.
 */
public class BrandingBarUtil_1140 extends BrandingBarUtil_1130 {

	@Override
	public boolean isVisualAnalyzerLinkExisted(WebDriver driver, String visualAnalyzerLinkName)
	{
		Validator.notEmptyString("visualAnalyzerLinkName in [isVisualAnalyzerLinkExisted]", visualAnalyzerLinkName);
		driver.getLogger().info(
				"Start to check if visual analyzer link is existed in navigation bar. Link name: " + visualAnalyzerLinkName);
		boolean isExisted = false;
		//backward compatible mode
		if (NAV_LINK_TEXT_VA_TA.equals(visualAnalyzerLinkName)){
			visualAnalyzerLinkName = NAV_LINK_TEXT_DATAEXPLORER;
			driver.getLogger().info(
					"Use NEW Link name: " + visualAnalyzerLinkName);
		}else if (NAV_LINK_TEXT_VA_LA.equals(visualAnalyzerLinkName)){
			visualAnalyzerLinkName = NAV_LINK_TEXT_LOGEXPLORER;
			driver.getLogger().info(
					"Use NEW Link name: " + visualAnalyzerLinkName);			
		}
		isExisted = isApplicationLinkExisted(driver, "va", visualAnalyzerLinkName);
		driver.getLogger().info("Existence check for visual analyzer link is completed. Result: " + isExisted);
		return isExisted;
	}
	
	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.tests.ui.util.IBrandingBarUtil#visitApplicationVisualAnalyzer(oracle.sysman.qatool.uifwk.webdriver.WebDriver, java.lang.String)
	 */
	@Override
	public void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName)
	{
		Validator.notEmptyString("visualAnalyzerLinkName in [visitApplicationVisualAnalyzer]", visualAnalyzerLinkName);
		driver.getLogger().info("Start to visit visual analyzer link from branding bar. Link name: " + visualAnalyzerLinkName);
		//backward compatible mode
		if (NAV_LINK_TEXT_VA_TA.equals(visualAnalyzerLinkName)){
			visualAnalyzerLinkName = NAV_LINK_TEXT_DATAEXPLORER;
			driver.getLogger().info(
					"Use NEW Link name: " + visualAnalyzerLinkName);
		}else if (NAV_LINK_TEXT_VA_LA.equals(visualAnalyzerLinkName)){
			visualAnalyzerLinkName = NAV_LINK_TEXT_LOGEXPLORER;
			driver.getLogger().info(
					"Use NEW Link name: " + visualAnalyzerLinkName);			
		}		
		visitApplicationLink(driver, "va", visualAnalyzerLinkName);
	}
}
