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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter.ApplicationOPCName;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.EndpointEntity;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.TenantSubscriptionUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	public static final String NAME_ADMIN_LINK = "administration";
	public static final String NAME_DASHBOARD_UI_SERVICENAME = "Dashboard-UI";
	public static final String NAME_DASHBOARD_UI_VERSION = "0.1";
	public static final String NAME_REGISTRY_SERVICENAME = "RegistryService";
	public static final String NAME_REGISTRY_VERSION = "0.1";
	public static final String NAME_REGISTRY_REL_SSO = "sso.endpoint/virtual";
	private static final Logger _logger = LogManager.getLogger(RegistrationEntity.class);
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
			//			exception.printStackTrace();
			_logger.error("Failed to initialize Lookup Manager", exception);
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
	 * @return Administration links discovered from service manager
	 */
	public List<LinkEntity> getAdminLinks()
	{
		return lookupLinksWithRelPrefix(NAME_ADMIN_LINK, true);
	}

	/**
	 * @return the rest API end point for dashboard framework
	 * @throws Exception
	 */
	public String getDfRestApiEndPoint()
	{
		EndpointEntity entity = RegistryLookupUtil.getServiceExternalEndPoint(NAME_DASHBOARD_API_SERVICENAME,
				NAME_DASHBOARD_API_VERSION, TenantContext.getCurrentTenant());
		return entity != null ? entity.getHref() : null;
	}

	/**
	 * @return the registryUrl
	 */
	//	public String getRegistryUrl()
	//	{
	//		Link link = RegistryLookupUtil.getServiceExternalLink(NAME_REGISTRY_SERVICENAME, NAME_REGISTRY_VERSION,
	//				NAME_REGISTRY_REL_SSO);
	//		return link != null ? link.getHref() : null;
	//	}

	//	/*
	//	 * @return Quick links discovered from service manager
	//	 */
	//	public List<LinkEntity> getQuickLinks()
	//	{
	//		return lookupLinksWithRelPrefix(NAME_QUICK_LINK);
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
	 * @return the rest API end point for SSF
	 * @throws Exception
	 */
	public String getSsfRestApiEndPoint() throws Exception
	{
		EndpointEntity entity = RegistryLookupUtil.getServiceExternalEndPoint(NAME_SSF_SERVICENAME, NAME_SSF_VERSION,
				TenantContext.getCurrentTenant());
		return entity != null ? entity.getHref() : null;
		//		if (true) {
		//			return "https://slc07hcn.us.oracle.com:4443/microservice/2875e44b-1a71-4bf2-9544-82ddc3b2d486";
		//		}
	}

	/**
	 * @return Visual analyzer links discovered from service manager
	 */
	public List<LinkEntity> getVisualAnalyzers()
	{
		return lookupLinksWithRelPrefix(NAME_VISUAL_ANALYZER);
	}

	private void addToLinksMap(Map<String, LinkEntity> linksMap, List<Link> links, String serviceName, String version)
	{
		for (Link link : links) {
			String key = serviceName + "_" + version + "_" + link.getRel();
			if (!linksMap.containsKey(key)) {
				LinkEntity le = new LinkEntity(getLinkName(link.getRel()), link.getHref(), serviceName, version);
				linksMap.put(key, le);
			}
			else if (linksMap.get(key).getHref().toLowerCase().startsWith("http://")
					&& link.getHref().toLowerCase().startsWith("https://")) {
				LinkEntity le = new LinkEntity(getLinkName(link.getRel()), link.getHref(), serviceName, version);
				linksMap.put(key, le);
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

	/**
	 * This method returns a set of SM(service manager) services names represents the subscribed services for specified tenant
	 *
	 * @param tenantName
	 * @return
	 */
	private Set<String> getTenantSubscribedApplicationSet(boolean isAdmin)
	{
		String tenantName = TenantContext.getCurrentTenant();
		Set<String> appSet = new HashSet<String>();
		if (StringUtil.isEmpty(tenantName)) {
			return appSet;
		}
		List<String> apps = TenantSubscriptionUtil.getTenantSubscribedServices(tenantName);
		if (apps == null || apps.isEmpty()) {
			return appSet;
		}
		for (String app : apps) {
			if (ApplicationOPCName.APM.toString().equals(app)) {
				appSet.add("ApmUI");
			}
			else if (ApplicationOPCName.ITAnalytics.toString().equals(app)) {
				appSet.add("EmcitasApplications");
				appSet.add("TargetAnalytics");
			}
			else if (ApplicationOPCName.LogAnalytics.toString().equals(app)) {
				appSet.add("LoganService");
			}
		}
		//if any of APM/LA/TA is subscribed, TenantManagementUI should be subscribed accordingly as agreement now
		if (appSet.size() > 0 && isAdmin) {
			appSet.add("TenantManagementUI");
		}
		return appSet;
	}

	private List<LinkEntity> lookupLinksWithRelPrefix(String linkPrefix)
	{
		return lookupLinksWithRelPrefix(linkPrefix, false);
	}

	private List<LinkEntity> lookupLinksWithRelPrefix(String linkPrefix, boolean isAdminLink)
	{
		_logger.info("lookupLinksWithRelPrefix(" + linkPrefix + "," + isAdminLink + ")");
		List<LinkEntity> linkList = new ArrayList<LinkEntity>();

		LookupClient lookUpClient = LookupManager.getInstance().getLookupClient();
		List<InstanceInfo> instanceList = lookUpClient.getInstancesWithLinkRelPrefix(linkPrefix);

		Set<String> subscribedApps = getTenantSubscribedApplicationSet(isAdminLink);
		_logger.info("Got Subscribed applications:", subscribedApps != null ? subscribedApps.toString() : "null");
		Map<String, LinkEntity> linksMap = new HashMap<String, LinkEntity>();
		Map<String, LinkEntity> dashboardLinksMap = new HashMap<String, LinkEntity>();
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
				//				e.printStackTrace();
				_logger.error("Error to get SanitizedInstanceInfo", e);
			}
			if (NAME_DASHBOARD_UI_SERVICENAME.equals(internalInstance.getServiceName())
					&& NAME_DASHBOARD_UI_VERSION.equals(internalInstance.getVersion())) {
				addToLinksMap(dashboardLinksMap, links, internalInstance.getServiceName(), internalInstance.getVersion());
			}
			else if (subscribedApps != null && subscribedApps.contains(internalInstance.getServiceName())) {
				addToLinksMap(linksMap, links, internalInstance.getServiceName(), internalInstance.getVersion());
			}

		}
		_logger.info("dashboardLinksMap: " + dashboardLinksMap);
		Iterator<Map.Entry<String, LinkEntity>> iterDashboardLinks = dashboardLinksMap.entrySet().iterator();
		while (iterDashboardLinks.hasNext()) {
			Map.Entry<String, LinkEntity> entry = iterDashboardLinks.next();
			LinkEntity val = entry.getValue();
			linkList.add(val);
		}

		_logger.info("linksMap: " + dashboardLinksMap);
		Iterator<Map.Entry<String, LinkEntity>> iterLinks = linksMap.entrySet().iterator();
		while (iterLinks.hasNext()) {
			Map.Entry<String, LinkEntity> entry = iterLinks.next();
			LinkEntity val = entry.getValue();
			if (!dashboardLinksMap.containsKey(entry.getKey())) {
				linkList.add(val);
			}
		}
		_logger.info("Got links matching prefix:" + linkPrefix, linkList.toString());
		return linkList;
	}

}
