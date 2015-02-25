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
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.RegistryLookupUtil;

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
	public static final String NAME_REGISTRY_SERVICENAME = "RegistryService";
	public static final String NAME_REGISTRY_VERSION = "0.1";
	public static final String NAME_REGISTRY_REL_SSO = "sso.endpoint/virtual";
	//	private String registryUrls;

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

	/**
	 * @return the authorizationHeader
	 */
	//	public String getAuthToken()
	//	{
	//		return new String(LookupManager.getInstance().getAuthorizationToken());
	//	}

	/**
	 * @return the rest API end point for dashboard framework
	 * @throws Exception
	 */
	public String getDfRestApiEndPoint()
	{
		return RegistryLookupUtil.getServiceExternalEndPoint(NAME_DASHBOARD_API_SERVICENAME, NAME_DASHBOARD_API_VERSION);
	}

	/*
	 * @return Quick links discovered from service manager
	 */
	public List<LinkEntity> getQuickLinks()
	{
		return lookupLinksWithRelPrefix(NAME_QUICK_LINK);
	}

	/**
	 * @return the registryUrl
	 */
	public String getRegistryUrl()
	{
		Link link = RegistryLookupUtil.getServiceExternalLink(NAME_REGISTRY_SERVICENAME, NAME_REGISTRY_VERSION,
				NAME_REGISTRY_REL_SSO);
		return link != null ? link.getHref() : null;
	}

	/**
	 * @return the rest API end point for SSF
	 * @throws Exception
	 */
	public String getSsfRestApiEndPoint() throws Exception
	{
		return RegistryLookupUtil.getServiceExternalEndPoint(NAME_SSF_SERVICENAME, NAME_SSF_VERSION);
		//		if (true) {
		//			return "https://slc07hcn.us.oracle.com:4443/microservice/2875e44b-1a71-4bf2-9544-82ddc3b2d486";
		//		}
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
	//	/**
	//	 * @param registryUrls
	//	 *            the registryUrls to set
	//	 */
	//	public void setRegistryUrls(String registryUrls)
	//	{
	//		this.registryUrls = registryUrls;
	//	}
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
		for (InstanceInfo internalInstance : instanceList) {
			List<Link> links = internalInstance.getLinksWithRelPrefix(linkPrefix);
			try {
				SanitizedInstanceInfo sanitizedInstance = LookupManager.getInstance().getLookupClient()
						.getSanitizedInstanceInfo(internalInstance);
				if (sanitizedInstance != null) {
					links = RegistryLookupUtil.getLinksWithRelPrefix(linkPrefix, sanitizedInstance);
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
