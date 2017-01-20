package oracle.sysman.emaas.platform.emcpdf.cache.support;

/**
 * Created by chehao on 2016/12/23.
 */
public class CachedItem {

    private Object key;
    private Object value;
    private Long creationTime;
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

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
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
