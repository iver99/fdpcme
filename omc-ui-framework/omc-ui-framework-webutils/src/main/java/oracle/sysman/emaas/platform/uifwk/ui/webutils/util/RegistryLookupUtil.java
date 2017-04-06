/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

import java.util.ArrayList;
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
    public static class VersionedLink extends Link
    {
        private String authToken;
        /**
         *
         */
        public VersionedLink()
        {
            // TODO Auto-generated constructor stub
        }

        public VersionedLink(Link link, String authToken)
        {
            withHref(link.getHref());
            withOverrideTypes(link.getOverrideTypes());
            withRel(link.getRel());
            withTypesStr(link.getTypesStr());
            this.authToken = authToken;
        }
        /**
         * @return the authToken
         */
        public String getAuthToken()
        {
            return authToken;
        }

        /**
         * @param authToken the authToken to set
         */
        public void setAuthToken(String authToken)
        {
            this.authToken = authToken;
        }
    }
    
	private static final Logger LOGGER = LogManager.getLogger(RegistryLookupUtil.class);
	private static final Logger itrLogger = LogUtil.getInteractionLogger();
	public static VersionedLink getServiceInternalLink(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceInternalLink(serviceName, version, rel, false, tenantName);
	}
	
    private static String getAuthorizationAccessToken(InstanceInfo instanceInfo) {
        char[] authToken = LookupManager.getInstance().getAuthorizationAccessToken(instanceInfo);
        return new String(authToken);
    }
    
    private static InstanceInfo getInstanceInfo(String serviceName, String version) {
        InstanceInfo.Builder builder = InstanceInfo.Builder.newBuilder().withServiceName(serviceName);
        if (!StringUtil.isEmpty(version)) {
            builder = builder.withVersion(version);
        }
        return builder.build();
    }

	private static VersionedLink getServiceInternalLink(String serviceName, String version, String rel, boolean prefixMatch,
			String tenantName)
	{
		LOGGER.debug(
				"/getServiceInternalLink/ Trying to retrieve service internal link for service: \"{}\", version: \"{}\", rel: \"{}\", prefixMatch: \"{}\", tenant: \"{}\"",
				serviceName, version, rel, prefixMatch, tenantName);
		InstanceInfo info = getInstanceInfo(serviceName, version);
		VersionedLink lk = null;
		try {
			List<InstanceInfo> result = null;
			if (!StringUtil.isEmpty(tenantName)) {
				InstanceInfo ins = LookupManager.getInstance().getLookupClient().getInstanceForTenant(info, tenantName);
				LOGGER.debug("Retrieved instance {} by using getInstanceForTenant for tenant {}", ins, tenantName);
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
			LogUtil.setInteractionLogThreadContext(tenantName, "Retristry lookup client", LogUtil.InteractionLogDirection.OUT);
			itrLogger.debug("Retrieved instance {} by using getInstanceForTenant for tenant {}", result, tenantName);
			if (result != null && !result.isEmpty()) {
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
					if (links != null && !links.isEmpty()) {
						lk = new VersionedLink(links.get(0), getAuthorizationAccessToken(internalInstance));
						itrLogger.debug("Retrieved link {} by using getLinks(WithRelPrefix)WithProtocol for rel={} for https",
								lk, rel);
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

	private RegistryLookupUtil()
	{
	}
}
