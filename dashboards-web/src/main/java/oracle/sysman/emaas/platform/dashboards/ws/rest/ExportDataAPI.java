package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DataExportManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DatabaseDependencyUnavailableException;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfDatautil.SSFDataUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * 
 * @author pingwu
 *
 */

@Path("/v1/export")
public class ExportDataAPI extends APIBase {
	
	private static final Logger LOGGER = LogManager.getLogger("ExportDataAPI");
	
	private static final String DASHBOARD_SET_TABLE_NAME = "EMS_DASHBOARD_SET";
	private static final String DASHBOARD_TABLE_NAME = "EMS_DASHBOARD";
	private static final String DASHBOARD_TILE_TABLE_NAME = "EMS_DASHBOARD_TILE";
	private static final String DASHBOARD_TILE_PARAMS_TABLE_NAME = "EMS_DASHBOARD_TILE_PARAMS";
	private static final String USER_OPTIONS_TABLE_NAME = "EMS_DASHBOARD_USER_OPTIONS";
	
	@PUT
	@Path("dashboards")
	public Response exportDashboards(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
			@HeaderParam(value = "X-REMOTE-USER") String userTenant, 
			@HeaderParam(value = "Referer") String referer,
			JSONArray array){
		infoInteractionLogAPIIncomingCall(tenantIdParam, referer, "Service call to [PUT] /v1/export/dashboards");
		List<BigInteger> dbdIds = new ArrayList<BigInteger>();
		if (array != null && array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				try {
					dbdIds.add(new BigInteger(array.getString(i)));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		Long tenantId = null;
		try {
			tenantId = getTenantId(tenantIdParam);
		} catch (BasicServiceMalfunctionException | DashboardException e) {
			LOGGER.debug("could not get tenant ID from header");
		}
		JSONObject obj = new JSONObject();
		DataExportManager exportManager = DataExportManager.getInstatnce();
		//dashboard set table data
		List<Map<String, Object>> dashboardSets = exportManager.getDashboardSetByDashboardSetIDs(dbdIds, tenantId);
		JSONArray tableData = getJSONArrayForListOfObjects(DASHBOARD_SET_TABLE_NAME,dashboardSets);
		obj.put(DASHBOARD_SET_TABLE_NAME, tableData);
		List<BigInteger> subDbds = getSubDashboardIds(dashboardSets);
		subDbds.addAll(dbdIds);
		List<BigInteger> allDbds = getAllDashboardIds(subDbds);
		//dashboard table data
		List<Map<String, Object>> dashboards = exportManager.getDashboardByDashboardIDs(allDbds, tenantId);
		//dashboard tile table data
		List<Map<String, Object>> tiles = exportManager.getTileByDashboardIDs(allDbds, tenantId);
		List<BigInteger> tileIds = getTileIds(tiles);
		//dashboard tile params table data
		List<Map<String, Object>> tileParams = null;
		if (tileIds != null && !tileIds.isEmpty()) {			
			tileParams = exportManager.getTileParamsByTileIDs(tileIds, tenantId);
		}		
		//dashboard user options table data
		List<Map<String, Object>> userOptions = exportManager.getUserOptionsByDashboardIds(allDbds, tenantId);
		//ssf data
		List<BigInteger> widgetIds = getWidgetIds(tiles);
 		JSONArray requestEntity = new JSONArray();
		requestEntity.put(widgetIds);
		String ssfDataResponse = SSFDataUtil.getSSFData(userTenant, tenantIdParam, requestEntity);
		
		//response data
		
		
	}
	
	
	
	/**
	 * @param list
	 * @return
	 */
	private JSONArray getJSONArrayForListOfObjects(String dataName, List<Map<String, Object>> list)
	{
		if (list == null) {
			LOGGER.warn("Trying to get a JSON object for {} from a null object/list. Returning null JSON object", dataName);
			return null;
		}
		JSONArray array = new JSONArray();
		for (Map<String, Object> row : list) {
			array.put(row);
		}
		LOGGER.debug("Retrieved table data for {} is \"{}\"", dataName, array.toString());
		return array;
	}
	
	private List<BigInteger> getIds(List<Map<String, Object>> tableData, String columnName) {
		List<BigInteger> ids = null;
		if (tableData != null) {
			ids =  new ArrayList<BigInteger>();
			for (Map<String, Object> tile : tableData) {
				Object value = tile.get(columnName);
				BigInteger id = new BigInteger(value.toString());
				ids.add(id);
			}
		}
		return ids;
	}
	
	private List<BigInteger> getWidgetIds(List<Map<String, Object>> tiles) {
		return getIds(tiles, "WIDGET_UNIQUE_ID");
	}
	
	private List<BigInteger> getTileIds(List<Map<String, Object>> tiles) {
		return getIds(tiles, "TILE_ID");
	}
	
	private List<BigInteger> getSubDashboardIds (List<Map<String, Object>> dashboardSets ) {
		return getIds(dashboardSets, "SUB_DASHBOARD_ID");
	}
	
	private List<BigInteger> getAllDashboardIds (List<BigInteger> ids) {
		Set<BigInteger> allIds = new HashSet<BigInteger>();
		if (ids != null) {
			allIds.addAll(ids);
		}
		List<BigInteger> allDbdIds = new ArrayList<BigInteger>();
		allDbdIds.addAll(allIds);
		return allDbdIds;
	}
		
	
}
