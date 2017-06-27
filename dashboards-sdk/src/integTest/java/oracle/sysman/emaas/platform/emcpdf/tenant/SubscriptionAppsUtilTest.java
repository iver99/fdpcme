package oracle.sysman.emaas.platform.emcpdf.tenant;

import oracle.sysman.emaas.platform.emcpdf.tenant.SubscriptionAppsUtil;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.AppsInfo;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.EditionComponent;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.SubscriptionApps;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.TenantSubscriptionInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chehao on 2017/4/17 10:33.
 */
@Test(groups = {"s1"})
public class SubscriptionAppsUtilTest {

    @Test
    public void testSubscriptionAppsUtilV1_V2(){
        TenantSubscriptionInfo t = new TenantSubscriptionInfo();
        List<SubscriptionApps> subscriptionAppsList = new ArrayList<>();

        //V1 tenant
        SubscriptionApps subscriptionApps = new SubscriptionApps();
        subscriptionApps.setVersion(SubscriptionAppsUtil.V1_TENANT);
        subscriptionApps.setServiceType("Monitoring");
        List<EditionComponent> editionComponentList = new ArrayList<>();
        subscriptionApps.setEditionComponentsList(editionComponentList);

        //V2 tenant
        SubscriptionApps subscriptionApps1 = new SubscriptionApps();
        subscriptionApps1.setVersion(SubscriptionAppsUtil.V2_TENANT);
        subscriptionApps1.setServiceType("OMC");
        List<EditionComponent> editionComponentList1 = new ArrayList<>();
        EditionComponent editionComponent1 = new EditionComponent();
        editionComponent1.setEdition("Standard Edition");
        editionComponentList1.add(editionComponent1);
        subscriptionApps1.setEditionComponentsList(editionComponentList1);

        SubscriptionApps subscriptionApps1_1 = new SubscriptionApps();
        subscriptionApps1_1.setVersion(SubscriptionAppsUtil.V2_TENANT);
        subscriptionApps1_1.setServiceType("OMC");
        subscriptionApps1_1.setTrial(true);
        List<EditionComponent> editionComponentList1_1 = new ArrayList<>();
        EditionComponent editionComponent1_1 = new EditionComponent();
        editionComponent1_1.setEdition("Standard Edition");
        editionComponentList1_1.add(editionComponent1);
        subscriptionApps1_1.setEditionComponentsList(editionComponentList1_1);


        SubscriptionApps subscriptionApps2 = new SubscriptionApps();
        subscriptionApps2.setVersion(SubscriptionAppsUtil.V2_TENANT);
        subscriptionApps2.setServiceType("OSMACC");
        List<EditionComponent> editionComponentList2 = new ArrayList<>();
        EditionComponent editionComponent2 = new EditionComponent();
        editionComponent2.setEdition("Standard Edition");
        editionComponentList2.add(editionComponent2);
        subscriptionApps2.setEditionComponentsList(editionComponentList2);

        subscriptionAppsList.add(subscriptionApps);
        subscriptionAppsList.add(subscriptionApps1);
        subscriptionAppsList.add(subscriptionApps1_1);
        subscriptionAppsList.add(subscriptionApps2);


        List<AppsInfo> appsInfoList = new ArrayList<>();
        t.setAppsInfoList(appsInfoList);
        t.setSubscriptionAppsList(subscriptionAppsList);
        SubscriptionAppsUtil.getSubscribedAppsList(t);
    }

    @Test
    public void testSubscriptionAppsUtilV3(){

        TenantSubscriptionInfo t = new TenantSubscriptionInfo();
        List<SubscriptionApps> subscriptionAppsList = new ArrayList<>();
        //V3 tenant
        SubscriptionApps subscriptionApps3 = new SubscriptionApps();
        subscriptionApps3.setServiceType("OMCSE");
        subscriptionApps3.setTenantName(SubscriptionAppsUtil.V3_TENANT);
        List<EditionComponent> editionComponentList3 = new ArrayList<>();
        EditionComponent editionComponent3 = new EditionComponent();
        editionComponent3.setEdition("V3 editions");
        editionComponentList3.add(editionComponent3);
        subscriptionApps3.setEditionComponentsList(editionComponentList3);

        SubscriptionApps subscriptionApps4 = new SubscriptionApps();
        subscriptionApps4.setServiceType("OMCEE");
        subscriptionApps4.setTenantName(SubscriptionAppsUtil.V3_TENANT);
        List<EditionComponent> editionComponentList4 = new ArrayList<>();
        EditionComponent editionComponent4 = new EditionComponent();
        editionComponent4.setEdition("V3 editions");
        editionComponentList4.add(editionComponent4);
        subscriptionApps4.setEditionComponentsList(editionComponentList4);

        SubscriptionApps subscriptionApps5 = new SubscriptionApps();
        subscriptionApps5.setServiceType("OMCLOG");
        subscriptionApps5.setTenantName(SubscriptionAppsUtil.V3_TENANT);
        subscriptionApps5.setEditionComponentsList(null);

        SubscriptionApps subscriptionApps6 = new SubscriptionApps();
        subscriptionApps6.setServiceType("SECSE");
        subscriptionApps6.setTenantName(SubscriptionAppsUtil.V3_TENANT);
        subscriptionApps6.setEditionComponentsList(null);

        SubscriptionApps subscriptionApps7 = new SubscriptionApps();
        subscriptionApps7.setServiceType("SECSMA");
        subscriptionApps7.setTenantName(SubscriptionAppsUtil.V3_TENANT);
        subscriptionApps7.setEditionComponentsList(null);

        subscriptionAppsList.add(subscriptionApps3);
        subscriptionAppsList.add(subscriptionApps4);
        subscriptionAppsList.add(subscriptionApps5);
        subscriptionAppsList.add(subscriptionApps6);
        subscriptionAppsList.add(subscriptionApps7);

        List<AppsInfo> appsInfoList = new ArrayList<>();
        t.setAppsInfoList(appsInfoList);
        t.setSubscriptionAppsList(subscriptionAppsList);
        SubscriptionAppsUtil.getSubscribedAppsList(t);
    }

    @Test
    public void testSubscriptionAppsUtilV4(){
        TenantSubscriptionInfo t = new TenantSubscriptionInfo();
        List<SubscriptionApps> subscriptionAppsList = new ArrayList<>();
        //v4
        SubscriptionApps subscriptionApps8 = new SubscriptionApps();
        subscriptionApps8.setServiceType("OMC");
        subscriptionApps8.setTenantName(SubscriptionAppsUtil.V3_TENANT);
        List<EditionComponent> editionComponentList8 = new ArrayList<>();
        EditionComponent editionComponent8 = new EditionComponent();
        EditionComponent editionComponent8_2 = new EditionComponent();
        EditionComponent editionComponent8_3 = new EditionComponent();
        EditionComponent editionComponent8_4 = new EditionComponent();
        EditionComponent editionComponent8_5 = new EditionComponent();
        editionComponent8.setEdition("V4 editions");
        editionComponent8_2.setEdition("V4 editions");
        editionComponent8_3.setEdition("V4 editions");
        editionComponent8_4.setEdition("V4 editions");
        editionComponent8_5.setEdition("V4 editions");
        //make sure size == 5
        editionComponentList8.add(editionComponent8);
        editionComponentList8.add(editionComponent8_2);
        editionComponentList8.add(editionComponent8_3);
        editionComponentList8.add(editionComponent8_4);
        editionComponentList8.add(editionComponent8_5);
        Assert.assertEquals(5,editionComponentList8.size());
        subscriptionApps8.setEditionComponentsList(editionComponentList8);

        subscriptionAppsList.add(subscriptionApps8);
        List<AppsInfo> appsInfoList = new ArrayList<>();
        t.setAppsInfoList(appsInfoList);
        t.setSubscriptionAppsList(subscriptionAppsList);
        SubscriptionAppsUtil.getSubscribedAppsList(t);
    }

}
