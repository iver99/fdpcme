/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.zdt.DataManager;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.TableRowsSynchronizer;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.ZDTEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.TableRowsEntity;

/**
 * @author guochen
 */
@Path("/v1/zdt")
public class ZDTAPI extends APIBase
{
	private static final Logger logger = LogManager.getLogger(ZDTAPI.class);

	private static final String TABLE_DATA_KEY_DASHBOARD = "EMS_DASHBOARD";
	private static final String TABLE_DATA_KEY_DASHBOARD_SET = "EMS_DASHBOARD_SET";
	private static final String TABLE_DATA_KEY_DASHBOARD_TILES = "EMS_DASHBOARD_TILE";
	private static final String TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS = "EMS_DASHBOARD_TILE_PARAMS";
	private static final String TABLE_DATA_KEY_DASHBOARD_USER_OPTIONS = "EMS_DASHBOARD_USER_OPTIONS";
	private static final String TABLE_DATA_KEY_DASHBOARD_PREFERENCES = "EMS_PREFERENCE";

	public ZDTAPI()
	{
		super();
	}

	@GET
	@Path("tablerows")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTableData()
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to [GET] /v1/zdt/tablerows");

		JSONObject obj = new JSONObject();
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			
			JSONArray tableData = getDashboardTableData(em);
			obj.put(TABLE_DATA_KEY_DASHBOARD, tableData);
			tableData = getDashboardSetTableData(em);
			obj.put(TABLE_DATA_KEY_DASHBOARD_SET, tableData);
			tableData = getDashboardTileTableData(em);
			obj.put(TABLE_DATA_KEY_DASHBOARD_TILES, tableData);
			tableData = getDashboardTileParamsTableData(em);
			obj.put(TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS, tableData);
			tableData = getDashboardUserOptionsTableData(em);
			obj.put(TABLE_DATA_KEY_DASHBOARD_USER_OPTIONS, tableData);
			tableData = getPreferenceTableData(em);
			obj.put(TABLE_DATA_KEY_DASHBOARD_PREFERENCES, tableData);
		}
		catch (JSONException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Errors:" + e.getLocalizedMessage()).build();
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
		return Response.status(Status.OK).entity(obj).build();
	}

	@GET
	@Path("counts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntitiesCount()
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to [GET] /v1/zdt/counts");
		EntityManager em = null;
		ZDTEntity zdte = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			long dashboardCount = DataManager.getInstance().getAllDashboardsCount(em);
			long favoriteCount = DataManager.getInstance().getAllFavoriteCount(em);
			long preferenceCount = DataManager.getInstance().getAllPreferencessCount(em);
			logger.debug("ZDT counters: dashboards count - {}, favorite count - {}, preference count - {}", dashboardCount,
				favoriteCount, preferenceCount);
			zdte = new ZDTEntity(dashboardCount, favoriteCount, preferenceCount);
		
		} catch (Exception e) {
			logger.error("error while getting count of tables");
		} finally {
			if (em != null) {
				em.close();
			}

		}
		return Response.ok(getJsonUtil().toJson(zdte)).build();
	}

	@PUT
	@Path("sync")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sync(JSONObject dataToSync)
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to [PUT] /v1/zdt/sync");
		logger.info("Service call to /v1/zdt/sync");
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			TableRowsEntity data = getJsonUtil().fromJson(dataToSync.toString(), TableRowsEntity.class);
			String response = new TableRowsSynchronizer().sync(em,data);
 			if (response.contains("Errors:")) {
 				return Response.status(500).entity(response).build();
 			}
 			return Response.status(Status.NO_CONTENT).entity(response).build();
		}
		catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return Response.status(500).entity("Errors:" + e.getLocalizedMessage()).build();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private JSONArray getDashboardSetTableData(EntityManager em)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardSetTableData(em);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_SET, list);
	}

	private JSONArray getDashboardTableData(EntityManager em)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTableData(em);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD, list);
	}

	private JSONArray getDashboardTileParamsTableData(EntityManager em)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTileParamsTableData(em);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS, list);
	}

	private JSONArray getDashboardTileTableData(EntityManager em)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTileTableData(em);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_TILES, list);
	}

	private JSONArray getDashboardUserOptionsTableData(EntityManager em)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardUserOptionsTableData(em);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_USER_OPTIONS, list);
	}

	/**
	 * @param list
	 * @return
	 */
	private JSONArray getJSONArrayForListOfObjects(String dataName, List<Map<String, Object>> list)
	{
		if (list == null) {
			logger.warn("Trying to get a JSON object for {} from a null object/list. Returning null JSON object", dataName);
			return null;
		}
		JSONArray array = new JSONArray();
		for (Map<String, Object> row : list) {
			array.put(row);
		}
		logger.debug("Retrieved table data for {} is \"{}\"", dataName, array.toString());
		return array;
	}

	private JSONArray getPreferenceTableData(EntityManager em)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getPreferenceTableData(em);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_PREFERENCES, list);
	}
}
