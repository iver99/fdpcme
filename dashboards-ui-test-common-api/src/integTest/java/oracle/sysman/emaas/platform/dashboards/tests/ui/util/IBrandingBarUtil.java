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

public interface IBrandingBarUtil extends IUiTestCommonAPI
{
	// values for 'option' for user menu
	public static final String USERMENU_OPTION_HELP = "help";
	public static final String USERMENU_OPTION_ABOUT = "about";
	public static final String USERMENU_OPTION_SIGNOUT = "signout";

	//Branding bar link text
	//Cloud Services
	public static final String NAV_LINK_TEXT_CS_ITA = "IT Analytics";
	public static final String NAV_LINK_TEXT_CS_APM = "APM";
	public static final String NAV_LINK_TEXT_CS_LA = "Log Analytics";
	//Visual Analyzers
	public static final String NAV_LINK_TEXT_VA_ITA = "Analyze";
	public static final String NAV_LINK_TEXT_VA_TA = "Search";
	public static final String NAV_LINK_TEXT_VA_LA = "Log";
	//Administration
	public static final String NAV_LINK_TEXT_ADMIN_AGENT = "Agents";
	public static final String NAV_LINK_TEXT_ADMIN_ALERT = "Alert Rules";
	public static final String NAV_LINK_TEXT_ADMIN_ADMINCONSOLE = "Administration";

	/**
	 * Check whether admin field exist
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isAdmin(WebDriver driver) throws Exception;

	public boolean isAdminLinkExisted(WebDriver driver, String adminLinkName);

	/**
	 * Check if the specified cloud service link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param cloudServiceLinkName
	 *            Name of the link
	 * @return
	 */
	public boolean isCloudServiceLinkExisted(WebDriver driver, String cloudServiceLinkName);

	/**
	 * Check if the dashboard home link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isDashboardHomeLinkExisted(WebDriver driver);

	/**
	 * Check if the specified home link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param homeLinkName
	 *            Name of the link
	 * @return
	 */
	public boolean isHomeLinkExisted(WebDriver driver, String homeLinkName);

	/**
	 * Check if the favorites link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isMyFavoritesLinkExisted(WebDriver driver);

	/**
	 * Check if the Home link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isMyHomeLinkExisted(WebDriver driver);

	/**
	 * Check if the specified visual analyzer link is existed or not by given name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visualAnalyzerLinkName
	 *            Name of the link
	 * @return
	 */
	public boolean isVisualAnalyzerLinkExisted(WebDriver driver, String visualAnalyzerLinkName);

	/**
	 * Check if the welcome link is existed or not
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public boolean isWelcomeLinkExisted(WebDriver driver);

	/**
	 * Do the specified user option by the given option name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param option
	 *            Name of the option, should one of "about", "help", "signout"
	 * @return
	 */
	public void userMenuOptions(WebDriver driver, String option) throws Exception;

	/**
	 * Navigate to the specified administration page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param adminLinkName
	 *            Name of the link
	 * @return
	 */
	public void visitApplicationAdministration(WebDriver driver, String adminLinkName) throws Exception;

	/**
	 * Navigate to the specified cloud service page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param cloudServiceLinkName
	 *            Name of the link
	 * @return
	 */
	public void visitApplicationCloudService(WebDriver driver, String cloudServiceLinkName) throws Exception;

	public void visitApplicationHome(WebDriver driver, String homeLinkName) throws Exception;

	/**
	 * Navigate to the specified visual analyzer page by given link name
	 *
	 * @param driver
	 *            WebDriver instance
	 * @param visualAnalyzerLinkName
	 *            Name of the link
	 * @return
	 */
	public void visitApplicationVisualAnalyzer(WebDriver driver, String visualAnalyzerLinkName) throws Exception;

	/**
	 * Navigate to the dashboard home page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public void visitDashboardHome(WebDriver driver) throws Exception;

	/**
	 * Navigate to My Favorites page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public void visitMyFavorites(WebDriver driver) throws Exception;

	/**
	 * Navigate to my home page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public void visitMyHome(WebDriver driver) throws Exception;

	/**
	 * Navigate to the welcome page
	 *
	 * @param driver
	 *            WebDriver instance
	 * @return
	 */
	public void visitWelcome(WebDriver driver) throws Exception;

}
