package oracle.sysman.emaas.platform.emcpdf.cache.api;

/**
 * Created by chehao on 2016/12/11.
 */
public interface ICacheFetchFactory
{
    /**
     * Fetch cache data for the given cache key. Note that this method must be thread safe.
     *
     * @param key
     * @return The entry, or null if it does not exist.
     * @throws Exception
     *             On failure creating the object.
     */
    Object fetchCachable(Object key);
}
