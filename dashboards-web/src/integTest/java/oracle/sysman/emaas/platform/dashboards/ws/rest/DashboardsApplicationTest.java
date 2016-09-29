package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = { "s1" })
public class DashboardsApplicationTest
{
	DashboardsApplication dashboardsApplication;

	@Test
	public void testGetClasses() 
	{
		dashboardsApplication = new DashboardsApplication();
		Set<Class<?>> hs = dashboardsApplication.getClasses();
		Assert.assertTrue(hs.contains(DashboardAPI.class));
		Assert.assertTrue(hs.contains(FavoriteAPI.class));
		Assert.assertTrue(hs.contains(PreferenceAPI.class));
		Assert.assertTrue(hs.contains(LoggingAPI.class));
		Assert.assertTrue(hs.contains(LoggingConfigAPI.class));
		Assert.assertTrue(hs.contains(ConfigurationAPI.class));
		Assert.assertTrue(hs.contains(TenantSubscriptionsAPI.class));
		Assert.assertTrue(hs.contains(WidgetNotificationAPI.class));
	}
}