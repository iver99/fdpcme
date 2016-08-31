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

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import oracle.sysman.qatool.uifwk.utils.Utils;
import oracle.sysman.qatool.uifwk.webdriver.SetUpBrowser;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;
import oracle.sysman.qatool.uifwk.webdriver.logging.EMTestLogger;
import oracle.sysman.qatool.uifwk.webdriver.logging.WebDriverLogger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author wenjzhu
 */
public class LocalEmaasWebDriverLoader
{
	public static String T_WORK_PATH = "/Users/wenjzhu/Documents/Temp";

	private final java.util.logging.Logger log = java.util.logging.Logger.getLogger(LocalEmaasWebDriverLoader.class
			.getCanonicalName());
	private WebDriver wd;

	@Mocked
	WebDriverLogger logger1;
	@Mocked
	EMTestLogger logger2;
	@Mocked
	Utils utils;

	//	@Mocked
	//	WebDriver mockWebDriver;

	public WebDriver myWebDriver()
	{
		return wd;
	}

	public void runEmaasWebDriver()
	{
		final org.openqa.selenium.WebDriver webDriver = new FirefoxDriver();

		class MySetUpBrowser extends MockUp<SetUpBrowser>
		{
			@Mock
			public void $init(String testName)
			{
				//System.out.println("Init mock SetUpBrowser");
			}

			@Mock
			public org.openqa.selenium.WebDriver launch(WebDriver wd)
			{
				//System.out.println("mock SetUpBrowser: launch");
				return webDriver;
			}
		}

		new MySetUpBrowser();
		new NonStrictExpectations() {
			{
				WebDriverLogger.getLogger(anyString);
				result = log;
				EMTestLogger.getLogger(anyString);
				result = log;
				//				mockWebDriver.getLogger();
				//				result = log;
				Utils.getProperty("T_WORK");
				result = T_WORK_PATH;
			}
		};
		wd = WebDriverUtils.initWebDriver(this.getClass().getName() + "."
				+ Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	//@Test
	public void testDemo()
	{
		runEmaasWebDriver();
		wd.getWebDriver().get("http://www.google.com");
		//wd.getWebDriver().get("http://www.baidu.com");

		//File file = wd.takeScreenShot();
		wd.click("searchform");

		WebElement element = wd.getWebDriver().findElement(By.name("q"));

		// Enter something to search for
		element.sendKeys("Cheese!");

		wd.click("//input[@name='btnK']");

		wd.shutdownBrowser(true);

		// Now submit the form. WebDriver will find the form for us from the element
		//element.submit();
	}
}
