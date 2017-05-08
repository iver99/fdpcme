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
import java.util.*;
import java.net.SocketTimeoutException;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import oracle.sysman.emaas.platform.dashboards.core.model.subscription2.*;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.core.util.lookup.RetryableLookupClient;
import oracle.sysman.emaas.platform.dashboards.core.util.lookup.RetryableLookupClient.RetryableLookupException;
import oracle.sysman.emaas.platform.dashboards.core.util.lookup.RetryableLookupClient.RetryableRunner;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.CacheInconsistencyException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.core.restclient.AppMappingCollection;
import oracle.sysman.emaas.platform.dashboards.core.restclient.AppMappingEntity;
import oracle.sysman.emaas.platform.dashboards.core.restclient.DomainEntity;
import oracle.sysman.emaas.platform.dashboards.core.restclient.DomainsEntity;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil.VersionedLink;
import oracle.sysman.emaas.platform.dashboards.core.util.lookup.RetryableLookupClient;
import oracle.sysman.emaas.platform.dashboards.core.util.lookup.RetryableLookupClient.RetryableLookupException;
import oracle.sysman.emaas.platform.dashboards.core.util.lookup.RetryableLookupClient.RetryableRunner;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

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
		List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenant,new TenantSubscriptionInfo());
		if (apps == null) {
			apps = Collections.<String>emptyList();
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
	public static List<String> getTenantSubscribedServices(final String tenant, final TenantSubscriptionInfo tenantSubscriptionInfo)
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
		final long start = System.currentTimeMillis();
		final ICacheManager cm= CacheManagers.getInstance().build();
		final Tenant cacheTenant = new Tenant(tenant);
        final Object cacheKey = DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS));
        CachedTenantSubcriptionInfo cachedTenantSubcriptionInfo = null;
        List<String> cachedApps;
		try {
            cachedTenantSubcriptionInfo = (CachedTenantSubcriptionInfo) cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).get(cacheKey);
            cachedApps = cachedTenantSubcriptionInfo.getSubscribedAppsList();
            TenantSubscriptionInfo tenantSubscriptionInfo1 =cachedTenantSubcriptionInfo.getTenantSubscriptionInfo();
            LOGGER.info("retrieved tenantSubscriptionInfo for tenant {} from cache,data is {}",tenant,tenantSubscriptionInfo1);
            LOGGER.info("retrieved subscribed apps for tenant {} from cache,data is {}",tenant,cachedApps);
            if(cachedApps !=null ){
                copyTenantSubscriptionInfo(tenantSubscriptionInfo1, tenantSubscriptionInfo);
                return cachedApps;
            }
        }catch (Exception e) {
            LOGGER.error("context", e);
            return Collections.emptyList();
        }

        LOGGER.info("Retrieving subscribed apps from /serviceRequest for tenant {}",tenant);
        List<String> apps = new RetryableLookupClient<List<String>>().connectAndDoWithRetry("TenantService", "1.0+", "collection/tenants", false, null, new RetryableRunner<List<String>>() {
            public List<String> runWithLink(VersionedLink lookupLink) throws Exception {
                if (lookupLink == null || lookupLink.getHref() == null || "".equals(lookupLink.getHref())) {
                    LOGGER.warn(
                            "Failed to get entity naming service, or its rel (collection/lookups) link is empty. Exists the retrieval of subscribed service for tenant {}",
                            tenant);
//                    cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(cacheKey);
                    return Collections.emptyList();
                }

                LOGGER.info("Checking tenant (" + tenant + ") subscriptions. The serviceRequest lookups href is " + lookupLink.getHref());
                String queryHref = lookupLink.getHref() + "/" + tenant + "/serviceRequest";
                LOGGER.info("query new serviceRequest url is {}",queryHref);
                RestClient rc = new RestClient();
                long subappQueryStart = System.currentTimeMillis();
                String appsResponse = null;
                String user=UserContext.getCurrentUser();
                LOGGER.info("current user is {}",user);
                try {
					rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME",tenant);
                    appsResponse = rc.getWithException(queryHref, tenant, lookupLink.getAuthToken());
                } catch (UniformInterfaceException e) {
                    if (e.getResponse() != null && (e.getResponse().getStatus() == 404 || e.getResponse().getStatus() == 503)) {
                        LOGGER.error("Got status code {} when getting tenant {} subscribed apps", e.getResponse().getStatus(),tenant);
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
                LOGGER.info("Retrieved data for tenant ({}) from serviceRequest API. URL is {}, query response is {}. It took {}ms", tenant, queryHref, appsResponse, (System.currentTimeMillis() - subappQueryStart));
                JsonUtil ju = JsonUtil.buildNormalMapper();
                try {
                    List<ServiceRequestCollection> src = ju.fromJsonToList(appsResponse, ServiceRequestCollection.class);
                    if(src == null || src.isEmpty()){
                        LOGGER.error("Checking tenant (" + tenant + ") subscriptions. Empty application mapping items are retrieved");
                        return Collections.emptyList();
                    }
                    List<SubscriptionApps> subAppsList = new ArrayList<SubscriptionApps>();
                    for(ServiceRequestCollection s : src){
                        SubscriptionApps subscriptionApps = new SubscriptionApps();
                        if(s.getOrderComponents()!= null){
                            subscriptionApps.setTrial(s.isTrial());
                            subscriptionApps.setServiceType(s.getServiceType());
                            OrderComponents orderComponents = s.getOrderComponents();
                            if(orderComponents.getServiceComponent()!=null && orderComponents.getServiceComponent().getComponent()!=null){
                                for(Component com : orderComponents.getServiceComponent().getComponent()){
                                    EditionComponent editionComponent = new EditionComponent();
                                    editionComponent.setComponentId(orderComponents.getServiceComponent().getComponent_id());
                                    if(com !=null && com.getComponent_parameter()!=null && !com.getComponent_parameter().isEmpty()){
                                        for(ComponentParameter componentParameter : com.getComponent_parameter()){
                                            if("APPLICATION_EDITION".equals(componentParameter.getKey())){
                                                editionComponent.setEdition(componentParameter.getValue());
                                                subscriptionApps.addEditionComponent(editionComponent);
                                                LOGGER.info("EditionComponent with id {} and edition {} is added",editionComponent.getComponentId(),editionComponent.getEdition());
                                            }
                                        }
                                    }
                                }
                            }
                            subAppsList.add(subscriptionApps);
                        }
                    }
                    tenantSubscriptionInfo.setSubscriptionAppsList(subAppsList);
//                    LOGGER.info("Before mapping subcribed app list is {}",subAppsList.getEditionComponentsList().);
                    List<String> subscribeAppsList= SubsriptionAppsUtil.getSubscribedAppsList(tenantSubscriptionInfo);
                    LOGGER.info("After mapping Subscribed App list is {}",subscribeAppsList);
                    if(subscribeAppsList == null ){
                        LOGGER.error("After Mapping action,Empty subscription list found!");
                        return Collections.emptyList();
                    }
                    LOGGER.info("Put subscribe apps into cache,{},{}", subscribeAppsList, tenantSubscriptionInfo);
                    cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).put(cacheKey,new CachedTenantSubcriptionInfo(subscribeAppsList, tenantSubscriptionInfo));
                    return subscribeAppsList;

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

    private static void copyTenantSubscriptionInfo(TenantSubscriptionInfo from, TenantSubscriptionInfo to){
        if(from == null || to == null){
            LOGGER.error("Cannot copy value into or from null object!");
            return;
        }
        LOGGER.info("Copying TenantSubscriptionInfo data...");
        List<AppsInfo> toAppsInfoList  = new ArrayList<AppsInfo>();
        toAppsInfoList.addAll(from.getAppsInfoList());
        to.setAppsInfoList(toAppsInfoList);
    }

	private TenantSubscriptionUtil()
	{
	}
}
