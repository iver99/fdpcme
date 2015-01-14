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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;

/**
 * @author wenjzhu
 */
@Path("/api/v1/dashboards/favorites")
public class FavoriteAPI extends APIBase
{

	public FavoriteAPI()
	{
		super();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFavoriteDashboards()
	{
		DashboardManager manager = DashboardManager.getInstance();
		List<Dashboard> pd = manager.getFavoriteDashboards(getTenantId());
		return Response.ok(getJsonUtil().toJson(pd)).build();
	}

}
