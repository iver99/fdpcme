package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows;

import mockit.*;
import oracle.sysman.emInternalSDK.rproxy.lookup.CloudLookupException;
import oracle.sysman.emInternalSDK.rproxy.lookup.CloudLookups;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.AbstractComparator;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Created by chehao on 7/5/2017 11:16.
 */
@Test(groups = { "s1" })
public class AbstractComparatorTest {

    @Mocked
    CloudLookups cloudLookups;
    @Injectable
    HashMap<String, LookupClient> hashMap;
    @Test
    public void testGetIns() throws CloudLookupException {
        AbstractComparator abstractComparator = new AbstractComparator(){
            @Override
            public HashMap<String, LookupClient> getOMCInstances() {
                return super.getOMCInstances();
            }

            @Override
            protected Link getSingleInstanceUrl(LookupClient lc, String rel, String protocol) throws Exception {
                return super.getSingleInstanceUrl(lc, rel, protocol);
            }
        };
        new Expectations(){
            {
                cloudLookups.getCloudLookupClients();
                result = hashMap;
            }
        };
        abstractComparator.getOMCInstances();
    }

}
