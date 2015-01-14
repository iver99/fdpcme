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
import java.io.UnsupportedEncodingException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;

import org.codehaus.jettison.json.JSONObject;

/**
 * @author wenjzhu
 */
@Path("/api/v1/dashboards")
public class DashboardAPI extends APIBase
{
	@Context
	private UriInfo uriInfo;

	public DashboardAPI()
	{
		super();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDashboard(JSONObject dashboard)
	{
		//System.out.println(dashboard);
		try {
			Dashboard d = getJsonUtil().fromJson(dashboard.toString(), Dashboard.class);
			DashboardManager manager = DashboardManager.getInstance();
			String tenantId = getTenantId();
			d = manager.saveNewDashboard(d, tenantId);
			return Response.ok(getJsonUtil().toJson(d)).build();
		}
		catch (IOException e) {
			ErrorEntity error = new ErrorEntity();
			error.setErrorCode(DashboardErrorConstants.DASHBOARD_COMMON_UI_ERROR_CODE);
			error.setErrorMessage(MessageUtils.getDefaultBundleString("DASHBOARD_JSON_PARSE_ERROR"));
			return buildErrorResponse(error);
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@DELETE
	@Path("{id: [0-9]*}")
	public Response deleteDashboard(@PathParam("id") long dashboardId)
	{
		DashboardManager manager = DashboardManager.getInstance();
		manager.deleteDashboard(dashboardId, getTenantId());
		return Response.status(Status.OK).build();
	}

	@GET
	@Path("{id: [0-9]*}/screenshot")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getDashboardBase64ScreenShot(@PathParam("id") long dashboardId)
	{
		try {
			DashboardManager manager = DashboardManager.getInstance();
			String ss = manager.getDashboardBase64ScreenShotById(dashboardId, getTenantId());
			return Response.ok(ss).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}

	}

	@GET
	@Path("{id: [1-9][0-9]*}")
	public Response queryDashboardById(@PathParam("id") long dashboardId)
	{
		DashboardManager dm = DashboardManager.getInstance();
		String tenantId = super.getTenantId();
		try {
			Dashboard dbd = dm.getDashboardById(dashboardId, tenantId);
			updateDashboardHref(dbd);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboards(@QueryParam("queryString") String queryString,
			@DefaultValue("50") @QueryParam("limit") int limit, @DefaultValue("0") @QueryParam("offset") int offset)
	{
		String qs = null;
		try {
			qs = queryString == null ? null : java.net.URLDecoder.decode(queryString, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			DashboardManager manager = DashboardManager.getInstance();
			PaginatedDashboards pd = manager.listDashboards(qs, offset, limit, getTenantId(), true);
			return Response.ok(getJsonUtil().toJson(pd)).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}")
	public Response updateDashboard(@PathParam("id") long dashboardId, JSONObject inputJson)
	{
		Dashboard input = null;
		try {
			input = getJsonUtil().fromJson(inputJson.toString(), Dashboard.class);
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			if (e1.getCause() instanceof DashboardException) {
				return buildErrorResponse(new ErrorEntity((DashboardException) e1.getCause()));
			}
		}

		DashboardManager dm = DashboardManager.getInstance();
		String tenantId = getTenantId();
		try {
			input.setDashboardId(dashboardId);
			Dashboard dbd = dm.updateDashboard(input, tenantId);
			updateDashboardHref(dbd);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	private Dashboard updateDashboardHref(Dashboard dbd)
	{
		if (dbd == null) {
			return null;
		}
		String href = uriInfo.getBaseUri() + "api/v1/dashboards/" + dbd.getDashboardId();
		dbd.setHref(href);
		updateDashboardScreenshotHref(dbd);
		return dbd;
	}

	private Dashboard updateDashboardScreenshotHref(Dashboard dbd)
	{
		if (dbd == null) {
			return null;
		}
		String screenShotUrl = uriInfo.getBaseUri() + "api/v1/dashboards/" + dbd.getDashboardId() + "/screenshot";
		dbd.setScreenShotHref(screenShotUrl);
		return dbd;
	}
}
