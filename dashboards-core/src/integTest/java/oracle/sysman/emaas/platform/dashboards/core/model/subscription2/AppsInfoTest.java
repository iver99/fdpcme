package oracle.sysman.emaas.platform.dashboards.core.model.subscription2;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2017/4/7.
 */
@Test(groups = {"s1"})
public class AppsInfoTest {
    public void testAppsInfo(){
        AppsInfo appsInfo = new AppsInfo("","",null);
        appsInfo = new AppsInfo("","");
        appsInfo.getLicVersion();
        appsInfo.getId();
        appsInfo.getEditions();
        appsInfo.setEditions(null);
        appsInfo.setId(null);
        appsInfo.setLicVersion(null);
        Component component = new Component();
        component.getComponent_id();
        component.getComponent_parameter();
        component.setComponent_id(null);
        component.setComponent_parameter(null);
        ComponentParameter componentParameter = new ComponentParameter();
        componentParameter.getKey();
        componentParameter.getValue();
        componentParameter.setKey(null);
        componentParameter.setValue(null);
        EditionComponent editionComponent = new EditionComponent();
        editionComponent.getComponentId();
        editionComponent.getEdition();
        editionComponent.setComponentId(null);
        editionComponent.setEdition(null);
        OrderComponents orderComponents = new OrderComponents();
        orderComponents.getServiceComponent();
        orderComponents.setServiceComponent(null);
        ServiceRequestCollection serviceRequestCollection = new ServiceRequestCollection();
        serviceRequestCollection.getOrderComponents();
        serviceRequestCollection.getServiceType();
        serviceRequestCollection.setOrderComponents(null);
        serviceRequestCollection.setServiceType(null);
        serviceRequestCollection.setTrial(true);
        serviceRequestCollection.isTrial();
        SubscriptionApps subscriptionApps = new SubscriptionApps();
        subscriptionApps.getServiceType();
        subscriptionApps.getEditionComponentsList();
        subscriptionApps.getTenantName();
        subscriptionApps.getVersion();
        subscriptionApps.setTrial(true);
        subscriptionApps.setServiceType(null);
        subscriptionApps.setEditionComponentsList(null);
        subscriptionApps.setTenantName(null);
        subscriptionApps.setVersion(null);
        TenantSubscriptionInfo tenantSubscriptionInfo = new TenantSubscriptionInfo();
        tenantSubscriptionInfo.getAppsInfoList();
        tenantSubscriptionInfo.getSubscriptionAppsList();
        tenantSubscriptionInfo.setAppsInfoList(null);
        tenantSubscriptionInfo.setSubscriptionAppsList(null);
        tenantSubscriptionInfo.toJson(new TenantSubscriptionInfo());
        ServiceComponent sc = new ServiceComponent();
        sc.setComponent(null);
        sc.setComponent_id(null);
        sc.getComponent();
        sc.getComponent_id();
    }


}