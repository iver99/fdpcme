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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import oracle.sysman.emaas.platform.dashboards.ui.web.rest.model.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.model.RegistrationEntity;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.JsonUtil;

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

	private static RegistrationEntity registrationEntity;

	private static final String SERVICEMANAGER_FILE = "/opt/ORCLemaas/Applications/DashboardService-UI/init/servicemanager.properties";

	private static Response responseRegistration;
	private static final Response responseRegistrationError = Response.status(Status.NOT_FOUND)
			.entity(JsonUtil.buildNormalMapper().toJson(ErrorEntity.CONFIGURATIONS_REGISTRATION_ERROR)).build();
	private static final Response responseRegisgtryUrlsNotFound = Response.status(Status.NOT_FOUND)
			.entity(JsonUtil.buildNormalMapper().toJson(ErrorEntity.CONFIGURATIONS_REGISTRATION_REGISTRYURLS_NOT_FOUND_ERROR))
			.build();

	//	private static final Response responseSSFServiceNameNotFound = Response.status(Status.NOT_FOUND)
	//			.entity(JsonUtil.buildNormalMapper().toJson(ErrorEntity.CONFIGURATIONS_REGISTRATION_SSF_SERVICENAME_NOT_FOUND_ERROR))
	//			.build();
	//	private static final Response responseSSFVersionNotFound = Response.status(Status.NOT_FOUND)
	//			.entity(JsonUtil.buildNormalMapper().toJson(ErrorEntity.CONFIGURATIONS_REGISTRATION_SSF_VERSION_NOT_FOUND_ERROR))
	//			.build();
	static {
		Map<String, String> svMap = ConfigurationAPI.getServiceManagerContent();
		if (svMap == null) {
			responseRegistration = responseRegistrationError;
		}
		else if (!svMap.containsKey(RegistrationEntity.NAME_REGISTRYUTILS)) {
			responseRegistration = responseRegisgtryUrlsNotFound;
		}
		//		else if (!svMap.containsKey(RegistrationEntity.NAME_SSF_SERVICENAME)) {
		//			responseRegistration = responseSSFServiceNameNotFound;
		//		}
		//		else if (!svMap.containsKey(RegistrationEntity.NAME_SSF_VERSION)) {
		//			responseRegistration = responseSSFVersionNotFound;
		//		}
		else {
			//			String registryUrls = svMap.get(RegistrationEntity.NAME_REGISTRYUTILS);
			//			String ssfServiceName = svMap.get(RegistrationEntity.NAME_SSF_SERVICENAME);
			//			String ssfVersion = svMap.get(RegistrationEntity.NAME_SSF_VERSION);
			registrationEntity = new RegistrationEntity();
			responseRegistration = Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(registrationEntity))
					.build();
		}
	}

	@Path("/registration")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDiscoveryConfigurations()
	{
		responseRegistration = Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(registrationEntity)).build();
		return responseRegistration;
	}
}
