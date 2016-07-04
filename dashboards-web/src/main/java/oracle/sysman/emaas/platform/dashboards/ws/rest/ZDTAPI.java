/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.core.zdt.DataManager;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.ZDTEntity;

/**
 * @author guochen
 */
@Path("/v1/zdt")
public class ZDTAPI extends APIBase
{
	private static final Logger logger = LogManager.getLogger(ZDTAPI.class);

	public ZDTAPI()
	{
		super();
	}

	@GET
	@Path("counts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntitiesCoung()
	{
		long dashboardCount = DataManager.getInstance().getAllDashboardsCount();
		long favoriteCount = DataManager.getInstance().getAllFavoriteCount();
		long preferenceCount = DataManager.getInstance().getAllPreferencessCount();
		logger.debug("ZDT counters: dashboards count - {}, favorite count - {}, preference count - {}", dashboardCount,
				favoriteCount, preferenceCount);
		ZDTEntity zdte = new ZDTEntity(dashboardCount, favoriteCount, preferenceCount);
		return Response.ok(getJsonUtil().toJson(zdte)).build();
	}
}
