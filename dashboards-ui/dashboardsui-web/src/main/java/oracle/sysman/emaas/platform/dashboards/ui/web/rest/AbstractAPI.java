/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.web.rest;

import oracle.sysman.emaas.platform.dashboards.ui.web.rest.exception.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.ui.web.rest.util.MessageUtils;

/**
 * @author guobaochen
 */
public class AbstractAPI
{
	protected void validateTenantIdUserName(String opcTenantId, String userTenant) throws CommonSecurityException
	{
		if (opcTenantId == null || "".equals(opcTenantId)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.X_USER_IDENTITY_DOMAIN_NAME_REQUIRED));
		}
		if (userTenant == null || "".equals(userTenant)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		int idx = userTenant.indexOf(".");
		if (idx <= 0) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		String userName = userTenant.substring(idx + 1, userTenant.length());
		if (userName == null || "".equals(userName)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
		String tenantName = userTenant.substring(0, idx);
		if (tenantName == null || "".equals(tenantName)) {
			throw new CommonSecurityException(
					MessageUtils.getDefaultBundleString(CommonSecurityException.VALID_X_REMOTE_USER_REQUIRED));
		}
	}
}
