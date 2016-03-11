package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups = {"s1"})
public class EmsDashboardFavoritePKTest {
    EmsDashboardFavoritePK emsDashboardFavoritePK = new EmsDashboardFavoritePK("elephant",10L);
    @Test
    public void testEquals() throws Exception {
        EmsDashboardFavoritePK emsDashboardFavoritePK1 = new EmsDashboardFavoritePK("elephant",10L);
        EmsDashboardFavoritePK emsDashboardFavoritePK2 = new EmsDashboardFavoritePK("dolphine",10L);
        EmsDashboardFavoritePK emsDashboardFavoritePK3 = new EmsDashboardFavoritePK("elephant",11L);
        assertTrue(emsDashboardFavoritePK.equals(emsDashboardFavoritePK1));
        assertFalse(emsDashboardFavoritePK.equals(emsDashboardFavoritePK2));
        assertFalse(emsDashboardFavoritePK.equals(emsDashboardFavoritePK3));
        assertFalse(emsDashboardFavoritePK.equals(new Integer(10)));
    }

    @Test
    public void testGetDashboard() throws Exception {
        emsDashboardFavoritePK= new EmsDashboardFavoritePK();
        emsDashboardFavoritePK.setDashboard(10L);
        assertEquals(emsDashboardFavoritePK.getDashboard(),new Long(10));
    }

    @Test
    public void testGetUserName() throws Exception {
        emsDashboardFavoritePK.setUserName("elephant");
        assertEquals(emsDashboardFavoritePK.getUserName(),"elephant");
    }

    @Test
    public void testHashCode() throws Exception {
        assertNotNull(emsDashboardFavoritePK.hashCode());
    }

    @Test
    public void testSetDashboard() throws Exception {

    }

    @Test
    public void testSetUserName() throws Exception {

    }
}