/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.comparator.exception.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.exception.ZDTErrorConstants;
import oracle.sysman.emaas.platform.dashboards.comparator.exception.ZDTException;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.DashboardCountsComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.DashboardRowsComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstanceData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstancesComparedData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.ZDTComparatorStatusRowEntity;

/**
 * @author guochen
 */
@Path("/v1/comparator")
public class ZDTAPI
{
	public static class InstanceCounts
	{
		private String instanceName;
		private CountsEntity counts;

		public InstanceCounts(InstanceData<CountsEntity> data)
		{
			instanceName = data.getKey();
			counts = data.getData();
		}

		/**
		 * @return the counts
		 */
		public CountsEntity getCounts()
		{
			return counts;
		}

		/**
		 * @return the instanceName
		 */
		public String getInstanceName()
		{
			return instanceName;
		}

		/**
		 * @param counts
		 *            the counts to set
		 */
		public void setCounts(CountsEntity counts)
		{
			this.counts = counts;
		}

		/**
		 * @param instanceName
		 *            the instanceName to set
		 */
		public void setInstanceName(String instanceName)
		{
			this.instanceName = instanceName;
		}
	}

	public static class InstancesComapredCounts
	{
		private InstanceCounts instance1;
		private InstanceCounts instance2;

		/**
		 * @param instance1
		 * @param instance2
		 */
		public InstancesComapredCounts(InstanceCounts instance1, InstanceCounts instance2)
		{
			super();
			this.instance1 = instance1;
			this.instance2 = instance2;
		}

		/**
		 * @return the instance1
		 */
		public InstanceCounts getInstance1()
		{
			return instance1;
		}

		/**
		 * @return the instance2
		 */
		public InstanceCounts getInstance2()
		{
			return instance2;
		}

		/**
		 * @param instance1
		 *            the instance1 to set
		 */
		public void setInstance1(InstanceCounts instance1)
		{
			this.instance1 = instance1;
		}

		/**
		 * @param instance2
		 *            the instance2 to set
		 */
		public void setInstance2(InstanceCounts instance2)
		{
			this.instance2 = instance2;
		}
	}

	private static final Logger logger = LogManager.getLogger(ZDTAPI.class);

	public ZDTAPI()
	{
		super();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareOnDF(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            @HeaderParam(value = "X-REMOTE-USER") String userTenant)
	{
		logger.info("There is an incoming call from ZDT comparator API to compare");
		// this comparator invokes the 2 instances REST APIs and retrieves the counts for objects (like dashboards), and return the counts for each instance
		/*Long tenantId = null;
        tenantId = getTenantId(tenantIdParam);*/
		DashboardCountsComparator dcc = new DashboardCountsComparator();
		InstancesComparedData<CountsEntity> result = dcc.compare(tenantIdParam, userTenant);
		InstancesComapredCounts ic = new InstancesComapredCounts(new InstanceCounts(result.getInstance1()),
				new InstanceCounts(result.getInstance2()));

		return Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(ic)).build();
	}
	
	public Date getCurrentUTCTime()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		long localNow = System.currentTimeMillis();
		long offset = cal.getTimeZone().getOffset(localNow);
		Date utcDate = new Date(localNow - offset);
		
		return utcDate;
	}
	
	public String getTimeString(Date date)
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	@GET
	@Path("compare/status")
	public Response getCompareStatus(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            @HeaderParam(value = "X-REMOTE-USER") String userTenant) {
		logger.info("incoming call from zdt comparator to get comparitor status");
		DashboardRowsComparator dcc = null;
		String response = null;
		try {
			dcc = new DashboardRowsComparator();
			response = dcc.retrieveComparatorStatusForOmcInstance(tenantIdParam, userTenant);
		} catch (ZDTException e1) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e1))).build();
		} catch (Exception e2) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e2))).build();
		}
		
		return Response.status(Status.OK).entity(response).build();
		
	}
	
	@GET
	@Path("sync/status")
	public Response getSyncStatus(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            @HeaderParam(value = "X-REMOTE-USER") String userTenant) {
		logger.info("incoming call from zdt comparator to get sync status");
		DashboardRowsComparator dcc = null;
		String response = null;
		try {
			dcc = new DashboardRowsComparator();
			response = dcc.retrieveSyncStatusForOmcInstance(tenantIdParam, userTenant);
		} catch (ZDTException e1) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e1))).build();
		} catch (Exception e2) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e2))).build();
		}
		
		return Response.status(Status.OK).entity(response).build();
		
	}
	
	private String getMaxTimeStampStr(int skipMinutes) {
		Date currentUtcDate = getCurrentUTCTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentUtcDate);
		cal.add(Calendar.MINUTE, (skipMinutes * (-1)));
		Date maxTimeStamp = cal.getTime();
		String maxTimeStampStr = getTimeString(maxTimeStamp);
		return maxTimeStampStr;
	}
	
	@GET
	@Path("compare")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareRows(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            @HeaderParam(value = "X-REMOTE-USER") String userTenant, @QueryParam("type") @DefaultValue("incremental")  String compareType,
            @QueryParam("before") int skipMinutes) {
		logger.info("incoming call from zdt comparator to do row comparing");
		String message = "";
		int status = 200;
		if (compareType == null) {
			compareType = "incremental";
		}
		if (skipMinutes <= 0) {
			skipMinutes = 5;    // to check: what is the accurate default skipping time for comparator?
		}
		String maxComparedDate = null;
		if (skipMinutes > 0) {
			maxComparedDate = getMaxTimeStampStr(skipMinutes);
		}
		logger.info("the max compared date is "+maxComparedDate);
		
		try {
			DashboardRowsComparator dcc = new DashboardRowsComparator();
			InstancesComparedData<TableRowsEntity> result = dcc.compare(tenantIdParam, userTenant,compareType,maxComparedDate);
			
			if (result != null) {
				int comparedDataNum = dcc.countForComparedRows(result.getInstance1().getData()) + dcc.countForComparedRows(result.getInstance2().getData());
				logger.info("comparedNum={}",comparedDataNum);
				int totalRow = result.getInstance1().getTotalRowNum() + result.getInstance2().getTotalRowNum();
				logger.info("totalRow={}",totalRow);
				double percen = (double)comparedDataNum/(double)totalRow;
				DecimalFormat df = new DecimalFormat("#.######");
				double percentage = Double.parseDouble(df.format(percen));
				Date currentUtcDate = getCurrentUTCTime();
				String comparisonDate = getTimeString(currentUtcDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentUtcDate);
				cal.add(Calendar.HOUR_OF_DAY, 6);
				Date nextScheduleDate = cal.getTime();
				String nextScheduleDateStr = getTimeString(nextScheduleDate);
				JsonUtil jsonUtil = JsonUtil.buildNormalMapper();
				
				TableRowsEntity data1 = result.getInstance1().getData();
				String result1 = jsonUtil.toJson(data1);
				ZDTComparatorStatusRowEntity statusRow1 = new ZDTComparatorStatusRowEntity(comparisonDate,compareType,nextScheduleDateStr,percentage, result1);
								
				TableRowsEntity data2 = result.getInstance2().getData();
				String result2 = jsonUtil.toJson(data2);
				ZDTComparatorStatusRowEntity statusRow2 = new ZDTComparatorStatusRowEntity(comparisonDate,compareType,nextScheduleDateStr,percentage, result2);
				
				// save status information for client 1  -- switch data for sync
				LookupClient client1 = result.getInstance1().getClient();
				dcc.saveComparatorStatus(tenantIdParam,userTenant, client1, statusRow2);
				
				// save status informantion for client 2 -- switch data for sync
				LookupClient client2 = result.getInstance2().getClient();
				dcc.saveComparatorStatus(tenantIdParam,userTenant, client2, statusRow1);
				
				JSONObject obj = new JSONObject();
				obj.put("comparisonDateTime", comparisonDate);
				obj.put("comparisonType", compareType);
				obj.put("divergencePercentage", percentage);
				
				JSONObject subObj = new JSONObject();
				subObj.put(result.getInstance1().getKey(), result2);
				subObj.put(result.getInstance2().getKey(), result1);
				obj.put("divergenceSummary", subObj);
			
				message = obj.toString();
				
			} else {
				Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(ZDTErrorConstants.NULL_LINK_ERROR_CODE, ZDTErrorConstants.NULL_LINK_ERROR_MESSAGE))).build();
			}
		} catch(ZDTException zdtE) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(zdtE))).build();
		}
		catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e))).build();
		}
		
		return Response.status(status).entity(message).build();
	}

	@GET
	@Path("sync")
	@Produces(MediaType.APPLICATION_JSON)
	public Response syncOnDF(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            @HeaderParam(value = "X-REMOTE-USER") String userTenant,@QueryParam("type") @DefaultValue("full")  String syncType)
	{
		logger.info("There is an incoming call from ZDT comparator API to sync");
		// this comparator invokes the 2 instances REST APIs and retrieves the different table rows for the 2 instances, and update the 2 instances accordingly
		DashboardRowsComparator dcc = null;
		if (syncType == null) {
			syncType = "full";
		}
		try {
			dcc = new DashboardRowsComparator();		
			Date currentUtcDate = getCurrentUTCTime();
			String syncDate = getTimeString(currentUtcDate);
			
			String message1 = dcc.syncForInstance(tenantIdParam,  userTenant,  dcc.getClient1(),  syncType,  syncDate);
			String message2 = dcc.syncForInstance(tenantIdParam,  userTenant,  dcc.getClient2(),  syncType,  syncDate);
			if (message1.contains("Errors") || message2.contains("Errors")) {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(ZDTErrorConstants.FAIL_TO_SYNC_ERROR_CODE, ZDTErrorConstants.FAIL_TO_SYNC_ERROR_MESSAGE))).build();
			}
			return Response.ok(dcc.getKey1() + ":{"+ message1 + "} " + dcc.getKey2() + ":{"+ message2 + "}").build();
	    } catch(ZDTException zdtE) {
 			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(zdtE))).build();
 		} catch (Exception e) {
 			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(JsonUtil.buildNormalMapper().toJson(new ErrorEntity(e))).build();
 		}
		
	}

}
