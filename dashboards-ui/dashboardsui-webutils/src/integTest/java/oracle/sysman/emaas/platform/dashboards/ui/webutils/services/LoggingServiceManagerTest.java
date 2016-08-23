/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class LoggingServiceManagerTest
{

	private static final Logger logger = LogManager.getLogger(LoggingServiceManagerTest.class);

	LoggingServiceManager lsm = new LoggingServiceManager();
	@Mocked
	private URL _url;

	@Test(groups = { "s1" })
	public void testGetName()
	{
		AssertJUnit.assertEquals(lsm.getName(), "Dashboard UI Service API Logging Service");
	}

	@Test(groups = { "s2" })
	public void testStartStop(@Mocked final MBeanServer anyMbs, @Mocked final ManagementFactory anyMf) throws Exception 
	{
		new MockUp<Class<LoggingServiceManager>>() {
			@Mock
			public URL getResource(String path)
			{
				try {
					return new URL("TestURL1");
				}
				catch (MalformedURLException e) {
					logger.info("context",e);
					return null;
				}
			}
		};
		new Expectations() {
			{
				ManagementFactory.getPlatformMBeanServer();
				result = anyMbs;
				anyMbs.unregisterMBean((ObjectName) any);
				times = 1;
			}
		};

		lsm.preStart(null);
		lsm.preStop(null);
	}

	@Test(groups = { "s2" })
	public void testStartStopExceptions(@Mocked final MBeanServer anyMbs, @Mocked final ManagementFactory anyMf,
			@Mocked final ObjectName anyObjName)
	{
		new MockUp<Class<LoggingServiceManager>>() {
			@Mock
			public URL getResource(String path)
			{
				try {
					return new URL("TestURL1");
				}
				catch (MalformedURLException e) {
					logger.info("context",e);
					return null;
				}
			}
		};
		try {
			final ObjectName objName1 = new ObjectName(LoggingServiceManager.MBEAN_NAME);
			new Expectations() {
				{
					new ObjectName(LoggingServiceManager.MBEAN_NAME);
					result = objName1;
					ManagementFactory.getPlatformMBeanServer();
					result = anyMbs;
					anyMbs.registerMBean(any, objName1);
					result = new InstanceAlreadyExistsException();
					anyMbs.registerMBean(any, (ObjectName) any);
					anyMbs.unregisterMBean((ObjectName) any);
					result = new Exception();
				}
			};

			lsm.preStart(null);
			lsm.preStop(null);
		}
		catch (Exception e) {
			logger.info("context",e);
		}
	}
}
