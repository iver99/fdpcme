/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import java.io.IOException;
import java.util.*;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.json.AppMappingCollection;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.json.AppMappingEntity;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.json.DomainEntity;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.json.DomainsEntity;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.LogUtil.InteractionLogDirection;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.subscription.SubscribedAppCacheUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.subscription.SubscribedApps;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author guobaochen
 */
public class TenantSubscriptionUtil
{
	private TenantSubscriptionUtil() {
	  }

	public static class RestClient
	{
		private Map<String, Object> headers;

		public RestClient()
		{
		}

		public void setHeader(String header, Object value) {
			if (headers == null) {
				headers = new HashMap<String, Object>();
			}
			headers.put(header, value);
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
				itrLogger
				.info("RestClient is connecting to get response after getting authorization token from registration manager.");
			}
			Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth)
					.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
			if (headers != null && !headers.isEmpty()) {
				for (String key : headers.keySet()) {
					builder.header(key, headers.get(key));
					LOGGER.info("Setting header ({}, {}) for call to {}", key, headers.get(key), url);
				}
			}
			return builder.get(String.class);
		}
	}

	private static Logger LOGGER = LogManager.getLogger(TenantSubscriptionUtil.class);
	private static Logger itrLogger = LogUtil.getInteractionLogger();

	public static List<String> getTenantSubscribedServices(String tenant, String user) {
		if (tenant == null) {
			return Collections.emptyList();
		}

		long startTime = System.currentTimeMillis();
		SubscribedAppCacheUtil cache = SubscribedAppCacheUtil.getInstance();
		List<String> apps = cache.get(tenant);
		if (apps != null) {
			LOGGER.info("Retrieved subscribed app information from cache");
			return apps;
		}

		// instead of retrieving subscribed apps from entitynaming, we get that from dashboard api
		Link subAppLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.subscribedapps", null);
		if (subAppLink == null || StringUtils.isEmpty(subAppLink.getHref())) {
			LOGGER.warn("Checking tenant (" + tenant
					+ ") subscriptions: null/empty subscribedapp link retrieved from dashboard-api.");
			return Collections.emptyList();
		}
		LOGGER.info("Checking tenant (" + tenant + ") subscriptions. Subscribedapp link retrieved from dashboard-api href is " + subAppLink.getHref());
		String subAppHref = subAppLink.getHref();
		RestClient rc = new RestClient();
		String subAppResponse = rc.get(subAppHref, tenant);
		rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenant);
		rc.setHeader("X-REMOTE-USER", tenant + "." + user);
		LOGGER.info("Checking tenant (" + tenant + ") subscriptions. Dashboard-API subscribed app response is " + subAppResponse);
		JsonUtil ju = JsonUtil.buildNormalMapper();
		try {
			SubscribedApps sa = ju.fromJson(subAppResponse, SubscribedApps.class);
			if (sa == null || sa.getApplications() == null || sa.getApplications().length <= 0) {
				LOGGER.info("Checking tenant (" + tenant + ") subscriptions. Dashboard-API subscribed app application is null or empty");
				cache.remove(tenant);
				return Collections.emptyList();
			}
			apps = Arrays.asList(sa.getApplications());
			cache.put(tenant, apps);
			LOGGER.info("Getting subscribed app from dashboard-api: {} ms", System.currentTimeMillis() - startTime);
			return apps;
		}
		catch (IOException e) {
			LOGGER.error(e);
			return Collections.emptyList();
		}
	}

	public static boolean isAPMServiceOnly(List<String> services)
	{
		if (services == null || services.size() != 1) {
			return false;
		}
		String svc = services.get(0);
		if (svc == null) {
			return false;
		}
		if (svc.equals(ApplicationEditionConverter.ApplicationOPCName.APM.toString())) {
			return true;
		}
		return false;
	}

	public static boolean isMonitoringServiceOnly(List<String> services)
	{
		if (services == null || services.size() != 1) {
			return false;
		}
		String svc = services.get(0);
		if (svc == null) {
			return false;
		}
		//TODO update to use ApplicationEditionConverter.ApplicationOPCName once it's updated in tenant sdk
		if (("Monitoring").equals(svc)) {
			return true;
		}
		return false;
	}

}
