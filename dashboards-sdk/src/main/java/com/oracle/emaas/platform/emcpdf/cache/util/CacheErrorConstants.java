package com.oracle.emaas.platform.emcpdf.cache.util;

/**
 * Created by chehao on 2016/12/15.
 */
public class CacheErrorConstants {

    public static final Integer DASHBOARD_CACHE_ERROR_CODE=50000;
    public static final Integer DASHBOARD_CACHE_GROUP_NOT_FOUND_ERROR_CODE=50001;
    public static final Integer DASHBOARD_CACHE_GROUP_NAME_EMPTY_ERROR_CODE=50002;
    // important: don't assign value larger than this value to dashboard cache errors
    public static final Integer DASHBOARD_CACHE_MAX_ERROR_CODE=59999;
}
