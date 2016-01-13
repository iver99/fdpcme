/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.util;

import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavorite;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardLastAccess;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;

/**
 * @author wenjzhu
 */
public class FacadeUtil
{
	public static EmsDashboard cloneEmsDashboard(EmsDashboard c)
	{

		EmsDashboard result = new EmsDashboard();
		result.getDashboardTileList();
		/*
		result.setdashboardId = c.dashboardId;
		result.deleted = c.deleted;
		result.description = c.description;
		result.enableTimeRange = c.enableTimeRange;
		result.enableRefresh = c.enableRefresh;
		result.isSystem = c.isSystem;
		result.sharePublic = c.sharePublic;
		result.lastModificationDate = c.lastModificationDate;
		result.lastModifiedBy = c.lastModifiedBy;
		result.name = c.name;
		result.owner = c.owner;
		result.screenShot = c.screenShot;
		result.type = c.type;
		result.applicationType = c.applicationType;*/
		return null;
	}

	public static EmsDashboardFavorite cloneEmsDashboardFavorite(EmsDashboardFavorite df)
	{
		return null;
	}

	public static EmsDashboardLastAccess cloneEmsDashboardLastAccess(EmsDashboardLastAccess dla)
	{
		return null;
	}

	public static EmsDashboardTile cloneEmsDashboardTile(EmsDashboardTile dt)
	{
		return null;
	}

	public static EmsDashboardTileParams cloneEmsDashboardTileParams(EmsDashboardTileParams tp)
	{
		return null;
	}

	public static EmsPreference cloneEmsPreference(EmsPreference p)
	{
		return null;
	}

	private FacadeUtil()
	{
	}
}
