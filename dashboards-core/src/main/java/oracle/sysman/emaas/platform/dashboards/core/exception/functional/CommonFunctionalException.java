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
 * The class {@code CommonFunctionalException} and its subclasses are a form of {@code DashboardException} that indicates error
 * conditions of UI functional.
 *
 * @author guobaochen
 */
public class CommonFunctionalException extends DashboardException
{
	private static final long serialVersionUID = 4069848538099583269L;
	public static final String DASHBOARD_QUERY_INVALID_OFFSET = "DASHBOARD_QUERY_INVALID_OFFSET";
	public static final String DASHBOARD_QUERY_INVALID_LIMIT = "DASHBOARD_QUERY_INVALID_LIMIT";

	/**
	 * Constructs a new <code>CommonFunctionalException</code> with the specified detail message.
	 */
	public CommonFunctionalException(String message)
	{
		super(DashboardErrorConstants.DASHBOARD_COMMON_UI_ERROR_CODE, message);
	}

	/**
	 * Constructs a new <code>CommonFunctionalException</code> with the specified detail message and cause.
	 *
	 * @param message
	 * @param t
	 */
	public CommonFunctionalException(String message, Throwable t)
	{
		super(DashboardErrorConstants.DASHBOARD_COMMON_UI_ERROR_CODE, message, t);
	}

}
