/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.util;

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
	private static final Logger LOGGER = LogManager.getLogger(RegistryLookupUtil.class);

	public static VersionedLink getServiceInternalLink(String serviceName, String version, String rel, String tenantName)
	{
		return RegistryLookupUtil.getServiceInternalLink(serviceName, version, rel, false, tenantName);
	}

	private static VersionedLink getServiceInternalLink(String serviceName, String version, String rel, boolean prefixMatch,
			String tenantName)
	{
		LOGGER.debug(
				"/getServiceInternalLink/ Trying to retrieve service internal link for service: \"{}\", version: \"{}\", rel: \"{}\", prefixMatch: \"{}\", tenant: \"{}\"",
				serviceName, version, rel, prefixMatch, tenantName);
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(serviceName).withVersion(version).build();
		VersionedLink lk = null;
		try {
			List<InstanceInfo> result = null;
			if (null != tenantName && !"".equals(tenantName.trim())) {
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
	
    public static String getAuthorizationAccessToken(InstanceInfo instanceInfo)
    {
        char[] authToken = LookupManager.getInstance().getAuthorizationAccessToken(instanceInfo);
        return new String(authToken);
    }
	
    public static class VersionedLink extends Link {
        private String authToken;

        public VersionedLink() {
            
        }

        public VersionedLink(Link link, String authToken) {
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
         * @param authToken
         *            the authToken to set
         */
        public void setAuthToken(String authToken)
        {
            this.authToken = authToken;
        }
    }
}
