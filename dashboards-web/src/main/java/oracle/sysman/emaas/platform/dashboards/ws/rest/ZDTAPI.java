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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.zdt.DataManager;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.TableRowsSynchronizer;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.ZDTEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.TableRowsEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.ZDTComparatorStatusRowEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.zdt.tablerows.ZDTSyncStatusRowEntity;

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
	public Response getAllTableData(@QueryParam("comparisonType") String type)
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to [GET] /v1/zdt/tablerows?comparisonType=");

		JSONObject obj = new JSONObject();
		EntityManager em = null;
		if (type == null) {
			type = "full";
		}
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			String lastComparisonDate = DataManager.getInstance().getLatestComparisonDateForCompare(em);
			JSONArray tableData = getDashboardTableData(em,type, lastComparisonDate);
			obj.put(TABLE_DATA_KEY_DASHBOARD, tableData);
			tableData = getDashboardSetTableData(em,type, lastComparisonDate);
			obj.put(TABLE_DATA_KEY_DASHBOARD_SET, tableData);
			tableData = getDashboardTileTableData(em,type, lastComparisonDate);
			obj.put(TABLE_DATA_KEY_DASHBOARD_TILES, tableData);
			tableData = getDashboardTileParamsTableData(em,type, lastComparisonDate);
			obj.put(TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS, tableData);
			tableData = getDashboardUserOptionsTableData(em,type, lastComparisonDate);
			obj.put(TABLE_DATA_KEY_DASHBOARD_USER_OPTIONS, tableData);
			tableData = getPreferenceTableData(em,type, lastComparisonDate);
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

	@GET
	@Path("sync")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sync(@QueryParam("syncType") String type, @QueryParam("syncDate") String syncDate)
	{
		infoInteractionLogAPIIncomingCall(null, null, "Service call to (GET) /v1/zdt/sync?syncType=xx&syncDate=xx");
		logger.info("Service call to /v1/zdt/sync");
		TableRowsEntity data = null;
		String lastCompareDate = null;
		if (type == null) {
			type = "full";
		}
		EntityManager em = null;
		String lastComparisonDateForSync = null;
		List<Map<String, Object>> comparedDataToSync = null;
		try {			
			DashboardServiceFacade dsf = new DashboardServiceFacade();
			em = dsf.getEntityManager();
			lastComparisonDateForSync = DataManager.getInstance().getLastComparisonDateForSync(em);
			comparedDataToSync = DataManager.getInstance().getComparedDataToSync(em, lastComparisonDateForSync);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return Response.status(400).entity(e.getLocalizedMessage()).build();				
		} finally {
			if (em != null) {
				em.close();
			}
		}
		try {	
			if (comparedDataToSync != null && !comparedDataToSync.isEmpty()) {
				for (Map<String, Object> dataMap : comparedDataToSync) {
					Object compareResult = dataMap.get("COMPARISON_RESULT");
					Object compareDate = dataMap.get("COMPARISON_DATE");
					data = getJsonUtil().fromJson(compareResult.toString(), TableRowsEntity.class);
					String response = new TableRowsSynchronizer().sync(data);
					lastCompareDate = (String) compareDate;
					if (response.contains("Errors:")) {
						saveToSyncTable(syncDate, type, "FAILED",lastCompareDate);
						return Response.status(500).entity(response).build();
					}
				}
				int flag = saveToSyncTable(syncDate, type, "SUCCESSFUL",lastCompareDate);
				if (flag < 0) {
					return Response.status(500).entity("Fail to save sync status data").build();
				}
			} else {
				return Response.ok("Nothing to sync as no compared data").build();
			}
						
			return Response.ok("Sync is successful!").build();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Errors:" + e.getLocalizedMessage()).build();
		} 
	}
	
	private int saveToSyncTable(String syncDateStr, String type, String syncResult, String lastComparisonDate) {
		Date syncDate = null;
		try {  
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    syncDate = sdf.parse(syncDateStr);  
		} catch (ParseException e) {  
		    logger.error(e);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(syncDate);
		cal.add(Calendar.HOUR_OF_DAY, 6);
		Date nextScheduleDate = cal.getTime();
		String nextScheduleDateStr = getTimeString(nextScheduleDate);
		// TO DO... currently each sync operation will sync all of the existing compared result, 
		// so for the existing compared result the divergence percentage will always be 0.0
		// In the future, it is better to add logic to check the divergence percentage for the rows which are not compared;
		double divergencePercentage = 0.0; 
		return DataManager.getInstance().saveToSyncTable(syncDateStr, nextScheduleDateStr, type, syncResult, divergencePercentage, lastComparisonDate);
		
	}
	
	private String getTimeString(Date date)
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	@GET
	@Path("sync/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSyncStatus() {
		infoInteractionLogAPIIncomingCall(null, null, "Service calling to (GET) /v1/zdt/sync/status");
		EntityManager em = null;
		String message = null;
		int statusCode = 200;
		String sync_date = "";
		String sync_type = "";
		String next_schedule_date = "";
		double percentage = 0.0;
		em =  new DashboardServiceFacade().getEntityManager();
		List<Map<String, Object>> result = DataManager.getInstance().getSyncStatus(em);
		if (result != null && result.size() == 1) {
				Map<String, Object> resultMap = result.get(0);
				sync_date = resultMap.get("SYNC_DATE").toString();
				sync_type = resultMap.get("SYNC_TYPE").toString();
				next_schedule_date =  resultMap.get("NEXT_SCHEDULE_SYNC_DATE").toString();
				percentage = Double.parseDouble(resultMap.get("DIVERGENCE_PERCENTAGE").toString());
		}
		ZDTSyncStatusRowEntity syncStatus = new ZDTSyncStatusRowEntity(sync_date,sync_type, next_schedule_date, percentage);
		try {
			message = getJsonUtil().toJson(syncStatus);
			return Response.status(statusCode).entity(message).build();
		} catch (Exception e) {
			message = "Errors:" + e.getLocalizedMessage();
			statusCode = 500;
			logger.error("Errors while transfer sync status object to json string {}",e.getLocalizedMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return Response.status(statusCode).entity(message).build();
	}
	
	@GET
	@Path("compare/status")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComparisonStatus() {
		infoInteractionLogAPIIncomingCall(null, null, "Service calling to (GET) /v1/zdt/compare/status");
		EntityManager em = null;
		String message = null;
		int statusCode = 200;
		String comparison_date = "";
		String comparison_type = "";
		//String comparison_result = null;
		String next_schedule_date = "";
		double percentage = 0.0;
		em =  new DashboardServiceFacade().getEntityManager();
		List<Map<String, Object>> result = DataManager.getInstance().getComparatorStatus(em);
		if (result != null && result.size() == 1) {
				Map<String, Object> resultMap = result.get(0);
				comparison_date = resultMap.get("COMPARISON_DATE").toString();
				comparison_type = resultMap.get("COMPARISON_TYPE").toString();
				next_schedule_date =  resultMap.get("NEXT_SCHEDULE_COMPARISON_DATE").toString();
				//	comparison_result = resultMap.get("").toString();
				percentage = Double.parseDouble(resultMap.get("DIVERGENCE_PERCENTAGE").toString());
		}
		ZDTComparatorStatusRowEntity comparatorStatus = new ZDTComparatorStatusRowEntity(comparison_date,comparison_type, next_schedule_date, percentage);
		try {
			message = getJsonUtil().toJson(comparatorStatus);
			return Response.status(statusCode).entity(message).build();
		} catch (Exception e) {
			message = "Errors:" + e.getLocalizedMessage();
			statusCode = 500;
			logger.error("Errors while transfer comparator status object to json string {}",e.getLocalizedMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
		return Response.status(statusCode).entity(message).build();
	}

	@PUT
	@Path("compare/result")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveComparatorData(JSONObject jsonObj) {
		infoInteractionLogAPIIncomingCall(null, null, "Service calling to (PUT) /v1/zdt/compare/result");
		EntityManager em = null;
		String message = null;
		int statusCode = 200;
		String comparisonDate = null;
		String nextScheduleDate = null;
		String comparisonType = null;
		String comparisonResult = null;
		double divergencePercentage = 0;
		if (jsonObj != null) {
			try {
				if (jsonObj.getString("lastComparisonDateTime") == null) {
					message = "comparison date time can not be null";
					statusCode = 500;
				} else {
					try {
						comparisonDate = jsonObj.getString("lastComparisonDateTime");
						comparisonType = jsonObj.getString("comparisonType");
						comparisonResult = jsonObj.getString("comparisonResult");
						divergencePercentage = jsonObj.getDouble("divergencePercentage");
						nextScheduleDate = jsonObj.getString("nextScheduledComparisonDateTime");
						em =  new DashboardServiceFacade().getEntityManager();
						int result = DataManager.getInstance().saveToComparatorTable(em, comparisonDate,nextScheduleDate,
								comparisonType, comparisonResult, divergencePercentage);
						if (result < 0) {
							message = "Error: error occurs while saving data to zdt comparator table";
							statusCode = 500;
						} else {
							message = "succeed to save data to zdt comparator table";
						}		
					} catch (Exception e) {
						statusCode = 500;
						logger.error("could not save data to comparator table, "+e.getLocalizedMessage());
						return Response.status(statusCode).entity("could not save data to comparator table").build();						
					} finally {
						if (em != null) {
							em.close();
						}
					}
				}
			} catch (JSONException e) {
				statusCode = 500;
				logger.error("could not save data to comparator table, "+e.getLocalizedMessage());
				return Response.status(statusCode).entity("could not save data to comparator table").build();	
			}
		}
	
		return Response.status(statusCode).entity(message).build();
	}

	

	private JSONArray getDashboardSetTableData(EntityManager em, String type, String date)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardSetTableData(em, type,date);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_SET, list);
	}

	private JSONArray getDashboardTableData(EntityManager em, String type, String date)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTableData(em, type,date);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD, list);
	}

	private JSONArray getDashboardTileParamsTableData(EntityManager em, String type, String date)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTileParamsTableData(em ,type,date);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_TILE_PARAMS, list);
	}

	private JSONArray getDashboardTileTableData(EntityManager em, String type, String date)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardTileTableData(em, type,date);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_TILES, list);
	}

	private JSONArray getDashboardUserOptionsTableData(EntityManager em, String type, String date)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getDashboardUserOptionsTableData(em, type,date);
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

	private JSONArray getPreferenceTableData(EntityManager em, String type, String date)
	{
		List<Map<String, Object>> list = DataManager.getInstance().getPreferenceTableData(em, type,date);
		return getJSONArrayForListOfObjects(TABLE_DATA_KEY_DASHBOARD_PREFERENCES, list);
	}
}
