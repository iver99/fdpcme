/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.targetmodel.services;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class JMXUtilTest
{
	private final JMXUtil imxu = JMXUtil.getInstance();
	private final MBeanServer server = ManagementFactory.getPlatformMBeanServer();

	@Test(groups = { "s1" })
	public void testGetInstance()
	{
		Assert.assertNotNull(imxu);
	}

	@Test(groups = { "s1" })
	public void testRegisterMBeans() throws MalformedObjectNameException, InstanceAlreadyExistsException,
			MBeanRegistrationException, NotCompliantMBeanException, InstanceNotFoundException
	{
		imxu.registerMBeans();
		ObjectName statusObjectName = new ObjectName(JMXUtil.DASHBOARDUI_STATUS);
		Assert.assertTrue(server.isRegistered(statusObjectName));
		imxu.unregisterMBeans();
		Assert.assertFalse(server.isRegistered(statusObjectName));
	}
}
