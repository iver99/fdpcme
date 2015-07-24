/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.wls.management;

import java.net.URISyntaxException;
import java.net.URL;

import oracle.sysman.emaas.platform.dashboards.webutils.services.LoggingServiceManager;

import org.apache.logging.log4j.core.config.Configurator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class AppLoggingManageMXBeanTest
{
	@BeforeMethod
	public void beforeMethod() throws URISyntaxException
	{
		URL url = LoggingServiceManager.class.getResource("/log4j2_dsb.xml");
		Configurator.initialize("root", LoggingServiceManager.class.getClassLoader(), url.toURI());
	}

	@Test
	public void testGetLogLevels()
	{
		AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
		String levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"INFO\""));
	}

	@Test
	public void testSetLogLevels()
	{
		AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "DEBUG");
		String levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"DEBUG\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "INFO");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"INFO\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "WARN");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"WARN\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "ERROR");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"ERROR\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards", "FATAL");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards\":\"FATAL\""));
	}

}
