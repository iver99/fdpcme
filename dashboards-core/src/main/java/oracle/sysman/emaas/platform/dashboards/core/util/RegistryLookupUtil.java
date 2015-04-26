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
import java.util.List;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author miao
 */
public class RegistryLookupUtil
{
	private static final Logger logger = LogManager.getLogger(RegistryLookupUtil.class);

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
		InstanceInfo queryInfo = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		SanitizedInstanceInfo sanitizedInstance;
		InstanceInfo internalInstance = null;
		try {
			if (!StringUtil.isEmpty(tenantName) && RegistryLookupUtil.isServiceInApplicationShard(serviceName)) {
				internalInstance = LookupManager.getInstance().getLookupClient().getInstanceForTenant(queryInfo, tenantName);
				logger.debug("Retrieved instance {} by using getInstanceForTenant for tenant {}", internalInstance, tenantName);
				if (internalInstance == null) {
					logger.error(
							"Error: retrieved null instance info with getInstanceForTenant. Details: serviceName={}, version={}, tenantName={}",
							serviceName, version, tenantName);
				}
			}
			else {
				internalInstance = LookupManager.getInstance().getLookupClient().getInstance(queryInfo);
			}
			sanitizedInstance = LookupManager.getInstance().getLookupClient().getSanitizedInstanceInfo(internalInstance);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (internalInstance != null) {
				return RegistryLookupUtil.getInternalEndPoint(internalInstance);
			}
		}
		return null;
	}

	public static Link getServiceExternalLink(String serviceName, String version, String rel, String tenantName)
	{
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		Link lk = null;
		try {
			List<InstanceInfo> result = null;

			if (!StringUtil.isEmpty(tenantName) && RegistryLookupUtil.isServiceInApplicationShard(serviceName)) {
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
					List<Link> links = internalInstance.getLinksWithProtocol(rel, "https");
					try {
						SanitizedInstanceInfo sanitizedInstance = LookupManager.getInstance().getLookupClient()
								.getSanitizedInstanceInfo(internalInstance);
						if (sanitizedInstance != null) {
							links = RegistryLookupUtil.getLinksWithProtocol("https", sanitizedInstance.getLinks(rel));
						}
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
					List<Link> links = internalInstance.getLinksWithProtocol(rel, "http");
					try {
						SanitizedInstanceInfo sanitizedInstance = LookupManager.getInstance().getLookupClient()
								.getSanitizedInstanceInfo(internalInstance);
						if (sanitizedInstance != null) {
							links = RegistryLookupUtil.getLinksWithProtocol("http", sanitizedInstance.getLinks(rel));
						}
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
			e.printStackTrace();
			return lk;
		}
	}

	public static Link getServiceInternalLink(String serviceName, String version, String rel, String tenantName)
	{
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		Link lk = null;
		try {
			List<InstanceInfo> result = null;
			if (!StringUtil.isEmpty(tenantName) && RegistryLookupUtil.isServiceInApplicationShard(serviceName)) {
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
					List<Link> links = internalInstance.getLinksWithProtocol(rel, "https");
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
					List<Link> links = internalInstance.getLinksWithProtocol(rel, "http");
					if (links != null && links.size() > 0) {
						lk = links.get(0);
						return lk;
					}
				}
			}
			return lk;
		}
		catch (Exception e) {
			e.printStackTrace();
			return lk;
		}
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

	private static String getInternalEndPoint(List<InstanceInfo> instances)
	{
		if (instances == null) {
			return null;
		}
		String endPoint = null;
		for (InstanceInfo instance : instances) {
			endPoint = RegistryLookupUtil.getInternalEndPoint(instance);
			if (endPoint != null) {
				return endPoint;
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
			catch (Throwable thr) {
				return protocoledLinks;
			}
		}

		return protocoledLinks;
	}

	private static boolean isServiceInApplicationShard(String serviceName)
	{
		if (StringUtil.isEmpty(serviceName)) {
			return false;
		}
		if (serviceName.contains("LoganService") || serviceName.contains("EmcitasApplications")
				|| serviceName.contains("TargetAnalytics") || serviceName.contains("ApmUI")) {
			return true;
		}
		return false;
	}

}
