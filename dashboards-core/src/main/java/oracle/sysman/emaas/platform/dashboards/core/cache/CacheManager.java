/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.cache;

import oracle.sysman.emaas.platform.dashboards.core.cache.lru.CacheFactory;
import oracle.sysman.emaas.platform.dashboards.core.cache.lru.CacheUnit;
import oracle.sysman.emaas.platform.dashboards.core.cache.lru.Element;
import oracle.sysman.emaas.platform.dashboards.core.util.StringUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author guochen ## for more information about cache,you can find it in the wiki below ##
 *         https://confluence.oraclecorp.com/confluence/pages/viewpage.action?pageId=177111759
 */
public class CacheManager
{
	private static final Logger LOGGER = LogManager.getLogger(CacheManager.class);

	public static final String CACHES_SCREENSHOT_CACHE = "screenshotCache";
	public static final String CACHES_ETERNAL_CACHE = "eternalCache";
	public static final String CACHES_ADMIN_LINK_CACHE = "adminLinkCache";
	public static final String CACHES_CLOUD_SERVICE_LINK_CACHE = "cloudServiceLinkCache";
	public static final String CACHES_HOME_LINK_CACHE = "homeLinkCache";
	public static final String CACHES_VISUAL_ANALYZER_LINK_CACHE = "visualAnalyzerLinkCache";
	public static final String CACHES_SERVICE_EXTERNAL_LINK_CACHE = "externalLinkCache";
	public static final String CACHES_SERVICE_INTERNAL_LINK_CACHE = "internalLinkCache";
	public static final String CACHES_VANITY_BASE_URL_CACHE = "vanityBaseUrlCache";
	public static final String CACHES_DOMAINS_DATA_CACHE = "domainsDataCache";
	public static final String CACHES_TENANT_APP_MAPPING_CACHE = "tenantAppMappingCache";
	public static final String CACHES_SSO_LOGOUT_CACHE = "SsoLogoutCache";
	public static final String CACHES_SUBSCRIBED_SERVICE_CACHE = "subscribeCache";
	public static final String CACHES_ASSET_ROOT_CACHE = "assetRootCache";


	public static final String LOOKUP_CACHE_KEY_SUBSCRIBED_APPS = "subscribedApps";
	public static final String LOOKUP_CACHE_KEY_EXTERNAL_LINK = "externalLink";
	public static final String LOOKUP_CACHE_KEY_INTERNAL_LINK = "internalLink";
	public static final String LOOKUP_CACHE_KEY_VANITY_BASE_URL = "vanityBaseUrl";
	public static final String LOOKUP_CACHE_KEY_CLOUD_SERVICE_LINKS = "cloudServiceLinks";
	public static final String LOOKUP_CACHE_KEY_ADMIN_LINKS = "adminLinks";
	public static final String LOOKUP_CACHE_KEY_HOME_LINKS = "homeLinks";
	public static final String LOOKUP_CACHE_KEY_VISUAL_ANALYZER = "visualAnalyzer";
	public static final String LOOKUP_CACHE_KEY_ASSET_ROOTS = "assetRoots";
	public static final String LOOKUP_CACHE_KEY_SSO_LOGOUT_URL = "ssoLogoutUrl";

	private static transient long lastLogTime;
	ResourceBundle conf = ResourceBundle.getBundle("cache_config");
	private static CacheManager instance = new CacheManager();

	public static CacheManager getInstance()
	{
		return instance;
	}

	private KeyGenerator keyGen;

	private CacheManager()
	{
		this.lastLogTime=System.currentTimeMillis();
		keyGen = new DefaultKeyGenerator();
		LOGGER.info("Initialization LRU CacheManager!!");
		CacheFactory.getCache(CACHES_SCREENSHOT_CACHE, CacheConfig.SCREENSHOT_CAPACITY, CacheConfig.SCREENSHOT_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_ETERNAL_CACHE,CacheConfig.ETERNAL_CAPACITY, CacheConfig.ETERNAL_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_ADMIN_LINK_CACHE, CacheConfig.ADMIN_LINK_CACHE_CAPACITY, CacheConfig.ADMIN_LINK_CACHE_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_CLOUD_SERVICE_LINK_CACHE, CacheConfig.CLOUD_SERVICE_LINK_CAPACITY, CacheConfig.CLOUD_SERVICE_LINK_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_HOME_LINK_CACHE, CacheConfig.HOME_LINK_EXPIRE_CAPACITY, CacheConfig.HOME_LINK_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_VISUAL_ANALYZER_LINK_CACHE, CacheConfig.VISUAL_ANALYZER_LINK_CAPACITY, CacheConfig.VISUAL_ANALYZER_LINK_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_SERVICE_EXTERNAL_LINK_CACHE, CacheConfig.SERVICE_EXTERNAL_LINK_CAPACITY, CacheConfig.SERVICE_EXTERNAL_LINK_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_SERVICE_INTERNAL_LINK_CACHE, CacheConfig.SERVICE_INTERNAL_LINK_CAPACITY, CacheConfig.SERVICE_INTERNAL_LINK_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_VANITY_BASE_URL_CACHE, CacheConfig.VANITY_BASE_URL_CAPACITY, CacheConfig.VANITY_BASE_URL_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_DOMAINS_DATA_CACHE, CacheConfig.DOMAINS_DATA_CAPACITY, CacheConfig.DOMAINS_DATA_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_TENANT_APP_MAPPING_CACHE, CacheConfig.TENANT_APP_MAPPING_CAPACITY, CacheConfig.TENANT_APP_MAPPING_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_SUBSCRIBED_SERVICE_CACHE, CacheConfig.TENANT_SUBSCRIBED_SERVICES_CAPACITY, CacheConfig.TENANT_SUBSCRIBED_SERVICES_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_SSO_LOGOUT_CACHE, CacheConfig.SSO_LOGOUT_CAPACITY, CacheConfig.SSO_LOGOUT_EXPIRE_TIME);
		CacheFactory.getCache(CACHES_ASSET_ROOT_CACHE, CacheConfig.ASSET_ROOT_CAPACITY, CacheConfig.ASSET_ROOT_EXPIRE_TIME);

	}

	public CacheUnit getCache(String cacheName)
	{
		if (StringUtil.isEmpty(cacheName)) {
			return null;
		}
		return getInternalCache(cacheName);
	}

	public Object getCacheable(String cacheName, Keys keys) throws Exception
	{
		return getCacheable(null, cacheName, keys);
	}

	public Object getCacheable(String cacheName, Keys keys, ICacheFetchFactory ff) throws Exception
	{
		return getCacheable(null, cacheName, keys, ff);
	}

	public Object getCacheable(String cacheName, String key) throws Exception
	{
		return getCacheable(cacheName, new Keys(key));
	}

	public Object getCacheable(Tenant tenant, String cacheName, Keys keys) throws Exception
	{
		return getCacheable(tenant, cacheName, keys, null);
	}

	public Object getCacheable(Tenant tenant, String cacheName, Keys keys, ICacheFetchFactory ff) throws Exception
	{
		logCacheStatus();
		CacheUnit cache = getInternalCache(cacheName);
		if (cache == null) {
			return null;
		}
		Object key = getInternalKey(tenant, keys);
		if (key == null) {
			return null;
		}
		Object value = cache.get(key.toString());
		if (value == null && ff != null) {
			LOGGER.debug("Cache not retrieved, trying to load with fetch factory");
			value = ff.fetchCachable(key);
			if (value != null) {
				cache.put(key.toString(), new Element(key, value));
				LOGGER.debug("Successfully fetched data, putting to cache group {}",cacheName);
			}
		}
		if (value == null) {
			LOGGER.debug("Not retrieved cache with cache name {} and key {} for tenant {} from cache group {}", cacheName, key, tenant,cacheName);
			return null;
		}
		LOGGER.debug("Retrieved cacheable with key={} and value={} for tenant={} from cache group {}", key, value, tenant,cacheName);
		return value;
	}

	public Object getCacheable(Tenant tenant, String cacheName, String key) throws Exception
	{
		return getCacheable(tenant, cacheName, new Keys(key));
	}

	public Object getCacheable(Tenant tenant, String cacheName, String key, ICacheFetchFactory ff) throws Exception
	{
		return getCacheable(tenant, cacheName, new Keys(key), ff);
	}

	public Object getInternalKey(Tenant tenant, Keys keys)
	{
		return keyGen.generate(tenant, keys);
	}

	/*public Object putCache(String key, Object value)
	{
		CacheUnit nc = CacheFactory.getCache("lookupCache");
		nc.put(key, new Element(key, value));
		return value;
	}*/

	public Object putCacheable(String cacheName, Keys keys, Object value)
	{
		return putCacheable(null, cacheName, keys, value);
	}

	public Object putCacheable(String cacheName, String key, Object value)
	{
		return putCacheable(cacheName, new Keys(key), value);
	}

	public Object putCacheable(Tenant tenant, String cacheName, Keys keys, Object value)
	{
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

	public Object putCacheable(Tenant tenant, String cacheName, String key, Object value)
	{
		return putCacheable(tenant, cacheName, new Keys(key), value);
	}

	public Object removeCacheable(String cacheName, Keys keys)
	{
		return removeCacheable(null, cacheName, keys);
	}

	public Object removeCacheable(Tenant tenant, String cacheName, Keys keys)
	{
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

	public Object removeCacheable(Tenant tenant, String cacheName, String key)
	{
		return removeCacheable(tenant, cacheName, new Keys(key));
	}

	public void setKeyGenerator(KeyGenerator keyGenerator)
	{
		keyGen = keyGenerator;
	}

	/**
	 * @param cacheName
	 * @param key
	 * @return
	 */
	private CacheUnit getInternalCache(String cacheName)
	{
		if (cacheName == null) {
			LOGGER.warn("Not retrieved from cache for null cache name");
			return null;
		}
		CacheUnit cache = null;
		try {
			cache = CacheFactory.getCache(cacheName);
		}
		catch (IllegalArgumentException e) {
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
	public void logCacheStatus(){
		long now=System.currentTimeMillis();
		long logInterval=CacheConfig.LOG_INTERVAL;
		if(now-lastLogTime>= logInterval){
			for(Map.Entry<String,CacheUnit> e: CacheFactory.getCacheUnitMap().entrySet()){
				CacheUnit cu=e.getValue();
				LOGGER.info("[Cache Status] Cache name is [{}], "
						+ "Cache capacity is [{}], "
						+ "Cache usage is [{}], "
						+ "Cache usage rate is [{}], " +
						"Cache total request count is [{}], "
						+ "Cache hit count is [{}], "
						+ "Cache hit rate is [{}], "
						+ "Eviction Count is [{}]" ,
								cu.getName(),
								cu.getCacheCapacity(),
								cu.getCacheUnitStatus().getUsage(),
								cu.getCacheUnitStatus().getUsageRate(),
								cu.getCacheUnitStatus().getRequestCount(),
								cu.getCacheUnitStatus().getHitCount(),
								cu.getCacheUnitStatus().getHitRate(),
								cu.getCacheUnitStatus().getEvictionCount());
			}
			this.lastLogTime=now;
		}

	}
}
