package oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig;

import org.apache.logging.log4j.core.config.LoggerConfig;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/18/2016.
 */
@Test(groups={"s1"})
public class LoggingItemTest {

    LoggingItem loggingItem ;
    @BeforeMethod
    public void setUp() {
        LoggerConfig loggerConfig = new LoggerConfig();
        Long timeStamp = 122L;
        loggingItem = new LoggingItem( loggerConfig,timeStamp );
    }

    @AfterMethod
    public void tearDown() {
    	// do nothing
    }

    @Test
    public void testGetCurrentLoggingLevel()  {
        loggingItem.setCurrentLoggingLevel(null);
        Assert.assertNull(loggingItem.getCurrentLoggingLevel());
        loggingItem.setCurrentLoggingLevel("level");
        Assert.assertEquals(loggingItem.getCurrentLoggingLevel(),"level");
    }

    @Test
    public void testGetLastUpdatedEpoch()  {
        loggingItem.setLastUpdatedEpoch(null);
        Assert.assertNull(loggingItem.getLastUpdatedEpoch());
        Long myTimeStamp = 1234L;
        loggingItem.setLastUpdatedEpoch(myTimeStamp);
        Assert.assertEquals(loggingItem.getLastUpdatedEpoch(),myTimeStamp);
    }

    @Test
    public void testGetLoggerName()  {
        loggingItem.setLoggerName(null);
        Assert.assertNull(loggingItem.getLoggerName());
        loggingItem.setLoggerName("loggerName");
        Assert.assertEquals(loggingItem.getLoggerName(),"loggerName");
    }

}