package oracle.sysman.emaas.platform.dashboards.core.cache.lru;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import oracle.sysman.emaas.platform.dashboards.core.cache.CacheConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CacheFactory {

	private static final Logger LOGGER = LogManager.getLogger(CacheFactory.class);

	//never expired
	private static final int DEFAULT_EXPIRE_TIME= CacheConfig.DEFAULT_EXPIRE_TIME;
	private static final int DEFAULT_CACHE_UNIT_CAPACITY =CacheConfig.DEFAULT_CAPACITY;
	private static ConcurrentHashMap<String,CacheUnit> cacheUnitMap=new ConcurrentHashMap<String,CacheUnit>();

	public static CacheUnit getCache(String cacheName){
		return CacheFactory.getCache(cacheName,DEFAULT_CACHE_UNIT_CAPACITY,DEFAULT_EXPIRE_TIME);
	}


	public static CacheUnit getCache(String cacheName,int capacity,int timeToLive){
		if(cacheName ==null){
			LOGGER.debug("CacheFactory:Cache name cannot be null!");
			throw new IllegalArgumentException("Cache name cannot be null!");
		}
		if("".equals(cacheName)){
			LOGGER.debug("CacheFactory:Cache name cannot be empty!");
			throw new IllegalArgumentException("Cache name cannot be empty!");
		}
		CacheUnit cu=cacheUnitMap.get(cacheName);
		if(cu == null){
			return CacheFactory.createCacheUnit(cacheName,capacity, timeToLive);
		}else{
			return cu;
		}

	}

	private static CacheUnit createCacheUnit(String cacheName,int capacity,int timeToLive){
		CacheUnit cu=new CacheUnit(cacheName,capacity,timeToLive);
		cacheUnitMap.put(cacheName, cu);
		LOGGER.debug("CacheFactory:Cache named: {},timeToLive time: {} has been created.", cacheName, timeToLive);
		return cu;
	}

	private CacheFactory() {
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
