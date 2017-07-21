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

import java.util.Date;

import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
@Test(groups = {"s1"})
public class EmsResourceBundleTest {

    @Test
    public void testEmsResourceBundle() {
        EmsResourceBundle rb = new EmsResourceBundle();
        rb.getCountryCode();
        rb.getLanguageCode();
        rb.getLastModificationDate();
        rb.getPropertiesFile();
        rb.getServiceName();
        rb.getServiceVersion();
        rb.setCountryCode("countryCode");
        rb.setLanguageCode("languageCode");
        rb.setLastModificationDate(new Date());
        rb.setPropertiesFile("propertiesFile");
        rb.setServiceName("serviceName");
        rb.setServiceVersion("serviceVersion");
    }
}
