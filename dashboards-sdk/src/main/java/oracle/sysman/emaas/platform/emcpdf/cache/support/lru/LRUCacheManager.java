package oracle.sysman.emaas.platform.emcpdf.cache.support.lru;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.config.CacheConfig;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by chehao on 2016/12/22.
 */
public class LRUCacheManager extends AbstractCacheManager{

    private static final Logger LOGGER = LogManager.getLogger(LRUCacheManager.class);
    private static LRUCacheManager instance =new LRUCacheManager();

    private LRUCacheManager() {
        init();
    }

    public static LRUCacheManager getInstance(){
        return instance;
    }
    @Override
    public ICache createNewCache(String name, Integer capacity, Long timeToLive) {
        ICache<Object,Object> cache =new LinkedHashMapCache(name,capacity,timeToLive);
        return cache;
    }
    @Override
    public ICache createNewCache(String name){
      return this.createNewCache(name, CacheConfig.DEFAULT_CAPACITY,CacheConfig.DEFAULT_EXPIRE_TIME);
    }

    /**
     * Return a collection of the cache names known by this manager.
     *
     * @return the names of all caches known by the cache manager
     */
    @Override
    public void init() {
        super.init();
        //init default cache group
        LOGGER.info("Initialing LRU CacheManager...");
        getCache(CacheConstants.CACHES_ADMIN_LINK_CACHE, CacheConfig.ADMIN_LINK_CACHE_CAPACITY, CacheConfig.ADMIN_LINK_CACHE_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_CLOUD_SERVICE_LINK_CACHE, CacheConfig.CLOUD_SERVICE_LINK_CAPACITY, CacheConfig.CLOUD_SERVICE_LINK_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_HOME_LINK_CACHE, CacheConfig.HOME_LINK_EXPIRE_CAPACITY, CacheConfig.HOME_LINK_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_VISUAL_ANALYZER_LINK_CACHE, CacheConfig.VISUAL_ANALYZER_LINK_CAPACITY, CacheConfig.VISUAL_ANALYZER_LINK_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_SERVICE_EXTERNAL_LINK_CACHE, CacheConfig.SERVICE_EXTERNAL_LINK_CAPACITY, CacheConfig.SERVICE_EXTERNAL_LINK_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_SERVICE_INTERNAL_LINK_CACHE, CacheConfig.SERVICE_INTERNAL_LINK_CAPACITY, CacheConfig.SERVICE_INTERNAL_LINK_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_VANITY_BASE_URL_CACHE, CacheConfig.VANITY_BASE_URL_CAPACITY, CacheConfig.VANITY_BASE_URL_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_DOMAINS_DATA_CACHE, CacheConfig.DOMAINS_DATA_CAPACITY, CacheConfig.DOMAINS_DATA_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_TENANT_APP_MAPPING_CACHE, CacheConfig.TENANT_APP_MAPPING_CAPACITY, CacheConfig.TENANT_APP_MAPPING_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE, CacheConfig.TENANT_SUBSCRIBED_SERVICES_CAPACITY, CacheConfig.TENANT_SUBSCRIBED_SERVICES_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_SSO_LOGOUT_CACHE, CacheConfig.SSO_LOGOUT_CAPACITY, CacheConfig.SSO_LOGOUT_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_ASSET_ROOT_CACHE, CacheConfig.ASSET_ROOT_CAPACITY, CacheConfig.ASSET_ROOT_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_REGISTRY_CACHE, CacheConfig.REGISTRY_CAPACITY, CacheConfig.REGISTRY_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_TENANT_USER_CACHE, CacheConfig.TENANT_USER_CAPACITY, CacheConfig.TENANT_USER_EXPIRE_TIME);
        getCache(CacheConstants.CACHES_OOB_DASHBOARD_SAVEDSEARCH_CACHE, CacheConfig.OOB_DASHBOARD_SAVEDSEARCH_CAPACITY, CacheConfig.OOB_DASHBOARD_SAVEDSEARCH_EXPIRE_TIME);
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        LOGGER.info("LRU CacheManager is closing...");
        super.close();
    }
}
