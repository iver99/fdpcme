package oracle.sysman.emaas.platform.emcpdf.cache.api;


import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheStatus;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheStatusMessage;

import java.io.Closeable;

/**
 * Created by chehao on 2016/12/19.
 */
public interface ICacheManager extends Closeable {

    public Object getCacheable(String cacheName, Keys keys)throws Exception;

    public Object getCacheable(String cacheName, String key)throws Exception;

    public Object getCacheable(Tenant tenant, String cacheName, Keys keys)throws Exception;

    public Object getCacheable(Tenant tenant, String cacheName, String key)throws Exception;

    public Object getCacheable(String cacheName, Keys keys, ICacheFetchFactory ff)throws Exception;

    public Object getCacheable(Tenant tenant, String cacheName, Keys keys, ICacheFetchFactory ff)throws Exception;

    public Object getCacheable(Tenant tenant, String cacheName, String key, ICacheFetchFactory ff)throws Exception;

    public Object putCacheable(String cacheName, Keys keys, Object value);

    public Object putCacheable(String cacheName, String key, Object value);

    public Object putCacheable(Tenant tenant, String cacheName, Keys keys, Object value);

    public Object putCacheable(Tenant tenant, String cacheName, String key, Object value);

    public Object removeCacheable(String cacheName, Keys keys);

    public Object removeCacheable(Tenant tenant, String cacheName, Keys keys);

    public Object removeCacheable(Tenant tenant, String cacheName, String key);

    /**
     * log out cache current status, request count,hit count, hit rate...etc.
     */
    public void logCacheStatus();

    /**
     * This method will enable cache mechanism
     * Note: This method will init new cache groups according to configuration file
     */
    public CacheStatusMessage enableCache();

    /**
     * This method will disable cache mechanism
     * Note: This method will destroy all existing cache groups and cached elements!
     */
    public CacheStatusMessage disableCache();

    /**
     * This method will suspend cache mechanism, but will not destroy cache groups or cached elements
     */
    public CacheStatusMessage suspendCache();

    /**
     * This method will resume cache mechanism.
     */
    public CacheStatusMessage resumeCache();

    public void init();

    CacheStatus getStatus();


}
