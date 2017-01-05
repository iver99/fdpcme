/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.webutils.wls.lifecycle;

import java.util.ArrayList;
import java.util.List;

import weblogic.application.ApplicationException;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.i18n.logging.NonCatalogLogger;

public class AbstractApplicationLifecycleService extends ApplicationLifecycleListener
{
	public static final String APPLICATION_LOGGER_SUBSYSTEM = "oracle.sysman.emaas.platform.dashboards.comparator";

	private final NonCatalogLogger logger = new NonCatalogLogger(APPLICATION_LOGGER_SUBSYSTEM + ".services");

	private final List<ApplicationServiceManager> registeredServices = new ArrayList<ApplicationServiceManager>();

	public AbstractApplicationLifecycleService(ApplicationServiceManager... services)
	{
		for (ApplicationServiceManager service : services) {
			registeredServices.add(service);
		}
	}

	public void addApplicationServiceManager(ApplicationServiceManager asm)
	{
		registeredServices.add(asm);
	}

	@Override
	public void postStart(ApplicationLifecycleEvent evt) throws ApplicationException
	{
		logger.notice("Post-starting registered Dashboard Comparator services");
		try {
			logger.info("Post-starting 'Service Registry'");
			for (ApplicationServiceManager service : registeredServices) {
				service.postStart(evt);
			}
			logger.debug("Post-started 'Service Registry'");
		}
		catch (Exception t) {
			logger.error("Service failed to post-start", t);
			throw new ApplicationException("Service failed to post-start", t);
		}
	}

	@Override
	public void postStop(ApplicationLifecycleEvent evt) throws ApplicationException
	{
		logger.notice("Post-stopping registered Dashboard Comparator services");
		try {
			logger.info("Post-stopping 'Service Registry'");
			for (ApplicationServiceManager service : registeredServices) {
				service.postStop(evt);
			}
			logger.debug("Post-stopped 'Service Regsitry'");
		}
		catch (Exception t) {
			logger.error("Service failed to post-stop", t);
			throw new ApplicationException("some of the essential services failed to post-stop");
		}
	}

	@Override
	public void preStart(ApplicationLifecycleEvent evt) throws ApplicationException
	{
		logger.debug("Pre-starting registered Dashboard Comparator services");
		try {
			logger.info("Pre-starting 'Service Registry'");
			for (ApplicationServiceManager service : registeredServices) {
				service.preStart(evt);
			}
			logger.debug("Pre-started 'Service Registry'");
		}
		catch (Exception t) {
			logger.error("Service failed to pre-start", t);
			throw new ApplicationException("Service failed to pre-start", t);
		}
	}

	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws ApplicationException
	{
		logger.debug("Pre-stopping registered Dashboard Comparator services");
		try {
			logger.info("Pre-stopping 'Service Registry'");
			for (ApplicationServiceManager service : registeredServices) {
				service.preStop(evt);
			}
			logger.debug("Pre-stopped 'Service Registry'");
		}
		catch (Exception t) {
			logger.error("Service failed to pre-stop", t);
			throw new ApplicationException("some of the essential services failed to pre-stop");
		}
	}
}
