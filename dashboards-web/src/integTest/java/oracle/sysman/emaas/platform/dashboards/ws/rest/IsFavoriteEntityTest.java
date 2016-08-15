package oracle.sysman.emaas.platform.dashboards.ws.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author Troy
 * @since 2016/1/21.
 */
@Test(groups={"s1"})
public class IsFavoriteEntityTest {
    private IsFavoriteEntity isFavoriteEntity = new IsFavoriteEntity();
    @Test
    public void testGetIsFavorite() {
        isFavoriteEntity.setIsFavorite(true);
        assertEquals(isFavoriteEntity.getIsFavorite(), Boolean.TRUE);
    }

    @Test
    public void testSetIsFavorite() {

    }
}