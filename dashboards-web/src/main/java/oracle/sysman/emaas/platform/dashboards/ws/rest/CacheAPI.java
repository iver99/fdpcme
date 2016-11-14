package oracle.sysman.emaas.platform.dashboards.ws.rest;

import oracle.sysman.emaas.platform.dashboards.core.cache.lru.CacheFactory;
import oracle.sysman.emaas.platform.dashboards.core.cache.lru.CacheUnit;
import oracle.sysman.emaas.platform.dashboards.core.exception.CacheException;
import oracle.sysman.emaas.platform.dashboards.core.exception.cache.CacheGroupNameEmptyException;
import oracle.sysman.emaas.platform.dashboards.core.exception.cache.CacheGroupNotFoundException;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chehao on 2016/11/10.
 * emcpdf-2445,introduce internal API to change server side cache settings
 */
@Path("/v1/cache")

public class CacheAPI extends APIBase{
    private static final Logger LOGGER = LogManager.getLogger(APIBase.class);

    @GET
    public Response getAllCacheGroups(){
            ConcurrentHashMap<String,CacheUnit> cacheUnitMap= CacheFactory.getCacheUnitMap();
            List<CacheUnit> cacheUnitList=new ArrayList<CacheUnit>();
            for(Map.Entry<String,CacheUnit> e: cacheUnitMap.entrySet()){
                cacheUnitList.add(e.getValue());
            }
            return Response.ok(getJsonUtil().toJson(cacheUnitList)).build();
    }

    @GET
    @Path("/{cacheGroupName}")
    public Response getCacheGroups(@PathParam("cacheGroupName") String cacheGroupName){
        try{
            if(cacheGroupName == null || "".equals(cacheGroupName)){
                throw new CacheGroupNameEmptyException();
            }
            ConcurrentHashMap<String,CacheUnit> cacheUnitMap= CacheFactory.getCacheUnitMap();
            CacheUnit cu=cacheUnitMap.get(cacheGroupName);
            if(cu == null){
                throw new CacheGroupNotFoundException();
            }
            return Response.ok(getJsonUtil().toJson(cu)).build();
        }catch(CacheException e){
            LOGGER.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        }
    }

    /**
     * this action will eliminate all cache groups
     * @return
     */
    @PUT
    @Path("/clearCache")
    public Response clearAllCacheGroup(){
        CacheFactory.getCacheUnitMap().clear();
        Response resp = Response.status(Response.Status.OK).build();
        return resp;
    }

    @PUT
    @Path("/{cacheGroupName}")
    public Response clearCacheGroup(@PathParam("cacheGroupName") String cacheGroupName){
        try{
            if(cacheGroupName == null || "".equals(cacheGroupName)){
                throw new CacheGroupNameEmptyException();
            }
            CacheFactory.getCacheUnitMap().get(cacheGroupName).clearCache();
            Response resp = Response.status(Response.Status.OK).build();
            return resp;
        }catch(CacheException e){
            LOGGER.error(e.getLocalizedMessage(), e);
            return buildErrorResponse(new ErrorEntity(e));
        }
    }


}

