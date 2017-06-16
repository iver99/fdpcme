/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.NewOobExporter;

import org.codehaus.jettison.json.JSONException;
import org.testng.annotations.Test;

/**
 * @author reliang
 */
@Test(groups = { "s1" })
public class InternalToolAPITest {
    private InternalToolAPI api = new InternalToolAPI();
    @Test
    public void testQueryDashboardByName(@Mocked final DependencyStatus anyDependencyStatus, @Mocked final TenantIdProcessor processor,
            @Mocked final DashboardManager mockedDashboardManager) throws BasicServiceMalfunctionException {
        new Expectations() {
            {
                DashboardManager.getInstance();
                result = mockedDashboardManager;
                DependencyStatus.getInstance();
                result = anyDependencyStatus;
                anyDependencyStatus.isDatabaseUp();
                result = true;
                anyDependencyStatus.isEntityNamingUp();
                result = true;
                TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
                result = 1L;
                mockedDashboardManager.getDashboardByName(anyString, anyLong);
                result = new Dashboard();
            }
        };
        api.queryDashboardByName("tenantId", "tenant.user", "dashboardName");
    }
    
    @Test
    public void testQueryDashboardByNameWithDatabaseDependencyUnavailableException(
            @Mocked final DependencyStatus anyDependencyStatus) throws BasicServiceMalfunctionException, DashboardException
    {
        new Expectations() {
            {
                anyDependencyStatus.isDatabaseUp();
                result = false;
            }
        };
        api.queryDashboardByName("tenantId", "tenant.user", "dashboardName");
    }
    
    @Test
    public void testQueryDashboardByNameWithBasicServiceMalfunctionException(@Mocked final DependencyStatus anyDependencyStatus, 
            @Mocked final APIBase mockedAPIBase) throws BasicServiceMalfunctionException, DashboardException
    {
        new Expectations() {
            {
                anyDependencyStatus.isDatabaseUp();
                result = true;
                mockedAPIBase.getTenantId(anyString);
                result = new BasicServiceMalfunctionException("Test BasicServiceMalfunctionException", "emaas-platform");
            }
        };
        api.queryDashboardByName("tenantId", "tenant.user", "dashboardName");
    }
    
    @Test
    public void testQueryDashboardByNameWithJSONException(@Mocked final DependencyStatus anyDependencyStatus, @Mocked final TenantIdProcessor processor,
            @Mocked final DashboardManager mockedDashboardManager, @Mocked NewOobExporter anyExporter) throws BasicServiceMalfunctionException, JSONException {
        new Expectations() {
            {
                DashboardManager.getInstance();
                result = mockedDashboardManager;
                DependencyStatus.getInstance();
                result = anyDependencyStatus;
                anyDependencyStatus.isDatabaseUp();
                result = true;
                anyDependencyStatus.isEntityNamingUp();
                result = true;
                TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
                result = 1L;
                mockedDashboardManager.getDashboardByName(anyString, anyLong);
                result = new Dashboard();
                NewOobExporter.exportDashboard((Dashboard) any);
                result = new JSONException("testQueryDashboardByNameWithJSONException");
            }
        };
        api.queryDashboardByName("tenantId", "tenant.user", "dashboardName");
    }
}
