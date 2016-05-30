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

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.DBConnectionManager;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.targetmodel.services.GlobalStatus;
import oracle.sysman.emaas.platform.dashboards.webutils.services.RegistryServiceManager;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class AvailabilityNotificationTest
{
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

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				anyDcm.isDatabaseConnectionAvailable();
				result = false;
			}
		};
		an.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				anyDcm.isDatabaseConnectionAvailable();
				result = true;
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, null);
				result = null;
			}
		};
		an.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				anyDcm.isDatabaseConnectionAvailable();
				result = new Exception();
			}
		};
		an.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				anyDcm.isDatabaseConnectionAvailable();
				result = true;
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, null);
				result = new Exception();
			}
		};
		an.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUp());
	}
}
