package oracle.sysman.emaas.platform.emcpdf.cache.support;

import oracle.sysman.emaas.platform.emcpdf.cache.api.ICacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class CacheManagersTest {

    @Test
    public void testCacheManagers(){
        ICacheManager cm1=CacheManagers.getInstance().build();
        Assert.assertNotNull(cm1);
    }
}
