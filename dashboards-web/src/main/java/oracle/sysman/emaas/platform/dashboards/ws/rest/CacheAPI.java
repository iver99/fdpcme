package oracle.sysman.emaas.platform.dashboards.ws.rest;

import com.oracle.omc.omctf.testsdk.utils.StringUtils;
import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LinkedHashMapCache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.screenshot.LRUScreenshotCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chehao on 2017/3/1 10:36.
 */
@Path("/v1/cache")
public class CacheAPI {
    private static final Logger LOGGER = LogManager.getLogger(CacheAPI.class);

    @GET
    @Path("/cachedItem/{cacheGroup}")
    public Response getCacheGroupCachedItem(@PathParam("cacheGroup") String cacheGroup){
        if(StringUtils.isEmpty(cacheGroup)){
            LOGGER.warn("cache group name is null or empty!");
            return Response.status(Response.Status.NOT_FOUND).entity(new MsgModel(false, "Cache group name is null or empty!")).build();
        }
        LinkedHashMapCache cache = null;
        ConcurrentMap<String, ICache> map = null;
        if(CacheConstants.CACHES_SCREENSHOT_CACHE.equals(cacheGroup)){
            AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
             map = screenshotCacheManager.getCacheMap();
        }else{
            AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
            map = lruCacheManager.getCacheMap();
        }
        cache = (LinkedHashMapCache) map.get(cacheGroup);
        if(cache == null){
            LOGGER.error("Cache group named {} is not existed!", cacheGroup);
            return Response.status(Response.Status.NOT_FOUND).entity(new MsgModel(false, "Cache group name is not existed!")).build();
        }else{
            return Response.status(Response.Status.OK).entity(cache.toString()).build();
        }
    }

    @PUT
    @Path("/stopCacheGroup/{cacheGroup}")
    public Response stopCacheGrp(@PathParam("cacheGroup") String cacheGroup) {
        if(StringUtils.isEmpty(cacheGroup)){
            LOGGER.warn("cache group name is null or empty!");
            return Response.status(Response.Status.NOT_FOUND).entity(new MsgModel(false, "Cache group name is null or empty!")).build();
        }
        if(CacheConstants.CACHES_SCREENSHOT_CACHE.equals(cacheGroup)){
            LOGGER.info("Closing screenshot cache group...");
            AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
            clearCacheGroupDataByName(screenshotCacheManager, cacheGroup);
            resetCacheStatisticsByName(screenshotCacheManager, cacheGroup);
            changeCacheGroupStatusByName(screenshotCacheManager,CacheStatus.CLOSED, cacheGroup);

        }else{
            AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
            //clear all cache groups
            clearCacheGroupDataByName(lruCacheManager, cacheGroup);
            //reset all cache group statistics data
            resetCacheStatisticsByName(lruCacheManager, cacheGroup);
            //close all cache groups
            changeCacheGroupStatusByName(lruCacheManager,CacheStatus.CLOSED, cacheGroup);
        }
        return Response.status(Response.Status.OK).entity(new MsgModel(true, "Cache group" + cacheGroup+ " is stopped successfully!")).build();
    }

    @PUT
    @Path("/startCache/{cacheGroup}")
    public Response startCache(@PathParam("cacheGroup") String cacheGroup) {
        if(StringUtils.isEmpty(cacheGroup)){
            LOGGER.warn("cache group name is null or empty!");
            return Response.status(Response.Status.NOT_FOUND).entity(new MsgModel(false, "Cache group name is null or empty!")).build();
        }
        if(CacheConstants.CACHES_SCREENSHOT_CACHE.equals(cacheGroup)){
            AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
            changeCacheGroupStatusByName(screenshotCacheManager,CacheStatus.AVAILABLE, cacheGroup);

        }else{
            AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
            changeCacheGroupStatusByName(lruCacheManager,CacheStatus.AVAILABLE, cacheGroup);
        }
        return Response.status(Response.Status.OK).entity(new MsgModel(true, "All cache groups are started successfully!")).build();
    }

    @PUT
    @Path("/stopCache")
    public Response stopCache() {
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
        //clear all cache groups
        clearAllCacheGroupData(lruCacheManager);
        clearAllCacheGroupData(screenshotCacheManager);
        //reset all cache group statistics data
        resetAllCacheStatistics(lruCacheManager);
        resetAllCacheStatistics(screenshotCacheManager);
        //close all cache groups
        changeAllCacheGroupStatus(lruCacheManager,CacheStatus.CLOSED);
        changeAllCacheGroupStatus(screenshotCacheManager,CacheStatus.CLOSED);
        return Response.status(Response.Status.OK).entity(new MsgModel(true, "All cache groups are stopped successfully!")).build();
    }

    @PUT
    @Path("/startCache")
    public Response startCache() {
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeAllCacheGroupStatus(lruCacheManager,CacheStatus.AVAILABLE);
        AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
        changeAllCacheGroupStatus(screenshotCacheManager,CacheStatus.AVAILABLE);
        return Response.status(Response.Status.OK).entity(new MsgModel(true, "All cache groups are started successfully!")).build();
    }

    @PUT
    @Path("/suspendCache")
    public Response suspendCache() {
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeAllCacheGroupStatus(lruCacheManager,CacheStatus.SUSPEND);
        AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
        changeAllCacheGroupStatus(screenshotCacheManager,CacheStatus.SUSPEND);
        return Response.status(Response.Status.OK).entity(new MsgModel(true, "All cache groups are suspended successfully!")).build();
    }

    @PUT
    @Path("/resumeCache")
    public Response resumeCache() {
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeAllCacheGroupStatus(lruCacheManager,CacheStatus.AVAILABLE);
        AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
        changeAllCacheGroupStatus(screenshotCacheManager,CacheStatus.AVAILABLE);
        return Response.status(Response.Status.OK).entity(new MsgModel(true, "All cache groups are resumed successfully!")).build();
    }

    private void changeAllCacheGroupStatus(AbstractCacheManager cacheManager, CacheStatus cacheStatus) {
        ConcurrentMap<String, ICache> map = cacheManager.getCacheMap();
        if (map == null) {
            LOGGER.error("CacheMap in CacheManagers is null!");
            return;
        }
        for (ICache value : map.values()) {
            LinkedHashMapCache cache = (LinkedHashMapCache) value;
            cache.setCacheStatus(cacheStatus);
            LOGGER.info("Cache group {} status has been changed to {}", cache.getName(), cacheStatus);
        }
    }

    private void changeCacheGroupStatusByName(AbstractCacheManager cacheManager, CacheStatus cacheStatus, String cacheGroupName) {
        ConcurrentMap<String, ICache> map = cacheManager.getCacheMap();
        if (map == null) {
            LOGGER.error("CacheMap in CacheManagers is null!");
            return;
        }
        LinkedHashMapCache cache = (LinkedHashMapCache) map.get(cacheGroupName);
        if(cache == null){
            LOGGER.error("Cache group named {} is not existed!", cacheGroupName);
            return ;
        }
        cache.setCacheStatus(cacheStatus);
        LOGGER.info("Cache group {} status has been changed to {}", cacheGroupName, cacheStatus);
    }

    private void clearAllCacheGroupData(AbstractCacheManager cacheManager){
        ConcurrentMap<String, ICache> map = cacheManager.getCacheMap();
        if (map == null) {
            LOGGER.error("CacheMap in CacheManagers is null!");
            return;
        }
        for (ICache value : map.values()) {
            LinkedHashMapCache cache = (LinkedHashMapCache) value;
            cache.clear();
            LOGGER.info("Cache group {} data has been cleared!", cache.getName());
        }
    }
    private void clearCacheGroupDataByName(AbstractCacheManager cacheManager, String cacheGroupName){
        ConcurrentMap<String, ICache> map = cacheManager.getCacheMap();
        if (map == null) {
            LOGGER.error("CacheMap in CacheManagers is null!");
            return;
        }
        LinkedHashMapCache cache = (LinkedHashMapCache) map.get(cacheGroupName);
        if(cache == null){
            LOGGER.error("Cache group named {} is not existed!", cacheGroupName);
            return ;
        }
        LOGGER.info("Cache group named {} data has been cleared!", cache.getName());
        cache.clear();
    }

    private void resetAllCacheStatistics(AbstractCacheManager cacheManager){
        ConcurrentMap<String, ICache> map = cacheManager.getCacheMap();
        if (map == null) {
            LOGGER.error("CacheMap in CacheManagers is null!");
            return;
        }
        for (ICache value : map.values()) {
            AbstractCache cache = (AbstractCache) value;
            cache.getCacheCounter().reset();
            LOGGER.info("Cache group {} data has been cleared!", cache.getName());
        }
    }

    private void resetCacheStatisticsByName(AbstractCacheManager cacheManager, String cacheGroupName){
        ConcurrentMap<String, ICache> map = cacheManager.getCacheMap();
        if (map == null) {
            LOGGER.error("CacheMap in CacheManagers is null!");
            return;
        }
        AbstractCache cache = (AbstractCache) map.get(cacheGroupName);
        if(cache == null){
            LOGGER.error("Cache group named {} is not existed!", cacheGroupName);
            return ;
        }
        cache.getCacheCounter().reset();
        LOGGER.info("Cache group {} data has been cleared!", cache.getName());
    }



    class MsgModel{

        public MsgModel(boolean success, String msg) {
            this.success = success;
            this.msg = msg;
        }

        private boolean success;
        private String msg;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
