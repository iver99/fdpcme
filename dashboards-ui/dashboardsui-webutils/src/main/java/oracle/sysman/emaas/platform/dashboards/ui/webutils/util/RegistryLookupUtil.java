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
import java.util.List;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.SanitizedInstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;

/**
 * @author miao
 */
public class RegistryLookupUtil
{
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

	public static EndpointEntity getServiceExternalEndPoint(String serviceName, String version)
	{
		Link link = RegistryLookupUtil.getServiceExternalLink(serviceName, version, "sso.endpoint/virtual");
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

	public static Link getServiceExternalLink(String serviceName, String version, String rel)
	{
		return RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel, false);
	}

	public static Link getServiceExternalLinkWithRelPrefix(String serviceName, String version, String rel)
	{
		return RegistryLookupUtil.getServiceExternalLink(serviceName, version, rel, true);
	}

	public static Link getServiceInternalLink(String serviceName, String version, String rel)
	{
		return RegistryLookupUtil.getServiceInternalLink(serviceName, version, rel, false);
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
				return protocoledLinks;
			}
		}

		return protocoledLinks;
	}

	private static Link getServiceExternalLink(String serviceName, String version, String rel, boolean prefixMatch)
	{
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		Link lk = null;
		try {
			List<InstanceInfo> result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
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
						SanitizedInstanceInfo sanitizedInstance = LookupManager.getInstance().getLookupClient()
								.getSanitizedInstanceInfo(internalInstance);
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
					List<Link> links = null;
					if (prefixMatch) {
						links = internalInstance.getLinksWithRelPrefixWithProtocol(rel, "http");
					}
					else {
						links = internalInstance.getLinksWithProtocol(rel, "http");
					}
					try {
						SanitizedInstanceInfo sanitizedInstance = LookupManager.getInstance().getLookupClient()
								.getSanitizedInstanceInfo(internalInstance);
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

	private static Link getServiceInternalLink(String serviceName, String version, String rel, boolean prefixMatch)
	{
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		Link lk = null;
		try {
			List<InstanceInfo> result = LookupManager.getInstance().getLookupClient().lookup(new InstanceQuery(info));
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

}