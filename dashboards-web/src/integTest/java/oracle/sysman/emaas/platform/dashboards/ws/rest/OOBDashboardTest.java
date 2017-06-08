package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * Created by xiadai on 2017/2/9.
 */
@Test
public class OOBDashboardTest {
    // verify 1.16.0/emaas_dashboards_seed_data_os.sql
    @Test
    public void testSecurityOOBDashboard(){
        EntityManager entityManager = null;
        try {
            DashboardServiceFacade dsf = new DashboardServiceFacade();
            entityManager = dsf.getEntityManager();
            String sql = "SELECT COUNT(1) FROM EMS_DASHBOARD WHERE DASHBOARD_ID=40";
            Query query = entityManager.createNativeQuery(sql);
            Long count = ((Number) query.getSingleResult()).longValue();
            Assert.assertTrue(count > 0);
        }finally {
            if (entityManager != null) {
                entityManager.close();
           }
        }
    }
}
