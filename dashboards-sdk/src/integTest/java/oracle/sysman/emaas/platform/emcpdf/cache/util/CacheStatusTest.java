package oracle.sysman.emaas.platform.emcpdf.cache.util;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class CacheStatusTest {

    @Test
    public void testCacheStatus(){
        CacheStatus cs;
        cs=CacheStatus.AVAILABLE;
        cs=CacheStatus.CLOSED;
        cs=CacheStatus.SUSPEND;
    }
}
