/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.wls.management;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
@Test(groups = { "s2" })
public class AppLoggingManageMXBeanTest_S2
{
	private URI oldUri;

	/*@AfterMethod
	public void afterMethod() throws URISyntaxException
	{
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		context.setConfigLocation(oldUri);
	}*/

	@BeforeMethod
	public void beforeMethod() throws URISyntaxException
	{
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		oldUri = context.getConfigLocation();
		URL url = AppLoggingManageMXBeanTest_S2.class
				.getResource("/oracle/sysman/emaas/platform/dashboards/core/wls/management/log4j2_dsb_logging_unittest.xml");
		Configurator.initialize("root", AppLoggingManageMXBeanTest_S2.class.getClassLoader(), url.toURI());
	}

	public void testGetLogLevels() throws URISyntaxException
	{
		AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
		String levels = almmxb.getLogLevels();
//		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.interaction.log\":\"INFO\""));
	}

	public void testSetLogLevels() throws URISyntaxException
	{
		AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "DEBUG");
		String levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"DEBUG\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "WARN");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"WARN\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "ERROR");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"ERROR\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "FATAL");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"FATAL\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "INFO");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"INFO\""));
	}

}
