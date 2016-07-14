/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.DashboardCountsComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;

/**
 * @author guochen
 */
public class DashboardRowsComparator extends AbstractComparator
{
	private static final Logger logger = LogManager.getLogger(DashboardCountsComparator.class);

	public void compare()
	{
		try {
			logger.info("Starts to compare the two DF OMC instances: table by table and row by row");
			Entry<String, LookupClient>[] instances = getOMCInstances();
			if (instances == null) {
				logger.error("Failed to retrieve ZDT OMC instances: null retrieved");
				return;
			}
			Entry<String, LookupClient> ins1 = instances[0];
			TableRowsEntity tre1 = retrieveRowsForSingleInstance(ins1.getValue());
			if (tre1 == null) {
				logger.error("Failed to retrieve ZDT table rows entity for instance {}", ins1.getKey());
				logger.info("Completed to compare the two DF OMC instances");
				return;
			}

			Entry<String, LookupClient> ins2 = instances[1];
			TableRowsEntity tre2 = retrieveRowsForSingleInstance(ins2.getValue());
			if (tre2 == null) {
				logger.error("Failed to retrieve ZDT table rows entity for instance {}", ins2.getKey());
				logger.info("Completed to compare the two DF OMC instances");
				return;
			}
			logger.info("Completed to compare the two DF OMC instances");
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @throws Exception
	 * @throws IOException
	 */
	private TableRowsEntity retrieveRowsForSingleInstance(LookupClient lc) throws Exception, IOException
	{
		Link lk = getSingleInstanceUrl(lc, "zdt/tablerows", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			return null;
		}
		String response = new TenantSubscriptionUtil.RestClient().get(lk.getHref(), null);
		logger.info("Checking dashboard OMC instance table rows. Response is " + response);
		return retrieveRowsEntityFromJsonForSingleInstance(response);
	}

	/**
	 * @param response
	 * @return
	 * @throws IOException
	 */
	TableRowsEntity retrieveRowsEntityFromJsonForSingleInstance(String response) throws IOException
	{
		JsonUtil ju = JsonUtil.buildNormalMapper();
		TableRowsEntity tre = ju.fromJson(response, TableRowsEntity.class);
		if (tre == null) {
			logger.warn("Checking dashboard OMC instance table rows: null/empty entity retrieved.");
			return null;
		}
		return tre;
	}
}
