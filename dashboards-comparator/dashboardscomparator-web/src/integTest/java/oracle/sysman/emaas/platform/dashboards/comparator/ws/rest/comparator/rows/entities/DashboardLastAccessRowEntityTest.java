package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardLastAccessRowEntityTest
{
	@Test(groups = { "s1" })
	public void testEquals()
	{
		DashboardLastAccessRowEntity dlare1 = new DashboardLastAccessRowEntity();
		DashboardLastAccessRowEntity dlare2 = new DashboardLastAccessRowEntity();
		Assert.assertEquals(dlare1, dlare2);
		dlare1.setDashboardId(1L);
		dlare2.setDashboardId(1L);
		Assert.assertEquals(dlare1, dlare2);
		dlare1.setTenantId(1L);
		dlare2.setTenantId(1L);
		Assert.assertEquals(dlare1, dlare2);
		dlare1.setAccessedBy("emcsadmin");
		dlare2.setAccessedBy("emcsadmin");
		Assert.assertEquals(dlare1, dlare2);

		dlare2.setDashboardId(2L);
		Assert.assertNotEquals(dlare1, dlare2);
		dlare2.setDashboardId(1L);
		dlare2.setTenantId(2L);
		Assert.assertNotEquals(dlare1, dlare2);
		dlare2.setTenantId(1L);
		dlare2.setAccessedBy("emcsadmin2");
		Assert.assertNotEquals(dlare1, dlare2);
		dlare2.setAccessedBy("emcsadmin");
		Assert.assertEquals(dlare1, dlare2);
	}

	@Test(groups = { "s1" })
	public void testHashCode()
	{
		DashboardLastAccessRowEntity dlare1 = new DashboardLastAccessRowEntity();
		DashboardLastAccessRowEntity dlare2 = new DashboardLastAccessRowEntity();
		Assert.assertEquals(dlare1.hashCode(), dlare2.hashCode());
		dlare1.setDashboardId(1L);
		dlare2.setDashboardId(1L);
		Assert.assertEquals(dlare1.hashCode(), dlare2.hashCode());
		dlare1.setTenantId(1L);
		dlare2.setTenantId(1L);
		Assert.assertEquals(dlare1.hashCode(), dlare2.hashCode());
		dlare1.setAccessedBy("emcsadmin");
		dlare2.setAccessedBy("emcsadmin");
		Assert.assertEquals(dlare1.hashCode(), dlare2.hashCode());

		dlare2.setDashboardId(2L);
		Assert.assertNotEquals(dlare1.hashCode(), dlare2.hashCode());
		dlare2.setDashboardId(1L);
		dlare2.setTenantId(2L);
		Assert.assertNotEquals(dlare1.hashCode(), dlare2.hashCode());
		dlare2.setTenantId(1L);
		dlare2.setAccessedBy("emcsadmin2");
		Assert.assertNotEquals(dlare1.hashCode(), dlare2.hashCode());
		dlare2.setAccessedBy("emcsadmin");
		Assert.assertEquals(dlare1.hashCode(), dlare2.hashCode());
	}
}
