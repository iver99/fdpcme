package oracle.sysman.emaas.platform.emcpdf.cache.util;

import oracle.sysman.emaas.platform.emcpdf.cache.CacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.api.KeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.CacheGroupNotFoundException;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.CacheUnit;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.DefaultKeyGenerator;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by chehao on 2016/12/20.
 */
public class CacheManagerUtil {
    private static final Logger LOGGER = LogManager.getLogger(CacheManagerUtil.class);
    private static KeyGenerator keyGen =new DefaultKeyGenerator();;

    public static Object getInternalKey(Tenant tenant, Keys keys) {
        return keyGen.generate(tenant, keys);
    }

    public static void setKeyGenerator(KeyGenerator keyGenerator) {
        keyGen = keyGenerator;
    }

    public static void clearAllCacheGroup(){
        if(CacheManager.getInstance().getCacheUnitMap()!=null){
            Iterator<Map.Entry<String,CacheUnit>> it =CacheManager.getInstance().getCacheUnitMap().entrySet().iterator();
            while(it.hasNext()){
                CacheUnit cu=it.next().getValue();
                cu.getCacheUnitStatus().setEvictionCount(0L);
                cu.getCacheUnitStatus().setHitCount(0L);
                cu.getCacheUnitStatus().setRequestCount(0L);
                cu.getCacheUnitStatus().setUsage(0);
                cu.getCacheLinkedHashMap().clear();
            }
            CacheManager.getInstance().setCacheUnitMap(null);
        }
    }

    public static CacheUnit getCacheGroup(String cacheName) throws CacheGroupNotFoundException {
        if(cacheName ==null ||"".equals(cacheName)){
            LOGGER.debug("CacheManager:Cache name cannot be null or empty!");
            throw new IllegalArgumentException("Cache name cannot be null or empty!");
        }
        CacheUnit cu=CacheManager.getInstance().getCacheUnitMap().get(cacheName);
        if(cu == null){
            LOGGER.error("CacheManager:Cache group named {} is not found!",cacheName);
            throw new CacheGroupNotFoundException();
        }
        return cu;
    }
    /**
     * @param cacheName
     * @return
     */
    public static CacheUnit getInternalCache(String cacheName) {
        if (cacheName == null) {
            LOGGER.warn("Not retrieved from cache for null cache name");
            return null;
        }
        CacheUnit cache = null;
        try {
            cache = CacheManagerUtil.getCacheGroup(cacheName);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return null;
        } catch(CacheGroupNotFoundException e){
            LOGGER.error(e.getLocalizedMessage(), e);
            return null;
        }
        return cache;
    }
    public static CacheUnit getCache(String cacheName) {
        if (StringUtil.isEmpty(cacheName)) {
            return null;
        }
        return getInternalCache(cacheName);
    }

    public static CacheUnit createCacheUnit(String cacheName,int capacity,int timeToLive){
        CacheUnit cu=new CacheUnit(cacheName,capacity,timeToLive);
        CacheManager.getInstance().getCacheUnitMap().put(cacheName, cu);
        LOGGER.debug("CacheManager:Cache named: {},timeToLive time: {} has been created.", cacheName, timeToLive);
        return cu;
    }
}
