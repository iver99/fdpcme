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
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.RegistrationEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.UserInfoEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.TenantSubscriptionsAPI.SubscribedAppsEntity;
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

	private static class CombinedBrandingBarData {
		public CombinedBrandingBarData(UserInfoEntity userInfo, RegistrationEntity registration, SubscribedAppsEntity subscribedApps) {
			this.userInfo = userInfo;
			this.registration = registration;
			this.subscribedApps = subscribedApps;
		}

		private UserInfoEntity userInfo;
		private RegistrationEntity registration;
		private SubscribedAppsEntity subscribedApps;

		public UserInfoEntity getUserInfo() {
			return userInfo;
		}

		public void setUserInfo(UserInfoEntity userInfo) {
			this.userInfo = userInfo;
		}

		public SubscribedAppsEntity getSubscribedApps() {
			return subscribedApps;
		}

		public void setSubscribedApps(SubscribedAppsEntity subscribedApps) {
			this.subscribedApps = subscribedApps;
		}

		public RegistrationEntity getRegistration() {
			return registration;
		}

		public void setRegistration(RegistrationEntity registration) {
			this.registration = registration;
		}

		public String getBrandingbarInjectedJS() {
			StringBuilder sb = new StringBuilder();
			if (userInfo != null) {
				sb.append("window._uifwk.cachedData.userInfo=");
				sb.append(JsonUtil.buildNormalMapper().toJson(userInfo));
				sb.append(";");
			}

			if (registration != null) {
				sb.append("window._uifwk.cachedData.registrations=");
				sb.append(JsonUtil.buildNormalMapper().toJson(registration));
				sb.append(";");
			}

			if (subscribedApps != null) {
				sb.append("window._uifwk.cachedData.subscribedapps=");
				sb.append(JsonUtil.buildNormalMapper().toJson(subscribedApps));
				sb.append(";");
			}
			return sb.toString();
		}
	}

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
			String regEntity = JsonUtil.buildNonNullMapper().toJson(new RegistrationEntity(sessionExpiryTime, null));
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
				_LOGGER.error("Error to call [GET] /v1/configurations/userInfo: EntityNaming service is down");
				throw new EntityNamingDependencyUnavailableException();
			}
            String userInfoEntity = JsonUtil.buildNormalMapper().toJson(new UserInfoEntity());
			_LOGGER.info("Response for [GET] /v1/configurations/userInfo is \"{}\"", userInfoEntity);
			Response resp = Response.status(Status.OK)
					.entity(userInfoEntity).build();
			return resp;

		}
		catch (DashboardException e) {
			_LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
	}

	/**
	 * This API returned a combined data for branding bar, including user info, registration data, and subscribed application for the tenant.
	 * This API is supposed to be used internally by OMC services for html data injection for PSR consideration.
	 * This API returns the js String directly containing the branding bar data, thus additional object deserialization on the caller service side is avoided.
	 *
	 * @param tenantIdParam tenant for the request, from header "X-USER-IDENTITY-DOMAIN-NAME"
	 * @param userTenant <tenant.user> for the requesst, from header "X-REMOTE-USER"
	 * @param referer referer for the request
	 * @param sessionExpiryTime session expiry time for the request, from header "SESSION_EXP"
     * @return
     */
	@Path("/brandingbardata")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryCombinedBrandingBarData(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
												 @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
												 @HeaderParam(value = "SESSION_EXP") String sessionExpiryTime)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/configurations/brandingbardata");
		try {
			initializeUserContext(tenantIdParam, userTenant);
			UserInfoEntity uie = new UserInfoEntity();
			// this ensures subscribed app data inside cache, and reused by registration data retrieval
			List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenantIdParam);
			SubscribedAppsEntity sae = apps == null ? null : new SubscribedAppsEntity(apps);
			RegistrationEntity re = new RegistrationEntity(sessionExpiryTime, uie.getUserRoles());
			CombinedBrandingBarData cbbd = new CombinedBrandingBarData(uie, re, new SubscribedAppsEntity(apps));
			String brandingBarData = cbbd.getBrandingbarInjectedJS();
			_LOGGER.info("Response for [GET] /v1/configurations/brandingbardata is \"{}\"", brandingBarData);
			Response resp = Response.status(Status.OK).entity(brandingBarData).build();
			return resp;

		}
		catch (DashboardException e) {
			_LOGGER.error(e);
			ErrorEntity ee = new ErrorEntity(e);
			return Response.status(ee.getStatusCode()).entity(JsonUtil.buildNormalMapper().toJson(ee)).build();
		}
	}
}
