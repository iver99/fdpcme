package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@NamedQueries({ @NamedQuery(name = "EmsDashboard.findAll", query = "select o from EmsDashboard o where o.deleted=0") })
@Table(name = "EMS_DASHBOARD")
@SequenceGenerator(name = "EmsDashboard_Id_Seq_Gen", sequenceName = "EMS_DASHBOARD_SEQ", allocationSize = 1)
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", length = 32)
public class EmsDashboard implements Serializable
{
	private static final long serialVersionUID = 1219062974568988740L;

	@Id
	@Column(name = "DASHBOARD_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EmsDashboard_Id_Seq_Gen")
	private Long dashboardId;
	@Column(name = "DELETED")
	private Long deleted;
	@Column(name = "DESCRIPTION", length = 256)
	private String description;
	@Column(name = "ENABLE_TIME_RANGE", nullable = false)
	private Integer enableTimeRange;
	@Column(name = "IS_SYSTEM", nullable = false)
	private Integer isSystem;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION_DATE", nullable = false)
	private Date creationDate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFICATION_DATE")
	private Date lastModificationDate;
	@Column(name = "LAST_MODIFIED_BY", length = 128)
	private String lastModifiedBy;
	@Column(nullable = false, length = 64)
	private String name;
	@Column(nullable = false, length = 128)
	private String owner;
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "SCREEN_SHOT")
	@Lob
	private String screenShot;
	//@Column(name = "TENANT_ID", nullable = false, length = 32)
	//private String tenantId;
	@Column(nullable = false)
	private Integer type;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "dashboard", orphanRemoval = true)
	@OrderBy("position")
	private List<EmsDashboardTile> dashboardTileList;

	public EmsDashboard()
	{
	}

	public EmsDashboard(Date creationDate, Long dashboardId, Long deleted, String description, Integer enableTimeRange,
			Integer isSystem, Date lastModificationDate, String lastModifiedBy, String name, String owner, String screenShot,
			Integer type)
	{
		this.creationDate = creationDate;
		this.dashboardId = dashboardId;
		this.deleted = deleted;
		this.description = description;
		this.enableTimeRange = enableTimeRange;
		this.isSystem = isSystem;
		this.lastModificationDate = lastModificationDate;
		this.lastModifiedBy = lastModifiedBy;
		this.name = name;
		this.owner = owner;
		this.screenShot = screenShot;
		this.type = type;
	}

	public EmsDashboardTile addEmsDashboardTile(EmsDashboardTile emsDashboardTile)
	{
		if (dashboardTileList == null) {
			dashboardTileList = new ArrayList<EmsDashboardTile>();
		}
		dashboardTileList.add(emsDashboardTile);
		emsDashboardTile.setDashboard(this);
		return emsDashboardTile;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public Long getDashboardId()
	{
		return dashboardId;
	}

	public List<EmsDashboardTile> getDashboardTileList()
	{
		return dashboardTileList;
	}

	public Long getDeleted()
	{
		return deleted;
	}

	public String getDescription()
	{
		return description;
	}

	public Integer getEnableTimeRange()
	{
		return enableTimeRange;
	}

	public Integer getIsSystem()
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

	public String getScreenShot()
	{
		return screenShot;
	}

	public Integer getType()
	{
		return type;
	}

	public EmsDashboardTile removeEmsDashboardTile(EmsDashboardTile emsDashboardTile)
	{
		getDashboardTileList().remove(emsDashboardTile);
		emsDashboardTile.setDashboard(null);
		return emsDashboardTile;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public void setDashboardTileList(List<EmsDashboardTile> emsDashboardTileList)
	{
		dashboardTileList = emsDashboardTileList;
	}

	public void setDeleted(Long deleted)
	{
		this.deleted = deleted;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setEnableTimeRange(Integer enableTimeRange)
	{
		this.enableTimeRange = enableTimeRange;
	}

	public void setIsSystem(Integer isSystem)
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

	public void setType(Integer type)
	{
		this.type = type;
	}
}