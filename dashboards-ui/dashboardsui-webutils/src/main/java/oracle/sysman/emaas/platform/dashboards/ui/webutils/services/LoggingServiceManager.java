package oracle.sysman.emaas.platform.dashboards.ui.webutils.services;

/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

import java.lang.management.ManagementFactory;
import java.net.URL;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.management.AppLoggingManageMXBean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import weblogic.application.ApplicationLifecycleEvent;

/**
 * @author guobaochen by default we log entries to /var/opt/ORCLemaas/logs/dashboardsService/dashboardsUiService.log
 */
public class LoggingServiceManager implements ApplicationServiceManager
{
	private final Logger logger = LogManager.getLogger(LoggingServiceManager.class);
	public static final String MBEAN_NAME = "oracle.sysman.emaas.platform.dashboardsuiservice.logging.beans:type=AppLoggingManageMXBean";

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#getName()
	 */
	@Override
	public String getName()
	{
		return "Dashboard UI Service API Logging Service";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws Exception
	{
		URL url = LoggingServiceManager.class.getResource("/log4j2_dsbui.xml");
		Configurator.initialize("root", LoggingServiceManager.class.getClassLoader(), url.toURI());
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#postStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStop(ApplicationLifecycleEvent evt) throws Exception
	{

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStart(ApplicationLifecycleEvent evt) throws Exception
	{
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		mbs.registerMBean(new AppLoggingManageMXBean(), new ObjectName(MBEAN_NAME));
		logger.info("MBean '" + MBEAN_NAME + "' has been registered");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws Exception
	{
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		mbs.unregisterMBean(new ObjectName(MBEAN_NAME));
		logger.info("MBean '" + MBEAN_NAME + "' has been un-registered");
	}

}
