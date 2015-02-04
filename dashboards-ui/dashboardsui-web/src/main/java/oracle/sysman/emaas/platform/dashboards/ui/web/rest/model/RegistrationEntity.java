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
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;

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

	//	public RegistrationEntity(String regValue)
	//	{
	//		setRegistryUrls(regValue);
	//	}

	//	/**
	//	 * @return the authorizationHeader
	//	 */
	//	public String getAuthToken()
	//	{
	//		return new String(RegistrationManager.getInstance().getAuthorizationToken());
	//	}

	/**
	 * @return the rest API end point for dashboard framework
	 * @throws Exception
	 */
	public String getDfRestApiEndPoint()
	{
		return getRestApiEndPoint(NAME_DASHBOARD_API_SERVICENAME, NAME_DASHBOARD_API_VERSION);
	}

	/*
	 * @return Quick links discovered from service manager
	 */
	public List<LinkEntity> getQuickLinks()
	{
		return lookupLinksWithRelPrefix(NAME_QUICK_LINK);
	}

	/**
	 * @return the rest API end point for SSF
	 * @throws Exception
	 */
	public String getSsfRestApiEndPoint() throws Exception
	{
		return getRestApiEndPoint(NAME_SSF_SERVICENAME, NAME_SSF_VERSION);
		//		if (true) {
		//			return "https://slc07hcn.us.oracle.com:4443/microservice/2875e44b-1a71-4bf2-9544-82ddc3b2d486";
		//		}
	}

	//	/**
	//	 * @return the registryUrls
	//	 */
	//	public String getRegistryUrls()
	//	{
	//		return registryUrls;
	//	}

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
	 * @return Visual analyzer links discovered from service manager
	 */
	public List<LinkEntity> getVisualAnalyzers()
	{
		return lookupLinksWithRelPrefix(NAME_VISUAL_ANALYZER);
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

	private String getExternalEndPoint(SanitizedInstanceInfo instance)
	{
		if (instance == null) {
			return null;
		}
		String endPoint = null;
		List<String> endpoints = new ArrayList<String>();
		// virtual end points contains the URLs to the service that may be reached from outside the cloud
		List<String> virtualEndpoints = instance.getVirtualEndpoints();
		endpoints.addAll(virtualEndpoints);
		List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
		endpoints.addAll(canonicalEndpoints);
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

		return endPoint;
	}

	//	/**
	//	 * @param registryUrls
	//	 *            the registryUrls to set
	//	 */
	//	public void setRegistryUrls(String registryUrls)
	//	{
	//		this.registryUrls = registryUrls;
	//	}

	private String getInternalEndPoint(InstanceInfo instance)
	{
		if (instance == null) {
			return null;
		}
		String endPoint = null;
		List<String> endpoints = new ArrayList<String>();
		// virtual end points contains the URLs to the service that may be reached from outside the cloud
		List<String> virtualEndpoints = instance.getVirtualEndpoints();
		endpoints.addAll(virtualEndpoints);
		List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
		endpoints.addAll(canonicalEndpoints);
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

		return endPoint;
	}

	private String getInternalEndPoint(List<InstanceInfo> instances)
	{
		if (instances == null) {
			return null;
		}
		String endPoint = null;
		for (InstanceInfo instance : instances) {
			endPoint = getInternalEndPoint(instance);
			if (endPoint != null) {
				return endPoint;
			}
		}
		return endPoint;
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

	private List<Link> getLinksWithRelPrefix(String relPrefix, SanitizedInstanceInfo instance)
	{
		List<Link> matched = new ArrayList<Link>();
		if (relPrefix != null) {
			for (Link link : instance.getLinks()) {
				if (link.getRel() != null ? link.getRel().startsWith(relPrefix) : "".startsWith(relPrefix)) {
					matched.add(link);
				}
			}
		}
		return matched;
	}

	private String getRestApiEndPoint(String serviceName, String version)
	{
		InstanceInfo queryInfo = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		SanitizedInstanceInfo sanitizedInstance;
		InstanceInfo internalInstance = null;
		try {
			internalInstance = LookupManager.getInstance().getLookupClient().getInstance(queryInfo);
			sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(internalInstance);
			if (sanitizedInstance == null) {
				return getInternalEndPoint(internalInstance);
				//				return "https://slc07hcn.us.oracle.com:4443/microservice/c8c62151-e90d-489a-83f8-99c741ace530/";
				// this happens when
				//    1. no instance exists based on the query criteria
				// or
				//    2. the selected instance does not expose any safe endpoints that are externally routeable (e.g., no HTTPS virtualEndpoints)
				//
				// In this case, need to trigger the failover scheme, or alternatively, one could use the plural form of the lookup, and loop through the returned instances
			}
			else {
				return getExternalEndPoint(sanitizedInstance);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (internalInstance != null) {
				return getInternalEndPoint(internalInstance);
			}
		}
		return null;
	}

	private List<LinkEntity> lookupLinksWithRelPrefix(String linkPrefix)
	{
		List<LinkEntity> linkList = new ArrayList<LinkEntity>();

		LookupClient lookUpClient = LookupManager.getInstance().getLookupClient();
		List<InstanceInfo> instanceList = lookUpClient.getInstancesWithLinkRelPrefix(linkPrefix);

		Map<String, Link> linksMap = new HashMap<String, Link>();
		Map<String, Link> dashboardLinksMap = new HashMap<String, Link>();
		for (InstanceInfo internalInstance : instanceList) {
			List<Link> links = internalInstance.getLinksWithRelPrefix(linkPrefix);
			try {
				SanitizedInstanceInfo sanitizedInstance = LookupManager.getInstance().getLookupClient()
						.getSanitizedInstanceInfo(internalInstance);
				if (sanitizedInstance != null) {
					links = getLinksWithRelPrefix(linkPrefix, sanitizedInstance);
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (NAME_DASHBOARD_UI_SERVICENAME.equals(internalInstance.getServiceName())
					&& NAME_DASHBOARD_UI_VERSION.equals(internalInstance.getVersion())) {
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
