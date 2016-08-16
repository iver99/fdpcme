package oracle.sysman.emaas.platform.dashboards.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups = { "s1" })
public class EmsDashboardTileTest
{
	Date now = new Date();
	private final EmsDashboard emsDashboard = new EmsDashboard(now, BigInteger.valueOf(10L), BigInteger.valueOf(10L), "elephant",
			10, 10, 10, 10, 10, 10, now, "elephant", "elephant", "elephant", "elephant", 10, 10, "elephant");

	private final EmsDashboardTile emsDashboardTile = new EmsDashboardTile(now, emsDashboard, 10, 10, 10, 10, 10, now,
			"elephant", "elephant", /*Integer position, */
			"elephant", "elephant", "dolphine", BigInteger.valueOf(10L), "dolphine", "dolphine", "dolphine", "kitten", "kitten",
			"kitten", "kitten", "kitten", "lion", 10, "lion", "lion", "lion", 10, 10, BigInteger.valueOf(10L));

	private final EmsDashboardTileParams emsDashboardTileParams = new EmsDashboardTileParams(10, "elephant", 10, 10, "dolphine",
			now, emsDashboardTile);

	@Test
	public void testAddEmsDashboardTileParams() throws Exception
	{
		Assert.assertEquals(emsDashboardTile.addEmsDashboardTileParams(emsDashboardTileParams), emsDashboardTileParams);
	}

	@Test
	public void testGetColumn() throws Exception
	{
		EmsDashboardTile emsDashboardTile1 = new EmsDashboardTile();
		emsDashboardTile1.setColumn(10);
		Assert.assertEquals(emsDashboardTile1.getColumn(), new Integer(10));

	}

	@Test
	public void testGetCreationDate() throws Exception
	{
		emsDashboardTile.setCreationDate(now);
		Assert.assertEquals(emsDashboardTile.getCreationDate(), now);
	}

	@Test
	public void testGetDashboard() throws Exception
	{
		emsDashboardTile.setDashboard(emsDashboard);
		Assert.assertEquals(emsDashboardTile.getDashboard(), emsDashboard);
	}

	@Test
	public void testGetDashboardTileParamsList() throws Exception
	{

		List<EmsDashboardTileParams> list = new ArrayList<EmsDashboardTileParams>();
		emsDashboardTile.setDashboardTileParamsList(list);
		Assert.assertEquals(emsDashboardTile.getDashboardTileParamsList(), list);

	}

	@Test
	public void testGetHeight() throws Exception
	{
		emsDashboardTile.setHeight(10);
		Assert.assertEquals(emsDashboardTile.getHeight(), new Integer(10));
	}

	@Test
	public void testGetIsMaximized() throws Exception
	{
		emsDashboardTile.setIsMaximized(10);
		Assert.assertEquals(emsDashboardTile.getIsMaximized(), new Integer(10));
	}

	@Test
	public void testGetLastModificationDate() throws Exception
	{
		emsDashboardTile.setLastModificationDate(now);
		Assert.assertEquals(emsDashboardTile.getLastModificationDate(), now);
	}

	@Test
	public void testGetLastModifiedBy() throws Exception
	{
		emsDashboardTile.setLastModifiedBy("elephant");
		Assert.assertEquals(emsDashboardTile.getLastModifiedBy(), "elephant");
	}

	@Test
	public void testGetOwner() throws Exception
	{
		emsDashboardTile.setOwner("elephant");
		Assert.assertEquals(emsDashboardTile.getOwner(), "elephant");
	}

	@Test
	public void testGetPosition() throws Exception
	{
		emsDashboardTile.setPosition(10);
		Assert.assertEquals(emsDashboardTile.getPosition(), new Integer(0));

	}

	@Test
	public void testGetProviderAssetRoot() throws Exception
	{
		emsDashboardTile.setProviderAssetRoot("elephant");
		Assert.assertEquals(emsDashboardTile.getProviderAssetRoot(), "elephant");
	}

	@Test
	public void testGetProviderName() throws Exception
	{
		emsDashboardTile.setProviderName("elephant");
		Assert.assertEquals(emsDashboardTile.getProviderName(), "elephant");
	}

	@Test
	public void testGetProviderVersion() throws Exception
	{
		emsDashboardTile.setProviderVersion("elephant");
		Assert.assertEquals(emsDashboardTile.getProviderVersion(), "elephant");

	}

	@Test
	public void testGetRow() throws Exception
	{
		emsDashboardTile.setRow(10);
		Assert.assertEquals(emsDashboardTile.getRow(), new Integer(10));
	}

	@Test
	public void testGetTileId() throws Exception
	{
		Assert.assertEquals(emsDashboardTile.getTileId(), BigInteger.valueOf(10L));

	}

	@Test
	public void testGetTitle() throws Exception
	{
		emsDashboardTile.setTitle("elephant");
		Assert.assertEquals(emsDashboardTile.getTitle(), "elephant");

	}

	@Test
	public void testGetType() throws Exception
	{
		emsDashboardTile.setType(10);
		Assert.assertEquals(emsDashboardTile.getType(), new Integer(10));

	}

	@Test
	public void testGetWidgetCreationTime() throws Exception
	{
		emsDashboardTile.setWidgetCreationTime("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetCreationTime(), "elephant");
	}

	@Test
	public void testGetWidgetDescription() throws Exception
	{
		emsDashboardTile.setWidgetDescription("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetDescription(), "elephant");

	}

	@Test
	public void testGetWidgetGroupName() throws Exception
	{
		emsDashboardTile.setWidgetGroupName("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetGroupName(), "elephant");

	}

	@Test
	public void testGetWidgetHistogram() throws Exception
	{
		emsDashboardTile.setWidgetHistogram("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetHistogram(), "elephant");
	}

	@Test
	public void testGetWidgetIcon() throws Exception
	{
		emsDashboardTile.setWidgetIcon("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetIcon(), "elephant");
	}

	@Test
	public void testGetWidgetKocName() throws Exception
	{
		emsDashboardTile.setWidgetKocName("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetKocName(), "elephant");
	}

	@Test
	public void testGetWidgetLinkedDashboard() throws Exception
	{
		emsDashboardTile.setWidgetLinkedDashboard(BigInteger.valueOf(10L));
		new Long(10);
		Assert.assertEquals(emsDashboardTile.getWidgetLinkedDashboard(), BigInteger.valueOf(10L));

	}

	@Test
	public void testGetWidgetName() throws Exception
	{
		emsDashboardTile.setWidgetName("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetName(), "elephant");

	}

	@Test
	public void testGetWidgetOwner() throws Exception
	{
		emsDashboardTile.setWidgetOwner("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetOwner(), "elephant");
	}

	@Test
	public void testGetWidgetSource() throws Exception
	{
		emsDashboardTile.setWidgetSource(10);
		Assert.assertEquals(emsDashboardTile.getWidgetSource(), new Integer(10));

	}

	@Test
	public void testGetWidgetSupportTimeControl() throws Exception
	{
		emsDashboardTile.setWidgetSupportTimeControl(10);
		Assert.assertEquals(emsDashboardTile.getWidgetSupportTimeControl(), new Integer(10));

	}

	@Test
	public void testGetWidgetTemplate() throws Exception
	{
		emsDashboardTile.setWidgetTemplate("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetTemplate(), "elephant");
	}

	@Test
	public void testGetWidgetUniqueId() throws Exception
	{
		emsDashboardTile.setWidgetUniqueId("123456");
		Assert.assertEquals(emsDashboardTile.getWidgetUniqueId(), "123456");

	}

	@Test
	public void testGetWidgetViewmode() throws Exception
	{
		emsDashboardTile.setWidgetViewmode("elephant");
		Assert.assertEquals(emsDashboardTile.getWidgetViewmode(), "elephant");
	}

	@Test
	public void testGetWidth() throws Exception
	{
		emsDashboardTile.setWidth(10);
		Assert.assertEquals(emsDashboardTile.getWidth(), new Integer(10));
	}

	@Test
	public void testRemoveEmsDashboardTileParams() throws Exception
	{
		List<EmsDashboardTileParams> list = new ArrayList<EmsDashboardTileParams>();
		list.add(emsDashboardTileParams);
		emsDashboardTile.setDashboardTileParamsList(list);
		emsDashboardTile.removeEmsDashboardTileParams(emsDashboardTileParams);
	}

	@Test
	public void testSetColumn() throws Exception
	{

	}

	@Test
	public void testSetCreationDate() throws Exception
	{

	}

	@Test
	public void testSetDashboard() throws Exception
	{

	}

	@Test
	public void testSetDashboardTileParamsList() throws Exception
	{

	}

	@Test
	public void testSetHeight() throws Exception
	{

	}

	@Test
	public void testSetIsMaximized() throws Exception
	{

	}

	@Test
	public void testSetLastModificationDate() throws Exception
	{

	}

	@Test
	public void testSetLastModifiedBy() throws Exception
	{

	}

	@Test
	public void testSetOwner() throws Exception
	{

	}

	@Test
	public void testSetPosition() throws Exception
	{

	}

	@Test
	public void testSetProviderAssetRoot() throws Exception
	{

	}

	@Test
	public void testSetProviderName() throws Exception
	{

	}

	@Test
	public void testSetProviderVersion() throws Exception
	{

	}

	@Test
	public void testSetRow() throws Exception
	{

	}

	@Test
	public void testSetTitle() throws Exception
	{

	}

	@Test
	public void testSetType() throws Exception
	{

	}

	@Test
	public void testSetWidgetCreationTime() throws Exception
	{

	}

	@Test
	public void testSetWidgetDescription() throws Exception
	{

	}

	@Test
	public void testSetWidgetGroupName() throws Exception
	{

	}

	@Test
	public void testSetWidgetHistogram() throws Exception
	{

	}

	@Test
	public void testSetWidgetIcon() throws Exception
	{

	}

	@Test
	public void testSetWidgetKocName() throws Exception
	{

	}

	@Test
	public void testSetWidgetLinkedDashboard() throws Exception
	{

	}

	@Test
	public void testSetWidgetName() throws Exception
	{

	}

	@Test
	public void testSetWidgetOwner() throws Exception
	{

	}

	@Test
	public void testSetWidgetSource() throws Exception
	{

	}

	@Test
	public void testSetWidgetSupportTimeControl() throws Exception
	{

	}

	@Test
	public void testSetWidgetTemplate() throws Exception
	{

	}

	@Test
	public void testSetWidgetUniqueId() throws Exception
	{

	}

	@Test
	public void testSetWidgetViewmode() throws Exception
	{

	}

	@Test
	public void testSetWidth() throws Exception
	{

	}
}
