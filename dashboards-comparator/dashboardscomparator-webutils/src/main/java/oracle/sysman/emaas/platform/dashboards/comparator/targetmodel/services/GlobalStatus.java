/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.targetmodel.services;

import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalStatus
{
	public static final String STATUS_DOWN = "DOWN";
	public static final String STATUS_UP = "UP";
	public static final String STATUS_OUT_OF_SERVICE = "OUT_OF_SERVICE";
	private final static boolean DASHBOARDCOMPARATOR_UP = true;
	private final static boolean DASHBOARDCOMPARATOR_DOWN = false;
	private static AtomicBoolean dashboardComparatorStatus = new AtomicBoolean(DASHBOARDCOMPARATOR_UP);

	public static boolean isDashboardComparatorUp()
	{
		return dashboardComparatorStatus.get();
	}

	public static void setDashboardComparatorDownStatus()
	{
		dashboardComparatorStatus.set(DASHBOARDCOMPARATOR_DOWN);
	}

	public static void setDashboardComparatorUpStatus()
	{
		dashboardComparatorStatus.set(DASHBOARDCOMPARATOR_UP);
	}

}
