package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PreferenceRowEntityTest
{
	@Test(groups = { "s1" })
	public void testEquals()
	{
		PreferenceRowEntity pre1 = new PreferenceRowEntity();
		PreferenceRowEntity pre2 = new PreferenceRowEntity();
		Assert.assertEquals(pre1, pre2);
		pre1.setUserName("emcsadmin");
		pre2.setUserName("emcsadmin");
		Assert.assertEquals(pre1, pre2);
		pre1.setPrefKey("prefKey");
		pre2.setPrefKey("prefKey");
		Assert.assertEquals(pre1, pre2);
		pre1.setPrefValue("prefValue");
		pre2.setPrefValue("prefValue");
		Assert.assertEquals(pre1, pre2);
		pre1.setTenantId(1L);
		pre2.setTenantId(1L);
		Assert.assertEquals(pre1, pre2);

		pre2.setUserName("emcsadmin2");
		Assert.assertNotEquals(pre1, pre2);
		pre2.setUserName("emcsadmin");
		pre2.setPrefKey("prefKey2");
		Assert.assertNotEquals(pre1, pre2);
		pre2.setPrefKey("prefKey");
		pre2.setPrefValue("prefValue2");
		Assert.assertNotEquals(pre1, pre2);
		pre2.setPrefValue("prefValue");
		pre2.setTenantId(2L);
		Assert.assertNotEquals(pre1, pre2);
		pre2.setTenantId(1L);
		Assert.assertEquals(pre1, pre2);
	}

	@Test(groups = { "s1" })
	public void testHashCode()
	{
		PreferenceRowEntity pre1 = new PreferenceRowEntity();
		PreferenceRowEntity pre2 = new PreferenceRowEntity();
		Assert.assertEquals(pre1.hashCode(), pre2.hashCode());
		pre1.setUserName("emcsadmin");
		pre2.setUserName("emcsadmin");
		Assert.assertEquals(pre1.hashCode(), pre2.hashCode());
		pre1.setPrefKey("prefKey");
		pre2.setPrefKey("prefKey");
		Assert.assertEquals(pre1.hashCode(), pre2.hashCode());
		pre1.setPrefValue("prefValue");
		pre2.setPrefValue("prefValue");
		Assert.assertEquals(pre1.hashCode(), pre2.hashCode());
		pre1.setTenantId(1L);
		pre2.setTenantId(1L);
		Assert.assertEquals(pre1.hashCode(), pre2.hashCode());

		pre2.setUserName("emcsadmin2");
		Assert.assertNotEquals(pre1.hashCode(), pre2.hashCode());
		pre2.setUserName("emcsadmin");
		pre2.setPrefKey("prefKey2");
		Assert.assertNotEquals(pre1.hashCode(), pre2.hashCode());
		pre2.setPrefKey("prefKey");
		pre2.setPrefValue("prefValue2");
		Assert.assertNotEquals(pre1.hashCode(), pre2.hashCode());
		pre2.setPrefValue("prefValue");
		pre2.setTenantId(2L);
		Assert.assertNotEquals(pre1.hashCode(), pre2.hashCode());
		pre2.setTenantId(1L);
		Assert.assertEquals(pre1.hashCode(), pre2.hashCode());
	}
}
