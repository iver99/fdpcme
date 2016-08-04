/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.zdt;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultType;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;

/**
 * @author guochen
 */
public class DataManager
{
	private static final Logger logger = LogManager.getLogger(DataManager.class);

	private static DataManager instance = new DataManager();
	/**
	 * Returns the singleton instance for zdt data manager
	 *
	 * @return
	 */
	public static DataManager getInstance()
	{
		return instance;
	}

	/**
	 * Retrieves total count for all dashbaord from all tenants
	 *
	 * @return
	 */
	public long getAllDashboardsCount()
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD WHERE DELETED <> 1";
			Query query = em.createNativeQuery(sql);
			long count = ((Number) query.getSingleResult()).longValue();
			return count;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Retrieves total count for all favorites from all tenants
	 *
	 * @return
	 */
	public long getAllFavoriteCount()
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD d, EMS_DASHBOARD_USER_OPTIONS uo WHERE d.DASHBOARD_ID=uo.DASHBOARD_ID AND d.TENANT_ID=uo.TENANT_ID AND d.DELETED<>1 AND IS_FAVORITE=1";
			Query query = em.createNativeQuery(sql);
			long count = ((Number) query.getSingleResult()).longValue();
			return count;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Retrieves total count for all preferences from all tenants
	 *
	 * @return
	 */
	public long getAllPreferencessCount()
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			String sql = "SELECT COUNT(1) FROM EMS_PREFERENCE";
			Query query = em.createNativeQuery(sql);
			long count = ((Number) query.getSingleResult()).longValue();
			return count;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Retrieves all dashboard favorite data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardFavoriteTableData()
	{
		String sql = "select * FROM EMS_DASHBOARD_FAVORITE";
		return getDatabaseTableData(sql);
	}

	/**
	 * Retrieves all dashboard last access data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardLastAccessTableData()
	{
		String sql = "select * FROM EMS_DASHBOARD_LAST_ACCESS";
		return getDatabaseTableData(sql);
	}

	/**
	 * Retrieves all dashboard set data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardSetTableData()
	{
		String sql = "SELECT * FROM EMS_DASHBOARD_SET";
		return getDatabaseTableData(sql);
	}

	/**
	 * Retrieves all dashboard data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardTableData()
	{
		String sql = "SELECT * FROM EMS_DASHBOARD";
		return getDatabaseTableData(sql);
	}

	/**
	 * Retrieves all dashboard tile params data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardTileParamsTableData()
	{
		String sql = "SELECT * FROM EMS_DASHBOARD_TILE_PARAMS";
		return getDatabaseTableData(sql);
	}

	/**
	 * Retrieves all dashboard tile data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardTileTableData()
	{
		String sql = "SELECT * FROM EMS_DASHBOARD_TILE";
		return getDatabaseTableData(sql);
	}

	/**
	 * Retrieves all dashboard user options data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getDashboardUserOptionsTableData()
	{
		String sql = "SELECT * FROM EMS_DASHBOARD_USER_OPTIONS";
		return getDatabaseTableData(sql);
	}

	/**
	 * Retrieves all preference data rows for all tenant
	 *
	 * @return
	 */
	public List<Map<String, Object>> getPreferenceTableData()
	{
		String sql = "SELECT * FROM EMS_PREFERENCE";
		return getDatabaseTableData(sql);
	}

	/**
	 * Retrieves database data rows for specific native SQL for all tenant
	 *
	 * @return
	 */
	private List<Map<String, Object>> getDatabaseTableData(String nativeSql)
	{
		if (StringUtil.isEmpty(nativeSql)) {
			logger.error("Can't query database table with null or empty SQL statement!");
			return null;
		}
		DashboardServiceFacade dsf = new DashboardServiceFacade();
		EntityManager em = dsf.getEntityManager();
		Query query = em.createNativeQuery(nativeSql);
		query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = query.getResultList();
		return list;
	}

	public int syncDashboardTableRow(BigInteger dashboardId, String name, Long type, String description, String creationDate, String lastModificationDate, String lastModifiedBy, String owner,
									 Integer isSystem, Integer applicationType, Integer enableTimeRange, String screenShot, Long deleted, Long tenantId, Integer enableRefresh, Integer sharePublic,
									 Integer enableEntityFilter, Integer enableDescription, String extendedOptions) {
		logger.info("Calling the DataManager.syncDashboardTableRow");
		if (dashboardId == null) {
			logger.info("dashboard id cannot be null!");
			return 0;
		}
		if (StringUtil.isEmpty(name)) {
			logger.info("name cannot be null!");
			return 0;
		}
		if (type == null) {
			logger.info("type cannot be null!");
			return 0;
		}
		if ((StringUtil.isEmpty(creationDate))) {
			logger.info("creation date cannot be null!");
			return 0;
		}
		if ((StringUtil.isEmpty(owner))) {
			logger.info("creation date cannot be null!");
			return 0;
		}
		if (isSystem == null) {
			logger.info("creation date cannot be null!");
			return 0;
		}

		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (isDashboardExist(entityManager, dashboardId, tenantId)) {
				logger.info("Dashboard with id {} exists", dashboardId);
				if (getDashboardLastModifiedDate(entityManager, dashboardId, tenantId).compareTo(lastModificationDate) >= 0) {
					logger.info("This lastModificationDate is earlier, there is no need to update");
					return 0;
				}
				return updateDashboard(entityManager, dashboardId, name, type, description, creationDate, lastModificationDate, lastModifiedBy, owner,
						isSystem, applicationType, enableTimeRange, screenShot, deleted, tenantId, enableRefresh, sharePublic,
						enableEntityFilter, enableDescription, extendedOptions);
			}
			logger.info("Dashboard with id {} not exist insert now", dashboardId);
			return insertDashboard(entityManager, dashboardId, name, type, description, creationDate, lastModificationDate, lastModifiedBy, owner,
					isSystem, applicationType, enableTimeRange, screenShot, deleted, tenantId, enableRefresh, sharePublic, enableEntityFilter, enableDescription, extendedOptions);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	public int syncDashboardTile(BigInteger tileId, BigInteger dashboardId, String creationDate, String lastModificationDate, String lastModifiedBy, String owner, String title, Long height,
								 Long width, Integer isMaximized, Long position, Long tenantId, String widgetUniqueId, String widgetName, String widgetDescription, String widgetGroupName,
								 String widgetIcon, String widgetHistogram, String widgetOwner, String widgetCreationTime, Long widgetSource, String widgetKocName, String widgetViewmode, String widgetTemplate,
								 String providerName, String providerVersion, String providerAssetRoot, Long tileRow, Long tileColumn, Long type, Integer widgetSupportTimeControl, Long widgetLinkedDashboard) {
		logger.info("Calling the DataManager,syncDashboardTile");
		if (tileId == null) {
			logger.info("TILE_ID is null!");
			return 0;
		}
		if (dashboardId == null) {
			logger.info("DASHBOARD_ID is null!");
			return 0;
		}
		if (StringUtil.isEmpty(creationDate)) {
			logger.info("CREATION_DATE is null!");
			return 0;
		}
		if (StringUtil.isEmpty(title)) {
			logger.info("TITLE is null!");
			return 0;
		}
		if (position == null) {
			logger.info("POSITION is null!");
			return 0;
		}
		if (tenantId == null) {
			logger.info("TENANT_ID is null!");
			return 0;
		}
		if (StringUtil.isEmpty(widgetUniqueId)) {
			logger.info("WIDGET_UNIQUE_ID is null!");
			return 0;
		}
		if (StringUtil.isEmpty(widgetName)) {
			logger.info("WIDGET_NAME is null!");
			return 0;
		}
		if (StringUtil.isEmpty(widgetOwner)) {
			logger.info("WIDGET_OWNER is null!");
			return 0;
		}
		if (StringUtil.isEmpty(widgetCreationTime)) {
			logger.info("WIDGET_CREATION_TIME is null!");
			return 0;
		}
		if (widgetSource == null) {
			logger.info("WIDGET_SOURCE is null!");
			return 0;
		}
		if (StringUtil.isEmpty(widgetKocName)) {
			logger.info("WIDGET_KOC_NAME is null!");
			return 0;
		}
		if (StringUtil.isEmpty(widgetViewmode)) {
			logger.info("WIDGET_VIEWMODE is null!");
			return 0;
		}
		if (StringUtil.isEmpty(widgetTemplate)) {
			logger.info("WIDGET_TEMPLATE is null!");
			return 0;
		}
		if (widgetSupportTimeControl == null) {
			logger.info("WIDGET_SUPPORT_TIME_CONTROL is null!");
			return 0;
		}
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (isDashboardTileExist(entityManager, tileId, dashboardId, tenantId)) {
				logger.info("Dashboard Tile with id {} exists", tileId);
				if (getDashboardTileLastModifiedDate(entityManager, tileId, dashboardId, tenantId).compareTo(lastModificationDate) >= 0) {
					logger.info("This lastModificationDate is earlier, there is no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later,do update now");
				return updateDashboardTile(entityManager, tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
						width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
						widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
						providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard);
			}
			logger.info("Tile with id {} not exist insert now", tileId);
			return insertDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
					width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
					widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
					providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	public int syncDashboardTileParam( BigInteger tileId, String paramName,
									  Long tenantId, Integer isSystem, Long paramType, String paramValueStr,
									  Long paramValueNum, String paramValueTimestamp, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.syncDashboardTileParam");
		if(tenantId == null){
			logger.info("TENANT_ID is null!");
			return 0;
		}
		if(paramName == null){
			logger.info("PARAM_NAME is null!");
			return 0;
		}
		if(tileId == null){
			logger.info("TILE_ID is null!");
			return 0;
		}
		if(isSystem == null){
			logger.info("IS_SYSTEM is null!");
			return 0;
		}
		if(paramType == null){
			logger.info("PARAM_TYPE is null!");
			return 0;
		}
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (isDashboardTileParamExist(entityManager, tileId, paramName, tenantId)) {
				logger.info("DashboardTileParam with tile id {} exists", tileId);
				if (getDashboardTileParamLastModifiedDate(entityManager, tileId, paramName, tenantId).compareTo(lastModificationDate) >= 0) {
					logger.info("The lastModificationDate is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later,do update now");
				return updateDashboardTileParam(entityManager, tileId, paramName, tenantId, isSystem,
						paramType, paramValueStr, paramValueNum, paramValueTimestamp, creationDate, lastModificationDate);
			}
			logger.info("Tile param with id {} does not exist insert now", tileId);
			return insertDashboardTileParam(entityManager, tileId, paramName, tenantId,
					isSystem, paramType, paramValueStr, paramValueNum,
					paramValueTimestamp, creationDate, lastModificationDate);
		}catch (Exception e){
			logger.error(e.getLocalizedMessage());
			return 0;
		}finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	public int syncDashboardLastAccess(BigInteger dashboardId, String accessedBy, String accessDate, Long tenantId,
									   String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.syncDashboardLastAccess");
		if (dashboardId == null) {
			logger.info("DASHBOARD_ID is null!");
			return 0;
		}
		if (StringUtil.isEmpty(accessedBy)) {
			logger.info("ACCESSED_BY is null!");
			return 0;
		}
		if (StringUtil.isEmpty(accessDate)) {
			logger.info("ACCESS_DATE is null!");
		}
		if (tenantId == null) {
			logger.info("TENANT_ID is null!");
			return 0;
		}
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (isDashboardLastAccessExit(entityManager, dashboardId, tenantId)) {
				logger.info("DashboardLastAccess with tile id {} exists", dashboardId);
				if (getDashboardLastAccessLastModifiedDate(entityManager, dashboardId, tenantId).compareTo(lastModificationDate) >= 0) {
					logger.info("The lastModificationDate is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later, do update now");
				return updateDashboardLastAccess(entityManager, dashboardId, accessedBy, accessDate, tenantId, creationDate, lastModificationDate);
			}
			logger.info("Dashboard Last Access with id {} does not exist insert now", dashboardId);
			return insertDashboardLastAccess(entityManager, dashboardId, accessedBy, accessDate, tenantId, creationDate, lastModificationDate);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	public int syncDashboardFavorite(String userName, BigInteger dashboardId, String creationDate,
									 Long tenantId, String lastModificationDate) {
		logger.info("Calling DataManager.syncDashboardFavorite");
		if (StringUtil.isEmpty(userName)) {
			logger.info("USER_NAME is null!");
			return 0;
		}
		if (dashboardId == null) {
			logger.info("DASHBOARD_ID is null");
			return 0;
		}
		if (StringUtil.isEmpty(creationDate)) {
			logger.info("CREATION_DATE");
			return 0;
		}
		if (tenantId == null) {
			logger.info("TENANT_ID");
			return 0;
		}
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (isDashboardFavoriteExit(entityManager, userName, dashboardId, tenantId)) {
				logger.info("DashboardFavorite with dashboard id {} exists", dashboardId);
				if (getDashboardFavoriteLastModifiedDate(entityManager, userName, dashboardId, tenantId).compareTo(lastModificationDate) >= 0) {
					logger.info("The lastModification is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModification is later, do update now");
				return updateDashboardFavorite(entityManager, userName, dashboardId, creationDate, tenantId, lastModificationDate);
			}
			logger.info("Dashboard Favorite with id {} does not exist insert now", dashboardId);
			return insertDashboardFavorite(entityManager, userName, dashboardId, creationDate, tenantId, lastModificationDate);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	public int syncDashboardUserOption(String userName, Long tenantId, BigInteger dashboardId,
									   Long autoRefreshInterval, String accessDate, Integer isFavorite, String extendedOptions,
									   String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.syncDashboardUserOption");
		if (StringUtil.isEmpty(userName)) {
			logger.info("USER_NAME is null!");
			return 0;
		}
		if (tenantId == null) {
			logger.info("TENANT_ID is null !");
			return 0;
		}
		if (dashboardId == null) {
			logger.info("DASHBOARD_ID is null !");
			return 0;
		}
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (isDashboardUserOptionExist(entityManager, userName, tenantId, dashboardId)) {
				logger.info("DashboardUserOption with dashboardId {} exists", dashboardId);
				if (getDashboardUserOptionLastModifiedDate(entityManager, userName, tenantId, dashboardId).compareTo(lastModificationDate) >= 0) {
					logger.info("The lastModificationDate is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later, do update now");
				return updateDashboardUserOption(entityManager, userName, tenantId, dashboardId,
						autoRefreshInterval, accessDate, isFavorite, extendedOptions,
						creationDate, lastModificationDate);
			}
			logger.info("Dashboard User Option with id {} does not exist insert now", dashboardId);
			return insertDashboardUserOption(entityManager, userName, tenantId, dashboardId, autoRefreshInterval,
					accessDate, isFavorite, extendedOptions, creationDate, lastModificationDate);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	public int syncDashboardSet(BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId,
								Long position, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.syncDashboardSet", dashboardSetId);
		if (dashboardSetId == null) {
			logger.info("DASHBOARD_SET_ID is null !");
			return 0;
		}
		if (tenantId == null) {
			logger.info("TENANT_ID is null !");
			return 0;
		}
		if (subDashboardId == null) {
			logger.info("SUB_DASHBOARD_ID is null !");
			return 0;
		}
		if (position == null) {
			logger.info("POSITION is null !");
			return 0;
		}
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (isDashboardSetExist(entityManager, dashboardSetId, tenantId, subDashboardId)) {
				logger.info("DashboardSet with dashboardSetId {} exist", dashboardSetId);
				if (getDashboardSetLastModifiedDate(entityManager, dashboardSetId, tenantId, subDashboardId).compareTo(lastModificationDate) >= 0) {
					logger.info("The lastModification is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later, do update now");
				return updateDashboardSet(entityManager, dashboardSetId, tenantId, subDashboardId, position, creationDate, lastModificationDate);
			}
			logger.info("Dashboard Set with id {} does not exist insert now", dashboardSetId);
			return insertDashboardSet(entityManager, dashboardSetId, tenantId, subDashboardId, position, creationDate, lastModificationDate);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	public int syncPreferences(String userName, String prefKey, String prefValue,
							   Long tenantId, String creationDate, String lastModificationDate) {
		logger.info("Calling syncPreference");
		if (StringUtil.isEmpty(userName)) {
			logger.info("USER_NAME is null");
			return 0;
		}
		if (StringUtil.isEmpty(prefKey)) {
			logger.info("PREF_KEY is null");
			return 0;
		}
		if (StringUtil.isEmpty(prefValue)) {
			logger.info("PREF_VALUE is null");
			return 0;
		}
		if (tenantId == null) {
			logger.info("TENANT_ID is null");
			return 0;
		}
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		entityManager.getTransaction().begin();
		try {
			if (isPreferenceExist(entityManager, userName, prefKey, tenantId)) {
				logger.info("Preference with prefKey {} exists", prefKey);
				if (getPreferenceLastModifiedDate(entityManager, userName, prefKey, tenantId).compareTo(lastModificationDate) >= 0) {
					logger.info("The lastModificationDate is earlier, no need to update");
					return 0;
				}
				logger.info("The lastModificationDate is later, do update now");
				return updatePreferences(entityManager, userName, prefKey, prefValue, tenantId, creationDate, lastModificationDate);
			}
			logger.info("Preference with prefKey {} does not exist insert now", prefKey);
			return insertPreferences(entityManager, userName, prefKey, prefValue, tenantId, creationDate, lastModificationDate);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	private int insertDashboard(EntityManager entityManager, BigInteger dashboardId, String name, Long type, String description, String creationDate, String lastModificationDate, String lastModifiedBy, String owner,
								Integer isSystem, Integer applicationType, Integer enableTimeRange, String screenShot, Long deleted, Long tenantId, Integer enableRefresh, Integer sharePublic,
								Integer enableEntityFilter, Integer enableDescription, String extendedOptions) {
		logger.info("Calling the Datamanager.insertDashboard");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD(DASHBOARD_ID,  NAME, TYPE, DESCRIPTION, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY, OWNER, IS_SYSTEM, APPLICATION_TYPE, ENABLE_TIME_RANGE, SCREEN_SHOT, DELETED, TENANT_ID, ENABLE_REFRESH, SHARE_PUBLIC, ENABLE_ENTITY_FILTER, ENABLE_DESCRIPTION, EXTENDED_OPTIONS)values(?,?,?,?,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') ,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') ,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardId)
				.setParameter(2, name)
				.setParameter(3, type)
				.setParameter(4, description)
				.setParameter(5, creationDate)
				.setParameter(6, lastModificationDate)
				.setParameter(7, lastModifiedBy)
				.setParameter(8, owner)
				.setParameter(9, isSystem)
				.setParameter(10, applicationType)
				.setParameter(11, enableTimeRange)
				.setParameter(12, screenShot)
				.setParameter(13, deleted)
				.setParameter(14, tenantId)
				.setParameter(15, enableRefresh)
				.setParameter(16, sharePublic)
				.setParameter(17, enableEntityFilter)
				.setParameter(18, enableDescription)
				.setParameter(19, extendedOptions);
		logger.info(query.toString());
		result = query.executeUpdate();
		return result;
	}

	private int insertDashboardTile(EntityManager entityManager, BigInteger tileId, BigInteger dashboardId, String creationDate, String lastModificationDate, String lastModifiedBy, String owner, String title, Long height,
									Long width, Integer isMaximized, Long position, Long tenantId, String widgetUniqueId, String widgetName, String widgetDescription, String widgetGroupName,
									String widgetIcon, String widgetHistogram, String widgetOwner, String widgetCreationTime, Long widgetSource, String widgetKocName, String widgetViewmode, String widgetTemplate,
									String providerName, String providerVersion, String providerAssetRoot, Long tileRow, Long tileColumn, Long type, Integer widgetSupportTimeControl, Long widgetLinkedDashboard) {
		logger.info("Calling DataManager.insertDashboardTiles");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_TILE(TILE_ID, DASHBOARD_ID, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY, OWNER, TITLE, HEIGHT, WIDTH, IS_MAXIMIZED, POSITION, TENANT_ID, WIDGET_UNIQUE_ID, WIDGET_NAME, WIDGET_DESCRIPTION, WIDGET_GROUP_NAME, WIDGET_ICON, WIDGET_HISTOGRAM, WIDGET_OWNER, WIDGET_CREATION_TIME, WIDGET_SOURCE, WIDGET_KOC_NAME, WIDGET_VIEWMODE, WIDGET_TEMPLATE, PROVIDER_NAME, PROVIDER_VERSION, PROVIDER_ASSET_ROOT, TILE_ROW, TILE_COLUMN, TYPE, WIDGET_SUPPORT_TIME_CONTROL, WIDGET_LINKED_DASHBOARD)values(?,?,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') ,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId).setParameter(2, dashboardId)
				.setParameter(3, creationDate).setParameter(4, lastModificationDate)
				.setParameter(5, lastModifiedBy).setParameter(6, owner)
				.setParameter(7, title).setParameter(8, height)
				.setParameter(9, width).setParameter(10, isMaximized)
				.setParameter(11, position).setParameter(12, tenantId)
				.setParameter(13, widgetUniqueId).setParameter(14, widgetName)
				.setParameter(15, widgetDescription).setParameter(16, widgetGroupName)
				.setParameter(17, widgetIcon).setParameter(18, widgetHistogram)
				.setParameter(19, widgetOwner).setParameter(20, widgetCreationTime)
				.setParameter(21, widgetSource).setParameter(22, widgetKocName)
				.setParameter(23, widgetViewmode).setParameter(24, widgetTemplate)
				.setParameter(25, providerName).setParameter(26, providerVersion)
				.setParameter(27, providerAssetRoot).setParameter(28, tileRow)
				.setParameter(29, tileColumn).setParameter(30, type)
				.setParameter(31, widgetSupportTimeControl).setParameter(32, widgetLinkedDashboard);
		result = query.executeUpdate();
		return result;
	}

	private int insertDashboardTileParam(EntityManager entityManager, BigInteger tileId, String paramName, Long tenantId, Integer isSystem, Long paramType, String paramValueStr, Long paramValueNum, String paramValueTimestamp, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.insertDashboardTileParam");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_TILE_PARAMS(TILE_ID, PARAM_NAME, TENANT_ID, IS_SYSTEM, PARAM_TYPE, PARAM_VALUE_STR, PARAM_VALUE_NUM, PARAM_VALUE_TIMESTAMP, CREATION_DATE, LAST_MODIFICATION_DATE)values(?, ?, ?, ?, ?, ?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'))";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId)
				.setParameter(2, paramName)
				.setParameter(3, tenantId)
				.setParameter(4, isSystem)
				.setParameter(5, paramType)
				.setParameter(6, paramValueStr)
				.setParameter(7, paramValueNum)
				.setParameter(8, paramValueTimestamp)
				.setParameter(9, creationDate)
				.setParameter(10, lastModificationDate);
		result = query.executeUpdate();
		return result;
	}

	private int insertDashboardLastAccess(EntityManager entityManager, BigInteger dashboardId, String accessedBy, String accessDate, Long tenantId, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.insertDashboardLastAccess");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_LAST_ACCESS(DASHBOARD_ID, ACCESSED_BY, ACCESS_DATE, TENANT_ID, CREATION_DATE, LAST_MODIFICATION_DATE)values(?, ?,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') , to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') )";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardId)
				.setParameter(2, accessedBy)
				.setParameter(3, accessDate)
				.setParameter(4, tenantId)
				.setParameter(5, creationDate)
				.setParameter(6, lastModificationDate);
		result = query.executeUpdate();
		return result;
	}

	private int insertDashboardFavorite(EntityManager entityManager, String userName, BigInteger dashboardId, String creationDate, Long tenantId, String lastModificationDate) {
		logger.info("Calling DataManager.insertDashboardFavorite");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_FAVORITE(USER_NAME, DASHBOARD_ID, CREATION_DATE, TENANT_ID, LAST_MODIFICATION_DATE)values(?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') , ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') )";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, dashboardId)
				.setParameter(3, creationDate)
				.setParameter(4, tenantId)
				.setParameter(5,lastModificationDate);
		result = query.executeUpdate();
		return result;
	}

	private int insertDashboardUserOption(EntityManager entityManager, String userName, Long tenantId, BigInteger dashboardId, Long autoRefreshInterval, String accessDate,
										  Integer isFavorite, String extendedOptions, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.insertDashboardUserOptions");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_USER_OPTIONS(USER_NAME, TENANT_ID, DASHBOARD_ID, AUTO_REFRESH_INTERVAL, ACCESS_DATE, IS_FAVORITE, EXTENDED_OPTIONS, CREATION_DATE, LAST_MODIFICATION_DATE)values(?, ?, ?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), ?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'))";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName).setParameter(2, tenantId)
				.setParameter(3, dashboardId).setParameter(4, autoRefreshInterval)
				.setParameter(5, accessDate).setParameter(6, isFavorite)
				.setParameter(7, extendedOptions)
				.setParameter(8, creationDate)
				.setParameter(9, lastModificationDate);
		result = query.executeUpdate();
		return result;

	}

	private int insertDashboardSet(EntityManager entityManager, BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId, Long position, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.insertDashboardSet");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD_SET(DASHBOARD_SET_ID, TENANT_ID, SUB_DASHBOARD_ID, POSITION, CREATION_DATE, LAST_MODIFICATION_DATE)values(?, ?, ?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'))";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardSetId)
				.setParameter(2, tenantId)
				.setParameter(3, subDashboardId)
				.setParameter(4, position)
				.setParameter(5, creationDate)
				.setParameter(6, lastModificationDate);
		result = query.executeUpdate();
		return result;
	}

	private int insertPreferences(EntityManager entityManager, String userName, String prefKey, String prefValue, Long tenantId, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.insertPreference");
		int result;
		String sql = "INSERT INTO EMS_PREFERENCE (USER_NAME, PREF_KEY, PREF_VALUE, TENANT_ID, CREATION_DATE, LAST_MODIFICATION_DATE)values(?, ?, ?, ?, to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'))";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, prefKey)
				.setParameter(3, prefValue)
				.setParameter(4, tenantId)
				.setParameter(5, creationDate)
				.setParameter(6, lastModificationDate);
		result = query.executeUpdate();
		return result;
	}

	private int updateDashboard(EntityManager entityManager, BigInteger dashboardId, String name, Long type, String description, String creationDate, String lastModificationDate, String lastModifiedBy, String owner,
								Integer isSystem, Integer applicationType, Integer enableTimeRange, String screenShot, Long deleted, Long tenantId, Integer enableRefresh, Integer sharePublic,
								Integer enableEntityFilter, Integer enableDescription, String extendedOptions) {
		logger.info("Calling the Datamanager.updateDashboard");
		int result;
		String sql = "UPDATE EMS_DASHBOARD SET  NAME=?, TYPE=?, DESCRIPTION=?, CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFIED_BY=?, OWNER=?, IS_SYSTEM=?, APPLICATION_TYPE=?, ENABLE_TIME_RANGE=?, SCREEN_SHOT=?, DELETED=?, ENABLE_REFRESH=?, SHARE_PUBLIC=?, ENABLE_ENTITY_FILTER=?, ENABLE_DESCRIPTION=?, EXTENDED_OPTIONS=? WHERE DASHBOARD_ID=? AND TENANT_ID=?";
		if (StringUtil.isEmpty(dashboardId.toString())) {
			logger.error("dashboardId is null or empty!");
		}
		if (StringUtil.isEmpty(name)) {
			logger.error("name is null or empty!");
		}
		if (StringUtil.isEmpty(type.toString())) {
			logger.error("type is null or empty!");
		}
		if (StringUtil.isEmpty(creationDate)) {
			logger.error("create time is null or empty!");
		}
		if (StringUtil.isEmpty(owner)) {
			logger.error("owner is null or empty!");
		}
		if (StringUtil.isEmpty(isSystem.toString())) {
			logger.error("IS_SYSTEM is null or empty");
		}
		if (StringUtil.isEmpty(enableTimeRange.toString())) {
			logger.error("ENABLE_TIME_RANGE is null or empty");
		}
		if (StringUtil.isEmpty(deleted.toString())) {
			logger.error("DELETED is null or empty");
		}
		if (StringUtil.isEmpty(tenantId.toString())) {
			logger.error("TENANT_ID is null or empty!");

		}
		if (StringUtil.isEmpty(enableRefresh.toString())) {
			logger.error("ENABLE_REFRESH is null or empty!");
		}
		if (StringUtil.isEmpty(sharePublic.toString())) {
			logger.error("SHARE_PUBLIC is null or empty!");
		}
		if (StringUtil.isEmpty(enableEntityFilter.toString())) {
			logger.error("ENABLE_ENTITY_FILTER is null or empty!");
		}
		if (StringUtil.isEmpty(enableDescription.toString())) {
			logger.error("ENABLE_DESCRIPTION is null or empty!");
		}
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, name)
				.setParameter(2, type)
				.setParameter(3, description)
				.setParameter(4, creationDate)
				.setParameter(5, lastModificationDate)
				.setParameter(6, lastModifiedBy)
				.setParameter(7, owner)
				.setParameter(8, isSystem)
				.setParameter(9, applicationType)
				.setParameter(10, enableTimeRange)
				.setParameter(11, screenShot)
				.setParameter(12, deleted)
				.setParameter(13, enableRefresh)
				.setParameter(14, sharePublic)
				.setParameter(15, enableEntityFilter)
				.setParameter(16, enableDescription)
				.setParameter(17, extendedOptions)
				.setParameter(18, dashboardId)
				.setParameter(19, tenantId);
		result = query.executeUpdate();
		return result;
	}

	private int updateDashboardTile(EntityManager entityManager, BigInteger tileId, BigInteger dashboardId, String creationDate, String lastModificationDate, String lastModifiedBy, String owner, String title, Long height,
                                    Long width, Integer isMaximized, Long position, Long tenantId, String widgetUniqueId, String widgetName, String widgetDescription, String widgetGroupName,
                                    String widgetIcon, String widgetHistogram, String widgetOwner, String widgetCreationTime, Long widgetSource, String widgetKocName, String widgetViewmode, String widgetTemplate,
                                    String providerName, String providerVersion, String providerAssetRoot, Long tileRow, Long tileColumn, Long type, Integer widgetSupportTimeControl, Long widgetLinkedDashboard) {
		logger.info("Calling Datamanager.updateDashboardTiles");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_TILE SET CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') , LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') , LAST_MODIFIED_BY=?, OWNER=?, TITLE=?, HEIGHT=?, WIDTH=?, IS_MAXIMIZED=?, POSITION=?, WIDGET_UNIQUE_ID=?, WIDGET_NAME=?, WIDGET_DESCRIPTION=?, WIDGET_GROUP_NAME=?, WIDGET_ICON=?, WIDGET_HISTOGRAM=?, WIDGET_OWNER=?, WIDGET_CREATION_TIME=?, WIDGET_SOURCE=?, WIDGET_KOC_NAME=?, WIDGET_VIEWMODE=?, WIDGET_TEMPLATE=?, PROVIDER_NAME=?, PROVIDER_VERSION=?, PROVIDER_ASSET_ROOT=?, TILE_ROW=?, TILE_COLUMN=?, TYPE=?, WIDGET_SUPPORT_TIME_CONTROL=?, WIDGET_LINKED_DASHBOARD=? WHERE TILE_ID=? AND DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, creationDate)
				.setParameter(2, lastModificationDate)
				.setParameter(3, lastModifiedBy)
				.setParameter(4, owner)
				.setParameter(5, title)
				.setParameter(6, height)
				.setParameter(7, width)
				.setParameter(8, isMaximized)
				.setParameter(9, position)
				.setParameter(10, widgetUniqueId)
				.setParameter(11, widgetName)
				.setParameter(12, widgetDescription)
				.setParameter(13, widgetGroupName)
				.setParameter(14, widgetIcon)
				.setParameter(15, widgetHistogram)
				.setParameter(16, widgetOwner)
				.setParameter(17, widgetCreationTime)
				.setParameter(18, widgetSource)
				.setParameter(19, widgetKocName)
				.setParameter(20, widgetViewmode)
				.setParameter(21, widgetTemplate)
				.setParameter(22, providerName)
				.setParameter(23, providerVersion)
				.setParameter(24, providerAssetRoot)
				.setParameter(25, tileRow)
				.setParameter(26, tileColumn)
				.setParameter(27, type)
				.setParameter(28, widgetSupportTimeControl)
				.setParameter(29, widgetLinkedDashboard)
				.setParameter(30, tileId)
				.setParameter(31, dashboardId)
				.setParameter(32, tenantId);
		result = query.executeUpdate();
		return result;
	}

	private int updateDashboardTileParam(EntityManager entityManager, BigInteger tileId, String paramName, Long tenantId,
										 Integer isSystem, Long paramType, String paramValueStr, Long paramValueNum,
										 String paramValueTimestamp, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.updateDashboardTileParam");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_TILE_PARAMS SET IS_SYSTEM=?, PARAM_TYPE=?, PARAM_VALUE_STR=?, PARAM_VALUE_NUM=?, PARAM_VALUE_TIMESTAMP=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') WHERE TILE_ID=? AND PARAM_NAME=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, isSystem)
				.setParameter(2, paramType)
				.setParameter(3, paramValueStr)
				.setParameter(4, paramValueNum)
				.setParameter(5, paramValueTimestamp)
				.setParameter(6, creationDate)
				.setParameter(7, lastModificationDate)
				.setParameter(8, tileId)
				.setParameter(9, paramName)
				.setParameter(10, tenantId);
		result = query.executeUpdate();
		return result;
	}

	private int updateDashboardLastAccess(EntityManager entityManager, BigInteger dashboardId, String accessedBy,
										  String accessDate, Long tenantId, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.updateDashboardLastAccess");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_LAST_ACCESS SET CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), ACCESSED_BY=?, ACCESS_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') WHERE DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, creationDate)
				.setParameter(2, lastModificationDate)
				.setParameter(3, accessedBy)
				.setParameter(4, accessDate)
				.setParameter(5, dashboardId)
				.setParameter(6, tenantId);
		result = query.executeUpdate();
		return result;
	}

	private int updateDashboardFavorite(EntityManager entityManager, String userName, BigInteger dashboardId, String creationDate, Long tenantId, String lastModificationDate) {
		logger.info("Calling DataManager.updateDashboardFavorite");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_FAVORITE SET CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), DASHBOARD_ID=?, LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff')  WHERE USER_NAME=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, creationDate)
				.setParameter(2, userName)
				.setParameter(3, lastModificationDate)
				.setParameter(4, tenantId)
				.setParameter(5, dashboardId);
		result = query.executeUpdate();
		return result;
	}

	private int updateDashboardUserOption(EntityManager entityManager, String userName, Long tenantId, BigInteger dashboardId, Long autoRefreshInterval, String accessDate,
										  Integer isFavorite, String extendedOptions, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.updateDashboardUserOption");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_USER_OPTIONS SET CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), AUTO_REFRESH_INTERVAL=?, ACCESS_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), IS_FAVORITE=?, EXTENDED_OPTIONS=? WHERE USER_NAME=? AND TENANT_ID=? AND DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, creationDate)
				.setParameter(2, lastModificationDate)
				.setParameter(3, autoRefreshInterval)
				.setParameter(4, accessDate)
				.setParameter(5, isFavorite)
				.setParameter(6, extendedOptions)
				.setParameter(7, userName)
				.setParameter(8, tenantId)
				.setParameter(9, dashboardId);
		result = query.executeUpdate();
		return result;

	}

	private int updateDashboardSet(EntityManager entityManager, BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId,
								   Long position, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.updateDashboardSet");
		int result;
		String sql = "UPDATE EMS_DASHBOARD_SET SET CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), POSITION=? WHERE DASHBOARD_SET_ID=? AND TENANT_ID=? AND SUB_DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, creationDate)
				.setParameter(2, lastModificationDate)
				.setParameter(3, position)
				.setParameter(4, dashboardSetId)
				.setParameter(5, tenantId)
				.setParameter(6, subDashboardId);
		result = query.executeUpdate();
		return result;
	}

	private int updatePreferences(EntityManager entityManager, String userName, String prefKey,
								  String prefValue, Long tenantId, String creationDate, String lastModificationDate) {
		logger.info("Calling DataManager.updatePreference");
		int result;
		String sql = "UPDATE EMS_PREFERENCE SET PREF_VALUE=?, CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') WHERE USER_NAME=? AND PREF_KEY=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, prefValue)
				.setParameter(2, creationDate)
				.setParameter(3, lastModificationDate)
				.setParameter(4, userName)
				.setParameter(5, prefKey)
				.setParameter(6, tenantId);
		result = query.executeUpdate();
		return result;
	}

	private boolean isDashboardExist(EntityManager entityManager, BigInteger dashboardId, Long tenantId) {
		logger.info("Calling the Datamanager.isDashboardsExist");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD WHERE DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardId)
				.setParameter(2, tenantId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardTileExist(EntityManager entityManager, BigInteger tileId, BigInteger dashboardId, Long tenantId) {
		logger.info("Calling Datamanager.isDashboardTileExist");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_TILE WHERE TILE_ID=? AND DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId)
				.setParameter(2, dashboardId)
				.setParameter(3, tenantId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardTileParamExist(EntityManager entityManager, BigInteger tileId, String paramName, Long tenantId) {
		logger.info("Calling DataManager.isDashboardTileParamExit");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_TILE_PARAMS WHERE TILE_ID=? AND PARAM_NAME=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId)
				.setParameter(2, paramName)
				.setParameter(3, tenantId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardLastAccessExit(EntityManager entityManager, BigInteger dashboardId, Long tenantId) {
		logger.info("Calling DataManager.isDashboardLastAccessExit");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_LAST_ACCESS WHERE DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardId)
				.setParameter(2, tenantId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardFavoriteExit(EntityManager entityManager, String userName, BigInteger dashboardId, Long tenantId) {
		logger.info("Calling DataManager.updateDashboardFavorite");
        String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_FAVORITE WHERE USER_NAME=? AND DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, dashboardId)
				.setParameter(3, tenantId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardUserOptionExist(EntityManager entityManager, String userName, Long tenantId, BigInteger dashboardId) {
		logger.info("Calling DataManager.isDashboardUserOptionExit");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_USER_OPTIONS WHERE USER_NAME=? AND TENANT_ID=? AND DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, tenantId)
				.setParameter(3, dashboardId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardSetExist(EntityManager entityManager, BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId) {
		logger.info("Calling DataManager.isDashboardSetExist");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_SET WHERE DASHBOARD_SET_ID=? AND TENANT_ID=? AND SUB_DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardSetId)
				.setParameter(2, tenantId)
				.setParameter(3, subDashboardId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isPreferenceExist(EntityManager entityManager, String userName, String prefKey, Long tenantId) {
		logger.info("Calling DataManager.isPreferenceExist");
		String sql = "SELECT COUNT(1) FROM EMS_PREFERENCE WHERE USER_NAME=? AND PREF_KEY=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, prefKey)
				.setParameter(3, tenantId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private String getDashboardLastModifiedDate(EntityManager entityManager,BigInteger dashboardId, Long tenantId) {
		logger.info("Calling the Datamanager.getDashboardsLastModifiedDate");
		String sql = "SELECT LAST_MODIFICATION_DATE FROM EMS_DASHBOARD WHERE DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardId)
				.setParameter(2, tenantId);
		return query.getSingleResult().toString();

	}

	private String getDashboardTileLastModifiedDate(EntityManager entityManager, BigInteger tileId, BigInteger dashboardId, Long tenantId) {
		logger.info("Calling Datamanager.getDashboardTileLastModifiedDate");
		String sql = "SELECT LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_TILE WHERE TILE_ID=? AND DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId)
				.setParameter(2, dashboardId)
				.setParameter(3, tenantId);
		return query.getSingleResult().toString();
	}

	private String getDashboardTileParamLastModifiedDate(EntityManager entityManager, BigInteger tileId, String paramName, Long tenantId){
		logger.info("Calling DataManager.getDashboardTileParamLastModifiedDate");
		String sql = "SELECT LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_TILE_PARAMS WHERE TILE_ID=? AND PARAM_NAME=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1,tileId)
				.setParameter(2,paramName)
				.setParameter(3,tenantId);
		return query.getSingleResult().toString();
	}

	private String getDashboardLastAccessLastModifiedDate(EntityManager entityManager, BigInteger dashboardId, Long tenantId){
		logger.info("Calling DataManager.getDashboardLastAccessLastModifiedDate");
		String sql = "SELECT LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_LAST_ACCESS WHERE DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1,dashboardId)
				.setParameter(2,tenantId);
		return  query.getSingleResult().toString();
	}

	private String getDashboardFavoriteLastModifiedDate(EntityManager entityManager, String userName, BigInteger dashboardId, Long tenantId){
		logger.info("Calling DataManager.getDashboardFavoriteLastModifiedDate");
        String sql = "SELECT LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_FAVORITE WHERE USER_NAME=? AND DASHBOARD_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, dashboardId)
				.setParameter(3, tenantId);
		return query.getSingleResult().toString();
	}

	private String getDashboardUserOptionLastModifiedDate(EntityManager entityManager, String userName, Long tenantId, BigInteger dashboardId){
		logger.info("Calling DataManager.getDashboardUserOptionLastModifiedDate");
		String sql = "SELECT LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_USER_OPTIONS WHERE USER_NAME=? AND TENANT_ID=? AND DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, tenantId)
				.setParameter(3, dashboardId);
		return  query.getSingleResult().toString();
	}

	private String getDashboardSetLastModifiedDate(EntityManager entityManager, BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId){
		logger.info("Calling DataManager.getDashboardSetLastModifiedDate");
		String sql = "SELECT LAST_MODIFICATION_DATE FROM EMS_DASHBOARD_SET WHERE DASHBOARD_SET_ID=? AND TENANT_ID=? AND SUB_DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1,dashboardSetId)
				.setParameter(2,tenantId)
				.setParameter(3,subDashboardId);
		return query.getSingleResult().toString();
	}

	private String getPreferenceLastModifiedDate(EntityManager entityManager, String userName, String prefKey, Long tenantId){
		logger.info("Calling DataManager.isPreferenceExist");
		String sql = "SELECT LAST_MODIFICATION_DATE FROM EMS_PREFERENCE WHERE USER_NAME=? AND PREF_KEY=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1,userName)
				.setParameter(2,prefKey)
				.setParameter(3,tenantId);
		return query.getSingleResult().toString();
	}
}
