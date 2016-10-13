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

import java.util.Date;

import javax.management.InstanceNotFoundException;
import javax.management.NotificationListener;

import oracle.sysman.emaas.platform.dashboards.webutils.timer.AvailabilityNotification;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.management.timer.Timer;

/**
 * @author guobaochen
 */
public class AvailabilityServiceManager implements ApplicationServiceManager
{
	private final static Logger LOGGER = LogManager.getLogger(AvailabilityServiceManager.class);

	private static final long PERIOD = Timer.ONE_MINUTE;

	private Timer timer;
	private Integer notificationId;
	private final RegistryServiceManager rsm;

	public AvailabilityServiceManager(RegistryServiceManager rsManager)
	{
		rsm = rsManager;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#getName()
	 */
	@Override
	public String getName()
	{
		return "Dashboard Service API Timer Service";
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStart(ApplicationLifecycleEvent evt) 
	{
		timer = new Timer();
		NotificationListener notification = new AvailabilityNotification(rsm);
		timer.addNotificationListener(notification, null, null);
		Date timerTriggerAt = new Date(new Date().getTime() + 10000L);
		notificationId = timer.addNotification("DashboardsServiceTimer", null, notification, timerTriggerAt, PERIOD, 0);
		timer.start();
		LOGGER.info("Timer for dashboard service dependencies checking started. notificationId={}", notificationId);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStop(ApplicationLifecycleEvent evt) 
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStart(ApplicationLifecycleEvent evt) 
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStop(ApplicationLifecycleEvent evt) 
	{
		LOGGER.info("Pre-stopping availability service");
		try {
			timer.stop();
			timer.removeNotification(notificationId);
			LOGGER.info("Timer for dashboards dependencies checking stopped. notificationId={}", notificationId);
		}
		catch (InstanceNotFoundException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}

}
