package oracle.sysman.emaas.platform.emcpdf.cache.api;

import oracle.sysman.emaas.platform.emcpdf.cache.support.CachedItem;

/**
 * Created by chehao on 2016/12/9.
 */
public interface ICache<K,V> {
    public V get(K key);

    public V get(K key,CacheLoader factory);

//    public V get(K key,CacheLoader factory,boolean refresh);

    public void put(K key, Object value);

    public void evict(K key);

    public void clear();

    public String getName();

    public boolean isExpired(CachedItem cachedItem);

    public Object refreshAfterGet(K key,CacheLoader factory);

}
