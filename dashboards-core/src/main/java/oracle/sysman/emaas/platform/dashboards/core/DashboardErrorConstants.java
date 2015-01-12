/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

/**
 * Class to define constants that represent the error code returned to the end user For specific error code that has corresponding
 * error message, better please keep the constants name consistent with the message key. e.g.: error message resource key for
 * DASHBOARD_SAME_NAME_EXISTS_ERROR_CODE is DASHBOARD_SAME_NAME_EXISTS_ERROR
 *
 * @author guobaochen
 */
public class DashboardErrorConstants
{
	public static final Integer DASHBOARD_COMMON_UI_ERROR_CODE = 10000;
	public static final Integer DASHBOARD_SAME_NAME_EXISTS_ERROR_CODE = 10001;
	public static final Integer DASHBOARD_CREATE_SAME_ID_ERROR_CODE = 10002;

	public static final Integer DASHBOARD_COMMON_RESOURCE_ERROR_CODE = 20000;
	public static final Integer DASHBOARD_NOT_FOUND_ERROR_CODE = 20001;

	public static final Integer DASHBOARD_COMMON_SECURITY_ERROR_CODE = 30000;
	public static final Integer NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR_CODE = 30001;
}
