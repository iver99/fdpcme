/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.target.services;

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

/**
 * @author vinjoshi
 */
public class JMXUtil
{
	private static final Logger _LOGGER = LogManager.getLogger(JMXUtil.class);
	private MBeanServer server = null;

	private static volatile JMXUtil instance = null;

	public static final String OMCUI_STATUS = "oracle.sysman.emaas.platform.uifwk:Name=OmcUiStatus,Type=oracle.sysman.emaas.platform.uifwk.ui.target.services.OmcUiStatus";

	public static JMXUtil getInstance()
	{
		synchronized (JMXUtil.class) {
			if (instance == null) {
				instance = new JMXUtil();
			}
			return instance;
		}
	}

	private JMXUtil()
	{
	}

	public void registerMBeans() throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException
	{
		server = ManagementFactory.getPlatformMBeanServer();
		ObjectName statusObjectName = new ObjectName(OMCUI_STATUS);
		if (!server.isRegistered(statusObjectName)) {
			OmcUiStatus savedSearchStatus = new OmcUiStatus();
			server.registerMBean(savedSearchStatus, statusObjectName);
		}

		_LOGGER.info("start OMC UI MBeans!");
	}

	public void unregisterMBeans() throws MalformedObjectNameException, MBeanRegistrationException, InstanceNotFoundException
	{
		ObjectName statusObjectName = new ObjectName(OMCUI_STATUS);
		if (server.isRegistered(statusObjectName)) {
			server.unregisterMBean(statusObjectName);
		}
		_LOGGER.info("stop OMC UI MBeans!");
	}

}
