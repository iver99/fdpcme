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

/**
 * @author guobaochen
 */
public interface IAppLoggingManageMXBean
{
	String getLogLevels();

	void setLogLevel(String logger, String level);

}
