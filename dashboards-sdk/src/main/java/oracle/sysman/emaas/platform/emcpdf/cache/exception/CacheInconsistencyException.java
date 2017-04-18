package oracle.sysman.emaas.platform.emcpdf.cache.exception;

import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheErrorConstants;

/**
 * Created by chehao on 4/4/17.
 */
public class CacheInconsistencyException extends CacheException {

    private static final String CACHE_INCONSISTENCY_ERROR = "CACHE_INCONSISTENCY_ERROR";

    public CacheInconsistencyException()
    {
        super(CacheErrorConstants.CACHE_INCONSISTENCY_ERROR_CODE, CACHE_INCONSISTENCY_ERROR);
    }
}

