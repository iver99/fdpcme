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

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.zdt.DataManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardSetRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileParamsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardTileRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardUserOptionsRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.PreferenceRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.TableRowsEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * @author guochen
 */
public class TableRowsSynchronizer {
    private static final Logger logger = LogManager.getLogger(TableRowsSynchronizer.class);

    public void sync(TableRowsEntity data) {
        if (data == null) {
            logger.error("Failed to sync for input data is null");
            return;
        }
        if (syncPreferenceTableRows(data.getEmsPreference()) <= 0) logger.debug("nothing was added to EMS_Prefernce!");
        if (syncDashboardTableRows(data.getEmsDashboard()) <= 0) logger.debug("nothing was added to EMS_DASHBOARD!");
        if (syncDashboardSetTableRows(data.getEmsDashboardSet()) <= 0)
            logger.debug("nothing was added to EMS_DASHBOARD_SET!");
        if (syncDashboardUserOptionsTableRows(data.getEmsDashboardUserOptions()) <= 0)
            logger.debug("nothing was added to EMS_DASHBOARD_USER_OPTIONS!");
        if (syncDashboardTileTableRows(data.getEmsDashboardTile()) <= 0)
            logger.debug("nothing was added to EMS_DASHBOARD_TILE!");
        if (syncDashboardTileParamsTableRows(data.getEmsDashboardTileParams()) <= 0)
            logger.debug("nothing was added to EMS_DASHBOARD_TILE_PARAMS!");
    }


    private int syncDashboardSetTableRows(List<DashboardSetRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.debug("DashboardSetRows is empty or null!");
            return 0;
        }
        int result = 0;
        for (DashboardSetRowEntity dashboardSetRowEntity : rows) {
            result += DataManager.getInstance().syncDashboardSet(dashboardSetRowEntity.getDashboardSetId(), dashboardSetRowEntity.getTenantId(),
                    dashboardSetRowEntity.getSubDashboardId(), dashboardSetRowEntity.getPosition(), dashboardSetRowEntity.getCreationDate(), dashboardSetRowEntity.getLastModificationDate());
        }
        return result;
    }

    private int syncDashboardTableRows(List<DashboardRowEntity> dashboardRows) {
        if (dashboardRows == null || dashboardRows.isEmpty()) {
            logger.debug("dashboardRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (DashboardRowEntity dashboardRowEntity : dashboardRows) {
            result += DataManager.getInstance().syncDashboardTableRow(dashboardRowEntity.getDashboardId(), dashboardRowEntity.getName(), dashboardRowEntity.getType(), dashboardRowEntity.getDescription()
                    , dashboardRowEntity.getCreationDate(), dashboardRowEntity.getLastModificationDate(), dashboardRowEntity.getLastModifiedBy(), dashboardRowEntity.getOwner()
                    , dashboardRowEntity.getIsSystem(), dashboardRowEntity.getApplicationType(), dashboardRowEntity.getEnableTimeRange(), dashboardRowEntity.getScreenShot()
                    , dashboardRowEntity.getDeleted(), dashboardRowEntity.getTenantId(), dashboardRowEntity.getEnableRefresh(), dashboardRowEntity.getSharePublic()
                    , dashboardRowEntity.getEnableEntityFilter(), dashboardRowEntity.getEnableDescription(), dashboardRowEntity.getExtendedOptions());
        }
        return result;
    }


    private int syncDashboardTileParamsTableRows(List<DashboardTileParamsRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.debug("TileParamsRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (DashboardTileParamsRowEntity dashboardTileParamsRowEntity : rows) {
            result += DataManager.getInstance().syncDashboardTileParam(dashboardTileParamsRowEntity.getTileId(), dashboardTileParamsRowEntity.getParamName()
                    , dashboardTileParamsRowEntity.getTenantId(), dashboardTileParamsRowEntity.getIsSystem(), dashboardTileParamsRowEntity.getParamType()
                    , dashboardTileParamsRowEntity.getParamValueStr(), dashboardTileParamsRowEntity.getParamValueNum(), dashboardTileParamsRowEntity.getParamValueTimestamp()
                    , dashboardTileParamsRowEntity.getCreationDate(), dashboardTileParamsRowEntity.getLastModificationDate());
        }
        return result;
    }

    private int syncDashboardTileTableRows(List<DashboardTileRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.debug("TileRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (DashboardTileRowEntity dashboardTileRowEntity : rows) {
            result += DataManager.getInstance().syncDashboardTile(dashboardTileRowEntity.getTileId(), dashboardTileRowEntity.getDashboardId(), dashboardTileRowEntity.getCreationDate()
                    , dashboardTileRowEntity.getLastModificationDate(), dashboardTileRowEntity.getLastModifiedBy(), dashboardTileRowEntity.getOwner(), dashboardTileRowEntity.getTitle()
                    , dashboardTileRowEntity.getHeight(), dashboardTileRowEntity.getWidth(), dashboardTileRowEntity.getIsMaximized(), dashboardTileRowEntity.getPosition()
                    , dashboardTileRowEntity.getTenantId(), dashboardTileRowEntity.getWidgetUniqueId(), dashboardTileRowEntity.getWidgetName(), dashboardTileRowEntity.getWidgetDescription()
                    , dashboardTileRowEntity.getWidgetGroupName(), dashboardTileRowEntity.getWidgetIcon(), dashboardTileRowEntity.getWidgetHistogram(), dashboardTileRowEntity.getWidgetOwner()
                    , dashboardTileRowEntity.getWidgetCreationTime(), dashboardTileRowEntity.getWidgetSource(), dashboardTileRowEntity.getWidgetKocName(), dashboardTileRowEntity.getWidgetViewmode()
                    , dashboardTileRowEntity.getWidgetTemplate(), dashboardTileRowEntity.getProviderName(), dashboardTileRowEntity.getProviderVersion(), dashboardTileRowEntity.getProviderAssetRoot()
                    , dashboardTileRowEntity.getTileRow(), dashboardTileRowEntity.getTileColumn(), dashboardTileRowEntity.getType(), dashboardTileRowEntity.getWidgetSupportTimeControl()
                    , dashboardTileRowEntity.getWidgetLinkedDashboard());
        }
        return result;
    }

    private int syncDashboardUserOptionsTableRows(List<DashboardUserOptionsRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.debug("DashboardUserOptionsRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (DashboardUserOptionsRowEntity dashboardUserOptionsRowEntity : rows) {
            result += DataManager.getInstance().syncDashboardUserOption(dashboardUserOptionsRowEntity.getUserName(), dashboardUserOptionsRowEntity.getTenantId()
                    , dashboardUserOptionsRowEntity.getDashboardId(), dashboardUserOptionsRowEntity.getAutoRefreshInterval(), dashboardUserOptionsRowEntity.getAccessDate()
                    , dashboardUserOptionsRowEntity.getIsFavorite(), dashboardUserOptionsRowEntity.getExtendedOptions(), dashboardUserOptionsRowEntity.getCreationDate(), dashboardUserOptionsRowEntity.getLastModificationDate());
        }
        return result;
    }

    private int syncPreferenceTableRows(List<PreferenceRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.debug("PreferenceRows is null or empty!");
            return 0;
        }
        int result = 0;
        for (PreferenceRowEntity preferenceRowEntity : rows) {
            result += DataManager.getInstance().syncPreferences(preferenceRowEntity.getUserName(), preferenceRowEntity.getPrefKey(), preferenceRowEntity.getPrefValue()
                    , preferenceRowEntity.getTenantId(), preferenceRowEntity.getCreationDate(), preferenceRowEntity.getLastModificationDate());
        }
        return result;
    }
}
