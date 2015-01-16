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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;

/**
 * @author wenjzhu
 * @author guobaochen introduce to add favorite, to delete favorite
 */
@Path("/v1/dashboards/favorites")
public class FavoriteAPI extends APIBase
{

	public FavoriteAPI()
	{
		super();
	}

	@POST
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addOneFavoriteDashboard(@PathParam("id") Long dashboardId)
	{
		DashboardManager dm = DashboardManager.getInstance();
		String tenantId = getTenantId();
		try {
			dm.addFavoriteDashboard(dashboardId, tenantId);
			return Response.status(Status.OK).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@DELETE
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOneFavoriteDashboard(@PathParam("id") Long dashboardId)
	{
		DashboardManager dm = DashboardManager.getInstance();
		String tenantId = getTenantId();
		try {
			dm.removeFavoriteDashboard(dashboardId, tenantId);
			return Response.status(Status.OK).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllFavoriteDashboards()
	{
		DashboardManager manager = DashboardManager.getInstance();
		List<Dashboard> pd = manager.getFavoriteDashboards(getTenantId());
		List<FavoriteEntity> entities = new ArrayList<FavoriteEntity>();
		if (pd != null) {
			for (Dashboard dashboard : pd) {
				updateDashboardHref(dashboard);
				entities.add(new FavoriteEntity(dashboard));
			}
		}
		return Response.ok(getJsonUtil().toJson(entities)).build();
	}

	@GET
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isFavoriteDashboard(@PathParam("id") Long dashboardId)
	{
		DashboardManager dm = DashboardManager.getInstance();
		String tenantId = getTenantId();
		try {
			Dashboard dbd = dm.getDashboardById(dashboardId, tenantId);
			boolean isFavorite = dm.isDashboardFavorite(dbd.getDashboardId(), tenantId);
			IsFavoriteEntity ife = new IsFavoriteEntity();
			ife.setIsFavorite(isFavorite);
			return Response.status(Status.OK).entity(getJsonUtil().toJson(ife)).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
	}
}
