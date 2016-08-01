package oracle.sysman.emaas.platform.dashboards.entity;

import java.math.BigInteger;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups = { "s1" })
public class EmsDashboardTileParamsTest
{
	Date now = new Date();
	private final EmsDashboard emsDashboard = new EmsDashboard(now, BigInteger.valueOf(10L), BigInteger.valueOf(10L), "elephant",
			10, 10, 10, 10, 10, 10, now, "elephant", "elephant", "elephant", "elephant", 10, 10, "elephant");
	private final EmsDashboardTile emsDashboardTile = new EmsDashboardTile(now, emsDashboard, 10, 10, 10, 10, 10, now,
			"elephant", "elephant", /*Integer position, */
			"elephant", "elephant", "dolphine", BigInteger.valueOf(10L), "dolphine", "dolphine", "dolphine", "kitten", "kitten",
			"kitten", "kitten", "kitten", "lion", 10, "lion", "lion", "lion", 10, 10, BigInteger.valueOf(10L));
	private EmsDashboardTileParams emsDashboardTileParams = new EmsDashboardTileParams(10, "elephant", 10, 10, "dolphine", now,
			emsDashboardTile);

	@Test
	public void testGetDashboardTile() throws Exception
	{
		emsDashboardTileParams.setDashboardTile(emsDashboardTile);
		Assert.assertEquals(emsDashboardTileParams.getDashboardTile(), emsDashboardTile);
	}

	@Test
	public void testGetIsSystem() throws Exception
	{
		emsDashboardTileParams.setIsSystem(10);
		Assert.assertEquals(emsDashboardTileParams.getIsSystem(), new Integer(10));
	}

	@Test
	public void testGetParamName() throws Exception
	{
		emsDashboardTileParams.setParamName("elephant");
		Assert.assertEquals(emsDashboardTileParams.getParamName(), "elephant");
	}

	@Test
	public void testGetParamType() throws Exception
	{
		emsDashboardTileParams.setParamType(10);
		Assert.assertEquals(emsDashboardTileParams.getParamType(), new Integer(10));
	}

	@Test
	public void testGetParamValueNum() throws Exception
	{
		emsDashboardTileParams.setParamValueNum(10);
		Assert.assertEquals(emsDashboardTileParams.getParamValueNum(), new Integer(10));

	}

	@Test
	public void testGetParamValueStr() throws Exception
	{
		emsDashboardTileParams.setParamValueStr("elephant");
		Assert.assertEquals(emsDashboardTileParams.getParamValueStr(), "elephant");
	}

	@Test
	public void testGetParamValueTimestamp() throws Exception
	{
		emsDashboardTileParams = new EmsDashboardTileParams();
		emsDashboardTileParams.setParamValueTimestamp(now);
		Assert.assertEquals(emsDashboardTileParams.getParamValueTimestamp(), now);
	}

	@Test
	public void testSetDashboardTile() throws Exception
	{

	}

	@Test
	public void testSetIsSystem() throws Exception
	{

	}

	@Test
	public void testSetParamName() throws Exception
	{

	}

	@Test
	public void testSetParamType() throws Exception
	{

	}

	@Test
	public void testSetParamValueNum() throws Exception
	{

	}

	@Test
	public void testSetParamValueStr() throws Exception
	{

	}

	@Test
	public void testSetParamValueTimestamp() throws Exception
	{

	}
}
