/*
 * Copyright (C) 2016 Oracle
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
 * @author guochen
 *
 */
public class DatabaseDependencyUnavailableException extends DashboardException
{
	private static final long serialVersionUID = 1407510137550439509L;
	private static final String DATABASE_DEPENDENCY_UNAVAILABLE = "DATABASE_DEPENDENCY_UNAVAILABLE";
	
	/**
	 * @param errorCode
	 * @param message
	 */
	public DatabaseDependencyUnavailableException()
	{
		super(DashboardErrorConstants.DATABASE_DEPENDENCY_UNAVAILABLE_ERROR_CODE, MessageUtils
				.getDefaultBundleString(DATABASE_DEPENDENCY_UNAVAILABLE));
	}
}
