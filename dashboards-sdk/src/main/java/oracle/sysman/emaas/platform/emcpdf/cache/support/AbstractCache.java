package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.CacheLoader;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.exception.ExecutionException;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.CacheThreadPools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by chehao on 2016/12/22.
 */
public abstract class AbstractCache implements ICache{
    Logger LOGGER=LogManager.getLogger(AbstractCache.class);

    @Override
    public Object get(Object key) throws ExecutionException {
        return get(key,null);
    }

    @Override
    public Object get(Object key, CacheLoader factory)throws ExecutionException {
        checkNotNull(key);
        CachedItem value=lookup(key);
        Object valueFromFactory= null;
        if(value!=null ){
            if(isExpired(value)){
                evict(key);
            }else{
                return value.getValue();
            }
        }

        if (factory != null) {
            try {
                valueFromFactory = factory.load(key);
            } catch (Exception e) {
                LOGGER.error(e.getLocalizedMessage());
                throw new ExecutionException(e);
            }
//            CachedItem ci = new CachedItem(key, valueFromFactory);
            put(key, valueFromFactory);
        }
        return valueFromFactory;
    }

    @Override
    public Object refreshAfterGet(final Object key, final CacheLoader factory) throws ExecutionException {
        ScheduledExecutorService pool= CacheThreadPools.getThreadPool();
        LOGGER.info("Refresh after get action begin...");
        pool.schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("Refresh...");
                Object obj= null;
                try {
                    obj = get(key,factory);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                put(key,new CachedItem(key,obj));
            }
        }, 30, TimeUnit.SECONDS);
        LOGGER.info("Refresh after get action end...");
        return get(key,factory);
    }

    protected abstract CachedItem lookup(Object key);

    private  <T> T checkNotNull(T reference) {
        if (reference == null) {
            LOGGER.error("Null Pointer Exception occurred!");
            throw new NullPointerException();
        }
        return reference;
    }
}
