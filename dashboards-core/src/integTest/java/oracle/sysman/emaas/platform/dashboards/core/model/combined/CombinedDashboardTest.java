/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.core.model.combined;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

/**
 * @author guochen
 *
 */
@Test(groups = {"s2"})
public class CombinedDashboardTest {
	@Test
	public void testValueOf() {
		long dashboardId = 1L;
		String prefKey = "prefKey";
		
        EmsDashboard ed  = new EmsDashboard();
        ed .setCreationDate(new Date());
        ed.setDashboardId(dashboardId);
        ed.setDeleted(0L);
        ed.setName("dashboard1");
        ed.setDescription("desc");
        ed.setType(2);
        
        EmsPreference ep = new EmsPreference();
        ep.setPrefKey(prefKey);
        ep.setPrefValue("value");
        ep.setUserName("user");
        
        EmsUserOptions euo = new EmsUserOptions();
        euo.setDashboardId(dashboardId);
        euo.setUserName("user");
        euo.setIsFavorite(1);
        euo.setAutoRefreshInterval(300L);
        
        CombinedDashboard cd = CombinedDashboard.valueOf(ed, ep, euo);
        Assert.assertEquals(cd.getDashboardId(), Long.valueOf(dashboardId));
        Assert.assertEquals(cd.getPreference().getKey(), prefKey);
        Assert.assertEquals(cd.getUserOptions().getDashboardId(), Long.valueOf(dashboardId));
        Assert.assertTrue(cd.getIsFavorite());
	}

}
