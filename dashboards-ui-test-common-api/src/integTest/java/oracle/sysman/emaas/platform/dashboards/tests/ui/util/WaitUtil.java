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

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

/**
 * @author wenjzhu
 */
public class WaitUtil
{
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
				boolean activeAjax = (Boolean) ((JavascriptExecutor) d).executeScript("return $.active === 0");
				webd.getLogger().info(
						"Wait for ajax finished: " + System.currentTimeMillis() + " has active ajax: " + !activeAjax);
				return activeAjax;
			}
		});
	}

	public static void waitForAjaxCompleted(final oracle.sysman.qatool.uifwk.webdriver.WebDriver webd)
	{
		webd.getLogger().info("START wait for ajax start: " + System.currentTimeMillis());
		org.openqa.selenium.WebDriver driver = webd.getWebDriver();
		WebDriverWait wait = new WebDriverWait(driver, WAIT_TIMEOUT);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("df-ajax-flag")));
		webd.getLogger().info("START wait for ajax end: " + System.currentTimeMillis());
		wait = new WebDriverWait(driver, WAIT_TIMEOUT);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("df-ajax-flag")));
		webd.getLogger().info("End wait for ajax end: " + System.currentTimeMillis());
	}

}
