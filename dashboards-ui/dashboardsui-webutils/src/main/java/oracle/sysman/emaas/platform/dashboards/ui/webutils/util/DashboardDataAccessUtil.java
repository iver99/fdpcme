package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.registration.StringCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import java.util.Collections;

/**
 * Created by guochen on 11/18/16.
 */
public class DashboardDataAccessUtil {
    private static final Logger LOGGER = LogManager.getLogger(DashboardDataAccessUtil.class);

    public static String getDashboardData(String tenantIdParam,
                                          String userTenant, String referer,
                                          long dashboardId) {
        try {
            long start = System.currentTimeMillis();
            StringCacheUtil cache = StringCacheUtil.getRegistrationCacheInstance();
            String data = cache.get(userTenant);
            if (!StringUtil.isEmpty(data)) {
                LOGGER.info("Retrieved registration data from cache for userTenant {}", userTenant);
                return data;
            }

            Link dashboardsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.service", null);
            if (dashboardsLink == null || StringUtils.isEmpty(dashboardsLink.getHref())) {
                LOGGER.warn("Retrieving dashboard data for tenant {}: null/empty dashboardsLink retrieved from service registry.");
                return null;
            }
            LOGGER.info("Dashboard REST API from dashboard-api href is: " + dashboardsLink.getHref());
            String dashboardHref = dashboardsLink.getHref() + "/" + dashboardId;
            TenantSubscriptionUtil.RestClient rc = new TenantSubscriptionUtil.RestClient();
            rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantIdParam);
            rc.setHeader("X-REMOTE-USER", userTenant);
            rc.setHeader("Referer", referer);
            String response = rc.get(dashboardHref, tenantIdParam);
            cache.put(userTenant, response);
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
        try {
            long start = System.currentTimeMillis();
            StringCacheUtil cache = StringCacheUtil.getUserInfoCacheInstance();
            String data = cache.get(userTenant);
            if (!StringUtil.isEmpty(data)) {
                LOGGER.info("Retrieved user info from cache for userTenant {}", userTenant);
                return data;
            }

            Link configurationsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.configurations", null);
            if (configurationsLink == null || StringUtils.isEmpty(configurationsLink.getHref())) {
                LOGGER.warn("Retrieving configurations links for tenant {}: null/empty configurationsLink retrieved from service registry.");
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
            cache.put(userTenant, response);
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
        try {
            long start = System.currentTimeMillis();
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
            LOGGER.info("Retrieved registration data is: {}", response);
            LOGGER.info("It takes {}ms to retrieve registration data from Dashboard-API", (System.currentTimeMillis() - start));
            return response;
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
