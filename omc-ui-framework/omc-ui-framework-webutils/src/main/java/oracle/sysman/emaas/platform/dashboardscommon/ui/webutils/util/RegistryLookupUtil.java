/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboardscommon.ui.webutils.util;

import java.util.List;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author aduan
 */
public class RegistryLookupUtil
{
	private static final Logger logger = LogManager.getLogger(RegistryLookupUtil.class);

	public static Link getServiceInternalLink(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceInternalLink(serviceName, version, rel, false, tenantName);
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
			logger.error(e.getLocalizedMessage(), e);
			return lk;
		}
	}
}
