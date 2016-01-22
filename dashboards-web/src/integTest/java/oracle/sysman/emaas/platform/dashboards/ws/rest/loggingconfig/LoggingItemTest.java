package oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig;

import org.apache.logging.log4j.core.config.LoggerConfig;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by jishshi on 1/18/2016.
 */
@Test(groups={"s1"})
public class LoggingItemTest {

    LoggingItem loggingItem ;
    @BeforeMethod
    public void setUp() throws Exception {
        LoggerConfig loggerConfig = new LoggerConfig();
        Long timeStamp = new Long(122);
        loggingItem = new LoggingItem( loggerConfig,timeStamp );
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetCurrentLoggingLevel() throws Exception {
        loggingItem.setCurrentLoggingLevel(null);
        Assert.assertNull(loggingItem.getCurrentLoggingLevel());
        loggingItem.setCurrentLoggingLevel("level");
        Assert.assertEquals(loggingItem.getCurrentLoggingLevel(),"level");
    }

    @Test
    public void testGetLastUpdatedEpoch() throws Exception {
        loggingItem.setLastUpdatedEpoch(null);
        Assert.assertNull(loggingItem.getLastUpdatedEpoch());
        Long myTimeStamp = new Long(1234);
        loggingItem.setLastUpdatedEpoch(myTimeStamp);
        Assert.assertEquals(loggingItem.getLastUpdatedEpoch(),myTimeStamp);
    }

    @Test
    public void testGetLoggerName() throws Exception {
        loggingItem.setLoggerName(null);
        Assert.assertNull(loggingItem.getLoggerName());
        loggingItem.setLoggerName("loggerName");
        Assert.assertEquals(loggingItem.getLoggerName(),"loggerName");
    }

}