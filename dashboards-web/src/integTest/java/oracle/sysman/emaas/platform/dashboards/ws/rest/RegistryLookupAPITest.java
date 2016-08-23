package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.*;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.core.util.EndpointEntity;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = {"s2"})
public class RegistryLookupAPITest {

    RegistryLookupAPI registryLookupAPI;

    @BeforeMethod
    public void setUp() {
        registryLookupAPI = new RegistryLookupAPI();

        new NonStrictExpectations() {
            {

            }
        };
    }

    @Test
    public void testGetRegistryLink() {
        // Test 403 Exception;
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", "userTenant", "refer", "serviceName", "version").getStatus(), 403);

        //Test 404 Exception with validUserTenant;
        String validUserTenant = "userTenant.userName";
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version").getStatus(), 404);

        //Test 404 Exception with validUserTenant;
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", null).getStatus(), 404);

        //Test 404 empty ServiceName
        String emptyServiceName = "";
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", emptyServiceName, "version").getStatus(), 404);

    }

    @Test
    public void testGetRegistryLink1(@Mocked final RegistryLookupUtil registryLookupUtil) {
        //Test 200 valid endpoint
        String validUserTenant = "userTenant.userName";
        new Expectations() {{
            RegistryLookupUtil.getServiceExternalEndPointEntity(anyString, anyString, anyString);
            result = withAny(new EndpointEntity(anyString, anyString, anyString));
        }};
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version").getStatus(), 200);
    }


    @Test
    public void testGetRegistryLink2() {
        // Test 403 Exception;
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", "userTenant", "refer", "serviceName", "version", "rel").getStatus(), 403);

        //Test 404 Exception with validUserTenant;
        String validUserTenant = "userTenant.userName";
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 404);

        //Test 404 Exception with validUserTenant;
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 404);

        //Test 404  null ServiceName
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", null, "version", "rel").getStatus(), 404);

        //Test 404  null versionName
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", null, "rel").getStatus(), 404);

        //Test 404 null rel
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", null).getStatus(), 404);
    }

    @Test
    public void testGetRegistryLink3(@Mocked final RegistryLookupUtil registryLookupUtil) {
        //Test 200 valid endpoint
        String validUserTenant = "userTenant.userName";
        new Expectations() {{
            RegistryLookupUtil.getServiceExternalLink(anyString, anyString, anyString, anyString);
            result = withAny(new Link());
        }};

        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 200);
    }

    @Test
    public void testGetRegistryLinkWithRelPrefix4() {
        String validUserTenant = "userTenant.userName";
        new MockUp<RegistryLookupUtil>() {
            @Mock
            public EndpointEntity getServiceExternalEndPointEntity(String serviceName, String version, String tenantName) throws Exception {
                throw new Exception("exception from getDefaultBundleString");
            }

            @Mock
            public Link getServiceExternalLink(String serviceName, String version, String rel, String tenantName) throws Exception {
                throw new Exception("exception from getDefaultBundleString");
            }

        };
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version").getStatus(), 503);
        Assert.assertEquals(registryLookupAPI.getRegistryLink("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 503);
    }

    @Test
    public void testGetRegistryLinkWithRelPrefix() {
        // Test 403 Exception;
        Assert.assertEquals(registryLookupAPI.getRegistryLinkWithRelPrefix("tenantIdParam", "userTenant", "refer", "serviceName", "version", "rel").getStatus(), 403);

        //Test 404 Exception with validUserTenant;
        String validUserTenant = "userTenant.userName";
        Assert.assertEquals(registryLookupAPI.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 404);

        //Test 404 Exception with validUserTenant;
        Assert.assertEquals(registryLookupAPI.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 404);

        //Test 404  null ServiceName
        Assert.assertEquals(registryLookupAPI.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", null, "version", "rel").getStatus(), 404);

        //Test 404  null versionName
        Assert.assertEquals(registryLookupAPI.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", null, "rel").getStatus(), 404);

        //Test 404 null rel
        Assert.assertEquals(registryLookupAPI.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", null).getStatus(), 404);
    }

    @Test
    public void testGetRegistryLinkWithRelPrefix1(@Mocked final RegistryLookupUtil registryLookupUtil) {
        //Test 200 valid endpoint
        String validUserTenant = "userTenant.userName";
        new Expectations() {{
            RegistryLookupUtil.getServiceExternalLinkWithRelPrefix(anyString, anyString, anyString, anyString);
            result = withAny(new Link());
        }};

        Assert.assertEquals(registryLookupAPI.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 200);
    }

    @Test
    public void testGetRegistryLinkWithRelPrefix2() {
        String validUserTenant = "userTenant.userName";
        new MockUp<RegistryLookupUtil>() {
            @Mock
            public Link getServiceExternalLinkWithRelPrefix(String serviceName, String version, String
                    rel, String tenantName) throws Exception {
                throw new Exception("exception from getServiceExternalLinkWithRelPrefix");
            }
        };
        Assert.assertEquals(registryLookupAPI.getRegistryLinkWithRelPrefix("tenantIdParam", validUserTenant, "refer", "serviceName", "version", "rel").getStatus(), 503);
    }

}