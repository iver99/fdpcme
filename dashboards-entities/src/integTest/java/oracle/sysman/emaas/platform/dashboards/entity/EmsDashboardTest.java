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
public class EmsDashboardTest
{
	Date now = new Date();
	private EmsDashboard emsDashboard = new EmsDashboard(now, BigInteger.valueOf(10L), BigInteger.valueOf(10L), "elephant", 10,
			10, 10, 10, 10, 10, now, "elephant", "elephant", "elephant", "elephant", 10, 10, "elephant");

	private final EmsDashboardTile emsDashboardTile = new EmsDashboardTile(now, emsDashboard, 10, 10, 10, 10, 10, now, "elephant",
			"elephant", /*Integer position, */
			"elephant", "elephant", "dolphine", BigInteger.valueOf(10L), "dolphine", "dolphine", "dolphine", "kitten", "kitten",
			"kitten", "kitten", "kitten", "lion", 10, "lion", "lion", "lion", 10, 10, BigInteger.valueOf(10L));

	@Test
	public void testAddEmsDashboardTile() throws Exception
	{
		emsDashboard = new EmsDashboard();
		EmsDashboardTile emsDashboardTile = new EmsDashboardTile();
		emsDashboard.addEmsDashboardTile(emsDashboardTile);
		Assert.assertEquals(emsDashboard.getDashboardTileList().get(0), emsDashboardTile);
	}

	@Test
	public void testGetApplicationType() throws Exception
	{
		emsDashboard.setApplicationType(10);
		Assert.assertEquals(emsDashboard.getApplicationType(), new Integer(10));

	}

	@Test
	public void testGetCreationDate() throws Exception
	{
		emsDashboard.setCreationDate(now);
		Assert.assertEquals(emsDashboard.getCreationDate(), now);
	}

	@Test
	public void testGetDashboardId() throws Exception
	{
		emsDashboard.setDashboardId(BigInteger.valueOf(10L));
		Assert.assertEquals(emsDashboard.getDashboardId(), BigInteger.valueOf(10L));
	}

	@Test
	public void testGetDashboardTileList() throws Exception
	{

		List<EmsDashboardTile> list = new ArrayList<EmsDashboardTile>();
		list.add(emsDashboardTile);
		emsDashboard.setDashboardTileList(list);
		Assert.assertEquals(emsDashboard.getDashboardTileList(), list);

	}

	@Test
	public void testGetDeleted() throws Exception
	{
		emsDashboard.setDeleted(BigInteger.valueOf(19L));
		Assert.assertEquals(emsDashboard.getDeleted().longValue(), 19L);

	}

	@Test
	public void testGetDescription() throws Exception
	{
		emsDashboard.setDescription("elephant");
		Assert.assertEquals(emsDashboard.getDescription(), "elephant");
	}

	@Test
	public void testGetEnableDescription() throws Exception
	{
		emsDashboard.setEnableDescription(10);
		Assert.assertEquals(emsDashboard.getEnableDescription(), new Integer(10));
	}

	@Test
	public void testGetEnableEntityFilter() throws Exception
	{
		emsDashboard.setEnableEntityFilter(10);
		Assert.assertEquals(emsDashboard.getEnableEntityFilter(), new Integer(10));
	}

	@Test
	public void testGetEnableRefresh() throws Exception
	{
		emsDashboard.setEnableRefresh(10);
		Assert.assertEquals(emsDashboard.getEnableRefresh(), new Integer(10));
	}

	@Test
	public void testGetEnableTimeRange() throws Exception
	{
		emsDashboard.setEnableTimeRange(10);
		Assert.assertEquals(emsDashboard.getEnableTimeRange(), new Integer(10));
	}

	@Test
	public void testGetExtendedOptions() throws Exception
	{
		emsDashboard.setExtendedOptions("elephant");
		Assert.assertEquals(emsDashboard.getExtendedOptions(), "elephant");
	}

	@Test
	public void testGetIsSystem() throws Exception
	{
		emsDashboard.setIsSystem(10);
		Assert.assertEquals(emsDashboard.getIsSystem(), new Integer(10));

	}

	@Test
	public void testGetLastModificationDate() throws Exception
	{
		emsDashboard.setLastModificationDate(now);
		Assert.assertEquals(emsDashboard.getLastModificationDate(), now);
	}

	@Test
	public void testGetLastModifiedBy() throws Exception
	{
		emsDashboard.setLastModifiedBy("elephant");
		Assert.assertEquals(emsDashboard.getLastModifiedBy(), "elephant");
	}

	@Test
	public void testGetName() throws Exception
	{
		emsDashboard.setName("elephant");
		Assert.assertEquals(emsDashboard.getName(), "elephant");
	}

	@Test
	public void testGetOwner() throws Exception
	{
		emsDashboard.setOwner("elephant");
		Assert.assertEquals(emsDashboard.getOwner(), "elephant");
	}

	@Test
	public void testGetScreenShot() throws Exception
	{
		emsDashboard.setScreenShot("elephant");
		Assert.assertEquals(emsDashboard.getScreenShot(), "elephant");

	}

	@Test
	public void testGetSharePublic() throws Exception
	{
		emsDashboard.setSharePublic(10);
		Assert.assertEquals(emsDashboard.getSharePublic(), new Integer(10));

	}

	@Test
	public void testGetTenantId() throws Exception
	{
		emsDashboard.setTenantId(10L);
		Assert.assertEquals(emsDashboard.getTenantId(), new Long(10));

	}

	@Test
	public void testGetType() throws Exception
	{
		emsDashboard.setType(10);
		Assert.assertEquals(emsDashboard.getType(), new Integer(10));
	}

	@Test
	public void testRemoveEmsDashboardTile() throws Exception
	{
		List<EmsDashboardTile> list = new ArrayList<EmsDashboardTile>();
		list.add(emsDashboardTile);
		emsDashboard.setDashboardTileList(list);
		Assert.assertEquals(emsDashboard.removeEmsDashboardTile(emsDashboardTile), emsDashboardTile);
	}

	@Test
	public void testSetApplicationType() throws Exception
	{

	}

	@Test
	public void testSetCreationDate() throws Exception
	{

	}

	@Test
	public void testSetDashboardId() throws Exception
	{

	}

	@Test
	public void testSetDashboardTileList() throws Exception
	{

	}

	@Test
	public void testSetDeleted() throws Exception
	{

	}

	@Test
	public void testSetDescription() throws Exception
	{

	}

	@Test
	public void testSetEnableDescription() throws Exception
	{

	}

	@Test
	public void testSetEnableEntityFilter() throws Exception
	{

	}

	@Test
	public void testSetEnableRefresh() throws Exception
	{

	}

	@Test
	public void testSetEnableTimeRange() throws Exception
	{

	}

	@Test
	public void testSetExtendedOptions() throws Exception
	{

	}

	@Test
	public void testSetIsSystem() throws Exception
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
	public void testSetName() throws Exception
	{

	}

	@Test
	public void testSetOwner() throws Exception
	{

	}

	@Test
	public void testSetScreenShot() throws Exception
	{

	}

	@Test
	public void testSetSharePublic() throws Exception
	{

	}

	@Test
	public void testSetTenantId() throws Exception
	{

	}

	@Test
	public void testSetType() throws Exception
	{

	}
}

