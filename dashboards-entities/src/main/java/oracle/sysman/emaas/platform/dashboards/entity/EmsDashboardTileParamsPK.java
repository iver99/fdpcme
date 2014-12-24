package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

public class EmsDashboardTileParamsPK implements Serializable {
    private String paramName;
    private Long dashboardTile;

    public EmsDashboardTileParamsPK() {
    }

    public EmsDashboardTileParamsPK(String paramName, Long emsDashboardTile) {
        this.paramName = paramName;
        this.dashboardTile = emsDashboardTile;
    }

    public boolean equals(Object other) {
        if (other instanceof EmsDashboardTileParamsPK) {
            final EmsDashboardTileParamsPK otherEmsDashboardTileParamsPK = (EmsDashboardTileParamsPK) other;
            final boolean areEqual =
                (otherEmsDashboardTileParamsPK.paramName.equals(paramName) &&
                 otherEmsDashboardTileParamsPK.dashboardTile.equals(dashboardTile));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Long getDashboardTile() {
        return dashboardTile;
    }

    public void setDashboardTile(Long emsDashboardTile) {
        this.dashboardTile = emsDashboardTile;
    }
}
