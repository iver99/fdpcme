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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.TenantWithoutSubscriptionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.subscription2.TenantSubscriptionInfo;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.AppsInfoWeb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author chehao
 */
@Path("/v1/subscribedapps2")
public class TenantSubscriptionsAPIv2 extends APIBase
{
    public static class SubscribedAppsEntity<E>
    {
        private List<E> applications = new ArrayList<>();

        public SubscribedAppsEntity(List<E> applications) {
            this.applications = applications;
        }

        public List<E> getApplications() {
            return applications;
        }

        public void setApplications(List<E> applications) {
            this.applications = applications;
        }

    }

    private static final Logger LOGGER = LogManager.getLogger(TenantSubscriptionsAPI.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSubscribedApplications(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
                                              @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer)
    {
        infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/subscribedapps2");
        try {
            initializeUserContext(tenantIdParam, userTenant);
            String tenantName = TenantContext.getCurrentTenant();
            TenantSubscriptionInfo tenantSubscriptionInfo = new TenantSubscriptionInfo();
            List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenantName, tenantSubscriptionInfo);
            if (apps == null || apps.isEmpty()) {
                throw new TenantWithoutSubscriptionException();
            }
            List<AppsInfoWeb> list = new ArrayList<>();
            SubscribedAppsEntity<AppsInfoWeb> subscribedAppsEntity = new SubscribedAppsEntity(tenantSubscriptionInfo.getAppsInfoList());
            return Response.ok(getJsonUtil().toJson(subscribedAppsEntity)).build();
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

}
