package oracle.sysman.emaas.platform.dashboards.comparator.webutils.services;

import mockit.Mocked;
import org.apache.logging.log4j.core.config.Configurator;
import org.testng.annotations.Test;
import weblogic.application.ApplicationLifecycleEvent;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class DashboardComparatorLifecycleServiceTest {

    @Mocked
    ApplicationLifecycleEvent applicationLifecycleEvent;
    @Mocked
    Configurator configurator;
    @Mocked
    URL url;
    @Mocked
    URI uri;
    @Mocked
    Throwable throwable;
    @Mocked
    TimeUnit timeUnit;

    private LoggingServiceManager loggingServiceManager = new LoggingServiceManager();
    @Test
    public void testLoggingServiceManager() throws Exception {
        loggingServiceManager.equals(loggingServiceManager);
        loggingServiceManager.getName();
        loggingServiceManager.postStart(applicationLifecycleEvent);
        loggingServiceManager.postStop(applicationLifecycleEvent);
    }
    private PropertyReader propertyReader = new PropertyReader();
   /* @Test
    public void testPropertyReader() throws IOException {
        PropertyReader.getInstallDir();
        PropertyReader.loadProperty("");
    }*/
    private RegistryServiceManager registryServiceManager = new RegistryServiceManager();
    private RegistryServiceManager.ServiceConfigBuilder serviceConfigBuilder =registryServiceManager.new ServiceConfigBuilder();
    @Test
    public void testRegistryServiceManager() throws Exception {
        serviceConfigBuilder.build();
        serviceConfigBuilder.canonicalEndpoints("");
        serviceConfigBuilder.characteristics("");
        serviceConfigBuilder.controlledDatatypes("");
        serviceConfigBuilder.datatypes("");
        serviceConfigBuilder.leaseRenewalInterval(1L, timeUnit);
        serviceConfigBuilder.loadScore(2);
        serviceConfigBuilder.registryUrls("");
        serviceConfigBuilder.serviceName("");
        serviceConfigBuilder.serviceUrls("");
        serviceConfigBuilder.supportedTargetTypes("");
        serviceConfigBuilder.version("");
        serviceConfigBuilder.virtualEndpoints("");
        registryServiceManager.postStop(applicationLifecycleEvent);
        registryServiceManager.preStart(applicationLifecycleEvent);
    }

    /*@Test
    public void testDashboardComparatorLifecycleService(){
        DashboardComparatorLifecycleService dcs=new DashboardComparatorLifecycleService();
    }*/
}
