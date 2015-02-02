/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web.rest.model;

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;

/**
 * @author miao
 */
public class RegistrationEntity
{
	public static final String NAME_REGISTRYUTILS = "registryUrls";
	public static final String NAME_SSF_SERVICENAME = "SavedSearch";
	public static final String NAME_SSF_VERSION = "0.1";
	public static final String NAME_DASHBOARD_API_SERVICENAME = "Dashboard-API";
	public static final String NAME_DASHBOARD_API_VERSION = "0.1";
	private String registryUrls;

	static boolean successfullyInitialized = false;
	static {
		try {
			//.initComponent() reads the default "looup-client.properties" file in class path
			//.initComponent(List<String> urls) can override the default Registry urls with a list of urls
			if (LookupManager.getInstance().getLookupClient() == null) {
				// to ensure initComponent is only called once during the entire lifecycle
				LookupManager.getInstance().initComponent();
			}
			successfullyInitialized = true;
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	//	private String ssfServiceName;
	//	private String ssfVersion;

	//	public RegistrationEntity(String regValue, String ssfServiceName, String ssfVersion)
	//	{
	//		setRegistryUrls(regValue);
	//		setSsfServiceName(ssfServiceName);
	//		setSsfVersion(ssfVersion);
	//	}

	public RegistrationEntity(String regValue)
	{
		setRegistryUrls(regValue);
	}

	/**
	 * @return the authorizationHeader
	 */
	public String getAuthToken()
	{
		return new String(RegistrationManager.getInstance().getAuthorizationToken());
	}

	/**
	 * @return the rest API end point for dashboard framework
	 * @throws Exception
	 */
	public String getDfRestApiEndPoint()
	{
		List<InstanceInfo> instances = null;
		try {
			if (!successfullyInitialized) {
				throw new Exception("did not have the lookup successfully initialized");
			}
			InstanceInfo queryInfo = InstanceInfo.Builder.newBuilder().withServiceName(NAME_DASHBOARD_API_SERVICENAME)
					.withVersion(NAME_SSF_VERSION).build();
			instances = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(queryInfo));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		String endPoint = null;
		for (InstanceInfo instance : instances) {
			List<String> endpoints = new ArrayList<String>();
			List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
			endpoints.addAll(canonicalEndpoints);
			// virtual end points contains the URLs to the service that may be reached from outside the cloud
			List<String> virtualEndpoints = instance.getVirtualEndpoints();
			endpoints.addAll(virtualEndpoints);
			if (endpoints != null && endpoints.size() > 0) {
				for (String ep : endpoints) {
					if (ep.startsWith("https://")) {
						return ep;
					}
					if (endPoint == null) {
						endPoint = ep;
					}
				}
			}
		}

		return endPoint;
	}

	/**
	 * @return the registryUrls
	 */
	public String getRegistryUrls()
	{
		return registryUrls;
	}

	/**
	 * @return the rest API end point for SSF
	 * @throws Exception
	 */
	public String getSsfRestApiEndPoint() throws Exception
	{
		List<InstanceInfo> instances = null;
		try {
			if (!successfullyInitialized) {
				throw new Exception("did not have the lookup successfully initialized");
			}
			InstanceInfo queryInfo = InstanceInfo.Builder.newBuilder().withServiceName(NAME_SSF_SERVICENAME)
					.withVersion(NAME_SSF_VERSION).build();
			instances = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(queryInfo));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		String endPoint = null;
		for (InstanceInfo instance : instances) {
			List<String> endpoints = new ArrayList<String>();
			List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
			endpoints.addAll(canonicalEndpoints);
			// virtual end points contains the URLs to the service that may be reached from outside the cloud
			List<String> virtualEndpoints = instance.getVirtualEndpoints();
			endpoints.addAll(virtualEndpoints);
			if (endpoints != null && endpoints.size() > 0) {
				for (String ep : endpoints) {
					if (ep.startsWith("https://")) {
						return ep;
					}
					if (endPoint == null) {
						endPoint = ep;
					}
				}
			}
		}

		return endPoint;
	}

	//	/**
	//	 * @return the ssfServiceName
	//	 */
	//	public String getSsfServiceName()
	//	{
	//		return ssfServiceName;
	//	}
	//
	//	/**
	//	 * @return the ssfVersion
	//	 */
	//	public String getSsfVersion()
	//	{
	//		return ssfVersion;
	//	}

	/**
	 * @param registryUrls
	 *            the registryUrls to set
	 */
	public void setRegistryUrls(String registryUrls)
	{
		this.registryUrls = registryUrls;
	}

	//	/**
	//	 * @param ssfServiceName
	//	 *            the ssfServiceName to set
	//	 */
	//	public void setSsfServiceName(String ssfServiceName)
	//	{
	//		this.ssfServiceName = ssfServiceName;
	//	}
	//
	//	/**
	//	 * @param ssfVersion
	//	 *            the ssfVersion to set
	//	 */
	//	public void setSsfVersion(String ssfVersion)
	//	{
	//		this.ssfVersion = ssfVersion;
	//	}
}
