/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle;

import java.util.ArrayList;
import java.util.List;

import weblogic.application.ApplicationException;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.i18n.logging.NonCatalogLogger;

public class AbstractApplicationLifecycleService extends ApplicationLifecycleListener
{
	public static final String APPLICATION_LOGGER_SUBSYSTEM = "oracle.sysman.emaas.platform.dashboards.ui";

	private final static NonCatalogLogger LOGGER = new NonCatalogLogger(APPLICATION_LOGGER_SUBSYSTEM + ".services");

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
		LOGGER.notice("Post-starting registered Dashboard Framework UI services");
		try {
			LOGGER.info("Post-starting 'Service Registry'");
			for (ApplicationServiceManager service : registeredServices) {
				service.postStart(evt);
			}
			LOGGER.debug("Post-started 'Service Registry'");
		}
		catch (Exception t) {
			LOGGER.error("Service failed to post-start", t);
			throw new ApplicationException("Service failed to post-start", t);
		}
	}

	@Override
	public void postStop(ApplicationLifecycleEvent evt) throws ApplicationException
	{
		LOGGER.notice("Post-stopping registered Dashboard Framework UI services");
		try {
			LOGGER.info("Post-stopping 'Service Registry'");
			for (ApplicationServiceManager service : registeredServices) {
				service.postStop(evt);
			}
			LOGGER.debug("Post-stopped 'Service Regsitry'");
		}
		catch (Exception t) {
			LOGGER.error("Service failed to post-stop", t);
			throw new ApplicationException("some of the essential services failed to post-stop");
		}
	}

	@Override
	public void preStart(ApplicationLifecycleEvent evt) throws ApplicationException
	{
		LOGGER.debug("Pre-starting registered Dashboard Framework UI services");
		try {
			LOGGER.info("Pre-starting 'Service Registry'");
			for (ApplicationServiceManager service : registeredServices) {
				service.preStart(evt);
			}
			LOGGER.debug("Pre-started 'Service Registry'");
		}
		catch (Exception t) {
			LOGGER.error("Service failed to pre-start", t);
			throw new ApplicationException("Service failed to pre-start", t);
		}
	}

	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws ApplicationException
	{
		LOGGER.debug("Pre-stopping registered Dashboard Framework UI services");
		try {
			LOGGER.info("Pre-stopping 'Service Registry'");
			for (ApplicationServiceManager service : registeredServices) {
				service.preStop(evt);
			}
			LOGGER.debug("Pre-stopped 'Service Registry'");
		}
		catch (Exception t) {
			LOGGER.error("Service failed to pre-stop", t);
			throw new ApplicationException("some of the essential services failed to pre-stop");
		}
	}
}
