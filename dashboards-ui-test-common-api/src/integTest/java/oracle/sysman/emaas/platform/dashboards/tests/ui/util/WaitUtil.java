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
		            boolean activeAjax =false;
                            try{
                                activeAjax = (Boolean) ((JavascriptExecutor) d).executeScript("return $ !== undefined && $.active === 0");
                            }catch(org.openqa.selenium.WebDriverException e){
                                if (e.getMessage()!=null && e.getMessage().contains("$ is not defined")){
                                   webd.getLogger().info("$ is not loaded, will wait more");
                                }else{
                                   throw e;
                                }
                            }
				webd.getLogger().info(
						"Wait for ajax finished: " + System.currentTimeMillis() + " has active ajax: " + !activeAjax);
				return activeAjax;
			}
		});
	}

}
