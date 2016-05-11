package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

/**
 * @author jishshi
 * @since 2/1/2016.
 */
public class EmsUserOptionsPK implements Serializable {

    private  String userName;
    private  Long dashboardId;

    private static final long serialVersionUID = 8346753118084614627L;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public EmsUserOptionsPK(String userName, Long dashboardId) {
        this.userName = userName;
        this.dashboardId = dashboardId;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof EmsUserOptions) {
            final EmsUserOptions otherEmsUserOptions = (EmsUserOptions) other;
            final boolean isEqual = otherEmsUserOptions.dashboardId.equals(dashboardId)
                    && otherEmsUserOptions.userName.equals(userName);
            return isEqual;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
