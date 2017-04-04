package oracle.sysman.emaas.platform.emcpdf.cache.exception;

import java.security.PrivilegedActionException;

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

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public CacheException(Throwable cause, Integer errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }
}
