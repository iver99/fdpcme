package oracle.sysman.emaas.platform.dashboards.comparator.webutils.services;

import mockit.Mocked;
import org.testng.annotations.Test;
import weblogic.application.ApplicationLifecycleEvent;

/**
 * Created by chehao on 2017/1/11.
 */
@Test(groups = {"s2"})
public class LoggingServiceManagerTest {

    @Test
    public void testGetName(){
        LoggingServiceManager manager=new LoggingServiceManager();
        manager.getName();
    }

    @Test
    public void testLifeCycle(final @Mocked ApplicationLifecycleEvent evt) throws Exception {
        LoggingServiceManager manager=new LoggingServiceManager();

        manager.preStart(evt);

        manager.postStart(evt);

        manager.preStop(evt);

        manager.postStop(evt);
    }
}
