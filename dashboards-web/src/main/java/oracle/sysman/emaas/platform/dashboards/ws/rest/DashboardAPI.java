/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.JsonUtil;

import org.codehaus.jettison.json.JSONObject;

/**
 * @author wenjzhu
 */
@Path("/api/v1/dashboards")
public class DashboardAPI extends APIBase
{

	public DashboardAPI()
	{
		super();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDashboard(JSONObject dashboard)
	{
		System.out.println(dashboard);
		try {
			Dashboard d = getJsonUtil().fromJson(dashboard.toString(), Dashboard.class);
			DashboardManager manager = DashboardManager.getInstance();
			String tenantId = getTenantId();
			d = manager.saveNewDashboard(d, tenantId);
			return Response.ok(JsonUtil.buildNormalMapper().toJson(d)).build();
		}
		catch (IOException e) {
			return Response.status(404).build();
		}
		catch (DashboardException e) {
			ErrorEntity error = new ErrorEntity(e);
			return Response.status(error.getStatusCode()).entity(error).build();
		}

	}
}
