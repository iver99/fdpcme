package oracle.sysman.emaas.platform.dashboards.core.model;

import java.math.BigInteger;
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

/**
 * @author jishshi
 * @since 2/2/2016.
 */
public class UserOptions
{
	public static UserOptions valueOf(EmsUserOptions emsUserOptions)
	{
		if (emsUserOptions == null) {
			return null;
		}
		UserOptions userOptions = new UserOptions();
		userOptions.setAutoRefreshInterval(Long.valueOf(emsUserOptions.getAutoRefreshInterval()));
		userOptions.setUserName(emsUserOptions.getUserName());
		userOptions.setDashboardId(emsUserOptions.getDashboardId());
		userOptions.setExtendedOptions(emsUserOptions.getExtendedOptions());
		return userOptions;
	}

	private String userName;
	private BigInteger dashboardId;
	private Long autoRefreshInterval;

	private String extendedOptions;

	public Long getAutoRefreshInterval()
	{
		return autoRefreshInterval;
	}

	public BigInteger getDashboardId()
	{
		return dashboardId;
	}

	public String getExtendedOptions()
	{
		return extendedOptions;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setAutoRefreshInterval(Long autoRefreshInterval)
	{
		this.autoRefreshInterval = autoRefreshInterval;
	}

	public void setDashboardId(BigInteger id)
	{
		dashboardId = id;
	}

	public void setExtendedOptions(String extendedOptions)
	{
		this.extendedOptions = extendedOptions;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public EmsUserOptions toEntity(EmsUserOptions emsUserOptions, String userName) throws DashboardException
	{
		EmsUserOptions euo;
		Date now = DateUtil.getGatewayTime();
		if (emsUserOptions == null) {
			euo = new EmsUserOptions();
			euo.setCreationDate(now);
		} else {
			euo = emsUserOptions;
		}
		euo.setLastModificationDate(now);

		if (dashboardId == null) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.USER_OPTIONS_INVALID_DASHBOARD_ID));
		}

		if (autoRefreshInterval == null) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.USER_OPTIONS_INVALID_AUTO_REFRESH_INTERVAL));
		}

		euo.setAutoRefreshInterval(autoRefreshInterval);
		euo.setDashboardId(dashboardId);
		euo.setUserName(userName);
		euo.setExtendedOptions(extendedOptions);

		return euo;
	}
}
