/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.core;

import java.util.List;

import javax.persistence.EntityManager;

import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.entity.EmsResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
public class ResourceBundleManager {
    private static final Logger LOGGER = LogManager.getLogger(ResourceBundleManager.class);
    private static final ResourceBundleManager instance = new ResourceBundleManager();

    public static ResourceBundleManager getInstance() {
        return instance;
    }
    
    /**
     * 1. delete all resource bundles by serviceName
     * 2. insert rbList as new resource bundles
     * @param serviceName
     * @param rbList
     * @throws CommonResourceException 
     */
    public void refreshResourceBundleByService(String serviceName, List<EmsResourceBundle> rbList) throws CommonResourceException {
        LOGGER.info("Refresh resource bundles for service : {}", serviceName);
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        try {
            dsf.refreshResourceBundleByService(serviceName, rbList);
        } catch (Exception e) {
            LOGGER.error("Error when refreshing resource bundles: {}", e.getLocalizedMessage());
            throw new CommonResourceException("Error when refreshing resource bundles: " + e.getLocalizedMessage());
        }
        
        EntityManager em = dsf.getEntityManager();
        if (em != null) {
            em.close();
        }
    }
}
