package oracle.sysman.emaas.platform.dashboards.entity;

import java.math.BigInteger;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups = { "s1" })
public class EmsDashboardTileParamsPKTest
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
	private EmsDashboardTileParamsPK emsDashboardTileParamsPK = new EmsDashboardTileParamsPK("elephant", BigInteger.valueOf(10L));

	@Test
	public void testEquals() throws Exception
	{
		EmsDashboardTileParamsPK emsDashboardTileParamsPK1 = new EmsDashboardTileParamsPK("elephant", BigInteger.valueOf(10L));
		EmsDashboardTileParamsPK emsDashboardTileParamsPK2 = new EmsDashboardTileParamsPK("elephant", BigInteger.valueOf(11L));
		EmsDashboardTileParamsPK emsDashboardTileParamsPK3 = new EmsDashboardTileParamsPK("dolphine", BigInteger.valueOf(11L));
		EmsDashboardTileParamsPK emsDashboardTileParamsPK4 = new EmsDashboardTileParamsPK("dolphine", BigInteger.valueOf(10L));
		Assert.assertFalse(emsDashboardTileParamsPK.equals(new Integer(10)));
		Assert.assertTrue(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK1));
		Assert.assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK2));
		Assert.assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK3));
		Assert.assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK4));
	}

	@Test
	public void testGetDashboardTile() throws Exception
	{
		emsDashboardTileParamsPK.setDashboardTile(BigInteger.valueOf(10L));
		Assert.assertEquals(emsDashboardTileParamsPK.getDashboardTile(), BigInteger.valueOf(10L));

	}

	@Test
	public void testGetParamName() throws Exception
	{
		emsDashboardTileParamsPK = new EmsDashboardTileParamsPK();
		emsDashboardTileParamsPK.setParamName("elephant");
		Assert.assertEquals(emsDashboardTileParamsPK.getParamName(), "elephant");

	}

	@Test
	public void testHashCode() throws Exception
	{
		Assert.assertNotNull(emsDashboardTileParamsPK.hashCode());
	}

	@Test
	public void testSetDashboardTile() throws Exception
	{

	}

	@Test
	public void testSetParamName() throws Exception
	{

	}
}
