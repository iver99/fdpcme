/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author guobaochen
 */
@ApplicationPath("api")
public class DashboardsApplication extends Application
{
	@Override
	public Set<Class<?>> getClasses()
	{
		Set<Class<?>> hs = new HashSet<Class<?>>();
		hs.add(DashboardAPI.class);
		hs.add(UserOptionsAPI.class);
		hs.add(FavoriteAPI.class);
		hs.add(PreferenceAPI.class);
		hs.add(LoggingAPI.class);
		hs.add(LoggingConfigAPI.class);
		hs.add(TenantSubscriptionsAPI.class);
		hs.add(ConfigurationAPI.class);
		hs.add(RegistryLookupAPI.class);
		return hs;
	}
}
