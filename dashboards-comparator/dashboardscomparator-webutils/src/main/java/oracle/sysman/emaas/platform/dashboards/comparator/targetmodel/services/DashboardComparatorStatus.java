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

public class DashboardComparatorStatus implements DashboardComparatorStatusMBean
{
	@Override
	public String getStatus()
	{
		if (!GlobalStatus.isDashboardComparatorUp()) {
			return GlobalStatus.STATUS_DOWN;
		}

		else if (GlobalStatus.isDashboardComparatorUp()) {

			return GlobalStatus.STATUS_UP;
		}
		else {
			return GlobalStatus.STATUS_OUT_OF_SERVICE;
		}

	}

	@Override
	public String getStatusMsg()
	{

		if (!GlobalStatus.isDashboardComparatorUp()) {
			return "Dashboard-Comparator is being stopped.";
		}

		else if (GlobalStatus.isDashboardComparatorUp()) {
			return "Dashboard-Comparator is up and running.";
		}
		else {
			return "Dashboard-Comparator is out of service.";
		}

	}

}
