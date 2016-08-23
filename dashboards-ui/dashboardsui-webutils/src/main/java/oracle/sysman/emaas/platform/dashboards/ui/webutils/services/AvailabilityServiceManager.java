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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.dashboards.ui.targetmodel.services.GlobalStatus;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.management.timer.Timer;

/**
 * @author guobaochen
 */
public class AvailabilityServiceManager implements ApplicationServiceManager, NotificationListener
{
	private static final long PERIOD = Timer.ONE_MINUTE;

	private static final String DASHBOARD_API_SERVICE_NAME = "Dashboard-API";

	private static final String DASHBOARD_API_SERVICE_VERSION = "1.0+";
	private static final String DASHBOARD_API_SERVICE_REL = "base";
	private static final String DASHBOARD_COMMON_UI_SERVICE_NAME = "OMC-UI-Framework";

	private static final String DASHBOARD_COMMON_UI_SERVICE_VERSION = "1.0+";
	private static final String SAVED_SEARCH_SERVICE_NAME = "SavedSearch";

	private static final String SAVED_SEARCH_SERVICE_VERSION = "1.0+";
	private static final String SAVED_SEARCH_SERVICE_REL = "search";
	private final static Logger LOGGER = LogManager.getLogger(AvailabilityServiceManager.class);

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
		LOGGER.debug("Time triggered handler method. sequenceNumber={}, notificationId={}", notification.getSequenceNumber(),
				notificationId);
		if (rsm.isRegistrationComplete() == null) {
			LOGGER.info("RegistryServiceManager hasn't registered. Check registry service next time");
			return;
		}
		// check if service manager is up and registration is complete
		if (!rsm.isRegistrationComplete() && !rsm.registerService()) {
			LOGGER.info("Dashboards UI service registration is not completed. Ignore dependant services availability checking");
			return;

		}
		// check ssf avaibility
		boolean isSSFAvailable = true;
		try {
			isSSFAvailable = isSavedSearchAvailable();
		}
		catch (Exception e) {
			isSSFAvailable = false;
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		if (!isSSFAvailable) {
			List<InstanceInfo> services = new ArrayList<InstanceInfo>();
			InstanceInfo ii = new InstanceInfo();
			ii.setServiceName(SAVED_SEARCH_SERVICE_NAME);
			ii.setVersion(SAVED_SEARCH_SERVICE_VERSION);
			services.add(ii);
			rsm.markOutOfService(services, null, null);
			GlobalStatus.setDashboardUIDownStatus();
			LOGGER.error("Dashboards UI service is out of service because Saved Search API service is unavailable");
			return;
		}

		// check df api service avaibility
		boolean isDFApiAvailable = true;
		try {
			isDFApiAvailable = isDashboardAPIAvailable();
		}
		catch (Exception e) {
			isDFApiAvailable = false;
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		if (!isDFApiAvailable) {
			List<InstanceInfo> services = new ArrayList<InstanceInfo>();
			InstanceInfo ii = new InstanceInfo();
			ii.setServiceName(DASHBOARD_API_SERVICE_NAME);
			ii.setVersion(DASHBOARD_API_SERVICE_VERSION);
			services.add(ii);
			rsm.markOutOfService(services, null, null);
			GlobalStatus.setDashboardUIDownStatus();
			LOGGER.error("Dashboards UI service is out of service because Dashboard API service is unavailable");
			return;
		}

		// check if dashboard common UI service availability
		boolean isCommonUIAvailable = true;
		try {
			isCommonUIAvailable = isCommonUIAvailable();
		}
		catch (Exception e) {
			isCommonUIAvailable = false;
			LOGGER.error(e.getLocalizedMessage(), e);
		}
		if (!isCommonUIAvailable) {
			List<InstanceInfo> services = new ArrayList<InstanceInfo>();
			InstanceInfo ii = new InstanceInfo();
			ii.setServiceName(DASHBOARD_COMMON_UI_SERVICE_NAME);
			ii.setVersion(DASHBOARD_COMMON_UI_SERVICE_VERSION);
			services.add(ii);
			rsm.markOutOfService(services, null, null);
			GlobalStatus.setDashboardUIDownStatus();
			LOGGER.info("Dashboards UI service is out of service because OMC UI Framework service is unavailable");
			return;
		}

		// now all checking is OK
		try {
			rsm.markServiceUp();
			GlobalStatus.setDashboardUIUpStatus();

			LOGGER.debug("Dashboards UI service is up");
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void postStart(ApplicationLifecycleEvent evt) 
	{
		timer = new Timer();
		timer.addNotificationListener(this, null, null);
		Date timerTriggerAt = new Date(new Date().getTime() + 10000L);
		notificationId = timer.addNotification("DashboardsUIServiceTimer", null, this, timerTriggerAt, PERIOD, 0);
		timer.start();
		LOGGER.info("Timer for dashboards UI service dependencies checking started. notificationId={}", notificationId);
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
	public void preStart(ApplicationLifecycleEvent evt) 
	{
	}

	/* (non-Javadoc)
	 * @see oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
	 */
	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws Exception
	{
		LOGGER.info("Pre-stopping availability service");
		try {
			timer.stop();
			timer.removeNotification(notificationId);
			LOGGER.info("Timer for dashboards UI dependencies checking stopped, notificationId={}", notificationId);
		}
		catch (InstanceNotFoundException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}

	private boolean isCommonUIAvailable()
	{
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(DASHBOARD_COMMON_UI_SERVICE_NAME)
				.withVersion(DASHBOARD_COMMON_UI_SERVICE_VERSION).build();
		try {
			List<InstanceInfo> result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			return result != null && !result.isEmpty();
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return false;
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
