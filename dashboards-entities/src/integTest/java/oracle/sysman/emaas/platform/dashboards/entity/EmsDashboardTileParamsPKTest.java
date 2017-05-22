package oracle.sysman.emaas.platform.dashboards.entity;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"s1"})
public class EmsDashboardTileParamsPKTest { 
    Date now =  new Date();
    Long tenantId = 1L;
    EmsDashboardTilePK tilePK1 = new EmsDashboardTilePK("tile1", tenantId);
    EmsDashboardTilePK tilePK2 = new EmsDashboardTilePK("tile2", tenantId);
    private EmsDashboardTileParamsPK emsDashboardTileParamsPK = new EmsDashboardTileParamsPK("elephant", tilePK1, tenantId);

    @Test
    public void testEquals() {
        EmsDashboardTileParamsPK emsDashboardTileParamsPK1 = new EmsDashboardTileParamsPK("elephant", tilePK1, tenantId);
        EmsDashboardTileParamsPK emsDashboardTileParamsPK2 = new EmsDashboardTileParamsPK("elephant", tilePK2, tenantId);
        EmsDashboardTileParamsPK emsDashboardTileParamsPK3 = new EmsDashboardTileParamsPK("dolphine", tilePK2, tenantId);
        EmsDashboardTileParamsPK emsDashboardTileParamsPK4 = new EmsDashboardTileParamsPK("dolphine", tilePK1, tenantId);
        Assert.assertFalse(emsDashboardTileParamsPK.equals(new Integer(10)));
        Assert.assertTrue(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK1));
        Assert.assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK2));
        Assert.assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK3));
        Assert.assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK4));
    }

    @Test
    public void testGetDashboardTile() {
        emsDashboardTileParamsPK.setDashboardTile(new EmsDashboardTilePK("10", tenantId));
        Assert.assertEquals(emsDashboardTileParamsPK.getDashboardTile().getTileId(), "10");
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
