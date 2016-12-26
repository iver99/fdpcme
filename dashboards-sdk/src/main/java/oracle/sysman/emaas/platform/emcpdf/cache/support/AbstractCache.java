package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheFetchFactory;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.CacheStatistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

/**
 * Created by chehao on 2016/12/22.
 */
public abstract class AbstractCache implements ICache{
    Logger LOGGER=LogManager.getLogger(AbstractCache.class);

    public CacheStatistics cacheStatistics =new CacheStatistics();

    @Override
    public Object get(Object key) {
        return get(key,null);
    }

    @Override
    public Object get(Object key, ICacheFetchFactory factory) {
        cacheStatistics.setRequestCount(cacheStatistics.getRequestCount()+1);
        CachedItem value=lookup(key);
        if(value == null){
            if(factory!=null){
                Object valueFromFactory= null;
                try {
                    valueFromFactory = factory.fetchCachable(key);
                } catch (Exception e) {
                    LOGGER.error(e.getLocalizedMessage());
                }
                CachedItem ci=new CachedItem(key,valueFromFactory);
                    put(key,ci);
                    return valueFromFactory;
            }
        }else{
            if(isExpired(value)){
                evict(key);
            }else{
                cacheStatistics.setHitCount(cacheStatistics.getHitCount()+1);
                return value.getValue();
            }
        }
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        cacheStatistics.setUsage(cacheStatistics.getUsage()+1);
    }

    @Override
    public void evict(Object key) {
        cacheStatistics.setEvictionCount(cacheStatistics.getEvictionCount()+1);
        cacheStatistics.setUsage(cacheStatistics.getUsage()-1);
    }

    protected abstract CachedItem lookup(Object key);

}
