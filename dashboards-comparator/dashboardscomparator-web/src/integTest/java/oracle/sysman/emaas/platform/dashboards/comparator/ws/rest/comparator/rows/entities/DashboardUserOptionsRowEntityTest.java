package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardUserOptionsRowEntityTest
{
	@Test(groups = { "s1" })
	public void testEquals()
	{
		DashboardUserOptionsRowEntity duore1 = new DashboardUserOptionsRowEntity();
		DashboardUserOptionsRowEntity duore2 = new DashboardUserOptionsRowEntity();
		Assert.assertEquals(duore1, duore2);
		duore1.setUserName("emcsadmin");
		duore2.setUserName("emcsadmin");
		Assert.assertEquals(duore1, duore2);
		duore1.setTenantId(1L);
		duore2.setTenantId(1L);
		Assert.assertEquals(duore1, duore2);
		duore1.setDashboardId(1L);
		duore2.setDashboardId(1L);
		Assert.assertEquals(duore1, duore2);
		duore1.setAutoRefreshInterval(300L);
		duore2.setAutoRefreshInterval(300L);
		Assert.assertEquals(duore1, duore2);
		duore1.setIsFavorite(1);
		duore2.setIsFavorite(1);
		Assert.assertEquals(duore1, duore2);
		duore1.setExtendedOptions("options");
		duore2.setExtendedOptions("options");
		Assert.assertEquals(duore1, duore2);

		duore2.setUserName("emcsadmin2");
		Assert.assertNotEquals(duore1, duore2);
		duore2.setUserName("emcsadmin");
		duore2.setTenantId(2L);
		Assert.assertNotEquals(duore1, duore2);
		duore2.setTenantId(1L);
		duore2.setDashboardId(2L);
		Assert.assertNotEquals(duore1, duore2);
		duore2.setDashboardId(1L);
		duore2.setAutoRefreshInterval(500L);
		Assert.assertNotEquals(duore1, duore2);
		duore2.setAutoRefreshInterval(300L);
		duore2.setIsFavorite(0);
		Assert.assertNotEquals(duore1, duore2);
		duore2.setIsFavorite(1);
		duore2.setExtendedOptions("options2");
		Assert.assertNotEquals(duore1, duore2);
		duore2.setExtendedOptions("options");
		Assert.assertEquals(duore1, duore2);
	}

	@Test(groups = { "s1" })
	public void testHashCode()
	{
		DashboardUserOptionsRowEntity duore1 = new DashboardUserOptionsRowEntity();
		DashboardUserOptionsRowEntity duore2 = new DashboardUserOptionsRowEntity();
		Assert.assertEquals(duore1.hashCode(), duore2.hashCode());
		duore1.setUserName("emcsadmin");
		duore2.setUserName("emcsadmin");
		Assert.assertEquals(duore1.hashCode(), duore2.hashCode());
		duore1.setTenantId(1L);
		duore2.setTenantId(1L);
		Assert.assertEquals(duore1.hashCode(), duore2.hashCode());
		duore1.setDashboardId(1L);
		duore2.setDashboardId(1L);
		Assert.assertEquals(duore1.hashCode(), duore2.hashCode());
		duore1.setAutoRefreshInterval(300L);
		duore2.setAutoRefreshInterval(300L);
		Assert.assertEquals(duore1.hashCode(), duore2.hashCode());
		duore1.setIsFavorite(1);
		duore2.setIsFavorite(1);
		Assert.assertEquals(duore1.hashCode(), duore2.hashCode());
		duore1.setExtendedOptions("options");
		duore2.setExtendedOptions("options");
		Assert.assertEquals(duore1.hashCode(), duore2.hashCode());

		duore2.setUserName("emcsadmin2");
		Assert.assertNotEquals(duore1.hashCode(), duore2.hashCode());
		duore2.setUserName("emcsadmin");
		duore2.setTenantId(2L);
		Assert.assertNotEquals(duore1.hashCode(), duore2.hashCode());
		duore2.setTenantId(1L);
		duore2.setDashboardId(2L);
		Assert.assertNotEquals(duore1.hashCode(), duore2.hashCode());
		duore2.setDashboardId(1L);
		duore2.setAutoRefreshInterval(500L);
		Assert.assertNotEquals(duore1.hashCode(), duore2.hashCode());
		duore2.setAutoRefreshInterval(300L);
		duore2.setIsFavorite(0);
		Assert.assertNotEquals(duore1.hashCode(), duore2.hashCode());
		duore2.setIsFavorite(1);
		duore2.setExtendedOptions("options2");
		Assert.assertNotEquals(duore1.hashCode(), duore2.hashCode());
		duore2.setExtendedOptions("options");
		Assert.assertEquals(duore1.hashCode(), duore2.hashCode());
	}
}
