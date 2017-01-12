package oracle.sysman.emaas.platform.dashboards.comparator.targetmodel.services;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/12.
 */
@Test(groups = {"s2"})
public class GlobalStatusTest {

    @Test
    public void testIsDashboardComparatorUp(){
        GlobalStatus.setDashboardComparatorUpStatus();
    }

    @Test
    public void testSetDashboardComparatorDownStatus(){
        GlobalStatus.setDashboardComparatorDownStatus();
    }
    @Test
    public void testSetDashboardComparatorUpStatus(){
        GlobalStatus.isDashboardComparatorUp();
    }
}
