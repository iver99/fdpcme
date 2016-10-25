package oracle.sysman.emaas.platform.dashboards.core.cache.lru;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CacheFactory {

	private static final Logger LOGGER = LogManager.getLogger(CacheFactory.class);

	//never expired
	private static final int DEFAULT_EXPIRE_TIME=0;

	private static ConcurrentHashMap<String,CacheUnit> cacheUnitMap=new ConcurrentHashMap<String,CacheUnit>();

	public static CacheUnit getCache(String cacheName){
		return CacheFactory.getCache(cacheName,DEFAULT_EXPIRE_TIME);
	}


	public static CacheUnit getCache(String cacheName,int timeToLive){
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
			return CacheFactory.createCacheUnit(cacheName, timeToLive);
		}else{
			return cu;
		}

	}

	private static CacheUnit createCacheUnit(String cacheName,int timeToLive){
		CacheUnit cu=new CacheUnit(cacheName,timeToLive);
		cacheUnitMap.put(cacheName, cu);
		LOGGER.debug("CacheFactory:Cache named: {},timeToLive time: {} has been created.", cacheName, timeToLive);
		return cu;
	}

	private CacheFactory() {
	}
}
