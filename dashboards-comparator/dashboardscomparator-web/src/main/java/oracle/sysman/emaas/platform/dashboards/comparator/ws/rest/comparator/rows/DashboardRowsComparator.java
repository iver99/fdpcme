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
import java.util.HashMap;
import java.util.List;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.RestClientProxy;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.RowEntityComparator.CompareListPair;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardSetRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardTileParamsRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardTileRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardUserOptionsRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.PreferenceRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author guochen
 */
public class DashboardRowsComparator extends AbstractComparator
{
	private static final Logger logger = LogManager.getLogger(DashboardRowsComparator.class);

	public InstancesComparedData<TableRowsEntity> compare(String tenantId, String userTenant)
	{
		try {
			logger.info("Starts to compare the two DF OMC instances: table by table and row by row");
			HashMap<String, LookupClient> instances = getOMCInstances();
			if (instances == null) {
				logger.error("Failed to retrieve ZDT OMC instances: null retrieved");
				return null;
			}
			
			String key1 = null;
			String key2 = null;
			LookupClient client1 = null;
			LookupClient client2 = null;
			for (String key : instances.keySet()) {
				if (client1 == null) {
					client1 = instances.get(key);
					key1 = key;
				} else {
					if (client2 == null)
					client2 = instances.get(key);
					key2 = key;
				}
			}
			
			TableRowsEntity tre1 = retrieveRowsForSingleInstance(client1, tenantId, userTenant);
			int rowNum1 = countForComparedRows(tre1);
			if (tre1 == null) {
				logger.error("Failed to retrieve ZDT table rows entity for instance {}", key1);
				logger.info("Completed to compare the two DF OMC instances");
				return null;
			}

			TableRowsEntity tre2 = retrieveRowsForSingleInstance(client2, tenantId, userTenant);
			int rowNum2 = countForComparedRows(tre2);
			if (tre2 == null) {
				logger.error("Failed to retrieve ZDT table rows entity for instance {}", key2);
				logger.info("Completed to compare the two DF OMC instances");
				return null;
			}
			InstancesComparedData<TableRowsEntity> cd = compareInstancesData(new InstanceData<TableRowsEntity>(key1, client1, tre1,rowNum1),
					new InstanceData<TableRowsEntity>(key1, client1, tre2,rowNum2));
			logger.info("Completed to compare the two DF OMC instances");
			return cd;
		}
		catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return null;
		}
	}
	
	public int countForComparedRows(TableRowsEntity tableRow) {
		int count = 0;
		
		count = count + (tableRow.getEmsDashboard()==null?0:tableRow.getEmsDashboard().size());
		count = count + (tableRow.getEmsDashboardSet()==null?0:tableRow.getEmsDashboardSet().size());
		count = count + (tableRow.getEmsDashboardTile()==null?0:tableRow.getEmsDashboardTile().size());
		count = count + (tableRow.getEmsDashboardTileParams()==null?0:tableRow.getEmsDashboardTileParams().size());
		count = count + (tableRow.getEmsDashboardUserOptions()==null?0:tableRow.getEmsDashboardUserOptions().size());
		count = count + (tableRow.getEmsPreference()==null?0:tableRow.getEmsPreference().size());
		return count;
	}

	public String sync(InstancesComparedData<TableRowsEntity> instancesData,String tenantId, String userTenant) throws Exception
	{
		if (instancesData == null) {
			return "Errors: Failed to retrieve ZDT OMC instances: null retrieved!";
		}
		// switch the data for the instances for sync
		InstanceData<TableRowsEntity> instance1 = new InstanceData<TableRowsEntity>(instancesData.getInstance1().getKey(),
				instancesData.getInstance1().getClient(),
				instancesData.getInstance2().getData(),0);
		InstanceData<TableRowsEntity> instance2 = new InstanceData<TableRowsEntity>(instancesData.getInstance2().getKey(),
				instancesData.getInstance2().getClient(),
				instancesData.getInstance1().getData(),0);
		InstancesComparedData<TableRowsEntity> syncData = new InstancesComparedData<TableRowsEntity>(instance1, instance2);
		syncForInstance(syncData.getInstance1(), tenantId, userTenant);
		syncForInstance(syncData.getInstance2(),  tenantId, userTenant);
		String message1 = syncForInstance(syncData.getInstance1(), tenantId, userTenant);
 		String message2 = syncForInstance(syncData.getInstance2(),  tenantId, userTenant);
 		if (message1 == null || message2 == null) {
 			return "Errors: Get a null or empty link for one single instance!";
 		}
 		return "cloud1: "+ message1 + "__cloud2: " + "{"+ message2+"}";
	}

	/**
	 * Compares the dashboard rows data for the 2 instances, and put the compare result into <code>ComparedData</code> object
	 *
	 * @param rows1
	 * @param rows2
	 * @param cd
	 */
	private void compareDashboardRows(List<DashboardRowEntity> rows1, List<DashboardRowEntity> rows2,
			InstancesComparedData<TableRowsEntity> cd)
	{
		if (cd == null) {
			return;
		}
		RowEntityComparator<DashboardRowEntity> rec = new RowEntityComparator<DashboardRowEntity>();
		CompareListPair<DashboardRowEntity> result = rec.compare(rows1, rows2);
		cd.getInstance1().getData().setEmsDashboard(result.getList1());
		cd.getInstance2().getData().setEmsDashboard(result.getList2());
	}

	/**
	 * Compares the dashboard set rows data for the 2 instances, and put the compare result into <code>ComparedData</code> object
	 *
	 * @param rows1
	 * @param rows2
	 * @param cd
	 */
	private void compareDashboardSetRows(List<DashboardSetRowEntity> rows1, List<DashboardSetRowEntity> rows2,
			InstancesComparedData<TableRowsEntity> cd)
	{
		if (cd == null) {
			return;
		}
		RowEntityComparator<DashboardSetRowEntity> rec = new RowEntityComparator<DashboardSetRowEntity>();
		CompareListPair<DashboardSetRowEntity> result = rec.compare(rows1, rows2);
		cd.getInstance1().getData().setEmsDashboardSet(result.getList1());
		cd.getInstance2().getData().setEmsDashboardSet(result.getList2());
	}

	/**
	 * Compares the dashboard tile params rows data for the 2 instances, and put the compare result into <code>ComparedData</code>
	 * object
	 *
	 * @param rows1
	 * @param rows2
	 * @param cd
	 */
	private void compareDashboardTileParamsRows(List<DashboardTileParamsRowEntity> rows1,
			List<DashboardTileParamsRowEntity> rows2, InstancesComparedData<TableRowsEntity> cd)
	{
		if (cd == null) {
			return;
		}
		RowEntityComparator<DashboardTileParamsRowEntity> rec = new RowEntityComparator<DashboardTileParamsRowEntity>();
		CompareListPair<DashboardTileParamsRowEntity> result = rec.compare(rows1, rows2);
		cd.getInstance1().getData().setEmsDashboardTileParams(result.getList1());
		cd.getInstance2().getData().setEmsDashboardTileParams(result.getList2());
	}

	/**
	 * Compares the dashboard tile rows data for the 2 instances, and put the compare result into <code>ComparedData</code> object
	 *
	 * @param rows1
	 * @param rows2
	 * @param cd
	 */
	private void compareDashboardTileRows(List<DashboardTileRowEntity> rows1, List<DashboardTileRowEntity> rows2,
			InstancesComparedData<TableRowsEntity> cd)
	{
		if (cd == null) {
			return;
		}
		RowEntityComparator<DashboardTileRowEntity> rec = new RowEntityComparator<DashboardTileRowEntity>();
		CompareListPair<DashboardTileRowEntity> result = rec.compare(rows1, rows2);
		cd.getInstance1().getData().setEmsDashboardTile(result.getList1());
		cd.getInstance2().getData().setEmsDashboardTile(result.getList2());
	}

	/**
	 * Compares the dashboard user options rows data for the 2 instances, and put the compare result into
	 * <code>ComparedData</code> object
	 *
	 * @param rows1
	 * @param rows2
	 * @param cd
	 */
	private void compareDashboardUserOptionsRows(List<DashboardUserOptionsRowEntity> rows1,
			List<DashboardUserOptionsRowEntity> rows2, InstancesComparedData<TableRowsEntity> cd)
	{
		if (cd == null) {
			return;
		}
		RowEntityComparator<DashboardUserOptionsRowEntity> rec = new RowEntityComparator<DashboardUserOptionsRowEntity>();
		CompareListPair<DashboardUserOptionsRowEntity> result = rec.compare(rows1, rows2);
		cd.getInstance1().getData().setEmsDashboardUserOptions(result.getList1());
		cd.getInstance2().getData().setEmsDashboardUserOptions(result.getList2());
	}

	private InstancesComparedData<TableRowsEntity> compareInstancesData(InstanceData<TableRowsEntity> insData1,
			InstanceData<TableRowsEntity> insData2)
	{
		if (insData1 == null || insData2 == null) {
			logger.error("Input instance data is null, can't compare and return null directly");
			return null;
		}
		// prepare the output compared data
		InstanceData<TableRowsEntity> outData1 = new InstanceData<TableRowsEntity>(insData1.getKey(), insData1.getClient(), new TableRowsEntity(),insData1.getTotalRowNum());
		InstanceData<TableRowsEntity> outData2 = new InstanceData<TableRowsEntity>(insData2.getKey(), insData2.getClient(), new TableRowsEntity(),insData2.getTotalRowNum());
		InstancesComparedData<TableRowsEntity> cd = new InstancesComparedData<TableRowsEntity>(outData1, outData2);
		compareDashboardRows(insData1.getData().getEmsDashboard(), insData2.getData().getEmsDashboard(), cd);
		compareDashboardSetRows(insData1.getData().getEmsDashboardSet(), insData2.getData().getEmsDashboardSet(), cd);
		compareDashboardTileRows(insData1.getData().getEmsDashboardTile(), insData2.getData().getEmsDashboardTile(), cd);
		compareDashboardTileParamsRows(insData1.getData().getEmsDashboardTileParams(),
				insData2.getData().getEmsDashboardTileParams(), cd);
		compareDashboardUserOptionsRows(insData1.getData().getEmsDashboardUserOptions(),
				insData2.getData().getEmsDashboardUserOptions(), cd);
		comparePreferenceRows(insData1.getData().getEmsPreference(), insData2.getData().getEmsPreference(), cd);
		return cd;
	}

	/**
	 * Compares the preference rows data for the 2 instances, and put the compare result into <code>ComparedData</code> object
	 *
	 * @param rows1
	 * @param rows2
	 * @param cd
	 */
	private void comparePreferenceRows(List<PreferenceRowEntity> rows1, List<PreferenceRowEntity> rows2,
			InstancesComparedData<TableRowsEntity> cd)
	{
		if (cd == null) {
			return;
		}
		RowEntityComparator<PreferenceRowEntity> rec = new RowEntityComparator<PreferenceRowEntity>();
		CompareListPair<PreferenceRowEntity> result = rec.compare(rows1, rows2);
		cd.getInstance1().getData().setEmsPreference(result.getList1());
		cd.getInstance2().getData().setEmsPreference(result.getList2());
	}

	/**
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private TableRowsEntity retrieveRowsEntityFromJsonForSingleInstance(String response) throws IOException
	{
		JsonUtil ju = JsonUtil.buildNormalMapper();
		TableRowsEntity tre = ju.fromJson(response, TableRowsEntity.class);
		if (tre == null) {
			logger.warn("Checking dashboard OMC instance table rows: null/empty entity retrieved.");
			return null;
		}
		return tre;
	}

	/**
	 * @throws Exception
	 * @throws IOException
	 */
	private TableRowsEntity retrieveRowsForSingleInstance(LookupClient lc, String tenantId, String userTenant) throws Exception, IOException
	{
		Link lk = getSingleInstanceUrl(lc, "zdt/tablerows", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			return null;
		}
		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME, tenantId);
		rc.setHeader(RestClient.X_REMOTE_USER, userTenant);
		//char[] authToken = RegistrationManager.getInstance().getAuthorizationToken();
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		String response = rc.get(lk.getHref(), tenantId, new String(authToken));
		logger.info("Checking dashboard OMC instance table rows. Response is " + response);
		return retrieveRowsEntityFromJsonForSingleInstance(response);
	}

	private String syncForInstance(InstanceData<TableRowsEntity> instance, String tenantId, String userTenant) throws Exception
	{
		Link lk = getSingleInstanceUrl(instance.getClient(), "zdt/sync", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			return null;
		}
		logger.info("print the sync data {} !",instance.getData());
		TableRowsEntity entity = instance.getData();
		JsonUtil jsonUtil = JsonUtil.buildNonNullMapper();
		jsonUtil.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String entityStr = jsonUtil.toJson(entity);
		logger.info("print the put data {} !",entityStr);
		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME,tenantId);
		rc.setHeader(RestClient.X_REMOTE_USER,userTenant);
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		String response = rc.put(lk.getHref(), entityStr, tenantId, new String(authToken));
		logger.info("Checking sync reponse. Response is " + response);
		return response;
	}
}
