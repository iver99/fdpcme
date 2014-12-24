package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;

public class Dashboard {
	private Long dashboardId;
	private String name;
	private Date creationDate;
	private Boolean deleted;
	private String description;
	private Boolean enableTimeRange;
	private Boolean isSystem;
	private Date lastModificationDate;
	private String lastModifiedBy;
	private String owner;
	private byte[] screenShot;
	private String tenantId;
	private Integer type;
	
	private List<Tile> tileList;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnableTimeRange() {
        return enableTimeRange;
    }

    public void setEnableTimeRange(Boolean enableTimeRange) {
        this.enableTimeRange = enableTimeRange;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
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

    public List<Tile> getTileList() {
    	if (tileList == null)
    		tileList = new ArrayList<Tile>();
        return tileList;
    }

    public void setTileList(List<Tile> emsDashboardTileList) {
        this.tileList = emsDashboardTileList;
    }

    public Tile addTile(Tile emsDashboardTile) {
    	getTileList().add(emsDashboardTile);
        emsDashboardTile.setDashboard(this);
        return emsDashboardTile;
    }

    public Tile removeEmsDashboardTile(Tile emsDashboardTile) {
    	getTileList().remove(emsDashboardTile);
        emsDashboardTile.setDashboard(null);
        return emsDashboardTile;
    }
    
    public EmsDashboard getPersistenceEntity() {
    	Integer isDeleted = DataFormatUtils.boolean2Integer(this.deleted);
    	Integer isEnableTimeRange = DataFormatUtils.boolean2Integer(this.enableTimeRange);
    	Integer isIsSystem = DataFormatUtils.boolean2Integer(this.isSystem == null);
    	EmsDashboard ed = new EmsDashboard(creationDate, dashboardId, isDeleted, description, isEnableTimeRange,isIsSystem,
    			lastModificationDate, lastModifiedBy, name, owner, screenShot, tenantId, type);
    	List<EmsDashboardTile> edtList = new ArrayList<EmsDashboardTile>();
    	if (tileList != null) {
    		for (Tile tile: tileList) {
    			edtList.add(tile.getTileEntity());
    		}
    		ed.setDashboardTileList(edtList);
    	}
    	return ed;
    }
    
    public static Dashboard valueOf(EmsDashboard ed) {
    	return valueOf(ed, null);
    }
    
    public static Dashboard valueOf(EmsDashboard from, Dashboard to) {
    	if (from == null)
    		return null;
    	if (to == null)
    		to = new Dashboard();
    	to.setCreationDate(from.getCreationDate());
    	to.setDashboardId(from.getDashboardId());
    	to.setDeleted(DataFormatUtils.integer2Boolean(from.getDeleted()));
    	to.setDescription(from.getDescription());
    	to.setEnableTimeRange(DataFormatUtils.integer2Boolean(from.getEnableTimeRange()));
    	to.setIsSystem(DataFormatUtils.integer2Boolean(from.getIsSystem()));
    	to.setLastModificationDate(from.getLastModificationDate());
    	to.setLastModifiedBy(from.getLastModifiedBy());
    	to.setName(from.getName());
    	to.setOwner(from.getOwner());
    	to.setScreenShot(from.getScreenShot());
    	to.setTenantId(from.getTenantId());
    	to.setType(from.getType());
    	List<EmsDashboardTile> edtList = from.getDashboardTileList();
    	if (edtList != null) {
    		List<Tile> tileList = new ArrayList<Tile>();
    		for (EmsDashboardTile edt: edtList) {
    			Tile tile = Tile.valueOf(edt);
    			tileList.add(tile);
    		}
    		to.setTileList(tileList);
    	}
    	return to;
    }
}
