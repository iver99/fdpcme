package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static mockit.Deencapsulation.invoke;
import static org.testng.Assert.*;

/**
 * Created by danfjian on 2016/1/14.
 */
@Test(groups = {"s2"})
public class DashboardAPITest {


    @Mocked
    APIBase mockedAPIBase;
    @Mocked
    DashboardManager mockedDashboardManager;

    DashboardAPI dashboardAPI = new DashboardAPI();

    private void assertCreateDashboard() {
        JSONObject dashboard = new JSONObject();
        assertTrue(dashboardAPI.createDashboard(
                "tenant01",
                "tenant01.emcsadmin",
                "https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101",
                dashboard) instanceof Response);
    }

    @Test
    public void testCreateDashboard() throws Exception {
        new Expectations() {
            {
                mockedAPIBase.initializeUserContext(anyString, anyString);
                result = null;

                mockedDashboardManager.saveNewDashboard(withAny(new Dashboard()), anyLong);
                result = any;

                invoke(dashboardAPI, "updateDashboardAllHref", withAny(new Dashboard()), anyString);
                result = any;
            }
        };
        assertCreateDashboard();
    }

    @Test
    public void testCreateDashboardWithIOException(@Mocked final JsonUtil jsonUtil) throws IOException {
        new Expectations() {
            {
                jsonUtil.fromJson(anyString, Dashboard.class);
                result = new IOException();
            }
        };
        assertCreateDashboard();
    }

    @Test
    public void testCreateDashboardWithDashboardException(@Mocked final JsonUtil jsonUtil) throws IOException, DashboardException {
        new Expectations() {
            {
                mockedDashboardManager.saveNewDashboard(withAny(new Dashboard()), anyLong);
                result = new CommonSecurityException("Test Security Error");
            }
        };
        assertCreateDashboard();
    }

    @Test
    public void testCreateDashboardWithBasicServiceMalfunctionException() throws Exception {
        new Expectations() {
            {
                mockedAPIBase.getTenantId(anyString);
                result = new BasicServiceMalfunctionException(
                        "Test BasicServiceMalfunctionException", "emaas-platform");
            }
        };
        assertCreateDashboard();
    }

    private void assertDeleteDashboard() {
        assertTrue(dashboardAPI.deleteDashboard(
                "tenant01",
                "tenant01.emcsadmin",
                "https://slc09csb.us.oracle.com:4443/emsaasui/emcpdfui/builder.html?dashboardId=1101",
                123L) instanceof Response);
    }

    @Test
    public void testDeleteDashboard() throws Exception {
        new Expectations() {
            {
                mockedDashboardManager.deleteDashboard(anyLong, anyLong);
            }
        };
        assertDeleteDashboard();
    }

    @Test
    public void testDeleteDashboardWithDashboardException() throws Exception {
        new Expectations() {
            {
                mockedDashboardManager.deleteDashboard(anyLong, anyLong);
                result = new CommonSecurityException("Test Security Error");
            }
        };
        assertDeleteDashboard();
    }

    @Test
    public void testDeleteDashboardWithDeleteSystemDashboardException() throws Exception {
        new Expectations() {
            {
                mockedDashboardManager.getDashboardById(anyLong, anyLong);
                result = new Dashboard();
            }
        };
        assertDeleteDashboard();
    }


    @Test
    public void testDeleteDashboardBasicServiceMalfunctionException() throws Exception {
        new Expectations() {
            {
                mockedAPIBase.getTenantId(anyString);
                result = new BasicServiceMalfunctionException(
                        "Test BasicServiceMalfunctionException", "emaas-platform");
            }
        };
        assertDeleteDashboard();
    }

    @Test
    public void testQueryDashboardById() throws Exception {

    }

    @Test
    public void testQueryDashboards() throws Exception {

    }

    @Test
    public void testQuickUpdateDashboard() throws Exception {

    }

    @Test
    public void testUpdateDashboard() throws Exception {

    }
}