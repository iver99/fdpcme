package oracle.sysman.emaas.platform.dashboards.core.model;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

/**
 * @author jishshi
 * @since 2/2/2016.
 */
public class UserOptions {
    private String userName;
    private Long dashboardId;
    private Long autoRefreshInterval;
    private String extendedOptions;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long id) {
        this.dashboardId = id;
    }

    public Long getAutoRefreshInterval() {
        return autoRefreshInterval;
    }

    public void setAutoRefreshInterval(Long autoRefreshInterval) {
        this.autoRefreshInterval = autoRefreshInterval;
    }

    public String getExtendedOptions() {
        return extendedOptions;
    }

    public void setExtendedOptions(String extendedOptions) {
        this.extendedOptions = extendedOptions;
    }

    public static UserOptions valueOf(EmsUserOptions emsUserOptions) {
    	return UserOptions.valueOf(emsUserOptions, null);
    }

    protected static UserOptions valueOf(EmsUserOptions emsUserOptions, UserOptions userOptions) {
        if (emsUserOptions == null) {
            return null;
        }
        if (userOptions == null) {
        	userOptions = new UserOptions();
        }
        userOptions.setAutoRefreshInterval(Long.valueOf(emsUserOptions.getAutoRefreshInterval()));
        userOptions.setUserName(emsUserOptions.getUserName());
        userOptions.setDashboardId(Long.valueOf(emsUserOptions.getDashboardId()));
        userOptions.setExtendedOptions(emsUserOptions.getExtendedOptions());
        return userOptions;
    }

    public EmsUserOptions toEntity(EmsUserOptions emsUserOptions, String userName) throws DashboardException {
        EmsUserOptions euo = emsUserOptions;
        if (emsUserOptions == null) {
            euo = new EmsUserOptions();
        }

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
