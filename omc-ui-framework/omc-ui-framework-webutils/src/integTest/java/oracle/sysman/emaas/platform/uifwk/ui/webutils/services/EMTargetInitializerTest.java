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

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.uifwk.ui.target.services.JMXUtil;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class EMTargetInitializerTest
{
	private final EMTargetInitializer emti = new EMTargetInitializer();

	@Test(groups = { "s1" })
	public void testGetName()
	{
		AssertJUnit.assertEquals(emti.getName(), "OMC UI Target Initializer");
	}

	@Test(groups = { "s2" })
	public void testStartStop(@Mocked final InitialContext anyInitContext, @Mocked final MBeanServer anyMbs,
			@Mocked final ManagementFactory anyMf, @Mocked final JMXUtil anyJmxUtil) throws Exception
	{
		String targetType = Deencapsulation.getField(emti, "M_TARGET_TYPE");
		Assert.assertEquals(targetType, EMTargetConstants.M_TARGET_TYPE);

		new Expectations() {
			{
				InitialContext.doLookup(anyString);
				result = "Dashboard-UI";
				times = 1;
				ManagementFactory.getPlatformMBeanServer();
				result = anyMbs;
				anyMbs.registerMBean(any, (ObjectName) any);
				times = 1;
				anyJmxUtil.registerMBeans();
				times = 1;
				anyMbs.isRegistered((ObjectName) any);
				result = true;
				anyMbs.unregisterMBean((ObjectName) any);
				times = 1;
				anyJmxUtil.unregisterMBeans();
				times = 1;
			}
		};

		emti.postStart(null);
		emti.preStop(null);
	}

	@Test(groups = { "s2" })
	public void testStartStopExceptions(@Mocked final InitialContext anyInitContext, @Mocked final MBeanServer anyMbs,
			@Mocked final ManagementFactory anyMf, @Mocked final JMXUtil anyJmxUtil) throws Exception
	{
		new Expectations() {
			{
				InitialContext.doLookup(anyString);
				result = "Dashboard-API";
				ManagementFactory.getPlatformMBeanServer();
				result = anyMbs;
				anyMbs.registerMBean(any, (ObjectName) any);
				result = new InstanceAlreadyExistsException();
			}
		};

		emti.postStart(null);

		new Expectations() {
			{
				anyMbs.registerMBean(any, (ObjectName) any);
				result = new MalformedObjectNameException();
			}
		};

		emti.postStart(null);

		new Expectations() {
			{
				anyMbs.registerMBean(any, (ObjectName) any);
				result = new Exception();
				anyMbs.isRegistered((ObjectName) any);
				result = true;
				anyMbs.unregisterMBean((ObjectName) any);
				result = new Exception();
				times = 1;
				anyJmxUtil.unregisterMBeans();
				times = 0;
			}
		};

		emti.postStart(null);
		emti.preStop(null);
	}
}
