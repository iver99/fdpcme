/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.services;

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
	LoggingServiceManager lsm = new LoggingServiceManager();

	@Mocked
	private URL _url;

	@Test(groups = { "s1" })
	public void testGetName()
	{
		AssertJUnit.assertEquals(lsm.getName(), "OMC UI Framework Service API Logging Service");
	}

	@Test(groups = { "s2" })
	public void testStartStop(@Mocked final MBeanServer anyMbs, @Mocked final ManagementFactory anyMf) 
	{
		new MockUp<Class<LoggingServiceManager>>() {
			@Mock
			public URL getResource(String path)
			{
				try {
					return new URL("TestURL");
				}
				catch (MalformedURLException e) {
					e.printStackTrace();
					return null;
				}
			}
		};

		try {
			new Expectations() {
				{
					ManagementFactory.getPlatformMBeanServer();
					result = anyMbs;
					anyMbs.unregisterMBean((ObjectName) any);
					times = 1;
				}
			};
		}
		catch (MBeanRegistrationException | InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			lsm.preStart(null);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			lsm.preStop(null);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(groups = { "s2" })
	public void testStartStopExceptions(@Mocked final MBeanServer anyMbs, @Mocked final ManagementFactory anyMf)
	{
		new MockUp<Class<LoggingServiceManager>>() {
			@Mock
			public URL getResource(String path)
			{
				try {
					return new URL("TestURL1");
				}
				catch (MalformedURLException e) {
					e.printStackTrace();
					return null;
				}
			}
		};
		try {
			new Expectations() {
				{
					ManagementFactory.getPlatformMBeanServer();
					result = anyMbs;
					anyMbs.registerMBean(any, (ObjectName) any);
					result = new InstanceAlreadyExistsException();
					anyMbs.unregisterMBean((ObjectName) any);
					result = new Exception();
				}
			};

			lsm.preStart(null);
			lsm.preStop(null);
		}
		catch (Exception e) {

		}
	}
}
