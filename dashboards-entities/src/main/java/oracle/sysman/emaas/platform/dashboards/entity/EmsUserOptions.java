package oracle.sysman.emaas.platform.dashboards.entity;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author jishshi
 * @since 2/1/2016
 */

@Entity
@Table(name = "EMS_DASHBOARD_USER_OPTIONS")
@IdClass(EmsUserOptionsPK.class)
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", length = 32, primaryKey = true)
public class EmsUserOptions implements Serializable {
    private static final long serialVersionUID = 8723513639667559582L;

    @Id
    @Column(name = "DASHBOARD_ID", nullable = false, length = 256)
    Long dashboardId;
    @Id
    @Column(name = "USER_NAME", nullable = false, length = 128)
    String userName;

    @Column(name = "AUTO_REFRESH_INTERVAL", nullable = false, length = 256)
    Long autoRefreshInterval;

    public EmsUserOptions() {

    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public Long getAutoRefreshInterval() {
        return autoRefreshInterval;
    }

    public void setAutoRefreshInterval(Long autoRefreshInterval) {
        this.autoRefreshInterval = autoRefreshInterval;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
