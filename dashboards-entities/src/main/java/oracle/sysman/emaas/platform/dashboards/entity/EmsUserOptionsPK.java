package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author jishshi
 * @since 2/1/2016.
 */
public class EmsUserOptionsPK implements Serializable
{

	private String userName;
	private BigInteger dashboardId;

	private static final long serialVersionUID = 8346753118084614627L;

	public EmsUserOptionsPK(String userName, BigInteger dashboardId)
	{
		this.userName = userName;
		this.dashboardId = dashboardId;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof EmsUserOptions) {
			final EmsUserOptions otherEmsUserOptions = (EmsUserOptions) other;
			final boolean isEqual = otherEmsUserOptions.dashboardId.equals(dashboardId)
					&& otherEmsUserOptions.userName.equals(userName);
			return isEqual;
		}
		return false;
	}

	public BigInteger getDashboardId()
	{
		return dashboardId;
	}

	public String getUserName()
	{
		return userName;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	public void setDashboardId(BigInteger dashboardId)
	{
		this.dashboardId = dashboardId;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
