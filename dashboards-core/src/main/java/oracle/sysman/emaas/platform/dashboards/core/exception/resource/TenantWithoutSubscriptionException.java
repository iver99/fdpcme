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
 * @author guobaochen
 */
public class TenantWithoutSubscriptionException extends DashboardException
{
	public static final String TENANT_NO_SUBSCRIPTION_ERROR_CODE = "TENANT_NO_SUBSCRIPTION_ERROR_CODE";

	private static final long serialVersionUID = -3720835210461193239L;

	/**
	 * @param errorCode
	 * @param message
	 */
	public TenantWithoutSubscriptionException()
	{
		super(DashboardErrorConstants.TENANT_NO_SUBSCRIPTION_ERROR_CODE, MessageUtils
				.getDefaultBundleString(TENANT_NO_SUBSCRIPTION_ERROR_CODE));
	}
}
