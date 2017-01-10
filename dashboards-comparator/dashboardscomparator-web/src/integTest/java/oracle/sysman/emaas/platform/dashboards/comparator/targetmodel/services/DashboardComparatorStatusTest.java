package oracle.sysman.emaas.platform.dashboards.comparator.targetmodel.services;

import mockit.Expectations;
import mockit.Mocked;
import org.testng.annotations.Test;

import javax.management.*;

import java.util.ArrayList;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2017/1/10.
 */
@Test(groups = {"s2"})
public class DashboardComparatorStatusTest {
    @Mocked
    GlobalStatus globalStatus;
    private DashboardComparatorStatus dashboardComparatorStatus = new DashboardComparatorStatus();
    @Test
    public void testGetStatus(){
        new Expectations(){
            {
                globalStatus.isDashboardComparatorUp();
                result = false;
            }
        };
        dashboardComparatorStatus.getStatus();
        dashboardComparatorStatus.getStatusMsg();
        dashboardComparatorStatus.equals(dashboardComparatorStatus);
    }

    @Test
    public void testGetStatus2nd(){
        new Expectations(){
            {
                globalStatus.isDashboardComparatorUp();
                result = true;
            }
        };
        dashboardComparatorStatus.getStatus();
        dashboardComparatorStatus.getStatusMsg();
        dashboardComparatorStatus.equals(dashboardComparatorStatus);
    }
    private JMXUtil jmxUtil = JMXUtil.getInstance();
    @Test
    public void testRegisterMBeans() throws MalformedObjectNameException, InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException, InstanceNotFoundException {
        jmxUtil.registerMBeans();
        jmxUtil.unregisterMBeans();
    }


}