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

import java.io.IOException;
import java.math.BigInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification.WidgetNotificationType;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification.WidgetNotifyEntity;

/**
 * @author guochen
 */
@Path("/v1/widgetnotification")
public class WidgetNotificationAPI extends APIBase
{
	private static final Logger LOGGER = LogManager.getLogger(WidgetNotificationAPI.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response notifyWidgetChangedOrDeleted(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam, JSONObject data)
	{
		try {
			infoInteractionLogAPIIncomingCall(tenantIdParam, "Saved Search Service",
					"Service call to [POST] /v1/widgetnotification \npayload is \"{}\"", data.toString());

			if (tenantIdParam == null || "".equals(tenantIdParam)) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.X_USER_IDENTITY_DOMAIN_REQUIRED));
			}
			Long tenantId = getTenantId(tenantIdParam);
			WidgetNotifyEntity wne = getJsonUtil().fromJson(data.toString(), WidgetNotifyEntity.class);
			// the return value for affacted objects in eclipse 2.4 will always be 1
			int affacted = 0;
			if (wne.getType() == null || wne.getType().equals(WidgetNotificationType.UPDATE_NAME)) {
				affacted = updateDashboardTilesName(tenantId, wne.getName(), wne.getUniqueId());
			}
			else {
				affacted = updateDashboardTilesWidgetDeleted(tenantId, wne.getUniqueId());
			}
			wne.setAffactedObjects(affacted);
			return Response.status(Status.OK).entity(getJsonUtil().toJson(wne)).build();
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	private int updateDashboardTilesName(Long tenantId, String widgetName, BigInteger widgetUniqueId)
	{
		return DashboardManager.getInstance().updateDashboardTilesName(tenantId, widgetName, widgetUniqueId);
	}

	private int updateDashboardTilesWidgetDeleted(Long tenantId, BigInteger widgetUniqueId)
	{
		return DashboardManager.getInstance().updateWidgetDeleteForTilesByWidgetId(tenantId, widgetUniqueId);
	}
}
