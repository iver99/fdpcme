/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.model.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.RegistryLookupUtil;

/**
 * @author miao
 */
@Path("/registry")
public class RegistryLookupAPI
{

	@Path("/lookup/link")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRegistryLink(@QueryParam("serviceName") String serviceName, @QueryParam("version") String version,
			@QueryParam("rel") String rel)
	{
		Link lk = RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel);
		if (lk != null) {
			return Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(lk)).build();
		}
		else {
			ErrorEntity error = new ErrorEntity(ErrorEntity.REGISTRY_LOOKUP_NOT_FOUND_ERROR_CODE,
					MessageUtils.getDefaultBundleString("REGISTRY_LOOKUP_NOT_FOUND_ERROR", serviceName, version, rel));
			return Response.status(Status.NOT_FOUND).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
		}
	}
}