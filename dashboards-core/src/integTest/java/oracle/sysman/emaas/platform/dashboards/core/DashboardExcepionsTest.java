/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.PreferenceNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.TenantWithoutSubscriptionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.DeleteSystemDashboardException;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class DashboardExcepionsTest
{
	@SuppressWarnings("unused")
	@Test(groups = { "s2" })
	public void testDashboardExcepions()
	{
		CommonResourceException cre = new CommonResourceException(CommonResourceException.LOGGER_LEVEL_NOT_FOUND_TO_CONFIG);
		cre = new CommonResourceException(CommonResourceException.LOGGER_NOT_FOUND_TO_CONFIG);
		cre = new CommonResourceException(CommonResourceException.NOT_SUPPORT_UPDATE_IS_SYSTEM_FIELD);
		cre = new CommonResourceException(CommonResourceException.NOT_SUPPORT_UPDATE_TYPE_FIELD);
		cre = new CommonResourceException(CommonResourceException.NOT_SUPPORT_UPDATE_IS_SYSTEM_FIELD, new Exception());
		PreferenceNotFoundException pnfe = new PreferenceNotFoundException();
		TenantWithoutSubscriptionException twse = new TenantWithoutSubscriptionException();
		DeleteSystemDashboardException dsde = new DeleteSystemDashboardException();
		CommonSecurityException cse = new CommonSecurityException(CommonSecurityException.DASHBOARD_ACTION_REQUIRE_OWNER,
				new Exception());
		Assert.assertNotNull(cse.getErrorCode());
		CommonFunctionalException cfe = new CommonFunctionalException(CommonFunctionalException.DASHBOARD_INVALID_NAME_ERROR,
				new Exception());
	}
}
