package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "EmsDashboardFavorite.findAll", query = "select o from EmsDashboardFavorite o") })
@Table(name = "EMS_DASHBOARD_FAVORITE")
@IdClass(EmsDashboardFavoritePK.class)
public class EmsDashboardFavorite implements Serializable {
    private static final long serialVersionUID = -8636822891842500745L;
    
    @Id
    @Column(name = "TENANT_ID", nullable = false, length = 32)
    private String tenantId;
    @Id
    @Column(name = "USER_NAME", nullable = false, length = 128)
    private String userName;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;
    @Id
    @ManyToOne
    @JoinColumn(name = "DASHBOARD_ID", referencedColumnName = "DASHBOARD_ID")
    private EmsDashboard dashboard;

    public EmsDashboardFavorite() {
    }

    public EmsDashboardFavorite(Date creationDate, EmsDashboard dashboard, String tenantId, String userName) {
        this.creationDate = creationDate;
        this.dashboard = dashboard;
        this.tenantId = tenantId;
        this.userName = userName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public EmsDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(EmsDashboard emsDashboard) {
        this.dashboard = emsDashboard;
    }
}
