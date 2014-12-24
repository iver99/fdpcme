package oracle.sysman.emaas.platform.dashboards.core.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

public class Tile {
	public static final int WIDGET_SOURCE_DASHBOARD_FRAMEWORK = 0;
	public static final int WIDGET_SOURCE_INTEGRATOR          = 1;
	
	private Date creationDate;
	private Integer height;
	private Boolean isMaximized;
	private Date lastModificationDate;
	private String lastModifiedBy;
	private String owner;
	private int position;
	private String providerAssetRoot;
	private String providerName;
	private String providerVersion;
	private String tenantId;
	private Long tileId;
	private String title;
	private String widgetCreationTime;
	private String widgetDescription;
	private String widgetGroupName;
	private String widgetHistogram;
	private String widgetIcon;
	private String widgetKocName;
	private String widgetName;
	private String widgetOwner;
	private Integer widgetSource;
	private String widgetTemplate;
	private String widgetUniqueId;
	private String widgetViewmode;
	private Integer width;
	private Dashboard dashboard;
	private List<TileParam> parameters;

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

    public Boolean getIsMaximized() {
        return isMaximized;
    }

    public void setIsMaximized(Boolean isMaximized) {
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
    
    public void setTileId(Long tileId) {
    	this.tileId = tileId;
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

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard emsDashboard1) {
        this.dashboard = emsDashboard1;
    }
    
    public List<TileParam> getParameters() {
		return parameters;
	}

	public void setParameters(List<TileParam> parameters) {
		this.parameters = parameters;
	}

	/**
     * Returns an instance of EmsDashboardTile for current tile object
     * @return
     */
    public EmsDashboardTile getTileEntity() {
    	Timestamp tsCreateDate = DataFormatUtils.date2Timestamp(creationDate);
    	EmsDashboard ed = this.getDashboard() == null? null: this.getDashboard().getPersistenceEntity();
    	BigDecimal bdHeight = DataFormatUtils.integer2BigDecimal(this.getHeight());
    	Integer intIsMaximized = DataFormatUtils.boolean2Integer(this.isMaximized);
    	Timestamp tslastModificationDate = DataFormatUtils.date2Timestamp(new Timestamp(this.lastModificationDate.getTime()));
    	BigDecimal bdPosition = DataFormatUtils.integer2BigDecimal(this.getPosition());
    	BigDecimal bdTileId = DataFormatUtils.long2BigDecimal(this.getTileId());
    	BigDecimal bdWidgetSource = DataFormatUtils.integer2BigDecimal(this.getWidgetSource());
    	BigDecimal bdWidth = DataFormatUtils.integer2BigDecimal(this.getWidth());
    	EmsDashboardTile edt = new EmsDashboardTile(tsCreateDate, ed, bdHeight, intIsMaximized, tslastModificationDate,
    			lastModifiedBy, owner, bdPosition, providerAssetRoot, providerName, providerVersion, tenantId,
    			bdTileId, title, widgetCreationTime, widgetDescription, widgetGroupName, widgetHistogram, widgetIcon,
    			widgetKocName, widgetName, widgetOwner, bdWidgetSource, widgetTemplate, widgetUniqueId, widgetViewmode,
    			bdWidth);
    	if (parameters != null) {
    		List<EmsDashboardTileParams> edtpList = new ArrayList<EmsDashboardTileParams>();
    		for (TileParam param: parameters) {
    			EmsDashboardTileParams edtp = param.getPersistentEntity();
    			edtpList.add(edtp);
    		}
    		edt.setEmsDashboardTileParamsList(edtpList);
    	}
    	return edt;
    }
    
    public static Tile valueOf(EmsDashboardTile edt) {
    	if (edt == null)
    		return null;
    	Tile tile = new Tile();
    	tile.setCreationDate(DataFormatUtils.timestamp2Date(edt.getCreationDate()));
    	tile.setDashboard(Dashboard.valueOf(edt.getDashboard()));
    	tile.setHeight(DataFormatUtils.bigDecimal2Integer(edt.getHeight()));
    	tile.setIsMaximized(DataFormatUtils.integer2Boolean(edt.getIsMaximized()));
    	tile.setLastModificationDate(DataFormatUtils.timestamp2Date(edt.getLastModificationDate()));
    	tile.setLastModifiedBy(edt.getLastModifiedBy());
    	tile.setOwner(edt.getOwner());
    	tile.setPosition(DataFormatUtils.bigDecimal2Integer(edt.getPosition()));
    	tile.setProviderAssetRoot(edt.getProviderAssetRoot());
    	tile.setProviderName(edt.getProviderName());
    	tile.setProviderVersion(edt.getProviderVersion());
    	tile.setTenantId(edt.getTenantId());
    	tile.setTileId(DataFormatUtils.bigDecimal2Long(edt.getTileId()));
    	tile.setTitle(edt.getTitle());
    	tile.setWidgetCreationTime(edt.getWidgetCreationTime());
    	tile.setWidgetDescription(edt.getWidgetDescription());
    	tile.setWidgetGroupName(edt.getWidgetGroupName());
    	tile.setWidgetHistogram(edt.getWidgetHistogram());
    	tile.setWidgetIcon(edt.getWidgetIcon());
    	tile.setWidgetKocName(edt.getWidgetKocName());
    	tile.setWidgetName(edt.getWidgetName());
    	tile.setWidgetOwner(edt.getWidgetOwner());
    	tile.setWidgetSource(DataFormatUtils.bigDecimal2Integer(edt.getWidgetSource()));
    	tile.setWidgetTemplate(edt.getWidgetTemplate());
    	tile.setWidgetUniqueId(edt.getWidgetUniqueId());
    	tile.setWidgetViewmode(edt.getWidgetViewmode());
    	tile.setWidth(DataFormatUtils.bigDecimal2Integer(edt.getWidth()));
    	List<EmsDashboardTileParams> edtpList = edt.getEmsDashboardTileParamsList();
    	if (edtpList != null) {
    		List<TileParam> paras = new ArrayList<TileParam>();
    		for (EmsDashboardTileParams edtp: edtpList) {
    			paras.add(TileParam.valueOf(edtp));
    		}
    		tile.setParameters(paras);
    	}
    	return tile;
    }
}
