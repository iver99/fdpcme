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

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * @author guobaochen
 */
public class LogUtil
{
	private LogUtil() {
	  }

	/**
	 * Direction for interaction log
	 *
	 * @author guobaochen
	 */
	public static enum InteractionLogDirection
	{
		/**
		 * for all incoming service requests
		 */
		IN("IN"),
		/**
		 * for all outbound service request
		 */
		OUT("OUT"),
		/**
		 * indicate that direction of service request is not available
		 */
		NA("N/A");

		public static InteractionLogDirection fromValue(String value)
		{
			for (InteractionLogDirection ild : InteractionLogDirection.values()) {
				if (ild.getValue().equals(value)) {
					return ild;
				}
			}
			return NA;
		}

		private final String value;

		private InteractionLogDirection(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
	}

	private static final Logger LOGGER = LogManager.getLogger(LogUtil.class);
	public static final String INTERACTION_LOG_PROP_TENANTID = "tenantId";
	public static final String INTERACTION_LOG_VALUE_NA = "N/A";
	public static final String INTERACTION_LOG_PROP_SERVICE_INVOKED = "serviceInvoked";
	public static final String INTERACTION_LOG_PROP_DIRECTION = "direction";

	private static final String INTERACTION_LOG_NAME = "oracle.sysman.emaas.platform.dashboards.ui.interaction.log";

	private static final String LOGGER_PROP_UPDATE_TIME = "DF_UPDATE_TIME";

	/**
	 * Clear the dashboard interaction log context
	 */
	public static void clearInteractionLogContext()
	{
		ThreadContext.remove(INTERACTION_LOG_PROP_TENANTID);
		ThreadContext.remove(INTERACTION_LOG_PROP_SERVICE_INVOKED);
		ThreadContext.remove(INTERACTION_LOG_PROP_DIRECTION);
	}

	/**
	 * Returns the DashboardService-API interaction log
	 *
	 * @return
	 */
	public static final Logger getInteractionLogger()
	{
		return LogManager.getLogger(INTERACTION_LOG_NAME);
	}

	/**
	 * Retrieve the long timestamp for update time for specified LOGGER
	 *
	 * @param cfg
	 * @param lc
	 * @return
	 */
	public static Long getLoggerUpdateTime(Configuration cfg, LoggerConfig lc)
	{
		Map<String, String> cfgProps = cfg.getProperties();
		if (cfgProps == null) {
			return null;
		}
		String time = cfgProps.get(LOGGER_PROP_UPDATE_TIME + lc.getName());
		if (time == null) {
			return null;
		}
		return Long.valueOf(time);
	}

	/**
	 * Initialize dashboard interaction log context
	 *
	 * @param serviceInvoked
	 * @param direction
	 */
	public static void setInteractionLogThreadContext(String tenantId, String serviceInvoked, InteractionLogDirection direction)
	{
		if (StringUtil.isEmpty(tenantId)) {
			LOGGER.debug("Initialize interaction log context: tenantId is null or empty");
			tenantId = INTERACTION_LOG_VALUE_NA;
		}
		if (StringUtil.isEmpty(serviceInvoked)) {
			LOGGER.debug("Failed to initialize interaction log context: serviceInvoked is null or empty");
			serviceInvoked = "Service invoked: N/A";
		}
		if (direction == null) {
			LOGGER.debug("Failed to initialize interaction log context: direction is null");
			direction = InteractionLogDirection.NA;
		}
		ThreadContext.put(INTERACTION_LOG_PROP_TENANTID, tenantId);
		ThreadContext.put(INTERACTION_LOG_PROP_SERVICE_INVOKED, serviceInvoked);
		ThreadContext.put(INTERACTION_LOG_PROP_DIRECTION, direction.getValue());
	}

	/**
	 * Sets the update timestamp for specified LOGGER
	 *
	 * @param cfg
	 * @param lc
	 * @param timestamp
	 */
	public static void setLoggerUpdateTime(Configuration cfg, LoggerConfig lc, Long timestamp)
	{
		if (timestamp == null) {
			return;
		}
		Map<String, String> cfgProps = cfg.getProperties();
		cfgProps.put(LOGGER_PROP_UPDATE_TIME + lc.getName(), timestamp.toString());
	}
}
