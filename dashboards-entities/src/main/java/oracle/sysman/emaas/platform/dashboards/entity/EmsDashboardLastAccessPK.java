package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

public class EmsDashboardLastAccessPK implements Serializable
{
	private static final long serialVersionUID = 6378570578531917471L;

	//private String tenantId;
	private String accessedBy;
	private Long dashboardId;

	public EmsDashboardLastAccessPK()
	{
	}

	public EmsDashboardLastAccessPK(String accessedBy, Long dashboardId)
	{
		this.accessedBy = accessedBy;
		this.dashboardId = dashboardId;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof EmsDashboardLastAccessPK) {
			final EmsDashboardLastAccessPK otherEmsDashboardLastAccessPK = (EmsDashboardLastAccessPK) other;
			final boolean areEqual = otherEmsDashboardLastAccessPK.accessedBy.equals(accessedBy)
					&& otherEmsDashboardLastAccessPK.dashboardId.equals(dashboardId);
			return areEqual;
		}
		return false;
	}

	public String getAccessedBy()
	{
		return accessedBy;
	}

	public Long getDashboardId()
	{
		return dashboardId;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	public void setAccessedBy(String accessedBy)
	{
		this.accessedBy = accessedBy;
	}

	public void setDashboardId(Long dashboardId)
	{
		this.dashboardId = dashboardId;
	}
}
