package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import java.math.BigInteger;

import javax.ws.rs.core.MediaType;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;

import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
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
        RestClient rc = new RestClient();
        rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME", tenantIdParam);
        rc.setHeader("X-REMOTE-USER", userTenant);
        rc.setHeader("SESSION_EXP", sessionExp);
        //EMCPDF-3448, FEB20: 3 admin link dif found in farm jobs
        rc.setHeader("OAM_REMOTE_USER", userTenant);
        rc.setHeader("Referer", referer);
        rc.setAccept(MediaType.TEXT_PLAIN);
        try{
        	String response = rc.get(dashboardHref, tenantIdParam);
        	LOGGER.debug("Retrieved combined data is: {}", response);
            LOGGER.info("It takes {}ms to retrieve dashboard data from Dashboard-API", (System.currentTimeMillis() - start));
            return response;
        }catch(Exception e){
            LOGGER.error("Error occurred when retrieving combined data from Dashboard-UI!");
            LOGGER.error(e);
        }
        
        LOGGER.warn("Error occurred when retrieve combined data, returning empty string now...");
        return null;
        
    	
    }
}
