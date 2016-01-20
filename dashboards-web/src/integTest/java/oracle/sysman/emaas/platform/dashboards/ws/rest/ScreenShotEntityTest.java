package oracle.sysman.emaas.platform.dashboards.ws.rest;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by jishshi on 1/20/2016.
 */
public class ScreenShotEntityTest {

    ScreenShotEntity screenShotEntity;

    @BeforeMethod
    public void setUp() throws Exception {
        screenShotEntity = new ScreenShotEntity();
    }

    @Test
    public void testGetHref() throws Exception {
        Assert.assertNull(screenShotEntity.getHref());
        screenShotEntity.setHref("href");
        Assert.assertEquals(screenShotEntity.getHref(),"href");
    }

    @Test
    public void testGetScreenShot() throws Exception {
        Assert.assertNull(screenShotEntity.getScreenShot());
        screenShotEntity.setScreenShot("shot");
        Assert.assertEquals(screenShotEntity.getScreenShot(),"shot");
    }

    @Test
    public void testConstruct() throws Exception {
        screenShotEntity = new ScreenShotEntity("anotherHref","anotherSceen");
        Assert.assertEquals(screenShotEntity.getScreenShot(),"anotherSceen");
        Assert.assertEquals(screenShotEntity.getHref(),"anotherHref");
    }

}