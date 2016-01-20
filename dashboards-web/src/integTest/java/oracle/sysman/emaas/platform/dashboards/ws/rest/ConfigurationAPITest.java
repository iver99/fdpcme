package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Deencapsulation;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

/**
 * Created by jishshi on 1/14/2016.
 */
@Test(groups = {"s1"})
public class ConfigurationAPITest {


    ConfigurationAPI configurationAPI;

    @BeforeClass
    public void setUp() throws Exception {
        configurationAPI = new ConfigurationAPI();
    }

    public void testGetDiscoveryConfigurations() {
        Response discoveryConfigurations1 = configurationAPI.getDiscoveryConfigurations("tenantIdParam", "userTenant", "referer", "sessionExpiryTime");
        Assert.assertNotNull(discoveryConfigurations1);
        Response discoveryConfigurations2 = configurationAPI.getDiscoveryConfigurations("tenantIdParam", null, "referer", "sessionExpiryTime");
        Assert.assertNotNull(discoveryConfigurations2);
    }

    @Test
    public void testGetDiscoveryConfigurations1(){
        Deencapsulation.setField(ConfigurationAPI.class, "responseError", null);
        Assert.assertNotNull(configurationAPI.getDiscoveryConfigurations("", null, null, null));
    }

    @Test
    public void testGetDiscoveryConfigurations2(){
        Deencapsulation.setField(ConfigurationAPI.class, "responseError", null);
        Assert.assertNotNull(configurationAPI.getDiscoveryConfigurations("tenantIdParam", "userTenant", "referer", "sessionExpiryTime"));
    }

}