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

import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;

/**
 * @author miao
 */
public class DashboardAPIUtil
{
	public static final String DASHBOARD_API_SERVICENAME = "Dashboard-API";

	public static final String DASHBOARD_API_VERSION = "0.1";

	public static String getExternalAPIBase()
	{
		String externalBase = RegistryLookupUtil.getServiceExternalEndPoint(DashboardAPIUtil.DASHBOARD_API_SERVICENAME,
				DashboardAPIUtil.DASHBOARD_API_VERSION);
		return externalBase; //e.g. https://slc07hcn.us.oracle.com:4443/microservice/be1d3d5f-2bd4-44ff-aab1-2b5aab1c7493/
	}
}
