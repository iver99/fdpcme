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

import javax.management.InstanceNotFoundException;
import javax.management.NotificationListener;

import mockit.Expectations;
import mockit.Mocked;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import weblogic.management.timer.Timer;

/**
 * @author aduan
 */
public class AvailabilityServiceManagerTest
{
	private final RegistryServiceManager rsm = new RegistryServiceManager();
	private final AvailabilityServiceManager asm = new AvailabilityServiceManager(rsm);

	@Test(groups = { "s1" })
	public void testGetName()
	{
		AssertJUnit.assertEquals(asm.getName(), "Dashboard Service API Timer Service");
	}

	@Test(groups = { "s2" })
	public void testStartStop(@Mocked final Timer anyTimer) throws Exception
	{
		new Expectations() {
			{
				new Timer();
				times = 1;
				anyTimer.addNotificationListener((NotificationListener) any, null, null);
				times = 1;
				anyTimer.start();
				times = 1;
				anyTimer.stop();
				times = 1;
				anyTimer.removeNotification((Integer) any);
				times = 1;
			}
		};

		asm.postStart(null);
		asm.preStop(null);
	}

	@Test(groups = { "s2" })
	public void testStartStopException(@Mocked final Timer anyTimer) throws Exception
	{
		new Expectations() {
			{
				anyTimer.removeNotification((Integer) any);
				result = new InstanceNotFoundException();
				times = 1;
			}
		};

		asm.preStop(null);
	}
}
