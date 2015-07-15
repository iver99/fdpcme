/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboardscommon.ui.webutils.services;

import oracle.sysman.emaas.platform.dashboardscommon.ui.webutils.wls.lifecycle.AbstractApplicationLifecycleService;

public class DashboardCommonUiLifecycleService extends AbstractApplicationLifecycleService
{
	public DashboardCommonUiLifecycleService()
	{
		RegistryServiceManager rsm = new RegistryServiceManager();
		addApplicationServiceManager(rsm);
		addApplicationServiceManager(new LoggingServiceManager());
		addApplicationServiceManager(new AvailabilityServiceManager(rsm));
	}

}
