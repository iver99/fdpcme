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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.model.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.MessageUtils;

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
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		try {
			List<InstanceInfo> result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			if (result != null && result.size() > 0) {
				//find https link first
				for (InstanceInfo inst : result) {
					List<Link> links = inst.getLinksWithProtocol(rel, "https");
					if (links != null && links.size() > 0) {
						Link l = links.get(0);
						return Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(l)).build();
					}
				}

				//https link is not found, then find http link
				for (InstanceInfo inst : result) {
					List<Link> links = inst.getLinksWithProtocol(rel, "http");
					if (links != null && links.size() > 0) {
						Link l = links.get(0);
						return Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(l)).build();
					}
				}
			}
			return Response.status(Status.NOT_FOUND)
					.entity(JsonUtil.buildNormalMapper().toJson(ErrorEntity.REGISTRY_LOOKUP_GENERIC_ERROR)).build();
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response
					.status(Status.NOT_FOUND)
					.entity(new ErrorEntity(ErrorEntity.UNKNOWN_ERROR_CODE, MessageUtils.getDefaultBundleString("UNKNOWN_ERROR",
							e.getLocalizedMessage()))).build();
		}
	}
}
