package oracle.sysman.emaas.platform.dashboards.comparator.webutils.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class PropertyReaderTest {

    private static final Logger LOGGER = LogManager.getLogger(PropertyReaderTest.class);

    @Test
    public void testGetInstallDir() {
        Assert.assertNotNull(PropertyReader.getInstallDir());
    }

    @Test
    public void testGetRunningInContainer() {
        Assert.assertFalse(PropertyReader.getRunningInContainer());
    }

    @Test
    public void testLoadProperty() {
        Properties prop = null;
        try {
            prop = PropertyReader.loadProperty("");
        } catch (IOException e) {
            LOGGER.info("context", e);
        }
        Assert.assertNotNull(prop);
    }
}

