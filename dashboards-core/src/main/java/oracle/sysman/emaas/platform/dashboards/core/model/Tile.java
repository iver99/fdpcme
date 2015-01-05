package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

public class Tile
{
	public static final int WIDGET_SOURCE_DASHBOARD_FRAMEWORK = 0;
	public static final int WIDGET_SOURCE_INTEGRATOR = 1;

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

	private Date creationDate;
	private Integer height;
	private Boolean isMaximized;
	private Date lastModificationDate;
	private String lastModifiedBy;
	private String owner;
	//	private int position;
	private String providerAssetRoot;
	private String providerName;
	private String providerVersion;
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
	 */
	public EmsDashboardTile getPersistenceEntity(EmsDashboardTile edt)
	{
		Integer intIsMaximized = DataFormatUtils.boolean2Integer(isMaximized);

		if (edt == null) {
			edt = new EmsDashboardTile(creationDate, null, height, intIsMaximized, lastModificationDate, lastModifiedBy, owner,
					0, providerAssetRoot, providerName, providerVersion, tileId, title, widgetCreationTime, widgetDescription,
					widgetGroupName, widgetHistogram, widgetIcon, widgetKocName, widgetName, widgetOwner, widgetSource,
					widgetTemplate, widgetUniqueId, widgetViewmode, width);
			if (parameters != null) {
				for (TileParam param : parameters) {
					EmsDashboardTileParams edtp = param.getPersistentEntity(null);
					edt.addEmsDashboardTileParams(edtp);
				}
			}
		}
		else {
			edt.setHeight(getHeight());
			edt.setIsMaximized(intIsMaximized);
			//    		edt.setPosition(this.position);
			edt.setProviderAssetRoot(providerAssetRoot);
			edt.setProviderName(providerName);
			edt.setProviderVersion(providerVersion);
			edt.setTitle(title);
			edt.setWidgetCreationTime(widgetCreationTime);
			edt.setWidgetDescription(widgetDescription);
			edt.setWidgetGroupName(widgetGroupName);
			edt.setWidgetHistogram(widgetHistogram);
			edt.setWidgetIcon(widgetIcon);
			edt.setWidgetKocName(widgetKocName);
			edt.setWidgetName(widgetName);
			edt.setWidgetOwner(widgetOwner);
			edt.setWidgetSource(intIsMaximized);
			edt.setWidgetTemplate(widgetTemplate);
			edt.setWidgetUniqueId(widgetUniqueId);
			edt.setWidgetViewmode(widgetViewmode);
			edt.setWidth(intIsMaximized);
			updateEmsDashboardTileParams(parameters, edt);
		}
		return edt;
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

	private void updateEmsDashboardTileParams(List<TileParam> paramList, EmsDashboardTile tile)
	{
		 Map<TileParam, EmsDashboardTileParams> rows = new HashMap<TileParam, EmsDashboardTileParams>();
		 List<EmsDashboardTileParams> edtpList = tile.getDashboardTileParamsList();
		 if (edtpList != null) {
			 int edtSize = edtpList.size();
			 for (int i = edtSize - 1; i >= 0; i--) {
				 EmsDashboardTileParams edtp = edtpList.get(i);
				 boolean isDeleted = true;
				 for (TileParam param : paramList) {
					 if (param.getName() != null && param.getName().equals(edtp.getParamName()) && param.getTile() != null
							&& param.getTile().getTileId() != null
							&& param.getTile().getTileId().equals(edtp.getDashboardTile().getTileId())) {
						 isDeleted = false;
						 rows.put(param, edtp);
						 break;
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
