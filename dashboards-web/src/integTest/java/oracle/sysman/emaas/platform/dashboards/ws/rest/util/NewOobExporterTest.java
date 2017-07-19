/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;

import org.codehaus.jettison.json.JSONException;
import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
@Test(groups = { "s1" })
public class NewOobExporterTest {
    
    @Test
    public void testExportDashboard() throws JSONException {
        Dashboard db = new Dashboard();
        db.setDashboardId(BigInteger.ONE);
        db.setName("dashboard");
        Tile tile = new Tile();
        db.addTile(tile);
        tile.setTileId("123");
        tile.setTitle("tile");
        List<Dashboard> subDashboards = new ArrayList<Dashboard>();
        Dashboard subDb = new Dashboard();
        subDashboards.add(subDb);
        db.setSubDashboards(subDashboards);
        subDb.setDashboardId(BigInteger.TEN);
        subDb.setName("sub dashboards");
        
        NewOobExporter.exportDashboard(db);
    }
}
