package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * To create ID generator sequence "EMS_DASHBOARD_ID_SEQ_GEN":
 * CREATE SEQUENCE "EMS_DASHBOARD_ID_SEQ_GEN" INCREMENT BY 50 START WITH 50;
 */
@Entity
@NamedQueries({ @NamedQuery(name = "EmsDashboard.findAll", query = "select o from EmsDashboard o") })
@Table(name = "EMS_DASHBOARD")
@SequenceGenerator(name = "EmsDashboard_Id_Seq_Gen", sequenceName="EMS_DASHBOARD_SEQ", allocationSize = 1 )
public class EmsDashboard implements Serializable {
    private static final long serialVersionUID = 1219062974568988740L;
    
    @Id
    @Column(name = "DASHBOARD_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EmsDashboard_Id_Seq_Gen")
    private Long dashboardId;
    @Column(name = "DELETED")
    private Integer deleted;
    @Column(name = "DESCRIPTION", length = 256)
    private String description;
    @Column(name = "ENABLE_TIME_RANGE", nullable = false)
    private Integer enableTimeRange;
    @Column(name = "IS_SYSTEM", nullable = false)
    private Integer isSystem;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;
    @Column(name = "LAST_MODIFIED_BY", length = 128)
    private String lastModifiedBy;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 128)
    private String owner;
    @Column(name = "SCREEN_SHOT")
    @Lob
    private byte[] screenShot;
    @Column(name = "TENANT_ID", nullable = false, length = 32)
    private String tenantId;
    @Column(nullable = false)
    private Integer type;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dashboard", orphanRemoval = true)
    private List<EmsDashboardTile> dashboardTileList;

    public EmsDashboard() {
    }

    public EmsDashboard(Date creationDate, Long dashboardId, Integer deleted, String description,
                        Integer enableTimeRange, Integer isSystem, Date lastModificationDate,
                        String lastModifiedBy, String name, String owner, byte[] screenShot, String tenantId,
                        Integer type) {
        this.creationDate = creationDate;
        this.dashboardId = dashboardId;
        this.deleted = deleted;
        this.description = description;
        this.enableTimeRange = enableTimeRange;
        this.isSystem = isSystem;
        this.lastModificationDate = lastModificationDate;
        this.lastModifiedBy = lastModifiedBy;
        this.name = name;
        this.owner = owner;
        this.screenShot = screenShot;
        this.tenantId = tenantId;
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEnableTimeRange() {
        return enableTimeRange;
    }

    public void setEnableTimeRange(Integer enableTimeRange) {
        this.enableTimeRange = enableTimeRange;
    }

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public byte[] getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(byte[] screenShot) {
        this.screenShot = screenShot;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<EmsDashboardTile> getDashboardTileList() {
        return dashboardTileList;
    }

    public void setDashboardTileList(List<EmsDashboardTile> emsDashboardTileList) {
        this.dashboardTileList = emsDashboardTileList;
    }

    public EmsDashboardTile addEmsDashboardTile(EmsDashboardTile emsDashboardTile) {
        getDashboardTileList().add(emsDashboardTile);
        emsDashboardTile.setDashboard(this);
        return emsDashboardTile;
    }

    public EmsDashboardTile removeEmsDashboardTile(EmsDashboardTile emsDashboardTile) {
        getDashboardTileList().remove(emsDashboardTile);
        emsDashboardTile.setDashboard(null);
        return emsDashboardTile;
    }
}
