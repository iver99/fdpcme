package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheFetchFactory;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by chehao on 2016/12/23.
 */
@Test(groups = { "s2" })
public class LRUCacheManagerTest {

    @Test
    public void testGetInstance(){
        ICacheManager cm= LRUCacheManager.getInstance();
    }
    @Test
    public void testGetCache(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.getCache("cache1");
    }
    @Test
    public void testGetCachedItem1(){
        ICacheManager cm=LRUCacheManager.getInstance();
        Object o1=cm.getCache("cache1").get("two");
        Assert.assertNull(o1);
        cm.getCache("cache1").put("two",new CachedItem("two",2));
        Object o2=cm.getCache("cache1").get("two");
        Assert.assertEquals((Integer)(((CachedItem)o2).getValue()),new Integer(2));
    }
    @Test
    public void testGetCachedItem2(){
        ICacheManager cm=LRUCacheManager.getInstance();
        Object o1=cm.getCache("cache1").get("factoryFetch", new ICacheFetchFactory() {
            @Override
            public Object fetchCachable(Object key) throws Exception {
                return "FromFactory";
            }
        });

        Assert.assertEquals(o1.toString(),"FromFactory");
    }
    @Test
    public void testEviction(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.getCache("cache1");
        Assert.assertNull(cm.getCache("cache1").get("three"));
        cm.getCache("cache1").put("three",new CachedItem("three",3));
        Assert.assertNotNull(cm.getCache("cache1").get("three"));
        cm.getCache("cache1").evict("three");
        Assert.assertNull(cm.getCache("cache1").get("three"));
    }
    @Test
    public void testExpiration(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.getCache("cache2",1000,2000L);
        cm.getCache("cache2").put("four",new CachedItem("four",4));
        Assert.assertNotNull(cm.getCache("cache2").get("four"));
        try {
            Thread.currentThread().sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertNull(cm.getCache("cache2").get("four"));

    }
    @Test
    public void testClear(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.getCache("cache1").clear();
    }
    @Test
    public void testCreateCache1(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.createNewCache("createCache1");
    }
    @Test
    public void testCreateCache2(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.createNewCache("createCache1",1000,1000L);
    }

    @Test
    public void testInit(){
        ICacheManager cm=LRUCacheManager.getInstance();
        cm.init();
    }
    @Test
    public void testClose(){
        ICacheManager cm=LRUCacheManager.getInstance();
        try {
            cm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
