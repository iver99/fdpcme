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

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.ws.rest.thread.MetadataRefreshRunnable;
import oracle.sysman.emaas.platform.dashboards.ws.rest.thread.NlsRefreshRunnable;
import oracle.sysman.emaas.platform.dashboards.ws.rest.thread.OobRefreshRunnable;

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
        LOGGER.info("Starting a new thread for fresh {} OOB.", serviceName);
        MetadataRefreshRunnable oobRunnable = new OobRefreshRunnable();
        oobRunnable.setServiceName(serviceName);
        Thread thread = new Thread(oobRunnable, "Refresh " + serviceName + " OOB.");
        thread.start();
        return Response.ok().build();
    }
    
    @PUT
    @Path("nls/{serviceName}")
    public Response refreshNLS(@PathParam("serviceName") String serviceName) {
        LOGGER.info("Starting a new thread for fresh {} resource bundles.", serviceName);
        MetadataRefreshRunnable nlsRunnable = new NlsRefreshRunnable();
        nlsRunnable.setServiceName(serviceName);
        Thread thread = new Thread(nlsRunnable, "Refresh " + serviceName + " resource bundles.");
        thread.start();
        return Response.ok().build();
    }

}
