package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.testng.Assert;
import org.testng.annotations.Test;

import mockit.Deencapsulation;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;

public class DashboardServiceFacadeSoftDeletionTest
{
	private static final long TENANT_ID = 12345l;

	@Test
	public void testPrefecence()
	{
		EntityManager em = null;
		try {
			String key = "prefKey1";
			String value = "prefValue";
			String user = "test user";
			DashboardServiceFacade dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			em = dashboardServiceFacade.getEntityManager();
			EmsPreference p = new EmsPreference(key, value, user);
			dashboardServiceFacade.persistEmsPreference(p);

			p = dashboardServiceFacade.getEmsPreference(user, key);
			Assert.assertNotNull(p);
			Assert.assertNotNull(dashboardServiceFacade.getEmsPreferenceFindAll("test"));

			dashboardServiceFacade.removeEmsPreference(p);
			String sql = "select count(*) from EMS_PREFERENCE p where p.USER_NAME=? and p.PREF_KEY=? and p.TENANT_ID=? and p.DELETED=?";
			Integer deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(user, key, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));

			// after soft deletion, re-create
			p = new EmsPreference(key, value, user);
			dashboardServiceFacade.persistEmsPreference(p);
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(user, key, TENANT_ID, 1));
			Assert.assertEquals(deletedCount, Integer.valueOf(0));
			deletedCount = sqlCheckCountsWithSql(sql, Arrays.asList(user, key, TENANT_ID, 0));
			Assert.assertEquals(deletedCount, Integer.valueOf(1));

			// cleanup
			sqlExecuteUpdate("delete from EMS_PREFERENCE p where p.USER_NAME=? and p.PREF_KEY=? and p.TENANT_ID=?",
					Arrays.asList(user, key, TENANT_ID));
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private Integer sqlCheckCountsWithSql(String sql, List<? extends Object> params)
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			em = dashboardServiceFacade.getEntityManager();
			Query query = em.createNativeQuery(sql);
			Deencapsulation.invoke(dashboardServiceFacade, "initializeQueryParams", query, params);
			Object result = query.getSingleResult();
			if (result instanceof BigDecimal) {
				return ((BigDecimal) result).intValue();
			}
			else if (result instanceof Integer) {
				return ((Integer) result).intValue();
			}
			return null;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private void sqlExecuteUpdate(String sql, List<? extends Object> params)
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dashboardServiceFacade = new DashboardServiceFacade(TENANT_ID);
			em = dashboardServiceFacade.getEntityManager();
			Query query = em.createNativeQuery(sql);
			Deencapsulation.invoke(dashboardServiceFacade, "initializeQueryParams", query, params);
			final EntityTransaction entityTransaction = em.getTransaction();
			if (!entityTransaction.isActive()) {
				entityTransaction.begin();
			}
			query.executeUpdate();
			entityTransaction.commit();
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
