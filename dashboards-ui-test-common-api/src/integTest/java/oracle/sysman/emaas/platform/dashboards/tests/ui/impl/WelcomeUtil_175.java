/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.Validator;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public class WelcomeUtil_175 extends WelcomeUtil_171
{
	public final static String DATA_EXPLORERS_LOG = "log";

	public final static String DATA_EXPLORERS_ANALYZE = "analyze";

	public final static String DATA_EXPLORERS_SEARCH = "search";

	public final static String ITA_DEFAULT = "default";

	public final static String ITA_PERFORMANCE_ANALYTICS_DATABASE = "performanceAnalyticsDatabase";

	public final static String ITA_PERFORMANCE_ANALYTICS_APPLICATION_SERVER = "performanceAnalyticsApplicationServer";

	public final static String ITA_RESOURCE_ANALYTICS_DATABASE = "resourceAnalyticsDatabase";

	public final static String ITA_RESOURCE_ANALYTICS_MIDDLEWARE = "resourceAnalyticsMiddleware";

	public final static String ITA_RESOURCE_ANALYTICS_HOST = "resourceAnalyticsHost";

	public final static String ITA_APPLICATION_PERFORMANCE_ANALYTICS = "applicationPerformanceAnalytic";

	public final static String ITA_AVAILABILITY_ANALYTICS = "availabilityAnalytics";

	public final static String ITA_DATA_EXPLORER = "dataExplorer";

	public final static String LEARN_MORE_GET_STARTED = "getStarted";

	public final static String LEARN_MORE_VIDEOS = "videos";

	public final static String LEARN_MORE_SERVICE_OFFERINGS = "serviceOfferings";

	public final static String SERVICE_NAME_APM = "APM";

	public final static String SERVICE_NAME_LA = "LA";
	public final static String SERVICE_NAME_ITA = "ITA";
	public final static String SERVICE_NAME_DASHBOARDS = "dashboards";

	public final static String SERVICE_NAME_DATA_EXPLORERS = "dataExplorers";

	@Override
	public void visitITA(WebDriver driver, String selection)
	{
		driver.getLogger().info("Visiting ITA-" + selection + " from Welcome Page...");

		Validator
		.fromValidValues("ITASelection", selection, ITA_DEFAULT, ITA_PERFORMANCE_ANALYTICS_DATABASE,
				ITA_PERFORMANCE_ANALYTICS_APPLICATION_SERVER, ITA_RESOURCE_ANALYTICS_DATABASE,
				ITA_RESOURCE_ANALYTICS_MIDDLEWARE, ITA_RESOURCE_ANALYTICS_HOST, ITA_APPLICATION_PERFORMANCE_ANALYTICS,
				ITA_AVAILABILITY_ANALYTICS, ITA_DATA_EXPLORER);

		WaitUtil.waitForPageFullyLoaded(driver);

		if (ITA_DEFAULT.equals(selection)) {
			driver.waitForElementPresent("id=" + DashBoardPageId.WELCOME_ITALINKID);
			driver.click("id=" + DashBoardPageId.WELCOME_ITALINKID);
			driver.takeScreenShot();
		}
		else {
			String eleXpath = null;
			driver.waitForElementPresent("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);
			driver.click("id=oj-select-choice-" + DashBoardPageId.WELCOME_ITA_SELECTID);
			driver.takeScreenShot();
			switch (selection) {
				case ITA_PERFORMANCE_ANALYTICS_DATABASE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_PADATABASE);
					break;
				case ITA_PERFORMANCE_ANALYTICS_APPLICATION_SERVER:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_PAMIDDLEWARE);
					break;
				case ITA_RESOURCE_ANALYTICS_DATABASE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_RADATABASE);
					break;
				case ITA_RESOURCE_ANALYTICS_MIDDLEWARE:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_RAMIDDLEWARE);
					break;
				case ITA_RESOURCE_ANALYTICS_HOST:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_RA_HOST);
					break;
				case ITA_APPLICATION_PERFORMANCE_ANALYTICS:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_PANALYTIC);
					break;
				case ITA_AVAILABILITY_ANALYTICS:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID,
							DashBoardPageId.WELCOME_ITA_AVANALYTIC);
					break;
				case ITA_DATA_EXPLORER:
					eleXpath = getOptionXpath(driver, DashBoardPageId.WELCOME_ITA_SELECTID, DashBoardPageId.WELCOME_ITA_DE);
					break;
				default:
					break;
			}
			driver.click(eleXpath);
			driver.takeScreenShot();
		}
	}
}
