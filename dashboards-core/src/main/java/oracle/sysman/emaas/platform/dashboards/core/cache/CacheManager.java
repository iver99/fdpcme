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

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 * @author guochen
 */
public class CacheManager
{
	private static final Logger logger = LogManager.getLogger(CacheManager.class);

	public static final String CACHES_LOOKUP_CACHE = "lookupCache";
	public static final String CACHES_ETERNAL_CACHE = "eternalCache";

	public static final String LOOKUP_CACHE_KEY_SUBSCRIBED_APPS = "subscribedApps";

	private static CacheManager instance;

	static {
		instance = new CacheManager();
	}

	public static net.sf.ehcache.CacheManager getEhCacheManager()
	{
		URL url = CacheManager.class.getResource("/ehcache.xml");
		net.sf.ehcache.CacheManager manager = net.sf.ehcache.CacheManager.newInstance(url);
		return manager;
	}

	public static CacheManager getInstance()
	{
		return instance;
	}

	private KeyGenerator keyGen;

	private CacheManager()
	{
		keyGen = new DefaultKeyGenerator();
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
		Cache cache = getInternalCache(cacheName);
		if (cache == null) {
			return null;
		}
		Object key = getInternalKey(tenant, keys);
		if (key == null) {
			return null;
		}
		Element elem = cache.get(key);
		if (elem == null && ff != null) {
			logger.debug("Cache not retrieved, trying to load with fetch factory");
			Object obj = ff.fetchCache(key);
			if (obj != null) {
				elem = new Element(key, obj);
				cache.put(elem);
				logger.debug("Successfully fetched data, putting to cache");
			}
		}
		if (elem == null) {
			logger.debug("Not retrieved cache element with cache name {} and key {} for tenant {}", cacheName, key, tenant);
			return null;
		}
		Object value = elem.getObjectValue();
		logger.debug("Retrieved cacheable with key={} and value={} for tenant={}", key, value, tenant);
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

	public String[] getCacheNames()
	{
		net.sf.ehcache.CacheManager manager = CacheManager.getEhCacheManager();
		String[] names = manager.getCacheNames();
		logger.debug("Retrieved all cache names: {}", (Object[]) names);
		return names;
	}

	public Object getInternalKey(Tenant tenant, Keys keys)
	{
		return keyGen.generate(tenant, keys);
	}

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
		Cache cache = getInternalCache(cacheName);
		if (cache == null) {
			return null;
		}
		Object key = getInternalKey(tenant, keys);
		if (key == null) {
			return null;
		}
		cache.put(new Element(key, value));
		logger.debug("Cacheable with tenant={}, key={} and value={} is put to cache {}", tenant, key, value, cacheName);

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
		Cache cache = getInternalCache(cacheName);
		if (cache == null) {
			return null;
		}
		Object key = getInternalKey(tenant, keys);
		if (key == null) {
			return null;
		}
		Object obj = cache.get(key);
		cache.remove(key);
		logger.debug("Cacheable with key={} and value={} is removed from cache {}", key, obj, cacheName);
		return obj;
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
	private Cache getInternalCache(String cacheName)
	{
		if (cacheName == null) {
			logger.warn("Not retrieved from cache for null cache name");
			return null;
		}
		net.sf.ehcache.CacheManager manager = CacheManager.getEhCacheManager();
		if (manager == null) {
			logger.debug("Not retrieved from cache for null EhCacheManager");
			return null;
		}
		Cache cache = manager.getCache(cacheName);
		if (cache == null) {
			logger.debug("Not retrieved cache with cache name {}", cacheName);
			return null;
		}
		return cache;
	}
}
