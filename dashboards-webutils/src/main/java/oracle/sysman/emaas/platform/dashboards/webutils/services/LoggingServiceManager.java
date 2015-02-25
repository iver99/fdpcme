/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.services;

import java.lang.management.ManagementFactory;
import java.net.URL;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.management.AppLoggingManageMXBean;

import org.apache.logging.log4j.core.config.Configurator;

import weblogic.application.ApplicationLifecycleEvent;

/**
 * @author guobaochen by default we log entries to /var/opt/ORCLemaas/logs/dashboardsService/dashboardsService.log
 */
public class LoggingServiceManager implements ApplicationServiceManager
{
	public static final String MBEAN_NAME = "oracle.sysman.emaas.platform.dashboardsservice.logging.beans:type=AppLoggingManageMXBean";

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#getName()
	 */
	@Override
	public String getName()
	{
		return "Dashboard Service API Logging Service";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws Exception
	{
		URL url = LoggingServiceManager.class.getResource("/log4j2_dsb.xml");
		Configurator.initialize("root", LoggingServiceManager.class.getClassLoader(), url.toURI());
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStop(ApplicationLifecycleEvent evt) throws Exception
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStart(ApplicationLifecycleEvent evt) throws Exception
	{
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		mbs.registerMBean(new AppLoggingManageMXBean(), new ObjectName(MBEAN_NAME));
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws Exception
	{
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		mbs.unregisterMBean(new ObjectName(MBEAN_NAME));
	}

}
