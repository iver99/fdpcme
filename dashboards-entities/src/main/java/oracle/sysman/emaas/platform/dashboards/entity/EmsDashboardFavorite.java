package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "EmsDashboardFavorite.findAll", query = "select o from EmsDashboardFavorite o") })
@Table(name = "EMS_DASHBOARD_FAVORITE")
@IdClass(EmsDashboardFavoritePK.class)
public class EmsDashboardFavorite implements Serializable {
    private static final long serialVersionUID = -8636822891842500745L;
    @Column(name = "CREATION_DATE", nullable = false)
    private Timestamp creationDate;
    @Id
    @Column(name = "TENANT_ID", nullable = false, length = 32)
    private String tenantId;
    @Id
    @Column(name = "USER_NAME", nullable = false, length = 128)
    private String userName;
    @ManyToOne
    @Id
    @JoinColumn(name = "DASHBOARD_ID")
    private EmsDashboard emsDashboard;

    public EmsDashboardFavorite() {
    }

    public EmsDashboardFavorite(Timestamp creationDate, EmsDashboard emsDashboard, String tenantId, String userName) {
        this.creationDate = creationDate;
        this.emsDashboard = emsDashboard;
        this.tenantId = tenantId;
        this.userName = userName;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
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

    public EmsDashboard getEmsDashboard() {
        return emsDashboard;
    }

    public void setEmsDashboard(EmsDashboard emsDashboard) {
        this.emsDashboard = emsDashboard;
    }
}
