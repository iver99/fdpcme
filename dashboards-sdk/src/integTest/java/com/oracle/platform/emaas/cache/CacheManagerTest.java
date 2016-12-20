package com.oracle.platform.emaas.cache;

/**
 * Created by chehao on 2016/12/13.
 */

import com.oracle.platform.emaas.cache.api.ICacheFetchFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
@Test(groups = {"s2"})
public class CacheManagerTest
{
	/*@Test
	public void testCacheExpires() throws Exception
	{
		String putValue = "value";
		CacheManager cm = CacheManager.getInstance();
		cm.putCacheable("lookupCache", new Keys("test"), putValue);
		String value = (String) cm.getCacheable("lookupCache", new Keys("test"));
		Assert.assertEquals(value, putValue);
		Thread.sleep(8500);
		value = (String) cm.getCacheable("lookupCache", new Keys("test"));
		Assert.assertNull(value);
	}*/

	/*@Test
	public void testNotExistsCache()
	{
		CacheManager cm = CacheManager.getInstance();
		CacheUnit nc = cm.getCache("NotExistingCache");
		Assert.assertNull(nc);
	}*/

    @Test
    public void testPutGetRemoveCacheable() throws Exception
    {
        final Integer cachedValue = 1;
        CacheManager cm = CacheManager.getInstance();
        cm.putCacheable("eternalCache", new Keys("test"), cachedValue);
        Integer value = (Integer) cm.getCacheable("eternalCache", new Keys("test"));
        Assert.assertEquals(value, Integer.valueOf(1));
        value = (Integer) cm.getCacheable("eternalCache", new Keys("test not exists"));
        Assert.assertNull(value);
        cm.removeCacheable("eternalCache", new Keys("test"));
        value = (Integer) cm.getCacheable("eternalCache", new Keys("test"));
        Assert.assertNull(value);
        value = (Integer) cm.getCacheable("eternalCache", new Keys("test"), new ICacheFetchFactory() {
            @Override
            public Object fetchCachable(Object key) throws Exception
            {
                return cachedValue;
            }

        });
        Assert.assertEquals(value, cachedValue);
        value = (Integer) cm.getCacheable("eternalCache", new Keys("test"));
        Assert.assertEquals(value, Integer.valueOf(1));
        cm.putCacheable("eternalCache", "test2", null);
        Assert.assertNull(cm.getCacheable("eternalCache", new Keys("test2")));
    }

    @Test
    public void testPutGetRemoveCacheableDifferentThread() throws Exception
    {
        CacheManager cm = CacheManager.getInstance();
        cm.putCacheable("eternalCache", new Keys("test thread"), 1);
        Integer value = (Integer) cm.getCacheable("eternalCache", new Keys("test thread"));
        Assert.assertEquals(value, Integer.valueOf(1));

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                CacheManager cm = CacheManager.getInstance();
                Integer value;
                try {
                    value = (Integer) cm.getCacheable("eternalCache", new Keys("test thread"));
                    Assert.assertEquals(value, Integer.valueOf(1));
                    value = (Integer) cm.getCacheable("eternalCache", new Keys("test not exists"));
                    Assert.assertNull(value);
                    cm.removeCacheable("eternalCache", new Keys("test thread"));
                    value = (Integer) cm.getCacheable("eternalCache", new Keys("test thread"));
                    Assert.assertNull(value);
                }
                catch (Exception e) {
                    Assert.fail(e.getLocalizedMessage(), e);
                }
            }
        }) {
        }.start();
    }

    @Test
    public void testPutGetRemoveCacheableWithTenant() throws Exception
    {
        final Integer cachedValue = 1;
        Tenant tenant1 = new Tenant("tenant1");
        Tenant tenant2 = new Tenant("tenant2");
        CacheManager cm = CacheManager.getInstance();
        cm.putCacheable(tenant1, "eternalCache", new Keys("test"), cachedValue);
        Integer value = (Integer) cm.getCacheable(tenant2, "eternalCache", new Keys("test not exists"));
        Assert.assertNull(value);
        cm = CacheManager.getInstance();
        value = (Integer) cm.getCacheable(tenant1, "eternalCache", new Keys("test"));
        Assert.assertEquals(value, cachedValue);
        cm.removeCacheable(tenant1, "eternalCache", new Keys("test"));
        value = (Integer) cm.getCacheable(tenant1, "eternalCache", new Keys("test"));
        Assert.assertNull(value);
        value = (Integer) cm.getCacheable(tenant1, "eternalCache", new Keys("test"), new ICacheFetchFactory() {
            @Override
            public Object fetchCachable(Object key) throws Exception
            {
                return cachedValue;
            }

        });
        Assert.assertEquals(value, cachedValue);
        value = (Integer) cm.getCacheable(tenant1, "eternalCache", new Keys("test"));
        Assert.assertEquals(value, cachedValue);
    }


}

