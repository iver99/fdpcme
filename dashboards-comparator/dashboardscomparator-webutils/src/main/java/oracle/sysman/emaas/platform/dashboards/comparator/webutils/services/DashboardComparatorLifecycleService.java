/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.webutils.services;

import oracle.sysman.emaas.platform.dashboards.comparator.webutils.wls.lifecycle.AbstractApplicationLifecycleService;

public class DashboardComparatorLifecycleService extends AbstractApplicationLifecycleService
{
	public DashboardComparatorLifecycleService()
	{
		RegistryServiceManager rsm = new RegistryServiceManager();
		addApplicationServiceManager(rsm);
		addApplicationServiceManager(new LoggingServiceManager());
		//		addApplicationServiceManager(new AvailabilityServiceManager(rsm));
		//		addApplicationServiceManager(new EMTargetInitializer());
	}
}
