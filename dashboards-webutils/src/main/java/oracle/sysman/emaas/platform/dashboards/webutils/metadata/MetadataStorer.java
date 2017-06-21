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

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.ResourceBundleManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
public class MetadataStorer
{
    private static final Logger LOGGER = LogManager.getLogger(MetadataStorer.class);
    
    public void storeResourceBundle(List<EmsResourceBundle> rbList) throws CommonResourceException {
        if(rbList == null || rbList.isEmpty()) {
            return;
        }
        
        String serviceName = rbList.get(0).getServiceName();
        ResourceBundleManager manager = ResourceBundleManager.getInstance();
        manager.refreshResourceBundleByService(serviceName, rbList);
    }
    
    /**
     * store OOB dashboards into database which are all provided by one service
     * @param oobList
     * @throws DashboardException
     */
    public void storeOobDashboards(List<Dashboard> oobList) throws DashboardException {
        if(oobList == null || oobList.isEmpty()) {
            return;
        }
        Integer applicationType = oobList.get(0).getAppicationType().getValue();
        List<EmsDashboard> emsDashboardList = new ArrayList<EmsDashboard>();
        for(Dashboard oob : oobList) {
            // OOB dashboard's id must be fixed and maintain by other service
            if(oob.getDashboardId() == null) {
                LOGGER.error("OOB Dashboard's id can not be null for - {}", oob.getName());
                throw new CommonResourceException("OOB Dashboard's id can not be null for - " + oob.getName());
            }
            emsDashboardList.add(oob.getPersistenceEntity(null));
        }
        DashboardManager manager = DashboardManager.getInstance();
        try {
            manager.refreshOobDashboardByAppType(applicationType, DashboardManager.NON_TENANT_ID, emsDashboardList);
        } catch(Exception e) {
            LOGGER.error("Error when refresh OOB: {}", e.getLocalizedMessage());
            throw new CommonResourceException("Error when clean up existed OOB: " + e.getLocalizedMessage());
        }
    }
    
    /**
     * TODO:
     * All OOB Dashboards for one service will be saved in a Map.
     * ------CacheKey:ApplicationType
     *     |----CacheValue:Map<DashboardId, Dashboard>
     * @param oobList
     */
/*    @SuppressWarnings("unchecked")
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
    }*/
    
    /**
     * generate cache's key by application type
     * @param applicationType
     * @param dashboardId
     * @return
     */
/*    static public Object getCacheKey(Integer applicationType) {
        return DefaultKeyGenerator.getInstance().generate(null, new Keys(applicationType));
    }*/
}
