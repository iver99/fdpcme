package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;

public class Dashboard {
	public static final int DASHBOARD_TYPE_NORMAL   = Integer.valueOf(0);
	public static final int DASHBOARD_TYPE_ONE_PAGE = Integer.valueOf(1);
	public static final boolean DASHBOARD_ENABLE_TIME_RANGE_DEFAULT = Boolean.FALSE;
	public static final boolean DASHBOARD_DELETED_DEFAULT = Boolean.FALSE;
	
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
	private String screenShot;
	private Integer type;
	
	private List<Tile> tileList;
	
	public Dashboard() {
		// defaults for non-null values
		type = Dashboard.DASHBOARD_TYPE_NORMAL;
		enableTimeRange = Dashboard.DASHBOARD_ENABLE_TIME_RANGE_DEFAULT;
		deleted = DASHBOARD_DELETED_DEFAULT;
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

    public String getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(String screenShot) {
        this.screenShot = screenShot;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Tile> getTileList() {
        return tileList;
    }

    public void setTileList(List<Tile> emsDashboardTileList) {
        this.tileList = emsDashboardTileList;
    }

    public Tile addTile(Tile tile) {
    	if (tileList == null)
    		tileList = new ArrayList<Tile>();
    	tileList.add(tile);
        tile.setDashboard(this);
        return tile;
    }

    public Tile removeEmsDashboardTile(Tile emsDashboardTile) {
    	getTileList().remove(emsDashboardTile);
        emsDashboardTile.setDashboard(null);
        return emsDashboardTile;
    }
    
    public EmsDashboard getPersistenceEntity(EmsDashboard ed) {
    	Integer isDeleted = DataFormatUtils.boolean2Integer(this.deleted);
    	Integer isEnableTimeRange = DataFormatUtils.boolean2Integer(this.enableTimeRange);
    	Integer isIsSystem = DataFormatUtils.boolean2Integer(this.isSystem == null);
    	if (ed == null) {
    		ed = new EmsDashboard(creationDate, dashboardId, isDeleted, description, isEnableTimeRange,isIsSystem,
    			lastModificationDate, lastModifiedBy, name, owner, screenShot, type);
	//    	List<EmsDashboardTile> edtList = new ArrayList<EmsDashboardTile>();
	    	if (tileList != null) {
	    		int i = 0;
	    		for (Tile tile: tileList) {
	    			EmsDashboardTile edt = tile.getPersistenceEntity(null);
	    			edt.setPosition(i++);
	    			ed.addEmsDashboardTile(edt);
	//    			edt.setDashboard(ed);
	//    			edtList.add(edt);
	    		}
	//    		ed.setDashboardTileList(edtList);
	    	}
    	}
    	else {
//    		ed.setCreationDate(creationDate);
    		ed.setDeleted(isDeleted);
    		ed.setDescription(description);
    		ed.setEnableTimeRange(isEnableTimeRange);
    		ed.setIsSystem(isIsSystem);
    		ed.setLastModificationDate(lastModificationDate);
    		ed.setLastModifiedBy(lastModifiedBy);
    		ed.setName(name);
//    		ed.setOwner(owner);
    		ed.setScreenShot(screenShot);
    		ed.setType(type);
    		updateEmsDashboardTiles(this.tileList, ed);
    	}
    	return ed;
    }
	
	private void updateEmsDashboardTiles(List<Tile> tiles, EmsDashboard ed) {
		Map<Tile, EmsDashboardTile> rows = new HashMap<Tile, EmsDashboardTile>();
		// remove deleted tile row in dashboard row first
		List<EmsDashboardTile> edtList = ed.getDashboardTileList();
		if (edtList != null) {
			int edtSize = edtList.size();
			for (int i = edtSize - 1; i >= 0; i--) {
				EmsDashboardTile edt = edtList.get(i);
				boolean isDeleted = true;
				for (Tile tile: tiles) {
					if (tile.getTileId() != null && tile.getTileId().equals(edt.getTileId())) {
						isDeleted = false;
						rows.put(tile, edt);
//						// remove existing props
//						List<EmsDashboardTileParams> edtpList = edt.getDashboardTileParamsList();
//						if (edtpList == null)
//							break;
//						while (!edt.getDashboardTileParamsList().isEmpty()) {
//							EmsDashboardTileParams edtp = edt.getDashboardTileParamsList().get(0);
////							dsf.removeEmsDashboardTileParams(edtp);
//							edt.getDashboardTileParamsList().remove(edtp);
////							edt.removeEmsDashboardTileParams(edtp);
//						}
						break;
					}
				}
				if (isDeleted) {
//					ed.removeEmsDashboardTile(edt);
					ed.getDashboardTileList().remove(edt);
				}
			}
		}
		
		if (tiles == null)
			return;
		for (Tile tile: tiles) {
			EmsDashboardTile edt = null;
			if (!rows.containsKey(tile)) {
				edt = tile.getPersistenceEntity(null);
				ed.addEmsDashboardTile(edt);
				rows.put(tile, edt);
//				dsf.persistEntity(edt);
			}
			else {
				edt = rows.get(tile);
				tile.getPersistenceEntity(edt);
			}
		}
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
    	to.setType(from.getType());
    	List<EmsDashboardTile> edtList = from.getDashboardTileList();
    	if (edtList != null) {
    		List<Tile> tileList = new ArrayList<Tile>();
    		for (EmsDashboardTile edt: edtList) {
    			Tile tile = Tile.valueOf(edt);
    			tile.setDashboard(to);
    			tileList.add(tile);
    		}
    		to.setTileList(tileList);
    	}
    	return to;
    }
}
