/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.webutils.wls.management;

import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

/**
 * @author guobaochen
 */
public class AppLoggingManageMXBean implements IAppLoggingManageMXBean
{
	private static final Logger sysLogger = LogManager.getLogger(AppLoggingManageMXBean.class);

	@Override
	public String getLogLevels()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');

		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
		Map<String, LoggerConfig> loggers = loggerContext.getConfiguration().getLoggers();
		for (LoggerConfig logger : loggers.values()) {

			if (logger.getLevel() != null) {
				sb.append('{').append('"').append(logger.getName()).append('"').append(':').append('"')
						.append(logger.getLevel().toString()).append('"').append('}');

				sb.append(',');
			}
		}

		if (sb.charAt(sb.length() - 1) == ',') {
			sb.replace(sb.length() - 1, sb.length(), "");
		}
		sb.append(']');
		return sb.toString();
	}

	@Override
	public void setLogLevel(String logger, String level)
	{
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration cfg = ctx.getConfiguration();
		LoggerConfig lc = cfg.getLoggerConfig(logger);
		if (lc == null) {
			return;
		}

		if (Level.DEBUG.name().equalsIgnoreCase(level)) {
			lc.setLevel(Level.DEBUG);
		}
		else if (Level.INFO.name().equalsIgnoreCase(level)) {
			lc.setLevel(Level.INFO);
		}
		else if (Level.WARN.name().equalsIgnoreCase(level)) {
			lc.setLevel(Level.WARN);
		}
		else if (Level.ERROR.name().equalsIgnoreCase(level)) {
			lc.setLevel(Level.ERROR);
		}
		else if (Level.FATAL.name().equalsIgnoreCase(level)) {
			lc.setLevel(Level.FATAL);
		}
		ctx.updateLoggers();
		sysLogger.info("Logging MXBean sets the log level to {}", lc.getLevel().name());
	}
}
