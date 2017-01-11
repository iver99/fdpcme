package oracle.sysman.emaas.platform.dashboards.comparator.webutils.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class StringUtilTest {

    @Test
    public void testStringUtil(){
        Assert.assertTrue(StringUtil.isEmpty(""));

        Assert.assertFalse(StringUtil.isEmpty("string"));
    }
}
