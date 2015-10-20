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

/**
 * @author vinjoshi
 */
public class DashboardUIStatus implements DashboardUIStatusMBean
{
	@Override
	public String getStatus()
	{
		if (!GlobalStatus.isDashboardUIUp()) {
			return GlobalStatus.STATUS_DOWN;
		}

		else if (GlobalStatus.isDashboardUIUp()) {

			return GlobalStatus.STATUS_UP;
		}
		else {
			return GlobalStatus.STATUS_OUT_OF_SERVICE;
		}

	}

	@Override
	public String getStatusMsg()
	{

		if (!GlobalStatus.isDashboardUIUp()) {
			return "Dashboard-UI is being stopped.";
		}

		else if (GlobalStatus.isDashboardUIUp()) {
			return "Dashboard-UI is up and running.";
		}
		else {
			return "Dashboard-UI is out of service.";
		}

	}

}
