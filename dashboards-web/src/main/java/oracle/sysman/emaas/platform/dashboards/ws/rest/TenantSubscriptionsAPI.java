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

import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author guobaochen
 */
@Path("/v1/subscribedapps")
public class TenantSubscriptionsAPI extends APIBase
{
	public static class SubscribedAppsEntity
	{
		private String[] applications;

		public SubscribedAppsEntity(List<String> apps)
		{
			if (apps == null) {
				applications = null;
			}
			else {
				applications = (String[]) apps.toArray();
			}
		}

		public String[] getApplications()
		{
			return applications;
		}

		public void setApplications(String[] applications)
		{
			this.applications = applications;
		}
	}

	private static Logger logger = LogManager.getLogger(TenantSubscriptionsAPI.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllPreferenceByKey(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant)
	{
		try {
			initializeUserContext(userTenant);
			String tenantName = TenantContext.getCurrentTenant();
			List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenantName);
			SubscribedAppsEntity sae = new SubscribedAppsEntity(apps);
			return Response.ok(getJsonUtil().toJson(sae)).build();
		}
		catch (CommonSecurityException e) {
			logger.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

}
