package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * To create ID generator sequence "EMS_DASHBOARD_TILE_ID_SEQ_GEN":
 * CREATE SEQUENCE "EMS_DASHBOARD_TILE_ID_SEQ_GEN" INCREMENT BY 50 START WITH 50;
 */
@Entity
@NamedQueries({ @NamedQuery(name = "EmsDashboardTile.findAll", query = "select o from EmsDashboardTile o") })
@Table(name = "EMS_DASHBOARD_TILE")
@SequenceGenerator(name = "EmsDashboardTile_Id_Seq_Gen", sequenceName = "EMS_DASHBOARD_TILE_SEQ", allocationSize = 1)
public class EmsDashboardTile implements Serializable {
    private static final long serialVersionUID = 6307069723661684517L;
    
    @Id
    @Column(name = "TILE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EmsDashboardTile_Id_Seq_Gen")
    private Long tileId;
    @Column(nullable = false, length = 64)
    private String title;
    @Column
    private Integer height;
    @Column
    private Integer width;
    @Column(name = "POSITION", nullable = false)
    private Integer position;
    @Column(name = "IS_MAXIMIZED", nullable = false)
    private Integer isMaximized;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;
    @Column(name = "LAST_MODIFIED_BY", length = 128)
    private String lastModifiedBy;
    @Column(nullable = false, length = 128)
    private String owner;
    @Column(name = "PROVIDER_ASSET_ROOT", length = 64)
    private String providerAssetRoot;
    @Column(name = "PROVIDER_NAME", length = 64)
    private String providerName;
    @Column(name = "PROVIDER_VERSION", length = 64)
    private String providerVersion;
    @Column(name = "TENANT_ID", nullable = false, length = 32)
    private String tenantId;
    @Column(name = "WIDGET_CREATION_TIME", nullable = false, length = 32)
    private String widgetCreationTime;
    @Column(name = "WIDGET_DESCRIPTION", length = 256)
    private String widgetDescription;
    @Column(name = "WIDGET_GROUP_NAME", length = 64)
    private String widgetGroupName;
    @Column(name = "WIDGET_HISTOGRAM", nullable = false, length = 1024)
    private String widgetHistogram;
    @Column(name = "WIDGET_ICON", nullable = false, length = 1024)
    private String widgetIcon;
    @Column(name = "WIDGET_KOC_NAME", nullable = false, length = 256)
    private String widgetKocName;
    @Column(name = "WIDGET_NAME", nullable = false, length = 64)
    private String widgetName;
    @Column(name = "WIDGET_OWNER", nullable = false, length = 128)
    private String widgetOwner;
    @Column(name = "WIDGET_SOURCE", nullable = false)
    private Integer widgetSource;
    @Column(name = "WIDGET_TEMPLATE", nullable = false, length = 1024)
    private String widgetTemplate;
    @Column(name = "WIDGET_UNIQUE_ID", nullable = false, length = 64)
    private String widgetUniqueId;
    @Column(name = "WIDGET_VIEWMODE", nullable = false, length = 1024)
    private String widgetViewmode;
    
    @ManyToOne
    @JoinColumn(name = "DASHBOARD_ID")
    private EmsDashboard dashboard;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dashboardTile", orphanRemoval = true)
    private List<EmsDashboardTileParams> dashboardTileParamsList;

    public EmsDashboardTile() {
    }

    public EmsDashboardTile(Date creationDate, EmsDashboard emsDashboard1, Integer height, Integer isMaximized,
                            Date lastModificationDate, String lastModifiedBy, String owner, Integer position,
                            String providerAssetRoot, String providerName, String providerVersion, String tenantId,
                            Long tileId, String title, String widgetCreationTime, String widgetDescription,
                            String widgetGroupName, String widgetHistogram, String widgetIcon, String widgetKocName,
                            String widgetName, String widgetOwner, Integer widgetSource, String widgetTemplate,
                            String widgetUniqueId, String widgetViewmode, Integer width) {
        this.creationDate = creationDate;
        this.dashboard = emsDashboard1;
        this.height = height;
        this.isMaximized = isMaximized;
        this.lastModificationDate = lastModificationDate;
        this.lastModifiedBy = lastModifiedBy;
        this.owner = owner;
        this.position = position;
        this.providerAssetRoot = providerAssetRoot;
        this.providerName = providerName;
        this.providerVersion = providerVersion;
        this.tenantId = tenantId;
        this.tileId = tileId;
        this.title = title;
        this.widgetCreationTime = widgetCreationTime;
        this.widgetDescription = widgetDescription;
        this.widgetGroupName = widgetGroupName;
        this.widgetHistogram = widgetHistogram;
        this.widgetIcon = widgetIcon;
        this.widgetKocName = widgetKocName;
        this.widgetName = widgetName;
        this.widgetOwner = widgetOwner;
        this.widgetSource = widgetSource;
        this.widgetTemplate = widgetTemplate;
        this.widgetUniqueId = widgetUniqueId;
        this.widgetViewmode = widgetViewmode;
        this.width = width;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getIsMaximized() {
        return isMaximized;
    }

    public void setIsMaximized(Integer isMaximized) {
        this.isMaximized = isMaximized;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getProviderAssetRoot() {
        return providerAssetRoot;
    }

    public void setProviderAssetRoot(String providerAssetRoot) {
        this.providerAssetRoot = providerAssetRoot;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderVersion() {
        return providerVersion;
    }

    public void setProviderVersion(String providerVersion) {
        this.providerVersion = providerVersion;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTileId() {
        return tileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWidgetCreationTime() {
        return widgetCreationTime;
    }

    public void setWidgetCreationTime(String widgetCreationTime) {
        this.widgetCreationTime = widgetCreationTime;
    }

    public String getWidgetDescription() {
        return widgetDescription;
    }

    public void setWidgetDescription(String widgetDescription) {
        this.widgetDescription = widgetDescription;
    }

    public String getWidgetGroupName() {
        return widgetGroupName;
    }

    public void setWidgetGroupName(String widgetGroupName) {
        this.widgetGroupName = widgetGroupName;
    }

    public String getWidgetHistogram() {
        return widgetHistogram;
    }

    public void setWidgetHistogram(String widgetHistogram) {
        this.widgetHistogram = widgetHistogram;
    }

    public String getWidgetIcon() {
        return widgetIcon;
    }

    public void setWidgetIcon(String widgetIcon) {
        this.widgetIcon = widgetIcon;
    }

    public String getWidgetKocName() {
        return widgetKocName;
    }

    public void setWidgetKocName(String widgetKocName) {
        this.widgetKocName = widgetKocName;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getWidgetOwner() {
        return widgetOwner;
    }

    public void setWidgetOwner(String widgetOwner) {
        this.widgetOwner = widgetOwner;
    }

    public Integer getWidgetSource() {
        return widgetSource;
    }

    public void setWidgetSource(Integer widgetSource) {
        this.widgetSource = widgetSource;
    }

    public String getWidgetTemplate() {
        return widgetTemplate;
    }

    public void setWidgetTemplate(String widgetTemplate) {
        this.widgetTemplate = widgetTemplate;
    }

    public String getWidgetUniqueId() {
        return widgetUniqueId;
    }

    public void setWidgetUniqueId(String widgetUniqueId) {
        this.widgetUniqueId = widgetUniqueId;
    }

    public String getWidgetViewmode() {
        return widgetViewmode;
    }

    public void setWidgetViewmode(String widgetViewmode) {
        this.widgetViewmode = widgetViewmode;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public EmsDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(EmsDashboard emsDashboard1) {
        this.dashboard = emsDashboard1;
    }

    public List<EmsDashboardTileParams> getDashboardTileParamsList() {
        return dashboardTileParamsList;
    }

    public void setDashboardTileParamsList(List<EmsDashboardTileParams> emsDashboardTileParamsList) {
        this.dashboardTileParamsList = emsDashboardTileParamsList;
    }

    public EmsDashboardTileParams addEmsDashboardTileParams(EmsDashboardTileParams emsDashboardTileParams) {
        if (this.dashboardTileParamsList == null) {
            this.dashboardTileParamsList = new ArrayList<EmsDashboardTileParams>();
        }
        this.dashboardTileParamsList.add(emsDashboardTileParams);
        emsDashboardTileParams.setDashboardTile(this);
        return emsDashboardTileParams;
    }

    public EmsDashboardTileParams removeEmsDashboardTileParams(EmsDashboardTileParams emsDashboardTileParams) {
        getDashboardTileParamsList().remove(emsDashboardTileParams);
        emsDashboardTileParams.setDashboardTile(null);
        return emsDashboardTileParams;
    }
}
