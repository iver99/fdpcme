/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig;

import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * @author guobaochen
 */
public class LoggingItem
{
	private String loggerName;
	private String currentLoggingLevel;
	private Long lastUpdatedEpoch;

	public LoggingItem(LoggerConfig config, Long timestamp)
	{
		setCurrentLoggingLevel(config.getLevel().toString());
		setLoggerName(config.getName());
		lastUpdatedEpoch = timestamp;
	}

	/**
	 * @return the currentLoggingLevel
	 */
	public String getCurrentLoggingLevel()
	{
		return currentLoggingLevel;
	}

	/**
	 * @return the lastUpdatedEpoch
	 */
	public Long getLastUpdatedEpoch()
	{
		return lastUpdatedEpoch;
	}

	/**
	 * @return the loggerName
	 */
	public String getLoggerName()
	{
		return loggerName;
	}

	/**
	 * @param currentLoggingLevel
	 *            the currentLoggingLevel to set
	 */
	public void setCurrentLoggingLevel(String currentLoggingLevel)
	{
		this.currentLoggingLevel = currentLoggingLevel;
	}

	/**
	 * @param lastUpdatedEpoch
	 *            the lastUpdatedEpoch to set
	 */
	public void setLastUpdatedEpoch(Long lastUpdatedEpoch)
	{
		this.lastUpdatedEpoch = lastUpdatedEpoch;
	}

	/**
	 * @param loggerName
	 *            the loggerName to set
	 */
	public void setLoggerName(String loggerName)
	{
		this.loggerName = loggerName;
	}
}
