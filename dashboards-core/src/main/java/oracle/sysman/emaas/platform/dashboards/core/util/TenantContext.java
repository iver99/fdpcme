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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 * @author guobaochen
 */
public class TenantContext
{
	private static final Logger logger = LogManager.getLogger(TenantContext.class);

	private static final ThreadLocal<String> tenantThreadLocal = new ThreadLocal<String>();

	public static void clearCurrentUser()
	{
		logger.debug("TenantContext is cleared");
		String tenant = tenantThreadLocal.get();
		if (tenant != null) {
			tenantThreadLocal.remove();
		}
		ThreadContext.remove(LogUtil.INTERACTION_LOG_PROP_TENANTID);
	}

	public static String getCurrentTenant()
	{
		return tenantThreadLocal.get();
	}

	public static void setCurrentTenant(String tenant)
	{
		logger.debug("TenantContext is set with new tenant value {}", tenant);
		tenantThreadLocal.set(tenant);
		ThreadContext.put(LogUtil.INTERACTION_LOG_PROP_TENANTID, tenant);
	}

	private TenantContext()
	{
	}

}
