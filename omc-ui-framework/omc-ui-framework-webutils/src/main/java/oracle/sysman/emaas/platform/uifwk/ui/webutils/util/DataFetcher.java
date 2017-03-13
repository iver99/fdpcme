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


/**
 * @author aduan
 */
public class DataFetcher
{
	private static final Logger LOGGER = LogManager.getLogger(DataFetcher.class);

	private static final Logger ITRLOGGER = LogUtil.getInteractionLogger();

	public static String getRegistrationData(String tenantIdParam, String userTenant, String referer, String sessionExp)
	{
		try {
			long start = System.currentTimeMillis();
			Tenant cacheTenant = new Tenant(tenantIdParam);
			Object userTenantKey = DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(userTenant));
			ICacheManager cm = CacheManagers.getInstance().build();
			ICache cache = cm.getCache(CacheConstants.CACHES_REGISTRY_CACHE);
			if (cache != null) {
				try {
					Object obj = cache.get(userTenantKey);
					if (obj instanceof String) {
						String data = (String)obj;
						LOGGER.info("Retrieved registration data from cache for userTenant {}, cached data is {}", userTenant, data);
						return data;
					}
				} catch (ExecutionException e) {
					// for cache issue, we'll continue retrieve data and just log a warning message
					LOGGER.warn(e.getLocalizedMessage(), e);
				}
			}

			Link configurationsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+",
					"static/dashboards.configurations", null);
			if (configurationsLink == null || StringUtils.isEmpty(configurationsLink.getHref())) {
				LOGGER.warn("Retrieving configurations links for tenant {}: null/empty configurationsLink retrieved from service registry.");
				return null;
			}
			LOGGER.info("Configurations REST API link from dashboard-api href is: " + configurationsLink.getHref());
			String registrationHref = configurationsLink.getHref() + "/registration";
			RestClient rc = new RestClient();
			rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantIdParam);
			rc.setHeader("X-REMOTE-USER", userTenant);
			rc.setHeader("OAM_REMOTE_USER", userTenant);
			if (!StringUtil.isEmpty(referer)) {
				rc.setHeader("Referer", referer);
			}
			if (!StringUtil.isEmpty(sessionExp)) {
				rc.setHeader("SESSION_EXP", sessionExp);
			}
			String response = rc.get(registrationHref, tenantIdParam);
			cache.put(userTenantKey, response);
			LOGGER.info("Retrieved registration data is: {}", response);
			LOGGER.info("It takes {}ms to retrieve registration data from Dashboard-API", System.currentTimeMillis() - start);
			return response;
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return null;
		}
	}
}
