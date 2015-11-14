/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.management;

/**
 * @author aduan
 */
public interface IAppLoggingManageMXBean
{
	String getLogLevels();

	void setLogLevel(String logger, String level);

}
