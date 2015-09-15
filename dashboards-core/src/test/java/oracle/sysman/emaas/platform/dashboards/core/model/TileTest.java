package oracle.sysman.emaas.platform.dashboards.core.model;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TileTest
{
	@Test
	public void testGetPersistenceEntity() throws DashboardException
	{
		Tile tile = new Tile();
		tile.setHeight(1);
		tile.setWidth(8);
		tile.setIsMaximized(false);
		tile.setLastModificationDate(DateUtil.getCurrentUTCTime());
		tile.setLastModifiedBy("SYSMAN");
		tile.setOwner("SYSMAN");
		tile.setRow(0);
		tile.setColumn(0);
		tile.setType(Tile.TILE_TYPE_DEFAULT);
		tile.setProviderAssetRoot("asserRoot");
		tile.setProviderName("provider");
		tile.setProviderVersion("providerVersion");
		//tile.setTenantId("tenantId");
		tile.setTitle("title");
		tile.setWidgetCreationTime("widgetCreationTime");
		tile.setWidgetDescription("widgetDescription");
		tile.setWidgetGroupName("widgetGroupName");
		tile.setWidgetIcon("widgetIcon");
		tile.setWidgetKocName("widgetKocName");
		tile.setWidgetName("widgetName");
		tile.setWidgetOwner("widgetOwner");
		tile.setWidgetSource(1);
		tile.setWidgetTemplate("widgetTemplate");
		tile.setWidgetUniqueId("widgetUniqueId");
		tile.setWidgetViewmode("widgetViewmode");
		tile.setWidgetHistogram("widgetHistogram");
		tile.setContent("Test content for text");
		TileParam tp = new TileParam();
		tp.setIsSystem(false);
		tp.setName("Test parameter");
		tp.setStringValue("Test parameter value");
		tile.addParameter(tp);

		EmsDashboardTile edt = tile.getPersistenceEntity(null);
		Assert.assertEquals(Integer.valueOf(0), edt.getType());
		Assert.assertEquals(Integer.valueOf(0), edt.getIsMaximized());
		Assert.assertEquals(Integer.valueOf(8), edt.getWidth());
		Assert.assertEquals(Integer.valueOf(1), edt.getHeight());
		Assert.assertEquals(Integer.valueOf(0), edt.getRow());
		Assert.assertEquals(Integer.valueOf(0), edt.getColumn());

		Assert.assertEquals(1, edt.getDashboardTileParamsList().size());
		EmsDashboardTileParams edtp = edt.getDashboardTileParamsList().get(0);
		Assert.assertEquals("Test parameter", edtp.getParamName());
		Assert.assertEquals("Test parameter value", edtp.getParamValueStr());
		Assert.assertEquals(Integer.valueOf(0), edtp.getIsSystem());

		tile.setType(Tile.TILE_TYPE_TEXT_WIDGET);
		edt = tile.getPersistenceEntity(null);
		Assert.assertEquals(2, edt.getDashboardTileParamsList().size());
		edtp = edt.getDashboardTileParamsList().get(0);
		Assert.assertEquals("Test parameter", edtp.getParamName());
		Assert.assertEquals("Test parameter value", edtp.getParamValueStr());
		Assert.assertEquals(Integer.valueOf(0), edtp.getIsSystem());
		edtp = edt.getDashboardTileParamsList().get(1);
		Assert.assertEquals(Tile.TEXT_WIDGET_PARAM_NAME_CONTENT, edtp.getParamName());
		Assert.assertEquals("Test content for text", edtp.getParamValueStr());
		Assert.assertEquals(Integer.valueOf(1), edtp.getIsSystem());

		edt = tile.getPersistenceEntity(edt);
		Assert.assertEquals(2, edt.getDashboardTileParamsList().size());
		edtp = edt.getDashboardTileParamsList().get(0);
		Assert.assertEquals("Test parameter", edtp.getParamName());
		Assert.assertEquals("Test parameter value", edtp.getParamValueStr());
		Assert.assertEquals(Integer.valueOf(0), edtp.getIsSystem());
		edtp = edt.getDashboardTileParamsList().get(1);
		Assert.assertEquals(Tile.TEXT_WIDGET_PARAM_NAME_CONTENT, edtp.getParamName());
		Assert.assertEquals("Test content for text", edtp.getParamValueStr());
		Assert.assertEquals(Integer.valueOf(1), edtp.getIsSystem());
	}

	@Test
	public void testValueOf()
	{

		EmsDashboardTile edt = new EmsDashboardTile();
		edt.setCreationDate(DateUtil.getCurrentUTCTime());
		edt.setHeight(1);
		edt.setWidth(8);
		edt.setIsMaximized(0);
		edt.setLastModificationDate(DateUtil.getCurrentUTCTime());
		edt.setLastModifiedBy("SYSMAN");
		edt.setOwner("SYSMAN");
		edt.setRow(0);
		edt.setColumn(0);
		edt.setType(0);
		edt.setPosition(0);
		edt.setProviderAssetRoot("asserRoot");
		edt.setProviderName("provider");
		edt.setProviderVersion("providerVersion");
		//tile.setTenantId("tenantId");
		edt.setTitle("title");
		edt.setWidgetCreationTime("widgetCreationTime");
		edt.setWidgetDescription("widgetDescription");
		edt.setWidgetGroupName("widgetGroupName");
		edt.setWidgetIcon("widgetIcon");
		edt.setWidgetKocName("widgetKocName");
		edt.setWidgetName("widgetName");
		edt.setWidgetOwner("widgetOwner");
		edt.setWidgetSource(1);
		edt.setWidgetTemplate("widgetTemplate");
		edt.setWidgetUniqueId("widgetUniqueId");
		edt.setWidgetViewmode("widgetViewmode");
		edt.setWidgetHistogram("widgetHistogram");
		EmsDashboardTileParams edtp = new EmsDashboardTileParams();
		edtp.setDashboardTile(edt);
		edtp.setIsSystem(1);
		edtp.setParamName(Tile.TEXT_WIDGET_PARAM_NAME_CONTENT);
		edtp.setParamValueStr("Test content for text");
		edt.addEmsDashboardTileParams(edtp);
		edtp = new EmsDashboardTileParams();
		edtp.setDashboardTile(edt);
		edtp.setIsSystem(0);
		edtp.setParamName("Test parameter");
		edtp.setParamValueStr("Test parameter value");
		edt.addEmsDashboardTileParams(edtp);

		Tile tile = Tile.valueOf(edt);
		Assert.assertEquals(Tile.TILE_TYPE_DEFAULT, tile.getType());
		Assert.assertFalse(tile.getIsMaximized());
		Assert.assertEquals(Integer.valueOf(8), tile.getWidth());
		Assert.assertEquals(Integer.valueOf(1), tile.getHeight());
		Assert.assertEquals(Integer.valueOf(0), tile.getRow());
		Assert.assertEquals(Integer.valueOf(0), tile.getColumn());
		Assert.assertEquals(Tile.TILE_TYPE_DEFAULT, tile.getType());
		Assert.assertEquals(2, tile.getParameters().size());
		TileParam tp = tile.getParameters().get(0);
		Assert.assertTrue(tp.getIsSystem());
		Assert.assertEquals(Tile.TEXT_WIDGET_PARAM_NAME_CONTENT, tp.getName());
		Assert.assertEquals("Test content for text", tp.getStringValue());
		tp = tile.getParameters().get(1);
		Assert.assertFalse(tp.getIsSystem());
		Assert.assertEquals("Test parameter", tp.getName());
		Assert.assertEquals("Test parameter value", tp.getStringValue());

		edt.setType(1);
		tile = Tile.valueOf(edt);
		Assert.assertEquals("Test content for text", tile.getContent());
		Assert.assertEquals(1, tile.getParameters().size());
		tp = tile.getParameters().get(0);
		Assert.assertFalse(tp.getIsSystem());
		Assert.assertEquals("Test parameter", tp.getName());
		Assert.assertEquals("Test parameter value", tp.getStringValue());
	}
}
