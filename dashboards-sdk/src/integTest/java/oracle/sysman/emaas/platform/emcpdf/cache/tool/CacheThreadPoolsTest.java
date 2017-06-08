package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class CacheThreadPoolsTest {

    @Test
    public void testThreadTest(){
        ScheduledExecutorService pool =CacheThreadPools.getThreadPool();
        Assert.assertNotNull(pool);
    }
}
