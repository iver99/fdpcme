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

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;
import java.util.concurrent.TimeUnit;

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
		final String AJAX_FLAG_TAG = "df-ajax-flag";
		webd.getLogger().info("START wait for ajax completed: " + System.currentTimeMillis());
		org.openqa.selenium.WebDriver driver = webd.getWebDriver();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(WAIT_TIMEOUT, TimeUnit.SECONDS)
				.pollingEvery(5,TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				webd.getLogger().info("Waiting for ajax completed: " + System.currentTimeMillis());
				return webd.isElementPresent("css="+AJAX_FLAG_TAG);
			}
		});

		webd.evalJavascript("$(\'"+AJAX_FLAG_TAG+"\').remove();");

		webd.getLogger().info("END wait for ajax completed: " + System.currentTimeMillis());

	}

}
