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

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

public interface IWelcomeUtil extends IUiTestCommonAPI
{
	/**
	 * Visit Log/Analyze/Search from Data Explorers dropdown in welcome
	 *
	 * @param driver
	 * @param selection
	 *            log | analyze | search
	 */
	public void dataExplorers(WebDriver driver, String selection) throws Exception;

	/**
	 * Check if specific item in Learn More is existed in welcome.
	 *
	 * @param driver
	 * @param itemName
	 *            getStarted | videos | serviceOfferings
	 * @return
	 */
	public boolean isLearnMoreItemExisted(WebDriver driver, String itemName);

	/**
	 * Check if specific service is existed in welcome
	 *
	 * @param driver
	 * @param serviceName
	 *            APM | LA | ITA | dashboards | dataExplorers
	 * @return
	 * @throws Exception
	 */
	public boolean isServiceExistedInWelcome(WebDriver driver, String serviceName) throws Exception;

	/**
	 * Visit "How to get started" in welcome
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void learnMoreHow(WebDriver driver) throws Exception;

	/**
	 * Visit "Service Offerings" in welcome
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void learnMoreServiceOffering(WebDriver driver) throws Exception;

	/**
	 * Visit "Videos" in welcome
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void learnMoreVideo(WebDriver driver) throws Exception;

	/**
	 * Visit "Application Performance Monitoring" in welcome
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void visitAPM(WebDriver driver) throws Exception;

	/**
	 * Visit "Dashboards" in welcome
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void visitDashboards(WebDriver driver) throws Exception;

	/**
	 * Visit specific item in IT Analytics in welcome
	 *
	 * @param driver
	 * @param selection
	 *            default | performanceAnayticsDatabase | performanceAnalyticsMiddleware | resourceAnalyticsDatabase |
	 *            resourceAnalyticsMiddleware | dataExplorerAnalyze | dataExplorer
	 * @throws Exception
	 */
	public void visitITA(WebDriver driver, String selection) throws Exception;

	/**
	 * Visit "Log Analytics" in welcome
	 *
	 * @param driver
	 * @throws Exception
	 */
	public void visitLA(WebDriver driver) throws Exception;
}
