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
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

/**
 * @author jishshi
 */
public class UserOptionsNotFoundException extends DashboardException
{
	private static final String USER_OPTIONS_NOT_FOUND = "USER_OPTIONS_NOT_FOUND";

	private static final long serialVersionUID = 8936689004366890218L;

	public UserOptionsNotFoundException()
	{
		super(DashboardErrorConstants.USER_OPTIONS_NOT_FOUND_ERROR_CODE, MessageUtils
				.getDefaultBundleString(USER_OPTIONS_NOT_FOUND));
	}

}
