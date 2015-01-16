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

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.JsonUtil;

/**
 * @author wenjzhu
 */
public class APIBase
{
	@Context
	protected UriInfo uriInfo;
	private final JsonUtil jsonUtil;

	public APIBase()
	{
		super();
		jsonUtil = JsonUtil.buildNonNullMapper();
		jsonUtil.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}

	public Response buildErrorResponse(ErrorEntity error)
	{
		if (error == null) {
			return null;
		}
		return Response.status(error.getStatusCode()).entity(getJsonUtil().toJson(error)).build();
	}

	public JsonUtil getJsonUtil()
	{
		return jsonUtil;
	}

	public String getTenantId()
	{
		return "test";
	}

	/*
	 * Updates the specified dashboard by generating all href fields
	 */
	protected Dashboard updateDashboardHref(Dashboard dbd)
	{
		if (dbd == null) {
			return null;
		}
		String href = uriInfo.getBaseUri() + "v1/dashboards/" + dbd.getDashboardId();
		dbd.setHref(href);
		return dbd;
	}
}
