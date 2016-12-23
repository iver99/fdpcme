package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheFetchFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chehao on 2016/12/22.
 */
public abstract class AbstractCache implements ICache{
    Logger LOGGER=LogManager.getLogger(AbstractCache.class);

    @Override
    public Object get(Object key) {
        return get(key,null);
    }

    @Override
    public Object get(Object key, ICacheFetchFactory factory) {
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
                return value.getValue();
            }
        }
        return null;
    }

    protected abstract CachedItem lookup(Object key);
}
