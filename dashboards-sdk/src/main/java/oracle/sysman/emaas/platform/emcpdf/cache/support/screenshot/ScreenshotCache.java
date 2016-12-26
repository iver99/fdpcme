/*
package oracle.sysman.emaas.platform.emcpdf.cache.support.screenshot;

import oracle.sysman.emaas.platform.emcpdf.cache.config.CacheConfig;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.CachedItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;

*/
/**
 * Created by chehao on 2016/12/26.
 *//*

public class ScreenshotCache extends AbstractCache{

    Logger LOGGER= LogManager.getLogger(ScreenshotCache.class);

    private LinkedHashMap<Object, CachedItem> cacheMap;
    private String name;
    private Integer capacity;
    private Long timeToLive;
    private Long creationTime;

    public ScreenshotCache(String name, Integer capacity, Long timeToLive){
        this.name=name;
        this.capacity = capacity;
        this.timeToLive=timeToLive;
        this.creationTime=System.currentTimeMillis();
        int hashTableSize = (int) Math.ceil(capacity/0.75f) + 1;
        cacheMap = new LinkedHashMap<Object,CachedItem>(hashTableSize, 0.75f, true) {//ordered by access time
            private static final long serialVersionUID = 1L;
            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<Object,CachedItem> eldest) {
                return size() > ScreenshotCache.this.capacity;
            }
        };
        LOGGER.info("Cache group named {} is created with capacity {} and timeToLive {}",name,capacity,timeToLive);
    }

    public ScreenshotCache(String name) {
        this(name, CacheConfig.DEFAULT_CAPACITY,CacheConfig.DEFAULT_EXPIRE_TIME);
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public void evict(Object key) {
        cacheMap.remove(key);
        LOGGER.debug("Cached Item with key {} is evicted from cache group {}",key,name);
    }

    @Override
    public void clear() {
        cacheMap.clear();
    }

    @Override
    public String getName() {
        return getName();
    }

    @Override
    public boolean isExpired(CachedItem cachedItem) {
        return false;
    }

    @Override
    protected CachedItem lookup(Object key) {
        return cacheMap.get(key);
    }

}
*/
