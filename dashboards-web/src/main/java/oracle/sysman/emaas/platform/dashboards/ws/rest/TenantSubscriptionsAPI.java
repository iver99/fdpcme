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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientHandlerException;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.EntityNamingDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.TenantWithoutSubscriptionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition.ServiceEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition.TenantDetailEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition.TenantEditionEntity;

import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * @author guobaochen
 */
@Path("/v1/subscribedapps")
public class TenantSubscriptionsAPI extends APIBase
{
	public static class SubscribedAppsEntity<E>
	{
		private List<E> applications;

		public SubscribedAppsEntity(List<E> apps)
		{
			applications = apps;
		}

		public List<E> getApplications()
		{
			return applications;
		}

		public void setApplications(List<E> applications)
		{
			this.applications = applications;
		}
	}

	private static final String APPLICATION_STATUS_ONBORDED = "TENANT_ONBOARDED";
	private static final String TENANT_SUBSCRIPTION_UPDATED = "TENANT_SUBSCRIPTION_UPDATED";

	private static final Logger LOGGER = LogManager.getLogger(TenantSubscriptionsAPI.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubscribedApplications(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@QueryParam("withEdition") String withEdition)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/subscribedapps?withEdition={}",
				withEdition);
		if (withEdition != null && ("true").equalsIgnoreCase(withEdition)) { // subscriptions with edition
			return getSubscribedApplicationsWithEdition(tenantIdParam, userTenant);
		}

		// handling normal requests without edition
		try {
//			if (!DependencyStatus.getInstance().isEntityNamingUp())  {
//				LOGGER.error("Error to call [GET] /v1/subscribedapps?withEdition={}: EntityNaming service is down", withEdition);
//				throw new EntityNamingDependencyUnavailableException();
//			}
			initializeUserContext(tenantIdParam, userTenant);
			String tenantName = TenantContext.getCurrentTenant();
			List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenantName);
			if (apps == null || apps.isEmpty()) {
				throw new TenantWithoutSubscriptionException();
			}
			SubscribedAppsEntity<String> sae = new SubscribedAppsEntity<String>(apps);
			return Response.ok(getJsonUtil().toJson(sae)).build();
		}
		catch (CommonSecurityException e) {
			LOGGER.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (TenantWithoutSubscriptionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch(DashboardException e){
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	private Response getSubscribedApplicationsWithEdition(String tenantIdParam, String userTenant)
	{
		try {
			initializeUserContext(tenantIdParam, userTenant);
			String tenantName = TenantContext.getCurrentTenant();
			// normal behavior here
			Link tenantsLink = RegistryLookupUtil.getServiceInternalLink("TenantService", "1.0+", "collection/tenants", null);
			if (tenantsLink == null || tenantsLink.getHref() == null || "".equals(tenantsLink.getHref())) {
				throw new TenantWithoutSubscriptionException();
			}
			LOGGER.debug("Checking tenant (" + tenantName + ") subscriptions with edition. The tenant service href is "
					+ tenantsLink.getHref());
			String tenantHref = tenantsLink.getHref() + "/" + tenantName;
			RestClient rc = new RestClient();
			rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME",tenantName);
			String tenantResponse = null;
			try{
				tenantResponse = rc.get(tenantHref, tenantName);
			}catch(UniformInterfaceException e){
				LOGGER.error("Error occurred: status code of the HTTP response indicates a response that is not expected");
				LOGGER.error(e);
			}catch(ClientHandlerException e){//RestClient may timeout, so catch this runtime exception to make sure the response can return.
				LOGGER.error("Error occurred: Signals a failure to process the HTTP request or HTTP response");
				LOGGER.error(e);
			}
			LOGGER.debug("Checking tenant (" + tenantName + ") subscriptions with edition. Tenant response is " + tenantResponse);
			JsonUtil ju = JsonUtil.buildNormalMapper();
			TenantDetailEntity de = ju.fromJson(tenantResponse, TenantDetailEntity.class);
			if (de == null || de.getServices() == null) {
				throw new TenantWithoutSubscriptionException();
			}
			List<TenantEditionEntity> teeList = new ArrayList<TenantEditionEntity>();
			for (ServiceEntity se : de.getServices()) {
				LOGGER.debug(
						"Get one subscribed application for tenant {}: name - \"{}\", serviceType - \"{}\", edition - \"{}\", editionUUID - \"{}\", status - \"{}\", serviceId - \"{}\"",
						tenantName, se.getServiceName(), se.getServiceType(), se.getEdition(), se.getEditionUUID(),
						se.getStatus(), se.getServiceId());
				// only application in state of onboarded or subscription updated are valid
				if (!APPLICATION_STATUS_ONBORDED.equals(se.getStatus()) && !TENANT_SUBSCRIPTION_UPDATED.equals(se.getStatus())) {
					LOGGER.debug("This application is ignored as it's status is \"{}\"", se.getStatus());
					continue;
				}
				TenantEditionEntity tee = new TenantEditionEntity(se.getServiceType(), se.getEdition());
				teeList.add(tee);
			}
			SubscribedAppsEntity<TenantEditionEntity> sae = new SubscribedAppsEntity<TenantEditionEntity>(teeList);
			return Response.ok(getJsonUtil().toJson(sae)).build();
		}
		catch (CommonSecurityException | TenantWithoutSubscriptionException e) {
			LOGGER.error(e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (IOException | UniformInterfaceException e) {
			LOGGER.error(e);
			return buildErrorResponse(new ErrorEntity(new TenantWithoutSubscriptionException()));
		}
		finally {
			clearUserContext();
		}
	}
}
