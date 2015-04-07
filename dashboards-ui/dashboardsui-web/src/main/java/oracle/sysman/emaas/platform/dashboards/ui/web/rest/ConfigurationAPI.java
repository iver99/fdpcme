/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emaas.platform.dashboards.ui.web.rest.exception.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.model.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.model.RegistrationEntity;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.JsonUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miao
 */
@Path("/configurations")
public class ConfigurationAPI
{
	private static Map<String, String> getServiceManagerContent()
	{
		Map<String, String> map = new HashMap<String, String>();
		File f = new File(SERVICEMANAGER_FILE);
		Properties ps = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			ps.load(is);
			for (Map.Entry entry : ps.entrySet()) {
				String key = String.valueOf(entry.getKey());
				String value = String.valueOf(entry.getValue());
				map.put(key, value);
			}
			return map;
		}
		catch (Exception e) {
			//TODO
			e.printStackTrace();
			return null;
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}

	private static Logger _logger = LogManager.getLogger(ConfigurationAPI.class);
	private static final String SERVICEMANAGER_FILE = "/opt/ORCLemaas/Applications/DashboardService-UI/init/servicemanager.properties";

	private static Response responseError = null;
	private static final Response responseRegistrationError = Response.status(Status.NOT_FOUND)
			.entity(JsonUtil.buildNormalMapper().toJson(ErrorEntity.CONFIGURATIONS_REGISTRATION_ERROR)).build();
	private static final Response responseRegisgtryUrlsNotFound = Response.status(Status.NOT_FOUND)
			.entity(JsonUtil.buildNormalMapper().toJson(ErrorEntity.CONFIGURATIONS_REGISTRATION_REGISTRYURLS_NOT_FOUND_ERROR))
			.build();

	static {
		Map<String, String> svMap = ConfigurationAPI.getServiceManagerContent();
		if (svMap == null) {
			responseError = responseRegistrationError;
			_logger.error("servicemanager.properties is empty");
		}
		else if (!svMap.containsKey(RegistrationEntity.NAME_REGISTRYUTILS)) {
			responseError = responseRegisgtryUrlsNotFound;
			_logger.error("required key: [registryUrls] is missing in servicemanager.properties");
		}

		else {
			//do nothing
		}
	}

	@Path("/registration")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiscoveryConfigurations(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant)
	{
		if (responseError != null) {
			return responseError; //need redeployment to remove error with fix
		}
		try {
			initializeUserTenantContext(userTenant);
			Response resp = Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(new RegistrationEntity()))
					.build();
			return resp;

		}
		catch (DashboardException e) {
			return Response.status(Status.BAD_REQUEST).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e))).build();
		}
	}

	private void initializeUserTenantContext(String userTenant) throws CommonSecurityException
	{
		if (userTenant == null || "".equals(userTenant)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		int idx = userTenant.indexOf(".");
		if (idx <= 0) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		String userName = userTenant.substring(idx + 1, userTenant.length());
		if (userName == null || "".equals(userName)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		String tenantName = userTenant.substring(0, idx);
		if (tenantName == null || "".equals(tenantName)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		TenantContext.setCurrentTenant(tenantName);
	}
}
