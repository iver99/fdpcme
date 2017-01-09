package oracle.sysman.emaas.platform.emcpdf.cache.config;


import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class CacheConfigTest {

    @Test
    public void testCacheConfig(){

        Integer i1=CacheConfig.ADMIN_LINK_CACHE_CAPACITY;
        Integer i2=CacheConfig.ASSET_ROOT_CAPACITY;
        Integer i3=CacheConfig.CLOUD_SERVICE_LINK_CAPACITY;
        Integer i4=CacheConfig.DEFAULT_CAPACITY;
        Integer i5=CacheConfig.DOMAINS_DATA_CAPACITY;
        Integer i6=CacheConfig.HOME_LINK_EXPIRE_CAPACITY;
        Integer i7=CacheConfig.SCREENSHOT_CAPACITY;
        Integer i8=CacheConfig.SERVICE_EXTERNAL_LINK_CAPACITY;
        Integer i9=CacheConfig.SERVICE_INTERNAL_LINK_CAPACITY;
        Integer i10=CacheConfig.TENANT_SUBSCRIBED_SERVICES_CAPACITY;
        Integer i11=CacheConfig.DEFAULT_CAPACITY;
        Integer i12=CacheConfig.SSO_LOGOUT_CAPACITY;
        Integer i13=CacheConfig.VISUAL_ANALYZER_LINK_CAPACITY;
        Integer i14=CacheConfig.VANITY_BASE_URL_CAPACITY;
        Integer i15=CacheConfig.TENANT_APP_MAPPING_CAPACITY;

        Long l1= CacheConfig.ADMIN_LINK_CACHE_EXPIRE_TIME;
        Long l2= CacheConfig.ASSET_ROOT_EXPIRE_TIME;
        Long l3= CacheConfig.CLOUD_SERVICE_LINK_EXPIRE_TIME;
        Long l4= CacheConfig.DEFAULT_EXPIRE_TIME;
        Long l5= CacheConfig.DOMAINS_DATA_EXPIRE_TIME;
        Long l6= CacheConfig.HOME_LINK_EXPIRE_TIME;
        Long l7= CacheConfig.SCREENSHOT_EXPIRE_TIME;
        Long l8= CacheConfig.SERVICE_EXTERNAL_LINK_EXPIRE_TIME;
        Long l9= CacheConfig.SERVICE_INTERNAL_LINK_EXPIRE_TIME;
        Long l10= CacheConfig.TENANT_SUBSCRIBED_SERVICES_EXPIRE_TIME;
        Long l11= CacheConfig.DEFAULT_EXPIRE_TIME;
        Long l12= CacheConfig.SSO_LOGOUT_EXPIRE_TIME;
        Long l13= CacheConfig.VISUAL_ANALYZER_LINK_EXPIRE_TIME;
        Long l14= CacheConfig.VANITY_BASE_URL_EXPIRE_TIME;
        Long l15= CacheConfig.TENANT_APP_MAPPING_EXPIRE_TIME;

    }
}
