/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.webutils.metadata;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.ResourceBundleManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.entity.EmsResourceBundle;

import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
public class MetadataStorerTest
{
    private List<EmsResourceBundle> initNls() {
        List<EmsResourceBundle> nlsList = new ArrayList<EmsResourceBundle>();
        EmsResourceBundle rb1 = new EmsResourceBundle();
        nlsList.add(rb1);
        rb1.setLanguageCode("en");
        rb1.setCountryCode("US");
        rb1.setLastModificationDate(new Date());
        rb1.setServiceName("serviceName");
        rb1.setServiceVersion("1.0+");
        rb1.setPropertiesFile("something");
        return nlsList;
    }
    
    private List<Dashboard> initOOBDashboards() {
        List<Dashboard> oobList = new ArrayList<Dashboard>();
        Dashboard oobDashboard1 = new Dashboard();
        oobList.add(oobDashboard1);
        oobDashboard1.setDashboardId(new BigInteger("2"));
        oobDashboard1.setName("Performance Analytics: Database");
        oobDashboard1.setApplicationType(2);
        oobDashboard1.setAppicationType(DashboardApplicationType.ITAnalytics);
        oobDashboard1.setCreationDate(new Date());
        oobDashboard1.setIsSystem(Boolean.TRUE);
        oobDashboard1.setType(Dashboard.DASHBOARD_TYPE_NORMAL);
        
        Dashboard oobDashboard2 = new Dashboard();
        oobList.add(oobDashboard2);
        oobDashboard2.setDashboardId(new BigInteger("3"));
        oobDashboard2.setName("Performance Analytics: Database Set");
        oobDashboard2.setApplicationType(2);
        oobDashboard2.setAppicationType(DashboardApplicationType.ITAnalytics);
        oobDashboard2.setCreationDate(new Date());
        oobDashboard2.setIsSystem(Boolean.TRUE);
        oobDashboard2.setType(Dashboard.DASHBOARD_TYPE_SET);
        List<Dashboard> subDashboards = new ArrayList<Dashboard>();
        subDashboards.add(oobDashboard1);
        oobDashboard2.setSubDashboards(subDashboards);
        return oobList;
    }
    
    @Test(groups = { "s1" })
    public void testStoreOobDashboards(@Mocked final DashboardServiceFacade dsf, @Mocked final EntityManager em) 
            throws DashboardException {
        new Expectations() {
            {
                new DashboardServiceFacade(anyLong);
                result = dsf;
                dsf.getEmsDashboardById((BigInteger) any);
                result = null;
                dsf.getEmsDashboardByNameAndDescriptionAndOwner(anyString, anyString, anyString);
                result = null;
            }
        };
        MetadataStorer storer = new MetadataStorer();
        storer.storeOobDashboards(initOOBDashboards());
    }
    
    @Test(groups = { "s1" })
    public void testStoreResourceBundle(@Mocked final ResourceBundleManager rbm, @Mocked final DashboardServiceFacade dsf,
            @Mocked final EntityManager em)
    {
        new Expectations() {
            {
                ResourceBundleManager.getInstance();
                result = rbm;
            }
        };
        MetadataStorer storer = new MetadataStorer();
        storer.storeResourceBundle(initNls());
    }
}
