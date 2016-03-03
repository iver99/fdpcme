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
public class EmsDashboardSet implements Serializable {

    private static final long serialVersionUID = 8344138185588082239L;

    @Id
    @Column(name = "DASHBOARD_SET_ID", nullable = false, length = 256)
    private Long dashboardSetId;
    @Id
    @Column(name = "TENANT_ID", nullable = false, length = 128)
    private String userName;
    @Id
    @Column(name = "SUB_DASHBOARD_ID", nullable = false, length = 256)
    private Long subDashboardId;

    @Column(name = "POSITION", nullable = false)
    private Integer position;

    @ManyToOne
    @Id
    @JoinColumns(value = {
            @JoinColumn(name = "DASHBOARD_SET_ID", referencedColumnName = "DASHBOARD_ID"),
            @JoinColumn(name = "TENANT_ID", referencedColumnName = "TENANT_ID")
    })
    private EmsDashboard dbdOfDashboardSet;

    public EmsDashboardSet() {

    }

    public void setDashboardSetId(Long dashboardSetId) {
        this.dashboardSetId = dashboardSetId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public EmsDashboard getDbdOfDashboardSet() {
        return dbdOfDashboardSet;
    }

    public void setDbdOfDashboardSet(EmsDashboard dashboard2) {
        this.dbdOfDashboardSet = dashboard2;
    }
}
