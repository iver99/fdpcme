package oracle.sysman.emaas.platform.emcpdf.rc;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CacheManagers;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by chehao on 2017/4/18 11:05.
 */
public class RestClientTest {
    private static final String ENTITY_NAMING_DOMAINS_URL = "http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains";
    private static final String TENANT_LOOKUP_URL = "http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/58bfe535-7134-4cae-be4a-d6d3dcfdb4d8/lookups?opcTenantId=emaastesttenant1";


    @Test(groups = {"s2"})
    public void testRestClientGetNull() {
        String res = new RestClient().get(null, null, null);
        Assert.assertNull(res);
    }

    @Test(groups = {"s2"})
    public void testRestClientGetNullAuthS2(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
                                            @Mocked final URI anyUri, @Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
                                            @Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder, @Mocked final StringUtil anyStringUtil)
            throws Exception {
        new NonStrictExpectations() {
            {
                new DefaultClientConfig();
                Client.create(anyClientConfig);
                StringUtil.isEmpty(anyString);
                result = false;
            }
        };
        new RestClient().get("http://test.link.com", "emaastesttenant1", null);
        new Verifications() {
            {
                UriBuilder.fromUri(anyString).build();
                anyClient.resource(anyUri).header(anyString, any);
                anyBuilder.get(String.class);
            }
        };
    }

    @Test(groups = {"s2"})
    public void testRestClientGetS2(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
                                    @Mocked final URI anyUri, @Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
                                    @Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder) {
        new NonStrictExpectations() {
            {
                new DefaultClientConfig();
                Client.create(anyClientConfig);
            }
        };
        new RestClient().get("http://test.link.com", "emaastesttenant1", null);
        new Verifications() {
            {
                UriBuilder.fromUri(anyString).build();
                anyClient.resource(anyUri).header(anyString, any);
                anyBuilder.get(String.class);
            }
        };
    }

    private void cleanCache() {

        ICacheManager cm = CacheManagers.getInstance().build();
        Tenant cacheTenant = new Tenant("emaastesttenant1");
        cm.getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant, new Keys(CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS)));
        cm.getCache(CacheConstants.CACHES_DOMAINS_DATA_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant, new Keys(ENTITY_NAMING_DOMAINS_URL)));
        cm.getCache(CacheConstants.CACHES_DOMAINS_DATA_CACHE).evict(DefaultKeyGenerator.getInstance().generate(cacheTenant, new Keys(TENANT_LOOKUP_URL)));
    }

    @AfterMethod(groups = {"s2"})
    public void afterMethod() {
        cleanCache();
    }

    @BeforeMethod(groups = {"s2"})
    public void beforeMethod() throws Exception {
        cleanCache();
    }
}
