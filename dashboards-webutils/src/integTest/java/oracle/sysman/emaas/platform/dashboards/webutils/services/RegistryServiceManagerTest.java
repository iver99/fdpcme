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

import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InfoManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo.InstanceStatus;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class RegistryServiceManagerTest
{
	RegistryServiceManager rsm = new RegistryServiceManager();
        private final static String VERSION="1.0+";
	@Test(groups = { "s1" })
	public void testGetName()
	{
		Assert.assertEquals(rsm.getName(), "Service Registry Service");
	}

	@Test(groups = { "s1" })
	public void testGetSetRegistrationComplete()
	{
		rsm.setRegistrationComplete(Boolean.TRUE);
		Assert.assertTrue(rsm.isRegistrationComplete());
		rsm.setRegistrationComplete(Boolean.FALSE);
		Assert.assertFalse(rsm.isRegistrationComplete());
	}

	@Test(groups = { "s2" })
	public void testMarkServiceStatus(@Mocked final RegistrationManager anyRm, @Mocked final RegistrationClient anyClient)
	{
		new Expectations() {
			{
				anyClient.updateStatus((InstanceStatus) any);
				times = 1;
			}
		};
		rsm.markOutOfService();

		new Expectations() {
			{
				anyClient.updateStatus((InstanceStatus) any);
				times = 1;
			}
		};
		rsm.markServiceUp();
	}

	@Test(groups = { "s2" })
	public void testStartStop(@Mocked final RegistrationManager anyRm, @Mocked final RegistrationClient anyClient,
			@Mocked final InitialContext anyContext, @Mocked final MBeanServer anyMb, @Mocked final PropertyReader anyReader,
			@Mocked final RegistryLookupUtil anyLookupUtil, @Mocked final InfoManager anyIm,
			@Mocked final InstanceInfo anyInstInfo)
	{
		try {
			final Properties serviceProps = new Properties();
			serviceProps.put("serviceUrls", "http://den00zyr.us.oracle.com:7004/registry/servicemanager/registry/v1");
			serviceProps.put("registryUrls", "http://den00zyr.us.oracle.com:7004/registry/servicemanager/registry/v1");
			serviceProps.put("serviceName", "Dashboard-API");
			serviceProps.put("version", VERSION);
			new Expectations() {
				{
					anyContext.lookup(anyString);
					result = anyMb;
					anyMb.invoke((ObjectName) any, "getURL", (Object[]) any, (String[]) any);
					result = "http://domain.hostname:port";
					PropertyReader.loadProperty(anyString);
					result = serviceProps;
					RegistryLookupUtil.getServiceInternalLink("RegistryService", VERSION, "collection/instances", anyString);
					result = new Link().withRel("collection/instances").withHref(
							"http://den00zyr.us.oracle.com:7004/registry/servicemanager/registry/v1");
				}
			};
			rsm.postStart(null);
			Assert.assertTrue(rsm.isRegistrationComplete());
			rsm.preStop(null);

			new Expectations() {
				{
					RegistryLookupUtil.getServiceInternalLink("RegistryService", VERSION, "collection/instances", anyString);
					result = null;
				}
			};
			rsm.postStart(null);
			Assert.assertFalse(rsm.isRegistrationComplete());

			new Expectations() {
				{
					RegistryLookupUtil.getServiceInternalLink("RegistryService", VERSION, "collection/instances", anyString);
					result = new Link().withRel("collection/instances").withHref(
							"http://den00zyr.us.oracle.com:7004/registry/servicemanager/registry/v1");
					RegistrationManager.getInstance().getRegistrationClient().register();
					result = new Exception();
				}
			};
			rsm.postStart(null);
			Assert.assertFalse(rsm.isRegistrationComplete());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
