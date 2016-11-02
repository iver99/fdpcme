package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2016/11/1.
 */
@Test(groups = {"s1"})
public class EmsDashboardSetPKTest {
    private EmsDashboardSetPK emsDashboardSetPK = new EmsDashboardSetPK();
    @Test
    public void testGetDashboardSetId() throws Exception {
        emsDashboardSetPK.setDashboardSetId(1L);
        emsDashboardSetPK.getDashboardSetId();
    }

    @Test
    public void testGetSubDashboardId() throws Exception {
        emsDashboardSetPK.setSubDashboardId(1L);
        emsDashboardSetPK.getSubDashboardId();
    }

    @Test
    public void testEquals() throws Exception {
        EmsDashboardSetPK emsDashboardSetPKIns = new EmsDashboardSetPK();
        emsDashboardSetPKIns.setDashboardSetId(1L);
        emsDashboardSetPK.equals(emsDashboardSetPKIns);
        emsDashboardSetPK.equals(new EmsDashboard());
    }

    @Test
    public void testHashCode() throws Exception {
        emsDashboardSetPK.hashCode();
    }

}