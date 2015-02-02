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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
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
	public static final String NAME_QUICK_LINK = "quickLink";
	public static final String NAME_VISUAL_ANALYZER = "visualAnalyzer";
	public static final String NAME_DASHBOARD_UI_SERVICENAME = "Dashboard-UI";
	public static final String NAME_DASHBOARD_UI_VERSION = "0.1";

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

	/*
	* @return Quick links discovered from service manager
	 */
	public List<LinkEntity> getQuickLinks()
	{
		return lookupLinksWithRelPrefix(NAME_QUICK_LINK);
	}

	/**
	 * @return the registryUrls
	 */
	public String getRegistryUrls()
	{
		return registryUrls;
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

	/**
	 * @return Visual analyzer links discovered from service manager
	 */
	public List<LinkEntity> getVisualAnalyzers()
	{
		return lookupLinksWithRelPrefix(NAME_VISUAL_ANALYZER);
	}

	/**
	 * @param registryUrls
	 *            the registryUrls to set
	 */
	public void setRegistryUrls(String registryUrls)
	{
		this.registryUrls = registryUrls;
	}

	private void addToLinksMap(Map<String, Link> linksMap, List<Link> links)
	{
		for (Link link : links) {
			if (!linksMap.containsKey(link.getRel())) {
				linksMap.put(link.getRel(), link);
			}
			else if (linksMap.get(link.getRel()).getHref().toLowerCase().startsWith("http://")
					&& link.getHref().toLowerCase().startsWith("https://")) {
				linksMap.put(link.getRel(), link);
			}
		}
	}

	private String getLinkName(String rel)
	{
		String name = "";
		if (rel.indexOf("/") > 0) {
			String[] relArray = rel.split("/");
			name = relArray[1];
		}

		return name;
	}

	private List<LinkEntity> lookupLinksWithRelPrefix(String linkPrefix)
	{
		List<LinkEntity> linkList = new ArrayList<LinkEntity>();

		LookupClient lookUpClient = LookupManager.getInstance().getLookupClient();
		List<InstanceInfo> instanceList = lookUpClient.getInstancesWithLinkRelPrefix(linkPrefix);
		Map<String, Link> linksMap = new HashMap<String, Link>();
		Map<String, Link> dashboardLinksMap = new HashMap<String, Link>();
		for (InstanceInfo instance : instanceList) {
			List<Link> links = instance.getLinksWithRelPrefix(linkPrefix);
			if (NAME_DASHBOARD_UI_SERVICENAME.equals(instance.getServiceName())
					&& NAME_DASHBOARD_UI_VERSION.equals(instance.getVersion())) {
				addToLinksMap(dashboardLinksMap, links);
			}
			else {
				addToLinksMap(linksMap, links);
			}
		}

		Iterator<Map.Entry<String, Link>> iterDashboardLinks = dashboardLinksMap.entrySet().iterator();
		while (iterDashboardLinks.hasNext()) {
			Map.Entry<String, Link> entry = iterDashboardLinks.next();
			Link val = entry.getValue();
			LinkEntity le = new LinkEntity(getLinkName(val.getRel()), val.getHref());
			linkList.add(le);
		}

		Iterator<Map.Entry<String, Link>> iterLinks = linksMap.entrySet().iterator();
		while (iterLinks.hasNext()) {
			Map.Entry<String, Link> entry = iterLinks.next();
			Link val = entry.getValue();
			LinkEntity le = new LinkEntity(getLinkName(val.getRel()), val.getHref());
			if (!dashboardLinksMap.containsKey(val.getRel())) {
				linkList.add(le);
			}
		}

		return linkList;
	}
}
