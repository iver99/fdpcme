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
        	LOGGER.debug("Retrieved combined data is: {}", response);
            LOGGER.info("It takes {}ms to retrieve dashboard data from Dashboard-API", (System.currentTimeMillis() - start));
            return response;
        }catch(UniformInterfaceException e){
        	LOGGER.error("Error occurred: status code of the HTTP response indicates a response that is not expected");
        	LOGGER.error(e);
        }catch(ClientHandlerException e){//RestClient may timeout, so catch this runtime exception to make sure the response can return.
        	LOGGER.error("Error occurred: Signals a failure to process the HTTP request or HTTP response");
        	LOGGER.error(e);
        }catch(Exception e){
            LOGGER.error("Error occurred when retrieving combined data from Dashboard-UI!");
            LOGGER.error(e);
        }
        
        LOGGER.warn("Error occurred when retrieve combined data, returning empty string now...");
        return "";
        
    	
    }
}
