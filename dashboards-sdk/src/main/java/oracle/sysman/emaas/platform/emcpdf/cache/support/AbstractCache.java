package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheFetchFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chehao on 2016/12/22.
 */
public abstract class AbstractCache implements ICache{

    Logger LOGGER = LogManager.getLogger(AbstractCache.class);
    @Override
    public Object get(Object key) {
        return get(key,null);
    }

    @Override
    public Object get(Object key, ICacheFetchFactory factory) {
        CachedItem value=lookup(key);
        if(value == null){
            if(factory!=null){
                try {
                    Object valueFromFactory=factory.fetchCachable(key);
                    CachedItem ci=new CachedItem(key,valueFromFactory);
                    put(key,ci);
                    return valueFromFactory;
                } catch (Exception e) {
                    LOGGER.error(e.getLocalizedMessage());
                }
            }
        }else{
            if(isExpired(value)){
                evict(key);
            }else{
                LOGGER.debug("Cached Item with key {} are retrieved from cache group",key);
                return value.getValue();
            }
        }
        return null;
    }

    protected abstract CachedItem lookup(Object key);
}
