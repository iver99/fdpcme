package oracle.sysman.emaas.platform.dashboards.comparator.targetmodel.services;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class DashboardComparatorStatusTest {


    @Test
    public void testGetStatus() {
        DashboardComparatorStatus dcs=new DashboardComparatorStatus();

        GlobalStatus.setDashboardComparatorUpStatus();
        System.out.println(dcs.getStatus());
        System.out.println(dcs.getStatusMsg());

        GlobalStatus.setDashboardComparatorDownStatus();

        System.out.println(dcs.getStatus());
        System.out.println(dcs.getStatusMsg());

    }
}
