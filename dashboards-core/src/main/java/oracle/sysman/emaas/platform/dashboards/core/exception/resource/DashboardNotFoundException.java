/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.exception.resource;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;

/**
 * @author guobaochen
 */
public class DashboardNotFoundException extends DashboardException
{
	private static final String DASHBOARD_NOT_FOUND_ERROR = "DASHBOARD_NOT_FOUND_ERROR";

	private static final long serialVersionUID = 4946021196242068954L;

	/**
	 * Constructs a new <code>DashboardNotFoundException</code>
	 */
	public DashboardNotFoundException()
	{
		super(DashboardErrorConstants.DASHBOARD_NOT_FOUND_ERROR_CODE, DASHBOARD_NOT_FOUND_ERROR);
	}

}
