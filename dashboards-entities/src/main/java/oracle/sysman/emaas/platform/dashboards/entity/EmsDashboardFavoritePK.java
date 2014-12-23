package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;


public class EmsDashboardFavoritePK implements Serializable {
    private String tenantId;
    private String userName;
    private Long emsDashboard;

    public EmsDashboardFavoritePK() {
    }

    public EmsDashboardFavoritePK(String tenantId, String userName, Long emsDashboard) {
        this.tenantId = tenantId;
        this.userName = userName;
        this.emsDashboard = emsDashboard;
    }

    public boolean equals(Object other) {
        if (other instanceof EmsDashboardFavoritePK) {
            final EmsDashboardFavoritePK otherEmsDashboardFavoritePK = (EmsDashboardFavoritePK) other;
            final boolean areEqual =
                (otherEmsDashboardFavoritePK.tenantId.equals(tenantId) &&
                 otherEmsDashboardFavoritePK.userName.equals(userName) &&
                 otherEmsDashboardFavoritePK.emsDashboard.equals(emsDashboard));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getEmsDashboard() {
        return emsDashboard;
    }

    public void setEmsDashboard(Long emsDashboard) {
        this.emsDashboard = emsDashboard;
    }
}
