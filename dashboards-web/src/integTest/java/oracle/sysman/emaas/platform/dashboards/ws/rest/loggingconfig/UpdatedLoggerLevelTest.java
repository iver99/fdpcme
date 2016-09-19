package oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/18/2016.
 */
@Test(groups={"s1"})
public class UpdatedLoggerLevelTest {
    UpdatedLoggerLevel updatedLoggerLevel;
    @BeforeMethod
    public void setUp() {
        updatedLoggerLevel = new UpdatedLoggerLevel();
    }

    @AfterMethod
    public void tearDown() {
    	// do nothing
    }

    @Test
    public void testGetLevel() {
        Assert.assertNull(updatedLoggerLevel.getLevel());

        updatedLoggerLevel.setLevel("level");
        Assert.assertEquals(updatedLoggerLevel.getLevel(),"level");

        updatedLoggerLevel.setLevel(null);
        Assert.assertNull(updatedLoggerLevel.getLevel());

    }
}