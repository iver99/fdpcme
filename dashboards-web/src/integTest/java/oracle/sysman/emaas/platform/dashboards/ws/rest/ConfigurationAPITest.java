package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

/**
 * @author jishshi
 * @since  1/14/2016.
 */
@Test(groups = {"s2"})
public class ConfigurationAPITest {
    ConfigurationAPI configurationAPI;

    @Mocked
    DependencyStatus dependencyStatus;
    @BeforeMethod
    public void setUp() {
        configurationAPI = new ConfigurationAPI();
    }

    @Test
    public void testGetDiscoveryConfigurations() {
        configurationAPI.getDiscoveryConfigurations("10000", "user.tenant","","");
    }

    @Test
    public void testGetRolesAndPriviledgesForDashboardException(){
        configurationAPI.getRolesAndPriviledges("10000", "user.tenant","","");
    }

    @Test
    public void testGetRolesAndPriviledgesForDashboardException_ENON(){
        new Expectations(){
            {
                DependencyStatus.getInstance();
                result = dependencyStatus;
                dependencyStatus.isEntityNamingUp();
                result = true;
            }
        };
        configurationAPI.getRolesAndPriviledges("10000", "user.tenant","","");
    }

}
