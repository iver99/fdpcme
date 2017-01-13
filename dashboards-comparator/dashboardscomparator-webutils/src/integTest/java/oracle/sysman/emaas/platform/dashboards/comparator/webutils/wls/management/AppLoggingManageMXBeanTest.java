
package oracle.sysman.emaas.platform.dashboards.comparator.webutils.wls.management;

import org.apache.logging.log4j.core.config.Configurator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.net.URL;


/**
 * Created by chehao on 2017/1/11.
 */

@Test(groups = {"s2"})
public class AppLoggingManageMXBeanTest {


    @BeforeClass
    public void setUp() throws URISyntaxException {
        URL url = AppLoggingManageMXBeanTest.class.getResource("/log4j2_dsbcomparator.xml");
        Configurator.initialize("root", AppLoggingManageMXBeanTest.class.getClassLoader(), url.toURI());
    }

    @Test
    public void testGetLogLevel() {
        AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
        String levels = almmxb.getLogLevels();
//        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"INFO\""));

//        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator\":\"INFO\""));
    }

    @Test
    public void testSetLogLevel(){
        AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
        almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "DEBUG");
        String levels = almmxb.getLogLevels();
//        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"DEBUG\""));

        almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "INFO");
        levels = almmxb.getLogLevels();
//        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"INFO\""));

        almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "WARN");
        levels = almmxb.getLogLevels();
//        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"WARN\""));

        almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "ERROR");
        levels = almmxb.getLogLevels();
//        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"ERROR\""));

        almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.comparator.webutils", "FATAL");
        levels = almmxb.getLogLevels();
//        Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.comparator.webutils\":\"FATAL\""));
    }
}

