package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardTileParamsRowEntityTest
{
	@Test(groups = { "s1" })
	public void testEquals()
	{
		DashboardTileParamsRowEntity dtpre1 = new DashboardTileParamsRowEntity();
		DashboardTileParamsRowEntity dtpre2 = new DashboardTileParamsRowEntity();
		Assert.assertEquals(dtpre1, dtpre2);
		dtpre1.setTileId(12L);
		dtpre2.setTileId(12L);
		Assert.assertEquals(dtpre1, dtpre2);
		dtpre1.setParamName("param1");
		dtpre2.setParamName("param1");
		Assert.assertEquals(dtpre1, dtpre2);
		dtpre1.setTenantId(1L);
		dtpre2.setTenantId(1L);
		Assert.assertEquals(dtpre1, dtpre2);
		dtpre1.setIsSystem(1);
		dtpre2.setIsSystem(1);
		Assert.assertEquals(dtpre1, dtpre2);
		dtpre1.setParamType(1L);
		dtpre2.setParamType(1L);
		Assert.assertEquals(dtpre1, dtpre2);
		dtpre1.setParamValueStr("param1 value");
		dtpre2.setParamValueStr("param1 value");
		Assert.assertEquals(dtpre1, dtpre2);
		dtpre1.setParamValueNum(1L);
		dtpre2.setParamValueNum(1L);
		Assert.assertEquals(dtpre1, dtpre2);

		dtpre2.setTileId(22L);
		Assert.assertNotEquals(dtpre1, dtpre2);
		dtpre2.setTileId(12L);
		dtpre2.setParamName("param2");
		Assert.assertNotEquals(dtpre1, dtpre2);
		dtpre2.setParamName("param1");
		dtpre2.setTenantId(2L);
		Assert.assertNotEquals(dtpre1, dtpre2);
		dtpre2.setTenantId(1L);
		dtpre2.setIsSystem(0);
		Assert.assertNotEquals(dtpre1, dtpre2);
		dtpre2.setIsSystem(1);
		dtpre2.setParamType(0L);
		Assert.assertNotEquals(dtpre1, dtpre2);
		dtpre2.setParamType(1L);
		dtpre2.setParamValueStr("param1 value changed");
		Assert.assertNotEquals(dtpre1, dtpre2);
		dtpre2.setParamValueStr("param1 value");
		dtpre2.setParamValueNum(100L);
		Assert.assertNotEquals(dtpre1, dtpre2);
		dtpre2.setParamValueNum(1L);
		Assert.assertEquals(dtpre1, dtpre2);
	}

	@Test(groups = { "s1" })
	public void testHashCode()
	{
		DashboardTileParamsRowEntity dtpre1 = new DashboardTileParamsRowEntity();
		DashboardTileParamsRowEntity dtpre2 = new DashboardTileParamsRowEntity();
		Assert.assertEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre1.setTileId(12L);
		dtpre2.setTileId(12L);
		Assert.assertEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre1.setParamName("param1");
		dtpre2.setParamName("param1");
		Assert.assertEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre1.setTenantId(1L);
		dtpre2.setTenantId(1L);
		Assert.assertEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre1.setIsSystem(1);
		dtpre2.setIsSystem(1);
		Assert.assertEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre1.setParamType(1L);
		dtpre2.setParamType(1L);
		Assert.assertEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre1.setParamValueStr("param1 value");
		dtpre2.setParamValueStr("param1 value");
		Assert.assertEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre1.setParamValueNum(1L);
		dtpre2.setParamValueNum(1L);
		Assert.assertEquals(dtpre1.hashCode(), dtpre2.hashCode());

		dtpre2.setTileId(22L);
		Assert.assertNotEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre2.setTileId(12L);
		dtpre2.setParamName("param2");
		Assert.assertNotEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre2.setParamName("param1");
		dtpre2.setTenantId(2L);
		Assert.assertNotEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre2.setTenantId(1L);
		dtpre2.setIsSystem(0);
		Assert.assertNotEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre2.setIsSystem(1);
		dtpre2.setParamType(0L);
		Assert.assertNotEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre2.setParamType(1L);
		dtpre2.setParamValueStr("param1 value changed");
		Assert.assertNotEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre2.setParamValueStr("param1 value");
		dtpre2.setParamValueNum(100L);
		Assert.assertNotEquals(dtpre1.hashCode(), dtpre2.hashCode());
		dtpre2.setParamValueNum(1L);
		Assert.assertEquals(dtpre1.hashCode(), dtpre2.hashCode());
	}
}
