package oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig;

import mockit.Mocked;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jishshi
 * @since 1/18/2016.
 */
@Test(groups={"s1"})
public class LoggingItemsTest {
    LoggingItems loggingItems ;

    @Mocked
    LoggingItem loggingItem;

    @BeforeMethod
    public void setUp()  {
         loggingItems = new LoggingItems();
    }

    @AfterMethod
    public void tearDown()  {

    }

    @Test
    public void testGetItems()  {
        Assert.assertNull(loggingItems.getItems());

        List<LoggingItem> list = new ArrayList<>();
        list.add(loggingItem);
        list.add(loggingItem);
        loggingItems.setItems(list);
        Assert.assertEquals(loggingItems.getItems().size(),list.size());
    }

    @Test
    public void testGetTotal()  {
        Assert.assertEquals(loggingItems.getTotal(),0);

        List<LoggingItem> list = new ArrayList<>();
        list.add(loggingItem);
        list.add(loggingItem);
        loggingItems.setItems(list);
        Assert.assertEquals(loggingItems.getTotal(),2);

        loggingItems.setTotal(2);
        Assert.assertEquals(loggingItems.getTotal(),2);

    }

    @Test
    public void testAddLoggerConfig()  {
        loggingItems.setItems(null);
        loggingItems.addLoggerConfig(new LoggerConfig(), 123L);
        Assert.assertEquals(loggingItems.getTotal(),1);
        loggingItems.addLoggerConfig(new LoggerConfig(), 123L);
        Assert.assertEquals(loggingItems.getTotal(),2);
        loggingItems.addLoggerConfig(null, 123L);
        Assert.assertEquals(loggingItems.getTotal(),2);

    }


}