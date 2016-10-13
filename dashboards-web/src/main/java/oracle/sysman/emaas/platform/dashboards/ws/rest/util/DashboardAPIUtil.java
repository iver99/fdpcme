/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;

/**
 * @author miao
 */
public class DashboardAPIUtil
{
	
	public static final String DASHBOARD_API_SERVICENAME = "Dashboard-API";

	public static final String DASHBOARD_API_VERSION = "1.0+";

	private static final String DASHBOARD_API_STATIC_REL = "sso.static/dashboards.service";
	private static final String PREFERENCE_API_STATIC_REL = "sso.static/dashboards.preferences";

	public static String getExternalDashboardAPIBase(String tenantName)
	{
		Link lnk = RegistryLookupUtil.getServiceExternalLink(DashboardAPIUtil.DASHBOARD_API_SERVICENAME,
				DashboardAPIUtil.DASHBOARD_API_VERSION, DASHBOARD_API_STATIC_REL, tenantName);
		return lnk == null ? null : lnk.getHref(); //e.g. https://slc07hcn.us.oracle.com:4443/microservice/be1d3d5f-2bd4-44ff-aab1-2b5aab1c7493/
	}

	public static String getExternalPreferenceAPIBase(String tenantName)
	{
		Link lnk = RegistryLookupUtil.getServiceExternalLink(DashboardAPIUtil.DASHBOARD_API_SERVICENAME,
				DashboardAPIUtil.DASHBOARD_API_VERSION, PREFERENCE_API_STATIC_REL, tenantName);
		return lnk == null ? null : lnk.getHref();
	}
}
