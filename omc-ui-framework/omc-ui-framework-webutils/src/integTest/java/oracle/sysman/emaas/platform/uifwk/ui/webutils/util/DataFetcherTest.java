/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.WebResource.Builder;

/**
 * @author aduan
 */
public class DataFetcherTest
{
	@Test(groups = { "s2" })
	public void testGetRegistrationFromCache(@Mocked final CacheManagers cms, @Mocked final ICacheManager cm, @Mocked final ICache cacheUtil) throws ExecutionException {
		new Expectations() {
			{
				CacheManagers.getInstance().build();
				result = cm;
				cm.getCache(anyString);
				result = cacheUtil;
				cacheUtil.get(any);
				result = "{registrationData}";
			}
		};
		String registration = DataFetcher.getRegistrationData("tenant", "tenant.user", "referer", "12345678");
		Assert.assertEquals(registration, "{registrationData}");
	}

	@Test(groups = { "s2" })
	public void testGetRegistrationFromService(@Mocked final RegistryLookupUtil registryUtil,
			@Mocked final RegistrationManager rm, @Mocked final Builder builder)
	{
		new Expectations() {
			{
				RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.configurations", null);
				result = null;
			}
		};
		String registration = DataFetcher.getRegistrationData("tenant", "tenant.user", "referer", "12345678");
		Assert.assertNull(registration);

		new Expectations() {
			{
				RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.configurations", null);
				result = new Link();
				RegistrationManager.getInstance().getAuthorizationToken();
				result = "Basic Auth".toCharArray();
				builder.get(String.class);
				result = "{registrationData}";
			}
		};
		registration = DataFetcher.getRegistrationData("tenant", "tenant.user", "referer", "12345678");
		Assert.assertNull(registration);

		final Link lk = new Link();
		lk.withHref("http://hostname:7019/emcpdf/api/v1/configurations/registration");
		new Expectations() {
			{
				RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.configurations", null);
				result = lk;
			}
		};
		registration = DataFetcher.getRegistrationData("tenant", "tenant.user", "referer", "12345678");
		Assert.assertEquals(registration, "{registrationData}");
	}
}
