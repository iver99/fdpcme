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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.dashboards.comparator.exception.ZDTErrorConstants;
import oracle.sysman.emaas.platform.dashboards.comparator.exception.ZDTException;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.RestClientProxy;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.RowEntityComparator.CompareListPair;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardSetRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardTileParamsRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardTileRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardUserOptionsRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.PreferenceRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.ZDTComparatorStatusRowEntity;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import oracle.sysman.emaas.platform.emcpdf.registry.RegistryLookupUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;


/**
 * @author guochen
 */
public class DashboardRowsComparator extends AbstractComparator
{
	private static final Logger logger = LogManager.getLogger(DashboardRowsComparator.class);
	
	private  String key1 = null;
	private String key2 = null;
	private  LookupClient client1 = null;
	private  LookupClient client2 = null;
	

	public DashboardRowsComparator() throws ZDTException {
		super();
		HashMap<String, LookupClient> instances = getOMCInstances();
		if (instances == null) {
			logger.error("Failed to retrieve ZDT OMC instances: null retrieved");
			throw new ZDTException(ZDTErrorConstants.NULL_RETRIEVED_ERROR_CODE,ZDTErrorConstants.NULL_RETRIEVED_ERROR_MESSAGE);
		}
		 
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
	}
	
	public TableRowsEntity combineRowEntity(List<TableRowsEntity> rowEntityList){
		TableRowsEntity finalEntity = new TableRowsEntity();
		List<DashboardRowEntity> dashboard = new ArrayList<DashboardRowEntity>();
		List<DashboardSetRowEntity> dashboardSet = new ArrayList<DashboardSetRowEntity>();
		List<DashboardTileRowEntity> dashboardTile = new ArrayList<DashboardTileRowEntity>();
		List<DashboardTileParamsRowEntity> dashboardTileParams = new ArrayList<DashboardTileParamsRowEntity>();
		List<PreferenceRowEntity> preference = new ArrayList<PreferenceRowEntity>();
		List<DashboardUserOptionsRowEntity> userOptions = new ArrayList<DashboardUserOptionsRowEntity>();
		for (TableRowsEntity entity : rowEntityList) {
			if (entity.getEmsDashboard() != null && !entity.getEmsDashboard().isEmpty()) {
				dashboard.addAll(entity.getEmsDashboard());
			}
			if (entity.getEmsDashboardSet()!= null && !entity.getEmsDashboardSet().isEmpty()) {
				dashboardSet.addAll(entity.getEmsDashboardSet());
			}
			if (entity.getEmsDashboardTile() != null && !entity.getEmsDashboardTile().isEmpty()) {
				dashboardTile.addAll(entity.getEmsDashboardTile());
			}
			if (entity.getEmsDashboardTileParams() != null && !entity.getEmsDashboardTileParams().isEmpty()) {
				dashboardTileParams.addAll(entity.getEmsDashboardTileParams());
			}
			if (entity.getEmsPreference() != null && !entity.getEmsPreference().isEmpty()) {
				preference.addAll(entity.getEmsPreference());
			}
			if (entity.getEmsDashboardUserOptions() != null && !entity.getEmsDashboardUserOptions().isEmpty()) {
				userOptions.addAll(entity.getEmsDashboardUserOptions());
			}
		}
		finalEntity.setEmsDashboard(dashboard);
		finalEntity.setEmsDashboardSet(dashboardSet);
		finalEntity.setEmsDashboardTile(dashboardTile);
		finalEntity.setEmsDashboardTileParams(dashboardTileParams);
		finalEntity.setEmsPreference(preference);
		finalEntity.setEmsDashboardUserOptions(userOptions);
		return finalEntity;
	}

	public InstancesComparedData<TableRowsEntity> compare(String tenantId, String userTenant, String comparisonType, 
			String maxComparedDate,boolean iscompared, JSONObject tenantObj) throws ZDTException
	{
		try {
			logger.info("Starts to compare the two DF OMC instances: table by table and row by row");
			
			//logger.info("key1={}, client1={}",key1, client1.getServiceUrls().get(0).toString());
			
			//logger.info("key2={}, client1={}",key2, client2.getServiceUrls().get(0).toString());
			
			TableRowsEntity tre1 = null;
			TableRowsEntity tre2 = null;
			if (!iscompared || comparisonType == "full") {
				// for the first time comparing, fetch all table data tenant by tenant
				// for client1
				List<TableRowsEntity> allRowEntitisForClient1 = new ArrayList<TableRowsEntity>();
				JSONArray array1 = tenantObj.getJSONArray("client1");
				for (int i = 0; i < array1.length(); i++) {
					String tenant = array1.get(i).toString();
					TableRowsEntity subTre = retrieveRowsForSingleInstance(client1, tenantId, userTenant, 
							comparisonType, maxComparedDate,tenant);
					if (subTre == null) {
						logger.error("Failed to retrieve ZDT table rows entity for instance {}", key1);
						logger.info("Completed to compare the two ssf OMC instances");
						return null;
					}
					allRowEntitisForClient1.add(subTre);
				}
				tre1 = combineRowEntity(allRowEntitisForClient1);
				
				logger.info("tre1 dashboard size = "+tre1.getEmsDashboard().size());
				logger.info("tre1 dashboard tile size = "+tre1.getEmsDashboardTile().size());
				// for client2
				List<TableRowsEntity> allRowEntitisForClient2 = new ArrayList<TableRowsEntity>();
				JSONArray array2 = tenantObj.getJSONArray("client2");
				for (int i = 0; i < array2.length(); i++) {
					String tenant = array2.get(i).toString();
					TableRowsEntity subTre = retrieveRowsForSingleInstance(client2, tenantId, userTenant, 
							comparisonType, maxComparedDate,tenant);
					allRowEntitisForClient2.add(subTre);
				}
				tre2 = combineRowEntity(allRowEntitisForClient2);
				logger.info("tre2 dashboard size = "+tre2.getEmsDashboard().size());
				logger.info("tre2 dashboard tile size = "+tre2.getEmsDashboardTile().size());
			} else {
				tre1 = retrieveRowsForSingleInstance(client1, tenantId, userTenant, 
						comparisonType, maxComparedDate,null);
				tre2 = retrieveRowsForSingleInstance(client2, tenantId, userTenant, 
						comparisonType, maxComparedDate,null);
			}
						
			CountsEntity entity1 = retrieveCountsForSingleInstance(tenantId, userTenant,client1, maxComparedDate);
			if (entity1 == null) {
				return null;
			}
			int rowNum1 = (int)(entity1.getCountOfDashboards()
					+ entity1.getCountOfPreference()
					+ entity1.getCountOfDashboardSet()
					+ entity1.getCountOfTile()
					+ entity1.getCountOfTileParam() 
					+ entity1.getCountOfUserOptions());
			if (tre1 == null) {
				logger.error("Failed to retrieve ZDT table rows entity for instance {}", key1);
				logger.info("Completed to compare the two DF OMC instances");
				return null;
			}

			CountsEntity entity2 = retrieveCountsForSingleInstance(tenantId, userTenant,client2,maxComparedDate);
			if (entity2 == null) {
				return null;
			}
			int rowNum2 = (int)(entity2.getCountOfDashboards()
					+ entity2.getCountOfPreference()
					+ entity2.getCountOfDashboardSet()
					+ entity2.getCountOfTile()
					+ entity2.getCountOfTileParam() 
					+ entity2.getCountOfUserOptions());
			if (tre2 == null) {
				logger.error("Failed to retrieve ZDT table rows entity for instance {}", key2);
				logger.info("Completed to compare the two DF OMC instances");
				return null;
			}
			InstancesComparedData<TableRowsEntity> cd = compareInstancesData(new InstanceData<TableRowsEntity>(key1, client1, tre1,rowNum1),
					new InstanceData<TableRowsEntity>(key2, client2, tre2,rowNum2));
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
		if (tableRow != null) {
			count = count + (tableRow.getEmsDashboard()==null?0:tableRow.getEmsDashboard().size());
			count = count + (tableRow.getEmsDashboardSet()==null?0:tableRow.getEmsDashboardSet().size());
			count = count + (tableRow.getEmsDashboardTile()==null?0:tableRow.getEmsDashboardTile().size());
			count = count + (tableRow.getEmsDashboardTileParams()==null?0:tableRow.getEmsDashboardTileParams().size());
			count = count + (tableRow.getEmsDashboardUserOptions()==null?0:tableRow.getEmsDashboardUserOptions().size());
			count = count + (tableRow.getEmsPreference()==null?0:tableRow.getEmsPreference().size());
		}
		
		return count;
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
		logger.info("rows1={}",rows1==null?0:rows1.size());
		logger.info("rows2={}",rows2==null?0:rows2.size());
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
		logger.info("cloud1: {} ", insData1.getData().toString());
		logger.info("cloud2: {} ", insData2.getData().toString());
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
	 * @throws ZDTException 
	 */
	private TableRowsEntity retrieveRowsEntityFromJsonForSingleInstance(String response) throws IOException, ZDTException
	{
		JsonUtil ju = JsonUtil.buildNormalMapper();
		TableRowsEntity tre = ju.fromJson(response, TableRowsEntity.class);
		if (tre == null) {
			logger.warn("Checking dashboard OMC instance table rows: null/empty entity retrieved.");
			throw new ZDTException(ZDTErrorConstants.NULL_TABLE_ROWS_ERROR_CODE, ZDTErrorConstants.NULL_TABLE_ROWS_ERROR_MESSAGE);
		}
		logger.info(tre.getEmsDashboard()==null?"nullRows":tre.getEmsDashboard().size());
		return tre;
	}
	
	public String retrieveTenants(String tenantId, String userTenant, LookupClient client) throws Exception {
		Link lk = getSingleInstanceUrl(client, "zdt/tenants", "http");
		if (lk == null) {
			logger.warn("get a null or empty link for omc instance!");
			throw new ZDTException(ZDTErrorConstants.NULL_LINK_ERROR_CODE, ZDTErrorConstants.NULL_LINK_ERROR_MESSAGE);
		}
		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME,tenantId);
		rc.setHeader(RestClient.X_REMOTE_USER,userTenant);
		
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		String response = rc.get(lk.getHref(), tenantId, new String(authToken));
		logger.info("checking tenants list " + response);
		return response;
	}
	
	private CountsEntity retrieveCountsForSingleInstance(String tenantId, String userTenant,LookupClient lc ,String maxComparedTime) throws Exception, IOException
	{
		Link lk = getSingleInstanceUrl(lc, "zdt/counts", "http");
		if (lk == null) {
			return null;
		}
		String url = lk.getHref() + "?maxComparedDate="+URLEncoder.encode(maxComparedTime, "UTF-8");
		//String response = new TenantSubscriptionUtil.RestClient().get(url, tenantId, userTenant);
		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME,tenantId);
		rc.setHeader(RestClient.X_REMOTE_USER,userTenant);
		
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		logger.info("start to get counts data");
		//String response = rc.get(lk.getHref(),tenantId,new String(authToken));
		String response = rc.get(url,tenantId,new String(authToken));
		logger.info("get counts data:"+response);
		JsonUtil ju = JsonUtil.buildNormalMapper();
		CountsEntity ze = ju.fromJson(response, CountsEntity.class);
		if (ze == null) {
			return null;
		}
		// TODO: for the 1st step implementation, let's log in log files then
		logger.info(
				"Retrieved counts for dashboards OMC instance: dashboard count - {}, favorites count - {}, preference count - {}",
				ze.getCountOfDashboards(), ze.getCountOfUserOptions(), ze.getCountOfPreference());
		return ze;
	}

	/**
	 * @throws Exception
	 * @throws IOException
	 */
	private TableRowsEntity retrieveRowsForSingleInstance(LookupClient lc, String tenantId, String userTenant, 
			String comparisonType, String maxComparedDate, String tenant) throws Exception, IOException, ZDTException
	{
		Link lk = getSingleInstanceUrl(lc, "zdt/tablerows", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			throw new ZDTException(ZDTErrorConstants.NULL_LINK_ERROR_CODE, ZDTErrorConstants.NULL_LINK_ERROR_MESSAGE);
		}		
		String url = null;
		if (tenant != null) {
			url = lk.getHref() + "?comparisonType="+comparisonType+"&maxComparedDate="+URLEncoder.encode(maxComparedDate, "UTF-8")+"&tenant="+tenant;
		} else {
			url = lk.getHref() + "?comparisonType="+comparisonType+"&maxComparedDate="+URLEncoder.encode(maxComparedDate, "UTF-8");
		}

		//String response = new TenantSubscriptionUtil.RestClient().get(url, tenantId,userTenant);

		logger.info("get table data URL is "+url);

		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME, tenantId);
		rc.setHeader(RestClient.X_REMOTE_USER, userTenant);
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		String response = rc.get(url, tenantId,new String(authToken));
		
		logger.info("Checking dashboard OMC instance table rows. Response is " + response);
		return retrieveRowsEntityFromJsonForSingleInstance(response);
	}
	
	public String retrieveComparatorStatusForOmcInstance(String tenantId, String userTenant) throws Exception {
		Link lk = getSingleInstanceUrl(client1, "zdt/compare/status", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			throw new ZDTException(ZDTErrorConstants.NULL_LINK_ERROR_CODE, ZDTErrorConstants.NULL_LINK_ERROR_MESSAGE);
		}
		//String response =  new TenantSubscriptionUtil.RestClient().get(lk.getHref(), tenantId,userTenant);
		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME, tenantId);
		rc.setHeader(RestClient.X_REMOTE_USER, userTenant);
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		String token = null;
		if (authToken != null) {
			token = new String(authToken);
		}
		String response = rc.get(lk.getHref(), tenantId, new String(authToken));
		logger.info("checking comparator status is " + response);
		
		return response;
	}
	
	public String retrieveSyncStatusForOmcInstance(String tenantId, String userTenant) throws Exception {
		Link lk = getSingleInstanceUrl(client1, "zdt/sync/status", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			throw new ZDTException(ZDTErrorConstants.NULL_LINK_ERROR_CODE, ZDTErrorConstants.NULL_LINK_ERROR_MESSAGE);
		}
		//String response =  new TenantSubscriptionUtil.RestClient().get(lk.getHref(), tenantId,userTenant);
		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME, tenantId);
		rc.setHeader(RestClient.X_REMOTE_USER, userTenant);
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		String response = rc.get(lk.getHref(), tenantId, new String(authToken));
		logger.info("checking sync status is " + response);
		
		return response;
	}
	
	public String saveComparatorStatus(String tenantId, String userTenant, LookupClient client, ZDTComparatorStatusRowEntity statusRowEntity) throws Exception{
		Link lk = getSingleInstanceUrl(client, "zdt/compare/result", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			return "Errors:Get a null or empty link for one single instance!";
		}
		JsonUtil jsonUtil = JsonUtil.buildNonNullMapper();
		String entityStr = jsonUtil.toJson(statusRowEntity);
 		logger.info("print the put data {} !",entityStr);
 		//String response = new TenantSubscriptionUtil.RestClient().put(lk.getHref(), entityStr, tenantId, userTenant);
 		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME, tenantId);
		rc.setHeader(RestClient.X_REMOTE_USER, userTenant);
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		String response = rc.put(lk.getHref(), entityStr,tenantId, new String(authToken));
 		logger.info("Checking sync reponse. Response is " + response);
		return response;
	}

	public String syncForInstance(String tenantId, String userTenant,  LookupClient client, String type, String syncDate) throws Exception
	{
		Link lk = getSingleInstanceUrl(client, "zdt/sync", "http");
		if (lk == null) {
			logger.warn("Get a null or empty link for one single instance!");
			return "Errors:Get a null or empty link for one single instance!";
		}
		logger.info("link is {} ",lk.getHref());
		String url = lk.getHref() + "?syncType=" + type + "&syncDate=" + URLEncoder.encode(syncDate, "UTF-8");
		logger.info("sync url is "+ url);
		//String response = new TenantSubscriptionUtil.RestClient().get(url, tenantId, userTenant);
		RestClient rc = RestClientProxy.getRestClient();
		rc.setHeader(RestClient.X_USER_IDENTITY_DOMAIN_NAME,tenantId);
		rc.setHeader(RestClient.X_REMOTE_USER,userTenant);
		char[] authToken = LookupManager.getInstance().getAuthorizationToken();
		String response = rc.get(lk.getHref(),tenantId, new String(authToken));
		
		logger.info("Checking sync reponse. Response is " + response);
		return response;
	}

	public String getKey1() {
		return key1;
	}


	public String getKey2() {
		return key2;
	}


	public LookupClient getClient1() {
		return client1;
	}

	public LookupClient getClient2() {
		return client2;
	}

	
}
