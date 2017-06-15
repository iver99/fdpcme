package oracle.sysman.emaas.platform.emcpdf.tenant.subscription2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chehao on 3/30/17.
 */
public class SubscriptionApps {
    private String tenantName;
    private String serviceType;
    private boolean isTrial;
    private String version;
    List<EditionComponent> editionComponentsList = new ArrayList<>();

    public List<EditionComponent> getEditionComponentsList() {
        return editionComponentsList;
    }

    public void setEditionComponentsList(List<EditionComponent> editionComponentsList) {
        this.editionComponentsList = editionComponentsList;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isTrial() {
        return isTrial;
    }

    public void setTrial(boolean isTrial) {
        this.isTrial = isTrial;
    }

    public void addEditionComponent(EditionComponent editionComponent) {
        editionComponentsList.add(editionComponent);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
