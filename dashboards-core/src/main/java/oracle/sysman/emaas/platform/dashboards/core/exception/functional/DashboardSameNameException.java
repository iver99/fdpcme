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
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

/**
 * @author guobaochen
 */
public class DashboardSameNameException extends DashboardException
{
	private static final String DASHBOARD_SAME_NAME_EXISTS_ERROR = "DASHBOARD_SAME_NAME_EXISTS_ERROR";
	private static final long serialVersionUID = 6161695271673357859L;

	/**
	 * Constructs a new <code>DashboardSameNameException</code>
	 */
	public DashboardSameNameException()
	{
		super(DashboardErrorConstants.DASHBOARD_SAME_NAME_EXISTS_ERROR_CODE, MessageUtils
				.getDefaultBundleString(DASHBOARD_SAME_NAME_EXISTS_ERROR));
	}

}
