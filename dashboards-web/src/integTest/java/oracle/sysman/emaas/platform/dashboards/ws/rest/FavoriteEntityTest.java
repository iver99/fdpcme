package oracle.sysman.emaas.platform.dashboards.ws.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author Troy
 * @since 2016/1/21.
 */
@Test(groups = {"s1"})
public class FavoriteEntityTest {
    private FavoriteEntity favoriteEntity = new FavoriteEntity();

    @Test
    public void testGetHref() {
        favoriteEntity.setHref("www.oracle.com");
        assertEquals(favoriteEntity.getHref(),"www.oracle.com");
    }

    @Test
    public void testGetId() {
        favoriteEntity.setId(10L);
        assertEquals(favoriteEntity.getId(),new Long(10));

    }

    @Test
    public void testGetName() {
        favoriteEntity.setName("elephant");
        assertEquals(favoriteEntity.getName(),"elephant");

    }

    @Test
    public void testSetHref() {

    }

    @Test
    public void testSetId() {

    }

    @Test
    public void testSetName() {

    }
}