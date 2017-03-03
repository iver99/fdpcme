package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultType;

/**
 * 
 * @author pingwu
 *
 */
public class DataExportManager {
	
	private static final Logger LOGGER = LogManager.getLogger(DataExportManager.class);
	
	private static DataExportManager exportManager = new DataExportManager();
	
	
	
	public static DataExportManager getInstatnce() {
		return exportManager;
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */	
	public List<Map<String,Object>> getDashboardSetByDashboardSetIDs(List<BigInteger> ids, Long tenantId) {
		if (ids != null && !ids.isEmpty()) {
			String sql = "SELECT TO_CHAR(DASHBOARD_SET_ID) AS DASHBOARD_SET_ID, TENANT_ID, "
			        + "TO_CHAR(SUB_DASHBOARD_ID) AS SUB_DASHBOARD_ID, POSITION, CREATION_DATE, "
			        + "LAST_MODIFICATION_DATE, TO_CHAR(DELETED) AS DELETED FROM EMS_DASHBOARD_SET "
			        + "WHERE TENANT_ID = " + tenantId + " AND DASHBOARD_SET_ID IN ("
					+ generateQueryCondition(ids) + ")";
			return getTableData(sql);
		}
		return Collections.emptyList();
	}
	
	/**
	 * the input ids should contains the sub dashboard id of dashboard set
	 * @param ids
	 * @return
	 */
	public List<Map<String,Object>> getDashboardByDashboardIDs(List<BigInteger> ids, Long tenantId) {
		if (ids != null && !ids.isEmpty()) {
			String sql = "SELECT TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID,  NAME, TYPE, DESCRIPTION, CREATION_DATE, LAST_MODIFICATION_DATE,"
			        + " LAST_MODIFIED_BY, OWNER, IS_SYSTEM, APPLICATION_TYPE, ENABLE_TIME_RANGE, SCREEN_SHOT,"
			        + " TO_CHAR(DELETED) AS DELETED, TENANT_ID, ENABLE_REFRESH, SHARE_PUBLIC, ENABLE_ENTITY_FILTER, "
			        + "ENABLE_DESCRIPTION, EXTENDED_OPTIONS, SHOW_INHOME FROM EMS_DASHBOARD "
			        + "WHERE TENANT_ID = " + tenantId + "AND DASHBOARD_ID IN ("
					+ generateQueryCondition(ids) + ")";
			return getTableData(sql);
		}
		return Collections.emptyList();
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List<Map<String,Object>> getTileByDashboardIDs(List<BigInteger> ids, Long tenantId) {
		if (ids != null && !ids.isEmpty()) {
			String sql = "SELECT TO_CHAR(TILE_ID) AS TILE_ID, TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID, "
			        + "CREATION_DATE, LAST_MODIFICATION_DATE, "
			        + "LAST_MODIFIED_BY, OWNER, TITLE, HEIGHT, WIDTH, IS_MAXIMIZED, POSITION, "
			        + "TENANT_ID, TO_CHAR(WIDGET_UNIQUE_ID) AS WIDGET_UNIQUE_ID, WIDGET_NAME, WIDGET_DESCRIPTION, WIDGET_GROUP_NAME,"
			        + " WIDGET_ICON, WIDGET_HISTOGRAM, WIDGET_OWNER, WIDGET_CREATION_TIME, "
			        + "WIDGET_SOURCE, WIDGET_KOC_NAME, WIDGET_VIEWMODE, WIDGET_TEMPLATE, PROVIDER_NAME, "
			        + "PROVIDER_VERSION, PROVIDER_ASSET_ROOT, TILE_ROW, TILE_COLUMN, TYPE, "
			        + "WIDGET_SUPPORT_TIME_CONTROL, WIDGET_LINKED_DASHBOARD,WIDGET_DELETED,WIDGET_DELETION_DATE, DELETED "
			        + "FROM EMS_DASHBOARD_TILE WHERE TENANT_ID = " + tenantId + " AND DASHBOARD_ID IN ("
					+ generateQueryCondition(ids) + ")";
			return getTableData(sql);
		}
		return Collections.emptyList();
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List<Map<String,Object>> getTileParamsByTileIDs(List<BigInteger> ids, Long tenantId) {
		if (ids != null && !ids.isEmpty()) {
			String sql = "SELECT TO_CHAR(TILE_ID) AS TILE_ID, PARAM_NAME, TENANT_ID, IS_SYSTEM, PARAM_TYPE, "
			        + "PARAM_VALUE_STR, PARAM_VALUE_NUM, PARAM_VALUE_TIMESTAMP, CREATION_DATE, "
			        + "LAST_MODIFICATION_DATE, DELETED FROM EMS_DASHBOARD_TILE_PARAMS "
			        + "WHERE TENANT_ID = " + tenantId + " AND TILE_ID IN ("
					+ generateQueryCondition(ids) + ")";
			return getTableData(sql);
		}
		return Collections.emptyList();
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public List<Map<String,Object>> getUserOptionsByDashboardIds(List<BigInteger> ids, Long tenantId) {
		if (ids != null && !ids.isEmpty()) {
			String sql = "SELECT USER_NAME, TENANT_ID, TO_CHAR(DASHBOARD_ID) AS DASHBOARD_ID, AUTO_REFRESH_INTERVAL, ACCESS_DATE, "
			        + "IS_FAVORITE, EXTENDED_OPTIONS, CREATION_DATE, LAST_MODIFICATION_DATE, DELETED "
			        + "FROM EMS_DASHBOARD_USER_OPTIONS "
			        + "WHERE TENANT_ID = " + tenantId + " AND DASHBOARD_ID IN ("
					+ generateQueryCondition(ids) + ")";
			return getTableData(sql);
		}
		return Collections.emptyList();
	}
	
	private List<Map<String, Object>> getTableData(String querySql)
	{
		if (StringUtil.isEmpty(querySql)) {
			LOGGER.error("can not query table with null or empty SQL!");
			return null;
		}
		DashboardServiceFacade dsf = new DashboardServiceFacade();
		EntityManager em = dsf.getEntityManager();
		Query query = em.createNativeQuery(querySql);
		query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = query.getResultList();
		return list;
	}
	
	private String generateQueryCondition(List<BigInteger> ids) {
		if (ids != null && !ids.isEmpty()) {
			StringBuilder parameters = new StringBuilder();
			int flag = 0;
			for (BigInteger id : ids) {
				if (flag++ > 0) {
					parameters.append(",");
				}
				parameters.append(id);
			}
			return parameters.toString();
		}
		return null;
	}
	
	
  	

}
