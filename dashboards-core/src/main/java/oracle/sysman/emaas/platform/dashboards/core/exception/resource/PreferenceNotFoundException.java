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
 * @author wenjzhu
 */
public class PreferenceNotFoundException extends DashboardException
{
	private static final String PREFERENCE_KEY_NOT_FOUND = "PREFERENCE_KEY_NOT_FOUND";

	/**
	 *
	 */
	private static final long serialVersionUID = 4301668288659199279L;

	public PreferenceNotFoundException()
	{
		super(DashboardErrorConstants.DASHBOARD_NOT_FOUND_ERROR_CODE, MessageUtils
				.getDefaultBundleString(PREFERENCE_KEY_NOT_FOUND));
	}

}
