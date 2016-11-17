/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.util;


import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class MyDashboardTest extends LocalEmaasWebDriverLoader
{
	
	
	@Test
	public void testSearch() throws Exception
	{
		runEmaasWebDriver();
		WebDriver driver = myWebDriver();
		driver.getWebDriver().get("https://slc10uan.us.oracle.com:4443/emsaasui/emcpdfui/welcome.html");
		
boolean result = WelcomeUtil.isLearnMoreItemExisted(driver, "getStarted");
System.out.println(result);
		
	}
	
	
}
