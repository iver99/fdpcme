package oracle.sysman.emaas.platform.emcpdf.cache.exception;

import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheErrorConstants;

/**
 * Created by chehao on 2016/12/28.
 */
public class ExecutionException extends CacheException{
    private static final String CACHE_EXECUTION_ERROR = "CACHE_EXECUTION_ERROR";

    public ExecutionException()
    {
        super(CacheErrorConstants.CACHE_EXECUTION_ERROR_CODE, CACHE_EXECUTION_ERROR);
    }

    public ExecutionException(Throwable cause) {
        super(cause, CacheErrorConstants.CACHE_EXECUTION_ERROR_CODE);
    }
}
