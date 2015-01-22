package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Tile
{
	public static final int WIDGET_SOURCE_DASHBOARD_FRAMEWORK = 0;
	public static final int WIDGET_SOURCE_INTEGRATOR = 1;
	public static final Boolean TILE_DEFAULT_IS_MAX = false;
	public static final Integer TILE_DEFAULT_WIDTH = 2;
	public static final Integer TILE_DEFAULT_HEIGHT = 220;

	public static Tile valueOf(EmsDashboardTile edt)
	{
		if (edt == null) {
			return null;
		}
		Tile tile = new Tile();
		tile.setCreationDate(edt.getCreationDate());
		tile.setHeight(edt.getHeight());
		tile.setIsMaximized(DataFormatUtils.integer2Boolean(edt.getIsMaximized()));
		tile.setLastModificationDate(edt.getLastModificationDate());
		tile.setLastModifiedBy(edt.getLastModifiedBy());
		tile.setOwner(edt.getOwner());
		//    	tile.setPosition(edt.getPosition());
		tile.setProviderAssetRoot(edt.getProviderAssetRoot());
		tile.setProviderName(edt.getProviderName());
		tile.setProviderVersion(edt.getProviderVersion());
		tile.setTileId(edt.getTileId());
		tile.setTitle(edt.getTitle());
		tile.setWidgetCreationTime(edt.getWidgetCreationTime());
		tile.setWidgetDescription(edt.getWidgetDescription());
		tile.setWidgetGroupName(edt.getWidgetGroupName());
		tile.setWidgetHistogram(edt.getWidgetHistogram());
		tile.setWidgetIcon(edt.getWidgetIcon());
		tile.setWidgetKocName(edt.getWidgetKocName());
		tile.setWidgetName(edt.getWidgetName());
		tile.setWidgetOwner(edt.getWidgetOwner());
		tile.setWidgetSource(edt.getWidgetSource());
		tile.setWidgetTemplate(edt.getWidgetTemplate());
		tile.setWidgetUniqueId(edt.getWidgetUniqueId());
		tile.setWidgetViewmode(edt.getWidgetViewmode());
		tile.setWidth(edt.getWidth());
		List<EmsDashboardTileParams> edtpList = edt.getDashboardTileParamsList();
		if (edtpList != null) {
			List<TileParam> paras = new ArrayList<TileParam>(edtpList.size());
			for (EmsDashboardTileParams edtp : edtpList) {
				paras.add(TileParam.valueOf(edtp));
			}
			tile.setParameters(paras);
		}
		return tile;
	}

	@JsonIgnore
	private Date creationDate;

	private Integer height;

	private Boolean isMaximized;

	@JsonIgnore
	private Date lastModificationDate;

	@JsonIgnore
	private String lastModifiedBy;

	@JsonIgnore
	private String owner;
	//	private int position;
	@JsonProperty("PROVIDER_ASSET_ROOT")
	private String providerAssetRoot;

	@JsonProperty("PROVIDER_NAME")
	private String providerName;

	@JsonProperty("PROVIDER_VERSION")
	private String providerVersion;

	private Long tileId;

	private String title;

	@JsonProperty("WIDGET_CREATION_TIME")
	private String widgetCreationTime;

	@JsonProperty("WIDGET_DESCRIPTION")
	private String widgetDescription;

	@JsonProperty("WIDGET_GROUP_NAME")
	private String widgetGroupName;

	@JsonProperty("WIDGET_HISTOGRAM")
	private String widgetHistogram;

	@JsonProperty("WIDGET_ICON")
	private String widgetIcon;

	@JsonProperty("WIDGET_KOC_NAME")
	private String widgetKocName;

	@JsonProperty("WIDGET_NAME")
	private String widgetName;

	@JsonProperty("WIDGET_OWNER")
	private String widgetOwner;

	@JsonProperty("WIDGET_SOURCE")
	private Integer widgetSource;

	@JsonProperty("WIDGET_TEMPLATE")
	private String widgetTemplate;

	@JsonProperty("WIDGET_UNIQUE_ID")
	private String widgetUniqueId;

	@JsonProperty("WIDGET_VIEWMODEL")
	private String widgetViewmode;

	private Integer width;

	@JsonIgnore
	private Dashboard dashboard;

	@JsonProperty("tileParameters")
	private List<TileParam> parameters;

	public Tile()
	{
		// defaults for non-null values
		isMaximized = Boolean.FALSE;
	}

	public TileParam addParameter(TileParam tp)
	{
		if (tp == null) {
			return null;
		}
		if (parameters == null) {
			parameters = new ArrayList<TileParam>();
		}
		parameters.add(tp);
		tp.setTile(this);
		return tp;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public Dashboard getDashboard()
	{
		return dashboard;
	}

	public Integer getHeight()
	{
		return height;
	}

	public Boolean getIsMaximized()
	{
		return isMaximized;
	}

	public Date getLastModificationDate()
	{
		return lastModificationDate;
	}

	public String getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	public String getOwner()
	{
		return owner;
	}

	public TileParam getParameter(String name)
	{
		if (name == null || "".equals(name) || parameters == null) {
			return null;
		}
		for (TileParam tp : parameters) {
			if (name.equals(tp.getName())) {
				return tp;
			}
		}
		return null;
	}

	public List<TileParam> getParameters()
	{
		return parameters;
	}

	/**
	 * Returns an instance of EmsDashboardTile for current tile object
	 *
	 * @return
	 * @throws CommonFunctionalException
	 */
	public EmsDashboardTile getPersistenceEntity(EmsDashboardTile to) throws DashboardException
	{
		Integer intIsMaximized = DataFormatUtils.boolean2Integer(isMaximized);

		if (title == null || "".equals(title)) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_TILE_TITLE_REQUIRED));
		}
		if (width == null) {
			width = TILE_DEFAULT_WIDTH;
		}
		if (height == null) {
			height = TILE_DEFAULT_HEIGHT;
		}
		if (isMaximized == null) {
			isMaximized = TILE_DEFAULT_IS_MAX;
		}
		if (to == null) { // newly created tile
			if (widgetName == null || "".equals(widgetName)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_NAME_REQUIRED));
			}
			if (widgetUniqueId == null || "".equals(widgetUniqueId)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_UNIQUE_ID_REQUIRED));
			}
			if (widgetIcon == null || "".equals(widgetIcon)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_ICON_REQUIRED));
			}
			if (widgetHistogram == null || "".equals(widgetHistogram)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_HISTOGRAM_REQUIRED));
			}
			if (widgetOwner == null || "".equals(widgetOwner)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_OWNER_REQUIRED));
			}
			if (widgetCreationTime == null || "".equals(widgetCreationTime)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_CREATIONTIME_REQUIRED));
			}
			if (widgetSource == null || "".equals(widgetSource)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_SOURCE_REQUIRED));
			}
			if (widgetKocName == null || "".equals(widgetKocName)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_KOC_NAME_REQUIRED));
			}
			if (widgetViewmode == null || "".equals(widgetViewmode)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_VIEW_MODEL_REQUIRED));
			}
			if (widgetTemplate == null || "".equals(widgetTemplate)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.WIDGET_TEMPLATE_REQUIRED));
			}
			if (providerName == null || "".equals(providerName)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.PROVIDER_NAME_REQUIRED));
			}
			if (providerVersion == null || "".equals(providerVersion)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.PROVIDER_VERSION_REQUIRED));
			}
			if (providerAssetRoot == null || "".equals(providerAssetRoot)) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.PROVIDER_ASSET_ROOT_REQUIRED));
			}
			to = new EmsDashboardTile(creationDate, null, height, intIsMaximized, lastModificationDate, lastModifiedBy, owner, 0,
					providerAssetRoot, providerName, providerVersion, tileId, title, widgetCreationTime, widgetDescription,
					widgetGroupName, widgetHistogram, widgetIcon, widgetKocName, widgetName, widgetOwner, widgetSource,
					widgetTemplate, widgetUniqueId, widgetViewmode, width);
			if (parameters != null) {
				for (TileParam param : parameters) {
					EmsDashboardTileParams edtp = param.getPersistentEntity(null);
					to.addEmsDashboardTileParams(edtp);
				}
			}
		}
		else {
			to.setHeight(getHeight());
			to.setIsMaximized(intIsMaximized);
			//    		edt.setPosition(this.position);
			to.setProviderAssetRoot(providerAssetRoot);
			to.setProviderName(providerName);
			to.setProviderVersion(providerVersion);
			to.setTitle(title);
			to.setWidgetCreationTime(widgetCreationTime);
			to.setWidgetDescription(widgetDescription);
			to.setWidgetGroupName(widgetGroupName);
			to.setWidgetHistogram(widgetHistogram);
			to.setWidgetIcon(widgetIcon);
			to.setWidgetKocName(widgetKocName);
			to.setWidgetName(widgetName);
			to.setWidgetOwner(widgetOwner);
			to.setWidgetSource(intIsMaximized);
			to.setWidgetTemplate(widgetTemplate);
			to.setWidgetUniqueId(widgetUniqueId);
			to.setWidgetViewmode(widgetViewmode);
			to.setWidth(intIsMaximized);
			updateEmsDashboardTileParams(parameters, to);
		}
		return to;
	}

	//    public Integer getPosition() {
	//        return position;
	//    }
	//
	//    public void setPosition(Integer position) {
	//        this.position = position;
	//    }

	public String getProviderAssetRoot()
	{
		return providerAssetRoot;
	}

	public String getProviderName()
	{
		return providerName;
	}

	public String getProviderVersion()
	{
		return providerVersion;
	}

	public Long getTileId()
	{
		return tileId;
	}

	public String getTitle()
	{
		return title;
	}

	public String getWidgetCreationTime()
	{
		return widgetCreationTime;
	}

	public String getWidgetDescription()
	{
		return widgetDescription;
	}

	public String getWidgetGroupName()
	{
		return widgetGroupName;
	}

	public String getWidgetHistogram()
	{
		return widgetHistogram;
	}

	public String getWidgetIcon()
	{
		return widgetIcon;
	}

	public String getWidgetKocName()
	{
		return widgetKocName;
	}

	public String getWidgetName()
	{
		return widgetName;
	}

	public String getWidgetOwner()
	{
		return widgetOwner;
	}

	public Integer getWidgetSource()
	{
		return widgetSource;
	}

	public String getWidgetTemplate()
	{
		return widgetTemplate;
	}

	public String getWidgetUniqueId()
	{
		return widgetUniqueId;
	}

	public String getWidgetViewmode()
	{
		return widgetViewmode;
	}

	public Integer getWidth()
	{
		return width;
	}

	/**
	 * Removes a tile parameter specified by the name
	 *
	 * @param index
	 * @return
	 */
	public TileParam removeParameter(String name)
	{
		TileParam tp = getParameter(name);
		if (tp == null) {
			return null;
		}
		parameters.remove(name);
		return tp;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public void setDashboard(Dashboard emsDashboard1)
	{
		dashboard = emsDashboard1;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public void setIsMaximized(Boolean isMaximized)
	{
		this.isMaximized = isMaximized;
	}

	public void setLastModificationDate(Date lastModificationDate)
	{
		this.lastModificationDate = lastModificationDate;
	}

	public void setLastModifiedBy(String lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public void setParameters(List<TileParam> parameters)
	{
		this.parameters = parameters;
	}

	public void setProviderAssetRoot(String providerAssetRoot)
	{
		this.providerAssetRoot = providerAssetRoot;
	}

	public void setProviderName(String providerName)
	{
		this.providerName = providerName;
	}

	public void setProviderVersion(String providerVersion)
	{
		this.providerVersion = providerVersion;
	}

	public void setTileId(Long tileId)
	{
		this.tileId = tileId;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setWidgetCreationTime(String widgetCreationTime)
	{
		this.widgetCreationTime = widgetCreationTime;
	}

	public void setWidgetDescription(String widgetDescription)
	{
		this.widgetDescription = widgetDescription;
	}

	public void setWidgetGroupName(String widgetGroupName)
	{
		this.widgetGroupName = widgetGroupName;
	}

	public void setWidgetHistogram(String widgetHistogram)
	{
		this.widgetHistogram = widgetHistogram;
	}

	public void setWidgetIcon(String widgetIcon)
	{
		this.widgetIcon = widgetIcon;
	}

	public void setWidgetKocName(String widgetKocName)
	{
		this.widgetKocName = widgetKocName;
	}

	public void setWidgetName(String widgetName)
	{
		this.widgetName = widgetName;
	}

	public void setWidgetOwner(String widgetOwner)
	{
		this.widgetOwner = widgetOwner;
	}

	public void setWidgetSource(Integer widgetSource)
	{
		this.widgetSource = widgetSource;
	}

	public void setWidgetTemplate(String widgetTemplate)
	{
		this.widgetTemplate = widgetTemplate;
	}

	public void setWidgetUniqueId(String widgetUniqueId)
	{
		this.widgetUniqueId = widgetUniqueId;
	}

	public void setWidgetViewmode(String widgetViewmode)
	{
		this.widgetViewmode = widgetViewmode;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}

	private void updateEmsDashboardTileParams(List<TileParam> paramList, EmsDashboardTile tile) throws CommonFunctionalException
	{
		Map<TileParam, EmsDashboardTileParams> rows = new HashMap<TileParam, EmsDashboardTileParams>();
		List<EmsDashboardTileParams> edtpList = tile.getDashboardTileParamsList();
		if (edtpList != null) {
			int edtSize = edtpList.size();
			for (int i = edtSize - 1; i >= 0; i--) {
				EmsDashboardTileParams edtp = edtpList.get(i);
				boolean isDeleted = true;
				if (paramList != null) {
					for (TileParam param : paramList) {
						if (param.getName() != null && param.getName().equals(edtp.getParamName()) && param.getTile() != null
								&& param.getTile().getTileId() != null
								&& param.getTile().getTileId().equals(edtp.getDashboardTile().getTileId())) {
							isDeleted = false;
							rows.put(param, edtp);
							break;
						}
					}
				}
				if (isDeleted) {
					tile.getDashboardTileParamsList().remove(edtp);
				}
			}
		}

		if (paramList == null) {
			return;
		}
		for (TileParam tp : paramList) {
			EmsDashboardTileParams edtp = null;
			if (!rows.containsKey(tp)) {
				edtp = tp.getPersistentEntity(null);
				tile.addEmsDashboardTileParams(edtp);
				rows.put(tp, edtp);
			}
			else {
				edtp = rows.get(tile);
				edtp = tp.getPersistentEntity(edtp);
			}
		}
	}
}
