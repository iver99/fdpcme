package oracle.sysman.emaas.platform.dashboards.ws.rest;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = {"s1"})
public class ScreenShotEntityTest {

    ScreenShotEntity screenShotEntity;

    @BeforeMethod
    public void setUp() {
        screenShotEntity = new ScreenShotEntity();
    }

    @Test
    public void testGetHref() {
        Assert.assertNull(screenShotEntity.getHref());
        screenShotEntity.setHref("href");
        Assert.assertEquals(screenShotEntity.getHref(),"href");
    }

    @Test
    public void testGetScreenShot() {
        Assert.assertNull(screenShotEntity.getScreenShot());
        screenShotEntity.setScreenShot("shot");
        Assert.assertEquals(screenShotEntity.getScreenShot(),"shot");
    }

    @Test
    public void testConstruct() {
        screenShotEntity = new ScreenShotEntity("anotherHref","anotherSceen");
        Assert.assertEquals(screenShotEntity.getScreenShot(),"anotherSceen");
        Assert.assertEquals(screenShotEntity.getHref(),"anotherHref");
    }

}