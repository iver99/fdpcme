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
 * The class {@code CommonSecurityException} and its subclasses are a form of {@code DashboardException} that indicates error
 * conditions of security issues.
 *
 * @author guobaochen
 */
public class CommonSecurityException extends DashboardException
{
	private static final long serialVersionUID = 6034178675441388401L;

	public static final String X_USER_IDENTITY_DOMAIN_REQUIRED = "X_USER_IDENTITY_DOMAIN_REQUIRED";
	public static final String VALID_X_REMOTE_USER_REQUIRED = "VALID_X_REMOTE_USER_REQUIRED";
	public static final String TENANT_NAME_NOT_RECOGNIZED = "TENANT_NAME_NOT_RECOGNIZED";

	/**
	 * Constructs a new <code>CommonSecurityException</code> with the specified detail message.
	 *
	 * @param message
	 */
	public CommonSecurityException(String message)
	{
		super(DashboardErrorConstants.DASHBOARD_COMMON_SECURITY_ERROR_CODE, message);
	}

	/**
	 * Constructs a new <code>CommonSecurityException</code> with the specified detail message and cause.
	 *
	 * @param message
	 * @param t
	 */
	public CommonSecurityException(String message, Throwable t)
	{
		super(DashboardErrorConstants.DASHBOARD_COMMON_SECURITY_ERROR_CODE, message, t);
	}

}
