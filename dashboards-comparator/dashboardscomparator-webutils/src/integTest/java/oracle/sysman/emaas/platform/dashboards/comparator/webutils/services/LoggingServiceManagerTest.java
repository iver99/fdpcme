package oracle.sysman.emaas.platform.dashboards.comparator.webutils.services;

import mockit.Expectations;
import mockit.Mocked;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import weblogic.application.ApplicationLifecycleEvent;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.net.URL;

/**
 * Created by chehao on 2017/1/11.
 */
@Test(groups = {"s2"})
public class LoggingServiceManagerTest {

    LoggingServiceManager lsm = new LoggingServiceManager();

    private static final Logger LOGGER = LogManager.getLogger(LoggingServiceManagerTest.class);
    @Test
    public void testGetName(){
        LoggingServiceManager manager=new LoggingServiceManager();
        manager.getName();
    }

    @Test(groups = { "s2" })
    public void testStartStop(@Mocked final MBeanServer anyMbs, @Mocked final ManagementFactory anyMf) throws Exception
    {
        new Expectations() {
            {
                ManagementFactory.getPlatformMBeanServer();
                result = anyMbs;
                anyMbs.unregisterMBean((ObjectName) any);
                times = 1;
            }
        };

        lsm.preStart(null);
        lsm.preStop(null);
    }

    @Test(groups = { "s2" })
    public void testStartStopExceptions(@Mocked final MBeanServer anyMbs, @Mocked final ManagementFactory anyMf,
                                        @Mocked final ObjectName anyObjName)
    {
        try {
            final ObjectName objName1 = new ObjectName(LoggingServiceManager.MBEAN_NAME);
            new Expectations() {
                {
                    new ObjectName(LoggingServiceManager.MBEAN_NAME);
                    result = objName1;
                    ManagementFactory.getPlatformMBeanServer();
                    result = anyMbs;
                    anyMbs.registerMBean(any, objName1);
                    result = new InstanceAlreadyExistsException();
                    anyMbs.registerMBean(any, (ObjectName) any);
                    anyMbs.unregisterMBean((ObjectName) any);
                    result = new Exception();
                }
            };

            lsm.preStart(null);
            lsm.preStop(null);
        }
        catch (Exception e) {
            LOGGER.info("context",e);
        }
    }

   /* @Test
    public void testLifeCycle(final @Mocked ApplicationLifecycleEvent evt) throws Exception {
        LoggingServiceManager manager=new LoggingServiceManager();

        final URL url = AppLoggingManageMXBeanTest.class.getResource("/log4j2_dsbcomparator-test.xml");
        *//**//**//**//*new Expectations() {
            {
                LoggingServiceManager.class.getResource("/log4j2_dsbcomparator.xml");
                result=url;
            }
        };*//**//**//**//*
        manager.preStart(null);

        manager.postStart(null);

        manager.preStop(null);

        manager.postStop(null);
    }*/
}
