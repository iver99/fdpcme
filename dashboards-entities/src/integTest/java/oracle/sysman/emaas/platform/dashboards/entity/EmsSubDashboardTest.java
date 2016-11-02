package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test(groups = {"s1"})
public class EmsSubDashboardTest {
    private EmsSubDashboard emsSubDashboard = new EmsSubDashboard(1L,1L,1);
    @Test
    public void testSetDashboardSetId() throws Exception {
        emsSubDashboard.setDashboardSetId(1L);
        emsSubDashboard.getDashboardSetId();
    }

    @Test
    public void testSetSubDashboardId() throws Exception {
        emsSubDashboard.setSubDashboardId(1L);
        emsSubDashboard.getSubDashboardId();
    }

    @Test
    public void testGetPosition() throws Exception {
        emsSubDashboard.setPosition(1);
        emsSubDashboard.getPosition();
    }

    @Test
    public void testGetDashboardSet() throws Exception {
        emsSubDashboard.setDashboardSet(new EmsDashboard());
        emsSubDashboard.getDashboardSet();
    }
}