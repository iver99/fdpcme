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

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.DashboardCountsComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstanceData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstancesComparedData;

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
			instanceName = data.getInstance().getKey();
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

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareOnDF()
	{
		logger.info("There is an incoming call from ZDT comparator API");
		// this comparator invokes the 2 instances REST APIs and retrieves the counts for objects (like dashboards), and return the counts for each instance
		DashboardCountsComparator dcc = new DashboardCountsComparator();
		InstancesComparedData<CountsEntity> result = dcc.compare();
		InstancesComapredCounts ic = new InstancesComapredCounts(new InstanceCounts(result.getInstance1()),
				new InstanceCounts(result.getInstance2()));
		return Response.status(Status.OK).entity(JsonUtil.buildNormalMapper().toJson(ic)).build();
	}
}
