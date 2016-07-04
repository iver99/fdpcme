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

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
		String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD_FAVORITE";
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
}
