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
import javax.management.Notification;
import javax.management.NotificationListener;

import oracle.sysman.emaas.platform.dashboards.core.DBConnectionManager;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.management.timer.Timer;

/**
 * @author guobaochen
 */
public class AvailabilityServiceManager implements ApplicationServiceManager, NotificationListener
{
	private final Logger logger = LogManager.getLogger(AvailabilityServiceManager.class);

	private static final long PERIOD = Timer.ONE_MINUTE;
	private Timer timer;
	private Integer notificationId;
	private final RegistryServiceManager rsm;

	public AvailabilityServiceManager(RegistryServiceManager rsm)
	{
		this.rsm = rsm;
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
	 * @see javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object)
	 */
	@Override
	public void handleNotification(Notification notification, Object handback)
	{
		// check database available
		boolean isDBAvailable = isDatabaseAvailable();
		// update dashboard API service status
		if (!isDBAvailable) {
			rsm.makeServiceOutOfService();
			logger.info("Dashboards service is out of service because database is unavailable");
		}
		else {
			try {
				rsm.makeServiceUp();
				logger.debug("Dashboards service is up");
			}
			catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws Exception
	{
		timer = new Timer();
		timer.addNotificationListener(this, null, null);
		Date timerTriggerAt = new Date(new Date().getTime() + 10000L);
		notificationId = timer.addNotification("DashboardsServiceTimer", null, this, timerTriggerAt, PERIOD, 0);
		timer.start();
		logger.info("Timer for dashboard service dependencies checking started");
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStop(ApplicationLifecycleEvent evt) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStart(ApplicationLifecycleEvent evt) throws Exception
	{
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws Exception
	{
		try {
			timer.stop();
			timer.removeNotification(notificationId);
			logger.info("Timer for dashboards dependencies checking stopped");
		}
		catch (InstanceNotFoundException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private boolean isDatabaseAvailable()
	{
		DBConnectionManager dbcm = DBConnectionManager.getInstance();
		return dbcm.isDatabaseConnectionAvailable();
	}

}
