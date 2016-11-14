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
	public void dataExplorers(WebDriver driver, String selection);

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
	 * @return @
	 */
	public boolean isServiceExistedInWelcome(WebDriver driver, String serviceName);

	/**
	 * Visit "How to get started" in welcome
	 *
	 * @param driver
	 *            @
	 */
	public void learnMoreHow(WebDriver driver);

	/**
	 * Visit "Service Offerings" in welcome
	 *
	 * @param driver
	 *            @
	 */
	public void learnMoreServiceOffering(WebDriver driver);

	/**
	 * Visit "Videos" in welcome
	 *
	 * @param driver
	 *            @
	 */
	public void learnMoreVideo(WebDriver driver);

	/**
	 * Visit "Application Performance Monitoring" in welcome
	 *
	 * @param driver
	 *            @
	 */
	public void visitAPM(WebDriver driver);

	/**
	 * Visit "Compliance" in welcome
	 *
	 * @param driver
	 *            @
	 */
	public void visitCompliance(WebDriver driver);

	/**
	 * Visit "Dashboards" in welcome
	 *
	 * @param driver
	 *            @
	 */
	public void visitDashboards(WebDriver driver);

	/**
	 * Visit specific item in IT Analytics in welcome
	 *
	 * @param driver
	 * @param selection
	 *            default | performanceAnayticsDatabase | performanceAnalyticsMiddleware | resourceAnalyticsDatabase |
	 *            resourceAnalyticsMiddleware | dataExplorerAnalyze | dataExplorer @
	 */
	public void visitITA(WebDriver driver, String selection);

	/**
	 * Visit "Log Analytics" in welcome
	 *
	 * @param driver
	 *            @
	 */
	public void visitLA(WebDriver driver);

	/**
	 * Visit "Orchestration" in welcome
	 *
	 * @param driver
	 *            @
	 */
	public void visitOrchestration(WebDriver driver);

	/**
	 * Visit "Security" in welcome
	 *
	 * @param driver
	 *            @
	 */
	public void visitSecurity(WebDriver driver);

	/**
	 * Visit "Infrustructure Monitoring" in welcome
	 * 
	 * @param driver
	 * @
	 */
	public void visitInfraMonitoring(WebDriver driver); 	
}
