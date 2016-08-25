/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import mockit.Expectations;
import mockit.Mocked;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class LoggingServiceManagerTest
{
	LoggingServiceManager lsm = new LoggingServiceManager();
	

	private static final Logger LOGGER = LogManager.getLogger(LoggingServiceManagerTest.class);


	@Test(groups = { "s1" })
	public void testGetName()
	{
		AssertJUnit.assertEquals(lsm.getName(), "Dashboard Service API Logging Service");
	}

	@Test(groups = { "s2" })
	public void testStartStop(@Mocked final MBeanServer anyMbs, @Mocked final ManagementFactory anyMf) throws Exception
	{
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
			LOGGER.info("context",e);
		}
	}
}
