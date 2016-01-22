package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.*;

/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups = {"s1"})
public class EmsDashboardFavoriteTest {
    private EmsDashboard emsDashboard = new EmsDashboard();
    private EmsDashboardFavorite emsDashboardFavorite = new EmsDashboardFavorite
            (new Date(),emsDashboard,"elephant");
    @Test
    public void testGetCreationDate() throws Exception {
        EmsDashboardFavorite emsDashboardFavorite = new EmsDashboardFavorite();
        emsDashboardFavorite.setCreationDate(new Date());
        assertEquals(emsDashboardFavorite.getCreationDate(),new Date());
    }

    @Test
    public void testGetDashboard() throws Exception {
        emsDashboardFavorite.setDashboard(emsDashboard);
        assertEquals(emsDashboardFavorite.getDashboard(),emsDashboard);

    }

    @Test
    public void testGetUserName() throws Exception {
        emsDashboardFavorite.setUserName("elephant");
        assertEquals(emsDashboardFavorite.getUserName(),"elephant");
    }

    @Test
    public void testSetCreationDate() throws Exception {

    }

    @Test
    public void testSetDashboard() throws Exception {

    }

    @Test
    public void testSetUserName() throws Exception {

    }
}