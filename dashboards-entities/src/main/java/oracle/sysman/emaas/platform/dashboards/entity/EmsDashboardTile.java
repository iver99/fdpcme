package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.AdditionalCriteria;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.QueryRedirectors;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import oracle.sysman.emaas.platform.dashboards.entity.customizer.EmsDashboardTileRedirector;

@Entity
@NamedQueries({ @NamedQuery(name = "EmsDashboardTile.findAll", query = "select o from EmsDashboardTile o") })
@Table(name = "EMS_DASHBOARD_TILE")
//@SequenceGenerator(name = "EmsDashboardTile_Id_Seq_Gen", sequenceName = "EMS_DASHBOARD_TILE_SEQ", allocationSize = 1)
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", length = 32, primaryKey = true)
@AdditionalCriteria("this.deleted = '0'")
@QueryRedirectors(delete = EmsDashboardTileRedirector.class)
public class EmsDashboardTile implements Serializable
{
	private static final long serialVersionUID = 6307069723661684517L;

	@Id
	@Column(name = "TILE_ID", nullable = false)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EmsDashboardTile_Id_Seq_Gen")
	private BigInteger tileId;
	@Column(nullable = false, length = 64)
	private String title;
	@Column(name = "TILE_ROW")
	private Integer row;
	@Column(name = "TILE_COLUMN")
	private Integer column;
	@Column
	private Integer height;
	@Column
	private Integer width;
	@Column(name = "TYPE")
	private Integer type;
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
	@Column(name = "WIDGET_CREATION_TIME", nullable = false, length = 32)
	private String widgetCreationTime;
	@Column(name = "WIDGET_DESCRIPTION", length = 256)
	private String widgetDescription;
	@Column(name = "WIDGET_GROUP_NAME", length = 64)
	private String widgetGroupName;

	@Column(name = "WIDGET_HISTOGRAM", length = 1024)
	private String widgetHistogram;

	@Column(name = "WIDGET_ICON", length = 1024)
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
	@Column(name = "WIDGET_SUPPORT_TIME_CONTROL", nullable = false)
	private Integer widgetSupportTimeControl;
	@Column(name = "WIDGET_LINKED_DASHBOARD")
	private BigInteger widgetLinkedDashboard;
	@Column(name = "DELETED", nullable = false, length = 1)
	private Boolean deleted;
	@ManyToOne
	@JoinColumns(value = { @JoinColumn(name = "DASHBOARD_ID", referencedColumnName = "DASHBOARD_ID"),
			@JoinColumn(name = "TENANT_ID", referencedColumnName = "TENANT_ID", insertable = false, updatable = false) })
	private EmsDashboard dashboard;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dashboardTile", orphanRemoval = true)
	private List<EmsDashboardTileParams> dashboardTileParamsList;

	public EmsDashboardTile()
	{
		deleted = Boolean.FALSE;
	}

	public EmsDashboardTile(Date creationDate, EmsDashboard emsDashboard1, Integer type, Integer row, Integer column,
			Integer height, Integer isMaximized, Date lastModificationDate, String lastModifiedBy,
			String owner, /*Integer position, */
			String providerAssetRoot, String providerName, String providerVersion, BigInteger tileId, String title,
			String widgetCreationTime, String widgetDescription, String widgetGroupName, String widgetHistogram,
			String widgetIcon, String widgetKocName, String widgetName, String widgetOwner, Integer widgetSource,
			String widgetTemplate, String widgetUniqueId, String widgetViewmode, Integer widgetSupportTimeControl, Integer width,
			BigInteger widgetLinkedDashboard)
	{
		this();
		this.creationDate = creationDate;
		dashboard = emsDashboard1;
		this.type = type;
		this.row = row;
		this.column = column;
		this.height = height;
		this.isMaximized = isMaximized;
		this.lastModificationDate = lastModificationDate;
		this.lastModifiedBy = lastModifiedBy;
		this.owner = owner;
		position = 0;
		this.providerAssetRoot = providerAssetRoot;
		this.providerName = providerName;
		this.providerVersion = providerVersion;
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
		this.widgetSupportTimeControl = widgetSupportTimeControl;
		this.widgetLinkedDashboard = widgetLinkedDashboard;
		this.width = width;
	}

	public EmsDashboardTileParams addEmsDashboardTileParams(EmsDashboardTileParams emsDashboardTileParams)
	{
		if (dashboardTileParamsList == null) {
			dashboardTileParamsList = new ArrayList<EmsDashboardTileParams>();
		}
		dashboardTileParamsList.add(emsDashboardTileParams);
		emsDashboardTileParams.setDashboardTile(this);
		return emsDashboardTileParams;
	}

	/**
	 * @return the column
	 */
	public Integer getColumn()
	{
		return column;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public EmsDashboard getDashboard()
	{
		return dashboard;
	}

	public List<EmsDashboardTileParams> getDashboardTileParamsList()
	{
		return dashboardTileParamsList;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted()
	{
		return deleted;
	}

	public Integer getHeight()
	{
		return height;
	}

	public Integer getIsMaximized()
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

	public Integer getPosition()
	{
		return position;
	}

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

	/**
	 * @return the row
	 */
	public Integer getRow()
	{
		return row;
	}

	public BigInteger getTileId()
	{
		return tileId;
	}

	public String getTitle()
	{
		return title;
	}

	/**
	 * @return the type
	 */
	public Integer getType()
	{
		return type;
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

	public BigInteger getWidgetLinkedDashboard()
	{
		return widgetLinkedDashboard;
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

	public Integer getWidgetSupportTimeControl()
	{
		return widgetSupportTimeControl;
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

	public EmsDashboardTileParams removeEmsDashboardTileParams(EmsDashboardTileParams emsDashboardTileParams)
	{
		getDashboardTileParamsList().remove(emsDashboardTileParams);
		emsDashboardTileParams.setDashboardTile(null);
		return emsDashboardTileParams;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(Integer column)
	{
		this.column = column;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public void setDashboard(EmsDashboard emsDashboard1)
	{
		dashboard = emsDashboard1;
	}

	public void setDashboardTileParamsList(List<EmsDashboardTileParams> emsDashboardTileParamsList)
	{
		dashboardTileParamsList = emsDashboardTileParamsList;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public void setIsMaximized(Integer isMaximized)
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

	public void setPosition(Integer position)
	{
		//			this.position = position;
		this.position = 0;
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

	/**
	 * @param row
	 *            the row to set
	 */
	public void setRow(Integer row)
	{
		this.row = row;
	}

	public void setTileId(BigInteger tileId)
	{
		this.tileId = tileId;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Integer type)
	{
		this.type = type;
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

	public void setWidgetLinkedDashboard(BigInteger widgetLinkedDashboard)
	{
		this.widgetLinkedDashboard = widgetLinkedDashboard;
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

	public void setWidgetSupportTimeControl(Integer widgetSupportTimeControl)
	{
		this.widgetSupportTimeControl = widgetSupportTimeControl;
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
}
