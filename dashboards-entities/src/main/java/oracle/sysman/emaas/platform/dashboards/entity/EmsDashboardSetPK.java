package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

/**
 * @author jishshi
 * @since 2016/3/3.
 */
public class EmsDashboardSetPK implements Serializable {
    private static final long serialVersionUID = 6345975239287337011L;


    private Long dashboardSetId;

    private Long subDashboardId;

    private String userName;

    public Long getDashboardSetId() {
        return dashboardSetId;
    }

    public void setDashboardSetId(Long dashboardSetId) {
        this.dashboardSetId = dashboardSetId;
    }

    public Long getSubDashboardId() {
        return subDashboardId;
    }

    public void setSubDashboardId(Long subDashboardId) {
        this.subDashboardId = subDashboardId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof EmsDashboardSetPK) {
            final EmsDashboardSetPK otherEmsDashboardSetPK = (EmsDashboardSetPK) other;
            final boolean isEqual = otherEmsDashboardSetPK.dashboardSetId.equals(dashboardSetId)
                    && otherEmsDashboardSetPK.userName.equals(userName) && otherEmsDashboardSetPK.subDashboardId.equals(subDashboardId);
            return isEqual;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
