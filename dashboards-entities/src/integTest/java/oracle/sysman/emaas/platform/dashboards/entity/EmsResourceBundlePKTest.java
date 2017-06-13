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

import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
@Test(groups = {"s1"})
public class EmsResourceBundlePKTest {
    @Test
    public void testEmsResourceBundlePK() {
        EmsResourceBundlePK rbPK = new EmsResourceBundlePK();
        rbPK.getCountryCode();
        rbPK.getLanguageCode();
        rbPK.getServiceName();
        rbPK.setCountryCode("countryCode");
        rbPK.setLanguageCode("languageCode");
        rbPK.setServiceName("serviceName");
        rbPK.equals(rbPK);
    }
}
