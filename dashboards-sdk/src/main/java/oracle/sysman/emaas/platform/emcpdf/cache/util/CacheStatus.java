package oracle.sysman.emaas.platform.emcpdf.cache.util;

/**
 * Created by chehao on 2016/12/19.
 */
public enum CacheStatus {
    UNINITIALIZED,
    AVAILABLE,
    SUSPEND,
    CLOSED;

    private CacheStatus() {
    }
}
