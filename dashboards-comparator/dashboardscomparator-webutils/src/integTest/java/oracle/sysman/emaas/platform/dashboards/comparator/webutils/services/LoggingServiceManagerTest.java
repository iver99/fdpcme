package oracle.sysman.emaas.platform.dashboards.comparator.webutils.services;

import mockit.Expectations;
import mockit.Mocked;
import org.testng.annotations.Test;
import weblogic.application.ApplicationLifecycleEvent;

import java.net.URL;

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

    /*@Test
    public void testLifeCycle(final @Mocked ApplicationLifecycleEvent evt) throws Exception {
        LoggingServiceManager manager=new LoggingServiceManager();

        final URL url = AppLoggingManageMXBeanTest.class.getResource("/log4j2_dsbcomparator-test.xml");
        *//*new Expectations() {
            {
                LoggingServiceManager.class.getResource("/log4j2_dsbcomparator.xml");
                result=url;
            }
        };*//*
        manager.preStart(null);

        manager.postStart(null);

        manager.preStop(null);

        manager.postStop(null);
    }*/
}
