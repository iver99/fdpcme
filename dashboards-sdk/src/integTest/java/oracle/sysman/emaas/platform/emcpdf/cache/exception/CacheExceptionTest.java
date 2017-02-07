package oracle.sysman.emaas.platform.emcpdf.cache.exception;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class CacheExceptionTest {

    @Test(expectedExceptions = CacheException.class)
    public void testException() throws CacheException {
        throw new CacheException(1,"testException");

    }

    @Test(expectedExceptions = CacheException.class)
    public void testException2() throws CacheException {

        throw new CacheException(1,"testException",new Throwable());
    }

    @Test(expectedExceptions = CacheException.class)
    public void testException3() throws CacheException {

        throw new CacheException(new Throwable(),1);
    }
}
