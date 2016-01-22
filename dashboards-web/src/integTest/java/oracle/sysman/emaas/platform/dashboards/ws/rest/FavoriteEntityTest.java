package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Troy on 2016/1/21.
 */
@Test(groups = {"s1"})
public class FavoriteEntityTest {
    private FavoriteEntity favoriteEntity = new FavoriteEntity();
    private Dashboard dbd = new Dashboard();
    private FavoriteEntity favoriteEntity1 = new FavoriteEntity(dbd);
    @Test
    public void testGetHref() throws Exception {
        favoriteEntity.setHref("www.oracle.com");
        assertEquals(favoriteEntity.getHref(),"www.oracle.com");
    }

    @Test
    public void testGetId() throws Exception {
        favoriteEntity.setId(10L);
        assertEquals(favoriteEntity.getId(),new Long(10));

    }

    @Test
    public void testGetName() throws Exception {
        favoriteEntity.setName("elephant");
        assertEquals(favoriteEntity.getName(),"elephant");

    }

    @Test
    public void testSetHref() throws Exception {

    }

    @Test
    public void testSetId() throws Exception {

    }

    @Test
    public void testSetName() throws Exception {

    }
}