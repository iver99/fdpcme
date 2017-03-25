package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LinkedHashMapCache;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/2/23 12:35.
 */
@Test(groups = {"s2"})
public class AbstractCacheTest {

    @Test
    public void testSimpleCacheCounter() {
//        AbstractCache.SimpleCacheCounter simpleCacheCounter = new AbstractCache.SimpleCacheCounter();
        AbstractCache cache = new LinkedHashMapCache("cache", 1, 1L);
        cache.cacheCounter.setEvictionCount(1L);
        cache.cacheCounter.setHitCount(1L);
        cache.cacheCounter.setRequestCount(1L);

        Assert.assertEquals(1L, cache.cacheCounter.getEvictionCount());
        Assert.assertEquals(1L, cache.cacheCounter.getRequestCount());
        Assert.assertEquals(1L, cache.cacheCounter.getHitCount());
    }

    @Test
    public void testGetHitRate() {
        AbstractCache cache = new LinkedHashMapCache("cache", 1, 1L);
        cache.cacheCounter.setHitCount(1L);
        cache.cacheCounter.setRequestCount(1L);
        Assert.assertEquals("100.00%", cache.cacheCounter.getHitRate());

        cache.cacheCounter.setHitCount(1L);
        cache.cacheCounter.setRequestCount(2L);

        Assert.assertEquals("50.00%", cache.cacheCounter.getHitRate());

        cache.cacheCounter.setHitCount(1L);
        cache.cacheCounter.setRequestCount(3L);

        Assert.assertEquals("33.33%", cache.cacheCounter.getHitRate());

        cache.cacheCounter.setHitCount(47L);
        cache.cacheCounter.setRequestCount(100L);

        Assert.assertEquals("47.00%", cache.cacheCounter.getHitRate());

        cache.cacheCounter.setHitCount(47L);
        cache.cacheCounter.setRequestCount(20L);

        Assert.assertEquals("100.00%", cache.cacheCounter.getHitRate());

        cache.cacheCounter.setHitCount(40L);
        cache.cacheCounter.setRequestCount(60L);

        Assert.assertEquals("66.67%", cache.cacheCounter.getHitRate());
    }
}
