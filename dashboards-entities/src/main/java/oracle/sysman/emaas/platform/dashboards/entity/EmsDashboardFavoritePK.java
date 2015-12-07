package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

public class EmsDashboardFavoritePK implements Serializable
{
	private static final long serialVersionUID = 7034841936424550167L;

	//private String tenantId;
	private String userName;
	private Long dashboard;

	public EmsDashboardFavoritePK()
	{
	}

	public EmsDashboardFavoritePK(String userName, Long emsDashboard)
	{
		this.userName = userName;
		dashboard = emsDashboard;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof EmsDashboardFavoritePK) {
			final EmsDashboardFavoritePK otherEmsDashboardFavoritePK = (EmsDashboardFavoritePK) other;
			final boolean areEqual = otherEmsDashboardFavoritePK.userName.equals(userName)
					&& otherEmsDashboardFavoritePK.dashboard.equals(dashboard);
			return areEqual;
		}
		return false;
	}

	public Long getDashboard()
	{
		return dashboard;
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

	public void setDashboard(Long emsDashboard)
	{
		dashboard = emsDashboard;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
