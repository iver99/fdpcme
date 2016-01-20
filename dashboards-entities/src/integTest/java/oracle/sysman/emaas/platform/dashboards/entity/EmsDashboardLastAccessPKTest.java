package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups = {"s1"})
public class EmsDashboardLastAccessPKTest {
    private EmsDashboardLastAccessPK emsDashboardLastAccessPK =
            new EmsDashboardLastAccessPK("elephant",10L);
    @Test
    public void testEquals() throws Exception {
        EmsDashboardLastAccessPK emsDashboardLastAccessPK1 = new EmsDashboardLastAccessPK("elephant",10L);
        EmsDashboardLastAccessPK emsDashboardLastAccessPK2 = new EmsDashboardLastAccessPK("elephant",11L);
        EmsDashboardLastAccessPK emsDashboardLastAccessPK3 = new EmsDashboardLastAccessPK("dolphine",10L);
        EmsDashboardLastAccessPK emsDashboardLastAccessPK4 = new EmsDashboardLastAccessPK("dolphine",11L);
        assertFalse(emsDashboardLastAccessPK.equals(new Integer(10)));
        assertTrue(emsDashboardLastAccessPK.equals(emsDashboardLastAccessPK1));
        assertFalse(emsDashboardLastAccessPK.equals(emsDashboardLastAccessPK2));
        assertFalse(emsDashboardLastAccessPK.equals(emsDashboardLastAccessPK3));
        assertFalse(emsDashboardLastAccessPK.equals(emsDashboardLastAccessPK4));
    }

    @Test
    public void testGetAccessedBy() throws Exception {
        emsDashboardLastAccessPK = new EmsDashboardLastAccessPK();
        emsDashboardLastAccessPK.setAccessedBy("elephant");
        assertEquals(emsDashboardLastAccessPK.getAccessedBy(),"elephant");

    }

    @Test
    public void testGetDashboardId() throws Exception {
        emsDashboardLastAccessPK.setDashboardId(new Long(111));
        assertEquals(emsDashboardLastAccessPK.getDashboardId(),new Long(111));
    }

    @Test
    public void testHashCode() throws Exception {
        assertNotNull(emsDashboardLastAccessPK.hashCode());
    }

    @Test
    public void testSetAccessedBy() throws Exception {

    }

    @Test
    public void testSetDashboardId() throws Exception {

    }
}