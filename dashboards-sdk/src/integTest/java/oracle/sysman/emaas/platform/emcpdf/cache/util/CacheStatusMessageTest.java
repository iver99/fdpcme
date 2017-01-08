package oracle.sysman.emaas.platform.emcpdf.cache.util;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class CacheStatusMessageTest {

    @Test
    public void testCacheStatusMsg(){
        CacheStatusMessage msg=new CacheStatusMessage(CacheStatus.AVAILABLE,"msg");
        msg.getCacheStatus();
        msg.getMsg();
        msg.setCacheStatus(CacheStatus.CLOSED);
        msg.setMsg("testMsg");
    }
}
