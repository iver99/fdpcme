/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.services;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.AbstractApplicationLifecycleService;

public class DashboardUiLifecycleService extends AbstractApplicationLifecycleService
{

	public DashboardUiLifecycleService()
	{
		super(new RegistryServiceManager());
	}

}
