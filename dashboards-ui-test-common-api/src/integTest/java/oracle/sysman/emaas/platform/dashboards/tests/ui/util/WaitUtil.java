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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

/**
 * @author wenjzhu
 */
public class WaitUtil
{
	private WaitUtil() {
	  }

	public static final long WAIT_TIMEOUT = 900; //unit sec

	public static void waitForPageFullyLoaded(final oracle.sysman.qatool.uifwk.webdriver.WebDriver webd)
	{
		webd.getLogger().info("START wait for ajax finished: " + System.currentTimeMillis());
		org.openqa.selenium.WebDriver driver = webd.getWebDriver();
		WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
		wait.until(new Predicate<org.openqa.selenium.WebDriver>() {
			@Override
			public boolean apply(org.openqa.selenium.WebDriver d)
			{
				boolean activeAjax = (Boolean) ((JavascriptExecutor) d).executeScript("return $ !== undefined && $.active === 0");
				webd.getLogger().info(
						"Wait for ajax finished: " + System.currentTimeMillis() + " has active ajax: " + !activeAjax);
				return activeAjax;
			}
		});
	}

}
