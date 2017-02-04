package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigInteger;
import java.util.Collections;
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
			String sql = "SELECT * FROM EMS_DASHBOARD_SET WHERE TENANT_ID = ? AND DASHBOARD_SET_ID IN ("
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
			String sql = "SELECT * FROM EMS_DASHBOARD WHERE TENANT_ID = ? AND DASHBOARD_ID IN ("
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
			String sql = "SELECT * FROM EMS_DASHBOARD_TILE WHERE TENANT_ID = ? AND DASHBOARD_ID IN ("
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
			String sql = "SELECT * FROM EMS_DASHBOARD_TILE_PARAMS WHERE TENANT_ID = ? AND TILE_ID IN ("
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
			String sql = "SELECT * FROM EMS_DASHBOARD_USER_OPTIONS WHERE TENANT_ID = ? AND DASHBOARD_ID IN ("
					+ generateQueryCondition(ids) + ")";
			return getTableData(sql);
		}
		return Collections.emptyList();
	}
	
	/**
	 * the input widget ids comes from the widget unique ids in dashboard tile table
	 * @param ids
	 * @return
	 */
	public List<Map<String,Object>> getSearchByWidgetIds(List<BigInteger> ids, Long tenantId) {
		// to do
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
