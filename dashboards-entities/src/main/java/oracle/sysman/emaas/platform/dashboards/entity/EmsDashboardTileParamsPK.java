package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.math.BigDecimal;

public class EmsDashboardTileParamsPK implements Serializable {
    private String paramName;
    private BigDecimal emsDashboardTile;

    public EmsDashboardTileParamsPK() {
    }

    public EmsDashboardTileParamsPK(String paramName, BigDecimal emsDashboardTile) {
        this.paramName = paramName;
        this.emsDashboardTile = emsDashboardTile;
    }

    public boolean equals(Object other) {
        if (other instanceof EmsDashboardTileParamsPK) {
            final EmsDashboardTileParamsPK otherEmsDashboardTileParamsPK = (EmsDashboardTileParamsPK) other;
            final boolean areEqual =
                (otherEmsDashboardTileParamsPK.paramName.equals(paramName) &&
                 otherEmsDashboardTileParamsPK.emsDashboardTile.equals(emsDashboardTile));
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

    public BigDecimal getEmsDashboardTile() {
        return emsDashboardTile;
    }

    public void setEmsDashboardTile(BigDecimal emsDashboardTile) {
        this.emsDashboardTile = emsDashboardTile;
    }
}
