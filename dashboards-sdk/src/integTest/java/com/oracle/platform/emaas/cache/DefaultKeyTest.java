package com.oracle.platform.emaas.cache;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by chehao on 2016/12/14.
 */
public class DefaultKeyTest {
    @Test
    public void testEquals()
    {
        DefaultKey dk1 = new DefaultKey(null, "test");
        DefaultKey dk2 = new DefaultKey(null, "test");
        Assert.assertEquals(dk1, dk2);
        DefaultKey dk3 = new DefaultKey(new Tenant("tenant"), "test");
        Assert.assertNotEquals(dk1, dk3);
    }

    @Test
    public void testHashCode()
    {
        DefaultKey dk1 = new DefaultKey(null, "test");
        DefaultKey dk2 = new DefaultKey(null, "test");
        Assert.assertEquals(dk1.hashCode(), dk2.hashCode());

        DefaultKey dk3 = new DefaultKey(new Tenant("tenant"), "test");
        Assert.assertNotEquals(dk1.hashCode(), dk3.hashCode());

        DefaultKey dk4 = new DefaultKey(new Tenant("t"), "test");
        DefaultKey dk5 = new DefaultKey(new Tenant("t"), "test");
        Assert.assertEquals(dk4.hashCode(), dk5.hashCode());
    }
}
