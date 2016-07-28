package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;
import java.math.BigInteger;

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

import org.eclipse.persistence.annotations.AdditionalCriteria;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.QueryRedirectors;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

import oracle.sysman.emaas.platform.dashboards.entity.customizer.EmsSubDashboardRedirector;

/**
 * @author jishshi
 * @since 2016/3/3.
 */

@Entity
@NamedQueries({
		@NamedQuery(name = "EmsSubDashboard.removeBySubDashboardID", query = "delete from EmsSubDashboard o where o.subDashboardId = :p"),
		@NamedQuery(name = "EmsSubDashboard.removeByDashboardSetID", query = "delete from EmsSubDashboard o where o.dashboardSetId = :p"),
		@NamedQuery(name = "EmsSubDashboard.removeUnshared", query = ""
				+ "delete from EmsSubDashboard b where b.dashboardSetId in (" + "select a.dashboardId from EmsDashboard a "
				+ "where a.dashboardId = b.dashboardSetId " + "and b.subDashboardId = :p1 " + "and a.owner != :p2" + ")"), })
@Table(name = "EMS_DASHBOARD_SET")
@IdClass(EmsDashboardSetPK.class)
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", length = 32, primaryKey = true)
@AdditionalCriteria("this.deleted = '0'")
@QueryRedirectors(insert = EmsSubDashboardRedirector.class, delete = EmsSubDashboardRedirector.class)
public class EmsSubDashboard extends EmBaseEntity implements Serializable
{

	private static final long serialVersionUID = 8344138185588082239L;

	@Id
	@Column(name = "DASHBOARD_SET_ID", nullable = false, length = 256)
	private BigInteger dashboardSetId;

	@Id
	@Column(name = "SUB_DASHBOARD_ID", nullable = false, length = 256)
	private BigInteger subDashboardId;

	@Column(name = "POSITION", nullable = false)
	private Integer position;

	@Column(name = "DELETED", nullable = false, length = 1)
	private Boolean deleted;

	@ManyToOne
	@JoinColumns(value = {
			@JoinColumn(name = "DASHBOARD_SET_ID", referencedColumnName = "DASHBOARD_ID", insertable = false, updatable = false),
			@JoinColumn(name = "TENANT_ID", referencedColumnName = "TENANT_ID", insertable = false, updatable = false) })
	private EmsDashboard dashboardSet;

	public EmsSubDashboard()
	{
		deleted = Boolean.FALSE;
	}

	public EmsSubDashboard(BigInteger dashboardSetId, BigInteger subDashboardId, int position)
	{
		this();
		this.dashboardSetId = dashboardSetId;
		this.subDashboardId = subDashboardId;
		this.position = position;
	}

	public EmsDashboard getDashboardSet()
	{
		return dashboardSet;
	}

	public BigInteger getDashboardSetId()
	{
		return dashboardSetId;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted()
	{
		return deleted;
	}

	public Integer getPosition()
	{
		return position;
	}

	public BigInteger getSubDashboardId()
	{
		return subDashboardId;
	}

	public void setDashboardSet(EmsDashboard dashboardSet)
	{
		this.dashboardSet = dashboardSet;
	}

	public void setDashboardSetId(BigInteger dashboardSetId)
	{
		this.dashboardSetId = dashboardSetId;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}

	public void setPosition(Integer position)
	{
		this.position = position;
	}

	public void setSubDashboardId(BigInteger subDashboardId)
	{
		this.subDashboardId = subDashboardId;
	}
}
