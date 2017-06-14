package oracle.sysman.emaas.platform.emcpdf.tenant.subscription2;

import java.util.List;

/**
 * Created by chehao on 2017/5/8 10:18.
 */
public class CachedTenantSubcriptionInfo {
    List<String> subscribedAppsList;
    TenantSubscriptionInfo tenantSubscriptionInfo;

    public List<String> getSubscribedAppsList() {
        return subscribedAppsList;
    }

    public void setSubscribedAppsList(List<String> subscribedAppsList) {
        this.subscribedAppsList = subscribedAppsList;
    }

    public TenantSubscriptionInfo getTenantSubscriptionInfo() {
        return tenantSubscriptionInfo;
    }

    public void setTenantSubscriptionInfo(TenantSubscriptionInfo tenantSubscriptionInfo) {
        this.tenantSubscriptionInfo = tenantSubscriptionInfo;
    }

    public CachedTenantSubcriptionInfo(List<String> subscribedAppsList, TenantSubscriptionInfo tenantSubscriptionInfo) {
        this.subscribedAppsList = subscribedAppsList;
        this.tenantSubscriptionInfo = tenantSubscriptionInfo;
    }
}
