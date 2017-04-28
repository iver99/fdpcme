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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
public class MetadataStorer
{
    private static final Logger LOGGER = LogManager.getLogger(MetadataStorer.class);
    
    /**
     * store OOB dashboards into database which are all provided by one service
     * @param oobList
     * @throws DashboardException
     */
    public void storeOobDashboards(List<Dashboard> oobList) throws DashboardException {
        if(oobList == null || oobList.isEmpty()) {
            return;
        }
        
        DashboardManager manager = DashboardManager.getInstance();
        // clean up existed OOB dashboards of this application
        Integer applicationType = oobList.get(0).getAppicationType().getValue();
        try {
            manager.deleteDashboardByAppType(applicationType, DashboardManager.NON_TENANT_ID);
        } catch(Exception e) {
            LOGGER.error("Error when clean up existed OOB: " + e.getLocalizedMessage());
            throw new CommonResourceException("Error when clean up existed OOB: " + e.getLocalizedMessage());
        }
        
        // store non-set OOB Dashboard first
        for(Dashboard oob : oobList) {
            if(!Dashboard.DASHBOARD_TYPE_SET.equals(oob.getType())) {
                manager.saveNewDashboard(oob, DashboardManager.NON_TENANT_ID);
            }
        }
        // store OOB Dashboard set
        for(Dashboard oob : oobList) {
            if(Dashboard.DASHBOARD_TYPE_SET.equals(oob.getType())) {
                manager.saveNewDashboard(oob, DashboardManager.NON_TENANT_ID);
            }
        }
    }
    
    /**
     * TODO:
     * All OOB Dashboards for one service will be saved in a Map.
     * ------CacheKey:ApplicationType
     *     |----CacheValue:Map<DashboardId, Dashboard>
     * @param oobList
     */
    @SuppressWarnings("unchecked")
    public void cacheOobDashboards(List<Dashboard> oobList) {
        if(oobList == null || oobList.isEmpty()) {
            return;
        }
        
        ICacheManager cm = CacheManagers.getInstance().build();
        ICache<Object, Map<BigInteger, Dashboard>> cache = cm.getCache(CacheConstants.CACHES_OOB_DASHBOARD_CACHE);
        
        for(Dashboard oob : oobList) {
            Object serviceKey = getCacheKey(oob.getApplicationType());
            try {
                Map<BigInteger, Dashboard> serviceDashboards = (Map<BigInteger, Dashboard>) cache.get(serviceKey);
                if(serviceDashboards == null) {
                    serviceDashboards = new HashMap<BigInteger, Dashboard>();
                    cache.put(serviceKey, serviceDashboards);
                }
                serviceDashboards.put(oob.getDashboardId(), oob);
            }
            catch (ExecutionException e) {
                LOGGER.error("Error when cacheOobDashboards: " + e.getLocalizedMessage());
            }
        }
    }
    
    /**
     * generate cache's key by application type
     * @param applicationType
     * @param dashboardId
     * @return
     */
    static public Object getCacheKey(Integer applicationType) {
        return DefaultKeyGenerator.getInstance().generate(null, new Keys(applicationType));
    }
}
