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

import javax.ws.rs.Path;

import oracle.sysman.emaas.platform.dashboards.ws.rest.util.JsonUtil;

/**
 * @author wenjzhu
 */
@Path("/api/v1/dashboards/favorites")
public class FavoriteAPI
{
	private final JsonUtil jsonUtil = JsonUtil.buildNonNullMapper();

	public FavoriteAPI()
	{
		super();
	}

}
