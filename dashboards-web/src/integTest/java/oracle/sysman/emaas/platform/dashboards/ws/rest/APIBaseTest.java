package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.util.DashboardAPIUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by danfjian on 2016/1/14.
 */
@Test(groups = {"s2"})
public class APIBaseTest {

    APIBase apiBase;

    @Mocked TenantIdProcessor tenantIdProcessor;

    @BeforeMethod
    public void initApiBase() {
        apiBase = new APIBase();
    }

    @Test
    public void testBuildErrorResponseWithNullErrorEntity() throws Exception {
        assertNull(apiBase.buildErrorResponse(null));
    }

    @Test
    public void testBuildErrorResponse() throws Exception {
        ErrorEntity entity = new ErrorEntity(400, "Test Error");
        assertEquals(apiBase.buildErrorResponse(entity).getStatus(), 400);
    }

    @Test
    public void testClearUserContext(@Mocked final UserContext userCtx, @Mocked final TenantContext tenantCtx) throws Exception {
        new Expectations() {
            {
                UserContext.clearCurrentUser();
                TenantContext.clearCurrentUser();
            }
        };
        apiBase.clearUserContext();
    }

    @Test
    public void testGetJsonUtil() throws Exception {
        assertNotNull(apiBase.getJsonUtil());
    }

    @Test(expectedExceptions = CommonSecurityException.class)
    public void testGetTenantIdWithNullTenantId() throws Exception {
        apiBase.getTenantId(null);
    }

    @Test
    public void testGetTenantIdWithCorrectTenantId() throws Exception {
        new Expectations() {
            {
                TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
                result = 12315;
            }
        };
        assertEquals(apiBase.getTenantId("tenant01").longValue(), 12315);
    }

    @Test(expectedExceptions = BasicServiceMalfunctionException.class)
    public void testGetTenantIdWithBasicServiceMalfunctionExceptionThrown() throws Exception {
        new Expectations() {
            {
                TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
                result = new BasicServiceMalfunctionException("Mockup exception for testing exception handling in APIBase#getTenantId", "emaas-platform");
            }
        };
        apiBase.getTenantId("tenant01");
    }

    @Test(expectedExceptions = CommonSecurityException.class)
    public void testGetTenantIdWithOtherExceptionThrown() throws Exception {
        new Expectations() {
            {
                TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
                result = new NullPointerException("Mockup exception for testing exception handling in APIBase#getTenantId");
            }
        };
        apiBase.getTenantId("tenant01");
    }

    @Test(expectedExceptions = CommonSecurityException.class)
    public void testInitializeUserContextWithNullOpcTenant() throws Exception {
        apiBase.initializeUserContext(null, "tenant01.emcsadmin");
    }

    @Test(expectedExceptions = CommonSecurityException.class)
    public void testInitializeUserContextWithNullUserTenant() throws Exception {
        apiBase.initializeUserContext("tenantopc01", "");
    }

    @Test(expectedExceptions = CommonSecurityException.class)
    public void testInitializeUserContextWithInvalidUserTenant1() throws Exception {
        apiBase.initializeUserContext("tenantopc01", "tenant01");
    }

    @Test(expectedExceptions = CommonSecurityException.class)
    public void testInitializeUserContextWithInvalidUserTenant2() throws Exception {
        apiBase.initializeUserContext("tenantopc01", "tenant01.");
    }

    @Test(expectedExceptions = CommonSecurityException.class)
    public void testInitializeUserContextWithInvalidUserTenant3() throws Exception {
        apiBase.initializeUserContext("tenantopc01", ".emcsadmin");
    }

    @Test
    public void testInitializeUserContextCorrect() throws Exception {
        apiBase.initializeUserContext("tenantopc01", "tenant01.emcsadmin");
    }


    @Test
    public void testInfoInteractionLogAPIIncomingCall() throws Exception {
        apiBase.infoInteractionLogAPIIncomingCall("tenant01.emcsadmin", "emaas-platform", "Test Log");
    }

    @Test
    public void testUpdateDashboardHrefWithNullDBD() throws Exception {
        assertNull(apiBase.updateDashboardHref(null, "tenant01"));
    }

    @Test
    public void testUpdateDashboardHrefWithNullTenantName() throws Exception {
        assertNull(apiBase.updateDashboardHref(new Dashboard(), null));
    }

    @Test
    public void testUpdateDashboardHrefCorrect(@Mocked final DashboardAPIUtil dashboardAPIUtil) throws Exception {
        Dashboard dbd = new Dashboard();
        dbd.setDashboardId(87654L);
        new Expectations() {
            {
                dashboardAPIUtil.getExternalDashboardAPIBase(anyString);
                result = "http://external/";
            }
        };
        dbd = apiBase.updateDashboardHref(dbd, "tenant01");
        assertEquals(dbd.getHref(), "http://external/87654");
    }
}