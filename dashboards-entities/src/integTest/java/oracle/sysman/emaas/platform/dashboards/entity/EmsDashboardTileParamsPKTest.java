package oracle.sysman.emaas.platform.dashboards.entity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Date;

import org.testng.annotations.Test;


@Test(groups = {"s1"})
public class EmsDashboardTileParamsPKTest { 
    Date now =  new Date();
    private EmsDashboard emsDashboard = new EmsDashboard(now,10L,10L,"elephant",10,10,10,10,10,10, now,
            "elephant","elephant","elephant","elephant",10,10, "{}");
    private EmsDashboardTile emsDashboardTile = new EmsDashboardTile(
            now,
            emsDashboard,
            10,
            10,
            10,
            10,
            10,
            now,
            "elephant",
            "elephant", /*Integer position, */
            "elephant",
            "elephant",
            "dolphine",
            10L,
            "dolphine",
            "dolphine",
            "dolphine",
            "kitten",
            "kitten",
            "kitten",
            "kitten",
            "kitten",
            "lion",
            10,
            "lion",
            "lion",
            "lion",
            10,
            10,
            10L);
    private EmsDashboardTileParams emsDashboardTileParams = new
            EmsDashboardTileParams(
            10,
            "elephant",
            10,
            10,
            "dolphine",
            now,
            emsDashboardTile);
    private EmsDashboardTileParamsPK emsDashboardTileParamsPK = new EmsDashboardTileParamsPK("elephant",10L);

    @Test
    public void testEquals() {
        EmsDashboardTileParamsPK emsDashboardTileParamsPK1 = new EmsDashboardTileParamsPK("elephant",10L);
        EmsDashboardTileParamsPK emsDashboardTileParamsPK2 = new EmsDashboardTileParamsPK("elephant",11L);
        EmsDashboardTileParamsPK emsDashboardTileParamsPK3 = new EmsDashboardTileParamsPK("dolphine",11L);
        EmsDashboardTileParamsPK emsDashboardTileParamsPK4 = new EmsDashboardTileParamsPK("dolphine",10L);
        assertFalse(emsDashboardTileParamsPK.equals(new Integer(10)));
        assertTrue(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK1));
        assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK2));
        assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK3));
        assertFalse(emsDashboardTileParamsPK.equals(emsDashboardTileParamsPK4));
    }

    @Test
    public void testGetDashboardTile() {
        emsDashboardTileParamsPK.setDashboardTile(10L);
        assertEquals(emsDashboardTileParamsPK.getDashboardTile(),new Long(10));

    }

    @Test
    public void testGetParamName() {
        emsDashboardTileParamsPK= new EmsDashboardTileParamsPK();
        emsDashboardTileParamsPK.setParamName("elephant");
        assertEquals(emsDashboardTileParamsPK.getParamName(),"elephant");

    }

    @Test
    public void testHashCode() {
        assertNotNull(emsDashboardTileParamsPK.hashCode());
    }

}
