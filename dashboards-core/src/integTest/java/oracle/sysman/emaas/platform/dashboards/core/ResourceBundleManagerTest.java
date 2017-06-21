/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.core;

import java.util.ArrayList;

import javax.persistence.EntityManager;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.entity.EmsResourceBundle;

import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
@Test(groups = {"s1"})
public class ResourceBundleManagerTest {
    private ResourceBundleManager manager = new ResourceBundleManager();
    
    @Test
    public void testRefreshResourceBundleByService(@Mocked final DashboardServiceFacade dsf, @Mocked final EntityManager em) throws CommonResourceException {
        new Expectations() {
            {
                new DashboardServiceFacade();
                result = dsf;
            }
        };
        manager.refreshResourceBundleByService("serviceName", new ArrayList<EmsResourceBundle>());
    }

}
