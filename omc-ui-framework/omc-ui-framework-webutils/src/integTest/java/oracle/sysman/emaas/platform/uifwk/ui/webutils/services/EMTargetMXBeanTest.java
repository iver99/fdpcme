/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.services;

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
	public void testGetEMTargetType() 
	{
		try {
			Assert.assertEquals(mb.getEMTargetType(), EMTargetConstants.M_TARGET_TYPE);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(groups = { "s1" })
	public void testGetName() 
	{
		try {
			Assert.assertEquals(mb.getName(), mbName);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
