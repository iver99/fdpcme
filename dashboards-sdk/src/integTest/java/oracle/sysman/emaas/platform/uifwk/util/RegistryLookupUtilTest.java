/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo.Builder;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.uifwk.util.RegistryLookupUtil.VersionedLink;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class RegistryLookupUtilTest
{
	@Test(groups = { "s2" })
	public void testGetServiceInternalLink(@Mocked final Builder anyBuilder, @Mocked final InstanceInfo anyInstanceInfo,
			@Mocked final LookupManager anyLockupManager, @Mocked final LookupClient anyClient,
			@Mocked final InstanceQuery anyInstanceQuery) throws Exception
	{
		final String serviceName = "ApmUI";
		final String version = "0.1";
		new Expectations() {
			{
				InstanceInfo.Builder.newBuilder().withServiceName(withEqual(serviceName)).withVersion(withEqual(version)).build();
				result = anyInstanceInfo;

				new InstanceQuery((InstanceInfo) any);
				LookupManager.getInstance();
				result = anyLockupManager;
				anyLockupManager.getLookupClient();
				result = anyClient;
				anyClient.lookup((InstanceQuery) any);
				result = new Delegate<List<InstanceInfo>>() {
					@SuppressWarnings("unused")
					List<InstanceInfo> lookup(InstanceQuery query)
					{
						List<InstanceInfo> list = new ArrayList<InstanceInfo>();
						for (int i = 0; i < 3; i++) {
							list.add(anyInstanceInfo);
						}
						return list;
					}
				};
				VersionedLink lkAPM = new VersionedLink();
				lkAPM.withHref("http://den00hvb.us.oracle.com:7028/emsaasui/apmUi/index.html");
				lkAPM.withRel("home");
				lkAPM.setAuthToken("auth");
				VersionedLink lkITA = new VersionedLink();
				lkITA.withHref("http://den00hvb.us.oracle.com:7019/emsaasui/emcitas/worksheet/html/displaying/worksheet-list.html");
				lkITA.withRel("home");
				lkITA.setAuthToken("auth");
				VersionedLink lkLA = new VersionedLink();
				lkLA.withHref("http://den00yse.us.oracle.com:7004/emsaasui/emlacore/resources/");
				lkLA.withRel("loganService");
				lkLA.setAuthToken("auth");
				anyInstanceInfo.getLinksWithProtocol(anyString, anyString);
				returns(Arrays.asList(lkAPM), Arrays.asList(lkITA), Arrays.asList(lkLA));
			}
		};
		Link lk = RegistryLookupUtil.getServiceInternalLink(serviceName, version, "home", null);
		Assert.assertEquals(lk.getHref(), "http://den00hvb.us.oracle.com:7028/emsaasui/apmUi/index.html");
	}
}
