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

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DatabaseDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.NewOobExporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

/**
 * @author reliang
 *
 */

@Path("/v1/tool")
public class InternalToolAPI extends APIBase {
    private static final Logger LOGGER = LogManager.getLogger(InternalToolAPI.class);
    
    @GET
    @Path("dashboard/{name}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response queryDashboardByName(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            @HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("name") String dashboardName){
        infoInteractionLogAPIIncomingCall(tenantIdParam, null, "Service call to [GET] /v1/tool/dashboard/{}", dashboardName);
        DashboardManager dm = DashboardManager.getInstance();
        try {
            if (!DependencyStatus.getInstance().isDatabaseUp())  {
                LOGGER.error("Error to call [GET] /v1/tool/dashboard/{}: database is down", dashboardName);
                throw new DatabaseDependencyUnavailableException();
            }
            Long tenantId = getTenantId(tenantIdParam);
            initializeUserContext(tenantIdParam, userTenant);
            Dashboard dbd = dm.getDashboardByName(dashboardName, tenantId);
            String dbJson = NewOobExporter.exportDashboard(dbd);
            return Response.ok(dbJson).build();
        }
        catch (DashboardException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        }
        catch (BasicServiceMalfunctionException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        }
        catch (JSONException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        }
        finally {
            clearUserContext();
        }
    }
}
