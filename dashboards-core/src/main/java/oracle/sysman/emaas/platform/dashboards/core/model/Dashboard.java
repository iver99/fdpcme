package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Dashboard
{
	public static final String DASHBOARD_TYPE_PLAIN = "PLAIN";
	public static final Integer DASHBOARD_TYPE_CODE_PLAIN = Integer.valueOf(0);
	public static final String DASHBOARD_TYPE_SOURCELINK = "SOURCELINK";
	public static final Integer DASHBOARD_TYPE_CODE_SOURCELINK = Integer.valueOf(1);
	public static final boolean DASHBOARD_ENABLE_TIME_RANGE_DEFAULT = Boolean.FALSE;
	public static final boolean DASHBOARD_DELETED_DEFAULT = Boolean.FALSE;

	public static Dashboard valueOf(EmsDashboard ed)
	{
		return Dashboard.valueOf(ed, null);
	}

	/**
	 * Get a dashboard instance from EmsDashboard instance, by providing the prototype dashboard object
	 *
	 * @param from
	 *            EmsDashboard instance
	 * @param to
	 *            prototype Dashboard object, and it's values will be covered by value from EmsDashboard instance, or a new
	 *            Dashboard instance will be created if it's null
	 * @return
	 */
	public static Dashboard valueOf(EmsDashboard from, Dashboard to)
	{
		if (from == null) {
			return null;
		}
		if (to == null) {
			to = new Dashboard();
		}
		to.setCreationDate(from.getCreationDate());
		to.setDashboardId(from.getDashboardId());
		to.setDeleted(from.getDeleted() == null ? null : from.getDeleted() > 0);
		to.setDescription(from.getDescription());
		to.setEnableTimeRange(DataFormatUtils.integer2Boolean(from.getEnableTimeRange()));
		to.setIsSystem(DataFormatUtils.integer2Boolean(from.getIsSystem()));
		to.setLastModificationDate(from.getLastModificationDate());
		to.setLastModifiedBy(from.getLastModifiedBy());
		to.setName(from.getName());
		to.setOwner(from.getOwner());
		// by default, we'll not load screenshot for query
		//		to.setScreenShot(from.getScreenShot());
		to.setType(DataFormatUtils.dashboardTypeInteger2String(from.getType()));
		List<EmsDashboardTile> edtList = from.getDashboardTileList();
		if (edtList != null) {
			List<Tile> tileList = new ArrayList<Tile>();
			for (EmsDashboardTile edt : edtList) {
				Tile tile = Tile.valueOf(edt);
				tile.setDashboard(to);
				tileList.add(tile);
			}
			to.setTileList(tileList);
		}
		return to;
	}

	@JsonProperty("id")
	private Long dashboardId;

	private String name;

	@JsonProperty("createdOn")
	private Date creationDate;

	@JsonIgnore
	private Boolean deleted;

	private String description;

	private Boolean enableTimeRange;

	@JsonProperty("systemDashboard")
	private Boolean isSystem;

	@JsonProperty("lastModifiedOn")
	private Date lastModificationDate;

	private String lastModifiedBy;

	private String owner;

	@JsonIgnore
	private String screenShot;

	private String screenShotHref;

	private String href;

	private String type;

	private List<Tile> tileList;

	public Dashboard()
	{
		// defaults for non-null values
		type = Dashboard.DASHBOARD_TYPE_PLAIN;
		enableTimeRange = Dashboard.DASHBOARD_ENABLE_TIME_RANGE_DEFAULT;
		deleted = DASHBOARD_DELETED_DEFAULT;
	}

	public Tile addTile(Tile tile)
	{
		if (tileList == null) {
			tileList = new ArrayList<Tile>();
		}
		tileList.add(tile);
		tile.setDashboard(this);
		return tile;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public Long getDashboardId()
	{
		return dashboardId;
	}

	public boolean getDeleted()
	{
		return deleted;
	}

	public String getDescription()
	{
		return description;
	}

	public Boolean getEnableTimeRange()
	{
		return enableTimeRange;
	}

	public String getHref()
	{
		return href;
	}

	public Boolean getIsSystem()
	{
		return isSystem;
	}

	public Date getLastModificationDate()
	{
		return lastModificationDate;
	}

	public String getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	public String getName()
	{
		return name;
	}

	public String getOwner()
	{
		return owner;
	}

	public EmsDashboard getPersistenceEntity(EmsDashboard ed) throws CommonFunctionalException
	{
		//    	Integer isDeleted = DataFormatUtils.boolean2Integer(this.deleted);
		Integer isEnableTimeRange = DataFormatUtils.boolean2Integer(enableTimeRange);
		Integer isIsSystem = DataFormatUtils.boolean2Integer(isSystem == null);
		Integer dashboardType = DataFormatUtils.dashboardTypeString2Integer(type);
		if (ed == null) {
			ed = new EmsDashboard(creationDate, dashboardId, 0L, description, isEnableTimeRange, isIsSystem,
					lastModificationDate, lastModifiedBy, name, owner, screenShot, dashboardType);
			//    	List<EmsDashboardTile> edtList = new ArrayList<EmsDashboardTile>();
			if (tileList != null) {
				int i = 0;
				for (Tile tile : tileList) {
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
			ed.setDeleted(deleted ? getDashboardId() : 0);
			ed.setDescription(description);
			ed.setEnableTimeRange(isEnableTimeRange);
			ed.setIsSystem(isIsSystem);
			ed.setLastModificationDate(lastModificationDate);
			ed.setLastModifiedBy(lastModifiedBy);
			ed.setName(name);
			//    		ed.setOwner(owner);
			ed.setScreenShot(screenShot);
			ed.setType(dashboardType);
			updateEmsDashboardTiles(tileList, ed);
		}
		return ed;
	}

	public String getScreenShot()
	{
		return screenShot;
	}

	public String getScreenShotHref()
	{
		return screenShotHref;
	}

	public List<Tile> getTileList()
	{
		return tileList;
	}

	public String getType()
	{
		return type;
	}

	public Tile removeTile(Tile tile)
	{
		getTileList().remove(tile);
		return tile;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public void setDashboardId(Long dashboardId)
	{
		this.dashboardId = dashboardId;
	}

	public void setDeleted(boolean deleted)
	{
		this.deleted = deleted;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setEnableTimeRange(Boolean enableTimeRange)
	{
		this.enableTimeRange = enableTimeRange;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public void setIsSystem(Boolean isSystem)
	{
		this.isSystem = isSystem;
	}

	public void setLastModificationDate(Date lastModificationDate)
	{
		this.lastModificationDate = lastModificationDate;
	}

	public void setLastModifiedBy(String lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public void setScreenShot(String screenShot)
	{
		this.screenShot = screenShot;
	}

	public void setScreenShotHref(String screenShotHref)
	{
		this.screenShotHref = screenShotHref;
	}

	public void setTileList(List<Tile> emsDashboardTileList)
	{
		tileList = emsDashboardTileList;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	private void updateEmsDashboardTiles(List<Tile> tiles, EmsDashboard ed) throws CommonFunctionalException
	{
		Map<Tile, EmsDashboardTile> rows = new HashMap<Tile, EmsDashboardTile>();
		// remove deleted tile row in dashboard row first
		List<EmsDashboardTile> edtList = ed.getDashboardTileList();
		if (edtList != null) {
			int edtSize = edtList.size();
			for (int i = edtSize - 1; i >= 0; i--) {
				EmsDashboardTile edt = edtList.get(i);
				boolean isDeleted = true;
				for (Tile tile : tiles) {
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

		if (tiles == null) {
			return;
		}
		for (int i = 0; i < tiles.size(); i++) {
			Tile tile = tiles.get(i);
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
			edt.setPosition(i);
		}
	}
}
