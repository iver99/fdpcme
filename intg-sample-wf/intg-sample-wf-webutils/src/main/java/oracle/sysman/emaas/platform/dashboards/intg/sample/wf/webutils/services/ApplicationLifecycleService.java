/*
 * Copyright (C) 2014 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.intg.sample.wf.webutils.services;

import oracle.sysman.emaas.platform.dashboards.intg.sample.wf.webutils.wls.lifecycle.AbstractApplicationLifecycleService;

public class ApplicationLifecycleService extends AbstractApplicationLifecycleService
{

	public ApplicationLifecycleService()
	{
		super(new RegistryServiceManager());
	}

}
