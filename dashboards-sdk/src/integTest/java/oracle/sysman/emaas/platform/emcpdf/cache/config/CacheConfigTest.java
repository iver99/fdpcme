package oracle.sysman.emaas.platform.emcpdf.cache.config;


import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class CacheConfigTest {

    @Test
    public void testCacheConfig(){

        Integer i1=CacheConfig.ADMIN_LINK_CACHE_CAPACITY;
        Assert.assertNotNull(i1);
        Integer i2=CacheConfig.ASSET_ROOT_CAPACITY;
        Assert.assertNotNull(i2);
        Integer i3=CacheConfig.CLOUD_SERVICE_LINK_CAPACITY;
        Assert.assertNotNull(i3);
        Integer i4=CacheConfig.DEFAULT_CAPACITY;
        Assert.assertNotNull(i4);
        Integer i5=CacheConfig.DOMAINS_DATA_CAPACITY;
        Assert.assertNotNull(i5);
        Integer i6=CacheConfig.HOME_LINK_EXPIRE_CAPACITY;
        Assert.assertNotNull(i6);
        Integer i7=CacheConfig.SCREENSHOT_CAPACITY;
        Assert.assertNotNull(i7);
        Integer i8=CacheConfig.SERVICE_EXTERNAL_LINK_CAPACITY;
        Assert.assertNotNull(i8);
        Integer i9=CacheConfig.SERVICE_INTERNAL_LINK_CAPACITY;
        Assert.assertNotNull(i9);
        Integer i10=CacheConfig.TENANT_SUBSCRIBED_SERVICES_CAPACITY;
        Assert.assertNotNull(i10);
        Integer i11=CacheConfig.DEFAULT_CAPACITY;
        Assert.assertNotNull(i11);
        Integer i12=CacheConfig.SSO_LOGOUT_CAPACITY;
        Assert.assertNotNull(i12);
        Integer i13=CacheConfig.VISUAL_ANALYZER_LINK_CAPACITY;
        Assert.assertNotNull(i13);
        Integer i14=CacheConfig.VANITY_BASE_URL_CAPACITY;
        Assert.assertNotNull(i14);
        Integer i15=CacheConfig.TENANT_APP_MAPPING_CAPACITY;
        Assert.assertNotNull(i15);

        Long l1= CacheConfig.ADMIN_LINK_CACHE_EXPIRE_TIME;
        Assert.assertNotNull(l1);
        Long l2= CacheConfig.ASSET_ROOT_EXPIRE_TIME;
        Assert.assertNotNull(l2);
        Long l3= CacheConfig.CLOUD_SERVICE_LINK_EXPIRE_TIME;
        Assert.assertNotNull(l3);
        Long l4= CacheConfig.DEFAULT_EXPIRE_TIME;
        Assert.assertNotNull(l4);
        Long l5= CacheConfig.DOMAINS_DATA_EXPIRE_TIME;
        Assert.assertNotNull(l5);
        Long l6= CacheConfig.HOME_LINK_EXPIRE_TIME;
        Assert.assertNotNull(l6);
        Long l7= CacheConfig.SCREENSHOT_EXPIRE_TIME;
        Assert.assertNotNull(l7);
        Long l8= CacheConfig.SERVICE_EXTERNAL_LINK_EXPIRE_TIME;
        Assert.assertNotNull(l8);
        Long l9= CacheConfig.SERVICE_INTERNAL_LINK_EXPIRE_TIME;
        Assert.assertNotNull(l9);
        Long l10= CacheConfig.TENANT_SUBSCRIBED_SERVICES_EXPIRE_TIME;
        Assert.assertNotNull(l10);
        Long l11= CacheConfig.DEFAULT_EXPIRE_TIME;
        Assert.assertNotNull(l11);
        Long l12= CacheConfig.SSO_LOGOUT_EXPIRE_TIME;
        Assert.assertNotNull(l12);
        Long l13= CacheConfig.VISUAL_ANALYZER_LINK_EXPIRE_TIME;
        Assert.assertNotNull(l13);
        Long l14= CacheConfig.VANITY_BASE_URL_EXPIRE_TIME;
        Assert.assertNotNull(l14);
        Long l15= CacheConfig.TENANT_APP_MAPPING_EXPIRE_TIME;
        Assert.assertNotNull(l15);

    }
}
