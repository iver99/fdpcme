package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

public class EmsDashboardTileParamsPK implements Serializable
{
	private static final long serialVersionUID = 7310567904803274617L;

	private String paramName;
	private EmsDashboardTilePK dashboardTile;
	private Long tenantId;

	public EmsDashboardTileParamsPK()
	{
	}

	public EmsDashboardTileParamsPK(String paramName, EmsDashboardTilePK emsDashboardTile, Long tenantId)
	{
		this.paramName = paramName;
		this.dashboardTile = emsDashboardTile;
		this.tenantId = tenantId;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof EmsDashboardTileParamsPK) {
			final EmsDashboardTileParamsPK otherEmsDashboardTileParamsPK = (EmsDashboardTileParamsPK) other;
			final boolean areEqual = otherEmsDashboardTileParamsPK.paramName.equals(paramName)
					&& otherEmsDashboardTileParamsPK.dashboardTile.equals(dashboardTile)
					&& otherEmsDashboardTileParamsPK.tenantId.equals(tenantId);
			return areEqual;
		}
		return false;
	}

	public EmsDashboardTilePK getDashboardTile()
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

	public void setDashboardTile(EmsDashboardTilePK emsDashboardTile)
	{
		dashboardTile = emsDashboardTile;
	}

	public void setParamName(String paramName)
	{
		this.paramName = paramName;
	}

    /**
     * @return the tenantId
     */
    public Long getTenantId()
    {
        return tenantId;
    }

    /**
     * @param tenantId the tenantId to set
     */
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }
}
