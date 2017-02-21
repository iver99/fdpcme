package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author pingwu
 */
public class DataImportManager
{
	private static final Logger LOGGER = LogManager.getLogger(DataImportManager.class);

	private static DataImportManager instance = new DataImportManager();
	/**
	 * Returns the singleton instance 
	 *
	 * @return
	 */
	public static DataImportManager getInstance()
	{
		return instance;
	}

	public int saveDashboards(BigInteger dashboardId, String name, Long type, String description, String creationDate, String lastModificationDate, String lastModifiedBy, String owner,
									 Integer isSystem, Integer applicationType, Integer enableTimeRange, String screenShot, BigInteger deleted, Long tenantId, Integer enableRefresh, Integer sharePublic,
									 Integer enableEntityFilter, Integer enableDescription, String extendedOptions, Integer showInHome) {
		LOGGER.debug("Calling the DataImportManager.saveDashboards");		

		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}		
		try {
			if (!isDashboardExist(entityManager, dashboardId, tenantId,description, owner, deleted)) {
				LOGGER.debug("Dashboard with id {} does not exist, insert it", dashboardId);
				return insertDashboard(entityManager, dashboardId, name, type, description, creationDate, lastModificationDate, lastModifiedBy, owner,
						isSystem, applicationType, enableTimeRange, screenShot, deleted, tenantId, enableRefresh, sharePublic, enableEntityFilter, enableDescription, extendedOptions, showInHome);
			
			} else {
				return 0;
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}
	
	private int updateDashboard(EntityManager entityManager, BigInteger dashboardId, String name, Long type, String description, String creationDate, String lastModificationDate, String lastModifiedBy, String owner,
			Integer isSystem, Integer applicationType, Integer enableTimeRange, String screenShot, BigInteger deleted, Long tenantId, Integer enableRefresh, Integer sharePublic,
			Integer enableEntityFilter, Integer enableDescription, String extendedOptions, Integer showInHome) {
		LOGGER.debug("Calling the DataImportManager.updateDashboard");
		int result;
		String sql = "UPDATE EMS_DASHBOARD SET  NAME=?, TYPE=?, DESCRIPTION=?, CREATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFICATION_DATE=to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff'), LAST_MODIFIED_BY=?, OWNER=?, IS_SYSTEM=?, APPLICATION_TYPE=?, ENABLE_TIME_RANGE=?, SCREEN_SHOT=?, DELETED=?, ENABLE_REFRESH=?, SHARE_PUBLIC=?, ENABLE_ENTITY_FILTER=?, ENABLE_DESCRIPTION=?, EXTENDED_OPTIONS=?, SHOW_INHOME=? WHERE DASHBOARD_ID=? AND TENANT_ID=?";
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
	.setParameter(19, tenantId)
	.setParameter(20, showInHome);
		result = query.executeUpdate();
		return result;
	}

	public int saveDashboardTile(String tileId, BigInteger dashboardId, String creationDate, String lastModificationDate, String lastModifiedBy, String owner, String title, Long height,
								 Long width, Integer isMaximized, Long position, Long tenantId, String widgetUniqueId, String widgetName, String widgetDescription, String widgetGroupName,
								 String widgetIcon, String widgetHistogram, String widgetOwner, String widgetCreationTime, Long widgetSource, String widgetKocName, String widgetViewmode, String widgetTemplate,
								 String providerName, String providerVersion, String providerAssetRoot, Long tileRow, Long tileColumn, Long type, Integer widgetSupportTimeControl, Long widgetLinkedDashboard) {
		LOGGER.debug("Calling the DataImportManager,saveDashboardTile");
		
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
		try {
			if (!isDashboardTileExist(entityManager, tileId, tenantId)) {
				LOGGER.debug("Tile with id {} does not exist, insert it", tileId);
				return insertDashboardTile(entityManager,tileId, dashboardId, creationDate, lastModificationDate, lastModifiedBy, owner, title, height,
						width, isMaximized, position, tenantId, widgetUniqueId, widgetName, widgetDescription, widgetGroupName,
						widgetIcon, widgetHistogram, widgetOwner, widgetCreationTime, widgetSource, widgetKocName, widgetViewmode, widgetTemplate,
						providerName, providerVersion, providerAssetRoot, tileRow, tileColumn, type, widgetSupportTimeControl, widgetLinkedDashboard);
			
			} else {
				return 0;
			}
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	public int saveDashboardTileParam( String tileId, String paramName,
									  Long tenantId, Integer isSystem, Long paramType, String paramValueStr,
									  Long paramValueNum, String paramValueTimestamp, String creationDate, String lastModificationDate) {
		LOGGER.debug("Calling DataImportManager.saveDashboardTileParam");
		
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
		try {
			if (!isDashboardTileParamExist(entityManager, tileId, paramName, tenantId)) {
				LOGGER.debug("Tile param with id {} does not exist, insert it", tileId);
				return insertDashboardTileParam(entityManager, tileId, paramName, tenantId,
						isSystem, paramType, paramValueStr, paramValueNum,
						paramValueTimestamp, creationDate, lastModificationDate);
			} else {
				return 0;
			}
			
		}catch (Exception e){
			LOGGER.error(e.getLocalizedMessage());
			return 0;
		}finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}


	public int saveDashboardUserOption(String userName, Long tenantId, BigInteger dashboardId,
									   Long autoRefreshInterval, String accessDate, Integer isFavorite, String extendedOptions,
									   String creationDate, String lastModificationDate) {
		LOGGER.debug("Calling DataImportManager.saveDashboardUserOption");
		
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
		try {
			if (!isDashboardUserOptionExist(entityManager, userName, tenantId, dashboardId)) {
				LOGGER.debug("Dashboard User Option with id {} does not exist, insert it", dashboardId);
				return insertDashboardUserOption(entityManager, userName, tenantId, dashboardId, autoRefreshInterval,
						accessDate, isFavorite, extendedOptions, creationDate, lastModificationDate);
			} else {
				return 0;
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	public int saveDashboardSet(BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId,
								Long position, String creationDate, String lastModificationDate) {
		LOGGER.debug("Calling DataImportManager.saveDashboardSet", dashboardSetId);
		
		EntityManager entityManager = new DashboardServiceFacade().getEntityManager();
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
		}
		try {
			if (!isDashboardSetExist(entityManager, dashboardSetId, tenantId, subDashboardId)) {
				LOGGER.debug("Dashboard Set with id {} does not exist insert now", dashboardSetId);
				return insertDashboardSet(entityManager, dashboardSetId, tenantId, subDashboardId, position, creationDate, 
						lastModificationDate);
			} else {
				return 0;
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
			return 0;
		} finally {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}

	private int insertDashboard(EntityManager entityManager, BigInteger dashboardId, String name, Long type, String description, String creationDate, String lastModificationDate, String lastModifiedBy, String owner,
								Integer isSystem, Integer applicationType, Integer enableTimeRange, String screenShot, BigInteger deleted, Long tenantId, Integer enableRefresh, Integer sharePublic,
								Integer enableEntityFilter, Integer enableDescription, String extendedOptions, Integer showInHome) {
		LOGGER.debug("Calling the DataImportManager.insertDashboard");
		int result;
		String sql = "INSERT INTO EMS_DASHBOARD(DASHBOARD_ID,  NAME, TYPE, DESCRIPTION, CREATION_DATE, LAST_MODIFICATION_DATE, LAST_MODIFIED_BY, OWNER, IS_SYSTEM, APPLICATION_TYPE, ENABLE_TIME_RANGE, SCREEN_SHOT, DELETED, TENANT_ID, ENABLE_REFRESH, SHARE_PUBLIC, ENABLE_ENTITY_FILTER, ENABLE_DESCRIPTION, EXTENDED_OPTIONS, SHOW_INHOME)values(?,?,?,?,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') ,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss.ff') ,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
				.setParameter(19, extendedOptions)
				.setParameter(20, showInHome);
		LOGGER.debug(query.toString());
		result = query.executeUpdate();
		return result;
	}

	private int insertDashboardTile(EntityManager entityManager, String tileId, BigInteger dashboardId, String creationDate, String lastModificationDate, String lastModifiedBy, String owner, String title, Long height,
									Long width, Integer isMaximized, Long position, Long tenantId, String widgetUniqueId, String widgetName, String widgetDescription, String widgetGroupName,
									String widgetIcon, String widgetHistogram, String widgetOwner, String widgetCreationTime, Long widgetSource, String widgetKocName, String widgetViewmode, String widgetTemplate,
									String providerName, String providerVersion, String providerAssetRoot, Long tileRow, Long tileColumn, Long type, Integer widgetSupportTimeControl, Long widgetLinkedDashboard) {
		LOGGER.debug("Calling DataImportManager.insertDashboardTiles");
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

	private int insertDashboardTileParam(EntityManager entityManager, String tileId, String paramName, Long tenantId, Integer isSystem, Long paramType, String paramValueStr, Long paramValueNum, String paramValueTimestamp, String creationDate, String lastModificationDate) {
		LOGGER.debug("Calling DataImportManager.insertDashboardTileParam");
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


	private int insertDashboardUserOption(EntityManager entityManager, String userName, Long tenantId, BigInteger dashboardId, Long autoRefreshInterval, String accessDate,
										  Integer isFavorite, String extendedOptions, String creationDate, String lastModificationDate) {
		LOGGER.debug("Calling DataImportManager.insertDashboardUserOptions");
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
		LOGGER.debug("Calling DataImportManager.insertDashboardSet");
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

	private boolean isDashboardExist(EntityManager entityManager, BigInteger dashboardId, Long tenantId, 
			String description, String owner, BigInteger deleted) {
		LOGGER.debug("Calling the DataImportManager.isDashboardsExist");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD WHERE (DASHBOARD_ID=? AND TENANT_ID=?) "
				+ "or (DESCRIPTION=? and OWNER=? AND TENANT_ID =? AND DELETED=?)";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardId)
				.setParameter(2, tenantId)
		.setParameter(3, description)
		.setParameter(4, owner)
		.setParameter(5, tenantId)
		.setParameter(6, deleted);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardTileExist(EntityManager entityManager, String tileId,Long tenantId) {
		LOGGER.debug("Calling DataImportManager.isDashboardTileExist");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_TILE WHERE TILE_ID=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId)
				.setParameter(2, tenantId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardTileParamExist(EntityManager entityManager, String tileId, String paramName, Long tenantId) {
		LOGGER.debug("Calling DataImportManager.isDashboardTileParamExit");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_TILE_PARAMS WHERE TILE_ID=? AND PARAM_NAME=? AND TENANT_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, tileId)
				.setParameter(2, paramName)
				.setParameter(3, tenantId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardUserOptionExist(EntityManager entityManager, String userName, Long tenantId, BigInteger dashboardId) {
		LOGGER.debug("Calling DataImportManager.isDashboardUserOptionExit");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_USER_OPTIONS WHERE USER_NAME=? AND TENANT_ID=? AND DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, userName)
				.setParameter(2, tenantId)
				.setParameter(3, dashboardId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}

	private boolean isDashboardSetExist(EntityManager entityManager, BigInteger dashboardSetId, Long tenantId, BigInteger subDashboardId) {
		LOGGER.debug("Calling DataImportManager.isDashboardSetExist");
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_SET WHERE DASHBOARD_SET_ID=? AND TENANT_ID=? AND SUB_DASHBOARD_ID=?";
		Query query = entityManager.createNativeQuery(sql)
				.setParameter(1, dashboardSetId)
				.setParameter(2, tenantId)
				.setParameter(3, subDashboardId);
		long count = ((Number) query.getSingleResult()).longValue();
		return count > 0;
	}
}
