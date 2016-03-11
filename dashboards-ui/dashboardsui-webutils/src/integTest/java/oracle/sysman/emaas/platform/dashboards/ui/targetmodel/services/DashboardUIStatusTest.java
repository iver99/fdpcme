/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.targetmodel.services;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class DashboardUIStatusTest
{
	private final DashboardUIStatus ds = new DashboardUIStatus();

	@Test(groups = { "s1" })
	public void testGetStatus()
	{
		GlobalStatus.setDashboardUIUpStatus();
		Assert.assertTrue(GlobalStatus.isDashboardUIUp());
		Assert.assertEquals(ds.getStatus(), GlobalStatus.STATUS_UP);
		GlobalStatus.setDashboardUIDownStatus();
		Assert.assertEquals(ds.getStatus(), GlobalStatus.STATUS_DOWN);
	}

	@Test(groups = { "s1" })
	public void testGetStatusMsg()
	{
		GlobalStatus.setDashboardUIUpStatus();
		Assert.assertEquals(ds.getStatusMsg(), "Dashboard-UI is up and running.");
		GlobalStatus.setDashboardUIDownStatus();
		Assert.assertEquals(ds.getStatusMsg(), "Dashboard-UI is being stopped.");
	}
}
