package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LinkedHashMapCache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.screenshot.LRUScreenshotCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chehao on 2017/3/1 10:36.
 */
@Path("/v1/cache")
public class CacheAPI {
    private static final Logger LOGGER = LogManager.getLogger(CacheAPI.class);

    @PUT
    @Path("/stopCache")
    public String stopCache() {
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeCacheGroupStatus(lruCacheManager,CacheStatus.CLOSED);
        AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
        changeCacheGroupStatus(screenshotCacheManager,CacheStatus.CLOSED);
        try {
            lruCacheManager.close();
            screenshotCacheManager.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

    @PUT
    @Path("/startCache")
    public String startCache() {
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeCacheGroupStatus(lruCacheManager,CacheStatus.AVAILABLE);
        AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
        changeCacheGroupStatus(screenshotCacheManager,CacheStatus.AVAILABLE);
        return "Cache is started!";
    }

    @PUT
    @Path("/suspendCache")
    public String suspendCache() {
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeCacheGroupStatus(lruCacheManager,CacheStatus.SUSPEND);
        AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
        changeCacheGroupStatus(screenshotCacheManager,CacheStatus.SUSPEND);
        return "Cache is suspended!";
    }

    @PUT
    @Path("/resumeCache")
    public String resumeCache() {
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeCacheGroupStatus(lruCacheManager,CacheStatus.AVAILABLE);
        AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
        changeCacheGroupStatus(screenshotCacheManager,CacheStatus.AVAILABLE);
        return "Cache is resumed!";
    }

    private void changeCacheGroupStatus(AbstractCacheManager cacheManager, CacheStatus cacheStatus) {
        ConcurrentMap<String, ICache> map = cacheManager.getCacheMap();
        if (map == null) {
            LOGGER.error("CacheMap in CacheManagers is null!");
            return;
        }
        for (ICache value : map.values()) {
            LinkedHashMapCache cache = (LinkedHashMapCache) value;
            cache.setCacheStatus(cacheStatus);
            LOGGER.info("Cache group {} status has been changed to {}", cache.getName(), cacheStatus);
        }
    }
}
