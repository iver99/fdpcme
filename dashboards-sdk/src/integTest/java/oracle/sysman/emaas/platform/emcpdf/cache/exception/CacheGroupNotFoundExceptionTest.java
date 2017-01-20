package oracle.sysman.emaas.platform.emcpdf.cache.exception;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class CacheGroupNotFoundExceptionTest {
    @Test(expectedExceptions = CacheGroupNotFoundException.class)
    public void testException() throws CacheException {
        throw new CacheGroupNotFoundException();

    }
}
