package oracle.sysman.emaas.platform.emcpdf.cache.support;

/**
 * Created by chehao on 2016/12/23.
 */
public class CachedItem {

    //make key, value, creationTime final, once created, can't be modified. EMCPDF-4115
    //if object is a Collections(eg.List), this 'final' cannot guarantee code modify Collection's data, so will handle this issue in cache get action by returning a immutable collections.
    final private Object key;
    final private Object value;
    final private Long creationTime;
    private Long lastAccessTime;
    private boolean available;
    private Long lastRefreshTime;


    public CachedItem(Object key, Object value) {
        this.key = key;
        this.value = value;
        this.creationTime=System.currentTimeMillis();
        this.lastAccessTime=this.creationTime;
    }

    public Object getKey() {
        return key;
    }


    public Object getValue() {
        return value;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getLastRefreshTime() {
        return lastRefreshTime;
    }

    public void setLastRefreshTime(Long lastRefreshTime) {
        this.lastRefreshTime = lastRefreshTime;
    }
}
