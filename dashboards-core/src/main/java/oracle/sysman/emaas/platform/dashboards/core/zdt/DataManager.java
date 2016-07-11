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

import org.codehaus.jettison.json.JSONArray;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultType;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;

/**
 * @author guochen
 */
public class DataManager
{
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
		DashboardServiceFacade dsf = new DashboardServiceFacade();
		EntityManager em = dsf.getEntityManager();
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD WHERE DELETED <> 1";
		Query query = em.createNativeQuery(sql);
		long count = ((Number) query.getSingleResult()).longValue();
		return count;
	}

	/**
	 * Retrieves total count for all favorites from all tenants
	 *
	 * @return
	 */
	public long getAllFavoriteCount()
	{
		DashboardServiceFacade dsf = new DashboardServiceFacade();
		EntityManager em = dsf.getEntityManager();
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD d, EMS_DASHBOARD_USER_OPTIONS uo WHERE d.DASHBOARD_ID=uo.DASHBOARD_ID AND d.TENANT_ID=uo.TENANT_ID AND d.DELETED<>1 AND IS_FAVORITE=1";
		Query query = em.createNativeQuery(sql);
		long count = ((Number) query.getSingleResult()).longValue();
		return count;
	}

	/**
	 * Retrieves total count for all preferences from all tenants
	 *
	 * @return
	 */
	public long getAllPreferencessCount()
	{
		DashboardServiceFacade dsf = new DashboardServiceFacade();
		EntityManager em = dsf.getEntityManager();
		String sql = "SELECT COUNT(1) FROM EMS_PREFERENCE";
		Query query = em.createNativeQuery(sql);
		long count = ((Number) query.getSingleResult()).longValue();
		return count;
	}

	public JSONArray getDashboardTableData()
	{
		JSONArray array = new JSONArray();
		DashboardServiceFacade dsf = new DashboardServiceFacade();
		EntityManager em = dsf.getEntityManager();
		String sql = "SELECT * FROM EMS_DASHBOARD";
		Query query = em.createNativeQuery(sql);
		query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = query.getResultList();
		for (Map<String, Object> row : list) {
			array.put(row);
		}
		return array;
	}
}
