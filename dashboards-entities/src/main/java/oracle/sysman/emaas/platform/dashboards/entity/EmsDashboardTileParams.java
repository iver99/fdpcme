package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@NamedQueries({ @NamedQuery(name = "EmsDashboardTileParams.findAll", query = "select o from EmsDashboardTileParams o") })
@Table(name = "EMS_DASHBOARD_TILE_PARAMS")
@IdClass(EmsDashboardTileParamsPK.class)
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", length = 32, primaryKey = true)
public class EmsDashboardTileParams extends EmBaseEntity implements Serializable
{
	private static final long serialVersionUID = 4988046039963971713L;

	@Column(name = "IS_SYSTEM", nullable = false)
	private Integer isSystem;

	@Id
	@Column(name = "PARAM_NAME", nullable = false, length = 64)
	private String paramName;
	@Column(name = "PARAM_TYPE", nullable = false)
	private Integer paramType;
	@Column(name = "PARAM_VALUE_NUM")
	private Integer paramValueNum;
	@Column(name = "PARAM_VALUE_STR", length = 1024)
	private String paramValueStr;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PARAM_VALUE_TIMESTAMP")
	private Date paramValueTimestamp;
	@Column(name = "DELETED", nullable = false, length = 1)
	private Boolean deleted;
	@ManyToOne
	@Id
	@JoinColumns(value = { @JoinColumn(name = "TILE_ID", referencedColumnName = "TILE_ID"),
			@JoinColumn(name = "TENANT_ID", referencedColumnName = "TENANT_ID") })
	private EmsDashboardTile dashboardTile;

	public EmsDashboardTileParams()
	{
		deleted = Boolean.FALSE;
	}

	public EmsDashboardTileParams(Integer isSystem, String paramName, Integer paramType, Integer paramValueNum,
			String paramValueStr, Date paramValueTimestamp, EmsDashboardTile emsDashboardTile)
	{
		this();
		this.isSystem = isSystem;
		this.paramName = paramName;
		this.paramType = paramType;
		this.paramValueNum = paramValueNum;
		this.paramValueStr = paramValueStr;
		this.paramValueTimestamp = paramValueTimestamp;
		dashboardTile = emsDashboardTile;
	}

	public EmsDashboardTile getDashboardTile()
	{
		return dashboardTile;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted()
	{
		return deleted;
	}

	public Integer getIsSystem()
	{
		return isSystem;
	}

	public String getParamName()
	{
		return paramName;
	}

	public Integer getParamType()
	{
		return paramType;
	}

	public Integer getParamValueNum()
	{
		return paramValueNum;
	}

	public String getParamValueStr()
	{
		return paramValueStr;
	}

	public Date getParamValueTimestamp()
	{
		return paramValueTimestamp;
	}

	public void setDashboardTile(EmsDashboardTile emsDashboardTile)
	{
		dashboardTile = emsDashboardTile;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}

	public void setIsSystem(Integer isSystem)
	{
		this.isSystem = isSystem;
	}

	public void setParamName(String paramName)
	{
		this.paramName = paramName;
	}

	public void setParamType(Integer paramType)
	{
		this.paramType = paramType;
	}

	public void setParamValueNum(Integer paramValueNum)
	{
		this.paramValueNum = paramValueNum;
	}

	public void setParamValueStr(String paramValueStr)
	{
		this.paramValueStr = paramValueStr;
	}

	public void setParamValueTimestamp(Date paramValueTimestamp)
	{
		this.paramValueTimestamp = paramValueTimestamp;
	}
}
