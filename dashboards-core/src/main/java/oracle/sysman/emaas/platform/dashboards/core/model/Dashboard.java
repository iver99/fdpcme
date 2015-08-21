package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Dashboard
{
	public static final String DASHBOARD_TYPE_NORMAL = "NORMAL";
	public static final Integer DASHBOARD_TYPE_CODE_NORMAL = Integer.valueOf(0);
	public static final String DASHBOARD_TYPE_SINGLEPAGE = "SINGLEPAGE";
	public static final Integer DASHBOARD_TYPE_CODE_SINGLEPAGE = Integer.valueOf(1);
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

	private String screenShot;

	private String screenShotHref;

	private String href;

	private String type;

	private DashboardApplicationType appicationType;

	@JsonProperty("tiles")
	private List<Tile> tileList;

	public Dashboard()
	{
		// defaults for non-null values
		type = Dashboard.DASHBOARD_TYPE_NORMAL;
		enableTimeRange = Dashboard.DASHBOARD_ENABLE_TIME_RANGE_DEFAULT;
		deleted = DASHBOARD_DELETED_DEFAULT;
		isSystem = Boolean.FALSE;
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

	public DashboardApplicationType getAppicationType()
	{
		return appicationType;
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

	public EmsDashboard getPersistenceEntity(EmsDashboard ed) throws DashboardException
	{
		//check dashboard name
		if (name == null || name.trim() == "" || name.length() > 64) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_NAME_ERROR));
		}
		//check dashboard description
		if (description != null && description.length() > 256) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_DESCRIPTION_ERROR));
		}
		Integer isEnableTimeRange = DataFormatUtils.boolean2Integer(enableTimeRange);
		Integer isIsSystem = DataFormatUtils.boolean2Integer(isSystem);
		Integer dashboardType = DataFormatUtils.dashboardTypeString2Integer(type);
		Integer appType = appicationType == null ? null : appicationType.getValue();
		if (ed == null) {
			ed = new EmsDashboard(creationDate, dashboardId, 0L, description, isEnableTimeRange, isIsSystem,
					lastModificationDate, lastModifiedBy, name, owner, screenShot, dashboardType, appType);
			if (tileList != null) {
				int i = 0;
				for (Tile tile : tileList) {
					EmsDashboardTile edt = tile.getPersistenceEntity(null);
					//					edt.setPosition(i++);
					ed.addEmsDashboardTile(edt);
				}
			}
		}
		else {
			ed.getScreenShot();
			ed.setDeleted(deleted ? getDashboardId() : 0);
			ed.setDescription(description);
			ed.setEnableTimeRange(isEnableTimeRange);
			if (ed.getIsSystem() != null && isIsSystem != null && !isIsSystem.equals(ed.getIsSystem())) {
				throw new CommonResourceException(
						MessageUtils.getDefaultBundleString(CommonResourceException.NOT_SUPPORT_UPDATE_IS_SYSTEM_FIELD));
			}
			ed.setLastModificationDate(lastModificationDate);
			ed.setLastModifiedBy(lastModifiedBy);
			ed.setName(name);
			ed.setScreenShot(screenShot);
			ed.setApplicationType(appType);
			if (ed.getType() != null && dashboardType != null && !dashboardType.equals(ed.getType())) {
				throw new CommonResourceException(
						MessageUtils.getDefaultBundleString(CommonResourceException.NOT_SUPPORT_UPDATE_TYPE_FIELD));
			}
			updateEmsDashboardTiles(tileList, ed);
		}
		return ed;
	}

	@JsonIgnore
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

	public void setAppicationType(DashboardApplicationType appicationType)
	{
		this.appicationType = appicationType;
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

	@JsonProperty("screenShot")
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

	private void updateEmsDashboardTiles(List<Tile> tiles, EmsDashboard ed) throws DashboardException
	{
		Map<Tile, EmsDashboardTile> rows = new HashMap<Tile, EmsDashboardTile>();
		// remove deleted tile row in dashboard row first
		List<EmsDashboardTile> edtList = ed.getDashboardTileList();
		if (edtList != null) {
			int edtSize = edtList.size();
			for (int i = edtSize - 1; i >= 0; i--) {
				EmsDashboardTile edt = edtList.get(i);
				boolean isDeleted = true;
				if (tiles != null) {
					for (Tile tile : tiles) {
						if (tile.getTileId() != null && tile.getTileId().equals(edt.getTileId())) {
							isDeleted = false;
							rows.put(tile, edt);
							break;
						}
					}
				}
				if (isDeleted) {
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
				if (tile.getTileId() != null) {
					throw new CommonFunctionalException(MessageUtils.getDefaultBundleString(
							CommonFunctionalException.DASHBOARD_TILE_INVALID_ID, String.valueOf(tile.getTileId())));
				}
				edt = tile.getPersistenceEntity(null);
				ed.addEmsDashboardTile(edt);
				rows.put(tile, edt);
				//				dsf.persistEntity(edt);
			}
			else {
				edt = rows.get(tile);
				tile.getPersistenceEntity(edt);
			}
			//			edt.setPosition(i);
		}
	}
}
