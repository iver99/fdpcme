package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.*;

/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups = {"s1"})
public class EmsDashboardLastAccessTest {
    Date now = new Date();
    private EmsDashboardLastAccess emsDashboardLastAccess = new EmsDashboardLastAccess(now,"elephant",10L);
    @Test
    public void testGetAccessDate() throws Exception {
        EmsDashboardLastAccess emsDashboardLastAccess = new EmsDashboardLastAccess();
        emsDashboardLastAccess.setAccessDate(now);
        assertEquals(emsDashboardLastAccess.getAccessDate(), now);
    }

    @Test
    public void testGetAccessedBy() throws Exception {
        emsDashboardLastAccess.setAccessedBy("elephant");
        assertEquals(emsDashboardLastAccess.getAccessedBy(),"elephant");

    }

    @Test
    public void testGetDashboardId() throws Exception {
        emsDashboardLastAccess.setDashboardId(10L);
        assertEquals(emsDashboardLastAccess.getDashboardId(),new Long(10));
    }

    @Test
    public void testSetAccessDate() throws Exception {

    }

    @Test
    public void testSetAccessedBy() throws Exception {

    }

    @Test
    public void testSetDashboardId() throws Exception {

    }
}