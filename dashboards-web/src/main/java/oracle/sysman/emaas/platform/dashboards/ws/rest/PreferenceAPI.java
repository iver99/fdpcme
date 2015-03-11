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
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.PreferenceManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;

import org.codehaus.jettison.json.JSONObject;

/**
 * @author wenjzhu
 */
@Path("/v1/preferences")
public class PreferenceAPI extends APIBase
{
	@DELETE
	public Response deleteAllPreferenceByKey(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant)
	{
		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			pm.removeAllPreferences(tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			e.printStackTrace();
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@DELETE
	@Path("{key}")
	//@Path("{key: [\\w\\-\\.]{1,256}}")
	public Response deletePreferenceByKey(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("key") String key)
	{
		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			pm.removePreference(key, tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			e.printStackTrace();
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@GET
	@Path("{key}")
	//@Path("{key: [\\w\\-\\.]{1,256}}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryPreferenceByKey(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("key") String key)
	{
		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			Preference input = pm.getPreferenceByKey(key, tenantId);
			return Response.ok(getJsonUtil().toJson(input)).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			e.printStackTrace();
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryPreferences(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant)
	{
		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			List<Preference> ps = pm.listPreferences(tenantId);
			if (ps != null) {
				for (Preference p : ps) {
					updatePreferenceHref(p);
				}
			}
			return Response.ok(getJsonUtil().toJson(ps)).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			e.printStackTrace();
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@PUT
	@Path("{key}")
	//@Path("{key: [\\w\\-\\.]{1,256}}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePreference(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("key") String key, JSONObject inputJson)
	{
		Preference input = null;
		try {
			input = getJsonUtil().fromJson(inputJson.toString(), Preference.class);
		}
		catch (IOException e) {
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		PreferenceManager pm = PreferenceManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			input.setKey(key);

			pm.savePreference(input, tenantId);
			updatePreferenceHref(input);
			return Response.ok(getJsonUtil().toJson(input)).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			e.printStackTrace();
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	private void updatePreferenceHref(Preference pref)
	{
		if (pref == null) {
			return;
		}
		String externalBase = DashboardAPIUtil.getExternalAPIBase();
		String url = externalBase + "preferences/" + pref.getKey();
		pref.setHref(url);
		//return pref;
	}
}
