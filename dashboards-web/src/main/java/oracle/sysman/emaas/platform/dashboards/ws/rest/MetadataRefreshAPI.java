/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataRetriever;
import oracle.sysman.emaas.platform.dashboards.webutils.metadata.MetadataStorer;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
@Path("/v1/refresh")
public class MetadataRefreshAPI extends APIBase
{
    private static final Logger LOGGER = LogManager.getLogger(MetadataRefreshAPI.class);
    
    @PUT
    @Path("oob/{serviceName}")
    public Response refreshOOB(@PathParam("serviceName") String serviceName) {
        List<Dashboard> oobList = new ArrayList<Dashboard>();
        
        MetadataRetriever oobRetriever = new MetadataRetriever();
        try {
            oobList = oobRetriever.getOobDashboardsByService(serviceName);
        } catch (CommonFunctionalException e) {
            LOGGER.error("Error when retrieving OOB from " + serviceName + " : " + e.getLocalizedMessage());
            return buildErrorResponse(new ErrorEntity(e));
        }
        
        MetadataStorer oobStorer = new MetadataStorer();
        // oobStorer.cacheOobDashboards(oobList);
        try {
            oobStorer.storeOobDashboards(oobList);
        } catch (DashboardException e) {
            LOGGER.error("Error when storing OOB into database for " + serviceName + " : " + e.getLocalizedMessage());
            return buildErrorResponse(new ErrorEntity(e));
        }
        
        return Response.ok().build();
    }
    
    @PUT
    @Path("nls/{serviceName}")
    public Response refreshNLS(@PathParam("serviceName") String serviceName) {
        return Response.ok().build();
    }

}
