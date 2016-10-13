/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.targetmodel.services;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author vinjoshi
 */
public class GlobalStatus
{
	private GlobalStatus() {
	  }

	public static final String STATUS_DOWN = "DOWN";
	public static final String STATUS_UP = "UP";
	public static final String STATUS_OUT_OF_SERVICE = "OUT_OF_SERVICE";
	private final static boolean DASHBOARD_UP = true;
	private final static boolean DASHBOARD_DOWN = false;
	private static AtomicBoolean dashboardStatus = new AtomicBoolean(DASHBOARD_UP);

	public static boolean isDashboardUp()
	{
		return dashboardStatus.get();
	}

	public static void setDashboardDownStatus()
	{
		dashboardStatus.set(DASHBOARD_DOWN);
	}

	public static void setDashboardUpStatus()
	{
		dashboardStatus.set(DASHBOARD_UP);
	}

}
