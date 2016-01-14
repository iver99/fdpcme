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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	public static EmsDashboard cloneEmsDashboard(EmsDashboard dsb)
	{

		EmsDashboard result = new EmsDashboard();
		result.setApplicationType(dsb.getApplicationType());
		result.setCreationDate(dsb.getCreationDate());

		List<EmsDashboardTile> dashboardTileList = new ArrayList<EmsDashboardTile>();
		EmsDashboardTile emsDashboardTile;
		Iterator<EmsDashboardTile> iterator = dsb.getDashboardTileList().iterator();
		while(iterator.hasNext()) {
			emsDashboardTile = cloneEmsDashboardTile(iterator.next());
			dashboardTileList.add(emsDashboardTile);
		}
		result.setDashboardTileList(dashboardTileList);
		
		result.setDeleted(dsb.getDeleted());
		result.setDescription(dsb.getDescription());
		result.setEnableRefresh(dsb.getEnableRefresh());
		result.setEnableTimeRange(dsb.getEnableTimeRange());
		result.setIsSystem(dsb.getIsSystem());
		result.setLastModificationDate(dsb.getLastModificationDate());
		result.setLastModifiedBy(dsb.getLastModifiedBy());
		result.setName(dsb.getName());
		result.setOwner(dsb.getOwner());
		result.setScreenShot(dsb.getScreenShot());
		result.setSharePublic(dsb.getSharePublic());
		result.setTenantId(dsb.getTenantId());
		result.setType(dsb.getType());

		return result;
	}

	public static EmsDashboardFavorite cloneEmsDashboardFavorite(EmsDashboardFavorite df)
	{
		EmsDashboardFavorite result = new EmsDashboardFavorite();
		result.setCreationDate(df.getCreationDate());
		result.setDashboard(cloneEmsDashboard(df.getDashboard()));
		result.setUserName(df.getUserName());
		
		return null;
	}

	public static EmsDashboardLastAccess cloneEmsDashboardLastAccess(EmsDashboardLastAccess dla)
	{
		EmsDashboardLastAccess result = new EmsDashboardLastAccess();
		result.setAccessDate(dla.getAccessDate());
		result.setAccessedBy(dla.getAccessedBy());
		result.setDashboardId(dla.getDashboardId());
		
		return result;
	}

	public static EmsDashboardTile cloneEmsDashboardTile(EmsDashboardTile dt)
	{
		EmsDashboardTile result = new EmsDashboardTile();
		result.setColumn(dt.getColumn());
		result.setCreationDate(dt.getCreationDate());
		result.setDashboard(cloneEmsDashboard(dt.getDashboard()));
		
		List<EmsDashboardTileParams> dashboardTileParamsList = new ArrayList<EmsDashboardTileParams>();
		EmsDashboardTileParams emsDashboardTileParams;
		for(int i=0; i<dt.getDashboardTileParamsList().size(); i++) {
			emsDashboardTileParams = cloneEmsDashboardTileParams(dt.getDashboardTileParamsList().get(i));
			dashboardTileParamsList.add(emsDashboardTileParams);
		}		
		result.setDashboardTileParamsList(dashboardTileParamsList);
		
		result.setHeight(dt.getHeight());
		result.setIsMaximized(dt.getIsMaximized());
	    result.setLastModificationDate(dt.getLastModificationDate());
	    result.setLastModifiedBy(dt.getLastModifiedBy());
	    result.setOwner(dt.getOwner());
	    result.setPosition(dt.getPosition());
	    result.setProviderAssetRoot(dt.getProviderAssetRoot());
	    result.setProviderName(dt.getProviderName());
	    result.setProviderVersion(dt.getProviderVersion());
	    result.setRow(dt.getRow());
	    result.setTitle(dt.getTitle());
	    result.setType(dt.getType());
	    result.setWidgetCreationTime(dt.getWidgetCreationTime());
	    result.setWidgetDescription(dt.getWidgetDescription());
	    result.setWidgetGroupName(dt.getWidgetGroupName());
	    result.setWidgetHistogram(dt.getWidgetHistogram());
	    result.setWidgetIcon(dt.getWidgetIcon());
	    result.setWidgetKocName(dt.getWidgetKocName());
	    result.setWidgetLinkedDashboard(dt.getWidgetLinkedDashboard());
	    result.setWidgetName(dt.getWidgetName());
	    result.setWidgetOwner(dt.getWidgetOwner());
	    result.setWidgetSource(dt.getWidgetSource());
	    result.setWidgetSupportTimeControl(dt.getWidgetSupportTimeControl());
	    result.setWidgetTemplate(dt.getWidgetTemplate());
	    result.setWidgetUniqueId(dt.getWidgetUniqueId());
	    result.setWidgetViewmode(dt.getWidgetViewmode());
	    result.setWidth(dt.getWidth());
	    
		return result;
	}

	public static EmsDashboardTileParams cloneEmsDashboardTileParams(EmsDashboardTileParams tp)
	{
		EmsDashboardTileParams result = new EmsDashboardTileParams();
		result.setDashboardTile(cloneEmsDashboardTile(tp.getDashboardTile()));
		result.setIsSystem(tp.getIsSystem());
		result.setParamName(tp.getParamName());
		result.setParamType(tp.getParamType());
		result.setParamValueNum(tp.getParamValueNum());
		result.setParamValueStr(tp.getParamValueStr());
		result.setParamValueTimestamp(tp.getParamValueTimestamp());
		
		return result;
	}

	public static EmsPreference cloneEmsPreference(EmsPreference p)
	{
		EmsPreference result = new EmsPreference();
		result.setPrefKey(p.getPrefKey());
		result.setPrefValue(p.getPrefValue());
		result.setUserName(p.getUserName());
		
		return result;
	}

	private FacadeUtil()
	{
	}
}
