package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import java.math.BigInteger;

import javax.ws.rs.core.MediaType;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * Created by guochen on 11/18/16.
 */
public class DashboardDataAccessUtil {
    private static final Logger LOGGER = LogManager.getLogger(DashboardDataAccessUtil.class);

    /*public static String getDashboardData(String tenantIdParam,
                                          String userTenant, String referer,
                                          BigInteger dashboardId) {
        try {
            long start = System.currentTimeMillis();
            Link dashboardsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.service", null);
            if (dashboardsLink == null || StringUtils.isEmpty(dashboardsLink.getHref())) {
                LOGGER.warn("Retrieving dashboard data for tenant {}: null/empty dashboardsLink retrieved from service registry.");
                return null;
            }
            LOGGER.info("Dashboard REST API from dashboard-api href is: " + dashboardsLink.getHref());
            String dashboardHref = dashboardsLink.getHref() + "/" + dashboardId.toString();
            TenantSubscriptionUtil.RestClient rc = new TenantSubscriptionUtil.RestClient();
            rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantIdParam);
            rc.setHeader("X-REMOTE-USER", userTenant);
            rc.setHeader("Referer", referer);
            String response = rc.get(dashboardHref, tenantIdParam);
            LOGGER.info("Retrieved dashboard data is: {}", response);
            LOGGER.info("It takes {}ms to retrieve dashboard data from Dashboard-API", (System.currentTimeMillis() - start));
            return response;
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static String getUserTenantInfo(String tenantIdParam,
                                          String userTenant, String referer, String sessionExp) {
        long start = System.currentTimeMillis();
        Tenant cacheTenant = new Tenant(tenantIdParam);
        Object userTenantKey = DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(userTenant));
        ICacheManager cm = CacheManagers.getInstance().build();
        ICache cache = cm.getCache(CacheConstants.CACHES_TENANT_USER_CACHE);
        if (cache != null) {
            try {
                Object obj = cache.get(userTenantKey);
                if (obj instanceof String) {
                    String data = (String)obj;
                    LOGGER.info("Retrieved user info from cache for userTenant {}, cached data is {}", userTenant, data);
                    return data;
                }
            } catch (ExecutionException e) {
                // for cache issue, we'll continue retrieve data and just log a warning message
                LOGGER.warn(e.getLocalizedMessage(), e);
            }
        }

        try {
            Link configurationsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.configurations", null);
            if (configurationsLink == null || StringUtils.isEmpty(configurationsLink.getHref())) {
                LOGGER.warn("Retrieving configurations links for tenant {}: null/empty configurationsLink retrieved from service registry.");
                cache.evict(userTenantKey);
                return null;
            }
            LOGGER.info("Configurations REST API link from dashboard-api href is: " + configurationsLink.getHref());
            String userInfoHref = configurationsLink.getHref() + "/userInfo";
            TenantSubscriptionUtil.RestClient rc = new TenantSubscriptionUtil.RestClient();
            rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantIdParam);
            rc.setHeader("X-REMOTE-USER", userTenant);
            if (!StringUtil.isEmpty(referer)) {
                rc.setHeader("Referer", referer);
            }
            if (!StringUtil.isEmpty(sessionExp)) {
                rc.setHeader("SESSION_EXP", sessionExp);
            }
            String response = rc.get(userInfoHref, tenantIdParam);
            cache.put(userTenantKey, response);
            LOGGER.info("Retrieved userInfo data is: {}", response);
            LOGGER.info("It takes {}ms to retrieve userInfo data from Dashboard-API", (System.currentTimeMillis() - start));
            return response;
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static String getRegistrationData(String tenantIdParam,
                                           String userTenant, String referer, String sessionExp) {
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

        try {
            Link configurationsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.configurations", null);
            if (configurationsLink == null || StringUtils.isEmpty(configurationsLink.getHref())) {
                LOGGER.warn("Retrieving configurations links for tenant {}: null/empty configurationsLink retrieved from service registry.");
                return null;
            }
            LOGGER.info("Configurations REST API link from dashboard-api href is: " + configurationsLink.getHref());
            String userInfoHref = configurationsLink.getHref() + "/registration";
            TenantSubscriptionUtil.RestClient rc = new TenantSubscriptionUtil.RestClient();
            rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantIdParam);
            rc.setHeader("X-REMOTE-USER", userTenant);
            if (!StringUtil.isEmpty(referer)) {
                rc.setHeader("Referer", referer);
            }
            if (!StringUtil.isEmpty(sessionExp)) {
                rc.setHeader("SESSION_EXP", sessionExp);
            }
            String response = rc.get(userInfoHref, tenantIdParam);
            cache.put(userTenantKey, response);
            LOGGER.info("Retrieved registration data is: {}", response);
            LOGGER.info("It takes {}ms to retrieve registration data from Dashboard-API", (System.currentTimeMillis() - start));
            return response;
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return null;
        }
    }*/
    
    public static String getCombinedData(String tenantIdParam,
            String userTenant, String referer, String sessionExp,BigInteger dashboardId) {
    	long start = System.currentTimeMillis();
        Link dashboardsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.service", null);
        if (dashboardsLink == null || StringUtils.isEmpty(dashboardsLink.getHref())) {
            LOGGER.warn("Retrieving dashboard data for tenant {}: null/empty dashboardsLink retrieved from service registry.");
            return null;
        }
        LOGGER.info("Dashboard REST API from dashboard-api href is: " + dashboardsLink.getHref());
        String dashboardHref = dashboardsLink.getHref() + "/" + dashboardId.toString() + "/"+ "combinedData";
        TenantSubscriptionUtil.RestClient rc = new TenantSubscriptionUtil.RestClient();
        rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantIdParam);
        rc.setHeader("X-REMOTE-USER", userTenant);
        rc.setHeader("SESSION_EXP", sessionExp);
        rc.setHeader("Referer", referer);
        try{
        	String response = rc.get(dashboardHref, tenantIdParam,MediaType.TEXT_PLAIN);
        	LOGGER.info("Retrieved combined data is: {}", response);
            LOGGER.info("It takes {}ms to retrieve dashboard data from Dashboard-API", (System.currentTimeMillis() - start));
            return response;
        }catch(UniformInterfaceException e){
        	LOGGER.error("Error occurred: status code of the HTTP response indicates a response that is not expected");
        	LOGGER.error(e.getLocalizedMessage(),e);
        }catch(ClientHandlerException e){//RestClient may timeout, so catch this runtime exception to make sure the response can return.
        	LOGGER.error("Error occurred: Signals a failure to process the HTTP request or HTTP response");
        	LOGGER.error(e.getLocalizedMessage(),e);
        }
        
        LOGGER.warn("Error occurred when retrieve combined data, returning empty string now...");
        return "";
        
    	
    }
}
