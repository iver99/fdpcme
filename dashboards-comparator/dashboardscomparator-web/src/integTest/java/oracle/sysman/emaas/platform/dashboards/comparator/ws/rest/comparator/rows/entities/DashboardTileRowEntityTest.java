package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities;

import org.testng.Assert;
import org.testng.annotations.Test;
@Test(groups = { "s1" })
public class DashboardTileRowEntityTest
{
	@Test
	public void testEquals()
	{
		DashboardTileRowEntity dtre1 = new DashboardTileRowEntity();
		DashboardTileRowEntity dtre2 = new DashboardTileRowEntity();
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setDashboardId(12L);
		dtre2.setDashboardId(12L);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setLastModifiedBy("emcsadmin");
		dtre2.setLastModifiedBy("emcsadmin");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setOwner("emcsadmin");
		dtre2.setOwner("emcsadmin");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setTitle("tile1");
		dtre2.setTitle("tile1");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setHeight(2L);
		dtre2.setHeight(2L);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidth(2L);
		dtre2.setWidth(2L);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setIsMaximized(1);
		dtre2.setIsMaximized(1);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setPosition(0L);
		dtre2.setPosition(0L);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setTenantId(1L);
		dtre2.setTenantId(1L);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetUniqueId("1");
		dtre2.setWidgetUniqueId("1");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetName("widget1");
		dtre2.setWidgetName("widget1");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetDescription("widget1");
		dtre2.setWidgetDescription("widget1");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetGroupName("widget group1");
		dtre2.setWidgetGroupName("widget group1");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetIcon("icon");
		dtre2.setWidgetIcon("icon");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetHistogram("Histogram");
		dtre2.setWidgetHistogram("Histogram");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetOwner("emcsadmin");
		dtre2.setWidgetOwner("emcsadmin");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetSource(1L);
		dtre2.setWidgetSource(1L);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetKocName("KocName");
		dtre2.setWidgetKocName("KocName");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetViewmode("Viewmode");
		dtre2.setWidgetViewmode("Viewmode");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetTemplate("Template");
		dtre2.setWidgetTemplate("Template");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setProviderName("providerName");
		dtre2.setProviderName("providerName");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setProviderVersion("providerVersion");
		dtre2.setProviderVersion("providerVersion");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setProviderAssetRoot("providerAssetRoot");
		dtre2.setProviderAssetRoot("providerAssetRoot");
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setTileRow(1L);
		dtre2.setTileRow(1L);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setTileColumn(1L);
		dtre2.setTileColumn(1L);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setType(1L);
		dtre2.setType(1L);
		Assert.assertEquals(dtre1, dtre2);
		dtre1.setWidgetSupportTimeControl(1);
		dtre2.setWidgetSupportTimeControl(1);
		Assert.assertEquals(dtre1, dtre2);

		dtre2.setDashboardId(22L);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setDashboardId(12L);
		dtre2.setLastModifiedBy("emcsadmin1");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setLastModifiedBy("emcsadmin");
		dtre2.setOwner("emcsadmin1");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setOwner("emcsadmin");
		dtre2.setTitle("tile2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setTitle("tile1");
		dtre2.setHeight(22L);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setHeight(2L);
		dtre2.setWidth(12L);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidth(2L);
		dtre2.setIsMaximized(0);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setIsMaximized(1);
		dtre2.setPosition(1L);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setPosition(0L);
		dtre2.setTenantId(2L);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setTenantId(1L);
		dtre2.setWidgetUniqueId("2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetUniqueId("1");
		dtre2.setWidgetName("widget2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetName("widget1");
		dtre2.setWidgetDescription("widget2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetDescription("widget1");
		dtre2.setWidgetGroupName("widget group2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetGroupName("widget group1");
		dtre2.setWidgetIcon("icon2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetIcon("icon");
		dtre2.setWidgetHistogram("Histogram2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetHistogram("Histogram");
		dtre2.setWidgetOwner("emcsadmin2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetOwner("emcsadmin");
		dtre2.setWidgetSource(2L);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetSource(1L);
		dtre2.setWidgetKocName("KocName2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetKocName("KocName");
		dtre2.setWidgetViewmode("Viewmode2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetViewmode("Viewmode");
		dtre2.setWidgetTemplate("Template2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetTemplate("Template");
		dtre2.setProviderName("providerName2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setProviderName("providerName");
		dtre2.setProviderVersion("providerVersion2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setProviderVersion("providerVersion");
		dtre2.setProviderAssetRoot("providerAssetRoot2");
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setProviderAssetRoot("providerAssetRoot");
		dtre2.setTileRow(2L);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setTileRow(1L);
		dtre2.setTileColumn(2L);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setTileColumn(1L);
		dtre2.setType(2L);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setType(1L);
		dtre2.setWidgetSupportTimeControl(2);
		Assert.assertNotEquals(dtre1, dtre2);
		dtre2.setWidgetSupportTimeControl(1);
		Assert.assertEquals(dtre1, dtre2);
	}

	@Test
	public void testHashCode()
	{
		DashboardTileRowEntity dtre1 = new DashboardTileRowEntity();
		DashboardTileRowEntity dtre2 = new DashboardTileRowEntity();
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setDashboardId(12L);
		dtre2.setDashboardId(12L);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setLastModifiedBy("emcsadmin");
		dtre2.setLastModifiedBy("emcsadmin");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setOwner("emcsadmin");
		dtre2.setOwner("emcsadmin");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setTitle("tile1");
		dtre2.setTitle("tile1");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setHeight(2L);
		dtre2.setHeight(2L);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidth(2L);
		dtre2.setWidth(2L);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setIsMaximized(1);
		dtre2.setIsMaximized(1);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setPosition(0L);
		dtre2.setPosition(0L);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setTenantId(1L);
		dtre2.setTenantId(1L);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetUniqueId("1");
		dtre2.setWidgetUniqueId("1");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetName("widget1");
		dtre2.setWidgetName("widget1");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetDescription("widget1");
		dtre2.setWidgetDescription("widget1");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetGroupName("widget group1");
		dtre2.setWidgetGroupName("widget group1");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetIcon("icon");
		dtre2.setWidgetIcon("icon");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetHistogram("Histogram");
		dtre2.setWidgetHistogram("Histogram");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetOwner("emcsadmin");
		dtre2.setWidgetOwner("emcsadmin");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetSource(1L);
		dtre2.setWidgetSource(1L);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetKocName("KocName");
		dtre2.setWidgetKocName("KocName");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetViewmode("Viewmode");
		dtre2.setWidgetViewmode("Viewmode");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetTemplate("Template");
		dtre2.setWidgetTemplate("Template");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setProviderName("providerName");
		dtre2.setProviderName("providerName");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setProviderVersion("providerVersion");
		dtre2.setProviderVersion("providerVersion");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setProviderAssetRoot("providerAssetRoot");
		dtre2.setProviderAssetRoot("providerAssetRoot");
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setTileRow(1L);
		dtre2.setTileRow(1L);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setTileColumn(1L);
		dtre2.setTileColumn(1L);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setType(1L);
		dtre2.setType(1L);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre1.setWidgetSupportTimeControl(1);
		dtre2.setWidgetSupportTimeControl(1);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());

		dtre2.setDashboardId(22L);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setDashboardId(12L);
		dtre2.setLastModifiedBy("emcsadmin1");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setLastModifiedBy("emcsadmin");
		dtre2.setOwner("emcsadmin1");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setOwner("emcsadmin");
		dtre2.setTitle("tile2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setTitle("tile1");
		dtre2.setHeight(22L);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setHeight(2L);
		dtre2.setWidth(12L);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidth(2L);
		dtre2.setIsMaximized(0);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setIsMaximized(1);
		dtre2.setPosition(1L);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setPosition(0L);
		dtre2.setTenantId(2L);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setTenantId(1L);
		dtre2.setWidgetUniqueId("2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetUniqueId("1");
		dtre2.setWidgetName("widget2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetName("widget1");
		dtre2.setWidgetDescription("widget2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetDescription("widget1");
		dtre2.setWidgetGroupName("widget group2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetGroupName("widget group1");
		dtre2.setWidgetIcon("icon2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetIcon("icon");
		dtre2.setWidgetHistogram("Histogram2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetHistogram("Histogram");
		dtre2.setWidgetOwner("emcsadmin2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetOwner("emcsadmin");
		dtre2.setWidgetSource(2L);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetSource(1L);
		dtre2.setWidgetKocName("KocName2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetKocName("KocName");
		dtre2.setWidgetViewmode("Viewmode2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetViewmode("Viewmode");
		dtre2.setWidgetTemplate("Template2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetTemplate("Template");
		dtre2.setProviderName("providerName2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setProviderName("providerName");
		dtre2.setProviderVersion("providerVersion2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setProviderVersion("providerVersion");
		dtre2.setProviderAssetRoot("providerAssetRoot2");
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setProviderAssetRoot("providerAssetRoot");
		dtre2.setTileRow(2L);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setTileRow(1L);
		dtre2.setTileColumn(2L);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setTileColumn(1L);
		dtre2.setType(2L);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setType(1L);
		dtre2.setWidgetSupportTimeControl(2);
		Assert.assertNotEquals(dtre1.hashCode(), dtre2.hashCode());
		dtre2.setWidgetSupportTimeControl(1);
		Assert.assertEquals(dtre1.hashCode(), dtre2.hashCode());
	}
}
