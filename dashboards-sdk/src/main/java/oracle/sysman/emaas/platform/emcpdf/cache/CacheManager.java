package oracle.sysman.emaas.platform.emcpdf.cache;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheFetchFactory;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.config.CacheConfig;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.CacheUnit;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Element;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheManagerUtil;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheStatus;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheStatusMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chehao on 2016/12/11.
 */
public class CacheManager implements ICacheManager {

    private static final Logger LOGGER = LogManager.getLogger(CacheManager.class);

    private static transient long lastLogTime;

    private CacheStatus cacheStatus;

    private ConcurrentHashMap<String, CacheUnit> cacheUnitMap;

    private static CacheManager instance = new CacheManager();

    public static CacheManager getInstance() {
        return instance;
    }

    private CacheManager() {
        //init cache manager
        init();
    }

    @Override
    public synchronized void init() {
        cacheStatus = CacheStatus.UNINITIALIZED;
        cacheUnitMap = new ConcurrentHashMap<String, CacheUnit>();
        this.lastLogTime = System.currentTimeMillis();
        LOGGER.info("Initialing LRU CacheManager..");
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_SCREENSHOT_CACHE, CacheConfig.SCREENSHOT_CAPACITY, CacheConfig.SCREENSHOT_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_ETERNAL_CACHE, CacheConfig.ETERNAL_CAPACITY, CacheConfig.ETERNAL_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_ADMIN_LINK_CACHE, CacheConfig.ADMIN_LINK_CACHE_CAPACITY, CacheConfig.ADMIN_LINK_CACHE_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_CLOUD_SERVICE_LINK_CACHE, CacheConfig.CLOUD_SERVICE_LINK_CAPACITY, CacheConfig.CLOUD_SERVICE_LINK_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_HOME_LINK_CACHE, CacheConfig.HOME_LINK_EXPIRE_CAPACITY, CacheConfig.HOME_LINK_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_VISUAL_ANALYZER_LINK_CACHE, CacheConfig.VISUAL_ANALYZER_LINK_CAPACITY, CacheConfig.VISUAL_ANALYZER_LINK_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_SERVICE_EXTERNAL_LINK_CACHE, CacheConfig.SERVICE_EXTERNAL_LINK_CAPACITY, CacheConfig.SERVICE_EXTERNAL_LINK_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_SERVICE_INTERNAL_LINK_CACHE, CacheConfig.SERVICE_INTERNAL_LINK_CAPACITY, CacheConfig.SERVICE_INTERNAL_LINK_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_VANITY_BASE_URL_CACHE, CacheConfig.VANITY_BASE_URL_CAPACITY, CacheConfig.VANITY_BASE_URL_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_DOMAINS_DATA_CACHE, CacheConfig.DOMAINS_DATA_CAPACITY, CacheConfig.DOMAINS_DATA_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_TENANT_APP_MAPPING_CACHE, CacheConfig.TENANT_APP_MAPPING_CAPACITY, CacheConfig.TENANT_APP_MAPPING_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE, CacheConfig.TENANT_SUBSCRIBED_SERVICES_CAPACITY, CacheConfig.TENANT_SUBSCRIBED_SERVICES_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_SSO_LOGOUT_CACHE, CacheConfig.SSO_LOGOUT_CAPACITY, CacheConfig.SSO_LOGOUT_EXPIRE_TIME);
        CacheManagerUtil.createCacheUnit(CacheConstants.CACHES_ASSET_ROOT_CACHE, CacheConfig.ASSET_ROOT_CAPACITY, CacheConfig.ASSET_ROOT_EXPIRE_TIME);
        cacheStatus = CacheStatus.AVAILABLE;
    }

    /**
     * This method will disable cache mechanism
     * Note: This method will destroy all existing cache groups and cached elements!
     */
    @Override
    public CacheStatusMessage disableCache() {
        if(cacheStatus.equals(CacheStatus.CLOSED)){
            return new CacheStatusMessage(cacheStatus,CacheConstants.NO_NEED_TO_CHANGE_CACHE_STATUS);
        }
        if(cacheStatus.equals(CacheStatus.UNINITIALIZED)){
            return new CacheStatusMessage(cacheStatus,CacheConstants.CAN_NOT_CHANGE_CACHE_STATUS);
        }
        synchronized (this) {
            try {
                close();
            } catch (IOException e) {
                LOGGER.error(e.getLocalizedMessage());
            }
            return new CacheStatusMessage(cacheStatus,CacheConstants.CHANGE_CACHE_STATUS_SUCCESSFULLY);
        }
    }

    /**
     * This method will enable cache mechanism
     * Note: This method will init new cache groups according to configuration file
     */
    @Override
    public CacheStatusMessage enableCache() {
        if(cacheStatus.equals(CacheStatus.AVAILABLE)){
            return new CacheStatusMessage(cacheStatus,CacheConstants.NO_NEED_TO_CHANGE_CACHE_STATUS);
        }
        if(cacheStatus.equals(CacheStatus.SUSPEND) || cacheStatus.equals(CacheStatus.UNINITIALIZED)){
            return new CacheStatusMessage(cacheStatus,CacheConstants.CAN_NOT_CHANGE_CACHE_STATUS);
        }
        synchronized (this) {
            init();
            return new CacheStatusMessage(cacheStatus,CacheConstants.CHANGE_CACHE_STATUS_SUCCESSFULLY);
        }

    }

    /**
     * This method will suspend cache mechanism, but will not destroy cache groups or cached elements
     */
    @Override
    public CacheStatusMessage suspendCache() {
        if(cacheStatus.equals(CacheStatus.SUSPEND)){
            return new CacheStatusMessage(cacheStatus,CacheConstants.NO_NEED_TO_CHANGE_CACHE_STATUS);
        }
        if(cacheStatus.equals(CacheStatus.CLOSED) || cacheStatus.equals(CacheStatus.UNINITIALIZED)){
            return new CacheStatusMessage(cacheStatus,CacheConstants.CAN_NOT_CHANGE_CACHE_STATUS);
        }
        synchronized (this) {
            cacheStatus = CacheStatus.SUSPEND;
            return new CacheStatusMessage(cacheStatus,CacheConstants.CHANGE_CACHE_STATUS_SUCCESSFULLY);
        }
    }

    /**
     * This method will resume cache mechanism.
     */
    @Override
    public CacheStatusMessage resumeCache() {
        if(cacheStatus.equals(CacheStatus.AVAILABLE)){
            return new CacheStatusMessage(cacheStatus,CacheConstants.NO_NEED_TO_CHANGE_CACHE_STATUS);
        }
        if(cacheStatus.equals(CacheStatus.CLOSED) || cacheStatus.equals(CacheStatus.UNINITIALIZED)){
            return new CacheStatusMessage(cacheStatus,CacheConstants.CAN_NOT_CHANGE_CACHE_STATUS);
        }
        synchronized (this) {
            cacheStatus = CacheStatus.AVAILABLE;
            return new CacheStatusMessage(cacheStatus,CacheConstants.CHANGE_CACHE_STATUS_SUCCESSFULLY);
        }
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public synchronized void close() throws IOException {
        CacheManagerUtil.clearAllCacheGroup();
        cacheStatus = CacheStatus.CLOSED;
    }

    @Override
    public CacheStatus getStatus() {
        return cacheStatus;
    }

    @Override
    public Object getCacheable(String cacheName, Keys keys) throws Exception {
        return getCacheable(null, cacheName, keys);
    }

    @Override
    public Object getCacheable(String cacheName, Keys keys, ICacheFetchFactory ff) throws Exception {
        return getCacheable(null, cacheName, keys, ff);
    }

    @Override
    public Object getCacheable(String cacheName, String key) throws Exception {
        return getCacheable(cacheName, new Keys(key));
    }

    @Override
    public Object getCacheable(Tenant tenant, String cacheName, Keys keys) throws Exception {
        return getCacheable(tenant, cacheName, keys, null);
    }

    @Override
    public Object getCacheable(Tenant tenant, String cacheName, Keys keys, ICacheFetchFactory ff) throws Exception {
        logCacheStatus();
        CacheUnit cache = CacheManagerUtil.getInternalCache(cacheName);
        if (cache == null) {
            return null;
        }
        Object key = CacheManagerUtil.getInternalKey(tenant, keys);
        if (key == null) {
            return null;
        }
        Object value = null;
        if (cacheStatus.equals(CacheStatus.AVAILABLE)) {
            value = cache.get(key.toString());
        } else {
            //if cache is disabled, we do not retrieve data from cache anymore
            LOGGER.debug("Cache Manager is not available, will not use the data retrieved from cache");
        }
        if (value == null && ff != null) {
            LOGGER.debug("Cache not retrieved, trying to load with fetch factory");
            value = ff.fetchCachable(key);
            if (value != null && cacheStatus.equals(CacheStatus.AVAILABLE)) {
                cache.put(key.toString(), new Element(key, value));
                LOGGER.debug("Successfully fetched data, putting to cache group {}", cacheName);
            }
        }
        if (value == null) {
            LOGGER.debug("Not retrieved cache with cache name {} and key {} for tenant {} from cache group {}", cacheName, key, tenant, cacheName);
            return null;
        }
        LOGGER.debug("Retrieved cacheable with key={} and value={} for tenant={} from cache group {}", key, value, tenant, cacheName);
        return value;
    }

    @Override
    public Object getCacheable(Tenant tenant, String cacheName, String key) throws Exception {
        return getCacheable(tenant, cacheName, new Keys(key));
    }

    @Override
    public Object getCacheable(Tenant tenant, String cacheName, String key, ICacheFetchFactory ff) throws Exception {
        return getCacheable(tenant, cacheName, new Keys(key), ff);
    }

    @Override
    public Object putCacheable(String cacheName, Keys keys, Object value) {
        return putCacheable(null, cacheName, keys, value);
    }

    @Override
    public Object putCacheable(String cacheName, String key, Object value) {
        return putCacheable(cacheName, new Keys(key), value);
    }

    @Override
    public Object putCacheable(Tenant tenant, String cacheName, Keys keys, Object value) {
        if (!cacheStatus.equals(CacheStatus.AVAILABLE)) {
            LOGGER.debug("CacheManager is not available, will not put element into cache!");
            return null;
        }
        logCacheStatus();
        CacheUnit cache = CacheManagerUtil.getInternalCache(cacheName);
        if (cache == null) {
            return null;
        }
        Object key = CacheManagerUtil.getInternalKey(tenant, keys);
        if (key == null) {
            return null;
        }
        cache.put(key.toString(), new Element(key, value));
        LOGGER.debug("Cacheable with tenant={}, key={} and value={} is put to cache group {}", tenant, key, value, cacheName);

        return value;
    }

    @Override
    public Object putCacheable(Tenant tenant, String cacheName, String key, Object value) {
        return putCacheable(tenant, cacheName, new Keys(key), value);
    }

    @Override
    public Object removeCacheable(String cacheName, Keys keys) {
        return removeCacheable(null, cacheName, keys);
    }

    @Override
    public Object removeCacheable(Tenant tenant, String cacheName, Keys keys) {
        if (!cacheStatus.equals(CacheStatus.AVAILABLE)) {
            LOGGER.debug("CacheManager is not available, will not remove element from cache!");
            return null;
        }
        CacheUnit cache = CacheManagerUtil.getInternalCache(cacheName);
        if (cache == null) {
            return null;
        }
        Object key = CacheManagerUtil.getInternalKey(tenant, keys);
        if (key == null) {
            return null;
        }
        Object obj = cache.get(key.toString());
        cache.remove(key.toString());
        LOGGER.debug("Remove Cacheable with key={} and value={} from cache group {}", key, obj, cacheName);
        return obj;
    }

    @Override
    public Object removeCacheable(Tenant tenant, String cacheName, String key) {
        return removeCacheable(tenant, cacheName, new Keys(key));
    }

    /**
     * log out current cache group's cache status
     */
    @Override
    public void logCacheStatus() {
        if (!cacheStatus.equals(CacheStatus.AVAILABLE)) {
            LOGGER.warn("Cache Manager is not available, will not log cache status!");
            return;
        }
        long now = System.currentTimeMillis();
        long logInterval = CacheConfig.LOG_INTERVAL;
        if (now - lastLogTime >= logInterval) {
            for (Map.Entry<String, CacheUnit> e : getCacheUnitMap().entrySet()) {
                CacheUnit cu = e.getValue();
                LOGGER.info("[Cache Status] Cache name is [{}], "
                                + "Cache capacity is [{}], "
                                + "Cache usage is [{}], "
                                + "Cache usage rate is [{}], " +
                                "Cache total request count is [{}], "
                                + "Cache hit count is [{}], "
                                + "Cache hit rate is [{}], "
                                + "Eviction Count is [{}]",
                        cu.getName(),
                        cu.getCacheCapacity(),
                        cu.getCacheUnitStatus().getUsage(),
                        cu.getCacheUnitStatus().getUsageRate(),
                        cu.getCacheUnitStatus().getRequestCount(),
                        cu.getCacheUnitStatus().getHitCount(),
                        cu.getCacheUnitStatus().getHitRate(),
                        cu.getCacheUnitStatus().getEvictionCount());
            }
            this.lastLogTime = now;
        }

    }

    public ConcurrentHashMap<String, CacheUnit> getCacheUnitMap() {
        return cacheUnitMap;
    }

    public void setCacheUnitMap(ConcurrentHashMap<String, CacheUnit> cacheUnitMap) {
        this.cacheUnitMap = cacheUnitMap;
    }
}
