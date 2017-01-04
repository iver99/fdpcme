package oracle.sysman.emaas.platform.dashboards.core.util;

import oracle.sysman.emaas.platform.dashboards.entity.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2016/11/1.
 */
@Test(groups = { "s1" })
public class FacadeUtilTest {
    @Test
    public void testCloneEmsDashboard() throws Exception {
        EmsDashboard emsDashboard = new EmsDashboard();
        EmsDashboardTile emsDashboardTile = new EmsDashboardTile();
        List<EmsDashboardTile> emsDashboardTiles = new ArrayList<>();
        emsDashboardTiles.add(emsDashboardTile);
        emsDashboard.setDashboardTileList(emsDashboardTiles);
        List<EmsDashboardTileParams> emsDashboardTileParamses = new ArrayList<>();
        emsDashboardTileParamses.add(new EmsDashboardTileParams());
        emsDashboardTile.setDashboardTileParamsList(emsDashboardTileParamses);
        FacadeUtil.cloneEmsDashboard(emsDashboard);
        FacadeUtil.cloneEmsDashboard(null);
    }

    @Test
    public void testCloneEmsPreference() throws Exception {
        FacadeUtil.cloneEmsPreference(null);
        FacadeUtil.cloneEmsPreference(new EmsPreference());
    }

    @Test
    public void testCloneEmsUserOptions() throws Exception {
        FacadeUtil.cloneEmsUserOptions(new EmsUserOptions());
    }

}