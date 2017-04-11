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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.TenantWithoutSubscriptionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.subscription2.AppsInfo;
import oracle.sysman.emaas.platform.dashboards.core.model.subscription2.TenantSubscriptionInfo;
import oracle.sysman.emaas.platform.dashboards.core.util.*;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil.VersionedLink;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition.TenantEditionEntity;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        public SubscribedAppsEntity(List<E> applications, String version) {
            this.applications = applications;
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

	private static class SubscribedAppsEntityWithVersion{
		List<TenantEditionEntity> applications;

		public SubscribedAppsEntityWithVersion(List<TenantEditionEntity> list) {
			this.applications = list;
		}

        public List<TenantEditionEntity> getApplications() {
            return applications;
        }

        public void setApplications(List<TenantEditionEntity> applications) {
            this.applications = applications;
        }
    }


	private static final Logger LOGGER = LogManager.getLogger(TenantSubscriptionsAPI.class.getName());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubscribedApplications(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@QueryParam("withEdition") String withEdition)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/subscribedapps?withEdition={}",
				withEdition);

		try {
			initializeUserContext(tenantIdParam, userTenant);
			String tenantName = TenantContext.getCurrentTenant();
            TenantSubscriptionInfo tenantSubscriptionInfo = new TenantSubscriptionInfo();
			List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenantName, tenantSubscriptionInfo);
			//return subscribapps with editions
			if(Boolean.valueOf(withEdition)){
                LOGGER.info("Return subscrib apps data with edition..");
				if(tenantSubscriptionInfo.getAppsInfoList() == null || tenantSubscriptionInfo.getAppsInfoList().isEmpty()){
					throw new TenantWithoutSubscriptionException();
				}
				List<TenantEditionEntity> applications = getServicesWithEdition(tenantSubscriptionInfo);
				SubscribedAppsEntityWithVersion saw = new SubscribedAppsEntityWithVersion(applications);
				return Response.ok(getJsonUtil().toJson(saw)).build();
			}
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

	private List getServicesWithEdition(TenantSubscriptionInfo tenantSubscriptionInfo){
		if(tenantSubscriptionInfo !=null && tenantSubscriptionInfo.getAppsInfoList()!=null
				&& !tenantSubscriptionInfo.getAppsInfoList().isEmpty()){
			List<TenantEditionEntity> tenantEditionEntityList = new ArrayList<>();
			TenantEditionEntity ne = null;
			for(AppsInfo appsInfo : tenantSubscriptionInfo.getAppsInfoList()){
				ne = new TenantEditionEntity();
				if(SubsriptionAppsUtil.V2_TENANT.equals(appsInfo.getLicVersion())) {
                    ne.setApplication(appsInfo.getId());
                    ne.setEdition(pickEdition(appsInfo));
                    LOGGER.info("V2: edition info is {}", ne.getEdition());
                }
				if(SubsriptionAppsUtil.V1_TENANT.equals(appsInfo.getLicVersion())){
					//if is V1, editions List only have one edition
					ne.setEdition(appsInfo.getEditions().get(0));
					ne.setApplication(appsInfo.getId());
                    LOGGER.info("V1: edition info is {}", ne.getEdition());
				}
                if(SubsriptionAppsUtil.V3_TENANT.equals(appsInfo.getLicVersion())){
                    //if is V3, editions List only have one edition
                    ne.setEdition(appsInfo.getEditions().get(0));
                    ne.setApplication(appsInfo.getId());
                    LOGGER.info("V3: Edition info is {}",ne.getEdition());
                }
				LOGGER.info("Application with name {} and edition {} is added.",ne.getApplication(),ne.getEdition());
				tenantEditionEntityList.add(ne);

            }
            return tenantEditionEntityList;
        }

		return null;
	}

	/**
	 * pick the highest edition if have multi editions
	 * For now ,only V2 will enter this method
	 * @return
	 */
	private String pickEdition(AppsInfo appsInfo){
		if(appsInfo == null || appsInfo.getEditions()==null || appsInfo.getEditions().isEmpty()){
			LOGGER.error("Editions information is null or empty!");
			return null;
		}
        LOGGER.debug("Editions in pick Edition is {}", appsInfo.getEditions());
		String result = null;
		for(String edition : appsInfo.getEditions()){
            //Handle V2 OMC suite
			if(edition !=null && (edition.contains(SubsriptionAppsUtil.EE_EDITION) || edition.contains(SubsriptionAppsUtil.LOG_EDITION))){
                LOGGER.debug("1Picking edition...{}", edition);
				return edition;
			}
            if(edition !=null && edition.contains(SubsriptionAppsUtil.SE_EDITION)){
                result = edition;
            }
            //Handle V2 OSMACC suite
            if(edition !=null &&(edition.contains(SubsriptionAppsUtil.CONFIGURATION_COMPLIANCE_EDITION))
                    || edition.contains(SubsriptionAppsUtil.SECURITY_MONITORING_ANALYTICS_EDITION)){
                LOGGER.debug("2Picking edition...{}", edition);
                return edition;
            }
		}
        LOGGER.debug("3Picking edition...{}", result);
//		LOGGER.error("Can not pick highest Edition, returning null!");
		return result;
	}

}
