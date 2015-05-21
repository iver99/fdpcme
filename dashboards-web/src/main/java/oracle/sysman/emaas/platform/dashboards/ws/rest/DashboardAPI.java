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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardConstants;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.DeleteSystemDashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author wenjzhu
 * @author guobaochen introduce API to query single dashboard by id, and update specified dashboard
 */
@Path("/v1/dashboards")
public class DashboardAPI extends APIBase
{
	private static final Logger logger = LogManager.getLogger(DashboardAPI.class);

	public DashboardAPI()
	{
		super();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, JSONObject dashboard)
	{
		try {
			Dashboard d = getJsonUtil().fromJson(dashboard.toString(), Dashboard.class);
			DashboardManager manager = DashboardManager.getInstance();
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			d = manager.saveNewDashboard(d, tenantId);
			updateDashboardAllHref(d, tenantIdParam);
			return Response.status(Status.CREATED).entity(getJsonUtil().toJson(d)).build();
		}
		catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@DELETE
	@Path("{id: [1-9][0-9]*}")
	public Response deleteDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("id") Long dashboardId)
	{
		DashboardManager manager = DashboardManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			Dashboard dsb = manager.getDashboardById(dashboardId, tenantId);
			if (dsb != null && dsb.getIsSystem() != null && dsb.getIsSystem()) {
				throw new DeleteSystemDashboardException();
			}
			manager.deleteDashboard(dashboardId, tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}/screenshot")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDashboardBase64ScreenShot(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("id") Long dashboardId)
	{
		try {
			DashboardManager manager = DashboardManager.getInstance();
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			String ss = manager.getDashboardBase64ScreenShotById(dashboardId, tenantId);
			//String screenShotUrl = uriInfo.getBaseUri() + "v1/dashboards/" + dashboardId + "/screenshot";
			String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantIdParam);
			String screenShotUrl = externalBase + (externalBase.endsWith("/") ? "" : "/") + dashboardId + "/screenshot";
			return Response.ok(getJsonUtil().toJson(new ScreenShotEntity(screenShotUrl, ss))).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}

	}

	@GET
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboardById(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("id") long dashboardId) throws DashboardException
	{
		DashboardManager dm = DashboardManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			Dashboard dbd = dm.getDashboardById(dashboardId, tenantId);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboards(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @QueryParam("queryString") String queryString,
			@DefaultValue("20") @QueryParam("limit") Integer limit, @DefaultValue("0") @QueryParam("offset") Integer offset,
			@DefaultValue(DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME) @QueryParam("orderBy") String orderBy,
			@DefaultValue("0") @QueryParam("itaSetup") Integer itaSetup,
			@DefaultValue("0") @QueryParam("laSetup") Integer laSetup, @DefaultValue("0") @QueryParam("apmSetup") Integer apmSetup)
	{
		String qs = null;
		try {
			qs = queryString == null ? null : java.net.URLDecoder.decode(queryString, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);

			List<Dashboard> setupDashboards = getSetupDashboards(qs, userTenant, itaSetup, laSetup, apmSetup);
			if (offset == null || offset < 0) {
				offset = Integer.valueOf(0);
			}
			if (limit == null || limit < 0) {
				limit = Integer.valueOf(0);
			}
			int ajoffset = offset - setupDashboards.size();
			int ajlimit = limit - (ajoffset > 0 ? 0 : setupDashboards.size() - offset);

			DashboardManager manager = DashboardManager.getInstance();
			PaginatedDashboards pd = null;
			if (ajlimit > 0) {
				pd = manager.listDashboards(qs, ajoffset < 0 ? 0 : ajoffset, ajlimit, tenantId, true, orderBy, itaSetup > 0,
						laSetup > 0, apmSetup > 0);
				if (pd != null && pd.getDashboards() != null) {
					for (Dashboard d : pd.getDashboards()) {
						updateDashboardAllHref(d, tenantIdParam);
					}
				}
			}

			List<Dashboard> dashboards = new ArrayList<Dashboard>();
			long totalResutls = setupDashboards.size();
			int count = setupDashboards.size() - offset > 0 ? setupDashboards.size() - offset : 0;
			if (ajoffset < 0) {
				for (int i = setupDashboards.size() + ajoffset; i < setupDashboards.size(); i++) {
					dashboards.add(setupDashboards.get(i));
				}
			}
			if (pd != null) {
				totalResutls = totalResutls + pd.getTotalResults();
				count = count + pd.getCount();
				if (pd.getDashboards() != null) {
					dashboards.addAll(pd.getDashboards());
				}
			}

			pd = new PaginatedDashboards(totalResutls, offset, count, limit, dashboards);

			return Response.ok(getJsonUtil().toJson(pd)).build();
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @PathParam("id") long dashboardId, JSONObject inputJson)
	{
		Dashboard input = null;
		try {
			input = getJsonUtil().fromJson(inputJson.toString(), Dashboard.class);
		}
		catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		DashboardManager dm = DashboardManager.getInstance();
		try {
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(userTenant);
			input.setDashboardId(dashboardId);
			if (input.getIsSystem() != null && input.getIsSystem()) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_UPDATE_SYSTEM_DASHBOARD_ERROR));
			}
			Dashboard dbd = dm.updateDashboard(input, tenantId);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			logger.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
	}

	private boolean checkSetup(List<String> appNames, String apptype, int setup)
	{
		if (setup > 0) {
			//List<String> appNames = TenantSubscriptionUtil.getTenantSubscribedServices(userTenant);
			if (appNames != null && appNames.contains(apptype)) {
				return true;
			}
		}

		return false;
	}

	private Dashboard getApmSetupDashboard()
	{
		Dashboard d = new Dashboard();
		d.setName("Application Performance Monitoring");
		//d.setDescription("Application Performance Monitoring");
		return d;
	}

	private Dashboard getItaSetupDashboard(int itaSetup)
	{
		Dashboard d = new Dashboard();
		d.setName("ITA Dashboard");
		//d.setDescription("ITA Dashboard");
		return d;
	}

	private Dashboard getLaSetupDashboard()
	{
		Dashboard d = new Dashboard();
		d.setName("Log Analytics");
		//d.setDescription("Log Analytics");
		return d;
	}

	private List<Dashboard> getSetupDashboards(String query, String userTenant, int itaSetup, int laSetup, int apmSetup)
	{
		List<Dashboard> setupDashboards = new ArrayList<Dashboard>();

		List<String> appNames = TenantSubscriptionUtil.getTenantSubscribedServices(userTenant);
		boolean itaInclude = checkSetup(appNames, DashboardApplicationType.ITA_SRING, itaSetup);
		boolean laInclude = checkSetup(appNames, DashboardApplicationType.LA_STRING, laSetup);
		boolean apmInclude = checkSetup(appNames, DashboardApplicationType.APM_STRING, apmSetup);

		if (itaInclude) {
			Dashboard itad = getItaSetupDashboard(itaSetup);
			if (includeSetupDashboard(query, itad)) {
				setupDashboards.add(itad);
			}
		}

		if (laInclude) {
			Dashboard lad = getLaSetupDashboard();
			if (includeSetupDashboard(query, lad)) {
				setupDashboards.add(lad);
			}
		}

		if (apmInclude) {
			Dashboard apmd = getApmSetupDashboard();
			if (includeSetupDashboard(query, apmd)) {
				setupDashboards.add(apmd);
			}
		}

		return setupDashboards;
	}

	private boolean includeSetupDashboard(String query, Dashboard dashboard)
	{
		if (dashboard == null) {
			return false;
		}
		if (query == null || query.isEmpty()) {
			return true;
		}

		if (dashboard.getName() != null && dashboard.getName().toLowerCase().contains(query.toLowerCase())
				|| dashboard.getDescription() != null && dashboard.getDescription().toLowerCase().contains(query.toLowerCase())) {
			return true;
		}

		return false;
	}

	/*
	 * Updates the specified dashboard by generating all href fields
	 */
	private Dashboard updateDashboardAllHref(Dashboard dbd, String tenantName)
	{
		updateDashboardHref(dbd, tenantName);
		updateDashboardScreenshotHref(dbd, tenantName);
		return dbd;
	}

	private Dashboard updateDashboardScreenshotHref(Dashboard dbd, String tenantName)
	{
		if (dbd == null) {
			return null;
		}
		//		String screenShotUrl = uriInfo.getBaseUri() + "v1/dashboards/" + dbd.getDashboardId() + "/screenshot";
		String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantName);
		if (StringUtil.isEmpty(externalBase)) {
			return null;
		}
		String screenShotUrl = externalBase + (externalBase.endsWith("/") ? "" : "/") + dbd.getDashboardId() + "/screenshot";
		dbd.setScreenShotHref(screenShotUrl);
		return dbd;
	}
}
