package com.oracle.platform.emaas.cache;

import com.oracle.platform.emaas.cache.util.TimeUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by chehao on 2016/12/14.
 */
public class TimeUtilTest {
    @Test(groups = { "s2" })
    public void testToSec(){
        Assert.assertEquals(TimeUtil.toSecs(1000), 1);
    }
    @Test(groups = { "s2" })
    public void testConvertTimeToInt(){
        Assert.assertEquals(TimeUtil.convertTimeToInt(2147483648L), 2147483647L);
        Assert.assertEquals(TimeUtil.convertTimeToInt(2147483646L), 2147483646);
    }
    @Test(groups = { "s2" })
    public void testToMillis(){
        Assert.assertEquals(TimeUtil.toMillis(1),1000L);
    }
}
