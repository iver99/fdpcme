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

import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.JsonUtil;

/**
 * @author wenjzhu
 */
public class APIBase
{
	private final JsonUtil jsonUtil = JsonUtil.buildNonNullMapper();

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

}
