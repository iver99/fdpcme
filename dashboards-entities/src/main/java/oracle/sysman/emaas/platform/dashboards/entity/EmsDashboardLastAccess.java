package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
              @NamedQuery(name = "EmsDashboardLastAccess.findAll", query = "select o from EmsDashboardLastAccess o") })
@Table(name = "EMS_DASHBOARD_LAST_ACCESS")
@IdClass(EmsDashboardLastAccessPK.class)
public class EmsDashboardLastAccess implements Serializable {
    private static final long serialVersionUID = -3829558525926721782L;
    @Id
    @Column(name = "ACCESSED_BY", nullable = false, length = 128)
    private String accessedBy;
    @Column(name = "ACCESS_DATE", nullable = false)
    private Timestamp accessDate;
    @Id
    @Column(name = "DASHBOARD_ID", nullable = false)
    private BigDecimal dashboardId;
    @Column(name = "TENANT_ID", nullable = false, length = 32)
    private String tenantId;

    public EmsDashboardLastAccess() {
    }

    public EmsDashboardLastAccess(Timestamp accessDate, String accessedBy, BigDecimal dashboardId, String tenantId) {
        this.accessDate = accessDate;
        this.accessedBy = accessedBy;
        this.dashboardId = dashboardId;
        this.tenantId = tenantId;
    }

    public String getAccessedBy() {
        return accessedBy;
    }

    public void setAccessedBy(String accessedBy) {
        this.accessedBy = accessedBy;
    }

    public Timestamp getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Timestamp accessDate) {
        this.accessDate = accessDate;
    }

    public BigDecimal getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(BigDecimal dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
