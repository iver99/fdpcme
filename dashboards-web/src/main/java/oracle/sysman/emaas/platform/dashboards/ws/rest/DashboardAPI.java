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

import com.sun.jersey.core.util.Base64;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardConstants;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.DashboardsFilter;
import oracle.sysman.emaas.platform.dashboards.core.UserOptionsManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DatabaseDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.UserOptionsNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.DeleteSystemDashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard.EnableDescriptionState;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard.EnableEntityFilterState;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard.EnableTimeRangeState;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.util.*;
import oracle.sysman.emaas.platform.dashboards.webutils.ParallelThreadPool;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.RegistrationEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.UserInfoEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.*;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.ScreenshotPathGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.concurrent.*;

/**
 * @author wenjzhu
 * @author guobaochen introduce API to query single dashboard by id, and update specified dashboard
 */
@Path("/v1/dashboards")
public class DashboardAPI extends APIBase
{
	private static final Logger LOGGER = LogManager.getLogger(DashboardAPI.class);

	public DashboardAPI()
	{
		super();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			JSONObject dashboard)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [POST] /v1/dashboards");
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [POST] /v1/dashboards: database is down");
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("createDashboard()", userTenant, tenantIdParam);
			Dashboard d = getJsonUtil().fromJson(dashboard.toString(), Dashboard.class);
			DashboardManager manager = DashboardManager.getInstance();
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			d = manager.saveNewDashboard(d, tenantId);
			updateDashboardAllHref(d, tenantIdParam);
			return Response.status(Status.CREATED).entity(getJsonUtil().toJson(d)).build();
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
			//e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@DELETE
	@Path("{id: [1-9][0-9]*}")
	public Response deleteDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [DELETE] /v1/dashboards/{}", dashboardId);
		DashboardManager manager = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [DELETE] /v1/dashboards/{}: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("deleteDashboard()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Dashboard dsb = manager.getDashboardById(dashboardId, tenantId);
			if (dsb != null && dsb.getIsSystem() != null && dsb.getIsSystem()) {
				throw new DeleteSystemDashboardException();
			}
			manager.deleteDashboard(dashboardId, tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			//e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}
	
	@DELETE
	@Path("all")
	//@Path("tenant/{id: [1-9][0-9]*}")  
	public Response deleteDashboards(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer)
			//@PathParam("id") Long tenantId)  
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [DELETE] /v1/dashboards/all");
		DashboardManager manager = DashboardManager.getInstance();
		try {
			logkeyHeaders("deleteDashboardsByTenant()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			manager.deleteDashboards(tenantId);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (DashboardException e) {
			return buildErrorResponse(new ErrorEntity(e));
		} catch (BasicServiceMalfunctionException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	//	@GET
	//	@Path("{id: [1-9][0-9]*}/screenshot")
	//	@Produces(MediaType.APPLICATION_JSON)
	//	public Response getDashboardBase64ScreenShot(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
	//			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
	//			@PathParam("id") Long dashboardId)
	//	{
	//		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}/screenshot",
	//				dashboardId);
	//		try {
	//			logkeyHeaders("getDashboardBase64ScreenShot()", userTenant, tenantIdParam);
	//			DashboardManager manager = DashboardManager.getInstance();
	//			Long tenantId = getTenantId(tenantIdParam);
	//			initializeUserContext(tenantIdParam, userTenant);
	//			ScreenshotData ss = manager.getDashboardBase64ScreenShotById(dashboardId, tenantId);
	//			//String screenShotUrl = uriInfo.getBaseUri() + "v1/dashboards/" + dashboardId + "/screenshot";
	//			String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantIdParam);
	//			String screenShotUrl = externalBase + (externalBase.endsWith("/") ? "" : "/") + dashboardId + "/screenshot";
	//			return Response.ok(getJsonUtil().toJson(new ScreenShotEntity(screenShotUrl, ss.getScreenshot()))).build();
	//		}
	//		catch (DashboardException e) {
	//			return buildErrorResponse(new ErrorEntity(e));
	//		}
	//		catch (BasicServiceMalfunctionException e) {
	//			//e.printStackTrace();
	//			LOGGER.error(e.getLocalizedMessage(), e);
	//			return buildErrorResponse(new ErrorEntity(e));
	//		}
	//		finally {
	//			clearUserContext();
	//		}
	//
	//	}

	@GET
	@Path("{id: [1-9][0-9]*}/screenshot/{serviceVersion}/images/{fileName}")
	@Produces("image/png")
	public Object getDashboardScreenShot(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, @PathParam("serviceVersion") String serviceVersion,
			@PathParam("fileName") String fileName)
	{
		ICacheManager scm= CacheManagers.getInstance().build(CacheConstants.LRU_SCREENSHOT_MANAGER);
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer,
				"Service call to [GET] /v1/dashboards/{}/screenshot/{}/images/{}", dashboardId, serviceVersion, fileName);

		logkeyHeaders("getDashboardScreenShot()", userTenant, tenantIdParam);
		DashboardManager manager = DashboardManager.getInstance();
		Long tenantId = null;
		try {
			tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
//		ScreenshotCacheManager scm = ScreenshotCacheManager.getInstance();
		Tenant cacheTenant = new Tenant(tenantId, TenantContext.getCurrentTenant());
		CacheControl cc = new CacheControl();
		cc.setMaxAge(2592000); //browser side keeps screenshot image in cache for 30 days
		//try to get from cache
		try {
//			final ScreenshotElement se = scm.getScreenshotFromCache(cacheTenant, dashboardId, fileName);
			if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
				LOGGER.warn("Unexpected dashboard id to get screenshot from cache for tenant={}, dashboard id={}, fileName={}",
						cacheTenant, dashboardId, fileName);
			}
			if (StringUtil.isEmpty(fileName)) {
				LOGGER.warn("Unexpected empty screenshot file name for tenant={}, dashboard id={}", cacheTenant, dashboardId);
			}
			final ScreenshotElement se = (ScreenshotElement) scm.getCache(CacheConstants.CACHES_SCREENSHOT_CACHE).get(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(dashboardId)));
			if (se != null) {
				if (fileName.equals(se.getFileName())) {
					return Response.ok(new StreamingOutput() {
						/* (non-Javadoc)
						 * @see javax.ws.rs.core.StreamingOutput#write(java.io.OutputStream)
						 */
						@Override
						public void write(OutputStream os) throws IOException, WebApplicationException
						{
							os.write(se.getBuffer().getData());
//							se.getBuffer().writeTo(os);
							os.flush();
							os.close();
						}

					}).cacheControl(cc).build();
				}
				else { // invalid screenshot file name
					if (!ScreenshotPathGenerator.getInstance().validFileName(dashboardId, fileName, se.getFileName())) {
						LOGGER.error("The requested screenshot file name {} for tenant={}, dashboard id={} is not a valid name",
								fileName, TenantContext.getCurrentTenant(), dashboardId, se.getFileName());
						return Response.status(Status.NOT_FOUND).build();
					}
					LOGGER.debug("The request screenshot file name is not equal to the file name in cache, but it is valid. "
							+ "Try to query from database to see if screenshot is actually updated already");
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Exception when getting screenshot from cache. Continue to get from database", e);
		}
		//try to get from persist layer
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards/{}/screenshot/{}/images/{}: database is down", dashboardId, serviceVersion, fileName);
				throw new DatabaseDependencyUnavailableException();
			}
			final ScreenshotData ss = manager.getDashboardBase64ScreenShotById(dashboardId, tenantId);
			if (ss == null || ss.getScreenshot() == null) {
				LOGGER.error("Does not retrieved base64 screenshot data");
				return Response.status(Status.NOT_FOUND).build();
			}
//			final ScreenshotElement se = scm.storeBase64ScreenshotToCache(cacheTenant, dashboardId, ss);
			String newFileName = ScreenshotPathGenerator.getInstance().generateFileName(dashboardId, ss.getCreationDate(), ss.getModificationDate());
			byte[] decoded = null;
			if (ss.getScreenshot().startsWith(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX)) {
				decoded = Base64.decode(ss.getScreenshot().substring(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX.length()));
			}
			else if (ss.getScreenshot().startsWith(DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX)) {
				decoded = Base64.decode(ss.getScreenshot().substring(DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX.length()));
			}
			else {
				LOGGER.debug("Failed to retrieve screenshot decoded bytes as the previs isn't supported");
				decoded=null;
			}
			Binary bin = new Binary(decoded);
			ScreenshotElement nse = new ScreenshotElement(newFileName, bin);
			scm.getCache(CacheConstants.CACHES_SCREENSHOT_CACHE).put(DefaultKeyGenerator.getInstance().generate(cacheTenant,new Keys(dashboardId)),nse);
			if (nse == null || nse.getBuffer() == null) {
				LOGGER.debug("Does not retrieved base64 screenshot data after store to cache. return 404 then");
				return Response.status(Status.NOT_FOUND).build();
			}
			if (!fileName.equals(nse.getFileName())) {
				LOGGER.error("The requested screenshot file name {} for tenant={}, dashboard id={} does not exist", fileName,
						TenantContext.getCurrentTenant(), dashboardId, nse.getFileName());
				return Response.status(Status.NOT_FOUND).build();
			}
			LOGGER.debug(
					"Retrieved screenshot data from persistence layer, and build response now. Data is {}" + ss.getScreenshot());
			return Response.ok(new StreamingOutput() {
				/* (non-Javadoc)
				 * @see javax.ws.rs.core.StreamingOutput#write(java.io.OutputStream)
				 */
				@Override
				public void write(OutputStream os) throws IOException, WebApplicationException
				{
					byte[] decoded = null;
					if (ss.getScreenshot().startsWith(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX)) {
						decoded = Base64.decode(ss.getScreenshot().substring(
								DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX.length()));
					}
					else if (ss.getScreenshot().startsWith(DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX)) {
						decoded = Base64.decode(ss.getScreenshot().substring(
								DashboardManager.SCREENSHOT_BASE64_JPG_PREFIX.length()));
					}
					else {
						LOGGER.debug("Failed to retrieve screenshot decoded bytes as the previs isn't supported");
						decoded = Base64.decode(DashboardManager.BLANK_SCREENSHOT
								.substring(DashboardManager.SCREENSHOT_BASE64_PNG_PREFIX.length()));
					}
					os.write(decoded);
					os.flush();
					os.close();
				}

			}).cacheControl(cc).build();
		}
		catch(DashboardNotFoundException e){
			LOGGER.warn("Specific dashboard not found for id {}",dashboardId);
			return Response.status(Status.NOT_FOUND).build();
		}
		catch(DatabaseDependencyUnavailableException e){
			LOGGER.error(e.getLocalizedMessage(), e);
			return Response.status(Status.NOT_FOUND).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}/options")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDashboardUserOptions(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}/options", dashboardId);
		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards/{}/options: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			UserOptions options = userOptionsManager.getOptionsById(dashboardId, tenantId);
			return Response.ok(getJsonUtil().toJson(options)).build();
		}
		catch (UserOptionsNotFoundException e){
			LOGGER.warn("Specific User Option is not found for dashboard id {}", dashboardId);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardNotFoundException e){
			LOGGER.warn("Specific dashboard is not found for dashboard id {}", dashboardId);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboardById(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}", dashboardId);
		DashboardManager dm = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards/{}: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("queryDashboardById()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			String userName = UserContext.getCurrentUser();
			Dashboard dbd = dm.getCombinedDashboardById(dashboardId, tenantId, userName);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch(DashboardNotFoundException e){
			//suppress error information in log file
			LOGGER.warn("Specific dashboard not found for id {}", dashboardId);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}
	@GET
	@Path("{id: [1-9][0-9]*}/combinedData")
	@Produces(MediaType.TEXT_PLAIN)
	public Response queryCombinedData(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") final String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") final String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") final BigInteger dashboardId,@HeaderParam(value = "SESSION_EXP") final String sessionExpiryTime)
	{
		final long TIMEOUT=5000;
		Long begin=System.currentTimeMillis();
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}", dashboardId);
		final DashboardManager dm = DashboardManager.getInstance();
		StringBuilder sb=new StringBuilder();
		ExecutorService pool = ParallelThreadPool.getThreadPool();

		Dashboard dbd =null;
		Future<Dashboard> futureDashboard=null;
		Future<String> futureUserInfo =null;
		Future<String> futureReg =null;
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards/{}/combinedData: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("combinedData()", userTenant, tenantIdParam);
			
			futureDashboard = pool.submit(new Callable<Dashboard>() {
				@Override
				public Dashboard call() throws Exception {
					LOGGER.info("Parallel request dashboard data info...");
					Long tenantId = getTenantId(tenantIdParam);
					initializeUserContext(tenantIdParam, userTenant);
					String userName = UserContext.getCurrentUser();
					return dm.getCombinedDashboardById(dashboardId, tenantId, userName);
				}
			});
		}
		
		catch (DashboardException e) {
			LOGGER.error(e.getStackTrace());
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
		//retrieve user info
		String userInfoEntity = null;
		futureUserInfo= pool.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					LOGGER.info("Parallel request user info...");
					initializeUserContext(tenantIdParam, userTenant);
					return JsonUtil.buildNormalMapper().toJson(new UserInfoEntity());
				}
		});

		//retrieve registration info
		String regEntity=null;
			futureReg = pool.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					LOGGER.info("Parallel request registry info...");
					initializeUserContext(tenantIdParam, userTenant);
					return JsonUtil.buildNonNullMapper().toJson(new RegistrationEntity(sessionExpiryTime));
				}
			});
		//get data
		try {
			if(futureReg!=null){
				regEntity = futureReg.get(TIMEOUT, TimeUnit.MILLISECONDS);
				if(regEntity !=null && !StringUtils.isEmpty(regEntity)){
					sb.append("window._registrationServerCache=");
					sb.append(regEntity).append(";");
				}
				LOGGER.debug("Registration data is "+regEntity);
			}
		} catch (InterruptedException e) {
			LOGGER.error(e.getStackTrace());
		} catch (ExecutionException e) {
			LOGGER.error(e.getCause().getStackTrace());
		}catch(TimeoutException e){
			LOGGER.error(e.getStackTrace());
		}

		try {
			if (futureUserInfo != null) {
				userInfoEntity = futureUserInfo.get(TIMEOUT, TimeUnit.MILLISECONDS);
				if (userInfoEntity != null && !StringUtils.isEmpty(userInfoEntity)) {
					sb.append("window._userInfoServerCache=");
					sb.append(userInfoEntity).append(";");
				}
				LOGGER.debug("User info data is " + regEntity);
			}
		} catch (InterruptedException e) {
			LOGGER.error(e.getStackTrace());
		} catch (ExecutionException e) {
			LOGGER.error(e.getCause().getStackTrace());
		}catch(TimeoutException e){
			LOGGER.error(e.getStackTrace());
		}

		try {
			if(futureDashboard!=null){
				dbd = futureDashboard.get(TIMEOUT, TimeUnit.MILLISECONDS);
				if(dbd !=null){
					sb.append("window._dashboardServerCache=");
					sb.append(getJsonUtil().toJson(dbd)).append(";");
				}
				LOGGER.debug("Dashboard data is " + getJsonUtil().toJson(dbd));
				updateDashboardAllHref(dbd, tenantIdParam);
			}
		} catch (ExecutionException e) {
			LOGGER.error(e.getCause().getStackTrace());
		}catch (InterruptedException e) {
			LOGGER.error(e.getStackTrace());
		}catch(TimeoutException e){
			LOGGER.error(e.getStackTrace());
		}

		LOGGER.info("Retrieving combined data cost {}ms",(System.currentTimeMillis()-begin));
		return Response.ok(sb.toString()).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboards(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@QueryParam("queryString") String queryString, @DefaultValue("") @QueryParam("limit") Integer limit,
			@DefaultValue("0") @QueryParam("offset") Integer offset,
			@DefaultValue(DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME) @QueryParam("orderBy") String orderBy,
			@QueryParam("filter") String filterString
	/*@QueryParam("types") String types, @QueryParam("appTypes") String appTypes, @QueryParam("owners") String owners,
	@QueryParam("onlyFavorites") Boolean onlyFavorites*/)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer,
				"Service call to [GET] /v1/dashboards?queryString={}&limit={}&offset={}&orderBy={}&filter={}", queryString,
				limit, offset, orderBy, filterString);
		logkeyHeaders("queryDashboards()", userTenant, tenantIdParam);
		String qs = null;
		try {
			//emcpdf-3012
			qs = queryString == null ? null : java.net.URLDecoder.decode(queryString.replaceAll("%", "\\%25"), "UTF-8").replace("%", "\\%");
		}
		catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards?queryString={}&limit={}&offset={}&orderBy={}&filter={}: database is down", queryString, limit,
						offset, orderBy, filterString);
				throw new DatabaseDependencyUnavailableException();
			}
			DashboardManager manager = DashboardManager.getInstance();
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			DashboardsFilter filter = new DashboardsFilter();
			//filter.setIncludedAppsFromString(appTypes);
			//filter.setIncludedOwnersFromString(owners);
			//filter.setIncludedTypesFromString(types);
			//filter.setIncludedFavorites(onlyFavorites);
			filter.initializeFilters(filterString);
			PaginatedDashboards pd = manager.listDashboards(qs, offset, limit, tenantId, true, orderBy, filter);
			if (pd != null && pd.getDashboards() != null) {
				for (Dashboard d : pd.getDashboards()) {
					updateDashboardAllHref(d, tenantIdParam);
				}
			}
			return Response.ok(getJsonUtil().toJson(pd)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}/quickUpdate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response quickUpdateDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/{}/quickUpdate",
				dashboardId);
		logkeyHeaders("quickUpdateDashboard()", userTenant, tenantIdParam);
		String name = null;
		String description = null;
		String enableDescription = null;
		String enableEntityFilter = null;
		String enableTimeRange = null;
		Boolean share = null;
		try {
			if (inputJson.has("name")) {
				name = inputJson.getString("name");
			}
			if (inputJson.has("description")) {
				description = inputJson.getString("description");
			}
			if (inputJson.has("enableDescription")) {
				enableDescription = inputJson.getString("enableDescription");
			}
			if (inputJson.has("enableEntityFilter")) {
				enableEntityFilter = inputJson.getString("enableEntityFilter");
			}
			if (inputJson.has("enableTimeRange")) {
				enableTimeRange = inputJson.getString("enableTimeRange");
			}

			if (inputJson.has("sharePublic")) {
				share = inputJson.getBoolean("sharePublic");
			}
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(new IOException("Can't parse input parameters.", e));
			return buildErrorResponse(error);
		}

		DashboardManager dm = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [PUT] /v1/dashboards/{}/quickUpdate: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Dashboard input = dm.getDashboardById(dashboardId, tenantId);
			if (input.getIsSystem() != null && input.getIsSystem()) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_UPDATE_SYSTEM_DASHBOARD_ERROR));
			}
			if (name != null) {
				input.setName(name);
			}
			if (description != null) {
				input.setDescription(description);
			}

			if (enableDescription != null) {
				input.setEnableDescription(EnableDescriptionState.fromName(enableDescription));
			}

			if (enableEntityFilter != null) {
				input.setEnableEntityFilter(EnableEntityFilterState.fromName(enableEntityFilter));
			}

			if (enableTimeRange != null) {
				input.setEnableTimeRange(EnableTimeRangeState.fromName(enableTimeRange));
			}

			if (share != null) {
				input.setSharePublic(share);
			}
			ScreenshotData screenShot = dm.getDashboardBase64ScreenShotById(dashboardId, tenantId);
			input.setScreenShot(screenShot.getScreenshot()); //set screen shot back otherwise it will be cleared
			Dashboard dbd = dm.updateDashboard(input, tenantId);
			updateDashboardAllHref(dbd, tenantIdParam);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@POST
	@Path("{id: [1-9][0-9]*}/options")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveUserOptions(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [POST] /v1/dashboards/{}/options/",
				dashboardId);
		UserOptions userOption;
		try {
			userOption = getJsonUtil().fromJson(inputJson.toString(), UserOptions.class);
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [POST] /v1/dashboards/{}/options/: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			userOption.setDashboardId(dashboardId);//override id in consumed json if exist;
			userOptionsManager.saveOrUpdateUserOptions(userOption, tenantId);
			return Response.ok(getJsonUtil().toJson(userOption)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDashboard(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/{}", dashboardId);
		logkeyHeaders("updateDashboard()", userTenant, tenantIdParam);
		Dashboard input = null;
		try {
			input = getJsonUtil().fromJson(inputJson.toString(), Dashboard.class);
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		DashboardManager dm = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [PUT] /v1/dashboards/{}: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
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
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@PUT
	@Path("{id: [1-9][0-9]*}/options")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUserOptions(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
			@PathParam("id") BigInteger dashboardId, JSONObject inputJson)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/dashboards/{}/options/", dashboardId);
		UserOptions userOption;
		try {
			userOption = getJsonUtil().fromJson(inputJson.toString(), UserOptions.class);
			userOption.setDashboardId(dashboardId);
		}
		catch (IOException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}

		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [PUT] /v1/dashboards/{}/options/: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			userOptionsManager.saveOrUpdateUserOptions(userOption, tenantId);
			return Response.ok(getJsonUtil().toJson(userOption)).build();
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		}
		finally {
			clearUserContext();
		}
	}

	@GET
	@Path("{id: [1-9][0-9]*}/dashboardsets")
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryDashboardSetsBySubId(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
									   @HeaderParam(value = "X-REMOTE-USER") String userTenant, @HeaderParam(value = "Referer") String referer,
									   @PathParam("id") BigInteger dashboardId)
	{
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [GET] /v1/dashboards/{}/dashboardsets", dashboardId);
		DashboardManager dm = DashboardManager.getInstance();
		try {
			if (!DependencyStatus.getInstance().isDatabaseUp())  {
				LOGGER.error("Error to call [GET] /v1/dashboards/{}/dashboardsets: database is down", dashboardId);
				throw new DatabaseDependencyUnavailableException();
			}
			logkeyHeaders("queryDashboardSetsBySubId()", userTenant, tenantIdParam);
			Long tenantId = getTenantId(tenantIdParam);
			initializeUserContext(tenantIdParam, userTenant);
			Dashboard dbd = dm.getDashboardSetsBySubId(dashboardId, tenantId);
			return Response.ok(getJsonUtil().toJson(dbd)).build();
		}
		catch(DashboardNotFoundException e){
			LOGGER.warn("Specific dashboard not found for id {}",dashboardId);
			return buildErrorResponse(new ErrorEntity(e));
		}
		catch (DashboardException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		} catch (BasicServiceMalfunctionException e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return buildErrorResponse(new ErrorEntity(e));
		} finally {
			clearUserContext();
		}
	}

	private void logkeyHeaders(String api, String x_remote_user, String domain_name)
	{
		LOGGER.info("Headers of " + api + ": X-REMOTE-USER=" + x_remote_user + ", X-USER-IDENTITY-DOMAIN-NAME=" + domain_name);
	}

	/*
	 * Updates the specified dashboard by generating all href fields
	 */
	private Dashboard updateDashboardAllHref(Dashboard dbd, String tenantName)
	{
		updateDashboardHref(dbd, tenantName);
		updateDashboardScreenshotHref(dbd, tenantName);
		updateDashboardOptionsHref(dbd, tenantName);
		if (dbd != null && dbd.getType() != null && dbd.getType().equals(Dashboard.DASHBOARD_TYPE_SET)
				&& dbd.getSubDashboards() != null) {
			for (Dashboard subDbd : dbd.getSubDashboards()) {
				updateDashboardHref(subDbd, tenantName);
			}
		}
		return dbd;
	}

	private Dashboard updateDashboardOptionsHref(Dashboard dbd, String tenantName)
	{
		if (dbd == null) {
			return null;
		}
		String externalBase = DashboardAPIUtil.getExternalDashboardAPIBase(tenantName);
		if (StringUtil.isEmpty(externalBase)) {
			return null;
		}
		String optionsUrl = externalBase + (externalBase.endsWith("/") ? "" : "/") + dbd.getDashboardId() + "/options";
		dbd.setOptionsHref(optionsUrl);
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
		String screenShotUrl = ScreenshotPathGenerator.getInstance().generateScreenshotUrl(externalBase, dbd.getDashboardId(),
				dbd.getCreationDate(), dbd.getLastModificationDate());
		LOGGER.debug("Generate screenshot URL is {} for dashboard id={}", screenShotUrl, dbd.getDashboardId());
		dbd.setScreenShotHref(screenShotUrl);
		return dbd;
	}
}
