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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author aduan
 */
public class DataAccessUtil
{
	public static class RestClient
	{
		private Map<String, Object> headers;

		public RestClient()
		{
		}

		public String get(String url, String tenant)
		{
			if (StringUtil.isEmpty(url)) {
				return null;
			}

			ClientConfig cc = new DefaultClientConfig();
			Client client = Client.create(cc);
			char[] authToken = RegistrationManager.getInstance().getAuthorizationToken();
			String auth = String.copyValueOf(authToken);
			if (StringUtil.isEmpty(auth)) {
				LOGGER.warn("Warning: RestClient get an empty auth token when connection to url {}", url);
			}
			else {
				LOGGER.info("RestClient is connecting to get response after getting authorization token from registration manager.");
			}
			Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth)
					.header(HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME, tenant).type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON);
			if (headers != null && !headers.isEmpty()) {
				for (String key : headers.keySet()) {
					if (HttpHeaders.AUTHORIZATION.equals(key) || HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME.equals(key)) {
						continue;
					}
					builder.header(key, headers.get(key));
					LOGGER.info("Setting header ({}, {}) for call to {}", key, headers.get(key), url);
				}
			}
			return builder.get(String.class);
		}

		public void setHeader(String header, Object value)
		{
			if (headers == null) {
				headers = new HashMap<String, Object>();
			}
			headers.put(header, value);
		}
	}

	private static final String HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";
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
			if (!StringUtil.isEmpty(referer)) {
				rc.setHeader("Referer", referer);
			}
			if (!StringUtil.isEmpty(sessionExp)) {
				rc.setHeader("SESSION_EXP", sessionExp);
			}
			String response = rc.get(registrationHref, tenantName);
			cache.put(userTenantKey, response);
			LOGGER.info("Retrieved registration data is: {}", response);
			LOGGER.info("It takes {}ms to retrieve registration data from Dashboard-API", System.currentTimeMillis() - start);
			return response;
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
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
			if (!StringUtil.isEmpty(referer)) {
				rc.setHeader("Referer", referer);
			}
			if (!StringUtil.isEmpty(sessionExp)) {
				rc.setHeader("SESSION_EXP", sessionExp);
			}
			String response = rc.get(userInfoHref, tenantName);
			cache.put(userTenantKey, response);
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
