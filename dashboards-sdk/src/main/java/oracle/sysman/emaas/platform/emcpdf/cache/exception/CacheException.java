package oracle.sysman.emaas.platform.emcpdf.cache.exception;

/**
 * Created by chehao on 2016/12/15.
 */
public class CacheException extends Exception{
    private final Integer errorCode;

    public CacheException(Integer errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }

    public CacheException(Integer errorCode, String message, Throwable t)
    {
        super(message, t);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode()
    {
        return errorCode;
    }
}
