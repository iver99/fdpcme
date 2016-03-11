/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.targetmodel.services;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class DashboardStatusTest
{
	private final DashboardStatus ds = new DashboardStatus();

	@Test(groups = { "s1" })
	public void testGetStatus()
	{
		GlobalStatus.setDashboardUpStatus();
		Assert.assertTrue(GlobalStatus.isDashboardUp());
		Assert.assertEquals(ds.getStatus(), GlobalStatus.STATUS_UP);
		GlobalStatus.setDashboardDownStatus();
		Assert.assertEquals(ds.getStatus(), GlobalStatus.STATUS_DOWN);
	}

	@Test(groups = { "s1" })
	public void testGetStatusMsg()
	{
		GlobalStatus.setDashboardUpStatus();
		Assert.assertEquals(ds.getStatusMsg(), "Dashboard-API is up and running.");
		GlobalStatus.setDashboardDownStatus();
		Assert.assertEquals(ds.getStatusMsg(), "Dashboard-API is being stopped.");
	}
}
