/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws;

import java.io.IOException;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class ErrorEntityTest
{
	@Test
	public void testErrorEntity()
	{
		BasicServiceMalfunctionException e = new BasicServiceMalfunctionException("Test Message", "Test Message");
		ErrorEntity ee = new ErrorEntity(e);
		Assert.assertEquals(ee.getErrorCode(), DashboardErrorConstants.DASHBOARD_COMMON_SECURITY_ERROR_CODE);
		Assert.assertEquals(ee.getErrorMessage(), "Test Message - Test Message");

		IOException ioe = new IOException("IO Exception");
		ee = new ErrorEntity(ioe);
		Assert.assertEquals(ee.getErrorCode(), DashboardErrorConstants.DASHBOARD_COMMON_UI_ERROR_CODE);
		Assert.assertEquals(ee.getErrorMessage(), MessageUtils.getDefaultBundleString("DASHBOARD_JSON_PARSE_ERROR"));

		DashboardException de = new CommonFunctionalException("Test");
		ioe = new IOException("IO Exception", de);
		ee = new ErrorEntity(ioe);
		Assert.assertEquals(ee.getErrorCode(), DashboardErrorConstants.DASHBOARD_COMMON_UI_ERROR_CODE);
		Assert.assertEquals(ee.getErrorMessage(), "Test");
	}
}
