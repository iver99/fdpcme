/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.entity;

import java.math.BigInteger;

import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
@Test(groups = {"s1"})
public class EmsDashboardPKTest {
    
    @Test
    public void testEmsDashboardPK() {
        EmsDashboardPK emsDashboardPK = new EmsDashboardPK(BigInteger.ONE, 1L);
        emsDashboardPK.getDashboardId();
        emsDashboardPK.getTenantId();
        emsDashboardPK.setDashboardId(BigInteger.ONE);
        emsDashboardPK.setTenantId(1L);
        
        EmsDashboardPK emsDashboardPKNew = new EmsDashboardPK(BigInteger.ONE, 1L);
        emsDashboardPKNew.equals(emsDashboardPK);
    }
}
