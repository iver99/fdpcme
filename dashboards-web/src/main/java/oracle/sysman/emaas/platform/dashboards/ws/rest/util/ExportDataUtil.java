package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.sysman.emaas.platform.dashboards.ws.rest.DashboardAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
/**
 * 
 * @author pingwu
 *
 */
public class ExportDataUtil {
	
	private static final Logger LOGGER = LogManager.getLogger(ExportDataUtil.class);
	
	/**
	 * @param list
	 * @return
	 */
	public JSONArray getJSONArrayForListOfObjects(String dataName, List<Map<String, Object>> list)
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
	
	public List<BigInteger> getWidgetIds(List<Map<String, Object>> tiles) {
		return getIds(tiles, "WIDGET_UNIQUE_ID");
	}
	
	public List<BigInteger> getTileIds(List<Map<String, Object>> tiles) {
		return getIds(tiles, "TILE_ID");
	}
	
	public List<BigInteger> getSubDashboardIds (List<Map<String, Object>> dashboardSets ) {
		return getIds(dashboardSets, "SUB_DASHBOARD_ID");
	}
	
	public List<BigInteger> getAllDashboardIds (List<BigInteger> ids) {
		Set<BigInteger> allIds = new HashSet<BigInteger>();
		if (ids != null) {
			allIds.addAll(ids);
		}
		List<BigInteger> allDbdIds = new ArrayList<BigInteger>();
		allDbdIds.addAll(allIds);
		return allDbdIds;
	}
		

}
