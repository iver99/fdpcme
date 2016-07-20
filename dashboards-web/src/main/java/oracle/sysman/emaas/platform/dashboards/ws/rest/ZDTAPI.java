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
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
	private static final String TABLE_DATA_KEY_DASHBOARD_FAVORITE = "EMS_DASHBOARD_FAVORITE";
	private static final String TABLE_DATA_KEY_DASHBOARD_LAST_ACCESS = "EMS_DASHBOARD_LAST_ACCESS";
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
		try {
			JSONArray tableData = getDashboardTableData();
			obj.put(TABLE_DATA_KEY_DASHBOARD, tableData);
			tableData = getDashboardFavoriteTableData();
			obj.put(TABLE_DATA_KEY_DASHBOARD_FAVORITE, tableData);
			tableData = getDashboardLastAccessTableData();
			obj.put(TABLE_DATA_KEY_DASHBOARD_LAST_ACCESS, tableData);
			tableData = getDashboardSetTableData();
			obj.put(TABLE_DATA_KEY_DASHBOARD_SET, tableData);
			tableData = getDashboardTileTableData();
			obj.put(TABLE_DATA_KEY_DASHBOARD_TILES, tableData);
			tableData = getDashboardTileParamsTableData();
			obj.put(TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS, tableData);
			tableData = getDashboardUserOptionsTableData();
			obj.put(TABLE_DATA_KEY_DASHBOARD_USER_OPTIONS, tableData);
			tableData = getPreferenceTableData();
			obj.put(TABLE_DATA_KEY_DASHBOARD_PREFERENCES, tableData);
		}
		catch (JSONException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return Response.status(Status.OK).entity(obj).build();
	}

	@GET
	@Path("counts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEntitiesCoung()
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to [GET] /v1/zdt/counts");

		long dashboardCount = DataManager.getInstance().getAllDashboardsCount();
		long favoriteCount = DataManager.getInstance().getAllFavoriteCount();
		long preferenceCount = DataManager.getInstance().getAllPreferencessCount();
		logger.debug("ZDT counters: dashboards count - {}, favorite count - {}, preference count - {}", dashboardCount,
				favoriteCount, preferenceCount);
		ZDTEntity zdte = new ZDTEntity(dashboardCount, favoriteCount, preferenceCount);
		return Response.ok(getJsonUtil().toJson(zdte)).build();
	}

	@PUT
	@Path("sync")
	public Response sync(JSONObject dataToSync)
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to [PUT] /v1/zdt/sync");
		try {
			TableRowsEntity data = getJsonUtil().fromJson(dataToSync.toString(), TableRowsEntity.class);
			new TableRowsSynchronizer().sync(data);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
			ErrorEntity error = new ErrorEntity(e);
			return buildErrorResponse(error);
		}
	}

	private JSONArray getDashboardFavoriteTableData()
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardFavoriteTableData();
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_FAVORITE, list);
	}

	private JSONArray getDashboardLastAccessTableData()
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardLastAccessTableData();
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_LAST_ACCESS, list);
	}

	private JSONArray getDashboardSetTableData()
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardSetTableData();
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_SET, list);
	}

	private JSONArray getDashboardTableData()
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTableData();
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD, list);
	}

	private JSONArray getDashboardTileParamsTableData()
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTileParamsTableData();
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS, list);
	}

	private JSONArray getDashboardTileTableData()
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTileTableData();
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_TILES, list);
	}

	private JSONArray getDashboardUserOptionsTableData()
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardUserOptionsTableData();
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

	private JSONArray getPreferenceTableData()
	{
		List<Map<String, Object>> list = DataManager.getInstance().getPreferenceTableData();
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_PREFERENCES, list);
	}
}
