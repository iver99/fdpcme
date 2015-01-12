/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.exception.functional;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;

/**
 * @author guobaochen
 */
public class DashboardSameIdException extends DashboardException
{
	private static final long serialVersionUID = 8950929902841666949L;

	private static final String DASHBOARD_CREATE_SAME_ID_ERROR = "DASHBOARD_CREATE_SAME_ID_ERROR";

	/**
	 * Constructs a new <code>DashboardSameIdException</code>
	 */
	public DashboardSameIdException()
	{
		super(DashboardErrorConstants.DASHBOARD_CREATE_SAME_ID_ERROR_CODE, DASHBOARD_CREATE_SAME_ID_ERROR);
	}
}
