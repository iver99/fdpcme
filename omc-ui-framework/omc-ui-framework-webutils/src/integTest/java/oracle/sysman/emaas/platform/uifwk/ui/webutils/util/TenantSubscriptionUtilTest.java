package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

import com.sun.jersey.api.client.WebResource;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by guochen on 1/18/17.
 */
public class TenantSubscriptionUtilTest {
    @Test(groups = { "s2" })
    public void testGetTenantSubscribedServicesFromCache(@Mocked final CacheManagers cms, @Mocked final ICacheManager cm, @Mocked final ICache cacheUtil) throws ExecutionException {
        new Expectations() {
            {
                CacheManagers.getInstance().build();
                result = cm;
                cm.getCache(anyString);
                result = cacheUtil;
                cacheUtil.get(any);
                result = "{subscribedApplicationsData}";
            }
        };
        String subscribedAppData = TenantSubscriptionUtil.getTenantSubscribedServices("tenant", "user");
        Assert.assertEquals(subscribedAppData, "{subscribedApplicationsData}");
    }

    @Test(groups = { "s2" })
    public void testGetTenantSubscribedServicesFromService(@Mocked final RegistryLookupUtil registryUtil,
                                               @Mocked final RegistrationManager rm, @Mocked final WebResource.Builder builder)
    {
        new Expectations() {
            {
                RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.subscribedapps", null);
                result = null;
            }
        };
        String subscribedApps = TenantSubscriptionUtil.getTenantSubscribedServices("tenant", "user");
        Assert.assertNull(subscribedApps);

        new Expectations() {
            {
                RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.subscribedapps", null);
                result = new Link();
                RegistrationManager.getInstance().getAuthorizationToken();
                result = "Basic Auth".toCharArray();
                builder.get(String.class);
                result = "{subscribedApplicationsData}";
            }
        };
        subscribedApps = TenantSubscriptionUtil.getTenantSubscribedServices("tenant", "user");
        Assert.assertNull(subscribedApps);

        final Link lk = new Link();
        lk.withHref("http://slc10uan.us.oracle.com:7019/emcpdf/api/v1/subscribedapps");
        new Expectations() {
            {
                RegistryLookupUtil.getServiceInternalLink("Dashboard-API", "1.0+", "static/dashboards.subscribedapps", null);
                result = lk;
            }
        };
        subscribedApps = TenantSubscriptionUtil.getTenantSubscribedServices("tenant", "user");
        Assert.assertEquals(subscribedApps, "{subscribedApplicationsData}");
    }
}
