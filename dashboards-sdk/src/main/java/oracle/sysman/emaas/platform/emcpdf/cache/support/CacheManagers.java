package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.screenshot.LRUScreenshotCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;

/**
 * Created by chehao on 2016/12/27.
 */
public class CacheManagers {


    private static CacheManagers instance=new CacheManagers();

    private CacheManagers() {
    }

    public static CacheManagers getInstance(){
        return instance;
    }

    public ICacheManager build(String type){
        if(type!=null && CacheConstants.LRU_SCREENSHOT_MANAGER.equals(type)){
            return LRUScreenshotCacheManager.getInstance();
        }
        return build();
    }

    public ICacheManager build(){
        return LRUCacheManager.getInstance();
    }

}
