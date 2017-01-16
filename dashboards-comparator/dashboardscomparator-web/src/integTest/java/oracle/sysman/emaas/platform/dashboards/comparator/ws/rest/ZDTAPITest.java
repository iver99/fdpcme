package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;

import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.counts.CountsEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstanceData;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.InstancesComparedData;
import org.testng.annotations.Test;

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
    Map.Entry<String, LookupClient> entry;
    @Mocked
    CountsEntity count;
    @Mocked
    Throwable throwable;

    @Test(expectedExceptions={NullPointerException.class})
    public void testCompareOnDF(){
        new Expectations(){
            {
                abstractComparator.getOMCInstances();
                result = null;
            }
        };
        zdtapi.compareOnDF();
    }

    @Test
    public void testSyncOnDF(){
        zdtapi.syncOnDF();
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

}