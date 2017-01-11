package oracle.sysman.emaas.platform.dashboards.comparator.webutils.wls.management;

import org.apache.logging.log4j.core.config.Configurator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by chehao on 2017/1/11.
 */
@Test(groups = {"s2"})
public class AppLoggingManageMXBeanTest {

    private IAppLoggingManageMXBean bean;

    @BeforeMethod
    public void beforeMethod() throws URISyntaxException
    {
        URL url = AppLoggingManageMXBeanTest.class.getResource("/log4j2_dsbcomparator.xml");
        Configurator.initialize("root", AppLoggingManageMXBeanTest.class.getClassLoader(), url.toURI());
    }
    @BeforeClass
    public void setUp(){
        bean = new AppLoggingManageMXBean();
    }

    @Test
    public void testGetLogLevel() {
        String levels = bean.getLogLevels();
        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"INFO\""));

        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator\":\"INFO\""));
    }

    @Test
    public void testSetLogLevel(){
        bean.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "DEBUG");
        String levels = bean.getLogLevels();
        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"DEBUG\""));

        bean.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "INFO");
        levels = bean.getLogLevels();
        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"INFO\""));

        bean.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "WARN");
        levels = bean.getLogLevels();
        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"WARN\""));

        bean.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "ERROR");
        levels = bean.getLogLevels();
        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"ERROR\""));

        bean.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "FATAL");
        levels = bean.getLogLevels();
        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"FATAL\""));
    }
}
