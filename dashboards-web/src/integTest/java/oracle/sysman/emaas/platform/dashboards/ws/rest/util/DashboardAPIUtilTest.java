package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/18/2016.
 */
@Test(groups = {"s1"})
public class DashboardAPIUtilTest {

    DashboardAPIUtil dashboardAPIUtil;

    @Mocked
    RegistryLookupUtil registryLookupUtil;

    @BeforeMethod
    public void setUp() throws Exception {
        dashboardAPIUtil = new DashboardAPIUtil();
    }

    @Test
    public void testGetExternalDashboardAPIBase() throws Exception {
        Assert.assertNull(DashboardAPIUtil.getExternalDashboardAPIBase(null));
    }

    @Test
    public void testGetExternalPreferenceAPIBase() throws Exception {
        Assert.assertNull(DashboardAPIUtil.getExternalPreferenceAPIBase(null));
    }
}