/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.targetmodel.services;

/**
 * @author vinjoshi
 *
 */

import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class JMXUtil
{

	private static final Logger _LOGGER = LogManager.getLogger(JMXUtil.class);
	private MBeanServer server = null;

	private static volatile JMXUtil instance = null;

	public static final String DASHBOARDUI_STATUS = "oracle.sysman.emaas.platform.dashboards:Name=DashboardUIStatus,Type=oracle.sysman.emaas.platform.dashboards.ui.targetmodel.services.DashboardUIStatus";

	public static JMXUtil getInstance()
	{
		synchronized (JMXUtil.class) {
			if (instance == null) {
				instance = new JMXUtil();
			}
		}

		return instance;
	}

	private JMXUtil()
	{
	}

	public void registerMBeans() throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException
	{
		server = ManagementFactory.getPlatformMBeanServer();
		ObjectName statusObjectName = new ObjectName(DASHBOARDUI_STATUS);
		if (!server.isRegistered(statusObjectName)) {
			DashboardUIStatus savedSearchStatus = new DashboardUIStatus();
			server.registerMBean(savedSearchStatus, statusObjectName);
		}

		_LOGGER.info("start Dashboard UI MBeans!");
	}

	public void unregisterMBeans() throws MalformedObjectNameException, MBeanRegistrationException, InstanceNotFoundException
	{
		ObjectName statusObjectName = new ObjectName(DASHBOARDUI_STATUS);
		if (server.isRegistered(statusObjectName)) {
			server.unregisterMBean(statusObjectName);
		}
		_LOGGER.info("stop Dashboard UI MBeans!");
	}

}
