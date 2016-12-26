/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 * @author wenjzhu
 */
public class ZDTContext
{

	private static final Logger logger = LogManager.getLogger(ZDTContext.class);

	private static final ThreadLocal<UUID> requestIdThreadLocal = new ThreadLocal<UUID>();
	private static final ThreadLocal<Long> requestTimeThreadLocal = new ThreadLocal<Long>();

	public static void clear()
	{
		ZDTContext.clearRequestTime();
		ZDTContext.clearRequestId();
	}

	public static void clearRequestId()
	{
		logger.debug("TenantContext RequestId  is cleared");
		UUID id = requestIdThreadLocal.get();
		if (id != null) {
			requestIdThreadLocal.remove();
		}
		ThreadContext.remove(LogUtil.INTERACTION_LOG_PROP_ZDT_REQID);
	}

	public static void clearRequestTime()
	{
		logger.debug("TenantContext RequestTime  is cleared");
		Long time = requestTimeThreadLocal.get();
		if (time != null) {
			requestTimeThreadLocal.remove();
		}
		ThreadContext.remove(LogUtil.INTERACTION_LOG_PROP_ZDT_REQTIME);
	}

	public static UUID getRequestId()
	{
		return requestIdThreadLocal.get();
	}

	public static Long getRequestTime()
	{
		return requestTimeThreadLocal.get();
	}

	public static void setRequestId(UUID uuid)
	{
		logger.debug("TenantContext RequestId is set with new UUID value {}", uuid.toString());
		requestIdThreadLocal.set(uuid);
		ThreadContext.put(LogUtil.INTERACTION_LOG_PROP_ZDT_REQID, uuid == null ? null : uuid.toString());
	}

	public static void setRequestTime(Long time)
	{
		logger.debug("TenantContext RequestId is set with new time value {}", time);
		requestTimeThreadLocal.set(time);
		ThreadContext.put(LogUtil.INTERACTION_LOG_PROP_ZDT_REQTIME, time == null ? null : time.toString());
	}

}
