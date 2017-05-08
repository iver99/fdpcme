/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.webutils.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataRetriever;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataStorer;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import weblogic.application.ApplicationLifecycleEvent;

/**
 * @author reliang
 *
 */
public class MetadataManager implements ApplicationServiceManager
{
    private static final Logger LOGGER = LogManager.getLogger(MetadataManager.class);
    //TODO fetch all services by rel name
    private static final List<String> oobProvider = 
            Arrays.asList("TargetAnalytics", "LogAnalyticsProcessor", "ApmDataServer", "CosFacadeService");
    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#getName()
     */
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStart(weblogic.application.ApplicationLifecycleEvent)
     */
    @Override
    public void postStart(ApplicationLifecycleEvent evt) throws Exception
    {
        LOGGER.info("Entry: MetadataManager.postStart");
        for(String serviceName : oobProvider) {
            // Load OOB Dashboard from other services
            List<Dashboard> oobList = new ArrayList<Dashboard>();
            
            MetadataRetriever oobRetriever = new MetadataRetriever();
            try {
                oobList = oobRetriever.getOobDashboardsByService(serviceName);
            } catch (CommonFunctionalException e) {
                LOGGER.error("Error when retrieving OOB from " + serviceName + " : " + e.getLocalizedMessage());
            }
            
            MetadataStorer oobStorer = new MetadataStorer();
            try {
                oobStorer.storeOobDashboards(oobList);
            } catch (DashboardException e) {
                LOGGER.error("Error when storing OOB into database for " + serviceName + " : " + e.getLocalizedMessage());
            }
            
            // Load resource bundle for OOB metadata from other services
            // TODO api.refreshNLS(serviceName);
        }
    }

    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#postStop(weblogic.application.ApplicationLifecycleEvent)
     */
    @Override
    public void postStop(ApplicationLifecycleEvent evt) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStart(weblogic.application.ApplicationLifecycleEvent)
     */
    @Override
    public void preStart(ApplicationLifecycleEvent evt) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager#preStop(weblogic.application.ApplicationLifecycleEvent)
     */
    @Override
    public void preStop(ApplicationLifecycleEvent evt) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
