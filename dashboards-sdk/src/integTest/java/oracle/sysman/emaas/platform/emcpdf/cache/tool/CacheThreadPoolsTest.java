package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class CacheThreadPoolsTest {

    @Test
    public void testThreadTest(){
        CacheThreadPools.getThreadPool();
    }
}
