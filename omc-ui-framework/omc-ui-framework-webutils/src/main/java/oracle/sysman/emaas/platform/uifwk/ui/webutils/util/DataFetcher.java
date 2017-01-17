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
import oracle.sysman.emaas.platform.uifwk.ui.webutils.util.LogUtil.InteractionLogDirection;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author aduan
 */
public class DataFetcher
{
	public static class RestClient
	{
		private static final String HTTP_HEADER_X_USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";

		private Map<String, Object> headers;

		public RestClient()
		{
		}

		public String get(String url, String tenant)
		{
			if (StringUtils.isEmpty(url)) {
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
				LogUtil.setInteractionLogThreadContext(tenant, url, InteractionLogDirection.OUT);
				ITRLOGGER
				.info("RestClient is connecting to get response after getting authorization token from registration manager.");
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
			cache.put(userTenant, response);
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
