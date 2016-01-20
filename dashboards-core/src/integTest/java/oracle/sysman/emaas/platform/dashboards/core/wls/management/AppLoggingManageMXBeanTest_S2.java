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

import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.core.config.Configurator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
@Test(groups = { "s2" })
public class AppLoggingManageMXBeanTest_S2
{
	@BeforeClass
	public void beforeClass() throws URISyntaxException
	{
		URL url = AppLoggingManageMXBeanTest_S2.class
				.getResource("/oracle/sysman/emaas/platform/dashboards/core/wls/management/log4j2_dsb_logging_unittest.xml");
		Configurator.initialize("root", AppLoggingManageMXBeanTest_S2.class.getClassLoader(), url.toURI());
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
