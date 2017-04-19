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

import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.DashboardCountsComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.DashboardRowsComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstanceData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstancesComparedData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.ZDTStatusRowEntity;

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
	
	private Date getCurrentUTCTime()
	{
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		long localNow = System.currentTimeMillis();
		long offset = cal.getTimeZone().getOffset(localNow);
		Date utcDate = new Date(localNow - offset);
		
		return utcDate;
	}
	
	private String getTimeString(Date date)
	{
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	@GET
	@Path("compare")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareRows(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            @HeaderParam(value = "X-REMOTE-USER") String userTenant, @QueryParam("type") @DefaultValue("full")  String compareType) {
		logger.info("incoming call from zdt comparator to do row comparing");
		String message = "";
		int status = 200;
		try {
			DashboardRowsComparator dcc = new DashboardRowsComparator();
			InstancesComparedData<TableRowsEntity> result = dcc.compare(tenantIdParam, userTenant);
			
			if (result == null) {
				message = "Errors while comparing the two OMC instances.";
				status = 500;
			} else {
				int comparedDataNum = dcc.countForComparedRows(result.getInstance1().getData()) + dcc.countForComparedRows(result.getInstance2().getData());
				logger.info("comparedNum={}",comparedDataNum);
				int totalRow = result.getInstance1().getTotalRowNum() + result.getInstance2().getTotalRowNum();
				logger.info("totalRow={}",totalRow);
				double percen = (double)comparedDataNum/(double)totalRow;
				DecimalFormat df = new DecimalFormat("#.##");
				double percentage = Double.parseDouble(df.format(percen));
				Date currentUtcDate = getCurrentUTCTime();
				String comparisonDate = getTimeString(currentUtcDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentUtcDate);
				cal.add(Calendar.HOUR_OF_DAY, 6);
				Date nextScheduleDate = cal.getTime();
				String nextScheduleDateStr = getTimeString(nextScheduleDate);
				String type = "full";
				if (compareType.equals("incremental")) {
					type = "incremental";
				}
				ZDTStatusRowEntity statusRow = new ZDTStatusRowEntity(comparisonDate,type,nextScheduleDateStr,percentage);
				message = JsonUtil.buildNormalMapper().toJson(statusRow);
			}
		} catch (Exception e) {
			message = "Errors while comparing the two OMC instances, "+ e.getLocalizedMessage();
			status = 500;
		}
		
		return Response.status(status).entity(message).build();
	}

	@PUT
	@Path("sync")
	@Produces(MediaType.APPLICATION_JSON)
	public Response syncOnDF(@HeaderParam(value = "X-USER-IDENTITY-DOMAIN-NAME") String tenantIdParam,
            @HeaderParam(value = "X-REMOTE-USER") String userTenant)
	{
		logger.info("There is an incoming call from ZDT comparator API to sync");
		// this comparator invokes the 2 instances REST APIs and retrieves the different table rows for the 2 instances, and update the 2 instances accordingly
		DashboardRowsComparator dcc = new DashboardRowsComparator();
		InstancesComparedData<TableRowsEntity> result = dcc.compare(tenantIdParam, userTenant);
		try {
			dcc.sync(result, tenantIdParam, userTenant);
			return Response.status(Status.NO_CONTENT).build();
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to sync data for the 2 instances").build();
		}
	}
	
	public Long getTenantId(String tenantId)
	{
		if (tenantId == null) {
			logger.error("No tenant information");
			return null;
		}
					
			try {
				long internalTenantId = TenantIdProcessor.getInternalTenantIdFromOpcTenantId(tenantId);
				logger.info("Get internal tenant id {} from opc tenant id {}", internalTenantId, tenantId);
				return internalTenantId;
			} catch (BasicServiceMalfunctionException e) {
				logger.error("could not get internal tenantId from OPC tenant Id {}",e.getLocalizedMessage());
			}
			
			return null;
		

	}
}
