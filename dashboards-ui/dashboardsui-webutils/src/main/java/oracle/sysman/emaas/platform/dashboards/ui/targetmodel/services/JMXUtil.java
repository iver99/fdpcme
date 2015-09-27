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

	private static final Logger _logger = LogManager.getLogger(JMXUtil.class);
	private MBeanServer server = null;

	private static volatile JMXUtil instance = null;

	public static final String DASHBOARDUI_STATUS = "oracle.sysman.emaas.platform.dashboards:Name=DashboardUIStatus,Type=oracle.sysman.emaas.platform.dashboards.ui.targetmodel.services.DashboardUIStatus";

	public static JMXUtil getInstance()
	{
		if (instance == null) {
			synchronized (JMXUtil.class) {
				if (instance == null) {
					instance = new JMXUtil();
				}
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
		ObjectName savedSearchStatusObjectName = new ObjectName(DASHBOARDUI_STATUS);
		if (!server.isRegistered(savedSearchStatusObjectName)) {
			DashboardUIStatus savedSearchStatus = new DashboardUIStatus();
			server.registerMBean(savedSearchStatus, savedSearchStatusObjectName);
		}

		_logger.info("start MBeans!");
	}

	public void unregisterMBeans() throws MalformedObjectNameException, MBeanRegistrationException, InstanceNotFoundException
	{
		ObjectName savedSearchStatusObjectName = new ObjectName(DASHBOARDUI_STATUS);
		if (server.isRegistered(savedSearchStatusObjectName)) {
			server.unregisterMBean(savedSearchStatusObjectName);
		}
		_logger.info("stop MBeans!");
	}

}
