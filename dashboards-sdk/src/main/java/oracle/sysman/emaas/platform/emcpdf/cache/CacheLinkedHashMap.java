package oracle.sysman.emaas.platform.emcpdf.cache;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by chehao on 2016/12/9.
 */
public class CacheLinkedHashMap<K,V> {
    private ReentrantLock lock=new ReentrantLock();
    private LinkedHashMap<K,V> cacheMap;
    private int cacheCapacity;

    public CacheLinkedHashMap(int cacheCapacity){
        this.cacheCapacity = cacheCapacity;
        int hashTableSize = (int) Math.ceil(cacheCapacity/0.75f) + 1;
        cacheMap = new LinkedHashMap<K,V>(hashTableSize, 0.75f, true) {//ordered by access time
            private static final long serialVersionUID = 1L;
            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<K,V> eldest) {
                return size() > CacheLinkedHashMap.this.cacheCapacity;
            }
        };
    }
    //synchronized put
    public void put(K key, V value) {
        lock.lock();
        try{
            cacheMap.put(key, value);
        }finally{
            lock.unlock();
        }

    }
    //un-synchronized put
    public void putWithoutLock(K key,V value){
        cacheMap.put(key, value);
    }

    //un-synchronized get
    public Object get(K key) {
        return cacheMap.get(key);

    }
    //synchronized get
    public Object getUnderLock(K key){
        lock.lock();
        try{
            return cacheMap.get(key);
        }finally{
            lock.unlock();
        }

    }

    public V remove(K key) {
        lock.lock();
        try{
            return cacheMap.remove(key);
        }finally{
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try{
            return cacheMap.size();
        }finally{
            lock.unlock();
        }
    }

    public void clear() {
        lock.lock();
        try{
            cacheMap.clear();
        }finally{
            lock.unlock();
        }
    }

    public boolean isEmpty(){
        return this.size()==0?true:false;
    }

    public boolean containsKey(K key){
        return cacheMap.containsKey(key);
    }

    public boolean containsValue(V value){
        return cacheMap.containsValue(value);
    }

    public LinkedHashMap<K, V> getCacheMap() {
        return cacheMap;
    }

    public void setCacheMap(LinkedHashMap<K, V> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public int getCacheCapacity() {
        return cacheCapacity;
    }

    public void setCacheCapacity(int cacheCapacity) {
        this.cacheCapacity = cacheCapacity;
    }




}

