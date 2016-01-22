package oracle.sysman.emaas.platform.dashboards.ws.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Troy on 2016/1/21.
 */
@Test(groups={"s1"})
public class IsFavoriteEntityTest {
    private IsFavoriteEntity isFavoriteEntity = new IsFavoriteEntity();
    @Test
    public void testGetIsFavorite() throws Exception {
        isFavoriteEntity.setIsFavorite(true);
        assertEquals(isFavoriteEntity.getIsFavorite(), new Boolean(true));
    }

    @Test
    public void testSetIsFavorite() throws Exception {

    }
}