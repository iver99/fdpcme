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

import java.math.BigInteger;
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
		BigInteger dashboardId = BigInteger.ONE;
		String prefKey = "prefKey";
		
        EmsDashboard ed  = new EmsDashboard();
        ed .setCreationDate(new Date());
        ed.setDashboardId(dashboardId);
        ed.setDeleted(BigInteger.ZERO);
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
        
        CombinedDashboard cd = CombinedDashboard.valueOf(ed, ep, euo,null);
        Assert.assertEquals(cd.getDashboardId().toString(), dashboardId.toString());
        Assert.assertEquals(cd.getPreference().getKey(), prefKey);
        Assert.assertEquals(cd.getUserOptions().getDashboardId().toString(), dashboardId.toString());
        Assert.assertTrue(cd.getIsFavorite());
	}

}
