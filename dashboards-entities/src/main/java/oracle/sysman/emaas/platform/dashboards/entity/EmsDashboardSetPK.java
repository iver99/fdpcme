package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author jishshi
 * @since 2016/3/3.
 */
public class EmsDashboardSetPK implements Serializable
{
	private static final long serialVersionUID = 6345975239287337011L;

	private BigInteger dashboardSetId;

	private BigInteger subDashboardId;

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof EmsDashboardSetPK) {
			final EmsDashboardSetPK otherEmsDashboardSetPK = (EmsDashboardSetPK) other;
			final boolean isEqual = otherEmsDashboardSetPK.dashboardSetId.equals(dashboardSetId)
					&& otherEmsDashboardSetPK.subDashboardId.equals(subDashboardId);
			return isEqual;
		}
		return false;
	}

	public BigInteger getDashboardSetId()
	{
		return dashboardSetId;
	}

	public BigInteger getSubDashboardId()
	{
		return subDashboardId;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	public void setDashboardSetId(BigInteger dashboardSetId)
	{
		this.dashboardSetId = dashboardSetId;
	}

	public void setSubDashboardId(BigInteger subDashboardId)
	{
		this.subDashboardId = subDashboardId;
	}
}
