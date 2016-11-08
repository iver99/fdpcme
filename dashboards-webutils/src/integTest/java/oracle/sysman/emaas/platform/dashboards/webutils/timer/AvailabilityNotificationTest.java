/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.timer;

import javax.management.Notification;

import org.testng.Assert;
import org.testng.annotations.Test;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.DBConnectionManager;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.targetmodel.services.GlobalStatus;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.webutils.services.RegistryServiceManager;

/**
 * @author aduan
 */
public class AvailabilityNotificationTest
{
	@SuppressWarnings("unchecked")
	@Test(groups = { "s2" })
	public void testHandleNotification(@Mocked final Notification anyNoti, @Mocked final RegistryServiceManager anyRsm,
			@Mocked final DBConnectionManager anyDcm, @Mocked final RegistryLookupUtil anyLookupUtil)
	{
		AvailabilityNotification an = new AvailabilityNotification(anyRsm);
		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = null;
				anyNoti.getSequenceNumber();
				result = 123456789;
			}
		};
		an.handleNotification(anyNoti, null);

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.FALSE;
			}
		};
		an.handleNotification(anyNoti, null);

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				anyDcm.isDatabaseConnectionAvailable();
				result = true;
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, null);
				result = new Link().withRel("collection/domains").withHref(
						"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
			}
		};
		an.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isDashboardUp());
		Assert.assertTrue(DependencyStatus.getInstance().isDatabaseUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				anyDcm.isDatabaseConnectionAvailable();
				result = false;
			}
		};
		an.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isDashboardUp());
		Assert.assertFalse(DependencyStatus.getInstance().isDatabaseUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				anyDcm.isDatabaseConnectionAvailable();
				result = true;
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, null);
				result = null;
//				anyRsm.markOutOfService((List<InstanceInfo>) any, (List<NonServiceResource>) any, (List<String>) any);
			}
		};
		an.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isDashboardUp());
		Assert.assertTrue(DependencyStatus.getInstance().isDatabaseUp());
		Assert.assertFalse(DependencyStatus.getInstance().isEntityNamingUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				anyDcm.isDatabaseConnectionAvailable();
				result = new Exception();
			}
		};
		an.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isDashboardUp());
		Assert.assertFalse(DependencyStatus.getInstance().isDatabaseUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				anyDcm.isDatabaseConnectionAvailable();
				result = true;
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, null);
				result = new Exception();
//				anyRsm.markOutOfService((List<InstanceInfo>) any, (List<NonServiceResource>) any, (List<String>) any);
			}
		};
		an.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isDashboardUp());
		Assert.assertTrue(DependencyStatus.getInstance().isDatabaseUp());
		Assert.assertFalse(DependencyStatus.getInstance().isEntityNamingUp());
	}
}
