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
 * The class {@code CommonResourceException} and its subclasses are a form of {@code DashboardException} that indicates error
 * conditions of invalid resource access.
 *
 * @author guobaochen
 */
public class CommonResourceException extends DashboardException
{
	private static final long serialVersionUID = 1159225613176742290L;

	public static final String NOT_SUPPORT_UPDATE_IS_SYSTEM_FIELD = "NOT_SUPPORT_UPDATE_IS_SYSTEM_FIELD";
	public static final String NOT_SUPPORT_UPDATE_TYPE_FIELD = "NOT_SUPPORT_UPDATE_TYPE_FIELD";
	public static final String LOGGER_NOT_FOUND_TO_CONFIG = "LOGGER_NOT_FOUND_TO_CONFIG";
	public static final String LOGGER_LEVEL_NOT_FOUND_TO_CONFIG = "LOGGER_LEVEL_NOT_FOUND_TO_CONFIG";

	/**
	 * Constructs a new <code>CommonResourceException</code> with the specified detail message.
	 *
	 * @param message
	 */
	public CommonResourceException(String message)
	{
		super(DashboardErrorConstants.DASHBOARD_COMMON_RESOURCE_ERROR_CODE, message);
	}

	/**
	 * Constructs a new <code>CommonResourceException</code> with the specified detail message and cause.
	 *
	 * @param message
	 * @param t
	 */
	public CommonResourceException(String message, Throwable t)
	{
		super(DashboardErrorConstants.DASHBOARD_COMMON_RESOURCE_ERROR_CODE, message, t);
	}

}
