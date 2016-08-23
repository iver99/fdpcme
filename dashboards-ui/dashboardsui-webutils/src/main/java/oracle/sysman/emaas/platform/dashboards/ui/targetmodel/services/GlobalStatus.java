/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.targetmodel.services;

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
	private final static boolean DASHBOARDUI_UP = true;
	private final static boolean DASHBOARDUI_DOWN = false;
	private static AtomicBoolean dashboardUIStatus = new AtomicBoolean(DASHBOARDUI_UP);

	public static boolean isDashboardUIUp()
	{
		return dashboardUIStatus.get();
	}

	public static void setDashboardUIDownStatus()
	{
		dashboardUIStatus.set(DASHBOARDUI_DOWN);
	}

	public static void setDashboardUIUpStatus()
	{
		dashboardUIStatus.set(DASHBOARDUI_UP);
	}

}
