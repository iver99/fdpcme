/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.services;

import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.AbstractApplicationLifecycleService;

public class DashboardApiLifecycleService extends AbstractApplicationLifecycleService
{

	public DashboardApiLifecycleService()
	{
		RegistryServiceManager rsm = new RegistryServiceManager();
		addApplicationServiceManager(rsm);
		addApplicationServiceManager(new LoggingServiceManager());
		addApplicationServiceManager(new AvailabilityServiceManager(rsm));
		addApplicationServiceManager(new EMTargetInitializer());
		addApplicationServiceManager(new ThreadPoolManager());
		addApplicationServiceManager(new MetadataManager());
	}
}
