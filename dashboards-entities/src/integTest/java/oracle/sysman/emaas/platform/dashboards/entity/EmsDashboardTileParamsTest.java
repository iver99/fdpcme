package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.assertEquals;


@Test(groups = {"s1"})
public class EmsDashboardTileParamsTest { 
    Date now = new Date();
    private EmsDashboard emsDashboard = new EmsDashboard(now,10L,10L,"elephant",10,10,10,10,10,10, now,
            "elephant","elephant","elephant","elephant",10,10,1, "{}");
    private EmsDashboardTile emsDashboardTile = new EmsDashboardTile(
            now,
            emsDashboard,
            10,
            10,
            10,
            10,
            10,
            now,
            "elephant",
            "elephant", /*Integer position, */
            "elephant",
            "elephant",
            "dolphine",
            10L,
            "dolphine",
            "dolphine",
            "dolphine",
            "kitten",
            "kitten",
            "kitten",
            "kitten",
            "kitten",
            "lion",
            10,
            "lion",
            "lion",
            "lion",
            10,
            10,
            10L);
    private EmsDashboardTileParams emsDashboardTileParams = new
            EmsDashboardTileParams(
            10,
            "elephant",
            10,
            10,
            "dolphine",
            now,
            emsDashboardTile);
    @Test
    public void testGetDashboardTile() {
        emsDashboardTileParams.setDashboardTile(emsDashboardTile);
        assertEquals(emsDashboardTileParams.getDashboardTile(),emsDashboardTile);
    }

    @Test
    public void testGetIsSystem() {
        emsDashboardTileParams.setIsSystem(10);
        assertEquals(emsDashboardTileParams.getIsSystem(),new Integer(10));
    }

    @Test
    public void testGetParamName() {
        emsDashboardTileParams.setParamName("elephant");
        assertEquals(emsDashboardTileParams.getParamName(),"elephant");
    }

    @Test
    public void testGetParamType() {
        emsDashboardTileParams.setParamType(10);
        assertEquals(emsDashboardTileParams.getParamType(),new Integer(10));
    }

    @Test
    public void testGetParamValueNum() {
        emsDashboardTileParams.setParamValueNum(10);
        assertEquals(emsDashboardTileParams.getParamValueNum(),new Integer(10));

    }

    @Test
    public void testGetParamValueStr() {
        emsDashboardTileParams.setParamValueStr("elephant");
        assertEquals(emsDashboardTileParams.getParamValueStr(),"elephant");
    }

    @Test
    public void testGetParamValueTimestamp() {
        emsDashboardTileParams = new EmsDashboardTileParams();
        emsDashboardTileParams.setParamValueTimestamp(now);
        assertEquals(emsDashboardTileParams.getParamValueTimestamp(), now);
    }

   
}
