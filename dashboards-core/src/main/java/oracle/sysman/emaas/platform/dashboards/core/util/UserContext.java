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
		return userThreadLocal.get();
	}

	public static void setCurrentUser(String user)
	{
		userThreadLocal.set(user);
	}

	private UserContext()
	{

	}
}
