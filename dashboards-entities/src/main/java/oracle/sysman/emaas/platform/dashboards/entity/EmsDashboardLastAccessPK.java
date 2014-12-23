package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.math.BigDecimal;

public class EmsDashboardLastAccessPK implements Serializable {
    private String accessedBy;
    private BigDecimal dashboardId;

    public EmsDashboardLastAccessPK() {
    }

    public EmsDashboardLastAccessPK(String accessedBy, BigDecimal dashboardId) {
        this.accessedBy = accessedBy;
        this.dashboardId = dashboardId;
    }

    public boolean equals(Object other) {
        if (other instanceof EmsDashboardLastAccessPK) {
            final EmsDashboardLastAccessPK otherEmsDashboardLastAccessPK = (EmsDashboardLastAccessPK) other;
            final boolean areEqual =
                (otherEmsDashboardLastAccessPK.accessedBy.equals(accessedBy) &&
                 otherEmsDashboardLastAccessPK.dashboardId.equals(dashboardId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getAccessedBy() {
        return accessedBy;
    }

    public void setAccessedBy(String accessedBy) {
        this.accessedBy = accessedBy;
    }

    public BigDecimal getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(BigDecimal dashboardId) {
        this.dashboardId = dashboardId;
    }
}
