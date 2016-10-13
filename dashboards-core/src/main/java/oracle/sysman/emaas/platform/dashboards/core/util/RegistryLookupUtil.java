/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.dashboards.core.cache.CacheManager;
import oracle.sysman.emaas.platform.dashboards.core.cache.CachedLink;
import oracle.sysman.emaas.platform.dashboards.core.cache.Keys;
import oracle.sysman.emaas.platform.dashboards.core.cache.Tenant;

/**
 * @author miao
 */
public class RegistryLookupUtil
{
	public static class VersionedLink extends Link
	{
		private String version;

		/**
		 *
		 */
		public VersionedLink()
		{
			// TODO Auto-generated constructor stub
		}

		public VersionedLink(Link link, String version)
		{
			withHref(link.getHref());
			withOverrideTypes(link.getOverrideTypes());
			withRel(link.getRel());
			withTypesStr(link.getTypesStr());
			this.version = version;
		}

		/**
		 * @return the version
		 */
		public String getVersion()
		{
			return version;
		}

		/**
		 * @param version
		 *            the version to set
		 */
		public void setVersion(String version)
		{
			this.version = version;
		}

	}

	private static final Logger LOGGER = LogManager.getLogger(RegistryLookupUtil.class);

	private static final Logger itrLogger = LogUtil.getInteractionLogger();
	// keep the following the same with service name
	public static final String APM_SERVICE = "ApmUI";

	public static final String ITA_SERVICE = "emcitas-ui-apps";
	public static final String LA_SERVICE = "LogAnalyticsUI";
	public static final String TA_SERVICE = "TargetAnalytics";
	public static final String MONITORING_SERVICE = "MonitoringServiceUI";
	public static final String SECURITY_ANALYTICS_SERVICE = "SecurityAnalyticsUI";
	public static final String COMPLIANCE_SERVICE = "ComplianceUIService";
	public static final String ORCHESTRATION_SERVICE = "CosUIService";

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

	public static String getServiceExternalEndPoint(String serviceName, String version, String tenantName)
	{
		LOGGER.debug(
				"/getServiceExternalEndPoint/ Trying to retrieve service external end point for service: \"{}\", version: \"{}\", tenant: \"{}\"",
				serviceName, version, tenantName);
		InstanceInfo queryInfo = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		LogUtil.setInteractionLogThreadContext(tenantName, "Retristry lookup client", LogUtil.InteractionLogDirection.OUT);
		itrLogger.debug("Retrieved instance {}", queryInfo);
		SanitizedInstanceInfo sanitizedInstance;
		InstanceInfo internalInstance = null;
		try {
			if (!StringUtil.isEmpty(tenantName)) {
				internalInstance = LookupManager.getInstance().getLookupClient().getInstanceForTenant(queryInfo, tenantName);
				itrLogger
						.debug("Retrieved instance {} by using getInstanceForTenant for tenant {}", internalInstance, tenantName);
				if (internalInstance == null) {
					LOGGER.error(
							"Error: retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
							serviceName, version, tenantName);
				}
			}
			else {
				internalInstance = LookupManager.getInstance().getLookupClient().getInstance(queryInfo);
				itrLogger.debug("Retrieved internal instance {} by using LookupClient.getInstance");
			}
			sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(internalInstance);
			itrLogger.debug("Retrieved sanitized instance {} by using LookupClient.getSanitizedInstanceInfo");
			if (sanitizedInstance == null) {
				return RegistryLookupUtil.getInternalEndPoint(internalInstance);
				//				return "https://slc07hcn.us.oracle.com:4443/microservice/c8c62151-e90d-489a-83f8-99c741ace530/";
				// this happens when
				//    1. no instance exists based on the query criteria
				// or
				//    2. the selected instance does not expose any safe endpoints that are externally routeable (e.g., no HTTPS virtualEndpoints)
				//
				// In this case, need to trigger the failover scheme, or alternatively, one could use the plural form of the lookup, and loop through the returned instances
			}
			else {
				return RegistryLookupUtil.getExternalEndPoint(sanitizedInstance);
			}
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			if (internalInstance != null) {
				return RegistryLookupUtil.getInternalEndPoint(internalInstance);
			}
		}
		return null;
	}

	public static EndpointEntity getServiceExternalEndPointEntity(String serviceName, String version, String tenantName)
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
		    e.printStackTrace();
		    if (internalInstance != null) {
		        String url = RegistryLookupUtil.getInternalEndPoint(internalInstance);
		        return new EndpointEntity(serviceName, version, url);
		    }
		}
		return null;
		 */
	}

	public static VersionedLink getServiceExternalLink(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel, false, tenantName);
	}

	public static VersionedLink getServiceExternalLinkWithRelPrefix(String serviceName, String version, String rel,
			String tenantName)
	{
		return RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel, true, tenantName);
	}

	public static String getServiceInternalEndpoint(String serviceName, String version, String tenantName)
	{
		LOGGER.debug(
				"/getServiceInternalLink/ Trying to retrieve service internal link for service: \"{}\", version: \"{}\", tenant: \"{}\"",
				serviceName, version, tenantName);
		LogUtil.setInteractionLogThreadContext(tenantName, "Retristry lookup client", LogUtil.InteractionLogDirection.OUT);
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		String endpoint = null;
		try {
			List<InstanceInfo> result = null;
			if (!StringUtil.isEmpty(tenantName)) {
				InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
				itrLogger.debug("Retrieved instance {} by using getInstanceForTenant for tenant {}", ins, tenantName);
				if (ins == null) {
					LOGGER.error(
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
			if (result != null && !result.isEmpty()) {
				// [EMCPDF-733] Rest client can't handle https currently, so http protocol is enough for internal use
				//https link is not found, then find http link
				for (InstanceInfo internalInstance : result) {
					endpoint = RegistryLookupUtil.getHttpInternalEndPoint(internalInstance);
					if (endpoint != null) {
						break;
					}
				}
			}
			return endpoint;
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return endpoint;
		}
	}

	public static Link getServiceInternalLink(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceInternalLink(serviceName, version, rel, false, tenantName);
	}

	public static EndpointEntity replaceWithVanityUrl(EndpointEntity ep, String tenantName, String serviceName)
	{
		if (ep == null || StringUtil.isEmpty(serviceName)) {
			return ep;
		}
		Map<String, String> vanityBaseUrls = RegistryLookupUtil.getVanityBaseURLs(tenantName);
		if (vanityBaseUrls != null && vanityBaseUrls.containsKey(serviceName)) {
			ep = RegistryLookupUtil.replaceVanityUrlDomainForEndpointEntity(vanityBaseUrls.get(serviceName), ep, tenantName);
			LOGGER.debug("Completed to (try to) replace URL with vanity URL. Updated url is {}", ep.getHref());
		}
		return ep;
	}

	public static Link replaceWithVanityUrl(Link lk, String tenantName, String serviceName)
	{
		if (lk == null || StringUtil.isEmpty(serviceName)) {
			return lk;
		}
		Map<String, String> vanityBaseUrls = RegistryLookupUtil.getVanityBaseURLs(tenantName);
		if (vanityBaseUrls != null && vanityBaseUrls.containsKey(serviceName)) {
			lk = RegistryLookupUtil.replaceVanityUrlDomainForLink(vanityBaseUrls.get(serviceName), lk, tenantName);
			LOGGER.debug("Completed to (try to) replace URL with vanity URL. Updated url is {}", lk.getHref());
		}
		return lk;
	}

	public static String replaceWithVanityUrl(String url, String tenantName, String serviceName)
	{
		if (url == null || StringUtil.isEmpty(serviceName)) {
			return url;
		}
		Map<String, String> vanityBaseUrls = RegistryLookupUtil.getVanityBaseURLs(tenantName);
		if (vanityBaseUrls != null && vanityBaseUrls.containsKey(serviceName)) {
			url = RegistryLookupUtil.replaceVanityUrlDomainForUrl(vanityBaseUrls.get(serviceName), url, tenantName);
			LOGGER.debug("Completed to (try to) replace URL with vanity URL. Updated url is {}", url);
		}
		return url;
	}

	/**
	 * @param tenantName
	 * @param internalInstance
	 * @return
	 * @throws Exception
	 */
	private static SanitizedInstanceInfo findSaniInsInfo(String tenantName, InstanceInfo internalInstance)
	{
		SanitizedInstanceInfo sanitizedInstance = null;
		try {
			if (!StringUtil.isEmpty(tenantName)) {
				sanitizedInstance = LookupManager.getInstance().getLookupClient()
						.getSanitizedInstanceInfo(internalInstance, tenantName);
				itrLogger.debug("Retrieved sanitizedInstance {} by using getSanitizedInstanceInfo for tenant {}",
						sanitizedInstance, tenantName);
			}
			else {
				LOGGER.debug("Failed to retrieve tenant when getting external link. Using tenant non-specific APIs to get sanitized instance");
				sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(internalInstance);
				itrLogger.debug("Retrieved sanitizedInstance {} by using getSanitizedInstanceInfo without tenant id",
						sanitizedInstance);
			}
		}
		catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
		return sanitizedInstance;
	}

	private static String getExternalEndPoint(SanitizedInstanceInfo instance)
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
		if (endpoints != null && !endpoints.isEmpty()) {
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

	private static String getHttpInternalEndPoint(InstanceInfo instance)
	{
		if (instance == null) {
			return null;
		}
		List<String> endpoints = new ArrayList<String>();
		/**
		 * canonicalEndpoints and virtualEndpoints are the service endpoints Canonical endpoints contains the URLs to the service
		 * that is reachable internally Virtual end points contains the URLs to the service that may be reached from outside the
		 * cloud
		 **/
		List<String> canonicalEndpoints = instance.getCanonicalEndpoints();
		endpoints.addAll(canonicalEndpoints);
		List<String> virtualEndpoints = instance.getVirtualEndpoints();
		endpoints.addAll(virtualEndpoints);
		if (endpoints != null && !endpoints.isEmpty()) {
			for (String ep : endpoints) {
				if (ep.startsWith("http://")) {
					return ep;
				}
			}
		}

		return null;
	}

	private static String getInternalEndPoint(InstanceInfo instance)
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
		if (endpoints != null && !endpoints.isEmpty()) {
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
			catch (Exception thr) {
				LOGGER.error(thr.getLocalizedMessage(), thr);
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
				LOGGER.debug("Checks link on protocol {} with expected rel prefix {} against retrieved link (rel={}, href={})",
						protocol, relPrefix, link.getRel(), link.getHref());
				URI uri = URI.create(link.getHref());
				if (protocol.equalsIgnoreCase(uri.getScheme()) && link.getRel() != null
						&& link.getRel().indexOf(relPrefix) == 0) {
					protocoledLinks.add(link);
				}
			}
			catch (Exception thr) {
				LOGGER.error(thr.getLocalizedMessage(), thr);
				return protocoledLinks;
			}
		}

		return protocoledLinks;
	}

	private static VersionedLink getServiceExternalLink(String serviceName, String version, String rel, boolean prefixMatch,
			String tenantName)
	{
		LOGGER.debug(
				"/getServiceExternalLink/ Trying to retrieve service external link for service: \"{}\", version: \"{}\", rel: \"{}\", tenant: \"{}\"",
				serviceName, version, rel, tenantName);
		Tenant cacheTenant = new Tenant(tenantName);
		try {
			CachedLink cl = (CachedLink) CacheManager.getInstance().getCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
					new Keys(CacheManager.LOOKUP_CACHE_KEY_EXTERNAL_LINK, serviceName, version, rel, prefixMatch));
			if (cl != null) {
				LOGGER.debug(
						"Retrieved exteral link {} from cache, serviceName={}, version={}, rel={}, prefixMatch={}, tenantName={}",
						cl.getHref(), serviceName, version, rel, prefixMatch, tenantName);
				return cl.getLink();
			}
		}
		catch (Exception e) {
			LOGGER.error("Error to retrieve external link from cache. Try to lookup the link", e);
		}

		InstanceInfo.Builder builder = InstanceInfo.Builder.newBuilder().withServiceName(serviceName);
		if (!StringUtil.isEmpty(version)) {
			builder = builder.withVersion(version);
		}
		InstanceInfo info = builder.build();
		LogUtil.setInteractionLogThreadContext(tenantName, "Retristry lookup client", LogUtil.InteractionLogDirection.OUT);
		VersionedLink lk = null;
		try {
			List<InstanceInfo> result = null;

			if (!StringUtil.isEmpty(tenantName)) {
				InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
				itrLogger.debug("Retrieved instance {} by using getInstanceForTenant for tenant {}", ins, tenantName);
				if (ins == null) {
					LOGGER.warn(
							"retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
							serviceName, version, tenantName);
					result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
					itrLogger.debug("Retrieved InstanceInfo list {} by using LookupClient.lookup for InstanceInfo {}", result,
							info);
				}
				else {
					result = new ArrayList<InstanceInfo>();
					result.add(ins);
				}

			}
			else {
				result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
				itrLogger.debug("Retrieved InstanceInfo list {} by using LookupClient.lookup for InstanceInfo {}", result, info);
			}
			if (result != null && !result.isEmpty()) {

				//find https link first
				for (InstanceInfo internalInstance : result) {
					List<Link> links = null;
					if (prefixMatch) {
						links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, "https");
					}
					else {
						links = internalInstance.getLinksWithProtocol(rel, "https");
					}
					if (version == null) {
						version = internalInstance.getVersion();
						LOGGER.debug(
								"Input version is null. Retrieved version from internalInstance for service: \"{}\" is \"{}\", rel: \"{}\", tenant: \"{}\"",
								serviceName, version, rel, tenantName);
					}

					SanitizedInstanceInfo sanitizedInstance = RegistryLookupUtil.findSaniInsInfo(tenantName, internalInstance);
					if (sanitizedInstance != null) {
						if (prefixMatch) {
							links = RegistryLookupUtil.getLinksWithRelPrefixWithProtocol("https", rel,
									sanitizedInstance.getLinks());
						}
						else {
							links = RegistryLookupUtil.getLinksWithProtocol("https", sanitizedInstance.getLinks(rel));
						}
						if (version == null) {
							version = sanitizedInstance.getVersion();
							LOGGER.debug(
									"Input version is null. Retrieved version from sanitizedInstance for service: \"{}\" is \"{}\", rel: \"{}\", tenant: \"{}\"",
									serviceName, version, rel, tenantName);
						}
					}
					if (links != null && !links.isEmpty()) {
						lk = new VersionedLink(links.get(0), version);
						break;
					}
				}

				if (lk != null) {
					LOGGER.debug(
							"[branch 1] Retrieved link: \"{}\" for service: \"{}\", version: \"{}\", rel: \"{}\", tenant: \"{}\"",
							lk.getHref(), serviceName, version, rel, tenantName);
					CacheManager.getInstance().putCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
							new Keys(CacheManager.LOOKUP_CACHE_KEY_EXTERNAL_LINK, serviceName, version, rel, prefixMatch),
							new CachedLink(lk));
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
					if (version == null) {
						version = internalInstance.getVersion();
					}

					SanitizedInstanceInfo sanitizedInstance = RegistryLookupUtil.findSaniInsInfo(tenantName, internalInstance);
					if (sanitizedInstance != null) {
						if (prefixMatch) {
							links = RegistryLookupUtil.getLinksWithRelPrefixWithProtocol("http", rel,
									sanitizedInstance.getLinks());
						}
						else {
							links = RegistryLookupUtil.getLinksWithProtocol("http", sanitizedInstance.getLinks(rel));
						}
						if (version == null) {
							version = sanitizedInstance.getVersion();
						}
					}
					if (links != null && !links.isEmpty()) {
						lk = new VersionedLink(links.get(0), version);
						LOGGER.debug(
								"[branch 2] Retrieved link: \"{}\" for service: \"{}\", version: \"{}\", rel: \"{}\", tenant: \"{}\"",
								lk == null ? null : lk.getHref(), serviceName, version, rel, tenantName);
						CacheManager.getInstance().putCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
								new Keys(CacheManager.LOOKUP_CACHE_KEY_EXTERNAL_LINK, serviceName, version, rel, prefixMatch),
								new CachedLink(lk));
						return lk;
					}
				}
			}
			LOGGER.debug("[branch 3] Retrieved link: \"{}\" for service: \"{}\", version: \"{}\", rel: \"{}\", tenant: \"{}\"",
					lk == null ? null : lk.getHref(), serviceName, version, rel, tenantName);
			return lk;
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return lk;
		}
	}

	private static Link getServiceInternalLink(String serviceName, String version, String rel, boolean prefixMatch,
			String tenantName)
	{
		LOGGER.debug(
				"/getServiceInternalLink/ Trying to retrieve service internal link for service: \"{}\", version: \"{}\", rel: \"{}\", prefixMatch: \"{}\", tenant: \"{}\"",
				serviceName, version, rel, prefixMatch, tenantName);
		Tenant cacheTenant = new Tenant(tenantName);
		try {
			CachedLink cl = (CachedLink) CacheManager.getInstance().getCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
					new Keys(CacheManager.LOOKUP_CACHE_KEY_INTERNAL_LINK, serviceName, version, rel, prefixMatch));
			if (cl != null) {
				LOGGER.debug(
						"Retrieved internal link {} from cache, serviceName={}, version={}, rel={}, prefixMatch={}, tenantName={}",
						cl.getHref(), serviceName, version, rel, prefixMatch, tenantName);
				return cl.getLink();
			}
		}
		catch (Exception e) {
			LOGGER.error("Error to retrieve internal link from cache. Try to lookup the link", e);
		}

		LogUtil.setInteractionLogThreadContext(tenantName, "Retristry lookup client", LogUtil.InteractionLogDirection.OUT);
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		VersionedLink lk = null;
		try {
			List<InstanceInfo> result = null;
			if (!StringUtil.isEmpty(tenantName)) {
				InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
				itrLogger.debug("Retrieved instance {} by using getInstanceForTenant for tenant {}", ins, tenantName);
				if (ins == null) {
					LOGGER.error(
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
			if (result != null && !result.isEmpty()) {
				// [EMCPDF-733] Rest client can't handle https currently, so http protocol is enough for internal use
				//https link is not found, then find http link
				for (InstanceInfo internalInstance : result) {
					List<Link> links = null;
					if (prefixMatch) {
						links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, "http");
					}
					else {
						links = internalInstance.getLinksWithProtocol(rel, "http");
					}
					if (version == null) {
						version = internalInstance.getVersion();
					}
					if (links != null && !links.isEmpty()) {
						lk = new VersionedLink(links.get(0), version);
						itrLogger.debug("Retrieved link {}", lk == null ? null : lk.getHref());
						CacheManager.getInstance().putCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
								new Keys(serviceName, version, rel, prefixMatch), new CachedLink(lk));
						return lk;
					}
				}
			}
			return lk;
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
			return lk;
		}
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> getVanityBaseURLs(String tenantName)
	{
		LOGGER.debug("/getVanityBaseURLs/ Trying to retrieve service internal link for tenant: \"{}\"", tenantName);
		Tenant cacheTenant = new Tenant(tenantName);
		Map<String, String> map = null;
		try {
			map = (Map<String, String>) CacheManager.getInstance().getCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
					CacheManager.LOOKUP_CACHE_KEY_VANITY_BASE_URL);
			if (map != null) {
				return map;
			}
		}
		catch (Exception e) {
			LOGGER.error(e);
		}
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName("OHS").build();
		Link lk = null;
		map = new HashMap<String, String>();
		try {
			List<InstanceInfo> result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
			if (result != null && !result.isEmpty()) {
				for (InstanceInfo internalInstance : result) {
					if (map.containsKey(APM_SERVICE) && map.containsKey(ITA_SERVICE) && map.containsKey(LA_SERVICE)
							&& map.containsKey(MONITORING_SERVICE) && map.containsKey(SECURITY_ANALYTICS_SERVICE)
							&& map.containsKey(COMPLIANCE_SERVICE) && map.containsKey(ORCHESTRATION_SERVICE)) {
						break;
					}
					if (!map.containsKey(APM_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/apm", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for apm: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for apm. The URL is {}", url);
							map.put(APM_SERVICE, url);
						}
					}
					if (!map.containsKey(ITA_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/ita", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for ita: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for ita. The URL is {}", url);
							map.put(ITA_SERVICE, url);
							// ta/ita has the same URL pattern
							map.put(TA_SERVICE, url);
						}
					}
					if (!map.containsKey(LA_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/la", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for la: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for la. The URL is {}", url);
							map.put(LA_SERVICE, url);
						}
					}
					if (!map.containsKey(MONITORING_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/monitoring", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for monitoring service: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for monitoring service. The URL is {}",
									url);
							map.put(MONITORING_SERVICE, url);
						}
					}
					if (!map.containsKey(SECURITY_ANALYTICS_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/security", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for Security Analytics service: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug(
									"Tenant id is inserted into the base vanity URL for Security Analytics service. The URL is {}",
									url);
							map.put(SECURITY_ANALYTICS_SERVICE, url);
						}
					}
					if (!map.containsKey(COMPLIANCE_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/compliance", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for Compliance service: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug("Tenant id is inserted into the base vanity URL for Compliance service. The URL is {}",
									url);
							map.put(COMPLIANCE_SERVICE, url);
						}
					}
					if (!map.containsKey(ORCHESTRATION_SERVICE)) {
						List<Link> links = internalInstance.getLinksWithProtocol("vanity/ocs", "https");
						links = RegistryLookupUtil.getLinksWithProtocol("https", links);

						if (links != null && !links.isEmpty()) {
							lk = links.get(0);
							LOGGER.debug("Retrieved base vanity URL for Orchestration service: {} ", lk.getHref());
							String url = RegistryLookupUtil.insertTenantIntoVanityBaseUrl(tenantName, lk.getHref());
							LOGGER.debug(
									"Tenant id is inserted into the base vanity URL for Orchestration service. The URL is {}",
									url);
							map.put(ORCHESTRATION_SERVICE, url);
						}
					}
				}
			}
		}
		catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}

		if (LOGGER.isDebugEnabled() && !map.isEmpty()) {
			LOGGER.debug("Printing out vanity URLs map:");
			for (String service : map.keySet()) {
				String url = map.get(service);
				LOGGER.debug("service name is {}, and url is {}", service, url);
			}
		}
		CacheManager.getInstance().putCacheable(cacheTenant, CacheManager.CACHES_LOOKUP_CACHE,
				CacheManager.LOOKUP_CACHE_KEY_VANITY_BASE_URL, map);
		return map;
	}

	private static String insertTenantIntoVanityBaseUrl(String tenantName, String vanityBaseUrl)
	{
		LOGGER.debug("/insertTenantIntoVanityBaseUrl/ Trying to insert tenant \"{}\" to base vanity url \"{}\"", tenantName,
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
			LOGGER.debug("/insertTenantIntoVanityBaseUrl/ URL \"{}\" is updated to \"{}\"", vanityBaseUrl, sb.toString());
			return sb.toString();
		}
		return vanityBaseUrl;
	}

	private static EndpointEntity replaceVanityUrlDomainForEndpointEntity(String domainPort, EndpointEntity ee, String tenantName)
	{
		LOGGER.debug("/replaceDomainForEndpointEntity/ Trying to replace endpoint entity \"{}\" with domain \"{}\"",
				ee != null ? ee.getHref() : null, domainPort);
		if (StringUtil.isEmpty(domainPort) || ee == null || StringUtil.isEmpty(ee.getHref())) {
			return ee;
		}
		String replacedHref = RegistryLookupUtil.replaceVanityUrlDomainForUrl(domainPort, ee.getHref(), tenantName);
		LOGGER.debug("/replaceDomainForEndpointEntity/ Endpoint entity \"{}\" URL (after replaced) is \"{}\"", ee.getHref(),
				replacedHref);
		ee.setHref(replacedHref);
		return ee;
	}

	private static Link replaceVanityUrlDomainForLink(String domainPort, Link lk, String tenantName)
	{
		LOGGER.debug("/replaceDomainForLink/ Trying to replace link url \"{}\" with domain \"{}\"",
				lk != null ? lk.getHref() : null, domainPort);
		if (StringUtil.isEmpty(domainPort) || lk == null || StringUtil.isEmpty(lk.getHref())) {
			return lk;
		}
		String replacedHref = RegistryLookupUtil.replaceVanityUrlDomainForUrl(domainPort, lk.getHref(), tenantName);
		LOGGER.debug("/replaceDomainForLink/ Link \"{}\" URL (after replaced) is \"{}\"", lk.getHref(), replacedHref);
		lk.withHref(replacedHref);
		return lk;
	}

	private static String replaceVanityUrlDomainForUrl(String vanityBaseUrl, String targetUrl, String tenantName)
	{
		if (StringUtil.isEmpty(vanityBaseUrl) || StringUtil.isEmpty(targetUrl) || targetUrl.indexOf("://") == -1) {
			return targetUrl;
		}
		// replace URLs started with tenant only
		String[] splittedProtocol = targetUrl.split("://");
		if (splittedProtocol == null || splittedProtocol.length < 2) {
			LOGGER.warn("Specified url \"{}\" is invalid, can't splitted into multiple parts by '://'", targetUrl);
			return targetUrl;
		}
		if (splittedProtocol[1] == null || !splittedProtocol[1].startsWith(tenantName)) {
			LOGGER.debug(
					"Do not need to replace the url with vanity URL, because the URL \"{}\" doesn't start with opc tenant id",
					targetUrl);
			return targetUrl;
		}
		LOGGER.info("Replacing with vanity base URL for target url. Vanity url is {}, url is {}", vanityBaseUrl, targetUrl);
		String domainToReplace = vanityBaseUrl;
		if (domainToReplace.indexOf("://") != -1) {
			String[] splittedDomain = domainToReplace.split("://");
			if (splittedDomain != null && splittedDomain.length > 1) {
				domainToReplace = splittedDomain[1];
			}
		}
		LOGGER.info("Replacing with vanity base url for url. Vanity url w/o protocol is {}", vanityBaseUrl);
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
		LOGGER.info("After replacing with vanity url, the target url is: \"{}\"", sb.toString());
		return sb.toString();
	}

	private RegistryLookupUtil()
	{
	}
}
