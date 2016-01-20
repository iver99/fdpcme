package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by jishshi on 1/18/2016.
 */
public class DashboardAPIUtilTest {

    private static final String TENANT_NAME = "emaastesttenant1";
    DashboardAPIUtil dashboardAPIUtil;

    @Mocked
    RegistryLookupUtil registryLookupUtil;

    @BeforeMethod
    public void setUp() throws Exception {
        dashboardAPIUtil = new DashboardAPIUtil();
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetExternalDashboardAPIBase() throws Exception {
        Assert.assertNull(dashboardAPIUtil.getExternalDashboardAPIBase(null));
        //TODO
//        Assert.assertNotNull(DashboardAPIUtil.getExternalDashboardAPIBase(TENANT_NAME));
    }

    @Test
    public void testGetExternalPreferenceAPIBase() throws Exception {
        Assert.assertNull(dashboardAPIUtil.getExternalPreferenceAPIBase(null));
        //TODO
//        Assert.assertNotNull(DashboardAPIUtil.getExternalPreferenceAPIBase(TENANT_NAME));
    }
}