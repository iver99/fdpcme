package oracle.sysman.emaas.platform.emcpdf.tenant.subscription2;


import oracle.sysman.emaas.platform.emcpdf.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chehao on 3/30/17.
 */
public class TenantSubscriptionInfo {
    private final static Logger LOGGER = LogManager.getLogger(TenantSubscriptionInfo.class);

    private List<SubscriptionApps> subscriptionAppsList;
    @JsonProperty("applications")
    private List<AppsInfo> appsInfoList;

    public List<AppsInfo> getAppsInfoList() {
        return appsInfoList;
    }

    public void setAppsInfoList(List<AppsInfo> appsInfoList) {
        this.appsInfoList = appsInfoList;
    }

    public List<SubscriptionApps> getSubscriptionAppsList() {
        return subscriptionAppsList;
    }

    public void setSubscriptionAppsList(List<SubscriptionApps> subscriptionAppsList) {
        this.subscriptionAppsList = subscriptionAppsList;
    }

    public String toJson(TenantSubscriptionInfo obj) {
        JsonUtil jsonUtil = JsonUtil.buildNonNullMapper();
        String result = null;
        if (obj != null && obj.getAppsInfoList() != null && !obj.getAppsInfoList().isEmpty()) {

            SubscribedAppsEntity subscribedAppsEntity = new SubscribedAppsEntity(obj.getAppsInfoList());
            result = jsonUtil.toJson(subscribedAppsEntity);
        }
        LOGGER.info("ToJson is {}", result);
        return result;
    }

    private static class SubscribedAppsEntity<E> {
        private List<E> applications = new ArrayList<>();

        public SubscribedAppsEntity(List<E> applications) {
            this.applications = applications;
        }

        public List<E> getApplications() {
            return applications;
        }

        public void setApplications(List<E> applications) {
            this.applications = applications;
        }

    }

}
