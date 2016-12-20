package oracle.sysman.emaas.platform.emcpdf.cache.exception;

import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheErrorConstants;

/**
 * Created by chehao on 2016/12/15.
 */
public class CacheGroupNameEmptyException extends CacheException {

    private static final String DASHBOARD_CACHE_GROUP_NAME_EMPTY_ERROR = "DASHBOARD_CACHE_GROUP_NAME_EMPTY_ERROR";

    public CacheGroupNameEmptyException()
    {
        super(CacheErrorConstants.DASHBOARD_CACHE_GROUP_NAME_EMPTY_ERROR_CODE, DASHBOARD_CACHE_GROUP_NAME_EMPTY_ERROR);
    }
}
