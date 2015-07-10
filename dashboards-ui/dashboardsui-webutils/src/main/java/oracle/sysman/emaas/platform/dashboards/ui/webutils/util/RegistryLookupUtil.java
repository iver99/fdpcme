/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miao
 */
public class RegistryLookupUtil
{
	private static final Logger logger = LogManager.getLogger(RegistryLookupUtil.class);

	// keep the following the same with service name
	public static final String APM_SERVICE = "ApmUI";
	public static final String ITA_SERVICE = "EmcitasApplications";
	public static final String LA_SERVICE = "LoganService";
	public static final String TA_SERVICE = "TargetAnalytics";

	public static List<Link> getLinksWithRelPrefix(String relPrefix, SanitizedInstanceInfo instance)
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

	public static EndpointEntity getServiceExternalEndPoint(String serviceName, String version, String tenantName)
	{
		Link link = RegistryLookupUtil.getServiceExternalLink(serviceName, version, "sso.endpoint/virtual", tenantName);
		if (link != null) {
			return new EndpointEntity(serviceName, version, link.getHref());
		}
		else {
			return null;
		}
		/*
		InstanceInfo queryInfo = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		SanitizedInstanceInfo sanitizedInstance;
		InstanceInfo internalInstance = null;
		try {
		    internalInstance = LookupManager.getInstance().getLookupClient().getInstance(queryInfo);
		    sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(internalInstance);
		    if (sanitizedInstance == null) {
		        String url = RegistryLookupUtil.getInternalEndPoint(internalInstance);
		        return new EndpointEntity(serviceName, version, url);
		        //                return "https://slc07hcn.us.oracle.com:4443/microservice/c8c62151-e90d-489a-83f8-99c741ace530/";
		        // this happens when
		        //    1. no instance exists based on the query criteria
		        // or
		        //    2. the selected instance does not expose any safe endpoints that are externally routeable (e.g., no HTTPS virtualEndpoints)
		        //
		        // In this case, need to trigger the failover scheme, or alternatively, one could use the plural form of the lookup, and loop through the returned instances
		    }
		    else {
		        String url = RegistryLookupUtil.getExternalEndPoint(sanitizedInstance);
		        return new EndpointEntity(serviceName, version, url);
		    }
		}
		catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    if (internalInstance != null) {
		        String url = RegistryLookupUtil.getInternalEndPoint(internalInstance);
		        return new EndpointEntity(serviceName, version, url);
		    }
		}
		return null;
		 */
	}

	public static Link getServiceExternalLink(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel, false, tenantName);
	}

	public static Link getServiceExternalLinkWithRelPrefix(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel, true, tenantName);
	}

	public static Link getServiceInternalLink(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceInternalLink(serviceName, version, rel, false, tenantName);
	}

	public static EndpointEntity replaceWithVanityUrl(EndpointEntity ep, String tenantName, String serviceName)
	{
		if (ep == null || StringUtils.isEmpty(serviceName)) {
			return ep;
		}
		Map<String, String> vanityBaseUrls = RegistryLookupUtil.getVanityBaseURLs(tenantName);
		if (vanityBaseUrls != null && vanityBaseUrls.containsKey(serviceName)) {
			ep = RegistryLookupUtil.replaceVanityUrlDomainForEndpointEntity(vanityBaseUrls.get(serviceName), ep, tenantName);
			logger.debug("Completed to (try to) replace URL with vanity URL. Updated url is {}", ep.getHref());
		}
		return ep;
	}

	public static Link replaceWithVanityUrl(Link lk, String tenantName, String serviceName)
	{
		if (lk == null || StringUtils.isEmpty(serviceName)) {
			return lk;
		}
		Map<String, String> vanityBaseUrls = RegistryLookupUtil.getVanityBaseURLs(tenantName);
		if (vanityBaseUrls != null && vanityBaseUrls.containsKey(serviceName)) {
			lk = RegistryLookupUtil.replaceVanityUrlDomainForLink(vanityBaseUrls.get(serviceName), lk, tenantName);
			logger.debug("Completed to (try to) replace URL with vanity URL. Updated url is {}", lk.getHref());
		}
		return lk;
	}

	public static String replaceWithVanityUrl(String url, String tenantName, String serviceName)
	{
		if (url == null || StringUtils.isEmpty(serviceName)) {
			return url;
		}
		Map<String, String> vanityBaseUrls = RegistryLookupUtil.getVanityBaseURLs(tenantName);
		if (vanityBaseUrls != null && vanityBaseUrls.containsKey(serviceName)) {
			url = RegistryLookupUtil.replaceVanityUrlDomainForUrl(vanityBaseUrls.get(serviceName), url, tenantName);
			logger.debug("Completed to (try to) replace URL with vanity URL. Updated url is {}", url);
		}
		return url;
	}

	//    private static String getExternalEndPoint(SanitizedInstanceInfo instance)
	//    {
	//        if (instance == null) {
	//            return null;
	//        }
	//        String endPoint = null;
	//        List<String> endpoints = new ArrayList<String>();
	//        // virtual end points contains the URLs to the service that may be reached from outside the cloud
	//        List<String> virtualEndpoints = instance.getVirtualEndpoints();
	//        endpoints.addAll(virtualEndpoints);
	//        List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
	//        endpoints.addAll(canonicalEndpoints);
	//        if (endpoints != null && endpoints.size() > 0) {
	//            for (String ep : endpoints) {
	//                if (ep.startsWith("https://")) {
	//                    return ep;
	//                }
	//                if (endPoint == null) {
	//                    endPoint = ep;
	//                }
	//            }
	//        }
	//
	//        return endPoint;
	//    }

	//    private static String getInternalEndPoint(InstanceInfo instance)
	//    {
	//        if (instance == null) {
	//            return null;
	//        }
	//        String endPoint = null;
	//        List<String> endpoints = new ArrayList<String>();
	//        // virtual end points contains the URLs to the service that may be reached from outside the cloud
	//        List<String> virtualEndpoints = instance.getVirtualEndpoints();
	//        endpoints.addAll(virtualEndpoints);
	//        List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
	//        endpoints.addAll(canonicalEndpoints);
	//        if (endpoints != null && endpoints.size() > 0) {
	//            for (String ep : endpoints) {
	//                if (ep.startsWith("https://")) {
	//                    return ep;
	//                }
	//                if (endPoint == null) {
	//                    endPoint = ep;
	//                }
	//            }
	//        }
	//
	//        return endPoint;
	//    }

	//    private static String getInternalEndPoint(List<InstanceInfo> instances)
	//    {
	//        if (instances == null) {
	//            return null;
	//        }
	//        String endPoint = null;
	//        for (InstanceInfo instance : instances) {
	//            endPoint = RegistryLookupUtil.getInternalEndPoint(instance);
	//            if (endPoint != null) {
	//                return endPoint;
	//            }
	//        }
	//        return endPoint;
	//    }

	private static List<Link> getLinksWithProtocol(String protocol, List<Link> links)
	{
		if (protocol == null || links == null || protocol.length() == 0 || links.size() == 0) {
			if (links == null) {
				return new ArrayList<Link>();
			}
			return links;
		}
		List<Link> protocoledLinks = new ArrayList<Link>();
		for (Link link : links) {
			try {
				URI uri = URI.create(link.getHref());
				if (protocol.equalsIgnoreCase(uri.getScheme())) {
					protocoledLinks.add(link);
				}
			}
			catch (Throwable thr) {
				logger.error(thr.getLocalizedMessage(), thr);
				return protocoledLinks;
			}
		}

		return protocoledLinks;
	}

	private static List<Link> getLinksWithRelPrefixWithProtocol(String protocol, String relPrefix, List<Link> links)
	{
		if (protocol == null || relPrefix == null || links == null || protocol.length() == 0 || links.size() == 0) {
			if (links == null) {
				return new ArrayList<Link>();
			}
			return links;
		}
		List<Link> protocoledLinks = new ArrayList<Link>();
		for (Link link : links) {
			try {
				URI uri = URI.create(link.getHref());
				if (protocol.equalsIgnoreCase(uri.getScheme()) && link.getRel() != null && link.getRel().indexOf(relPrefix) == 0) {
					protocoledLinks.add(link);
				}
			}
			catch (Throwable thr) {
				logger.error(thr.getLocalizedMessage(), thr);
				return protocoledLinks;
			}
		}

		return protocoledLinks;
	}

	private static Link getServiceExternalLink(String serviceName, String version, String rel, boolean prefixMatch,
			String tenantName)
	{
		logger.debug(
				"/getServiceExternalLink/ Trying to retrieve service external link for service: \"{}\", version: \"{}\", rel: \"{}\", tenant: \"{}\"",
				serviceName, version, rel, tenantName);
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		Link lk = null;
		try {
			List<InstanceInfo> result = null;
			if (!StringUtil.isEmpty(tenantName)) {
				InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
				logger.debug("Retrieved instance {} by using getInstanceForTenant for tenant {}", ins, tenantName);
				if (ins == null) {
					logger.error(
							"Error: retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
							serviceName, version, tenantName);
				}
				else {
					result = new ArrayList<InstanceInfo>();
					result.add(ins);
				}

			}
			else {
				result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			}
			if (result != null && result.size() > 0) {

				//find https link first
				for (InstanceInfo internalInstance : result) {
					List<Link> links = null;
					if (prefixMatch) {
						links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, "https");
					}
					else {
						links = internalInstance.getLinksWithProtocol(rel, "https");
					}

					try {
						SanitizedInstanceInfo sanitizedInstance = null;
						if (!StringUtil.isEmpty(tenantName)) {
							sanitizedInstance = LookupManager.getInstance().getLookupClient()
									.getSanitizedInstanceInfo(internalInstance, tenantName);
							logger.debug("Retrieved sanitizedInstance {} by using getSanitizedInstanceInfo for tenant {}",
									sanitizedInstance, tenantName);
						}
						else {
							logger.warn("Failed to retrieve tenant when getting external end point. Using tenant non-specific APIs to get sanitized instance");
							sanitizedInstance = LookupManager.getInstance().getLookupClient()
									.getSanitizedInstanceInfo(internalInstance);
						}
						if (sanitizedInstance != null) {
							if (prefixMatch) {
								links = RegistryLookupUtil.getLinksWithRelPrefixWithProtocol("https", rel,
										sanitizedInstance.getLinks());
							}
							else {
								links = RegistryLookupUtil.getLinksWithProtocol("https", sanitizedInstance.getLinks(rel));
							}
						}
					}
					catch (Exception e) {
						logger.error(e.getLocalizedMessage(), e);
					}
					if (links != null && links.size() > 0) {
						lk = links.get(0);
						break;
					}
				}

				if (lk != null) {
					return lk;
				}

				//https link is not found, then find http link
				for (InstanceInfo internalInstance : result) {
					List<Link> links = null;
					if (prefixMatch) {
						links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, "http");
					}
					else {
						links = internalInstance.getLinksWithProtocol(rel, "http");
					}
					try {
						SanitizedInstanceInfo sanitizedInstance = null;
						if (!StringUtil.isEmpty(tenantName)) {
							sanitizedInstance = LookupManager.getInstance().getLookupClient()
									.getSanitizedInstanceInfo(internalInstance, tenantName);
							logger.debug("Retrieved sanitizedInstance {} by using getSanitizedInstanceInfo for tenant {}",
									sanitizedInstance, tenantName);
						}
						else {
							logger.warn("Failed to retrieve tenant when getting external end point. Using tenant non-specific APIs to get sanitized instance");
							sanitizedInstance = LookupManager.getInstance().getLookupClient()
									.getSanitizedInstanceInfo(internalInstance);
						}
						if (sanitizedInstance != null) {
							if (prefixMatch) {
								links = RegistryLookupUtil.getLinksWithRelPrefixWithProtocol("https", rel,
										sanitizedInstance.getLinks());
							}
							else {
								links = RegistryLookupUtil.getLinksWithProtocol("https", sanitizedInstance.getLinks(rel));
							}

						}
					}
					catch (Exception e) {
						logger.error(e.getLocalizedMessage(), e);
					}
					if (links != null && links.size() > 0) {
						lk = links.get(0);
						return lk;
					}
				}
			}
			return lk;
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return lk;
		}
	}

	private static Link getServiceInternalLink(String serviceName, String version, String rel, boolean prefixMatch,
			String tenantName)
	{
		logger.debug(
				"/getServiceInternalLink/ Trying to retrieve service internal link for service: \"{}\", version: \"{}\", rel: \"{}\", prefixMatch: \"{}\", tenant: \"{}\"",
				serviceName, version, rel, prefixMatch, tenantName);
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		Link lk = null;
		try {
			List<InstanceInfo> result = null;
			if (!StringUtil.isEmpty(tenantName)) {
				InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
				logger.debug("Retrieved instance {} by using getInstanceForTenant for tenant {}", ins, tenantName);
				if (ins == null) {
					logger.error(
							"Error: retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
							serviceName, version, tenantName);
				}
				else {
					result = new ArrayList<InstanceInfo>();
					result.add(ins);
				}

			}
			else {
				result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			}
			if (result != null && result.size() > 0) {
				// [EMCPDF-733] Rest client can't handle https currently, so http protocol is enough for internal use
				// find http link only
				for (InstanceInfo internalInstance : result) {
					List<Link> links = null;
					if (prefixMatch) {
						links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, "http");
					}
					else {
						links = internalInstance.getLinksWithProtocol(rel, "http");
					}
					if (links != null && links.size() > 0) {
						lk = links.get(0);
						return lk;
					}
				}
			}
			return lk;
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return lk;
		}
	}

	private static Map<String, String> getVanityBaseURLs(String tenantName)
	{
		logger.debug("/getVanityBaseURLs/ Trying to retrieve service internal link for tenant: \"{}\"", tenantName);
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName("OHS").build();
		Link lk = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			List<InstanceInfo> result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			if (result != null && result.size() > 0) {
				for (InstanceInfo internalInstance : result) {
					if (map.containsKey(APM_SERVICE) && map.containsKey(ITA_SERVICE) && map.containsKey(LA_SERVICE)) {
						break;
					}
					if (!map.containsKey(APM_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/apm", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && links.size() > 0) {
							lk = links.get(0);
							logger.debug("Retrieved base vanity URL for apm: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							logger.debug("Tenant id is inserted into the base vanity URL for apm. The URL is {}", url);
							map.put(APM_SERVICE, url);
						}
					}
					if (!map.containsKey(ITA_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/ita", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && links.size() > 0) {
							lk = links.get(0);
							logger.debug("Retrieved base vanity URL for ita: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							logger.debug("Tenant id is inserted into the base vanity URL for ita. The URL is {}", url);
							map.put(ITA_SERVICE, url);
							// ta/ita has the same URL pattern
							map.put(TA_SERVICE, url);
						}
					}
					if (!map.containsKey(LA_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/la", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && links.size() > 0) {
							lk = links.get(0);
							logger.debug("Retrieved base vanity URL for la: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							logger.debug("Tenant id is inserted into the base vanity URL for la. The URL is {}", url);
							map.put(LA_SERVICE, url);
						}
					}
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}

		if (logger.isDebugEnabled() && !map.isEmpty()) {
			logger.debug("Printing out vanity URLs map:");
			for (String service : map.keySet()) {
				String url = map.get(service);
				logger.debug("service name is {}, and url is {}", service, url);
			}
		}
		return map;
	}

	private static String insertTenantIntoVanityBaseUrl(String tenantName, String vanityBaseUrl)
	{
		logger.debug("/insertTenantIntoVanityBaseUrl/ Trying to insert tenant \"{}\" to base vanity url \"{}\"", tenantName,
				vanityBaseUrl);
		if (StringUtil.isEmpty(tenantName) || StringUtil.isEmpty(vanityBaseUrl)) {
			return vanityBaseUrl;
		}

		if (vanityBaseUrl.indexOf("://") != -1) {
			String[] splittedProtocol = vanityBaseUrl.split("://");
			StringBuilder sb = new StringBuilder();
			sb.append(splittedProtocol[0]);
			sb.append("://");
			sb.append(tenantName);
			sb.append(".");
			for (int i = 1; i < splittedProtocol.length; i++) {
				sb.append(splittedProtocol[i]);
				if (i != splittedProtocol.length - 1) {
					sb.append("://");
				}
			}
			logger.debug("/insertTenantIntoVanityBaseUrl/ URL \"{}\" is updated to \"{}\"", vanityBaseUrl, sb.toString());
			return sb.toString();
		}
		return vanityBaseUrl;
	}

	private static EndpointEntity replaceVanityUrlDomainForEndpointEntity(String domainPort, EndpointEntity ee, String tenantName)
	{
		logger.debug("/replaceDomainForEndpointEntity/ Trying to replace endpoint entity \"{}\" with domain \"{}\"",
				ee != null ? ee.getHref() : null, domainPort);
		if (StringUtil.isEmpty(domainPort) || ee == null || StringUtil.isEmpty(ee.getHref())) {
			return ee;
		}
		String replacedHref = RegistryLookupUtil.replaceVanityUrlDomainForUrl(domainPort, ee.getHref(), tenantName);
		logger.debug("/replaceDomainForEndpointEntity/ Endpoint entity \"{}\" URL (after replaced) is \"{}\"", ee.getHref(),
				replacedHref);
		ee.setHref(replacedHref);
		return ee;
	}

	private static Link replaceVanityUrlDomainForLink(String domainPort, Link lk, String tenantName)
	{
		logger.debug("/replaceDomainForLink/ Trying to replace link url \"{}\" with domain \"{}\"", lk != null ? lk.getHref()
				: null, domainPort);
		if (StringUtil.isEmpty(domainPort) || lk == null || StringUtil.isEmpty(lk.getHref())) {
			return lk;
		}
		String replacedHref = RegistryLookupUtil.replaceVanityUrlDomainForUrl(domainPort, lk.getHref(), tenantName);
		logger.debug("/replaceDomainForLink/ Link \"{}\" URL (after replaced) is \"{}\"", lk.getHref(), replacedHref);
		lk.withHref(replacedHref);
		return lk;
	}

	private static String replaceVanityUrlDomainForUrl(String vanityBaseUrl, String targetUrl, String tenantName)
	{
		if (StringUtils.isEmpty(vanityBaseUrl) || StringUtils.isEmpty(targetUrl) || targetUrl.indexOf("://") == -1) {
			return targetUrl;
		}
		// replace URLs started with tenant only
		String[] splittedProtocol = targetUrl.split("://");
		if (splittedProtocol == null || splittedProtocol.length < 2) {
			logger.warn("Specified url \"{}\" is invalid, can't splitted into multiple parts by '://'", targetUrl);
			return targetUrl;
		}
		if (splittedProtocol[1] == null || !splittedProtocol[1].startsWith(tenantName)) {
			logger.debug(
					"Do not need to replace the url with vanity URL, because the URL \"{}\" doesn't start with opc tenant id",
					targetUrl);
			return targetUrl;
		}
		logger.info("Replacing with vanity base URL for target url. Vanity url is {}, url is {}", vanityBaseUrl, targetUrl);
		String domainToReplace = vanityBaseUrl;
		if (domainToReplace.indexOf("://") != -1) {
			String[] splittedDomain = domainToReplace.split("://");
			if (splittedDomain != null && splittedDomain.length > 1) {
				domainToReplace = splittedDomain[1];
			}
		}
		logger.info("Replacing with vanity base url for url. Vanity url w/o protocol is {}", vanityBaseUrl);
		StringBuilder sb = new StringBuilder();

		sb.append(splittedProtocol[0]);
		sb.append("://");
		sb.append(domainToReplace);

		if (splittedProtocol[1].indexOf("/") != -1) {
			String[] splitted = splittedProtocol[1].split("/");
			if (splitted.length > 1) {
				for (int i = 1; i < splitted.length; i++) {
					sb.append("/");
					sb.append(splitted[i]);
				}
			}
		}
		logger.info("After replacing with vanity url, the target url is: \"{}\"", sb.toString());
		return sb.toString();
	}

}
