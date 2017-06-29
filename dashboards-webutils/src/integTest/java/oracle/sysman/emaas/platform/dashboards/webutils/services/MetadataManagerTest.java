/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.webutils.services;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
public class MetadataManagerTest {
    MetadataManager mm = new MetadataManager();
    
    @Test(groups = { "s1" })
    public void testGetName() {
        Assert.assertEquals("Metadata Manager Service", mm.getName());
    }
    
    @Test(groups = { "s1" })
    public void testPostStart() {
        try {
            mm.postStart(null);
        }
        catch (Exception e) {
            
        }
    }
}
