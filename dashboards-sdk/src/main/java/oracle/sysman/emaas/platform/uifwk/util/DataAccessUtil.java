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
import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;

import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author aduan
 */
public class DataAccessUtil
{

	private static final Logger LOGGER = LogManager.getLogger(DataAccessUtil.class);

	public static String getRegistrationData(String tenantName, String userName, String referer, String sessionExp)
	{
		String userTenant = tenantName + "." + userName;
		long start = System.currentTimeMillis();
		Tenant cacheTenant = new Tenant(tenantName);
		Object userTenantKey = DefaultKeyGenerator.getInstance().generate(cacheTenant, new Keys(userTenant));
		ICacheManager cm = CacheManagers.getInstance().build();
		ICache cache = cm.getCache(CacheConstants.CACHES_REGISTRY_CACHE);
		if (cache != null) {
			try {
				Object obj = cache.get(userTenantKey);
				if (obj instanceof String) {
					String data = (String) obj;
					LOGGER.info("Retrieved registration data from cache for userTenant {}, cached data is {}", userTenant, data);
					return data;
				}
			}
			catch (ExecutionException e) {
				// for cache issue, we'll continue retrieve data and just log a warning message
				LOGGER.warn(e.getMessage(), e);
			}
		}

		try {
			Link configurationsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+",
					"static/dashboards.configurations", null);
			if (configurationsLink == null || StringUtil.isEmpty(configurationsLink.getHref())) {
				LOGGER.warn("Retrieving configurations links for tenant {}: null/empty configurationsLink retrieved from service registry.");
				return null;
			}
			LOGGER.info("Configurations REST API link from dashboard-api href is: " + configurationsLink.getHref());
			String registrationHref = configurationsLink.getHref() + "/registration";
			RestClient rc = new RestClient();
			rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantName);
			rc.setHeader("X-REMOTE-USER", userTenant);
			//EMCPDF-3448, FEB20: 3 admin link dif found in farm jobs
			rc.setHeader("OAM_REMOTE_USER", userTenant);
			if (!StringUtil.isEmpty(referer)) {
				rc.setHeader("Referer", referer);
			}
			if (!StringUtil.isEmpty(sessionExp)) {
				rc.setHeader("SESSION_EXP", sessionExp);
			}
			String response = rc.get(registrationHref, tenantName);
			if(response!=null){
				cache.put(userTenantKey, response);
			}
			LOGGER.info("Retrieved brandingbar data is: {}", response);
			LOGGER.info("It takes {}ms to retrieve brandingbar data from Dashboard-API", System.currentTimeMillis() - start);
			return response;
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}

	public static String getTenantSubscribedServices(String tenant, String user)
	{
		if (tenant == null) {
			return null;
		}

		long startTime = System.currentTimeMillis();
		Tenant cacheTenant = new Tenant(tenant);
		Object tenantKey = DefaultKeyGenerator.getInstance().generate(cacheTenant,
				new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS_UIFWK));
		ICacheManager cm = CacheManagers.getInstance().build();
		ICache cache = cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE);
		if (cache != null) {
			try {
				String data = (String) cache.get(tenantKey);
				if (data != null) {
					LOGGER.info("Retrieved subscribed app information from cache for tenant {}, cached data is {}", tenant, data);
					return data;
				}
			}
			catch (ExecutionException e) {
				// for cache issue, we'll continue retrieve data and just log a warning message
				LOGGER.warn(e.getLocalizedMessage(), e);
			}
		}

		// instead of retrieving subscribed apps from entitynaming, we get that from dashboard api
		Link subAppLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.subscribedapps",
				null);
		if (subAppLink == null || StringUtil.isEmpty(subAppLink.getHref())) {
			LOGGER.warn("Checking tenant (" + tenant
					+ ") subscriptions: null/empty subscribedapp link retrieved from dashboard-api.");
			return null;
		}
		LOGGER.info("Checking tenant (" + tenant + ") subscriptions. Subscribedapp link retrieved from dashboard-api href is "
				+ subAppLink.getHref());
		String subAppHref = subAppLink.getHref();
		RestClient rc = new RestClient();
		rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenant);
		rc.setHeader("X-REMOTE-USER", tenant + "." + user);
		//EMCPDF-3448, FEB20: 3 admin link dif found in farm jobs
		rc.setHeader("OAM_REMOTE_USER", tenant + "." + user);
		String subAppResponse = rc.get(subAppHref, tenant);
		if(subAppResponse!=null){
			cache.put(tenantKey, subAppResponse);
		}
		LOGGER.info("Checking tenant (" + tenant + ") subscriptions. Dashboard-API subscribed app response is " + subAppResponse);
		LOGGER.info("It takes {}ms to retrieve subscribed app data from Dashboard-API", System.currentTimeMillis() - startTime);
		return subAppResponse;
	}

	public static String getUserTenantInfo(String tenantName, String userName, String referer, String sessionExp)
	{
		String userTenant = tenantName + "." + userName;
		LOGGER.info("Retrieved tenantName: " + tenantName + ", userTenant: " + userTenant);
		long start = System.currentTimeMillis();
		Tenant cacheTenant = new Tenant(tenantName);
		Object userTenantKey = DefaultKeyGenerator.getInstance().generate(cacheTenant, new Keys(userTenant));
		ICacheManager cm = CacheManagers.getInstance().build();
		ICache cache = cm.getCache(CacheConstants.CACHES_TENANT_USER_CACHE);
		if (cache != null) {
			try {
				Object obj = cache.get(userTenantKey);
				if (obj instanceof String) {
					String data = (String) obj;
					LOGGER.info("Retrieved user info from cache for userTenant {}, cached data is {}", userTenant, data);
					return data;
				}
			}
			catch (ExecutionException e) {
				// for cache issue, we'll continue retrieve data and just log a warning message
				LOGGER.warn(e.getMessage(), e);
			}
		}

		try {
			Link configurationsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+",
					"static/dashboards.configurations", null);
			if (configurationsLink == null || StringUtil.isEmpty(configurationsLink.getHref())) {
				LOGGER.warn("Retrieving configurations links for tenant {}: null/empty configurationsLink retrieved from service registry.");
				cache.evict(userTenantKey);
				return null;
			}
			LOGGER.info("Configurations REST API link from dashboard-api href is: " + configurationsLink.getHref());
			String userInfoHref = configurationsLink.getHref() + "/userInfo";
			RestClient rc = new RestClient();
			rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantName);
			rc.setHeader("X-REMOTE-USER", userTenant);
			//EMCPDF-3448, FEB20: 3 admin link dif found in farm jobs
			rc.setHeader("OAM_REMOTE_USER", userTenant);
			if (!StringUtil.isEmpty(referer)) {
				rc.setHeader("Referer", referer);
			}
			if (!StringUtil.isEmpty(sessionExp)) {
				rc.setHeader("SESSION_EXP", sessionExp);
			}
			String response = rc.get(userInfoHref, tenantName);
			if(response!=null){
				cache.put(userTenantKey, response);
			}
			LOGGER.info("Retrieved userInfo data is: {}", response);
			LOGGER.info("It takes {}ms to retrieve userInfo data from Dashboard-API", System.currentTimeMillis() - start);
			return response;
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}
}
