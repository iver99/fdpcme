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
import java.net.URISyntaxException;
import java.net.URL;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
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
	public static final String MBEAN_NAME_TMP = "oracle.sysman.emaas.platform.dashboardsuiservice.logging.beans:type=AppLoggingManageMXBean"
			+ System.currentTimeMillis();
	private boolean tempMBeanExists = false;

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
	public void postStart(ApplicationLifecycleEvent evt) 
	{
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#postStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStop(ApplicationLifecycleEvent evt) 
	{

	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStart(ApplicationLifecycleEvent evt) throws Exception 
	{
		URL url = LoggingServiceManager.class.getResource("/log4j2_dsbui.xml");
		Configurator.initialize("root", LoggingServiceManager.class.getClassLoader(), url.toURI());

		try {
			registerMBean(MBEAN_NAME);
		}
		catch (InstanceAlreadyExistsException e) {
			logger.error("MBean '" + MBEAN_NAME + "' exists already when trying to register. Unregister it first.", e);
			try {
				unregisterMBean(MBEAN_NAME);
			}
			catch (Exception ex) {
				logger.error(ex);
				// failed to unregister with name 'MBEAN_NAME', register with a temporary name
				registerMBean(MBEAN_NAME_TMP);
				tempMBeanExists = true;
				return;
			}
			registerMBean(MBEAN_NAME);
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStop(ApplicationLifecycleEvent evt) 
	{
		logger.info("Pre-stopping logging service");
		try {
			if (tempMBeanExists) {
				tempMBeanExists = false;
				unregisterMBean(MBEAN_NAME_TMP);
			}
			unregisterMBean(MBEAN_NAME);
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private void registerMBean(String name) throws InstanceAlreadyExistsException, MBeanRegistrationException,
	NotCompliantMBeanException, MalformedObjectNameException
	{
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		mbs.registerMBean(new AppLoggingManageMXBean(), new ObjectName(name));
		logger.info("MBean '" + name + "' has been registered");
	}

	private void unregisterMBean(String name) throws MBeanRegistrationException, InstanceNotFoundException,
	MalformedObjectNameException
	{
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		mbs.unregisterMBean(new ObjectName(name));
		logger.info("MBean '" + name + "' has been un-registered");
	}

}
