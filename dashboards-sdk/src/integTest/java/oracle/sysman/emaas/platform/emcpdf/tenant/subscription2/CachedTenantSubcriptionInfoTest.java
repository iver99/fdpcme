package oracle.sysman.emaas.platform.emcpdf.tenant.subscription2;

import org.testng.annotations.Test;

/**
 * Created by chehao on 7/5/2017 10:23.
 */
@Test(groups = {"s1"})
public class CachedTenantSubcriptionInfoTest {
    public void testCachedTenantSubcriptionInfo(){
        CachedTenantSubcriptionInfo c = new CachedTenantSubcriptionInfo(null, null);
        c.setSubscribedAppsList(null);
        c.setTenantSubscriptionInfo(null);
        c.getSubscribedAppsList();
        c.getTenantSubscriptionInfo();

    }
}
