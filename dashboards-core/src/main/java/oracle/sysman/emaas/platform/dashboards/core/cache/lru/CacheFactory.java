package oracle.sysman.emaas.platform.dashboards.core.cache.lru;

import java.util.concurrent.ConcurrentHashMap;


public class CacheFactory {
	
	private CacheFactory() {
	  }

	
	private static final int DEFAULT_EXPIRE_TIME=0;
	
	private static ConcurrentHashMap<String,CacheUnit> cacheUnitMap=new ConcurrentHashMap<String,CacheUnit>();
	
	
	public static CacheUnit getCache(String cacheName){
		return getCache(cacheName,DEFAULT_EXPIRE_TIME);
	}
	
	public static CacheUnit getCache(String cacheName,int expire){
		if(cacheName ==null)
			throw new IllegalArgumentException("cache name cannot be null!");
		if("".equals(cacheName))
			throw new IllegalArgumentException("cache name cannot be empty!");
		CacheUnit cu=cacheUnitMap.get(cacheName);
		if(cu == null){
			return createCacheUnit(cacheName, expire);
		}else{
			return cu;
		}
		
	}
	
	private static CacheUnit createCacheUnit(String cacheName,int expire){
		CacheUnit cu=new CacheUnit(cacheName,expire);
		cacheUnitMap.put(cacheName, cu);
		return cu;
	}
}
