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

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class EMTargetMXBeanTest
{
	private final String mbName = "Test_EMTargetMXBean";
	private final EMTargetMXBean mb = new EMTargetMXBeanImpl(mbName);

	@Test(groups = { "s1" })
	public void testGetEMTargetType() throws Exception
	{
		Assert.assertEquals(mb.getEMTargetType(), EMTargetConstants.M_TARGET_TYPE);
	}

	@Test(groups = { "s1" })
	public void testGetName() throws Exception
	{
		Assert.assertEquals(mb.getName(), mbName);
	}
}
