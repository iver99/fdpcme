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

import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardFavoriteRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.DashboardLastAccessRowEntity;
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
        if (syncPreferenceTableRows(data.getEmsPreference()) <= 0) logger.info("nothing was added to EMS_Prefernce!");
        if (syncDashboardTableRows(data.getEmsDashboard()) <= 0) logger.info("nothing was added to EMS_DASHBOARD!");
        if (syncDashboardSetTableRows(data.getEmsDashboardSet()) <= 0)
            logger.info("nothing was added to EMS_DASHBOARD_SET!");
        if (syncDashboardUserOptionsTableRows(data.getEmsDashboardUserOptions()) <= 0)
            logger.info("nothing was added to EMS_DASHBOARD_USER_OPTIONS!");
        if (syncDashboardFavoriteTableRows(data.getEmsDashboardFavorite()) <= 0)
            logger.info("nothing was added to EMS_DASHBOARD_FAVORITE!");
        if (syncDashboardLastAccessTableRows(data.getEmsDashboardLastAccess()) <= 0)
            logger.info("nothing was added to EMS_DASHBOARD_LAST_ACCESS!");
        if (syncDashboardTileTableRows(data.getEmsDashboardTile()) <= 0)
            logger.info("nothing was added to EMS_DASHBOARD_TILE!");
        if (syncDashboardTileParamsTableRows(data.getEmsDashboardTileParams()) <= 0)
            logger.info("nothing was added to EMS_DASHBOARD_TILE_PARAMS!");
    }

    private int syncDashboardFavoriteTableRows(List<DashboardFavoriteRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.info("DashboardFavoriteRows is empty or null!");
            return 0;
        }
        int result = 0;
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        EntityManager em = dsf.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();
        entityTransaction.begin();
        try {
            for (DashboardFavoriteRowEntity dashboardFavoriteRowEntity : rows) {
                result += DataManager.getInstance().syncDashboardFavorite(em, dashboardFavoriteRowEntity.getUserName()
                        , dashboardFavoriteRowEntity.getDashboardId(), dashboardFavoriteRowEntity.getCreationDate(), dashboardFavoriteRowEntity.getTenantId()
                        , dashboardFavoriteRowEntity.getLastModificationDate());
            }
            dsf.commitTransaction();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            em.close();
        }
        return result;
    }

    private int syncDashboardLastAccessTableRows(List<DashboardLastAccessRowEntity> rows) {
        if (rows == null) {
            logger.info("DashboardLastAccessRows is empty or null!");
            return 0;
        }
        int result = 0;
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        EntityManager em = dsf.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();
        entityTransaction.begin();
        try {
            for (DashboardLastAccessRowEntity dashboardLastAccessRowEntity : rows) {
                result += DataManager.getInstance().syncDashboardLastAccess(em, dashboardLastAccessRowEntity.getDashboardId(),
                        dashboardLastAccessRowEntity.getAccessedBy(), dashboardLastAccessRowEntity.getAccessDate(), dashboardLastAccessRowEntity.getTenantId(),
                        dashboardLastAccessRowEntity.getCreationDate(), dashboardLastAccessRowEntity.getLastModificationDate());
            }
            dsf.commitTransaction();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            em.close();
            return result;
        }
    }

    private int syncDashboardSetTableRows(List<DashboardSetRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.info("DashboardSetRows is empty or null!");
            return 0;
        }
        int result = 0;
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        EntityManager em = dsf.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();
        entityTransaction.begin();
        try {
            for (DashboardSetRowEntity dashboardSetRowEntity : rows) {
                result += DataManager.getInstance().syncDashboardSet(em, dashboardSetRowEntity.getDashboardSetId(), dashboardSetRowEntity.getTenantId(),
                        dashboardSetRowEntity.getSubDashboardId(), dashboardSetRowEntity.getPosition(), dashboardSetRowEntity.getCreationDate(), dashboardSetRowEntity.getLastModificationDate());
            }
            dsf.commitTransaction();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            em.close();
            return result;
        }
    }

    private int syncDashboardTableRows(List<DashboardRowEntity> dashboardRows) {
        if (dashboardRows == null || dashboardRows.isEmpty()) {
            logger.info("dashboardRows is null or empty!");
            return 0;
        }
        int result = 0;
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        EntityManager em = dsf.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();
        entityTransaction.begin();
        try {
            for (DashboardRowEntity dashboardRowEntity : dashboardRows) {
                result += DataManager.getInstance().syncDashboardTableRow(em, dashboardRowEntity.getDashboardId(), dashboardRowEntity.getName(), dashboardRowEntity.getType(), dashboardRowEntity.getDescription()
                        , dashboardRowEntity.getCreationDate(), dashboardRowEntity.getLastModificationDate(), dashboardRowEntity.getLastModifiedBy(), dashboardRowEntity.getOwner()
                        , dashboardRowEntity.getIsSystem(), dashboardRowEntity.getApplicationType(), dashboardRowEntity.getEnableTimeRange(), dashboardRowEntity.getScreenShot()
                        , dashboardRowEntity.getDeleted(), dashboardRowEntity.getTenantId(), dashboardRowEntity.getEnableRefresh(), dashboardRowEntity.getSharePublic()
                        , dashboardRowEntity.getEnableEntityFilter(), dashboardRowEntity.getEnableDescription(), dashboardRowEntity.getExtendedOptions());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            em.close();
            return result;
        }
    }


    private int syncDashboardTileParamsTableRows(List<DashboardTileParamsRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.info("TileParamsRows is null or empty!");
            return 0;
        }
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        EntityManager em = dsf.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();
        entityTransaction.begin();
        int result = 0;
        try {
            for (DashboardTileParamsRowEntity dashboardTileParamsRowEntity : rows) {
                result += DataManager.getInstance().syncDashboardTileParam(em, dashboardTileParamsRowEntity.getTileId(), dashboardTileParamsRowEntity.getParamName()
                        , dashboardTileParamsRowEntity.getTenantId(), dashboardTileParamsRowEntity.getIsSystem(), dashboardTileParamsRowEntity.getParamType()
                        , dashboardTileParamsRowEntity.getParamValueStr(), dashboardTileParamsRowEntity.getParamValueNum(), dashboardTileParamsRowEntity.getParamValueTimestamp()
                        , dashboardTileParamsRowEntity.getCreationDate(), dashboardTileParamsRowEntity.getLastModificationDate());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            em.close();
            return result;
        }
    }

    private int syncDashboardTileTableRows(List<DashboardTileRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.info("TileRows is null or empty!");
            return 0;
        }
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        EntityManager em = dsf.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();
        entityTransaction.begin();
        int result = 0;
        try {
            for (DashboardTileRowEntity dashboardTileRowEntity : rows) {
                result += DataManager.getInstance().syncDashboardTile(em, dashboardTileRowEntity.getTileId(), dashboardTileRowEntity.getDashboardId(), dashboardTileRowEntity.getLastModificationDate()
                        , dashboardTileRowEntity.getLastModificationDate(), dashboardTileRowEntity.getLastModifiedBy(), dashboardTileRowEntity.getOwner(), dashboardTileRowEntity.getTitle()
                        , dashboardTileRowEntity.getHeight(), dashboardTileRowEntity.getWidth(), dashboardTileRowEntity.getIsMaximized(), dashboardTileRowEntity.getPosition()
                        , dashboardTileRowEntity.getTenantId(), dashboardTileRowEntity.getWidgetUniqueId(), dashboardTileRowEntity.getWidgetName(), dashboardTileRowEntity.getWidgetDescription()
                        , dashboardTileRowEntity.getWidgetGroupName(), dashboardTileRowEntity.getWidgetIcon(), dashboardTileRowEntity.getWidgetHistogram(), dashboardTileRowEntity.getWidgetOwner()
                        , dashboardTileRowEntity.getWidgetCreationTime(), dashboardTileRowEntity.getWidgetSource(), dashboardTileRowEntity.getWidgetKocName(), dashboardTileRowEntity.getWidgetViewmode()
                        , dashboardTileRowEntity.getWidgetTemplate(), dashboardTileRowEntity.getProviderName(), dashboardTileRowEntity.getProviderVersion(), dashboardTileRowEntity.getProviderAssetRoot()
                        , dashboardTileRowEntity.getTileRow(), dashboardTileRowEntity.getTileColumn(), dashboardTileRowEntity.getType(), dashboardTileRowEntity.getWidgetSupportTimeControl()
                        , dashboardTileRowEntity.getWidgetLinkedDashboard());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            em.close();
            return result;
        }
    }

    private int syncDashboardUserOptionsTableRows(List<DashboardUserOptionsRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.info("DashboardUserOptionsRows is null or empty!");
            return 0;
        }
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        EntityManager em = dsf.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();
        entityTransaction.begin();
        int result = 0;
        try {
            for (DashboardUserOptionsRowEntity dashboardUserOptionsRowEntity : rows) {
                result += DataManager.getInstance().syncDashboardUserOption(em, dashboardUserOptionsRowEntity.getUserName(), dashboardUserOptionsRowEntity.getTenantId()
                        , dashboardUserOptionsRowEntity.getDashboardId(), dashboardUserOptionsRowEntity.getAutoRefreshInterval(), dashboardUserOptionsRowEntity.getAccessDate()
                        , dashboardUserOptionsRowEntity.getIsFavorite(), dashboardUserOptionsRowEntity.getExtendedOptions(), dashboardUserOptionsRowEntity.getCreationDate(), dashboardUserOptionsRowEntity.getLastModificationDate());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            em.close();
            return result;
        }
    }

    private int syncPreferenceTableRows(List<PreferenceRowEntity> rows) {
        if (rows == null || rows.isEmpty()) {
            logger.info(" PreferenceRows is null or empty!");
            return 0;
        }
        int result = 0;
        DashboardServiceFacade dsf = new DashboardServiceFacade();
        EntityManager em = dsf.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();
        entityTransaction.begin();
        try {
            for (PreferenceRowEntity preferenceRowEntity : rows) {
                result += DataManager.getInstance().syncPreferences(em, preferenceRowEntity.getUserName(), preferenceRowEntity.getPrefKey(), preferenceRowEntity.getPrefValue()
                        , preferenceRowEntity.getTenantId(), preferenceRowEntity.getCreationDate(), preferenceRowEntity.getLastModificationDate());
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        } finally {
            em.close();
            return result;
        }
    }
}
