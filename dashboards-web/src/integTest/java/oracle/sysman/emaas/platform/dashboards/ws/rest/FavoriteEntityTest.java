package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.math.BigInteger;

import org.testng.Assert;
import org.testng.annotations.Test;


@Test(groups = { "s1" })
public class FavoriteEntityTest
{
	private final FavoriteEntity favoriteEntity = new FavoriteEntity();

	@Test
	public void testGetHref() throws Exception
	{
		favoriteEntity.setHref("www.oracle.com");
		Assert.assertEquals(favoriteEntity.getHref(), "www.oracle.com");
	}

	@Test
	public void testGetId() throws Exception
	{
		favoriteEntity.setId(BigInteger.valueOf(10L));
		Assert.assertEquals(favoriteEntity.getId(), BigInteger.valueOf(10L));

	}

	@Test
	public void testGetName() throws Exception
	{
		favoriteEntity.setName("elephant");
		Assert.assertEquals(favoriteEntity.getName(), "elephant");

	}

}