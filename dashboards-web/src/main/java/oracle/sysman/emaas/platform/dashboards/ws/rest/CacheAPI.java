package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.sysman.emaas.platform.dashboards.core.cache.CacheManager;
import oracle.sysman.emaas.platform.dashboards.core.cache.lru.CacheFactory;
import oracle.sysman.emaas.platform.dashboards.core.cache.lru.CacheUnit;
import oracle.sysman.emaas.platform.dashboards.core.exception.CacheException;
import oracle.sysman.emaas.platform.dashboards.core.exception.cache.CacheGroupNameEmptyException;
import oracle.sysman.emaas.platform.dashboards.core.exception.cache.CacheGroupNotFoundException;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chehao on 2016/11/10.
 * emcpdf-2445,introduce internal API to change server side cache settings
 */
@Path("/v1/cache")
public class CacheAPI extends APIBase{
    private static final Logger LOGGER = LogManager.getLogger(CacheAPI.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAllCacheGroups()
	{
		LOGGER.info("Service to call [GET] /v1/cache");
		ConcurrentHashMap<String, CacheUnit> cacheUnitMap = CacheFactory.getCacheUnitMap();
		List<CacheUnit> cacheUnitList = new ArrayList<CacheUnit>();
		Iterator<Map.Entry<String,CacheUnit>> iterator= cacheUnitMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, CacheUnit> entry = iterator.next();
			cacheUnitList.add(entry.getValue());
		}
		return Response.ok(getJsonUtil().toJson(cacheUnitList)).build();
	}

    @GET
    @Path("/{cacheGroupName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCacheGroups(@PathParam("cacheGroupName") String cacheGroupName){
    	LOGGER.info("Service to call [GET] /v1/cache/{}",cacheGroupName);
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
    @Path("clearCache")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearAllCacheGroup(){
    	LOGGER.info("Service to call [PUT] /v1/cache/clearCache");
        CacheFactory.getCacheUnitMap().clear();
        Response resp = Response.status(Response.Status.OK).build();
        return resp;
    }

    @PUT
    @Path("clearCache/{cacheGroupName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearCacheGroup(@PathParam("cacheGroupName") String cacheGroupName){
    	LOGGER.info("Service to call [PUT] /v1/cache/clearCache/{}",cacheGroupName);
        try{
            if(cacheGroupName == null || "".equals(cacheGroupName)){
                throw new CacheGroupNameEmptyException();
            }
            //clear specific cache group
            CacheFactory.getCacheUnitMap().get(cacheGroupName).clearCache();
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

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response cacheMechanismSwitch(String isEnable) {
        if (isEnable==null || "".equals(isEnable)){
            throw new IllegalArgumentException("parameter can only be TRUE or FALSE!");
        }
        Boolean flag=new Boolean(isEnable.toLowerCase());
        if(flag){
            CacheManager.getInstance().enableCacheManager();
        }else{
            CacheManager.getInstance().disableCacheManager();
        }
        Response resp = Response.status(Response.Status.OK).build();
        return resp;
    }


}

