/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.services;

import oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.lifecycle.AbstractApplicationLifecycleService;

public class OmcUiFrameworkLifecycleService extends AbstractApplicationLifecycleService
{
	public OmcUiFrameworkLifecycleService()
	{
		RegistryServiceManager rsm = new RegistryServiceManager();
		addApplicationServiceManager(rsm);
		addApplicationServiceManager(new LoggingServiceManager());
		addApplicationServiceManager(new AvailabilityServiceManager(rsm));
	}

}
