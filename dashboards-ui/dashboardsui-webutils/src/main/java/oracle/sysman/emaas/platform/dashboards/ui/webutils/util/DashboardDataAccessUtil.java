package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
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

    public static String get(String tenantIdParam,
                      String userTenant, String referer,
                      long dashboardId) {
        Link dashboardsLink = RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.service", null);
        if (dashboardsLink == null || StringUtils.isEmpty(dashboardsLink.getHref())) {
            LOGGER.warn("Retrieving dashboard data for tenant {}: null/empty dashboardsLink retrieved from service registry.");
            return null;
        }
        LOGGER.info("Dashboard REST API from dashboard-api href is: " + dashboardsLink.getHref());
        String dashboardHref = dashboardsLink.getHref() + "?dashboardId=" + dashboardId;
        TenantSubscriptionUtil.RestClient rc = new TenantSubscriptionUtil.RestClient();
        rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantIdParam);
        rc.setHeader("X-REMOTE-USER", userTenant);
        rc.setHeader("Referer", referer);
        String response = rc.get(dashboardHref, tenantIdParam);
        LOGGER.info("Retrieved dashboard data is: {}", response);
        return response;
    }
}
