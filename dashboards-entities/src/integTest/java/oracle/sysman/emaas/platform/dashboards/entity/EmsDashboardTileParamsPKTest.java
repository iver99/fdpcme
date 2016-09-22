package oracle.sysman.emaas.platform.dashboards.entity;

import java.math.BigInteger;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"s1"})
public class EmsDashboardTileParamsPKTest { 
    Date now =  new Date();
  
    private EmsDashboardTileParamsPK emsDashboardTileParamsPK = new EmsDashboardTileParamsPK("elephant",BigInteger.valueOf(10L));

    @Test
    public void testEquals() {
        EmsDashboardTileParamsPK emsDashboardTileParamsPK1 = new EmsDashboardTileParamsPK("elephant",BigInteger.valueOf(10L));
        EmsDashboardTileParamsPK emsDashboardTileParamsPK2 = new EmsDashboardTileParamsPK("elephant",BigInteger.valueOf(11L));
        EmsDashboardTileParamsPK emsDashboardTileParamsPK3 = new EmsDashboardTileParamsPK("dolphine",BigInteger.valueOf(11L));
        EmsDashboardTileParamsPK emsDashboardTileParamsPK4 = new EmsDashboardTileParamsPK("dolphine",BigInteger.valueOf(10L));
        Assert.assertFalse(emsDashboardTileParamsPK.equals(new Integer(10)));
        Assert.assertTrue(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK1));
        Assert.assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK2));
        Assert.assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK3));
        Assert.assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK4));
    }

    @Test
    public void testGetDashboardTile() {
        emsDashboardTileParamsPK.setDashboardTile(BigInteger.valueOf(10L));
        Assert.assertEquals(emsDashboardTileParamsPK.getDashboardTile(),BigInteger.valueOf(10L));
	}
    
    @Test
    public void testGetParamName() {
        emsDashboardTileParamsPK= new EmsDashboardTileParamsPK();
        emsDashboardTileParamsPK.setParamName("elephant");
        Assert.assertEquals(emsDashboardTileParamsPK.getParamName(),"elephant");
	}
    
	@Test
	public void testHashCode()
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
