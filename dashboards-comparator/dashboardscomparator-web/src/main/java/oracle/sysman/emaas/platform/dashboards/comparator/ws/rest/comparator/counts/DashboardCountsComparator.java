/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts;

import java.io.IOException;
import java.util.Map.Entry;

import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstancesComparedData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstanceData;

/**
 * @author guochen
 */
public class DashboardCountsComparator extends AbstractComparator
{
	private static final Logger logger = LogManager.getLogger(DashboardCountsComparator.class);

	public InstancesComparedData<CountsEntity> compare()
	{
		try {
			logger.info("Starts to compare the two DF OMC instances");
			Entry<String, LookupClient>[] instances = getOMCInstances();
			if (instances == null) {
				logger.error("Failed to retrieve ZDT OMC instances");
				return null;
			}

			Entry<String, LookupClient> ins1 = instances[0];
			CountsEntity ze1 = retrieveCountsForSingleInstance(ins1.getValue());
			if (ze1 == null) {
				logger.error("Failed to retrieve ZDT count entity for instance {}", ins1.getKey());
				logger.info("Completed to compare the two DF OMC instances");
				return null;
			}

			Entry<String, LookupClient> ins2 = instances[1];
			CountsEntity ze2 = retrieveCountsForSingleInstance(ins2.getValue());
			if (ze2 == null) {
				logger.error("Failed to retrieve ZDT count entity for instance {}", ins2.getKey());
				logger.info("Completed to compare the two DF OMC instances");
				return null;
			}

			// now compare
			InstancesComparedData<CountsEntity> cd = compareInstancesCounts(ins1, ze1, ins2, ze2);
			logger.info("Completed to compare the two DF OMC instances");
			return cd;
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return null;
		}
	}

	/**
	 * Compare the data/counts from the 2 instances<br>
	 * The returned <code>ComparedData</code> will contain only the different counts, and leave the same counts null
	 *
	 * @param ins1
	 * @param ze1
	 * @param ins2
	 * @param ze2
	 */
	private InstancesComparedData<CountsEntity> compareInstancesCounts(Entry<String, LookupClient> ins1, CountsEntity ze1,
			Entry<String, LookupClient> ins2, CountsEntity ze2)
	{
		CountsEntity differentCountsForInstance1 = new CountsEntity();
		CountsEntity differentCountsForInstance2 = new CountsEntity();
		boolean allMatch = true;
		if (ze1.getCountOfDashboards() != ze2.getCountOfDashboards()) {
			logger.error("Dashboards count does not match. In instance \"{}\", count is {}. In instance \"{}\", count is {}",
					ins1.getKey(), ze1.getCountOfDashboards(), ins2.getKey(), ze2.getCountOfDashboards());
			differentCountsForInstance1.setCountOfDashboards(ze1.getCountOfDashboards());
			differentCountsForInstance2.setCountOfDashboards(ze2.getCountOfDashboards());
			allMatch = false;
		}
		if (ze1.getCountOfFavorite() != ze2.getCountOfFavorite()) {
			logger.error("Favorites count does not match. In instance \"{}\", count is {}. In instance \"{}\", count is {}",
					ins1.getKey(), ze1.getCountOfFavorite(), ins2.getKey(), ze2.getCountOfFavorite());
			differentCountsForInstance1.setCountOfFavorite(ze1.getCountOfFavorite());
			differentCountsForInstance2.setCountOfFavorite(ze2.getCountOfFavorite());
			allMatch = false;
		}
		if (ze1.getCountOfPreference() != ze2.getCountOfPreference()) {
			logger.error("Preferences count does not match. In instance \"{}\", count is {}. In instance \"{}\", count is {}",
					ins1.getKey(), ze1.getCountOfPreference(), ins2.getKey(), ze2.getCountOfPreference());
			differentCountsForInstance1.setCountOfPreference(ze1.getCountOfPreference());
			differentCountsForInstance2.setCountOfPreference(ze2.getCountOfFavorite());
			allMatch = false;
		}
		if (allMatch) {
			logger.info("All counts from both instances (instance \"{}\" and instance \"{}\") match!", ins1.getKey(),
					ins2.getKey());
		}
		InstanceData<CountsEntity> instance1 = new InstanceData<CountsEntity>(ins1, differentCountsForInstance1);
		InstanceData<CountsEntity> instance2 = new InstanceData<CountsEntity>(ins2, differentCountsForInstance2);
		InstancesComparedData<CountsEntity> cd = new InstancesComparedData<CountsEntity>(instance1, instance2);
		return cd;
	}

	/**
	 * @throws Exception
	 * @throws IOException
	 */
	private CountsEntity retrieveCountsForSingleInstance(LookupClient lc) throws Exception, IOException
	{
		Link lk = getSingleInstanceUrl(lc, "zdt/counts", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			return null;
		}
		String response = new RestClient().get(lk.getHref(), null);
		logger.info("Checking dashboard OMC instance counts. Response is " + response);
		JsonUtil ju = JsonUtil.buildNormalMapper();
		CountsEntity ze = ju.fromJson(response, CountsEntity.class);
		if (ze == null) {
			logger.warn("Checking dashboard OMC instance counts: null/empty entity retrieved.");
			return null;
		}
		// TODO: for the 1st step implementation, let's log in log files then
		logger.info(
				"Retrieved counts for dashboards OMC instance: dashboard count - {}, favorites count - {}, preference count - {}",
				ze.getCountOfDashboards(), ze.getCountOfFavorite(), ze.getCountOfPreference());
		return ze;
	}
}
