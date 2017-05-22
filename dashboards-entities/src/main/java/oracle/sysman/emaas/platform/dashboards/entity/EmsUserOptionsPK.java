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
	private Long tenantId;

	private static final long serialVersionUID = 8346753118084614627L;

	public EmsUserOptionsPK(String userName, BigInteger dashboardId, Long tenantId)
	{
		this.userName = userName;
		this.dashboardId = dashboardId;
		this.tenantId = tenantId;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof EmsUserOptionsPK) {
			final EmsUserOptionsPK otherEmsUserOptions = (EmsUserOptionsPK) other;
			final boolean isEqual = otherEmsUserOptions.dashboardId.equals(dashboardId)
					&& otherEmsUserOptions.userName.equals(userName)
					&& otherEmsUserOptions.tenantId.equals(tenantId);
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
