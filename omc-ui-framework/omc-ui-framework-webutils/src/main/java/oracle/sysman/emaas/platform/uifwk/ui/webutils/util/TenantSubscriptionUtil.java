/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * @author guobaochen
 */
public class TenantSubscriptionUtil
{
	private static final String HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";

	private static Logger LOGGER = LogManager.getLogger(TenantSubscriptionUtil.class);

	private static Logger itrLogger = LogUtil.getInteractionLogger();

	public static String getTenantSubscribedServices(String tenant, String user)
	{
		if (tenant == null) {
			return null;
		}

		long startTime = System.currentTimeMillis();
		Tenant cacheTenant = new Tenant(tenant);
		Object tenantKey = DefaultKeyGenerator.getInstance().generate(cacheTenant, new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS_UIFWK));
		ICacheManager cm = CacheManagers.getInstance().build();
		ICache cache = cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE);
		if (cache != null) {
			try {
				String data = (String)cache.get(tenantKey);
				if (data != null) {
					LOGGER.info("Retrieved subscribed app information from cache for userTenant {}, cached data is {}", tenant, data);
					return data;
				}
			} catch (ExecutionException e) {
				// for cache issue, we'll continue retrieve data and just log a warning message
				LOGGER.warn(e.getLocalizedMessage(), e);
			}
		}

		// instead of retrieving subscribed apps from entitynaming, we get that from dashboard api
		Link subAppLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.subscribedapps",
				null);
		if (subAppLink == null || StringUtils.isEmpty(subAppLink.getHref())) {
			LOGGER.warn("Checking tenant (" + tenant
					+ ") subscriptions: null/empty subscribedapp link retrieved from dashboard-api.");
			return null;
		}
		LOGGER.info("Checking tenant (" + tenant + ") subscriptions. Subscribedapp link retrieved from dashboard-api href is "
				+ subAppLink.getHref());
		String subAppHref = subAppLink.getHref();
		RestClient rc = new RestClient();
		rc.setHeader(HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME, tenant);
		rc.setHeader("X-REMOTE-USER", tenant + "." + user);
		String subAppResponse = rc.get(subAppHref, tenant);
		if(subAppResponse!=null){
			cache.put(tenantKey, subAppResponse);
		}
		LOGGER.info("Checking tenant (" + tenant + ") subscriptions. Dashboard-API subscribed app response is " + subAppResponse);
		LOGGER.info("It takes {}ms to retrieve subscribed app data from Dashboard-API", System.currentTimeMillis() - startTime);
		return subAppResponse;
	}

	private TenantSubscriptionUtil()
	{
	}

}
