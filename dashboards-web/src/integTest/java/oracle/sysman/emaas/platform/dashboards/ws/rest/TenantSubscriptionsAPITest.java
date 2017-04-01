package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.resourcemgmt.response.Response;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.subscription2.TenantSubscriptionInfo;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition.ServiceEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition.TenantDetailEntity;

import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = {"s2"})
public class TenantSubscriptionsAPITest {
    TenantSubscriptionsAPI tenantSubscriptionsAPI;

    @BeforeMethod
    public void setUp() {
        tenantSubscriptionsAPI = new TenantSubscriptionsAPI();

    }

    @Test
    public void testGetSubscribedApplications(@Mocked final APIBase apiBase) {
        Assert.assertNotNull(tenantSubscriptionsAPI.getSubscribedApplications("tenantIdParam", "userTenant", "userTenant", null));
        Assert.assertNotNull(tenantSubscriptionsAPI.getSubscribedApplications("tenantIdParam", "userTenant", "userTenant", "true"));

    }

    @Test
    public void testGetSubscribedApplications1(@Mocked final DependencyStatus anyDependencyStatus,@Mocked final APIBase apiBase, @Mocked final TenantSubscriptionUtil tenantSubscriptionUtil) {
        new Expectations() {
            {
//            	anyDependencyStatus.isEntityNamingUp();
//            	result=true;
                List<String> apps = new ArrayList<>();
                apps.add("DBD");
                TenantSubscriptionUtil.getTenantSubscribedServices(anyString,(TenantSubscriptionInfo)any);
                result = apps;
            }
        };

        Assert.assertNotNull(tenantSubscriptionsAPI.getSubscribedApplications("tenantIdParam", "userTenant", "userTenant", null));

    }

    @Test
    public void testGetSubscribedApplications2() {

        Assert.assertNotNull(tenantSubscriptionsAPI.getSubscribedApplications("", "userTenant", "userTenant", null));

    }

    @Test
    public void testGetSubscribedApplications3(@Mocked final RestClient restClient, @Mocked final Link link) {
        new MockUp<APIBase>() {
            @Mock
            public void initializeUserContext(String opcTenantId, String userTenant) throws CommonSecurityException {
                //do nothing
            }
        };
        Assert.assertNotNull(tenantSubscriptionsAPI.getSubscribedApplications("", "userTenant", "userTenant", "true"));
    }

    @Test
    public void testGetSubscribedApplications4(@Mocked final ServiceEntity serviceEntity, @Mocked final TenantDetailEntity tenantDetailEntity, @Mocked final JsonUtil jsonUtil, @Mocked final RestClient restClient, @Mocked final Link link, @Mocked final RegistryLookupUtil registryLookupUtil) throws IOException {

        new MockUp<APIBase>() {
            @Mock
            public void initializeUserContext(String opcTenantId, String userTenant) throws CommonSecurityException {
                //do nothing
            }
        };

        Assert.assertNotNull(tenantSubscriptionsAPI.getSubscribedApplications("", "userTenant", "userTenant", "true"));
        Assert.assertNotNull(tenantSubscriptionsAPI.getSubscribedApplications("", "userTenant", "userTenant", "true"));
    }

    @Test(groups = {"s1"})
    public  void testSubscribedAppsEntity () throws  Exception{
        List<String> list = new ArrayList<>();
        list.add("appSample");
        TenantSubscriptionsAPI.SubscribedAppsEntity<String> stringSubscribedAppsEntity = new TenantSubscriptionsAPI.SubscribedAppsEntity<>(null);
        stringSubscribedAppsEntity.setApplications(null);
        Assert.assertNull(stringSubscribedAppsEntity.getApplications());
        stringSubscribedAppsEntity.setApplications(list);
        Assert.assertEquals(stringSubscribedAppsEntity.getApplications(),list);

    }

}