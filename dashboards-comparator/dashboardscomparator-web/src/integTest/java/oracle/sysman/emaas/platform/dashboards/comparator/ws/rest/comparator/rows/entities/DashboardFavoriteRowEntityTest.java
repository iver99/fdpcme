package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardFavoriteRowEntityTest
{
	@Test
	public void testEquals()
	{
		DashboardFavoriteRowEntity dfre1 = new DashboardFavoriteRowEntity();
		DashboardFavoriteRowEntity dfre2 = new DashboardFavoriteRowEntity();
		Assert.assertEquals(dfre1, dfre2);
		dfre1.setDashboardId(1L);
		dfre2.setDashboardId(1L);
		Assert.assertEquals(dfre1, dfre2);
		dfre1.setTenantId(1L);
		dfre2.setTenantId(1L);
		Assert.assertEquals(dfre1, dfre2);
		dfre1.setUserName("emcsadmin");
		dfre2.setUserName("emcsadmin");
		Assert.assertEquals(dfre1, dfre2);

		dfre2.setDashboardId(2L);
		Assert.assertNotEquals(dfre1, dfre2);
		dfre2.setDashboardId(1L);
		dfre2.setTenantId(2L);
		Assert.assertNotEquals(dfre1, dfre2);
		dfre2.setTenantId(1L);
		dfre2.setUserName("emcsadmin1");
		Assert.assertNotEquals(dfre1, dfre2);
	}

	@Test
	public void testHashCode()
	{
		DashboardFavoriteRowEntity dfre1 = new DashboardFavoriteRowEntity();
		DashboardFavoriteRowEntity dfre2 = new DashboardFavoriteRowEntity();
		Assert.assertEquals(dfre1.hashCode(), dfre2.hashCode());
		dfre1.setDashboardId(1L);
		dfre2.setDashboardId(1L);
		Assert.assertEquals(dfre1.hashCode(), dfre2.hashCode());
		dfre1.setTenantId(1L);
		dfre2.setTenantId(1L);
		Assert.assertEquals(dfre1.hashCode(), dfre2.hashCode());
		dfre1.setUserName("emcsadmin");
		dfre2.setUserName("emcsadmin");
		Assert.assertEquals(dfre1.hashCode(), dfre2.hashCode());

		dfre2.setDashboardId(2L);
		Assert.assertNotEquals(dfre1.hashCode(), dfre2.hashCode());
		dfre2.setDashboardId(1L);
		dfre2.setTenantId(2L);
		Assert.assertNotEquals(dfre1.hashCode(), dfre2.hashCode());
		dfre2.setTenantId(1L);
		dfre2.setUserName("emcsadmin1");
		Assert.assertNotEquals(dfre1.hashCode(), dfre2.hashCode());
	}
}
