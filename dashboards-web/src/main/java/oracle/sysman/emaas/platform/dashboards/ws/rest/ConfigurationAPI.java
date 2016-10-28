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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.EntityNamingDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.RegistrationEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.UserInfoEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miao
 * @author guobaochen moving registry APIs from DF UI to DF API project
 */
@Path("/v1/configurations")
public class ConfigurationAPI extends APIBase
{
	private static final Logger _LOGGER = LogManager.getLogger(ConfigurationAPI.class);

	@Path("/registration")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiscoveryConfigurations(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@HeaderParam(value = "SESSION_EXP") String sessionExpiryTime)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/configurations/registration");

		try {
			initializeUserContext(tenantIdParam, userTenant);
			String regEntity = JsonUtil.buildNormalMapper().toJson(new RegistrationEntity(sessionExpiryTime));
			_LOGGER.info("Response for [GET] /v1/configurations/registration is \"{}\"", regEntity);
			Response resp = Response.status(Status.OK).entity(regEntity).build();
			return resp;

		}
		catch (DashboardException e) {
			_LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
	}

	@Path("/userInfo")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRolesAndPriviledges(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
											   @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
											   @HeaderParam(value = "SESSION_EXP") String sessionExpiryTime)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/configurations/userInfo");
		try {
			initializeUserContext(tenantIdParam, userTenant);
			if (!DependencyStatus.getInstance().isEntityNamingUp())  {
				_LOGGER.error("Error to call [GET] /v1/configurations/roles: EntityNaming service is down");
				throw new EntityNamingDependencyUnavailableException();
			}
            String userInfoEntity = JsonUtil.buildNormalMapper().toJson(new UserInfoEntity());
			Response resp = Response.status(Status.OK)
					.entity(JsonUtil.buildNormalMapper().toJson(userInfoEntity)).build();
			return resp;

		}
		catch (DashboardException e) {
			_LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
	}
}
