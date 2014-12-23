package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 * To create ID generator sequence "EMS_DASHBOARD_TILE_ID_SEQ_GEN":
 * CREATE SEQUENCE "EMS_DASHBOARD_TILE_ID_SEQ_GEN" INCREMENT BY 50 START WITH 50;
 */
@Entity
@NamedQueries({ @NamedQuery(name = "EmsDashboardTile.findAll", query = "select o from EmsDashboardTile o") })
@Table(name = "EMS_DASHBOARD_TILE")
@SequenceGenerator(name = "EmsDashboardTile_Id_Seq_Gen", sequenceName = "EMS_DASHBOARD_TILE_ID_SEQ_GEN",
                   allocationSize = 50, initialValue = 50)
public class EmsDashboardTile implements Serializable {
    private static final long serialVersionUID = 6307069723661684517L;
    @Column(name = "CREATION_DATE", nullable = false)
    private Timestamp creationDate;
    private BigDecimal height;
    @Column(name = "IS_MAXIMIZED", nullable = false)
    private Integer isMaximized;
    @Column(name = "LAST_MODIFICATION_DATE")
    private Timestamp lastModificationDate;
    @Column(name = "LAST_MODIFIED_BY", length = 128)
    private String lastModifiedBy;
    @Column(nullable = false, length = 128)
    private String owner;
    @Column(nullable = false)
    private BigDecimal position;
    @Column(name = "PROVIDER_ASSET_ROOT", length = 64)
    private String providerAssetRoot;
    @Column(name = "PROVIDER_NAME", length = 64)
    private String providerName;
    @Column(name = "PROVIDER_VERSION", length = 64)
    private String providerVersion;
    @Column(name = "TENANT_ID", nullable = false, length = 32)
    private String tenantId;
    @Id
    @Column(name = "TILE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EmsDashboardTile_Id_Seq_Gen")
    private BigDecimal tileId;
    @Column(nullable = false, length = 64)
    private String title;
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
    private BigDecimal widgetSource;
    @Column(name = "WIDGET_TEMPLATE", nullable = false, length = 1024)
    private String widgetTemplate;
    @Column(name = "WIDGET_UNIQUE_ID", nullable = false, length = 64)
    private String widgetUniqueId;
    @Column(name = "WIDGET_VIEWMODE", nullable = false, length = 1024)
    private String widgetViewmode;
    private BigDecimal width;
    @ManyToOne
    @JoinColumn(name = "DASHBOARD_ID")
    private EmsDashboard dashboard;
    @OneToMany(mappedBy = "emsDashboardTile", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<EmsDashboardTileParams> emsDashboardTileParamsList;

    public EmsDashboardTile() {
    }

    public EmsDashboardTile(Timestamp creationDate, EmsDashboard emsDashboard1, BigDecimal height, Integer isMaximized,
                            Timestamp lastModificationDate, String lastModifiedBy, String owner, BigDecimal position,
                            String providerAssetRoot, String providerName, String providerVersion, String tenantId,
                            BigDecimal tileId, String title, String widgetCreationTime, String widgetDescription,
                            String widgetGroupName, String widgetHistogram, String widgetIcon, String widgetKocName,
                            String widgetName, String widgetOwner, BigDecimal widgetSource, String widgetTemplate,
                            String widgetUniqueId, String widgetViewmode, BigDecimal width) {
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }


    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Integer getIsMaximized() {
        return isMaximized;
    }

    public void setIsMaximized(Integer isMaximized) {
        this.isMaximized = isMaximized;
    }

    public Timestamp getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Timestamp lastModificationDate) {
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

    public BigDecimal getPosition() {
        return position;
    }

    public void setPosition(BigDecimal position) {
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

    public BigDecimal getTileId() {
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

    public BigDecimal getWidgetSource() {
        return widgetSource;
    }

    public void setWidgetSource(BigDecimal widgetSource) {
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

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public EmsDashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(EmsDashboard emsDashboard1) {
        this.dashboard = emsDashboard1;
    }

    public List<EmsDashboardTileParams> getEmsDashboardTileParamsList() {
        return emsDashboardTileParamsList;
    }

    public void setEmsDashboardTileParamsList(List<EmsDashboardTileParams> emsDashboardTileParamsList) {
        this.emsDashboardTileParamsList = emsDashboardTileParamsList;
    }

    public EmsDashboardTileParams addEmsDashboardTileParams(EmsDashboardTileParams emsDashboardTileParams) {
        getEmsDashboardTileParamsList().add(emsDashboardTileParams);
        emsDashboardTileParams.setEmsDashboardTile(this);
        return emsDashboardTileParams;
    }

    public EmsDashboardTileParams removeEmsDashboardTileParams(EmsDashboardTileParams emsDashboardTileParams) {
        getEmsDashboardTileParamsList().remove(emsDashboardTileParams);
        emsDashboardTileParams.setEmsDashboardTile(null);
        return emsDashboardTileParams;
    }
}
