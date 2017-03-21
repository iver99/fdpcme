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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.net.SocketTimeoutException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.core.restclient.AppMappingCollection;
import oracle.sysman.emaas.platform.dashboards.core.restclient.AppMappingEntity;
import oracle.sysman.emaas.platform.dashboards.core.restclient.DomainEntity;
import oracle.sysman.emaas.platform.dashboards.core.restclient.DomainsEntity;
import oracle.sysman.emaas.platform.dashboards.core.util.LogUtil.InteractionLogDirection;
import oracle.sysman.emaas.platform.dashboards.core.util.lookup.RetryableLookupClient;
import oracle.sysman.emaas.platform.dashboards.core.util.lookup.RetryableLookupClient.RetryableLookupException;
import oracle.sysman.emaas.platform.dashboards.core.util.lookup.RetryableLookupClient.RetryableRunner;

import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * @author guobaochen
 */
public class TenantSubscriptionUtil
{
	private static Boolean IS_TEST_ENV = null;

	private static Object lock = new Object();

	private static final Logger LOGGER = LogManager.getLogger(TenantSubscriptionUtil.class);

	private static Logger itrLogger = LogUtil.getInteractionLogger();

	public static String getTenantSubscribedServicesString(final String tenant) {
		List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenant);
		if (apps == null || apps.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{\"applications\":[");
		for (int i = 0; i < apps.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("\"").append(apps.get(i)).append("\"");
		}
		sb.append("]}");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static List<String> getTenantSubscribedServices(final String tenant)
	{
		// for junit test only
		if (Boolean.TRUE.equals(IS_TEST_ENV)) {
			LOGGER.warn("In test environment, the subscribed applications for are tenants are specified to \"APM\" and \"ITAnalytics\"");
			return Arrays.asList(new String[] { "APM", "ITAnalytics" });
		}

		// normal behavior here
		if (tenant == null) {
			LOGGER.warn("This is usually unexpected: now it's trying to retrieve subscribed applications for null tenant");
			return Collections.emptyList();
		}
		final ICacheManager cm= CacheManagers.getInstance().build();
		final Tenant cacheTenant = new Tenant(tenant);
		List<String> cachedApps;
		try {
			cachedApps = (List<String>) cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).get(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
		}
		catch (Exception e) {
			LOGGER.error("context", e);
			return Collections.emptyList();
		}
		if (cachedApps != null) {
			LOGGER.info(
                    "retrieved subscribed apps for tenant {} from cache: "
                            + StringUtil.arrayToCommaDelimitedString(cachedApps.toArray()), tenant);
			return cachedApps;
		}

        List<String> apps = new RetryableLookupClient<List<String>>().connectAndDoWithRetry("EntityNaming", "1.0+", "collection/domains", false, null, new RetryableRunner<List<String>>() {
            public List<String> runWithLink(Link domainLink) throws Exception {
                if (domainLink == null || domainLink.getHref() == null || "".equals(domainLink.getHref())) {
                    LOGGER.warn(
                            "Failed to get entity naming service, or its rel (collection/domains) link is empty. Exists the retrieval of subscribed service for tenant {}",
                            tenant);
                    cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
                    return Collections.emptyList();
                }
                LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. The entity naming href is " + domainLink.getHref());
                String domainHref = domainLink.getHref();
                RestClient rc = new RestClient();
                String domainsResponse = null;
                try {
					rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME",tenant);
					rc.setHeader("X-OMC-SERVICE-TRACE", "Dashboard-API");
					domainsResponse = rc.getWithException(domainHref, tenant);
                } catch (UniformInterfaceException e) {
                    if (e.getResponse() != null && (e.getResponse().getStatus() == 404 || e.getResponse().getStatus() == 503)) {
                        LOGGER.error("Got status code {} when getting tenant app mapping domain", e.getResponse().getStatus());
                        LOGGER.error(e);
                        throw new RetryableLookupException(e);
                    }
                    LOGGER.error(e);
                    throw e;
                } catch (ClientHandlerException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof SocketTimeoutException)
                    {
                        // don't want to retry if got Read Timeout.
                        LOGGER.error(e);
                        throw e;
                    }
                    LOGGER.error(e);
                    throw new RetryableLookupException(e);
                }
                LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. Domains list response is " + domainsResponse);
                JsonUtil ju = JsonUtil.buildNormalMapper();
                try {
                    DomainsEntity de = ju.fromJson(domainsResponse, DomainsEntity.class);
                    if (de == null || de.getItems() == null || de.getItems().isEmpty()) {
                        LOGGER.warn("Checking tenant (" + tenant
                                + ") subscriptions: null/empty domains entity or domains item retrieved.");
                        cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
                        return Collections.emptyList();
                    }
                    String tenantAppUrl = null;
                    for (DomainEntity domain : de.getItems()) {
                        if ("TenantApplicationMapping".equals(domain.getDomainName())) {
                            tenantAppUrl = domain.getCanonicalUrl();
                            break;
                        }
                    }
                    if (tenantAppUrl == null || "".equals(tenantAppUrl)) {
                        LOGGER.warn("Checking tenant (" + tenant + ") subscriptions. 'TenantApplicationMapping' not found");
                        cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
                        return Collections.emptyList();
                    }
                    String appMappingUrl = tenantAppUrl + "/lookups?opcTenantId=" + tenant;
                    LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. tenant application mapping lookup URL is "
                            + appMappingUrl);
                    String appMappingJson = rc.get(appMappingUrl, tenant);
                    LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. application lookup response json is " + appMappingJson);
                    if (appMappingJson == null || "".equals(appMappingJson)) {
                        cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
                        return Collections.emptyList();
                    }
                    AppMappingCollection amec = ju.fromJson(appMappingJson, AppMappingCollection.class);
                    if (amec == null || amec.getItems() == null || amec.getItems().isEmpty()) {
                        LOGGER.error("Checking tenant (" + tenant + ") subscriptions. Empty application mapping items are retrieved");
                        cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
                        return Collections.emptyList();
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
                        LOGGER.error("Checking tenant (" + tenant
                                + ") subscriptions. Failed to get an application mapping for the specified tenant");
                        cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
                        return Collections.emptyList();
                    }
                    String apps = null;
                    for (AppMappingEntity.AppMappingValue amv : ame.getValues()) {
                        if (tenant.equals(amv.getOpcTenantId())) {
                            apps = amv.getApplicationNames();
                            break;
                        }
                    }
                    LOGGER.debug("Checking tenant (" + tenant + ") subscriptions. applications for the tenant are " + apps);
                    if (apps == null || "".equals(apps)) {
                        cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
                        return Collections.emptyList();
                    }
                    List<String> origAppsList = Arrays.asList(apps
                            .split(ApplicationEditionConverter.APPLICATION_EDITION_ELEMENT_DELIMINATOR));
                    cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).put(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)),origAppsList);

                    return origAppsList;

                }
                catch (IOException e) {
                    LOGGER.error(e);
                    return Collections.emptyList();
                }
            }
        });
        if (apps == null) {
            apps = Collections.emptyList();
            LOGGER.warn("Retrieved null list of subscribed apps for tenant {}", tenant);
        }
        return apps;
	}

	public static boolean isAPMServiceOnly(List<String> services)
	{
		LOGGER.debug("Checking if only APM is subscribed, checked services are {}", services == null ? null : services.toString());
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

	private TenantSubscriptionUtil()
	{
	}
}
