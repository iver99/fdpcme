package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Deencapsulation;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

/**
 * @author jishshi
 * @since  1/14/2016.
 */
@Test(groups = {"s1"})
public class ConfigurationAPITest {


    ConfigurationAPI configurationAPI;

    @BeforeMethod
    public void setUp() {
        configurationAPI = new ConfigurationAPI();
    }

    @Test
    public void testGetDiscoveryConfigurations() {
        Response discoveryConfigurations1 = configurationAPI.getDiscoveryConfigurations("tenantIdParam", "userTenant", "referer", "sessionExpiryTime");
        Assert.assertNotNull(discoveryConfigurations1);
        Response discoveryConfigurations2 = configurationAPI.getDiscoveryConfigurations("tenantIdParam", null, "referer", "sessionExpiryTime");
        Assert.assertNotNull(discoveryConfigurations2);
    }

    //@Test
    //public void testGetDiscoveryConfigurations1(){
        //Deencapsulation.setField(ConfigurationAPI.class, "responseError", null);
        //Assert.assertNotNull(configurationAPI.getDiscoveryConfigurations("", null, null, null));
    //}

    //@Test
    //public void testGetDiscoveryConfigurations2(){
        //Deencapsulation.setField(ConfigurationAPI.class, "responseError", null);
        //Assert.assertNotNull(configurationAPI.getDiscoveryConfigurations("tenantIdParam", "userTenant", "referer", "sessionExpiryTime"));
    //}

}
