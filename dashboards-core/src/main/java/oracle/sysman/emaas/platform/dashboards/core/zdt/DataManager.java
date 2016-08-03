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
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			Query query = em.createNativeQuery(nativeSql);
			query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = query.getResultList();
			return list;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
