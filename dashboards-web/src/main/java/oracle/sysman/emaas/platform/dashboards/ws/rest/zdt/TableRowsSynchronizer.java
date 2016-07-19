/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest.zdt;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardFavoriteRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardLastAccessRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardSetRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileParamsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardUserOptionsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.PreferenceRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.TableRowsEntity;

/**
 * @author guochen
 */
public class TableRowsSynchronizer
{
	private static final Logger logger = LogManager.getLogger(TableRowsSynchronizer.class);

	public void sync(TableRowsEntity data)
	{
		if (data == null) {
			logger.error("Failed to sync for input data is null");
			return;
		}
		syncDashboardTableRows(data.getEmsDashboard());
		syncDashboardFavoriteTableRows(data.getEmsDashboardFavorite());
		syncDashboardLastAccessTableRows(data.getEmsDashboardLastAccess());
		syncDashboardSetTableRows(data.getEmsDashboardSet());
		syncDashboardTileTableRows(data.getEmsDashboardTile());
		syncDashboardTileParamsTableRows(data.getEmsDashboardTileParams());
		syncDashboardUserOptionsTableRows(data.getEmsDashboardUserOptions());
		syncPreferenceTableRows(data.getEmsPreference());
	}

	private void syncDashboardFavoriteTableRows(List<DashboardFavoriteRowEntity> rows)
	{
		if (rows == null) {
			return;
		}
		// TODO: call DataManager implementation to insert or update data to database
	}

	private void syncDashboardLastAccessTableRows(List<DashboardLastAccessRowEntity> rows)
	{
		if (rows == null) {
			return;
		}
		// TODO: call DataManager implementation to insert or update data to database
	}

	private void syncDashboardSetTableRows(List<DashboardSetRowEntity> rows)
	{
		if (rows == null) {
			return;
		}
		// TODO: call DataManager implementation to insert or update data to database
	}

	private void syncDashboardTableRows(List<DashboardRowEntity> dashboardRows)
	{
		if (dashboardRows == null) {
			return;
		}
		// TODO: call DataManager implementation to insert or update data to database
	}

	private void syncDashboardTileParamsTableRows(List<DashboardTileParamsRowEntity> rows)
	{
		if (rows == null) {
			return;
		}
		// TODO: call DataManager implementation to insert or update data to database
	}

	private void syncDashboardTileTableRows(List<DashboardTileRowEntity> rows)
	{
		if (rows == null) {
			return;
		}
		// TODO: call DataManager implementation to insert or update data to database
	}

	private void syncDashboardUserOptionsTableRows(List<DashboardUserOptionsRowEntity> rows)
	{
		if (rows == null) {
			return;
		}
		// TODO: call DataManager implementation to insert or update data to database
	}

	private void syncPreferenceTableRows(List<PreferenceRowEntity> rows)
	{
		if (rows == null) {
			return;
		}
		// TODO: call DataManager implementation to insert or update data to database
	}
}
