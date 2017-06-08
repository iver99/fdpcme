package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import java.math.BigInteger;

import org.testng.Assert;
import org.testng.annotations.Test;
@Test(groups = { "s1" })
public class DashboardSetRowEntityTest
{
	@Test
	public void testEquals()
	{
		DashboardSetRowEntity dsre1 = new DashboardSetRowEntity();
		DashboardSetRowEntity dsre2 = new DashboardSetRowEntity();
		Assert.assertEquals(dsre1, dsre2);
		dsre1.setDashboardSetId("12");
		dsre2.setDashboardSetId("12");
		Assert.assertEquals(dsre1, dsre2);
		dsre1.setTenantId(1L);
		dsre2.setTenantId(1L);
		Assert.assertEquals(dsre1, dsre2);
		dsre1.setSubDashboardId("1");
		dsre2.setSubDashboardId("1");
		Assert.assertEquals(dsre1, dsre2);
		dsre1.setPosition(0L);
		dsre2.setPosition(0L);
		Assert.assertEquals(dsre1, dsre2);

		dsre2.setDashboardSetId("22");
		Assert.assertNotEquals(dsre1, dsre2);
		dsre2.setDashboardSetId("12");
		dsre2.setTenantId(2L);
		Assert.assertNotEquals(dsre1, dsre2);
		dsre2.setTenantId(1L);
		dsre2.setSubDashboardId("2");
		Assert.assertNotEquals(dsre1, dsre2);
		dsre2.setSubDashboardId("1");
		dsre2.setPosition(1L);
		Assert.assertNotEquals(dsre1, dsre2);
		dsre2.setPosition(0L);
		Assert.assertEquals(dsre1, dsre2);
	}

	@Test
	public void testHashCode()
	{
		DashboardSetRowEntity dsre1 = new DashboardSetRowEntity();
		DashboardSetRowEntity dsre2 = new DashboardSetRowEntity();
		Assert.assertEquals(dsre1.hashCode(), dsre2.hashCode());
		dsre1.setDashboardSetId("12");
		dsre2.setDashboardSetId("12");
		Assert.assertEquals(dsre1.hashCode(), dsre2.hashCode());
		dsre1.setTenantId(1L);
		dsre2.setTenantId(1L);
		Assert.assertEquals(dsre1.hashCode(), dsre2.hashCode());
		dsre1.setSubDashboardId("1");
		dsre2.setSubDashboardId("1");
		Assert.assertEquals(dsre1.hashCode(), dsre2.hashCode());
		dsre1.setPosition(0L);
		dsre2.setPosition(0L);
		Assert.assertEquals(dsre1.hashCode(), dsre2.hashCode());

		dsre2.setDashboardSetId("22");
		Assert.assertNotEquals(dsre1.hashCode(), dsre2.hashCode());
		dsre2.setDashboardSetId("12");
		dsre2.setTenantId(2L);
		Assert.assertNotEquals(dsre1.hashCode(), dsre2.hashCode());
		dsre2.setTenantId(1L);
		dsre2.setSubDashboardId("2");
		Assert.assertNotEquals(dsre1.hashCode(), dsre2.hashCode());
		dsre2.setSubDashboardId("1");
		dsre2.setPosition(1L);
		Assert.assertNotEquals(dsre1.hashCode(), dsre2.hashCode());
		dsre2.setPosition(0L);
		Assert.assertEquals(dsre1.hashCode(), dsre2.hashCode());
	}
}
