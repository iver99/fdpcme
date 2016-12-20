package com.oracle.platform.emaas.cache;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2016/12/14.
 */
public class BinaryTest {
    @Test(groups = { "s2" })
    public void testBinary(){
        byte[] b=new byte[15];
        Binary binary=new Binary(b);
        binary.getData();
        binary.setData(b);
    }
}
