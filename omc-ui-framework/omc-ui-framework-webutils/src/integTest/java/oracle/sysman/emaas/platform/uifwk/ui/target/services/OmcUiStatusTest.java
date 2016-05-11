/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.target.services;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class OmcUiStatusTest
{
	private final OmcUiStatus status = new OmcUiStatus();

	@Test(groups = { "s1" })
	public void testGetStatus()
	{
		GlobalStatus.setOmcUiUpStatus();
		Assert.assertTrue(GlobalStatus.isOmcUiUp());
		Assert.assertEquals(status.getStatus(), GlobalStatus.STATUS_UP);
		GlobalStatus.setOmcUiDownStatus();
		Assert.assertEquals(status.getStatus(), GlobalStatus.STATUS_DOWN);
	}

	@Test(groups = { "s1" })
	public void testGetStatusMsg()
	{
		GlobalStatus.setOmcUiUpStatus();
		Assert.assertEquals(status.getStatusMsg(), "OMC-UI is up and running.");
		GlobalStatus.setOmcUiDownStatus();
		Assert.assertEquals(status.getStatusMsg(), "OMC-UI is being stopped.");
	}
}
