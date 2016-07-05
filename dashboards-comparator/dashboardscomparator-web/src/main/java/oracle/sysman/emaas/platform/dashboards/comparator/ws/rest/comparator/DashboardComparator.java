/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceQuery;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.TenantSubscriptionUtil;

/**
 * @author guochen
 */
public class DashboardComparator
{
	private static final Logger logger = LogManager.getLogger(DashboardComparator.class);

	private static final String DF_SERVICE_NAME = "Dashboard-API";
	private static final String DF_VERSION = "1.0+";
	private static final int ZDT_INSTANCES_AMOUNT = 2;

	/**
	 * TODO: From this wiki
	 * page(https://confluence.oraclecorp.com/confluence/display/EMS/ZDT+-+API+Gateway+Manual+Deployment+and+Developer+Docs), I
	 * got the description: It will publish this information over an omcinstances rest api so that other services in this tier can
	 * get this information We will also publish an SDK with the following signature (that uses the rest sdk) HashMap<String,
	 * LookupClient> getOMCInstanceClients();
	 */
	public void compare()
	{
		try {
			logger.info("Starts to compare the two DF OMC instances");
			// TODO: current code is using EMPTY/INVALID lookup clients. In the real env, it's needed to get real lookup clients for 2 instances and compare!
			//HashMap<String,LookupClient> lcMap = RegistrySDK.getOMCInstanceClients();
			HashMap<String, LookupClient> lcMap = new HashMap<String, LookupClient>();
			Set<Entry<String, LookupClient>> entrySet = lcMap.entrySet();

			if (entrySet.size() != ZDT_INSTANCES_AMOUNT) {
				logger.error(
						"Unexpected: retrieved unexpected amount of OMC instances from SDK method. Expected amount is {}, and actually amount is {}",
						ZDT_INSTANCES_AMOUNT, entrySet.size());
				logger.info("Completed to compare the two DF OMC instances");
				return;
			}
			Iterator<Entry<String, LookupClient>> itr = entrySet.iterator();

			Entry<String, LookupClient> ins1 = itr.next();
			ZDTEntity ze1 = retrieveCountsForSingleInstance(ins1.getValue());
			if (ze1 == null) {
				logger.error("Failed to retrieve ZDT count entity for instance {}", ins1.getKey());
				logger.info("Completed to compare the two DF OMC instances");
				return;
			}

			Entry<String, LookupClient> ins2 = itr.next();
			ZDTEntity ze2 = retrieveCountsForSingleInstance(ins2.getValue());
			if (ze2 == null) {
				logger.error("Failed to retrieve ZDT count entity for instance {}", ins2.getKey());
				logger.info("Completed to compare the two DF OMC instances");
				return;
			}

			// now compare
			boolean allMatch = true;
			if (ze1.getCountOfDashboards() != ze2.getCountOfDashboards()) {
				logger.error("Dashboards count does not match. In instance \"{}\", count is {}. In instance \"{}\", count is {}",
						ins1.getKey(), ze1.getCountOfDashboards(), ins2.getKey(), ze2.getCountOfDashboards());
				allMatch = false;
			}
			if (ze1.getCountOfFavorite() != ze2.getCountOfFavorite()) {
				logger.error("Favorites count does not match. In instance \"{}\", count is {}. In instance \"{}\", count is {}",
						ins1.getKey(), ze1.getCountOfFavorite(), ins2.getKey(), ze2.getCountOfFavorite());
				allMatch = false;
			}
			if (ze1.getCountOfPreference() != ze2.getCountOfPreference()) {
				logger.error("Preferences count does not match. In instance \"{}\", count is {}. In instance \"{}\", count is {}",
						ins1.getKey(), ze1.getCountOfPreference(), ins2.getKey(), ze2.getCountOfPreference());
				allMatch = false;
			}
			if (allMatch) {
				logger.info("All counts from both instances (instance \"{}\" and instance \"{}\") match!", ins1.getKey(),
						ins2.getKey());
			}
			logger.info("Completed to compare the two DF OMC instances");
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private Link getSingleInstanceUrl(LookupClient lc) throws Exception
	{
		if (lc == null) {
			logger.error("Failed to lookup instance for specified lookup client is null!");
			return null;
		}
		// TODO: following are testing code only!
		InstanceInfo info = InstanceInfo.Builder.newBuilder().withServiceName(DF_SERVICE_NAME).withVersion(DF_VERSION).build();
		List<InstanceInfo> result = lc.lookup(new InstanceQuery(info));
		Link lk = null;
		if (result != null && result.size() > 0) {
			// [EMCPDF-733] Rest client can't handle https currently, so http protocol is enough for internal use
			// find http link only
			for (InstanceInfo internalInstance : result) {
				List<Link> links = internalInstance.getLinksWithProtocol("zdt/counts", "http");
				if (links != null && links.size() > 0) {
					lk = links.get(0);
					return lk;
				}
			}
		}
		return lk;
	}

	/**
	 * @throws Exception
	 * @throws IOException
	 */
	private ZDTEntity retrieveCountsForSingleInstance(LookupClient lc) throws Exception, IOException
	{
		Link lk;
		lk = getSingleInstanceUrl(lc);
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			return null;
		}
		String response = new TenantSubscriptionUtil.RestClient().get(lk.getHref(), null);
		logger.info("Checking dashboard OMC instance counts. Response is " + response);
		JsonUtil ju = JsonUtil.buildNormalMapper();
		ZDTEntity ze = ju.fromJson(response, ZDTEntity.class);
		if (ze == null) {
			logger.warn("Checking dashboard OMC instance counts: null/empty entity retrieved.");
			return null;
		}
		// TODO: for the 1st implementation, let's log in log files then
		logger.info(
				"Retrieved counts for dashboards OMC instance: dashboard count - {}, favorites count - {}, preference count - {}",
				ze.getCountOfDashboards(), ze.getCountOfFavorite(), ze.getCountOfPreference());
		return ze;
	}
}
