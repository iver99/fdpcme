/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.services;

import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo.Builder;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.NonServiceResource;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.dashboards.ui.targetmodel.services.GlobalStatus;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;
import weblogic.management.timer.Timer;

/**
 * @author aduan
 */
public class AvailabilityServiceManagerTest
{
	private final RegistryServiceManager rsm = new RegistryServiceManager();
	private final AvailabilityServiceManager asm = new AvailabilityServiceManager(rsm);
	private final String DASHBOARD_API_SERVICE_NAME = "Dashboard-API";
	private final String DASHBOARD_API_SERVICE_VERSION = "1.0+";
	private final String DASHBOARD_API_SERVICE_REL = "base";

	private final String SAVED_SEARCH_SERVICE_NAME = "SavedSearch";
	private final String SAVED_SEARCH_SERVICE_VERSION = "1.0+";
	private final String SAVED_SEARCH_SERVICE_REL = "search";

	@Test(groups = { "s1" })
	public void testGetName()
	{
		AssertJUnit.assertEquals(asm.getName(), "Dashboard Service UI Timer Service");
	}

	@SuppressWarnings("unchecked")
	@Test(groups = { "s2" })
	public void testHandleNotification(@Mocked final Notification anyNoti, @Mocked final RegistryServiceManager anyRsm,
			@Mocked final RegistryLookupUtil anyLookupUtil, @Mocked final LookupManager lookupmgr,
			@Mocked final LookupClient rsClient, @Mocked final RegistrationManager anyRegistrationManager,
			@Mocked final InstanceInfo anyInstanceInfo, @Mocked final Builder anyBuilder) throws Exception
	{
		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = null;
				anyNoti.getSequenceNumber();
				result = 123456789;
			}
		};
		asm.handleNotification(anyNoti, null);

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.FALSE;
			}
		};
		asm.handleNotification(anyNoti, null);

		final List<InstanceInfo> instanceInfos = new ArrayList<InstanceInfo>();
		instanceInfos.add(new InstanceInfo());
		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search");
				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
						DASHBOARD_API_SERVICE_REL, null);
				result = new Link().withRel("base").withHref("http://den00zyr.us.oracle.com:7019/emcpdf/api/v1/");
				LookupManager.getInstance().getLookupClient().lookup((InstanceQuery) any);
				result = instanceInfos;
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertTrue(GlobalStatus.isDashboardUIUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = null;
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUIUp());

		//		new Expectations() {
		//			{
		//				anyRsm.isRegistrationComplete();
		//				result = Boolean.TRUE;
		//				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
		//						SAVED_SEARCH_SERVICE_REL, null);
		//				result = new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search");
		//				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
		//						DASHBOARD_API_SERVICE_REL, null);
		//				result = null;
		//				anyRsm.markOutOfService((List<InstanceInfo>) any, null, null);
		//			}
		//		};
		//		asm.handleNotification(anyNoti, null);
		//		Assert.assertFalse(GlobalStatus.isDashboardUIUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search");
				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
						DASHBOARD_API_SERVICE_REL, null);
				result = new Link().withRel("base").withHref("http://den00zyr.us.oracle.com:7019/emcpdf/api/v1/");
				InstanceInfo.Builder.newBuilder().withServiceName(anyString).withVersion(anyString).build();
				result = anyInstanceInfo;
				LookupManager.getInstance().getLookupClient().lookup((InstanceQuery) any);
				result = null;
				anyRsm.markOutOfService((List<InstanceInfo>) any, (List<NonServiceResource>) any, (List<String>) any);
				//				RegistrationManager.getInstance().getRegistrationClient().outOfServiceCausedBy((List<InstanceInfo>) any,
				//						(List<NonServiceResource>) any, (List<String>) any);
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUIUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new Exception();
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUIUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search");
				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
						DASHBOARD_API_SERVICE_REL, null);
				result = new Exception();
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUIUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search");
				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
						DASHBOARD_API_SERVICE_REL, null);
				result = new Link().withRel("base").withHref("http://den00zyr.us.oracle.com:7019/emcpdf/api/v1/");
				InstanceInfo.Builder.newBuilder().withServiceName(anyString).withVersion(anyString).build();
				//				LookupManager.getInstance().getLookupClient().lookup((InstanceQuery) any);
				result = new Exception();
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUIUp());

		new Expectations() {
			{
				anyRsm.isRegistrationComplete();
				result = Boolean.TRUE;
				RegistryLookupUtil.getServiceInternalLink(SAVED_SEARCH_SERVICE_NAME, SAVED_SEARCH_SERVICE_VERSION,
						SAVED_SEARCH_SERVICE_REL, null);
				result = new Link().withRel("search").withHref("http://den00zyr.us.oracle.com:7019/savedsearch/v1/search");
				RegistryLookupUtil.getServiceInternalLink(DASHBOARD_API_SERVICE_NAME, DASHBOARD_API_SERVICE_VERSION,
						DASHBOARD_API_SERVICE_REL, null);
				result = new Link().withRel("base").withHref("http://den00zyr.us.oracle.com:7019/emcpdf/api/v1/");
				InstanceInfo.Builder.newBuilder().withServiceName(anyString).withVersion(anyString).build();
				result = anyInstanceInfo;
				LookupManager.getInstance().getLookupClient().lookup((InstanceQuery) any);
				result = instanceInfos;
				anyRsm.markServiceUp();
				result = new Exception();
			}
		};
		asm.handleNotification(anyNoti, null);
		Assert.assertFalse(GlobalStatus.isDashboardUIUp());
	}

	@Test(groups = { "s2" })
	public void testStartStop(@Mocked final Timer anyTimer) throws Exception
	{
		new Expectations() {
			{
				new Timer();
				times = 1;
				anyTimer.addNotificationListener((NotificationListener) any, null, null);
				times = 1;
				anyTimer.start();
				times = 1;
				anyTimer.stop();
				times = 1;
				anyTimer.removeNotification((Integer) any);
				times = 1;
			}
		};

		asm.postStart(null);
		asm.preStop(null);
	}

	@Test(groups = { "s2" })
	public void testStartStopException(@Mocked final Timer anyTimer) throws Exception
	{
		new Expectations() {
			{
				anyTimer.removeNotification((Integer) any);
				result = new InstanceNotFoundException();
				times = 1;
			}
		};

		asm.preStop(null);
	}
}
