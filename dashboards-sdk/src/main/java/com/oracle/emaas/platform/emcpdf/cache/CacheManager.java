package com.oracle.emaas.platform.emcpdf.cache;

import com.oracle.emaas.platform.emcpdf.cache.api.ICacheFetchFactory;
import com.oracle.emaas.platform.emcpdf.cache.api.KeyGenerator;
import com.oracle.emaas.platform.emcpdf.cache.util.CacheConstants;
import com.oracle.emaas.platform.emcpdf.cache.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chehao on 2016/12/11.
 */
public class CacheManager {
    private static final Logger LOGGER = LogManager.getLogger(CacheManager.class);

    private static transient long lastLogTime;
    private static boolean isCacheEnabled;
    private static ConcurrentHashMap<String,CacheUnit> cacheUnitMap=new ConcurrentHashMap<String,CacheUnit>();

    private static CacheManager instance = new CacheManager();
    private static final int DEFAULT_EXPIRE_TIME= CacheConfig.DEFAULT_EXPIRE_TIME;
    private static final int DEFAULT_CACHE_UNIT_CAPACITY =CacheConfig.DEFAULT_CAPACITY;

    public static CacheManager getInstance() {
        return instance;
    }

    private KeyGenerator keyGen;

    private CacheManager() {
        this.isCacheEnabled = Boolean.TRUE;
        this.lastLogTime = System.currentTimeMillis();
        keyGen = new DefaultKeyGenerator();
        LOGGER.info("Initialization LRU CacheManager!!");
        getCacheGroup(CacheConstants.CACHES_SCREENSHOT_CACHE, CacheConfig.SCREENSHOT_CAPACITY, CacheConfig.SCREENSHOT_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_ETERNAL_CACHE, CacheConfig.ETERNAL_CAPACITY, CacheConfig.ETERNAL_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_ADMIN_LINK_CACHE, CacheConfig.ADMIN_LINK_CACHE_CAPACITY, CacheConfig.ADMIN_LINK_CACHE_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_CLOUD_SERVICE_LINK_CACHE, CacheConfig.CLOUD_SERVICE_LINK_CAPACITY, CacheConfig.CLOUD_SERVICE_LINK_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_HOME_LINK_CACHE, CacheConfig.HOME_LINK_EXPIRE_CAPACITY, CacheConfig.HOME_LINK_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_VISUAL_ANALYZER_LINK_CACHE, CacheConfig.VISUAL_ANALYZER_LINK_CAPACITY, CacheConfig.VISUAL_ANALYZER_LINK_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_SERVICE_EXTERNAL_LINK_CACHE, CacheConfig.SERVICE_EXTERNAL_LINK_CAPACITY, CacheConfig.SERVICE_EXTERNAL_LINK_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_SERVICE_INTERNAL_LINK_CACHE, CacheConfig.SERVICE_INTERNAL_LINK_CAPACITY, CacheConfig.SERVICE_INTERNAL_LINK_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_VANITY_BASE_URL_CACHE, CacheConfig.VANITY_BASE_URL_CAPACITY, CacheConfig.VANITY_BASE_URL_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_DOMAINS_DATA_CACHE, CacheConfig.DOMAINS_DATA_CAPACITY, CacheConfig.DOMAINS_DATA_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_TENANT_APP_MAPPING_CACHE, CacheConfig.TENANT_APP_MAPPING_CAPACITY, CacheConfig.TENANT_APP_MAPPING_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE, CacheConfig.TENANT_SUBSCRIBED_SERVICES_CAPACITY, CacheConfig.TENANT_SUBSCRIBED_SERVICES_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_SSO_LOGOUT_CACHE, CacheConfig.SSO_LOGOUT_CAPACITY, CacheConfig.SSO_LOGOUT_EXPIRE_TIME);
        getCacheGroup(CacheConstants.CACHES_ASSET_ROOT_CACHE, CacheConfig.ASSET_ROOT_CAPACITY, CacheConfig.ASSET_ROOT_EXPIRE_TIME);

    }

    public CacheUnit getCache(String cacheName) {
        if (StringUtil.isEmpty(cacheName)) {
            return null;
        }
        return getInternalCache(cacheName);
    }

    public Object getCacheable(String cacheName, Keys keys) throws Exception {
        return getCacheable(null, cacheName, keys);
    }

    public Object getCacheable(String cacheName, Keys keys, ICacheFetchFactory ff) throws Exception {
        return getCacheable(null, cacheName, keys, ff);
    }

    public Object getCacheable(String cacheName, String key) throws Exception {
        return getCacheable(cacheName, new Keys(key));
    }

    public Object getCacheable(Tenant tenant, String cacheName, Keys keys) throws Exception {
        return getCacheable(tenant, cacheName, keys, null);
    }

    public Object getCacheable(Tenant tenant, String cacheName, Keys keys, ICacheFetchFactory ff) throws Exception {
        logCacheStatus();
        CacheUnit cache = getInternalCache(cacheName);
        if (cache == null) {
            return null;
        }
        Object key = getInternalKey(tenant, keys);
        if (key == null) {
            return null;
        }
        Object value=null;
        if(isCacheEnabled){
            value = cache.get(key.toString());
        }else{
            //if cache is disabled, we do not retrieve data from cache anymore
            LOGGER.debug("Cache Manager is disabled, will not use the data retrieved from cache");
        }
        if (value == null && ff != null) {
            LOGGER.debug("Cache not retrieved, trying to load with fetch factory");
            value = ff.fetchCachable(key);
            if (value != null && isCacheEnabled) {
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

    public Object getCacheable(Tenant tenant, String cacheName, String key) throws Exception {
        return getCacheable(tenant, cacheName, new Keys(key));
    }

    public Object getCacheable(Tenant tenant, String cacheName, String key, ICacheFetchFactory ff) throws Exception {
        return getCacheable(tenant, cacheName, new Keys(key), ff);
    }

    public Object getInternalKey(Tenant tenant, Keys keys) {
        return keyGen.generate(tenant, keys);
    }


    public Object putCacheable(String cacheName, Keys keys, Object value) {
        return putCacheable(null, cacheName, keys, value);
    }

    public Object putCacheable(String cacheName, String key, Object value) {
        return putCacheable(cacheName, new Keys(key), value);
    }

    public Object putCacheable(Tenant tenant, String cacheName, Keys keys, Object value) {
        if(!isCacheEnabled){
            LOGGER.debug("CacheManager is disabled, will not put element into cache!");
            return null;
        }
        logCacheStatus();
        CacheUnit cache = getInternalCache(cacheName);
        if (cache == null) {
            return null;
        }
        Object key = getInternalKey(tenant, keys);
        if (key == null) {
            return null;
        }
        cache.put(key.toString(), new Element(key, value));
        LOGGER.debug("Cacheable with tenant={}, key={} and value={} is put to cache group {}", tenant, key, value, cacheName);

        return value;
    }

    public Object putCacheable(Tenant tenant, String cacheName, String key, Object value) {
        return putCacheable(tenant, cacheName, new Keys(key), value);
    }

    public Object removeCacheable(String cacheName, Keys keys) {
        return removeCacheable(null, cacheName, keys);
    }

    public Object removeCacheable(Tenant tenant, String cacheName, Keys keys) {
        if(!isCacheEnabled){
            LOGGER.debug("CacheManager is disabled, will not remove element from cache!");
            return null;
        }
        CacheUnit cache = getInternalCache(cacheName);
        if (cache == null) {
            return null;
        }
        Object key = getInternalKey(tenant, keys);
        if (key == null) {
            return null;
        }
        Object obj = cache.get(key.toString());
        cache.remove(key.toString());
        LOGGER.debug("Remove Cacheable with key={} and value={} from cache group {}", key, obj, cacheName);
        return obj;
    }

    public Object removeCacheable(Tenant tenant, String cacheName, String key) {
        return removeCacheable(tenant, cacheName, new Keys(key));
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        keyGen = keyGenerator;
    }

    /**
     * @param cacheName
     * @return
     */
    private CacheUnit getInternalCache(String cacheName) {
        if (cacheName == null) {
            LOGGER.warn("Not retrieved from cache for null cache name");
            return null;
        }
        CacheUnit cache = null;
        try {
            cache = getCacheGroup(cacheName);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        if (cache == null) {
            LOGGER.debug("Not retrieved cache with cache name {}", cacheName);
            return null;
        }
        return cache;
    }

    /**
     * log out current cache group's cache status
     */
    public void logCacheStatus() {
        if(!isCacheEnabled){
            LOGGER.warn("Cache Manager is disabled, will not log cache status!");
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

    public void disableCacheManager() {
        synchronized (this) {
            isCacheEnabled = Boolean.FALSE;
        }
    }

    public void enableCacheManager() {
        synchronized (this) {
            isCacheEnabled = Boolean.TRUE;
        }

    }

    public static CacheUnit getCacheGroup(String cacheName){
        return getCacheGroup(cacheName,DEFAULT_CACHE_UNIT_CAPACITY,DEFAULT_EXPIRE_TIME);
    }


    public static CacheUnit getCacheGroup(String cacheName,int capacity,int timeToLive){
        if(cacheName ==null){
            LOGGER.debug("CacheManager:Cache name cannot be null!");
            throw new IllegalArgumentException("Cache name cannot be null!");
        }
        if("".equals(cacheName)){
            LOGGER.debug("CacheManager:Cache name cannot be empty!");
            throw new IllegalArgumentException("Cache name cannot be empty!");
        }
        CacheUnit cu=cacheUnitMap.get(cacheName);
        if(cu == null){
            return createCacheUnit(cacheName,capacity, timeToLive);
        }else{
            return cu;
        }

    }

    private static CacheUnit createCacheUnit(String cacheName,int capacity,int timeToLive){
        CacheUnit cu=new CacheUnit(cacheName,capacity,timeToLive);
        cacheUnitMap.put(cacheName, cu);
        LOGGER.debug("CacheManager:Cache named: {},timeToLive time: {} has been created.", cacheName, timeToLive);
        return cu;
    }


    public static ConcurrentHashMap<String, CacheUnit> getCacheUnitMap() {
        return cacheUnitMap;
    }

    public static void clearAllCacheGroup(){
        if(cacheUnitMap!=null){
            Iterator<Map.Entry<String,CacheUnit>> it =cacheUnitMap.entrySet().iterator();
            while(it.hasNext()){
                CacheUnit cu=it.next().getValue();
                cu.getCacheUnitStatus().setEvictionCount(0L);
                cu.getCacheUnitStatus().setHitCount(0L);
                cu.getCacheUnitStatus().setRequestCount(0L);
                cu.getCacheUnitStatus().setUsage(0);
                cu.getCacheLinkedHashMap().clear();
            }
        }
    }
}
