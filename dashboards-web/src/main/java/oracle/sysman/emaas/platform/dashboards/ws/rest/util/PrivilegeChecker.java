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
import java.util.List;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.ws.rest.model.RoleNamesEntity;

import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author aduan
 */
public class PrivilegeChecker
{
	private static final Logger LOGGER = LogManager.getLogger(PrivilegeChecker.class);

	public static final String ADMIN_ROLE_NAME_APM = "APM Administrator";
	public static final String ADMIN_ROLE_NAME_ITA = "IT Analytics Administrator";
	public static final String ADMIN_ROLE_NAME_LA = "Log Analytics Administrator";
	public static final String ADMIN_ROLE_NAME_MONITORING = "Monitoring Service Administrator";
	public static final String ADMIN_ROLE_NAME_SECURITY = "Security Analytics Administrator";
	public static final String ADMIN_ROLE_NAME_COMPLIANCE = "Compliance Administrator";
	public static final String ADMIN_ROLE_NAME_ORCHESTRATION = "Orchestration Administrator";
	public static final String ADMIN_ROLE_NAME_OMC = "OMC Administrator";
	public static final String SECURITY_AUTH_ROLE_CHECK_API = "api/v1/roles/grants/getRoles?grantee=";
	private static final String SECURITY_AUTHORIZATION_SERVICENAME = "SecurityAuthorization";
	private static final String SECURITY_AUTHORIZATION_VERSION = "1.0+";

	public static List<String> getUserRoles(String tenantName, String userName)
	{
		List<String> roleNames = null;
		if (tenantName != null && userName != null) {
			try {
				String endPoint = RegistryLookupUtil.getServiceInternalEndpoint(SECURITY_AUTHORIZATION_SERVICENAME,
						SECURITY_AUTHORIZATION_VERSION, tenantName);
				if (endPoint == null) {
					LOGGER.error("Failed to discover SecurityAuthorization service URL for privilege checking.");
				}
				else {
					String tenantDotUser = tenantName + "." + userName;
					String secAuthRolesApiUrl = endPoint.endsWith("/") ? endPoint + SECURITY_AUTH_ROLE_CHECK_API + tenantDotUser
							: endPoint + "/" + SECURITY_AUTH_ROLE_CHECK_API + tenantDotUser;
					RestClient rc = new RestClient();
					rc.setHeader("X-USER-IDENTITY-DOMAIN-NAME",tenantName);
					rc.setHeader("OAM_REMOTE_USER",tenantDotUser);
					rc.setType(null);
					rc.setAccept(null);
					String roleCheckResponse = rc.get(secAuthRolesApiUrl, tenantName);
					LOGGER.debug("Checking roles for tenant user (" + tenantDotUser + "). The response is " + roleCheckResponse);
					JsonUtil ju = JsonUtil.buildNormalMapper();
					RoleNamesEntity rne = ju.fromJson(roleCheckResponse, RoleNamesEntity.class);
					if (rne != null && rne.getRoleNames() != null) {
						roleNames = rne.getRoleNames();
					}
				}
			}catch (IOException e) {
				LOGGER.error(e);
			}
		}
		else {
			LOGGER.warn("Tenant name or user name is empty. isAdminUser checking is skipped.");
		}

		return roleNames;
	}

	public static boolean isAdminUser(List<String> userRoles)
	{
		boolean isAdmin = false;
		if (userRoles != null && !userRoles.isEmpty()) {
			if (userRoles.contains(ADMIN_ROLE_NAME_OMC) || userRoles.contains(ADMIN_ROLE_NAME_APM)
					|| userRoles.contains(ADMIN_ROLE_NAME_ITA) || userRoles.contains(ADMIN_ROLE_NAME_LA)
					|| userRoles.contains(ADMIN_ROLE_NAME_MONITORING) || userRoles.contains(ADMIN_ROLE_NAME_SECURITY)
					|| userRoles.contains(ADMIN_ROLE_NAME_COMPLIANCE) || userRoles.contains(ADMIN_ROLE_NAME_ORCHESTRATION)) {
				isAdmin = true;
			}
		}
		return isAdmin;
	}

	private PrivilegeChecker()
	{
	}
}
