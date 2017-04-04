package oracle.sysman.emaas.platform.emcpdf.cache.support.screenshot;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LinkedHashMapCache;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by chehao on 2016/12/26.
 */
public class LRUScreenshotCacheManager extends AbstractCacheManager{
    private static final Logger LOGGER = LogManager.getLogger(LRUScreenshotCacheManager.class);
    private static LRUScreenshotCacheManager instance =new LRUScreenshotCacheManager();
    public static LRUScreenshotCacheManager getInstance(){
        return instance;
    }
    private LRUScreenshotCacheManager() {
        init();
    }

    @Override
    public ICache createNewCache(String name, Integer capacity, Long timeToLive) {
        ICache<Object,Object> cache =new LinkedHashMapCache(name,capacity,timeToLive);
        return cache;
    }

    @Override
    public ICache createNewCache(String name) {
        return this.createNewCache(name, CacheConstants.DEFAULT_CAPACITY,CacheConstants.DEFAULT_EXPIRATION);
    }

    @Override
    public void init() {
        super.init();
        /*LOGGER.info("Initialing Screenshot CacheManager....");
        getCache(CacheConstants.CACHES_SCREENSHOT_CACHE, CacheConfig.SCREENSHOT_CAPACITY, CacheConfig.SCREENSHOT_EXPIRE_TIME);*/
    }

    @Override
    public void close() throws IOException {
        super.close();
        LOGGER.info("LRU Screenshot CacheManager is closing...");
    }

}
