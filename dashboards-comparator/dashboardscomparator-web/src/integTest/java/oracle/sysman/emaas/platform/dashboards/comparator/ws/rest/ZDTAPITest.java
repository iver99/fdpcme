package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstanceData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstancesComparedData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.DashboardRowEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by xiadai on 2017/1/10.
 */
@Test(groups = {"s2"})
public class ZDTAPITest {
    private ZDTAPI zdtapi = new ZDTAPI();
    @Mocked
    AbstractComparator abstractComparator;
    @Mocked
    InstancesComparedData<CountsEntity> set;
    @Mocked
    InstanceData<CountsEntity> data;
    @Mocked
    CountsEntity count;
    @Mocked
    Throwable throwable;
    @Mocked
    String tenant;
    @Mocked
    String userTenant;
    

    @Test
    public void testCompareOnDF(@Mocked final JsonUtil jsonUtil, @Mocked final LookupClient client1, @Mocked final LookupClient client2) throws IOException{
    	final HashMap<String, LookupClient> lookupEntry = new HashMap<String, LookupClient>();
    	new Expectations(){
            {
                abstractComparator.getOMCInstances();
                result = lookupEntry;
    			lookupEntry.put("omc1",client1);
    	    	lookupEntry.put("omc2",client2);
            }
        };
        zdtapi.compareOnDF(tenant, userTenant);
    }

    @Test
    public void testSyncOnDF(@Mocked final JsonUtil jsonUtil, @Mocked final LookupClient client1, @Mocked final LookupClient client2) throws IOException{
    	final TableRowsEntity tableRow1 = new TableRowsEntity();
    	tableRow1.setEmsDashboard(new ArrayList<DashboardRowEntity>());
    	 
        final HashMap<String, LookupClient> lookupEntry = new HashMap<String, LookupClient>();
    	
    	new Expectations() {
    		{ 	
    			abstractComparator.getOMCInstances();
    			result = lookupEntry;
    			lookupEntry.put("omc1",client1);
    	    	lookupEntry.put("omc2",client2);
    		/*	
    			JsonUtil.buildNormalMapper();
    			result = jsonUtil;
    			jsonUtil.fromJson(anyString,TableRowsEntity.class);
    			result = tableRow1;*/
    		}
    	};
        zdtapi.syncOnDF(tenant, userTenant, tenant);
    }
    @Test
    public void testInnerClasses(){
        ZDTAPI.InstanceCounts counts = new ZDTAPI.InstanceCounts(data);
        counts.getCounts();
        counts.getInstanceName();
        counts.setCounts(count);
        counts.setInstanceName("name");

        ZDTAPI.InstancesComapredCounts instancesComapredCounts = new ZDTAPI.InstancesComapredCounts(counts, counts);
        instancesComapredCounts.getInstance1();
        instancesComapredCounts.getInstance2();
        instancesComapredCounts.setInstance1(counts);
        instancesComapredCounts.setInstance2(counts);
    }
    
    @Test
    public void testCompareRows1() {
    	
    	zdtapi.compareRows(tenant, userTenant,"full",30);
    }
    
    @Test
    public void testCompareRows2(@Mocked final JsonUtil jsonUtil, @Mocked final LookupClient client1, @Mocked final LookupClient client2,
    		@Mocked final Link link) throws Exception{
    	final TableRowsEntity tableRow1 = new TableRowsEntity();
    	tableRow1.setEmsDashboard(new ArrayList<DashboardRowEntity>());
    	TableRowsEntity tableRow2 = new TableRowsEntity();
    	tableRow2.setEmsDashboard(new ArrayList<DashboardRowEntity>());
    	InstanceData<TableRowsEntity> instance1 = new InstanceData<TableRowsEntity>("", null,tableRow1,  100);
    	InstanceData<TableRowsEntity> instance2 = new InstanceData<TableRowsEntity>("", null,tableRow2,  100);
    	instance1.setKey("omc");
    	instance1.setClient(null);
    	instance1.setData(tableRow2);;
    	instance1.setTotalRowNum(100);
    	final HashMap<String, LookupClient> lookupEntry = new HashMap<String, LookupClient>();
    	
    	
    	final InstancesComparedData<TableRowsEntity> comparedData = new InstancesComparedData<TableRowsEntity>(instance1, instance2);  
    	comparedData.setInstance1(instance1);
    	comparedData.setInstance2(instance2);
    	new Expectations() {
    		{ 	
    			abstractComparator.getOMCInstances();
    			result = lookupEntry;
    			lookupEntry.put("omc1",client1);
    	    	lookupEntry.put("omc2",client2);
    			
    		/*	JsonUtil.buildNormalMapper();
    			result = jsonUtil;
    			jsonUtil.fromJson(anyString,TableRowsEntity.class);
    			result = tableRow1;  */
    		}
    	};
    	
    	zdtapi.compareRows(tenant, userTenant,"full", 30);
    }
  
    
    @Test
    public void testGetCurrentTime() {
    	zdtapi.getCurrentUTCTime();
    }
    
    @Test
    public void testGetTimeString() {
    	zdtapi.getTimeString(new Date());
    }
    
    @Test
    public void testgetSyncStatus() {
    	zdtapi.getCompareStatus("id", "userName");
    	zdtapi.getCompareStatus(null,null);
    }
    
    @Test
    public void testgetCompareStatus() {
    	zdtapi.getSyncStatus("id", "userName");

    	zdtapi.getCompareStatus(null,null);
    }
    

}