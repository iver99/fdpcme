/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.services;

import mockit.Mock;
import mockit.MockUp;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.AbstractApplicationLifecycleService;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class DashboardApiLifecycleServiceTest
{
	@Test(groups = { "s2" })
	public void testDashboardApiLifecycleService()
	{
		new MockUp<AbstractApplicationLifecycleService>() {
			@Mock
			public void $init(ApplicationServiceManager... services)
			{
				System.out.println("Mock constructor called.");
			}

			@Mock
			public void addApplicationServiceManager(ApplicationServiceManager service)
			{
				System.out.println("Mock addApplicationServiceManager called.");
			}
		};

		Assert.assertNotNull(new DashboardApiLifecycleService());
	}
}
