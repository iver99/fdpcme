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

import java.util.Date;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.uifwk.ui.target.services.GlobalStatus;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.util.StringUtil;
import oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.management.timer.Timer;

/**
 * @author aduan
 */
public class AvailabilityServiceManager implements ApplicationServiceManager, NotificationListener
{
	private static final long PERIOD = Timer.ONE_SECOND * 20;

	private static final String DASHBOARD_API_SERVICE_NAME = "Dashboard-API";

	private static final String DASHBOARD_API_SERVICE_VERSION = "1.0+";
	private static final String DASHBOARD_API_SERVICE_REL = "base";
	private static final String SAVED_SEARCH_SERVICE_NAME = "SavedSearch";

	private static final String SAVED_SEARCH_SERVICE_VERSION = "1.0+";
	private static final String SAVED_SEARCH_SERVICE_REL = "search";
	private static final Logger LOGGER = LogManager.getLogger(AvailabilityServiceManager.class);

	private Timer timer;
	private Integer notificationId;
	private final RegistryServiceManager rsm;

	public AvailabilityServiceManager(RegistryServiceManager rsm)
	{
		this.rsm = rsm;
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#getName()
	 */
	@Override
	public String getName()
	{
		return "OMC UI Framework Timer Service";
	}

	/* (non-Javadoc)
	 * @see javax.management.NotificationListener#handleNotification(javax.management.Notification, java.lang.Object)
	 */
	@Override
	public void handleNotification(Notification notification, Object handback)
	{
		LOGGER.debug("Time triggered handler method. sequenceNumber={}, notificationId={}", notification.getSequenceNumber(),
				notificationId);
		if (rsm.isRegistrationComplete() == null) {
			LOGGER.info("RegistryServiceManager hasn't registered. Check registry service next time");
			return;
		}
		// check if service manager is up and registration is complete
		if (!rsm.isRegistrationComplete() && !rsm.registerService()) {
			LOGGER.info(
					"OMC UI Framework service registration is not completed. Ignore dependant services availability checking");
			return;

		}
		// check ssf's avaibility
		boolean isSSFAvailable = true;
		try {
			isSSFAvailable = isSavedSearchAvailable();
		}
		catch (Exception e) {
			isSSFAvailable = false;
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		if (!isSSFAvailable) {
//			List<InstanceInfo> services = new ArrayList<InstanceInfo>();
//			InstanceInfo ii = new InstanceInfo();
//			ii.setServiceName(SAVED_SEARCH_SERVICE_NAME);
//			ii.setVersion(SAVED_SEARCH_SERVICE_VERSION);
//			services.add(ii);
//			rsm.markOutOfService(services, null, null);
//			GlobalStatus.setOmcUiDownStatus();
			LOGGER.error("OMC UI Framework service keeps running, although Saved Search API service is unavailable");
//			return;
		}

		// check df api service's availability
		boolean isDFApiAvailable = true;
		try {
			isDFApiAvailable = isDashboardAPIAvailable();
		}
		catch (Exception e) {
			isDFApiAvailable = false;
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		if (!isDFApiAvailable) {
//			List<InstanceInfo> services = new ArrayList<InstanceInfo>();
//			InstanceInfo ii = new InstanceInfo();
//			ii.setServiceName(DASHBOARD_API_SERVICE_NAME);
//			ii.setVersion(DASHBOARD_API_SERVICE_VERSION);
//			services.add(ii);
//			rsm.markOutOfService(services, null, null);
//			GlobalStatus.setOmcUiDownStatus();
			LOGGER.error("OMC UI Framework service keeps running, although Dashboard API service is unavailable");
//			return;
		}
		
		// now all checking is OK
		try {
			rsm.markServiceUp();
			GlobalStatus.setOmcUiUpStatus();
			LOGGER.debug("OMC UI Framework service is up");
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws Exception
	{
		timer = new Timer();
		timer.addNotificationListener(this, null, null);
		Date timerTriggerAt = new Date(new Date().getTime() + 10000L);
		notificationId = timer.addNotification("OmcUiFrameworkServiceTimer", null, this, timerTriggerAt, PERIOD, 0);
		timer.start();
		LOGGER.info("Timer for OMC UI Framework service dependencies checking started. notificationId={}", notificationId);
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#postStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStop(ApplicationLifecycleEvent evt) 
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStart(ApplicationLifecycleEvent evt) 
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws Exception
	{
		LOGGER.info("Pre-stopping availability service");
		try {
			timer.stop();
			timer.removeNotification(notificationId);
			LOGGER.info("Timer for OMC UI Framework dependencies checking stopped, notificationId={}", notificationId);
		}
		catch (InstanceNotFoundException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
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
