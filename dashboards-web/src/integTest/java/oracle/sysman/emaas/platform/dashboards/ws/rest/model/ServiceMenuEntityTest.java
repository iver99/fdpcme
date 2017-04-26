/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
@Test(groups = { "s1" })
public class ServiceMenuEntityTest
{
	@Test
	public void testServiceMenuEntity()
	{
		ServiceMenuEntity sme = new ServiceMenuEntity();
		sme.setAppId("TestAppId");
		sme.setServiceName("TestServiceName");
		sme.setVersion("1.0");
		sme.setMetaDataHref("http://host:port/sample/testServiceMenu.json");
		Assert.assertEquals(sme.getAppId(), "TestAppId");
		Assert.assertEquals(sme.getServiceName(), "TestServiceName");
		Assert.assertEquals(sme.getVersion(), "1.0");
		Assert.assertEquals(sme.getMetaDataHref(), "http://host:port/sample/testServiceMenu.json");
	}
}
