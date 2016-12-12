package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

public class EmsDashboardTileParamsPK implements Serializable
{
	private static final long serialVersionUID = 7310567904803274617L;

	private String paramName;
	private String dashboardTile;

	public EmsDashboardTileParamsPK()
	{
	}

	public EmsDashboardTileParamsPK(String paramName, String emsDashboardTile)
	{
		this.paramName = paramName;
		dashboardTile = emsDashboardTile;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof EmsDashboardTileParamsPK) {
			final EmsDashboardTileParamsPK otherEmsDashboardTileParamsPK = (EmsDashboardTileParamsPK) other;
			final boolean areEqual = otherEmsDashboardTileParamsPK.paramName.equals(paramName)
					&& otherEmsDashboardTileParamsPK.dashboardTile.equals(dashboardTile);
			return areEqual;
		}
		return false;
	}

	public String getDashboardTile()
	{
		return dashboardTile;
	}

	public String getParamName()
	{
		return paramName;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	public void setDashboardTile(String emsDashboardTile)
	{
		dashboardTile = emsDashboardTile;
	}

	public void setParamName(String paramName)
	{
		this.paramName = paramName;
	}
}
