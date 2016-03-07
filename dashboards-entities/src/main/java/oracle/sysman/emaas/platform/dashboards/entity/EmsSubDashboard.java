package oracle.sysman.emaas.platform.dashboards.entity;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author jishshi
 * @since 2016/3/3.
 */

@Entity
@Table(name = "EMS_DASHBOARD_SET")
@IdClass(EmsDashboardSetPK.class)
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", length = 32, primaryKey = true)
public class EmsSubDashboard implements Serializable {

    private static final long serialVersionUID = 8344138185588082239L;

    @Id
    @Column(name = "DASHBOARD_SET_ID", nullable = false, length = 256)
    private Long dashboardSetId;
    @Id
    @Column(name = "SUB_DASHBOARD_ID", nullable = false, length = 256)
    private Long subDashboardId;

    @Column(name = "POSITION", nullable = false)
    private Integer position;

    @OneToOne
    @JoinColumns(value = {
            @JoinColumn(name = "SUB_DASHBOARD_ID", referencedColumnName = "DASHBOARD_ID",insertable = false,updatable = false),
            @JoinColumn(name = "TENANT_ID", referencedColumnName = "TENANT_ID",insertable = false,updatable = false)
    })
    private EmsDashboard subDashboard;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "DASHBOARD_SET_ID", referencedColumnName = "DASHBOARD_ID",insertable = false,updatable = false),
            @JoinColumn(name = "TENANT_ID", referencedColumnName = "TENANT_ID",insertable = false,updatable = false)
    })
    private EmsDashboard dashboardSet;

    public EmsSubDashboard() {

    }

    public EmsSubDashboard(Long dashboardSetId, Long subDashboardId, int position) {
        this.dashboardSetId = dashboardSetId;
        this.subDashboardId = subDashboardId;
        this.position = position;
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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }


    public Long getDashboardSetId() {
        return dashboardSetId;
    }

    public EmsDashboard getSubDashboard() {
        return subDashboard;
    }

    public void setSubDashboard(EmsDashboard subDashboard) {
        this.subDashboard = subDashboard;
    }

    public EmsDashboard getDashboardSet() {
        return dashboardSet;
    }

    public void setDashboardSet(EmsDashboard dashboardSet) {
        this.dashboardSet = dashboardSet;
    }
}
