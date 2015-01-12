/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.exception.security;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;

/**
 * @author guobaochen
 */
public class DeleteSystemDashboardException extends DashboardException
{
	private static final long serialVersionUID = 5123899685789229699L;
	private static final String NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR = "NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR";

	/**
	 * Constructs a new <code>DeleteSystemDashboardException</code>
	 */
	public DeleteSystemDashboardException(Integer errorCode, String message)
	{
		super(DashboardErrorConstants.NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR_CODE, NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR);
	}

}
