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

import java.util.Locale;

/**
 * @author guobaochen
 */
public class UserContext
{
	private static final ThreadLocal<String> userThreadLocal = new ThreadLocal<String>();
	private static final ThreadLocal<Locale> localeThreadLocal = new ThreadLocal<Locale>();

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
	
    public static void clearLocale()
    {
        Locale locale = localeThreadLocal.get();
        if (locale != null) {
            localeThreadLocal.remove();
        }
    }
    // default local is Locale.US
    public static Locale getLocale()
    {
        Locale locale = localeThreadLocal.get();
        return locale == null ? Locale.US : locale;
    }
    public static void setLocale(Locale locale)
    {
        localeThreadLocal.set(locale);
    }
    
    public static void clear() {
        clearCurrentUser();
        clearLocale();
    }

	private UserContext()
	{

	}
}
