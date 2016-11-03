/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

/**
 * @author guobaochen
 */
public class UserContext
{
	private static final ThreadLocal<String> userThreadLocal = new ThreadLocal<String>();

	public static void clearCurrentUser()
	{
		String user = userThreadLocal.get();
		if (user != null) {
			userThreadLocal.remove();
		}
	}

	public static String getCurrentUser()
	{
		String tenantAndUser = userThreadLocal.get();
		if(tenantAndUser != null){
			String currentUser = tenantAndUser.substring(tenantAndUser.indexOf(".")+1);
			return currentUser;
		}else{
			return tenantAndUser;
		}
	}

	public static String getUserTenant()
	{
		return userThreadLocal.get();
	}

	public static void setCurrentUser(String user)
	{
		userThreadLocal.set(user);
	}

	public static void setUserTenant(String tenant){
		userThreadLocal.set(tenant);
	}

	private UserContext()
	{

	}
}
