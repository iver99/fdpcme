package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by chehao on 4/26/2017 1:43 PM.
 */
@Test(groups = { "s2" })
public class RestClientProxyTest {

    @Test
    public void testRestProxy(){
        Assert.assertNotNull(RestClientProxy.getRestClient());
    }
}
