package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCache;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LinkedHashMapCache;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chehao on 2017/3/1 10:36.
 */
@Path("/v1/cache")
public class CacheAPI extends APIBase {
    private static final Logger LOGGER = LogManager.getLogger(CacheAPI.class);

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stopCacheGroup/{cacheGroup}")
    public Response stopCacheGrp(@PathParam("cacheGroup") String cacheGroup) {
        LOGGER.info("Service to call [PUT] /v1/cache/stopCacheGroup/{}:", cacheGroup);
        if (StringUtils.isEmpty(cacheGroup)) {
            LOGGER.warn("cache group name is null or empty!");
            return Response.status(Response.Status.NOT_FOUND).entity(new MsgModel(false, "Cache group name is null or empty!")).build();
        }
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        //clear all cache groups
        clearCacheGroupDataByName(lruCacheManager, cacheGroup);
        //reset all cache group statistics data
        resetCacheStatisticsByName(lruCacheManager, cacheGroup);
        //close all cache groups
        changeCacheGroupStatusByName(lruCacheManager, CacheStatus.CLOSED, cacheGroup);
        return Response.ok(getJsonUtil().toJson(new MsgModel(true, "Cache group" + cacheGroup + " is stopped successfully!"))).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/startCacheGroup/{cacheGroup}")
    public Response startCache(@PathParam("cacheGroup") String cacheGroup) {
        LOGGER.info("Service to call [PUT] /v1/cache/startCache/{}:", cacheGroup);
        if (StringUtils.isEmpty(cacheGroup)) {
            LOGGER.warn("cache group name is null or empty!");
            return Response.status(Response.Status.NOT_FOUND).entity(new MsgModel(false, "Cache group name is null or empty!")).build();
        }
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeCacheGroupStatusByName(lruCacheManager, CacheStatus.AVAILABLE, cacheGroup);
        return Response.ok(getJsonUtil().toJson(new MsgModel(true, "Cache group" + cacheGroup + " is started successfully!"))).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/stopCache")
    public Response stopCache() {
        LOGGER.info("Service to call [PUT] /v1/cache/stopCache");
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        //clear all cache groups
        clearAllCacheGroupData(lruCacheManager);
        //reset all cache group statistics data
        resetAllCacheStatistics(lruCacheManager);
        //close all cache groups
        changeAllCacheGroupStatus(lruCacheManager, CacheStatus.CLOSED);
        return Response.ok(getJsonUtil().toJson(new MsgModel(true, "All cache groups are stopped successfully!"))).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/startCache")
    public Response startCache() {
        LOGGER.info("Service to call [PUT] /v1/cache/startCache");
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeAllCacheGroupStatus(lruCacheManager, CacheStatus.AVAILABLE);
        return Response.ok(getJsonUtil().toJson(new MsgModel(true, "All cache groups are started successfully!"))).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/suspendCache")
    public Response suspendCache() {
        LOGGER.info("Service to call [PUT] /v1/cache/suspendCache");
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeAllCacheGroupStatus(lruCacheManager, CacheStatus.SUSPEND);
        return Response.ok(getJsonUtil().toJson(new MsgModel(true, "All cache groups are suspended successfully!"))).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resumeCache")
    public Response resumeCache() {
        LOGGER.info("Service to call [PUT] /v1/cache/resumeCache");
        AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
        changeAllCacheGroupStatus(lruCacheManager, CacheStatus.AVAILABLE);
        return Response.ok(getJsonUtil().toJson(new MsgModel(true, "All cache groups are resumed successfully!"))).build();
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
        if (cache == null) {
            LOGGER.error("Cache group named {} is not existed!", cacheGroupName);
            return;
        }
        cache.setCacheStatus(cacheStatus);
        LOGGER.info("Cache group {} status has been changed to {}", cacheGroupName, cacheStatus);
    }

    private void clearAllCacheGroupData(AbstractCacheManager cacheManager) {
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

    private void clearCacheGroupDataByName(AbstractCacheManager cacheManager, String cacheGroupName) {
        ConcurrentMap<String, ICache> map = cacheManager.getCacheMap();
        if (map == null) {
            LOGGER.error("CacheMap in CacheManagers is null!");
            return;
        }
        LinkedHashMapCache cache = (LinkedHashMapCache) map.get(cacheGroupName);
        if (cache == null) {
            LOGGER.error("Cache group named {} is not existed!", cacheGroupName);
            return;
        }
        LOGGER.info("Cache group named {} data has been cleared!", cache.getName());
        cache.clear();
    }

    private void resetAllCacheStatistics(AbstractCacheManager cacheManager) {
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

    private void resetCacheStatisticsByName(AbstractCacheManager cacheManager, String cacheGroupName) {
        ConcurrentMap<String, ICache> map = cacheManager.getCacheMap();
        if (map == null) {
            LOGGER.error("CacheMap in CacheManagers is null!");
            return;
        }
        AbstractCache cache = (AbstractCache) map.get(cacheGroupName);
        if (cache == null) {
            LOGGER.error("Cache group named {} is not existed!", cacheGroupName);
            return;
        }
        cache.getCacheCounter().reset();
        LOGGER.info("Cache group {} data has been cleared!", cache.getName());
    }


    class MsgModel {

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
