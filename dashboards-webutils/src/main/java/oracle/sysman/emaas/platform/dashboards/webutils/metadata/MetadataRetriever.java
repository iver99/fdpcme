/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.webutils.metadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.model.TileParam;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil.VersionedLink;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
public class MetadataRetriever
{
    public static final String DEFAULT_OOB_REL = "oob/dashboards";
    public static final String DEFAULT_NLS_REL = "resource_bundle";
    
    private static final Map<String, DashboardApplicationType> APPLICATION_MAP = new HashMap<String, DashboardApplicationType>();
    private static final Map<String, String> WIDGET_GROUP_MAP = new HashMap<String, String>();
    // TODO fix the correct service name in SM
    private static final String SERVICENAME_APM = "ApmDataServer";
    private static final String SERVICENAME_ITA = "TargetAnalytics";//"SavedSearch";
    private static final String SERVICENAME_LA = "LogAnalyticsProcessor";
    private static final String SERVICENAME_MONITORING = "Monitoring";
    private static final String SERVICENAME_SECURITY_ANALYTICS = "SecurityAnalytics";
    private static final String SERVICENAME_ORCHESTRATION = "CosFacadeService";
    private static final String SERVICENAME_COMPLIANCE = "Compliance";
    private static final String SERVICENAME_UDE = "UDE";
    private static final String INVISIBLE_WIDGET_GROUP = "Invisible Widget Group";
    private static final Logger LOGGER = LogManager.getLogger(MetadataRetriever.class);
    private static final String ORACLE = "Oracle";
    private static final String X_USER_IDENTITY_DOMAIN_NAME_HEADER = "X-USER-IDENTITY-DOMAIN-NAME";
    private static final String TENANT_CLOUD_SERVICES = "CloudServices";
    
    static {
        // service name from SM -> application type in DF
        APPLICATION_MAP.put(SERVICENAME_APM, DashboardApplicationType.APM);
        APPLICATION_MAP.put(SERVICENAME_ITA, DashboardApplicationType.ITAnalytics);
        APPLICATION_MAP.put(SERVICENAME_LA, DashboardApplicationType.LogAnalytics);
        APPLICATION_MAP.put(SERVICENAME_MONITORING, DashboardApplicationType.Monitoring);
        APPLICATION_MAP.put(SERVICENAME_SECURITY_ANALYTICS, DashboardApplicationType.SecurityAnalytics);
        APPLICATION_MAP.put(SERVICENAME_ORCHESTRATION, DashboardApplicationType.Orchestration);
        APPLICATION_MAP.put(SERVICENAME_COMPLIANCE, DashboardApplicationType.Compliance);
        APPLICATION_MAP.put(SERVICENAME_UDE, DashboardApplicationType.UDE);
        
        // service name from SM -> widget group name/category name in DF
        WIDGET_GROUP_MAP.put(SERVICENAME_APM, "APMCS");
        WIDGET_GROUP_MAP.put(SERVICENAME_ITA, "Data Explorer");
        WIDGET_GROUP_MAP.put(SERVICENAME_LA, "Log Analytics");
        //WIDGET_GROUP_MAP.put(SERVICENAME_MONITORING, "");
        WIDGET_GROUP_MAP.put(SERVICENAME_SECURITY_ANALYTICS, "Security Analytics");
        WIDGET_GROUP_MAP.put(SERVICENAME_ORCHESTRATION, "Orchestration");
        //WIDGET_GROUP_MAP.put(SERVICENAME_COMPLIANCE, "");
        //WIDGET_GROUP_MAP.put(SERVICENAME_UDE, "");
    }
    
    /**
     * Get OOB dashboards url by serviceName;<br />
     * Retrieve OOB from the url;<br />
     * Convert the JSON to {@link oracle.sysman.emaas.platform.dashboards.core.model.Dashboard};
     * @param serviceName
     * @return
     * @throws CommonFunctionalException 
     */
    public List<Dashboard> getOobDashboardsByService(String serviceName) throws CommonFunctionalException {
        LOGGER.info("Retrieve OOB Dashbaords from : {}", serviceName);
        VersionedLink link = RegistryLookupUtil.getServiceInternalLink(serviceName, DEFAULT_OOB_REL);
        if(link == null) {
            LOGGER.warn("{} has not provided {} for fetching OOB Dashboards", serviceName, DEFAULT_OOB_REL);
            return null;
        }
        RestClient rc = new RestClient();
        rc.setHeader(X_USER_IDENTITY_DOMAIN_NAME_HEADER, TENANT_CLOUD_SERVICES);
        String response = rc.get(link.getHref(), null, link.getAuthToken());
        JsonUtil jUtil = JsonUtil.buildNormalMapper();
        List<Dashboard> oobList = new ArrayList<Dashboard>();
        try {
            oobList = jUtil.fromJsonToList(response, Dashboard.class);
        }
        catch (IOException e) {
            LOGGER.error("Error when converting OOB Dashbaord: " + e.getLocalizedMessage());
            throw new CommonFunctionalException("Error when converting OOB Dashbaord: " + e.getLocalizedMessage());
        }
        return setDefaultOobValue(oobList, serviceName);
    }
    
    /**
     * set OOB Dashbaord's default values
     * @param oobList
     * @param serviceName
     * @return
     */
    private List<Dashboard> setDefaultOobValue(List<Dashboard> oobList, String serviceName) {
        if(oobList != null && !oobList.isEmpty()) {
            for(Dashboard oob : oobList) {
                oob.setOwner(ORACLE);
                oob.setLastModifiedBy(ORACLE);
                oob.setIsSystem(Boolean.TRUE);
                oob.setSharePublic(Boolean.FALSE);
                //oob.setApplicationType(APPLICATION_MAP.get(serviceName));
                oob.setAppicationType(APPLICATION_MAP.get(serviceName));
                
                // set tile's default values
                if(oob.getTileList() != null && !oob.getTileList().isEmpty()) {
                    for(Tile tile : oob.getTileList()) {
                        tile.setOwner(ORACLE);
                        tile.setLastModifiedBy(ORACLE);
                        tile.setCreationDate(oob.getCreationDate());
                        tile.setLastModificationDate(oob.getLastModificationDate());
                        tile.setWidgetOwner(ORACLE);
                        tile.setWidgetDeleted(Boolean.FALSE);
                        tile.setWidgetSource(1);
                        tile.setWidgetSupportTimeControl(Boolean.FALSE);
                        tile.setWidgetLinkedDashboard(null);
                        if(Dashboard.DASHBOARD_TYPE_SINGLEPAGE.equals(oob.getType())) {
                            tile.setWidgetGroupName(INVISIBLE_WIDGET_GROUP);
                        } else {
                            tile.setWidgetGroupName(WIDGET_GROUP_MAP.get(serviceName) == null ? INVISIBLE_WIDGET_GROUP
                                    : WIDGET_GROUP_MAP.get(serviceName));
                        }
                        
                        // set tile parameter's default values
                        if(tile.getParameters() != null && !tile.getParameters().isEmpty()) {
                            for(TileParam tp : tile.getParameters()) {
                                tp.setIsSystem(Boolean.TRUE);
                            }
                        }
                    }
                }
            }
        }
        return oobList;
    }
}
