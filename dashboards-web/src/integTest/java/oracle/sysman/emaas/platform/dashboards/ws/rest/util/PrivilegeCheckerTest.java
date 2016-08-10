/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.RoleNamesEntity;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class PrivilegeCheckerTest
{
	@Test(groups = { "s2" })
	public void testGetUserRoles(@Mocked final RegistryLookupUtil lookupUtil, @Mocked final TenantSubscriptionUtil.RestClient rc,
			@Mocked final JsonUtil jsonUtl)
	{
		String tenantName = "emaastesttenant1";
		String userName = "emcsadmin";

		// Test case when failed to discover the SecurityAuthorization service link
		new Expectations() {
			{
				RegistryLookupUtil.getServiceInternalEndpoint(anyString, anyString, anyString);
				result = null;
			}
		};
		Assert.assertNull(PrivilegeChecker.getUserRoles(tenantName, userName));

		// Test case for getting user roles successfully
		new Expectations() {
			{
				RegistryLookupUtil.getServiceInternalEndpoint(anyString, anyString, anyString);
				result = "http://hostname:port/testapi";
				rc.get(anyString, anyString, anyString);
				result = "{\"roleNames\": [\"APM Administrator\",\"APM User\",\"IT Analytics Administrator\",\"Log Analytics Administrator\",\"Log Analytics User\",\"IT Analytics User\"]}";
			}
		};
		Assert.assertNotNull(PrivilegeChecker.getUserRoles(tenantName, userName));
		Assert.assertNotNull(PrivilegeChecker.getUserRoles(tenantName, userName).size() == 6);

		// Test case for IOException for JsonUtil.fromJson()
		try {
			new Expectations() {
				{
					JsonUtil.buildNormalMapper();
					result = jsonUtl;
					jsonUtl.fromJson(anyString, RoleNamesEntity.class);
					result = new IOException();
				}
			};
		}
		catch (IOException e) {
			//Ignore the expected exception here
		}
		Assert.assertNull(PrivilegeChecker.getUserRoles(tenantName, userName));
	}

	@Test(groups = { "s1" })
	public void testGetUserRolesWithNullInput()
	{
		Assert.assertNull(PrivilegeChecker.getUserRoles(null, "TestUser"));
		Assert.assertNull(PrivilegeChecker.getUserRoles("TestTenant", null));
		Assert.assertNull(PrivilegeChecker.getUserRoles(null, null));
	}

	@Test(groups = { "s1" })
	public void testIsAdminUser()
	{
		Assert.assertFalse(PrivilegeChecker.isAdminUser(null));
		Assert.assertFalse(PrivilegeChecker.isAdminUser(new ArrayList<String>()));
		Assert.assertFalse(PrivilegeChecker.isAdminUser(Arrays.asList("APM User")));
		Assert.assertFalse(PrivilegeChecker.isAdminUser(Arrays.asList("Log Analytics User")));
		Assert.assertFalse(PrivilegeChecker.isAdminUser(Arrays.asList("IT Analytics User")));
		Assert.assertFalse(PrivilegeChecker.isAdminUser(Arrays.asList("Monitoring Service User")));
		Assert.assertFalse(PrivilegeChecker.isAdminUser(Arrays.asList("Orchestration User")));
		Assert.assertFalse(PrivilegeChecker.isAdminUser(Arrays.asList("APM User", "Monitoring Service User")));
		Assert.assertFalse(PrivilegeChecker.isAdminUser(Arrays.asList("Security Analytics User")));
		Assert.assertFalse(PrivilegeChecker.isAdminUser(Arrays.asList("Compliance User")));
		Assert.assertTrue(PrivilegeChecker.isAdminUser(Arrays.asList(PrivilegeChecker.ADMIN_ROLE_NAME_APM)));
		Assert.assertTrue(PrivilegeChecker.isAdminUser(Arrays.asList(PrivilegeChecker.ADMIN_ROLE_NAME_ITA)));
		Assert.assertTrue(PrivilegeChecker.isAdminUser(Arrays.asList(PrivilegeChecker.ADMIN_ROLE_NAME_LA)));
		Assert.assertTrue(PrivilegeChecker.isAdminUser(Arrays.asList(PrivilegeChecker.ADMIN_ROLE_NAME_MONITORING)));
		Assert.assertTrue(PrivilegeChecker.isAdminUser(Arrays.asList(PrivilegeChecker.ADMIN_ROLE_NAME_MONITORING,
				"Monitoring Service User")));
		Assert.assertTrue(PrivilegeChecker.isAdminUser(Arrays.asList(PrivilegeChecker.ADMIN_ROLE_NAME_SECURITY)));
		Assert.assertTrue(PrivilegeChecker.isAdminUser(Arrays.asList(PrivilegeChecker.ADMIN_ROLE_NAME_ORCHESTRATION)));
		Assert.assertTrue(PrivilegeChecker.isAdminUser(Arrays.asList(PrivilegeChecker.ADMIN_ROLE_NAME_COMPLIANCE)));
	}
}
