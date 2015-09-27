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

/**
 * @author vinjoshi
 */
public class DashboardStatus implements DashboardStatusBean
{
	@Override
	public String getStatus()
	{
		if (!GlobalStatus.isDashboardUp()) {
			return GlobalStatus.STATUS_DOWN;
		}

		else if (GlobalStatus.isDashboardUp()) {

			return GlobalStatus.STATUS_UP;
		}
		else {
			return GlobalStatus.STATUS_OUT_OF_SERVICE;
		}

	}

	@Override
	public String getStatusMsg()
	{

		if (!GlobalStatus.isDashboardUp()) {
			return "SavedSearch is being stopped.";
		}

		else if (GlobalStatus.isDashboardUp()) {
			return "SavedSearch is up running.";
		}
		else {
			return "SavedSearch is out of service.";
		}

	}

}
