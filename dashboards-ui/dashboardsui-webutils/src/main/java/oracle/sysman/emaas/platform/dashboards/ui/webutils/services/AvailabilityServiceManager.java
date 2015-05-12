/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.services;

import java.util.Date;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager;

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

	private static final String DASHBOARD_API_SERVICE_NAME = "Dashboard-API";
	private static final String DASHBOARD_API_SERVICE_VERSION = "0.1";
	private static final String DASHBOARD_API_SERVICE_REL = "base";

	private static final String SAVED_SEARCH_SERVICE_NAME = "SavedSearch";
	private static final String SAVED_SEARCH_SERVICE_VERSION = "0.1";
	private static final String SAVED_SEARCH_SERVICE_REL = "search";

	private Timer timer;
	private Integer notificationId;
	private final RegistryServiceManager rsm;

	public AvailabilityServiceManager(RegistryServiceManager rsm)
	{
		this.rsm = rsm;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#getName()
	 */
	@Override
	public String getName()
	{
		return "Dashboard Service UI Timer Service";
	}

	/* (non-Javadoc)
	 * @see javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object)
	 */
	@Override
	public void handleNotification(Notification notification, Object handback)
	{
		// check ssf avaibility
		boolean isSSFAvailable = true;
		try {
			isSSFAvailable = isSavedSearchAvailable();
		}
		catch (Exception e) {
			isSSFAvailable = false;
			logger.error(e.getLocalizedMessage(), e);
		}
		// check df api service avaibility
		boolean isDFApiAvailable = true;
		try {
			isDFApiAvailable = isDashboardAPIAvailable();
		}
		catch (Exception e) {
			isDFApiAvailable = false;
			logger.error(e.getLocalizedMessage(), e);
		}
		// update dashboards UI service status
		if (!isSSFAvailable) {
			rsm.makeServiceOutOfService();
			logger.info("Dashboards UI service is out of service because Saved Search API service is unavailable");
		}
		else if (!isDFApiAvailable) {
			rsm.makeServiceOutOfService();
			logger.info("Dashboards UI service is out of service because Dashboard API service is unavailable");
		}
		else {
			try {
				rsm.makeServiceUp();
				logger.debug("Dashboards UI service is up");
			}
			catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws Exception
	{
		timer = new Timer();
		timer.addNotificationListener(this, null, null);
		Date timerTriggerAt = new Date(new Date().getTime() + 10000L);
		notificationId = timer.addNotification("DashboardsUIServiceTimer", null, this, timerTriggerAt, PERIOD, 0);
		timer.start();
		logger.info("Timer for dashboards UI service dependencies checking started");
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
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws Exception
	{
		try {
			timer.stop();
			timer.removeNotification(notificationId);
			logger.info("Timer for dashboards UI dependencies checking stopped");
		}
		catch (InstanceNotFoundException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private boolean isDashboardAPIAvailable()
	{
		Link lk = RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
				DASHBOARD_API_SERVICE_REL, null);
		return lk != null && !StringUtil.isEmpty(lk.getHref());
	}

	private boolean isSavedSearchAvailable()
	{
		Link lk = RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
				SAVED_SEARCH_SERVICE_REL, null);
		return lk != null && !StringUtil.isEmpty(lk.getHref());
	}

}
