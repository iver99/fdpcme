package oracle.sysman.emaas.platform.emcpdf.cache.api;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by chehao on 2016/12/11.
 */
public abstract class CacheLoader<K, V>
{
    /**
     * Fetch cache data for the given cache key. Note that this method must be thread safe.
     *
     * @param key
     * @return The entry, or null if it does not exist.
     * @throws Exception
     *             On failure creating the object.
     */
    public abstract V load(K key) throws Exception;

}
