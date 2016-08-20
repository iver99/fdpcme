/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.services;

import java.lang.management.ManagementFactory;

import javax.annotation.Resource;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import oracle.sysman.emaas.platform.uifwk.ui.target.services.JMXUtil;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weblogic.application.ApplicationLifecycleEvent;

/**
 * @author vinjoshi
 */
public class EMTargetInitializer implements ApplicationServiceManager
{

	private static final Logger logger = LogManager.getLogger(EMTargetMXBeanImpl.class);
	private static final String M_TARGET_TYPE = EMTargetConstants.M_TARGET_TYPE;
	private static final String SVR_MBEAN_NAME_PREFIX = "EMDomain:Type=EMIntegration,EMTargetType=" + M_TARGET_TYPE + ",Name=";

	@Resource(lookup = "java:module/ModuleName")
	private String moduleName;

	@Resource(lookup = "java:app/AppName")
	private String appName;

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub

		return "OMC UI Target Initializer";
	}

	@Override
	public void postStart(ApplicationLifecycleEvent evt) 
	{
		// TODO Auto-generated method stub
		String emTargetMBeanName = "";
		try {
			appName = InitialContext.doLookup("java:app/AppName");
			emTargetMBeanName = SVR_MBEAN_NAME_PREFIX + appName;
			ObjectName emTargetRuntimeName = new ObjectName(emTargetMBeanName);
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			EMTargetMXBeanImpl mxbean = new EMTargetMXBeanImpl(appName);
			mbs.registerMBean(mxbean, emTargetRuntimeName);
			JMXUtil.getInstance().registerMBeans();
		}
		catch (InstanceAlreadyExistsException e) {
			logger.error("EMTargetMXBeanImpl already exists ", e);
		}
		catch (MalformedObjectNameException e) {
			logger.error("Incorrect Object name for MBean", e);
		}
		catch (Exception e) {
			logger.error("MBean Registration failed for EMTargetMxBean", e);
		}

	}

	@Override
	public void postStop(ApplicationLifecycleEvent evt) 
	{

	}

	@Override
	public void preStart(ApplicationLifecycleEvent evt) 
	{

	}

	@Override
	public void preStop(ApplicationLifecycleEvent evt) 
	{
		// TODO Auto-generated method stub
		String emTargetMBeanName = SVR_MBEAN_NAME_PREFIX + appName;
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName emTargetRuntimeName = new ObjectName(emTargetMBeanName);
			if (mbs.isRegistered(emTargetRuntimeName)) {
				mbs.unregisterMBean(emTargetRuntimeName);
			}
			JMXUtil.getInstance().unregisterMBeans();

		}
		catch (Exception e) {
			logger.error("Unregister MBean for " + M_TARGET_TYPE + " failed.", e);
		}

	}

}
