/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.core.cache.CacheManager;
import oracle.sysman.emaas.platform.dashboards.core.cache.ICacheFetchFactory;
import oracle.sysman.emaas.platform.dashboards.core.cache.Tenant;
import oracle.sysman.emaas.platform.dashboards.core.restclient.AppMappingCollection;
import oracle.sysman.emaas.platform.dashboards.core.restclient.AppMappingEntity;
import oracle.sysman.emaas.platform.dashboards.core.restclient.DomainEntity;
import oracle.sysman.emaas.platform.dashboards.core.restclient.DomainsEntity;
import oracle.sysman.emaas.platform.dashboards.core.util.LogUtil.InteractionLogDirection;

/**
 * @author guobaochen
 */
public class TenantSubscriptionUtil
{
	public static class RestClient
	{
		public RestClient()
		{
		}

		public String get(String url, String tenant)
		{
			if (url == null || "".equals(url)) {
				return null;
			}

			ClientConfig cc = new DefaultClientConfig();
			Client client = Client.create(cc);
			char[] authToken = RegistrationManager.getInstance().getAuthorizationToken();
			String auth = String.copyValueOf(authToken);
			if (StringUtil.isEmpty(auth)) {
				logger.warn("Warning: RestClient get an empty auth token when connection to url {}", url);
			}
			else {
				LogUtil.setInteractionLogThreadContext(tenant, url, InteractionLogDirection.OUT);
				itrLogger.info(
						"RestClient is connecting to get response after getting authorization token from registration manager.");
			}
			Builder builder = client.resource(UriBuilder.fromUri(url).build()).header(HttpHeaders.AUTHORIZATION, auth)
					.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
			return builder.get(String.class);
		}
	}

	private static Boolean IS_TEST_ENV = null;

	private static Object lock = new Object();

	private static Logger logger = LogManager.getLogger(TenantSubscriptionUtil.class);
	private static Logger itrLogger = LogUtil.getInteractionLogger();

	@SuppressWarnings("unchecked")
	public static List<String> getTenantSubscribedServices(final String tenant)
	{
		// for junit test only
		if (Boolean.TRUE.equals(IS_TEST_ENV)) {
			logger.warn(
					"In test environment, the subscribed applications for are tenants are specified to \"APM\" and \"ITAnalytics\"");
			return Arrays.asList(new String[] { "APM", "ITAnalytics" });
		}

		// normal behavior here
		if (tenant == null) {
			logger.warn("This is usually unexpected: now it's trying to retrieve subscribed applications for null tenant");
			return null;
		}

		CacheManager cm = CacheManager.getInstance();
		Tenant cacheTenant = new Tenant(tenant);
		List<String> cachedApps;
		try {
			cachedApps = (List<String>) cm.getCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
					CacheManager.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS);
		}
		catch (Exception e1) {
			logger.error(e1);
			return null;
		}
		if (cachedApps != null) {
			logger.debug("retrieved subscribed apps for tenant {} from cache: "
					+ StringUtil.arrayToCommaDelimitedString(cachedApps.toArray()), tenant);
			return cachedApps;
		}
		Link domainLink = RegistryLookupUtil.getServiceInternalLink("EntityNaming", "1.0+", "collection/domains", null);
		if (domainLink == null || domainLink.getHref() == null || "".equals(domainLink.getHref())) {
			logger.warn(
					"Failed to get entity naming service, or its rel (collection/domains) link is empty. Exists the retrieval of subscribed service for tenant {}",
					tenant);
			return null;
		}
		logger.debug("Checking tenant (" + tenant + ") subscriptions. The entity naming href is " + domainLink.getHref());
		final String domainHref = domainLink.getHref();
		final RestClient rc = new RestClient();
		String domainsResponse = TenantSubscriptionUtil.fetchDomainLinks(tenant, cm, domainHref, rc);
		JsonUtil ju = JsonUtil.buildNormalMapper();
		try {
			DomainsEntity de = ju.fromJson(domainsResponse, DomainsEntity.class);
			if (de == null || de.getItems() == null || de.getItems().size() <= 0) {
				logger.warn(
						"Checking tenant (" + tenant + ") subscriptions: null/empty domains entity or domains item retrieved.");
				return null;
			}
			String tenantAppUrl = null;
			for (DomainEntity domain : de.getItems()) {
				if ("TenantApplicationMapping".equals(domain.getDomainName())) {
					tenantAppUrl = domain.getCanonicalUrl();
					break;
				}
			}
			if (tenantAppUrl == null || "".equals(tenantAppUrl)) {
				logger.warn("Checking tenant (" + tenant + ") subscriptions. 'TenantApplicationMapping' not found");
				return null;
			}
			String appMappingJson = TenantSubscriptionUtil.fetchTenantAppMappingUrl(tenant, cm, rc, tenantAppUrl);
			if (appMappingJson == null || "".equals(appMappingJson)) {
				return null;
			}
			AppMappingCollection amec = ju.fromJson(appMappingJson, AppMappingCollection.class);
			if (amec == null || amec.getItems() == null || amec.getItems().isEmpty()) {
				logger.error("Checking tenant (" + tenant + ") subscriptions. Empty application mapping items are retrieved");
				return null;
			}
			AppMappingEntity ame = null;
			for (AppMappingEntity entity : amec.getItems()) {
				if (entity.getValues() == null) {
					continue;
				}
				for (AppMappingEntity.AppMappingValue amv : entity.getValues()) {
					if (tenant.equals(amv.getOpcTenantId())) {
						ame = entity;
						break;
					}

				}
			}
			if (ame == null || ame.getValues() == null || ame.getValues().isEmpty()) {
				logger.error("Checking tenant (" + tenant
						+ ") subscriptions. Failed to get an application mapping for the specified tenant");
				return null;
			}
			String apps = null;
			for (AppMappingEntity.AppMappingValue amv : ame.getValues()) {
				if (tenant.equals(amv.getOpcTenantId())) {
					apps = amv.getApplicationNames();
					break;
				}
			}
			logger.debug("Checking tenant (" + tenant + ") subscriptions. applications for the tenant are " + apps);
			if (apps == null || "".equals(apps)) {
				return null;
			}
			List<String> origAppsList = Arrays
					.asList(apps.split(ApplicationEditionConverter.APPLICATION_EDITION_ELEMENT_DELIMINATOR));
			cm.putCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE, CacheManager.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS,
					origAppsList);
			return origAppsList;

		}
		catch (IOException e) {
			logger.error(e);
			return null;
		}
	}

	public static boolean isAPMServiceOnly(List<String> services)
	{
		logger.debug("Checking if only APM is subscribed, checked services are {}",
				services == null ? null : services.toString());
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

	public static void setTestEnv()
	{
		synchronized (lock) {
			if (IS_TEST_ENV == null) {
				IS_TEST_ENV = true;
			}
		}
	}

	/**
	 * @param tenant
	 * @param cm
	 * @param domainHref
	 * @param rc
	 * @return
	 */
	private static String fetchDomainLinks(final String tenant, CacheManager cm, final String domainHref, final RestClient rc)
	{
		String domainsResponse = null;
		try {
			domainsResponse = (String) cm.getCacheable(new Tenant(tenant), CacheManager.CACHES_LOOKUP_CACHE, domainHref,
					new ICacheFetchFactory() {
						@Override
						public Object fetchCache(Object key) throws Exception
						{
							return rc.get(domainHref, tenant);
						}
					});
		}
		catch (Exception e1) {
			logger.error(e1);
			return null;
		}

		logger.debug("Checking tenant (" + tenant + ") subscriptions. Domains list response is " + domainsResponse);
		return domainsResponse;
	}

	/**
	 * @param tenant
	 * @param cm
	 * @param rc
	 * @param tenantAppUrl
	 * @return
	 */
	private static String fetchTenantAppMappingUrl(final String tenant, CacheManager cm, final RestClient rc, String tenantAppUrl)
	{
		final String appMappingUrl = tenantAppUrl + "/lookups?opcTenantId=" + tenant;
		logger.debug("Checking tenant (" + tenant + ") subscriptions. tenant application mapping lookup URL is " + appMappingUrl);
		String appMappingJson = null;
		try {
			appMappingJson = (String) cm.getCacheable(new Tenant(tenant), CacheManager.CACHES_LOOKUP_CACHE, appMappingUrl,
					new ICacheFetchFactory() {
						@Override
						public Object fetchCache(Object key) throws Exception
						{
							return rc.get(appMappingUrl, tenant);
						}
					});
		}
		catch (Exception e) {
			logger.error(e);
			return null;
		}

		logger.debug("Checking tenant (" + tenant + ") subscriptions. application lookup response json is " + appMappingJson);
		return appMappingJson;
	}
}
