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

import  oracle.sysman.emaas.platform.dashboards.ui.targetmodel.services.DashboardUIStatusBean;
/**
 * @author vinjoshi
 */
public class DashboardUIStatus implements DashboardUIStatusBean
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
			return "SavedSearch is being stopped.";
		}

		else if (GlobalStatus.isDashboardUIUp()) {
			return "SavedSearch is up running.";
		}
		else {
			return "SavedSearch is out of service.";
		}

	}

}
