package oracle.sysman.emaas.platform.emcpdf.cache.util;

/**
 * Created by chehao on 2016/12/20.
 */
public class CacheStatusMessage {

    private CacheStatus cacheStatus;

    private String msg;

    public CacheStatus getCacheStatus() {
        return cacheStatus;
    }

    public void setCacheStatus(CacheStatus cacheStatus) {
        this.cacheStatus = cacheStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CacheStatusMessage(CacheStatus cacheStatus, String msg) {
        this.cacheStatus = cacheStatus;
        this.msg = msg;
    }

    public CacheStatusMessage() {
    }


}
