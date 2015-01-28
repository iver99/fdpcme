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

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.JsonUtil;

/**
 * @author wenjzhu
 */
public class APIBase
{
	@Context
	protected UriInfo uriInfo;
	private final JsonUtil jsonUtil;

	public APIBase()
	{
		super();
		jsonUtil = JsonUtil.buildNonNullMapper();
		jsonUtil.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}

	public Response buildErrorResponse(ErrorEntity error)
	{
		if (error == null) {
			return null;
		}
		return Response.status(error.getStatusCode()).entity(getJsonUtil().toJson(error)).build();
	}

	public JsonUtil getJsonUtil()
	{
		return jsonUtil;
	}

	public Long getTenantId(String tenantId) throws CommonSecurityException, BasicServiceMalfunctionException
	{
		if (tenantId == null || "".equals(tenantId)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.X_USER_IDENTITY_DOMAIN_REQUIRED));
		}
		// TODO: once the cloud environment is available, return the queried tenant id instead
		//		long internalTenantId = TenantIdProcessor.getInternalTenantIdFromOpcTenantId(tenantId);
		long internalTenantId = 1L;
		return internalTenantId;
	}

	/*
	 * Updates the specified dashboard by generating all href fields
	 */
	protected Dashboard updateDashboardHref(Dashboard dbd)
	{
		if (dbd == null) {
			return null;
		}
		String href = uriInfo.getBaseUri() + "v1/dashboards/" + dbd.getDashboardId();
		dbd.setHref(href);
		return dbd;
	}
}
