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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.model.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.EndpointEntity;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miao
 */
@Path("/registry")
public class RegistryLookupAPI extends AbstractAPI
{
	private static Logger logger = LogManager.getLogger(RegistryLookupAPI.class);

	@Path("/lookup/endpoint")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRegistryLink(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @QueryParam("serviceName") String serviceName,
			@QueryParam("version") String version)
	{
		try {
			validateInitializeTenantIdUserName(tenantIdParam, userTenant);
			if (StringUtil.isEmpty(serviceName) || StringUtil.isEmpty(version)) {
				ErrorEntity error = new ErrorEntity(ErrorEntity.REGISTRY_LOOKUP_ENDPOINT_NOT_FOUND_ERROR_CODE,
						MessageUtils.getDefaultBundleString("REGISTRY_LOOKUP_ENDPOINT_NOT_FOUND_ERROR", serviceName, version));
				return Response.status(Status.NOT_FOUND).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
			}
			EndpointEntity endPoint = RegistryLookupUtil.getServiceExternalEndPoint(serviceName, version, tenantIdParam);
			if (endPoint != null) {
				endPoint = RegistryLookupUtil.replaceWithVanityUrl(endPoint, tenantIdParam, serviceName);
				return Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(endPoint)).build();
			}
			else {
				ErrorEntity error = new ErrorEntity(ErrorEntity.REGISTRY_LOOKUP_ENDPOINT_NOT_FOUND_ERROR_CODE,
						MessageUtils.getDefaultBundleString("REGISTRY_LOOKUP_ENDPOINT_NOT_FOUND_ERROR",
								getSafeOutputString(serviceName), getSafeOutputString(version)));
				return Response.status(Status.NOT_FOUND).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
			}
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(ErrorEntity.UNKNOWN_ERROR_CODE, MessageUtils.getDefaultBundleString(
					"UNKNOWN_ERROR", e.getLocalizedMessage()));
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
		}
	}

	@Path("/lookup/link")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRegistryLink(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @QueryParam("serviceName") String serviceName,
			@QueryParam("version") String version, @QueryParam("rel") String rel)
	{
		try {
			validateInitializeTenantIdUserName(tenantIdParam, userTenant);
			if (StringUtil.isEmpty(serviceName) || StringUtil.isEmpty(version) || StringUtil.isEmpty(rel)) {
				ErrorEntity error = new ErrorEntity(ErrorEntity.REGISTRY_LOOKUP_LINK_NOT_FOUND_ERROR_CODE,
						MessageUtils.getDefaultBundleString("REGISTRY_LOOKUP_LINK_NOT_FOUND_ERROR",
								getSafeOutputString(serviceName), getSafeOutputString(version), getSafeOutputString(rel)));
				return Response.status(Status.NOT_FOUND).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
			}

			Link lk = RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel, tenantIdParam);
			lk = RegistryLookupUtil.replaceWithVanityUrl(lk, tenantIdParam, serviceName);
			if (lk != null) {
				return Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(lk)).build();
			}
			else {
				ErrorEntity error = new ErrorEntity(ErrorEntity.REGISTRY_LOOKUP_LINK_NOT_FOUND_ERROR_CODE,
						MessageUtils.getDefaultBundleString("REGISTRY_LOOKUP_LINK_NOT_FOUND_ERROR",
								getSafeOutputString(serviceName), getSafeOutputString(version), getSafeOutputString(rel)));
				return Response.status(Status.NOT_FOUND).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
			}
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(ErrorEntity.UNKNOWN_ERROR_CODE, MessageUtils.getDefaultBundleString(
					"UNKNOWN_ERROR", e.getLocalizedMessage()));
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
		}
	}

	@Path("/lookup/linkWithRelPrefix")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRegistryLinkWithRelPrefix(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @QueryParam("serviceName") String serviceName,
			@QueryParam("version") String version, @QueryParam("rel") String rel)
	{
		try {
			validateInitializeTenantIdUserName(tenantIdParam, userTenant);
			if (StringUtil.isEmpty(serviceName) || StringUtil.isEmpty(version) || StringUtil.isEmpty(rel)) {
				ErrorEntity error = new ErrorEntity(ErrorEntity.REGISTRY_LOOKUP_LINK_WIT_REL_PREFIX_NOT_FOUND_ERROR_CODE,
						MessageUtils.getDefaultBundleString("REGISTRY_LOOKUP_LINK_WIT_REL_PREFIX_NOT_FOUND_ERROR",
								getSafeOutputString(serviceName), getSafeOutputString(version), getSafeOutputString(rel)));
				return Response.status(Status.NOT_FOUND).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
			}
			Link lk = RegistryLookupUtil.getServiceExternalLinkWithRelPrefix(serviceName, version, rel, tenantIdParam);
			lk = RegistryLookupUtil.replaceWithVanityUrl(lk, tenantIdParam, serviceName);
			if (lk != null) {
				return Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(lk)).build();
			}
			else {
				ErrorEntity error = new ErrorEntity(ErrorEntity.REGISTRY_LOOKUP_LINK_WIT_REL_PREFIX_NOT_FOUND_ERROR_CODE,
						MessageUtils.getDefaultBundleString("REGISTRY_LOOKUP_LINK_WIT_REL_PREFIX_NOT_FOUND_ERROR",
								getSafeOutputString(serviceName), getSafeOutputString(version), getSafeOutputString(rel)));
				return Response.status(Status.NOT_FOUND).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
			}
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(ErrorEntity.UNKNOWN_ERROR_CODE, MessageUtils.getDefaultBundleString(
					"UNKNOWN_ERROR", e.getLocalizedMessage()));
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(JsonUtil.buildNormalMapper().toJson(error)).build();
		}
	}

	private String getSafeOutputString(String input)
	{
		if (input == null) {
			return "null";
		}
		else {
			return input.replaceAll("&", "&amp;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;")
					.replaceAll("<", "&lt;");
		}
	}
}
