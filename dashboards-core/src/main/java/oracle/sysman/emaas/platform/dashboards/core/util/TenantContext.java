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

/**
 * @author guobaochen
 */
public class TenantContext
{
	private static final ThreadLocal<String> tenantThreadLocal = new ThreadLocal<String>();

	public static void clearCurrentUser()
	{
		String tenant = tenantThreadLocal.get();
		if (tenant != null) {
			tenantThreadLocal.remove();
		}
	}

	public static String getCurrentTenant()
	{
		return tenantThreadLocal.get();
	}

	public static void setCurrentTenant(String tenant)
	{
		tenantThreadLocal.set(tenant);
	}

	private TenantContext()
	{

	}

}
