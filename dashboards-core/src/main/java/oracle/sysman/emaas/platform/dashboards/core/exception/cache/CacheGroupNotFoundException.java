package oracle.sysman.emaas.platform.dashboards.core.exception.cache;

import oracle.sysman.emaas.platform.dashboards.core.DashboardErrorConstants;
import oracle.sysman.emaas.platform.dashboards.core.exception.CacheException;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;

/**
 * Created by chehao on 2016/11/14.
 */
public class CacheGroupNotFoundException extends CacheException{

    private static final String DASHBOARD_CACHE_GROUP_NOT_FOUND_ERROR = "DASHBOARD_CACHE_GROUP_NOT_FOUND_ERROR";

    public CacheGroupNotFoundException()
    {
        super(DashboardErrorConstants.DASHBOARD_CACHE_GROUP_NOT_FOUND_ERROR_CODE, MessageUtils
                .getDefaultBundleString(DASHBOARD_CACHE_GROUP_NOT_FOUND_ERROR));
    }
}
