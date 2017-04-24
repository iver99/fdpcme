package oracle.sysman.emaas.platform.dashboards.core.util;

/**
 * Created by chehao on 4/20/17.
 */
public class TenantVersionModel {

    private Boolean isV1Tenant;

    public Boolean getIsV1Tenant() {
        return isV1Tenant;
    }

    public void setIsV1Tenant(Boolean isV1Tenant) {
        this.isV1Tenant = isV1Tenant;
    }

    public TenantVersionModel(Boolean isV1Tenant) {
        this.isV1Tenant = isV1Tenant;
    }
}
