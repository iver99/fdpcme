/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import javax.ws.rs.core.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
@Test(groups = { "s1" })
public class OMCStatusAPITest
{
	private final OMCStatusAPI omcStatusApi = new OMCStatusAPI();

	@Test
	public void testGetOmcStatus()
	{
		Response omcStatus1 = omcStatusApi.getOmcStatus("tenantId", "tenantId.user", "referer");
		Assert.assertNotNull(omcStatus1);
		Response omcStatus2 = omcStatusApi.getOmcStatus("tenantId", null, "referer");
		Assert.assertNotNull(omcStatus2);
	}
}
