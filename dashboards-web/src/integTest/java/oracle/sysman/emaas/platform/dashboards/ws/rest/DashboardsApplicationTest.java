package oracle.sysman.emaas.platform.dashboards.ws.rest;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * Created by jishshi on 1/20/2016.
 */
@Test(groups = {"s1"})
public class DashboardsApplicationTest {
    DashboardsApplication dashboardsApplication;

    @Test
    public void testGetClasses() throws Exception {
        dashboardsApplication = new DashboardsApplication();
        Set<Class<?>> hs = dashboardsApplication.getClasses();
        Assert.assertTrue(hs.contains(DashboardAPI.class));
        Assert.assertTrue(hs.contains(FavoriteAPI.class));
        Assert.assertTrue(hs.contains(PreferenceAPI.class));
        Assert.assertTrue(hs.contains(LoggingAPI.class));
        Assert.assertTrue(hs.contains(LoggingConfigAPI.class));
        Assert.assertTrue(hs.contains(ConfigurationAPI.class));
        Assert.assertTrue(hs.contains(TenantSubscriptionsAPI.class));
    }
}