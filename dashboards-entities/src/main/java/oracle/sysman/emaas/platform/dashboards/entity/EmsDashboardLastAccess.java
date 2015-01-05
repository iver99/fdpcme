package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@NamedQueries({
              @NamedQuery(name = "EmsDashboardLastAccess.findAll", query = "select o from EmsDashboardLastAccess o") })
@Table(name = "EMS_DASHBOARD_LAST_ACCESS")
@IdClass(EmsDashboardLastAccessPK.class)
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", length = 32, primaryKey = true)
public class EmsDashboardLastAccess implements Serializable {
    private static final long serialVersionUID = -3829558525926721782L;
    @Id
    @Column(name = "ACCESSED_BY", nullable = false, length = 128)
    private String accessedBy;
    @Id
    @Column(name = "DASHBOARD_ID", nullable = false)
    private Long dashboardId;
    //@Id
    //@Column(name = "TENANT_ID", nullable = false, length = 32)
    //private String tenantId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACCESS_DATE", nullable = false)
    private Date accessDate;

    public EmsDashboardLastAccess() {
    }

    public EmsDashboardLastAccess(Date accessDate, String accessedBy, Long dashboardId) {
        this.accessDate = accessDate;
        this.accessedBy = accessedBy;
        this.dashboardId = dashboardId;
    }

    public String getAccessedBy() {
        return accessedBy;
    }

    public void setAccessedBy(String accessedBy) {
        this.accessedBy = accessedBy;
    }

    public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

}
