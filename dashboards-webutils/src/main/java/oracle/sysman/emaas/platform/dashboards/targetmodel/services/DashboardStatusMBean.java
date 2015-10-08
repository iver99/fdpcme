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
public interface DashboardStatusMBean
{
	public String getStatus();

	public String getStatusMsg();
}
