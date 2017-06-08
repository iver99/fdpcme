package oracle.sysman.emaas.platform.dashboards.core.util;

import oracle.sysman.emaas.platform.dashboards.core.model.subscription2.AppsInfo;
import oracle.sysman.emaas.platform.dashboards.core.model.subscription2.EditionComponent;
import oracle.sysman.emaas.platform.dashboards.core.model.subscription2.SubscriptionApps;
import oracle.sysman.emaas.platform.dashboards.core.model.subscription2.TenantSubscriptionInfo;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chehao on 2017/4/17 10:33.
 */
@Test(groups = {"s1"})
public class SubscriptionAppsUtilTest {

    @Test
    public void testSubscriptionAppsUtil1(){
        TenantSubscriptionInfo t = new TenantSubscriptionInfo();
        List<SubscriptionApps> subscriptionAppsList = new ArrayList<>();

        //V1 tenant
        SubscriptionApps subscriptionApps = new SubscriptionApps();
        subscriptionApps.setVersion(SubsriptionAppsUtil.V1_TENANT);
        subscriptionApps.setServiceType("Monitoring");
        List<EditionComponent> editionComponentList = new ArrayList<>();
        subscriptionApps.setEditionComponentsList(editionComponentList);

        //V2 tenant
        SubscriptionApps subscriptionApps1 = new SubscriptionApps();
        subscriptionApps1.setVersion(SubsriptionAppsUtil.V2_TENANT);
        subscriptionApps1.setServiceType("OMC");
        List<EditionComponent> editionComponentList1 = new ArrayList<>();
        EditionComponent editionComponent1 = new EditionComponent();
        editionComponent1.setEdition("Standard Edition");
        editionComponentList1.add(editionComponent1);
        subscriptionApps1.setEditionComponentsList(editionComponentList1);


        SubscriptionApps subscriptionApps2 = new SubscriptionApps();
        subscriptionApps2.setVersion(SubsriptionAppsUtil.V2_TENANT);
        subscriptionApps2.setServiceType("OSMACC");
        List<EditionComponent> editionComponentList2 = new ArrayList<>();
        EditionComponent editionComponent2 = new EditionComponent();
        editionComponent2.setEdition("Standard Edition");
        editionComponentList2.add(editionComponent2);
        subscriptionApps2.setEditionComponentsList(editionComponentList2);

        //V3 tenant
        SubscriptionApps subscriptionApps3 = new SubscriptionApps();
        subscriptionApps3.setServiceType("OMCSE");
        subscriptionApps3.setTenantName(SubsriptionAppsUtil.V3_TENANT);
        List<EditionComponent> editionComponentList3 = new ArrayList<>();
        EditionComponent editionComponent3 = new EditionComponent();
        editionComponent3.setEdition("V3 editions");
        editionComponentList3.add(editionComponent3);
        subscriptionApps3.setEditionComponentsList(null);

        SubscriptionApps subscriptionApps4 = new SubscriptionApps();
        subscriptionApps3.setServiceType("OMCEE");
        subscriptionApps3.setTenantName(SubsriptionAppsUtil.V3_TENANT);
        List<EditionComponent> editionComponentList4 = new ArrayList<>();
        EditionComponent editionComponent4 = new EditionComponent();
        editionComponent4.setEdition("V3 editions");
        editionComponentList4.add(editionComponent4);
        subscriptionApps3.setEditionComponentsList(null);

        SubscriptionApps subscriptionApps5 = new SubscriptionApps();
        subscriptionApps5.setServiceType("OMCLOG");
        subscriptionApps5.setTenantName(SubsriptionAppsUtil.V3_TENANT);
        subscriptionApps5.setEditionComponentsList(null);

        SubscriptionApps subscriptionApps6 = new SubscriptionApps();
        subscriptionApps6.setServiceType("SECSE");
        subscriptionApps6.setTenantName(SubsriptionAppsUtil.V3_TENANT);
        subscriptionApps6.setEditionComponentsList(null);

        SubscriptionApps subscriptionApps7 = new SubscriptionApps();
        subscriptionApps7.setServiceType("SECSMA");
        subscriptionApps7.setTenantName(SubsriptionAppsUtil.V3_TENANT);
        subscriptionApps7.setEditionComponentsList(null);


        subscriptionAppsList.add(subscriptionApps);
        subscriptionAppsList.add(subscriptionApps1);
        subscriptionAppsList.add(subscriptionApps2);
        subscriptionAppsList.add(subscriptionApps3);
        subscriptionAppsList.add(subscriptionApps4);
        subscriptionAppsList.add(subscriptionApps5);
        subscriptionAppsList.add(subscriptionApps6);
        subscriptionAppsList.add(subscriptionApps7);

        List<AppsInfo> appsInfoList = new ArrayList<>();
        t.setAppsInfoList(appsInfoList);
        t.setSubscriptionAppsList(subscriptionAppsList);
        SubsriptionAppsUtil.getSubscribedAppsList(t);
    }

}
