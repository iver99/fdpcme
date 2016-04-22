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
	public static final long WAIT_DELAY = 900;

	public static void waitForPageFullyLoaded(final oracle.sysman.qatool.uifwk.webdriver.WebDriver webd)
	{
		webd.getLogger().info("START wait for ajax finished: " + System.currentTimeMillis());
		org.openqa.selenium.WebDriver driver = webd.getWebDriver();
		WebDriverWait wait = new WebDriverWait(driver, WAIT_DELAY);
		wait.until(new Predicate<org.openqa.selenium.WebDriver>() {
			@Override
			public boolean apply(org.openqa.selenium.WebDriver d)
			{
				boolean activeAjax = (Boolean) ((JavascriptExecutor) d).executeScript("return $.active === 0");
				webd.getLogger().info(
						"Wait for ajax finished: " + System.currentTimeMillis() + " has active ajax: " + !activeAjax);
				return activeAjax;
			}
		});
	}

	//	public static void waitForPageFullyLoaded(final oracle.sysman.qatool.uifwk.webdriver.WebDriver webd)
	//	{
	//		Boolean loaded = false;
	//		int maxRetries = 60 * 2; //2 minutes
	//		for (int i = 0; i < maxRetries; ++i) {
	//			loaded = (Boolean) ((JavascriptExecutor) webd.getWebDriver())
	//					.executeScript("return window.operationStack.isComplete()");
	//			if (loaded) {
	//				break;
	//			}
	//			System.out.println("sleep after checking if pageFullyLoaded");
	//			try {
	//				Thread.sleep(1000);
	//			}
	//			catch (InterruptedException ie) {
	//			}
	//		}
	//	}
}
