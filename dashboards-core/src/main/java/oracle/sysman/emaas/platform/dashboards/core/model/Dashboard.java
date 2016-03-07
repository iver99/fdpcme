package oracle.sysman.emaas.platform.dashboards.core.model;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.CommonResourceException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsSubDashboard;
import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

import java.util.*;

public class Dashboard
{
	public static enum EnableTimeRangeState
	{
		FALSE("FALSE", 0), TRUE("TRUE", 1), AUTO("AUTO", 2);

		@JsonCreator
		public static EnableTimeRangeState fromName(String name)
		{
			if (name == null) {
				return null;
			}
			for (EnableTimeRangeState etrs : EnableTimeRangeState.values()) {
				if (etrs.getName().toLowerCase().equals(name.toLowerCase())) {
					return etrs;
				}
			}
			return null;
		}

		public static EnableTimeRangeState fromValue(Integer value)
		{
			for (EnableTimeRangeState etrs : EnableTimeRangeState.values()) {
				if (etrs.getValue().equals(value)) {
					return etrs;
				}
			}
			return null;
		}

		private String name;

		@JsonIgnore
		private Integer value;

		private EnableTimeRangeState(String name, Integer value)
		{
			this.name = name;
			this.value = value;
		}

		@JsonValue
		public String getName()
		{
			return name;
		}

		public Integer getValue()
		{
			return value;
		}
	}

	public static final String DASHBOARD_TYPE_NORMAL = "NORMAL";
	public static final Integer DASHBOARD_TYPE_CODE_NORMAL = Integer.valueOf(0);
	public static final String DASHBOARD_TYPE_SINGLEPAGE = "SINGLEPAGE";
	public static final Integer DASHBOARD_TYPE_CODE_SINGLEPAGE = Integer.valueOf(1);
	public static final String DASHBOARD_TYPE_SET = "SET";
	public static final Integer DASHBOARD_TYPE_CODE_SET = Integer.valueOf(2);

	public static final EnableTimeRangeState DASHBOARD_ENABLE_TIME_RANGE_DEFAULT = EnableTimeRangeState.AUTO;
	public static final boolean DASHBOARD_ENABLE_REFRESH_DEFAULT = Boolean.FALSE;

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
		to.setEnableRefresh(DataFormatUtils.integer2Boolean(from.getEnableRefresh()));
		to.setIsSystem(DataFormatUtils.integer2Boolean(from.getIsSystem()));
		to.setSharePublic(DataFormatUtils.integer2Boolean(from.getSharePublic()));
		to.setLastModificationDate(from.getLastModificationDate());
		to.setLastModifiedBy(from.getLastModifiedBy());
		to.setName(from.getName());
		to.setOwner(from.getOwner());
		// by default, we'll not load screenshot for query
		//		to.setScreenShot(from.getScreenShot());
		to.setType(DataFormatUtils.dashboardTypeInteger2String(from.getType()));
        if(from.getType().equals(DASHBOARD_TYPE_CODE_SET)) {
			to.setEnableTimeRange(null);
            to.setIsSystem(null);
            List<EmsSubDashboard> emsSubDashboards = from.getSubDashboardList();
			if (emsSubDashboards != null) {
				List<Dashboard> subDashboardList = new ArrayList<>();
                for (EmsSubDashboard esd : emsSubDashboards) {
                    Dashboard dbd = new Dashboard();
                    dbd.setEnableTimeRange(null);
                    dbd.setEnableRefresh(null);
                    dbd.setIsSystem(null);
                    dbd.setSharePublic(null);
                    dbd.setType(null);

                    Long subDashboardId = esd.getSubDashboardId();
                    Long tenantId = from.getTenantId();
                    DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
                    EmsDashboard ed = dsf.getEmsDashboardById(subDashboardId);

                    dbd.setDashboardId(ed.getDashboardId());
                    dbd.setName(ed.getName());

                    // // TODO: 2016/3/7
                    // updateDashboardHref(dbd, tenantName);
                    subDashboardList.add(dbd);
                }
                to.setSubDashboards(subDashboardList);
			}
        }else{
            to.setEnableTimeRange(EnableTimeRangeState.fromValue(from.getEnableTimeRange()));

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

	private EnableTimeRangeState enableTimeRange;

	private Boolean enableRefresh;

	@JsonProperty("systemDashboard")
	private Boolean isSystem;

	private Boolean sharePublic;

	@JsonProperty("lastModifiedOn")
	private Date lastModificationDate;

	private String lastModifiedBy;

	private String owner;

	private String screenShot;

	private String screenShotHref;

	private String optionsHref;

	private String href;

	private String type;

	private DashboardApplicationType appicationType;

	@JsonProperty("tiles")
	private List<Tile> tileList;

	@JsonProperty("subDashboards")
	private List<Dashboard> subDashboards;

	public List<Dashboard> getSubDashboards() {
		return subDashboards;
	}

    public void setSubDashboards(List<Dashboard> subDashboards) {
		this.subDashboards = subDashboards;
	}

	public Dashboard()
	{
		// defaults for non-null values
		type = Dashboard.DASHBOARD_TYPE_NORMAL;
		enableTimeRange = Dashboard.DASHBOARD_ENABLE_TIME_RANGE_DEFAULT;
		enableRefresh = Dashboard.DASHBOARD_ENABLE_REFRESH_DEFAULT;
		deleted = DASHBOARD_DELETED_DEFAULT;
		isSystem = Boolean.FALSE;
		sharePublic = Boolean.FALSE;
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

	/**
	 * @return the enableRefresh
	 */
	public Boolean getEnableRefresh()
	{
		return enableRefresh;
	}

	public EnableTimeRangeState getEnableTimeRange()
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
		Integer isEnableTimeRange = enableTimeRange == null ? null : enableTimeRange.getValue();
		Integer isEnableRefresh = DataFormatUtils.boolean2Integer(enableRefresh);
		Integer isIsSystem = DataFormatUtils.boolean2Integer(isSystem);
		Integer isShare = DataFormatUtils.boolean2Integer(sharePublic);
		Integer dashboardType = DataFormatUtils.dashboardTypeString2Integer(type);
		Integer appType = appicationType == null ? null : appicationType.getValue();
		String htmlEcodedName = StringEscapeUtils.escapeHtml4(name);
		String htmlEcodedDesc = description == null ? null : StringEscapeUtils.escapeHtml4(description);

		if (ed == null) {
			ed = new EmsDashboard(creationDate, dashboardId, 0L, htmlEcodedDesc, isEnableTimeRange, isEnableRefresh, isIsSystem,
					isShare, lastModificationDate, lastModifiedBy, htmlEcodedName, owner, screenShot, dashboardType, appType);

            if (type.equals(Dashboard.DASHBOARD_TYPE_SET)){
                if (subDashboards != null) {
                    for (int index=0;index < subDashboards.size() ;index++ ) {
                        Dashboard dbd = subDashboards.get(index);
                        EmsSubDashboard esdbd = new EmsSubDashboard(dashboardId,dbd.getDashboardId(),index);
                        ed.addEmsSubDashboard(esdbd);
                    }
                }
            }else{
                if (tileList != null) {
                    for (Tile tile : tileList) {
                        EmsDashboardTile edt = tile.getPersistenceEntity(null);
                        //					edt.setPosition(i++);
                        ed.addEmsDashboardTile(edt);
                    }
                }
            }
		}
		else {
			ed.getScreenShot();
			ed.setDeleted(deleted ? getDashboardId() : 0);
			ed.setDescription(htmlEcodedDesc);
			ed.setEnableTimeRange(isEnableTimeRange);
			ed.setEnableRefresh(isEnableRefresh);
			if (ed.getIsSystem() != null && isIsSystem != null && !isIsSystem.equals(ed.getIsSystem())) {
				throw new CommonResourceException(
						MessageUtils.getDefaultBundleString(CommonResourceException.NOT_SUPPORT_UPDATE_IS_SYSTEM_FIELD));
			}
			ed.setLastModificationDate(lastModificationDate);
			ed.setLastModifiedBy(lastModifiedBy);
			ed.setName(htmlEcodedName);
			ed.setScreenShot(screenShot);
			ed.setApplicationType(appType);
			ed.setSharePublic(isShare);
			if (ed.getType() != null && dashboardType != null && !dashboardType.equals(ed.getType())) {
				throw new CommonResourceException(
						MessageUtils.getDefaultBundleString(CommonResourceException.NOT_SUPPORT_UPDATE_TYPE_FIELD));
			}
			updateEmsDashboardTiles(tileList, ed);
            updateEmsSubDashboards(subDashboards,ed);
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

	/**
	 * @return the sharePublic
	 */
	public Boolean getSharePublic()
	{
		return sharePublic;
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

	/**
	 * @param enableRefresh
	 *            the enableRefresh to set
	 */
	public void setEnableRefresh(Boolean enableRefresh)
	{
		this.enableRefresh = enableRefresh;
	}

	public void setEnableTimeRange(EnableTimeRangeState enableTimeRange)
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

	public String getOptionsHref() {
		return optionsHref;
	}

	public void setOptionsHref(String optionsHref) {
		this.optionsHref = optionsHref;
	}

	/**
	 * @param sharePublic
	 *            the sharePublic to set
	 */
	public void setSharePublic(Boolean sharePublic)
	{
		this.sharePublic = sharePublic;
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

    private void updateEmsSubDashboards(List<Dashboard> dashboards, EmsDashboard ed) throws DashboardException {
        if (dashboards == null) {
            throw new CommonSecurityException("sub dashboard is null");
        }

        Map<Dashboard, EmsSubDashboard> rows = new HashMap();
        List<EmsSubDashboard> subDashboardList = ed.getSubDashboardList();
        if (subDashboardList != null) {
            for (int i = subDashboardList.size() - 1; i >= 0; i--) {
                EmsSubDashboard emsSubDashboard = subDashboardList.get(i);
//                for (Dashboard dashboard : dashboards) {
//                    if (dashboard.getDashboardId() != null && dashboard.getDashboardId().equals(emsSubDashboard.getSubDashboardId())) {
//                        rows.put(dashboard, emsSubDashboard);
//                    }
//                }
                ed.removeEmsSubDashboard(emsSubDashboard);
            }
        }

        for (int index = 0; index < dashboards.size(); index++) {
            Dashboard subDashboard = dashboards.get(index);
            if(subDashboard.getDashboardId() != null) {
                EmsSubDashboard emsSubDashboard;
                if (!rows.containsKey(subDashboard)) {
                    emsSubDashboard = new EmsSubDashboard(dashboardId, subDashboard.getDashboardId(), index);
                    ed.addEmsSubDashboard(emsSubDashboard);
                    rows.put(subDashboard, emsSubDashboard);
                } else {
                    emsSubDashboard = rows.get(subDashboard);
                    ed.removeEmsSubDashboard(emsSubDashboard);
                    ed.addEmsSubDashboard(emsSubDashboard);
                    rows.put(subDashboard, emsSubDashboard);
                }
            }
        }
    }
}
