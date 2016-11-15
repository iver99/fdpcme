package oracle.sysman.emaas.platform.dashboards.core.cache;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.cache.lru.CacheFactory;
import oracle.sysman.emaas.platform.dashboards.core.cache.lru.CacheUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2016/11/1.
 */
@Test(groups = {"s2"})
public class CacheManagerTest_S2 {
    private CacheManager cacheManager;
    @Mocked
    ResourceBundle resourceBundle;
    @Test
    public void testGetCache() throws Exception {
        new  Expectations(){
            {
                resourceBundle.getString(anyString);
                result = 1;
            }
        };
        cacheManager= CacheManager.getInstance();
        cacheManager.getCache("");
        cacheManager.getCache("name");
    }

    @Test
    public void testGetCacheable() throws Exception {
        cacheManager.getCacheable("cacheName",new Keys());
    }
    @Mocked
    ICacheFetchFactory  iCacheFetchFactory;
    @Test
    public void testGetCacheable1() throws Exception {
        cacheManager.getCacheable("cacheName", new Keys(), iCacheFetchFactory);
    }

    @Test
    public void testGetCacheable2() throws Exception {
        cacheManager.getCacheable("cacheName", "key");
    }

    @Mocked
    Tenant tenant;
    @Test
    public void testGetCacheable3() throws Exception {
        cacheManager.getCacheable(tenant, "cachename", "key");
    }

    @Test
    public void testGetCacheable4() throws Exception {
        cacheManager.getCacheable(tenant, "cachename", "key", iCacheFetchFactory);
    }

    @Test
    public void testPutCacheable() throws Exception {
        cacheManager.putCacheable("cachename", new Keys(), "value");
        cacheManager.removeCacheable("cachename", new Keys());
    }

    @Test
    public void testPutCacheable1() throws Exception {
        cacheManager.putCacheable("cachename", "key", "value");
        cacheManager.removeCacheable("cachename",new Keys("key") );
    }

    @Test
    public void testPutCacheable2() throws Exception {
        cacheManager.putCacheable(tenant, "cachename", "key", "value");
        cacheManager.removeCacheable(tenant, "cachename",new Keys("key") );
    }

    @Test
    public void testRemoveCacheable() throws Exception {
        cacheManager.removeCacheable("cachename", new Keys());
    }

    @Test
    public void testRemoveCacheable1() throws Exception {
        cacheManager.removeCacheable(tenant, "cachename", "key");
    }

}